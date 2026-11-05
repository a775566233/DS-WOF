# 冰翼龙的环境弱点：地狱掉血、禁用冰息与炎热减伤

适用于本工程的 Dragon Survival **2.0.71 / Minecraft 1.21.1**。以下是接入示例，没有自动修改当前龙种、冰息或 Java 实现。

三种行为可以复用 DS 机制，但入口不同：

| 需求 | 推荐入口 | 效果 |
| --- | --- | --- |
| 在地狱持续掉血 | `dragon_penalty` 的 `take_damage` | 对该龙种定期提交伤害 |
| 在地狱无法使用冰息 | 冰息顶层 `usage_blocked` | 自动禁用该技能；持续施放中条件成立也会停止施放 |
| 炎热环境中降低技能伤害 | `dragon_penalty` 的 `modifier` | 条件成立时修改技能伤害属性 |
| 只降低冰息伤害 | 冰息自定义效果中的环境判断，或专属属性 | 避免影响其他技能 |

## 1. 在地狱掉血

创建 `src/main/resources/data/dswof/dragonsurvival/dragon_penalty/nether_heat.json`：

```json
{
  "condition": {
    "condition": "minecraft:entity_properties",
    "entity": "this",
    "predicate": {
      "location": {
        "dimension": "minecraft:the_nether"
      }
    }
  },
  "effect": {
    "penalty_type": "dragonsurvival:take_damage",
    "damage_type": "minecraft:generic",
    "amount": 1.0
  },
  "trigger": {
    "penalty_trigger": "dragonsurvival:instant",
    "trigger_rate": 20
  }
}
```

含义：处于地狱时，每 20 tick（20 TPS 下约 1 秒）提交 1 点伤害。`instant` 表示不等待弱点抗性储备耗尽，不是只执行一次；检测按世界时间取模，因此进入后的第一次触发不保证刚好等待 1 秒。实际扣血仍经过伤害处理。

示例用 `minecraft:generic`，因此不能据此推断火焰抗性会免疫。若需要自定义死亡信息、伤害标签和免疫规则，可改为 `dswof:heat_exposure` 并创建 `data/dswof/damage_type/heat_exposure.json`；是否受护甲、火焰抗性等影响取决于伤害类型的标签与处理逻辑，名称含有 heat 不会自动使其成为火焰伤害。

在 [ice_wings_dragon.json](../src/main/resources/data/dswof/dragonsurvival/dragon_species/ice_wings_dragon.json) 的 `penalties` 中引用它：

```json
{
  "penalties": [
    "dswof:nether_heat"
  ]
}
```

上面仅展示要合并的字段，不是完整龙种文件。保留现有其他字段和弱点 ID。

如果希望进入地狱后有一段耐热时间，可把触发器换成 `dragonsurvival:supply`，设置独立的 `supply_type`、消耗和恢复速率。完整参数见[弱点手册](datapacks/04-penalties.md)。

## 2. 在地狱禁用冰息

在 [ice_breath.json](../src/main/resources/data/dswof/dragonsurvival/dragon_ability/ice_breath.json) 的顶层添加以下字段，与 `activation`、`actions`、`icon` 平级：

```json
{
  "usage_blocked": {
    "condition": "minecraft:entity_properties",
    "entity": "this",
    "predicate": {
      "location": {
        "dimension": "minecraft:the_nether"
      }
    }
  }
}
```

DS 在服务端的技能 tick 中检查该条件，成立时自动禁用并停止正在进行的施放；离开后解除自动禁用，之后可重新施放，仍受其他禁用状态和冷却约束。不要把 `target_conditions` 写成地狱限制：它负责筛选受影响目标，不能代替施法者的使用限制。

技能禁用直接检查维度，不需要等待掉血弱点触发，也不会禁用其他技能。若只想禁止开始施放、允许已经开始的施放继续，则这套 `usage_blocked` 行为不符合该需求，需要另写逻辑。

## 3. 先定义什么是“炎热环境”

DS 这一版没有可以直接填写任意温度阈值的通用 `temperature` 条件。可以先采用可控的生物群系标签，明确哪些地方算热；它不是体温模拟，也不会自动感知附近火把、熔炉或其他模组的温度系统。

创建 `src/main/resources/data/dswof/tags/worldgen/biome/hot_environment.json`：

```json
{
  "replace": false,
  "values": [
    "minecraft:desert",
    "minecraft:badlands",
    "minecraft:eroded_badlands",
    "minecraft:wooded_badlands",
    "minecraft:savanna",
    "minecraft:savanna_plateau",
    "minecraft:windswept_savanna"
  ]
}
```

再创建 `src/main/resources/data/dswof/predicate/in_hot_environment.json`，集中保存条件：

```json
{
  "condition": "minecraft:entity_properties",
  "entity": "this",
  "predicate": {
    "location": {
      "biomes": "#dswof:hot_environment"
    }
  }
}
```

这里判定的是施法者所在生物群系。示例标签没有加入地狱生物群系，因为地狱已由上一节单独禁用冰息；其他模组群系需要加入标签才会匹配。主世界的炎热群系位于地下、夜间或雨中时仍匹配这个定义。

如果希望按实际生物群系温度值、昼夜、附近岩浆或第三方温度模组判断，应明确规则后扩展 Java 条件；不要将光照强度直接当温度。

## 4. 纯 JSON 路线：炎热时通用技能伤害减半

创建 `src/main/resources/data/dswof/dragonsurvival/dragon_penalty/hot_ability_damage.json`：

