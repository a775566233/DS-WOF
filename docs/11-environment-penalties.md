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

如果希望进入地狱后有一段耐热时间，可把触发器换成 `dragonsurvival:supply`，设置独立的 `supply_type`、消耗和恢复速率。与原版被动升级对接的完整示例见[第 7 节](#7-对接原版被动升级等级越高耐热越久)，全部参数见[弱点手册](datapacks/04-penalties.md)。

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

若采用冰息专属属性方案，详见[属性注册与冰息专属倍率](12-attributes-and-ice-breath-multiplier.md)。该方案让弱点修改 `dswof:ice_breath_multiplier`，并让冰息同时读取通用技能伤害与专属倍率；本节保留通用属性方案作为对照。

创建 `../src/main/resources/data/dswof/dragonsurvival/dragon_penalty/hot_ice_breath.json`：

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

## 7. 对接原版被动升级：等级越高，耐热越久

可以纯 JSON 实现。原版 `dragonsurvival:contrast_shower` 就是这种设计：被动技能共 8 级，每级给 `dragonsurvival:penalty_resistance_time` 增加 200 点；`dragonsurvival:hydration` 则是 7 级，每级增加 1200 点。两者都用 `experience_points` 升级，升到第 L 级花费 `15L` 经验点。

这里新增一个 4 级的 `dswof:heat_resistance` 被动，沿用这条执行链：

```text
玩家在原版技能界面升级耐热被动
→ 被动给 penalty_resistance_time 增加属性修饰
→ 地狱弱点读取该属性，更新耐热储备上限
→ 在地狱每 tick 消耗储备，耗尽后周期性提交伤害
→ 离开地狱时恢复储备
```

### 7.1 将地狱弱点改为消耗储备

第 1 节的 `instant` 不读取耐受时间属性，升级被动也不会延后掉血。将 `data/dswof/dragonsurvival/dragon_penalty/nether_heat.json` 改为以下配置：

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
    "penalty_trigger": "dragonsurvival:supply",
    "supply_type": "dswof:heat_supply",
    "attribute": "dragonsurvival:penalty_resistance_time",
    "trigger_rate": 20,
    "reduction_rate": 1.0,
    "regeneration_rate": 0.001
  }
}
```

`reduction_rate = 1` 表示每 tick 扣 1 点储备。`trigger_rate = 20` 表示耗尽后每 20 tick 检查并提交 1 点伤害，不会让储备变成每秒才扣一次。储备耗尽与首次伤害之间还可能相差一个检查间隔，实际扣血仍经过后续伤害处理。

### 7.2 新建可升级的耐热被动

保存到 `data/dswof/dragonsurvival/dragon_ability/heat_resistance.json`：

```json
{
  "activation": {
    "activation_type": "dragonsurvival:passive"
  },
  "upgrade": {
    "upgrade_type": "dragonsurvival:experience_points",
    "maximum_level": 4,
    "experience_cost": {
      "type": "minecraft:linear",
      "base": 15.0,
      "per_level_above_first": 15.0
    }
  },
  "actions": [
    {
      "target_selection": {
        "target_type": "dragonsurvival:self",
        "applied_effects": {
          "targeting_mode": "allies_and_self",
          "entity_effect": [
            {
              "effect_type": "dragonsurvival:modifier",
              "modifiers": [
                {
                  "base": {
                    "id": "dswof:heat_resistance",
                    "is_hidden": true,
                    "should_remove_automatically": true
                  },
                  "modifiers": [
                    {
                      "attribute": "dragonsurvival:penalty_resistance_time",
                      "amount": {
                        "type": "minecraft:linear",
                        "base": 200.0,
                        "per_level_above_first": 200.0
                      },
                      "operation": "add_value"
                    }
                  ]
                }
              ]
            }
          ]
        }
      }
    }
  ],
  "icon": {
    "texture_entries": [
      {
        "from_level": 0,
        "texture_resource": "dragonsurvival:abilities/cave/contrast_shower_1"
      }
    ]
  }
}
```

图标暂时复用原版 GUI sprite，所有等级显示同一张；制作自己的图标时再替换。技能名称和描述的语言键写法见[技能参数手册](06-dragon-ability-json-reference.md)。

`amount` 是当前等级对应的总加成：L1 为 200，L2 为 400，依此类推，不是把每次升级的数值累加成 200 + 400 + 600。未学习的 L0 不运行该被动。修饰实例使用固定 ID，沿用原版被动的刷新与自动移除规则，无需自己维护定时任务。

在龙种 `ice_wings_dragon.json` 中合并以下引用，保留现有其他技能和弱点：

```json
{
  "abilities": [
    "dswof:ice_breath",
    "dswof:heat_resistance"
  ],
  "penalties": [
    "dswof:nether_heat"
  ]
}
```

这是龙种字段片段，不是完整文件；若已配置 `dswof:hot_ability_damage`，也应继续保留它。

### 7.3 各等级能待多久

原版 `penalty_resistance_time` 默认值为 200，允许范围为 0～16384。没有其他属性修饰、储备初始为满、20 TPS 时，上述配置得到：

| 被动等级 L | 被动增加的储备 | 储备上限 | 储备耗尽所需时间 | 升到该级花费 |
| --- | --- | --- | --- | --- |
| 0，未学习 | 0 | 200 | 10 秒 | 0 |
| 1 | 200 | 400 | 20 秒 | 15 经验点 |
| 2 | 400 | 600 | 30 秒 | 30 经验点 |
| 3 | 600 | 800 | 40 秒 | 45 经验点 |
| 4 | 800 | 1000 | 50 秒 | 60 经验点 |

```text
储备上限 = 200 + 200 × L
满储备耐热秒数 = 储备上限 ÷ reduction_rate ÷ 20
```

从 L0 升满共花费 150 **经验点**，不是 150 个经验等级。`experience_points` 使用原版技能升级入口付费升级；不会因为在地狱停留而自动获得熟练度。若希望接触热环境积累熟练度，那需要另做升级条件与进度逻辑。

调数值时主要改被动 `amount` 的线性公式。比如 `base = 1200`、`per_level_above_first = 1200`，每级便增加 60 秒耐热时间。属性仍有上限，其他装备、被动与模组的修饰也可能改变最终结果。

### 7.4 离开恢复，以及升级时的剩余时间

**恢复按上限的比例计算。** 源码每 tick 执行 `当前储备 + 上限 × regeneration_rate`，并限制在上限内。示例的 `0.001` 是每 tick 恢复上限的 0.1%，理想情况下从空恢复到满约需 1000 tick，即 50 秒；升级后恢复的绝对点数变多，恢复满所需时间基本相同。

离开地狱会逐步恢复，再次进入时继续消耗剩余储备，不会因为重新进入就自动回满。首次初始化该储备池时才以满储备开始。

**升级改变上限时保留剩余百分比。** 例如上限 400、剩余 200（50%），升级到上限 600 后剩余变为 300（仍是 50%），随后继续进行本 tick 的消耗或恢复。升级不会直接补满；降级时也按比例调整。

### 7.5 影响范围与验证

`dswof:heat_supply` 让地狱耐热有独立的储备池。但 `dragonsurvival:penalty_resistance_time` 是通用属性：提高它，也会延长同一玩家其他使用该属性的 `supply` 弱点。若要只提高地狱耐热，应在 Java 注册并给玩家添加专属属性 `dswof:heat_resistance_time`，再把弱点与被动中的 `attribute` 同时换成它。仅换 `supply_type` 不能隔离上限加成。多个每 tick 执行的弱点也不要共用同一储备 ID，以免多次推进消耗。

耐热被动只延后掉血；第 2 节的地狱 `usage_blocked` 仍立即禁止冰息。只有希望“储备耗尽前也能使用冰息”时，才需要进一步改变技能禁用条件。

接入后可在生存模式使用 `/attribute @s dragonsurvival:penalty_resistance_time get` 检查学习前后的上限，再测试满储备入地狱、离开恢复、半储备升级和重新进入地狱。仍需游戏加载与实测；本节仅提供配置示例，没有修改工程中的技能、弱点和龙种文件。

本节依据实际依赖 JAR 的 `contrast_shower.json`、`hydration.json`，以及源码 `DSAttributes.java`、`SupplyTrigger.java`、`DragonPenalty.java`、`PenaltySupply.java`、`PassiveActivation.java`、`ExperiencePointsUpgrade.java` 核对。

## 8. 同一耐热升级能否缓解炎热群系的吐息减伤

可以，但应先确定升级改变的是“何时开始减伤”，还是“减伤幅度”。这两种设计的实现不同。

### 8.1 延后伤害减半：直接复用第 7 节的被动

把第 4 节 `hot_ice_breath.json` 的 `trigger` 替换为：

```json
{
  "trigger": {
    "penalty_trigger": "dragonsurvival:supply",
    "supply_type": "dswof:hot_breath_supply",
    "attribute": "dragonsurvival:penalty_resistance_time",
    "trigger_rate": 20,
    "reduction_rate": 1.0,
    "regeneration_rate": 0.001
  }
}
```

这是替换顶层字段的片段。保留原来的炎热群系 `condition`，以及第 4 节修改 `dragon_ability_damage` 的 `effect`，包括其 `duration = 40` 与离开热环境的 `early_removal_condition`。龙种继续同时引用 `dswof:heat_resistance` 被动和 `dswof:hot_ability_damage` 弱点。

新行为是：在炎热群系先消耗耐热储备；储备耗尽后才开始定期刷新伤害 ×0.5 的修饰。其他属性不变、满储备、20 TPS 时，L0～L4 分别约在 10、20、30、40、50 秒后进入减伤状态，另有触发检查间隔。储备还有剩余时不施加此弱点的减伤，耗尽后倍率仍为 50%。

`dswof:hot_breath_supply` 与地狱掉血的 `dswof:heat_supply` 是两个独立储备池，读取同一个上限属性，因此一次被动升级能延长两种耐受时间。当前热群系标签没有地狱群系，所以在地狱会消耗地狱储备、恢复热群系储备；在主世界的热群系则相反。它们不是共用的一条体温或耐热进度条。

不要把两个每 tick 弱点的 `supply_type` 都设为 `dswof:heat_supply` 来模拟共用体温：两者会各自推进、扣减或恢复同一份状态。若希望真正共用一份热量，需要集中维护状态，再由掉血和减伤分别读取它。

离开炎热群系时，现有的 `early_removal_condition` 移除减伤，储备开始恢复。若储备耗尽后原地升级，上限变化仍保留剩余百分比，0% 仍是 0%，因此不会立即解除减伤；停止刷新后，已经施加的有限时长修饰也需按其移除规则消失。

第 5 节 Java 中的 `damage *= 0.5F` 是命中时立即判断环境，与这里的储备触发器独立。选择本节方案时，不要同时保留该立即减半逻辑，否则仍会立刻减半，储备耗尽后还可能再乘一次 0.5。本节的属性路线依然会影响其他读取 `dragon_ability_damage` 的技能；要严格只控制冰息，需要在冰息逻辑中读取储备或使用专属倍率属性。

### 8.2 升级减轻减伤幅度：另外计算倍率

如果希望“即使一直处于炎热群系，高等级也只降低少量伤害”，可设计为：

```text
炎热环境倍率 = 0.5 + 0.5 × CLAMP(耐热被动等级, 0, 4) ÷ 4
提交伤害 = 原冰息计算结果 × 炎热环境倍率 × 冻结免疫倍率
```

| 耐热被动等级 | 炎热环境倍率 |
| --- | --- |
| 0 | 50% |
| 1 | 62.5% |
| 2 | 75% |
| 3 | 87.5% |
| 4 | 100% |

这仍可沿用第 7 节的付费升级界面与被动，但倍率需要在 Java 中另行读取。使用 `MagicData.getData(dragon).getAbility(耐热技能的ResourceKey)` 获取 `dswof:heat_resistance` 的实例，再读取其 `level()`；未获取到实例时按 0 级处理。若希望玩家禁用该被动后加成也失效，还应按技能实例的可用与激活状态判断。

不要直接使用当前冰息表达式的 `ability_level` 来代表耐热等级：它是冰息自身的等级。弱点 `ModifierPenalty` 也不会把耐热被动等级传进自己的 LV；该版本创建弱点修饰时按等级 1 处理。因此仅在第 4 节弱点的 `amount` 中填写线性 LV，不能自动关联另一个被动的等级。

也可以让耐热被动提供专属属性，再在冰息中读取它。建议明确选择延后触发或减轻幅度；若同时使用两者，应分别规定储备耗尽前后的倍率，避免重复补偿或重复减半。

## 9. 地狱伤害改为 in_fire 后，抗火是否免疫

**是。** 当前工程的 `nether_heat.json` 已将伤害类型设为 `minecraft:in_fire`。在原版 1.21.1 / 当前 NeoForge 的伤害流程中，它属于 `minecraft:is_fire` 伤害标签；`LivingEntity.hurt` 检测到火焰伤害且目标具有 `MobEffects.FIRE_RESISTANCE` 时直接返回 `false`。DS 的 `DamagePenalty` 调用普通的 `player.hurt(...)`，所以这项弱点伤害会被抗火效果免疫。

这里的关系是“伤害类型属于火焰标签”，而不是名称中含有 fire 就免疫。自定义 `dswof:heat_exposure` 不会因为叫热暴露就自动成为火焰伤害；想让抗火免疫它，需要将它加入 `data/minecraft/tags/damage_type/is_fire.json`，或自行实现对应规则。自定义伤害类型文件本身放在 `data/dswof/damage_type/heat_exposure.json`。

服用抗火后，各系统仍分别工作：

| 行为 | 只有抗火效果时 |
| --- | --- |
| 地狱弱点提交的 `minecraft:in_fire` 伤害 | 被免疫 |
| 地狱耐热储备消耗 | 继续消耗；抗火没有改动地狱条件 |
| 地狱内冰息的 `usage_blocked` | 仍按原条件禁用 |
| 炎热群系的伤害减半 / 储备消耗 | 仍按其条件运行 |

抗火结束时，如果储备已经耗尽且仍在地狱，下一个弱点伤害检查点就可能再次掉血。免疫这项火焰伤害也不等于免疫地狱中的所有伤害，例如摔落、窒息、近战攻击仍分别处理。调用 `in_fire` 伤害本身不会自动点燃玩家。

如果设计上希望抗火同时暂停储备消耗、解除炎热减伤或放行冰息，需要分别修改弱点与 `usage_blocked` 条件；仅更换伤害类型只影响伤害处理。其他模组或数据包若修改火焰标签或伤害流程，最终行为还会跟随这些修改。

本节已核对 Minecraft 1.21.1 原版 JAR 的 `data/minecraft/tags/damage_type/is_fire.json`、本地 NeoForge 源码的 `LivingEntity.hurt`，以及 DS 的 `DamagePenalty.apply`。以上新增方案已做静态核对，尚未进行游戏内验证。

## 10. 多种耐受：储备 ID、上限属性与被动如何关联

### 10.1 heat_supply 是类似氧气的储备，但只是一个存储 ID

可以把耐热储备理解为类似原版氧气的资源：进入不利环境消耗，离开后恢复，耗尽后受到惩罚。惩罚由 `effect` 决定，可以是掉血、技能减伤或状态效果。

`dswof:heat_supply` 只是标识这份储备的自定义 ID；维护储备和触发惩罚的是 DS 内置的 `dragonsurvival:supply`。不需要为每个 `supply_type` 额外注册 Java 类型。它与原版氧气各自独立，不会修改玩家的水下氧气值，也不会仅凭这个 ID 自动显示原版氧气条。

### 10.2 储备量 HUD 图标

DS 的 `DragonPenaltyHUD` 会根据 `supply_type` 自动查找储备条贴图，规则是：

```text
assets/<命名空间>/textures/gui/custom/supply_icons/<路径>.png
```

因此 `supply_type: "dswof:heat_supply"` 对应：

```text
src/main/resources/assets/dswof/textures/gui/custom/supply_icons/heat_supply.png
```

这里不需要在 `supply` JSON 中填写 `icon` 字段，也不能使用 `base.custom_icon` 代替。贴图由多个 9×9 小格横向组成：

| `display_like_hunger_bar` | 贴图尺寸 | 横向小格（从左到右） | 显示行为 |
| --- | --- | --- | --- |
| `false`（默认） | 18×9 | 满格、部分格 | 储备未满时显示已拥有的格数；储备满时隐藏整条 |
| `true` | 27×9 | 满格、半格、空格 | 按类似饥饿条的 10 格显示，储备满时也显示 |

例如想让地狱中的耐热储备始终显示为 10 格，应在 `trigger` 中加入：

```json
"display_like_hunger_bar": true
```

如果保持默认 `false`，使用 18×9 的 `heat_supply.png`；如果设置为 `true`，使用 27×9 的贴图，并确保第三个 9×9 区域是空格图案。图标的实际数量由 `PenaltySupply` 中的当前储备百分比计算，不需要为每个等级单独制作图标。

第 7 节被动没有直接指定 `dswof:heat_supply`，两份 JSON 通过相同的 `attribute` 间接关联：

```text
耐热被动升级
→ modifier 增加玩家的 dragonsurvival:penalty_resistance_time
→ 弱点 trigger.attribute 读取该属性作为上限
→ 更新 dswof:heat_supply 的容量
```

| ID 所在位置 | 示例 | 职责 |
| --- | --- | --- |
| 技能文件路径对应的技能 ID | `dswof:heat_resistance` | 标识被动技能，供龙种 abilities 等引用 |
| 被动动作中的 `base.id` | `dswof:heat_resistance` | 标识属性修饰实例，供刷新和移除管理；不是储备 ID |
| 被动 modifier 与弱点 trigger 的 `attribute` | `dragonsurvival:penalty_resistance_time` | 被动修改、弱点读取的玩家属性，建立上限关联 |
| 弱点 trigger 的 `supply_type` | `dswof:heat_supply` | 区分玩家存储中的不同储备池 |

技能 ID 与修饰实例 ID 在示例中恰好相同，但职责不同。将 `base.id` 改成某个储备 ID，不会使该被动自动控制对应储备；要看双方的 `attribute` 是否一致。

### 10.2 耐热、耐寒、耐旱分别升级

每种耐受使用独立储备 ID 和专属上限属性，并由各自被动修改对应属性：

| 耐受类型 | 储备 `supply_type` | 上限 `attribute` | 被动技能 ID |
| --- | --- | --- | --- |
| 耐热 | `dswof:heat_supply` | `dswof:heat_resistance_time` | `dswof:heat_resistance` |
| 耐寒 | `dswof:cold_supply` | `dswof:cold_resistance_time` | `dswof:cold_resistance` |
| 耐旱 | `dswof:dry_supply` | `dswof:dry_resistance_time` | `dswof:dry_resistance` |

例如，在第 7 节耐热被动的内层 `modifiers` 数组中使用以下条目：

```json
{
  "attribute": "dswof:heat_resistance_time",
  "amount": {
    "type": "minecraft:linear",
    "base": 200.0,
    "per_level_above_first": 200.0
  },
  "operation": "add_value"
}
```

对应耐热弱点的顶层 `trigger` 替换为：

```json
{
  "trigger": {
    "penalty_trigger": "dragonsurvival:supply",
    "supply_type": "dswof:heat_supply",
    "attribute": "dswof:heat_resistance_time",
    "trigger_rate": 20,
    "reduction_rate": 1.0,
    "regeneration_rate": 0.001
  }
}
```

两段都是字段片段，需要合并到已有完整配置中。耐寒、耐旱照表替换属性与储备 ID，并各自定义环境条件和惩罚。龙种的 `abilities` 引用需要的被动，`penalties` 引用需要的弱点；仅创建文件不会使它们自动作用于所有龙种。

这些示例中的专属属性需要先在 Java 中接入：

1. 使用 `DeferredRegister<Attribute>` 注册 `heat_resistance_time`、`cold_resistance_time`、`dry_resistance_time`，为每项设置默认值、最小值与最大值。可用 `RangedAttribute` 保存储备点数，并按需求设置同步。
2. 在模组构造方法中，将属性的 DeferredRegister 注册到传入的模组事件总线。
3. 在模组事件总线的 `EntityAttributeModificationEvent` 中，通过 `event.add(EntityType.PLAYER, 对应属性Holder)` 将三项属性添加到玩家。
4. 再在被动与弱点 JSON 中引用已注册的属性 ID，按需要补充属性名称的语言翻译。

注册方式可参考本地 DS 源码的 `registry/DSAttributes.java`。仅在 JSON 中写一个新的 Attribute ID 不会创建该属性；注册了属性但没有添加到玩家时，DS 读取不到该属性实例，储备上限按 0 处理。

专属属性不会自动继承原版 `penalty_resistance_time` 的默认值。若专属属性也设为默认 200、范围 0～16384，则第 7 节的 10～50 秒示例仍适用；若默认值设为 0，未学习时就没有初始耐受储备。

### 10.3 一个被动同时提高多种耐受

按玩法选择以下方案：

| 方案 | 上限关联方式 | 升级结果 | Java 接入 |
| --- | --- | --- | --- |
| 共用原版属性 | 不同储备都读取 `dragonsurvival:penalty_resistance_time` | 被动提高该属性，所有相关储备上限一起提高 | 无需新增属性 |
| 各用专属属性 | 不同储备读取各自专属属性 | 同一被动配置多个 modifier，可分别增加耐热、耐寒、耐旱容量 | 先注册并添加专属属性 |
| 各用专属属性和独立被动 | 每项被动只修改对应属性 | 各种耐受分别付费升级 | 先注册并添加专属属性 |

使用第二种方案时，把耐热、耐寒、耐旱的属性条目放在同一个修饰实例的内层 `modifiers` 数组中，每个条目可使用不同的 LV 公式。同一个被动等级可以让耐热每级增加 200、耐寒每级增加 400，而耐旱每级增加 100。

**共用上限属性不等于共用剩余储备。** 各储备的剩余量仍分别存储，按各自条件、消耗和恢复速率更新。即使上限相同，`reduction_rate` 不同也会产生不同的耐受时间。

多个每 tick 执行的弱点应使用不同 `supply_type`。让它们复用同一 ID 会导致重复推进，甚至一个在消耗、另一个在恢复；若需要多个惩罚共享同一份热量进度，应集中维护储备，再分别触发惩罚。

### 10.4 验证独立性

在生存模式分别查看专属属性，例如 `/attribute @s dswof:heat_resistance_time get` 与 `/attribute @s dswof:cold_resistance_time get`。升级耐热后，确认只有耐热属性变化，再进入各自环境检查耐受时间。若选择统一升级方案，则确认所有目标属性都按各自公式变化。

本节示例的属性注册方式见 [属性注册与冰息专属倍率](12-attributes-and-ice-breath-multiplier.md)；当前工程已开始接入专属属性，实际注册项以源码为准，仍需加载与实测。

## 11. 站冰回蓝与环境弱点豁免

原版炽焰魔法、洞穴健将确实使用了脚下方块条件。可复用 `minecraft:entity_properties` 的 `predicate.stepping_on`，实现站冰增加 `mana_regeneration`，并把地狱弱点条件改成“在地狱且没有站冰”。Supply 条件不成立时会恢复储备并停止该惩罚，不需要另建同储备 ID 的恢复弱点。

完整原版分析、冰方块标签、可升级被动，以及炎热减伤的条件和提前移除配置，见 [站方块被动与站冰回蓝](13-standing-block-passives.md)。该方案是否绑定被动学习等级、是否解除冰息的 `usage_blocked`，需要分别设置条件。