```json
{
  "condition": {
    "condition": "minecraft:reference",
    "name": "dswof:in_hot_environment"
  },
  "effect": {
    "penalty_type": "dragonsurvival:modifier",
    "modifiers": [
      {
        "base": {
          "id": "dswof:hot_ability_damage",
          "duration": 40,
          "should_remove_automatically": true,
          "early_removal_condition": {
            "condition": "minecraft:inverted",
            "term": {
              "condition": "minecraft:reference",
              "name": "dswof:in_hot_environment"
            }
          }
        },
        "modifiers": [
          {
            "attribute": "dragonsurvival:dragon_ability_damage",
            "amount": -0.5,
            "operation": "add_multiplied_total"
          }
        ]
      }
    ]
  },
  "trigger": {
    "penalty_trigger": "dragonsurvival:instant",
    "trigger_rate": 20
  }
}
```

把 `dswof:hot_ability_damage` 也加入龙种的 `penalties`，最终示例列表为：

```json
{
  "penalties": [
    "dswof:nether_heat",
    "dswof:hot_ability_damage"
  ]
}
```

关键参数：

- `add_multiplied_total` 配合 `amount = -0.5` 表示乘 `1 - 0.5 = 0.5`。不要用 `add_value = -0.5`，后者只是扣固定属性值。
- 每 20 tick 刷新一个持续 40 tick 的修饰；同一个实例 ID 被刷新，不会每秒叠加一次减半。进入炎热群系后通常在下一个检查点生效。
- 离开热环境时，`early_removal_condition` 在持续实例的服务端 tick 中触发移除；有限时长也避免把修饰永久保留。`should_remove_automatically` 还参与来源状态检查。
- 当前冰息默认读取 `dragon_ability_damage` 作为表达式中的 `scale`，所以该属性减半会让整个表达式结果减半，包括接触时间增长部分和公式上限。

**此方案影响所有使用 `dragon_ability_damage` 的技能。** 当前冰翼龙只有冰息时可以满足需求；以后添加其他读取该属性的伤害技能，它们也会一起削弱。普通攻击属性不会因此直接减半；独立计算伤害的附加状态也不保证减半。

不要用 `damage_modification` 来代替这里的属性修饰：这一版 `DamageModifications.reduceDamage` 读取的是受伤实体的附件，控制的是持有者受到的伤害，不是其对外造成的冰息伤害。

## 5. 只让冰息减半：适合当前自定义效果的写法

如果要严格只削弱冰息，可在 [IceBreathAbilityEntityEffect.java](../src/main/java/org/wgx/dswof/dragonsurvival/dragon_ability/IceBreathAbilityEntityEffect.java) 中读取同一生物群系标签。它不需要另外定时维护冷热状态，每次命中时根据施法者位置计算。

需要的 import：

```java
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
```

在类内声明：

```java
private static final TagKey<Biome> HOT_ENVIRONMENT = TagKey.create(
        Registries.BIOME,
        ResourceLocation.fromNamespaceAndPath("dswof", "hot_environment")
);
```

在 `apply` 中算完表达式、调用 `target.hurt(...)` 前增加：

```java
if (dragon.serverLevel().getBiome(dragon.blockPosition()).is(HOT_ENVIRONMENT)) {
    damage *= 0.5F;
}
```

该方案继续复用第 1 节的掉血弱点、第 2 节的技能禁用，以及第 3 节的标签；不再配置第 4 节的通用伤害减半弱点，以免重复乘 0.5。

这里检查 `dragon`，而不是 `target`，含义是“施法者处于热环境中时吐息变弱”。与现有冻结免疫减伤相乘后：

```text
提交伤害 = 表达式结果 × 环境倍率 × 目标冻结免疫倍率
```

炎热环境中对可冻结目标是 50%，对冻结免疫目标是 25%。如果希望两项减伤同时存在时仍只降到 50%，应改为合并条件后统一乘一次，而不是两个独立乘法。

`dswof:frost` 是冰息动作中的另一项效果；上面的改法只调整直接伤害，不自动调整 Frost 的施加概率、等级或持续时间。

另一种做法是注册 `dswof:ice_breath_multiplier` 专属属性，由弱点修改它，在冰息中额外乘该属性。若直接把技能 `scale` 改成专属属性，便替换了默认 DS 伤害属性来源；需要同时保留两者时，应在自定义效果中显式相乘。

## 6. 执行边界与验证

DS 的 `ENABLE_PENALTIES` 服务端选项可以关闭弱点执行；默认 tick 弱点也跳过创造和旁观玩家。技能 `usage_blocked` 的维度限制是独立检查，因此关闭弱点不会自动解除它。第 5 节直接写在冰息里的环境倍率同样不会自动跟随弱点开关，若希望统一受开关控制，应额外检查该配置。

建议在生存模式分别测试：地狱、普通温带群系、标签中的炎热群系；在两类群系边界切换，并测试开始施放和施放过程中切换环境。纯 JSON 路线可用 `/attribute @s dragonsurvival:dragon_ability_damage get` 查看倍率变化。

以上配置已按本地 DS 源码的 Codec 与执行流程静态核对，尚未进行游戏加载和实测。核心源码位于本地 `build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/` 下：

- `registry/dragon/penalty/DragonPenalty.java`、`DamagePenalty.java`、`InstantTrigger.java`、`ModifierPenalty.java`。
- `registry/dragon/ability/DragonAbilityInstance.java` 中的 `tick`。
- `common/codecs/duration_instance/DurationInstanceBase.java`、`DurationInstance.java`。
- `registry/attachments/DamageModifications.java` 中的 `reduceDamage`。
- `common/handlers/DragonPenaltyHandler.java`。

全部弱点字段另见[弱点参数手册](datapacks/04-penalties.md)，技能禁用条件见[技能 JSON 参数手册](06-dragon-ability-json-reference.md)。
