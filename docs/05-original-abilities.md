# 原版龙技能实现参考

本文按 Dragon Survival **2.0.71** 的实际 JAR、对应 1.21.1 源码和官方 Dragon Ability 教程整理。重点是技能数据如何被执行，以及原版龙息如何把伤害、范围和视觉表现组合起来。不同版本可能更改 CODEC 字段，升级时应重新对照源码。

官方教程：[Dragon Ability](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/Dragon-Ability)、[Activation](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/Activation)、[ActionContainer](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/ActionContainer)。本篇是按 2.0.71 整理的中文开发参考，不是官方 Wiki 的逐字翻译。

查找 `dragon_ability/` 中 JSON 字段的具体作用、必填项、默认值与单位，请使用独立的 [dragon_ability JSON 参数手册](06-dragon-ability-json-reference.md)。

原版各级吐息伤害、经验等级门槛、成长值对范围的影响和附属调数值对照，见[技能等级、成长值与强度数值参考](10-ability-scaling-and-balance.md)。

## 1. 技能怎样运行

吐息附加状态（如 `dragonsurvival:drain`）的注册来源、`potion` 调用流程和状态实现方式，见[DS 状态效果：注册、施加与实现](09-status-effects.md)。

技能是注册在 `dragonsurvival:dragon_ability` 数据包注册表中的 `DragonAbility`。模组读取技能定义后，将其 ID 加入龙种 `abilities`，再由技能栏、按键或被动触发器运行对应实例。原版没有为每项技能单独写一个实体 tick 类；配置描述激活方式和动作，通用技能运行器负责调用它们。

一项技能由四层组成：

| 层 | 配置入口 | 作用 |
| --- | --- | --- |
| 何时激活 | `activation` | 被动、即时施放、引导施放；定义施法时间、冷却、法力、声音和动画 |
| 何时执行动作 | `actions[]` | 定义目标选择、触发时点和重复间隔 |
| 对谁做什么 | `target_selection` | 选自身、视线对象、区域或龙息前方包围盒，并指定实体/方块效果 |
| 如何成长与显示 | `upgrade`、`icon` | 定义技能等级和按等级显示的图标 |

顶层还可有 `usage_blocked`（loot predicate 条件，满足时禁用使用）和 `can_be_manually_disabled`。完整顶层契约见[接口参考的 DragonAbility 小节](02-dragonsurvival-api.md#25-dragonability)。

执行过程可以概括为：

1. 玩家输入由服务端处理，DS 创建或更新技能施放实例，并检查资格、禁用条件、法力与冷却。
2. 激活类型推进施法/引导状态。引导技能在开始时播放起始反馈，达到施法时间后进入持续阶段，结束时触发结束反馈。
3. 技能按动作的 `trigger_point` 运行每个 `target_selection`。目标选择和伤害/状态效果由服务端计算。
4. 若动作包含视觉效果，服务端向跟踪相关实体的客户端发送包；客户端绘制粒子、模型动画等表现。

因此，客户端粒子不能决定谁受伤；目标命中和游戏状态必须由服务端权威处理。

`ActionContainer.trigger_point` 当前接受 `charging`、`default`、`channel_completion`。`charging` 在施法时间到达前执行；`default` 在施法时间到达时及之后、技能仍运行期间执行；`channel_completion` 在引导达到最大持续时间时执行一次。被动技能只能使用 `default`，非引导技能不能用 `channel_completion`。省略时点即 `default`。

## 2. 龙息的结构

森林毒息、洞穴 Nether Breath 和海龙 Storm Breath 都使用 `dragonsurvival:channeled` 激活。它们有相似的骨架，但效果不同：森林息造成专用伤害并有概率施加 Drain；Nether Breath 造成火焰伤害、点燃并能影响部分方块；Storm Breath 造成闪电伤害并施加 Charged，也可在命中方块处生成区域效果。

以森林毒息为例，技能里有两个 `actions`：

- **对前方目标施加玩法效果。** `dragon_breath` targeting 取视线方向派生的前方 AABB（轴对齐包围盒）内满足 `target_conditions` 的实体；使用 `targeting_mode: non_allies` 和 `is_harmful: true` 筛选关系并表明这是有害目标，具体还受玩家攻击配置影响。`trigger_rate: 10` 让动作每 10 个游戏刻执行一次。对有正数蓄力时长的普通引导动作来说，第一次执行发生在 `cast_time` 到达时，之后按这个间隔重复。
- **对自己启动龙息粒子表现。** `self` targeting 将 `breath_particles` 作用到施法者。该效果由服务端发送 `SyncBreathParticles`，再由客户端绘制粒子，并非粒子碰撞产生伤害。

引导的 `cast_time` 是 20 tick（1 秒），`continuous_mana_cost` 按 tick 扣法力，`cooldown` 为 40 tick（2 秒）；配置还引用开始、循环、结束音效，以及 `spell_charge` 和 `breath` 动画。具体数值是原版该能力的配置，不应直接照抄为新技能的平衡值。

`dragon_breath` 的范围并非简单射线：它取玩家视线方向，按 `range_multiplier × DRAGON_BREATH_RANGE` 以及龙的缩放计算一个前方包围盒；实体或方块效果再由这个区域目标系统逐项筛选。成长/属性能影响实际射程，不能把 `range_multiplier` 当成区块或固定方块距离。

| 原版示例 | 玩法部分 | 表现部分 |
| --- | --- | --- |
| Forest Breath | `damage` + 专用 `forest_breath` damage type + 有概率的 Drain 药水效果 | 毒雾与较大毒粒子、breath 动画、开始/循环/结束声音 |
| Nether Breath | `damage` + `ignite` + Burn；另有火焰、有限方块破坏效果 | 火焰粒子、breath 动画与火息声音 |
| Storm Breath | `damage` + Charged；方块作用区域可生成带 Charged 的云 | 闪电粒子、breath 动画与风暴息声音 |

制作龙息变体时，可以按原版配置结构新建自己的技能，调用 DS 已有的激活、目标选择和效果实现。附属 JSON 使用自己的技能 ID；依赖上游已安装的逻辑与资源，无需把上游实现源码装入附属。

### 2.1 本工程路线：相同吐息逻辑、相同动画，独立外观与伤害

你的目标适合做成 **DS 原版吐息的变体技能**。保留所选原版吐息的施法参数、动画配置、目标选择、动作间隔和所需附加效果，主要调整以下内容：

| 内容 | 接入方式 |
| --- | --- |
| 技能 ID | 新建 `dswof:sky_breath` 等自有技能，由自己的 species `abilities` 引用 |
| 施法与目标逻辑 | 使用 `dragonsurvival:channeled`、`dragonsurvival:dragon_breath` 和原版的过滤配置 |
| 龙的吐息动作 | 沿用 `spell_charge` / `breath` 和 `BREATH` 层配置；体型继续引用已有 DS 动画资源 |
| 伤害数值 | 修改 `dragonsurvival:damage` 效果里的 `amount` |
| 伤害类型 | 需要不同分类或死亡消息时修改 `damage_type`；仅调整数值时可以继续引用原有类型 |
| 吐息粒子贴图 | 注册自己的粒子类型，在 `main_particle`、`secondary_particle` 中引用，再为粒子提供自己的 PNG |

当前工程 `sky_body` 已引用 `dragonsurvival:dragon_center` 动画资源。继续使用兼容骨架即可调用现有动画；如果后续更换几何模型，骨骼名称、层级与 pivot 仍会影响相同动画的表现。这里的“相同动画”指龙的身体动作；吐息粒子的序列帧外观由粒子资源另行决定。

**独立贴图需要一小层粒子接入代码。** DS 2.0.71 的 `SmallFireParticleOption` 等类型只有 `duration`、`swirls` 参数，没有任意 `texture` 路径字段。`breath_particles` 可以接收已注册的其他粒子类型，DS 的同步包和客户端发射逻辑会继续负责位置、方向、散布和发射速度。因此可以注册自己的粒子，复用整个吐息流程。

实现顺序：

1. 注册自己的两个粒子 ID，例如 `dswof:sky_breath_small` 和 `dswof:sky_breath_large`。若沿用 `duration`、`swirls` 结构，为自己的 options 提供 `MapCodec`、网络 `StreamCodec`，并让 `getType()` 返回自己的注册类型。
2. 客户端通过 `RegisterParticleProvidersEvent.registerSpriteSet` 为两个类型绑定 provider。可用子类调用 DS `DragonParticle` 的 protected 构造函数，继承其运动、旋转和按年龄选帧逻辑；若要完整保留某个具体火息粒子的亮度、消失烟雾等行为，还需适配该具体类的行为。
3. 给自己的粒子提供 `assets/dswof/particles/sky_breath_small.json` 和对应的 large 文件。这里的 JSON 定义贴图列表，**不会自动注册 Java 粒子类型**。
4. 在技能的 `dragonsurvival:breath_particles` 效果里，把 `main_particle.type`、`secondary_particle.type` 指向自己的注册 ID，保留原版 `spread`、`speed_per_growth` 和所需寿命/旋转参数。
5. 修改伤害配置，并把新技能加入自己的 species。先确认保留哪些原版附加效果，例如点燃、Burn、方块起火；更换 `damage_type` 不会自动删除或转换这些独立动作。

不能只把原版 `SmallFireParticleOption.CODEC` 换个 ID 注册就认为接入完成：原版 options 的 `getType()` 固定返回 DS 自己的粒子类型。自己的 options 必须返回自己的类型，才能保证同步和客户端选择正确的 provider。

例如小粒子的资源文件 `assets/dswof/particles/sky_breath_small.json`：

```json
{
  "textures": [
    "dswof:sky_breath_small_0",
    "dswof:sky_breath_small_1",
    "dswof:sky_breath_small_2"
  ]
}
```

对应图片放在 `assets/dswof/textures/particle/sky_breath_small_0.png` 等位置。这里列出独立帧图片，由继承的粒子逻辑按年龄切帧。large 粒子按相同结构提供自己的资源。

以下只是 `breath_particles` 中的**粒子选项片段**；假定自己的粒子已完成上述注册，并支持这两个参数：

```json
{
  "main_particle": {
    "type": "dswof:sky_breath_small",
    "duration": 37.0,
    "swirls": true
  },
  "secondary_particle": {
    "type": "dswof:sky_breath_large",
    "duration": 37.0,
    "swirls": false
  }
}
```

若直接覆盖 `assets/dragonsurvival/particles/fire.json`、`large_fire.json` 或它们使用的贴图，改动会作用于所有引用这些资源的技能。为不同龙种分别配置外观时，采用自有粒子 ID 与资源路径。

### 2.2 伤害调整时要一起核对的参数

`DamageEffect` 的默认计算式为 `amount * scale`，`scale` 默认取施法者的 `DSAttributes.DRAGON_ABILITY_DAMAGE` 属性。所以 JSON 中的 `amount` 不是所有情况下的最终扣血值，还会经过伤害类型、护甲、抗性及 Minecraft 伤害处理。

相同动作间隔下，调整 `amount` 即可改变伤害基数。`trigger_rate: 10` 表示约每秒执行两次动作，不保证每次伤害都被目标接受。若目标只是更换伤害，先保留原间隔，以免同时改变伤害频率。

只改变数值无需新建 damage type。若要让伤害具有独立名称、分类和死亡消息，再定义自己的 `data/dswof/damage_type/<id>.json` 并检查相应伤害类型标签。贴图颜色本身不决定伤害类型，例如蓝色粒子不会自动获得冰冻效果。

本节实现核对入口：[DSParticles](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/DSParticles.java)、[SmallFireParticleOption](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/particles/SmallFireParticleOption.java)、[DragonParticle](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/client/particles/dragon/DragonParticle.java)、[ClientProxy](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/network/client/ClientProxy.java)、[DamageEffect](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/entity_effects/DamageEffect.java)。这里只补充开发文档，尚未在工程注册这些粒子或提供美术文件。

## 3. 面向附属的结构示例

以下为教学用的**精简结构示例**，演示引导技能、龙息锥、伤害、点燃和独立视觉动作如何拼合。需将其保存为 `data/dswof/dragonsurvival/dragon_ability/sky_breath.json`，并将 `dswof:sky_breath` 加到自有 species `abilities`。替换图标路径，并在目标数据包中提供 `dswof:sky_breath` damage type；示例粒子选用 DS 已注册的粒子 ID，因此必须以 Dragon Survival 为依赖。

```json
{
  "activation": {
    "activation_type": "dragonsurvival:channeled",
    "cast_time": 15.0,
    "continuous_mana_cost": {
      "type": "ticking",
      "amount": 0.02
    },
    "cooldown": 60.0,
    "animations": {
      "start_and_charging": {
        "animation_key": "spell_charge",
        "layer": "BREATH",
        "transition_length": 5,
        "locks_neck": false,
        "locks_tail": false
      },
      "looping": {
        "animation_key": "breath",
        "layer": "BREATH",
        "transition_length": 5,
        "locks_neck": false,
        "locks_tail": false
      }
    }
  },
  "actions": [
    {
      "target_selection": {
        "target_type": "dragonsurvival:dragon_breath",
        "range_multiplier": 0.8,
        "applied_effects": {
          "entity_effect": [
            {
              "effect_type": "dragonsurvival:damage",
              "amount": {
                "type": "minecraft:linear",
                "base": 2.0,
                "per_level_above_first": 0.5
              },
              "damage_type": "dswof:sky_breath"
            },
            {
              "effect_type": "dragonsurvival:ignite",
              "ignite_ticks": 40.0
            }
          ],
          "targeting_mode": "non_allies",
          "is_harmful": true,
          "target_conditions": {
            "condition": "minecraft:entity_properties",
            "entity": "this",
            "predicate": {
              "type_specific": {
                "type": "dragonsurvival:entity_check_predicate",
                "check_for": "living_entity"
              }
            }
          }
        }
      },
      "trigger_rate": 10.0
    },
    {
      "target_selection": {
        "target_type": "dragonsurvival:self",
        "applied_effects": {
          "entity_effect": [
            {
              "effect_type": "dragonsurvival:breath_particles",
              "spread": 0.04,
              "speed_per_growth": 0.02,
              "main_particle": {
                "type": "dragonsurvival:fire",
                "duration": 37.0,
                "swirls": true
              },
              "secondary_particle": {
                "type": "dragonsurvival:large_fire",
                "duration": 37.0,
                "swirls": false
              }
            }
          ],
          "targeting_mode": "all"
        }
      }
    }
  ],
  "icon": {
    "texture_entries": [
      {
        "from_level": 0,
        "texture_resource": "dswof:abilities/sky_breath_0"
      }
    ]
  }
}
```

`amount`、`ignite_ticks` 和 `trigger_rate` 使用 `LevelBasedValue`，可用常量或 `minecraft:linear` / `minecraft:lookup` 等值表达式；值通常按技能等级计算。`trigger_rate` 的单位是游戏刻：1 为每 tick，10 为每 10 tick（每秒约两次）。森林息的 damage type 可查到其上游定义；附属专属伤害类型要在自己的 `data/dswof/damage_type/` 下定义，并确认想要的伤害标签、死亡消息和抗性行为。

例如 `data/dswof/damage_type/sky_breath.json`：

```json
{
  "exhaustion": 0.1,
  "message_id": "dswof.sky_breath",
  "scaling": "when_caused_by_living_non_player"
}
```

玩家死亡消息可在 `assets/dswof/lang/zh_cn.json` 等语言文件中提供 `death.attack.dswof.sky_breath` 与 `death.attack.dswof.sky_breath.player` 翻译。是否需要为伤害类型加入伤害标签，取决于它是否应该被火焰、魔法等分类规则识别。

片段省略了 `upgrade`、`sound`、`usage_blocked` 和语言文件。用于游戏前应补齐图标资源，确认动画键在所用体型上存在，并在客户端和专用服务器加载检查配置。JSON 语法正确不代表注册表引用和粒子 ID 一定有效。

## 4. 目标与效果的选择

当前内置 targeting 类型包括 `dragonsurvival:self`、`area`、`looking_at`、`dragon_breath`、`disc`。`applied_effects` 由实体效果 `entity_effect` 或方块效果 `block_effect` 构成，具体允许哪些字段取决于选择的目标类型。

实体 targeting 可配置：

- `targeting_mode`：例如 `all`、`allies_and_self`、`non_allies` 等目标关系过滤。
- `target_conditions`：Minecraft loot predicate，进一步过滤实体或方块。
- `is_harmful`：向目标模式提供有害能力标记，以应用其敌友相关过滤规则；它本身不是伤害数值。
- `entity_effect`：例如 `dragonsurvival:damage`、`potion`、`ignite`、`breath_particles`。

方块 targeting 使用 `block_effect`，可结合方块 predicate 和 `valid_blocks` 白名单。不要给玩家可施放的技能配置无边界破坏；服务端效果还应尊重区块保护、领地和其他模组的破坏事件。

## 5. 什么时候需要 Java

先检查内置接口。若只是在范围内加伤害、施加状态、点燃或生成粒子，数据定义通常够用。只有当玩法规则无法由现有 targeting/effect 表达时，再添加自定义类型：

1. 实现对应的 `AbilityTargeting`、[AbilityEntityEffect](relationclass/相关类.md) 或 `AbilityBlockEffect` 接口。
2. 为实现提供 `MapCodec`，编码全部必要参数。
3. 将 MapCodec 注册到对应接口的 DS 类型注册表，并在 JSON 中使用你的 `target_type` 或 `effect_type` ID。这些类型在模组注册阶段由 Java 注册；技能定义本身则属于数据包注册表。
4. 在 `apply` 中把玩法变化放在服务端；客户端展示经网络同步的状态或专用 payload。
5. 处理 `remove`、持续状态生命周期、实体无效、世界卸载和联机同步等边界。

当前接口入口与示例位置见[API 参考](02-dragonsurvival-api.md)和[源码定位](04-sources-and-version-notes.md)。没有一个通用的 `registerAbility()` 可以替代各类型自己的 CODEC 与注册流程。

### 5.1 示例：接触越久伤害越高，并对冰冻免疫目标减半

下面是基础教学示例。当前工程的冰息实现还支持自定义伤害公式，配置 ID 为 `dswof:ice_breath_damage`，其参数与下方示例有区别；以[冰息伤害表达式](07-ice-breath-expressions.md)为准。

这个规则不能只用 `damage_type` JSON 或内置 `dragonsurvival:damage` 表达：内置伤害表达式能读取技能等级、属性等施法者数据，但没有“目标连续处于吐息区域的 tick 数”变量，也不会自动调用目标的原版冻结能力判断。

建议保留 DS 的 `dragon_breath` targeting 和原版动画，只把实体伤害效果换成附属自己的 `AbilityEntityEffect`。设接触时间为 `t` tick，技能等级计算出的参数为 `base`、`growth`、`cap`，每次命中的伤害为：

```text
damage = min(cap, base + growth × (t / 20))
if (!target.canFreeze()) damage *= 0.5
```

`LivingEntity.canFreeze()` 是 Minecraft 1.21.1 的原版判断，已经包含旁观者、冻结免疫装备和实体自身规则。冰冻免疫不是普通 MobEffect；不要用 `hasEffect()` 判断。

技能 JSON 只负责选择目标和传入参数：

```json
{
  "target_selection": {
    "target_type": "dragonsurvival:dragon_breath",
    "range_multiplier": 1.0,
    "applied_effects": {
      "entity_effect": [
        {
          "effect_type": "dswof:exposure_damage",
          "damage_type": "dswof:ice_breath",
          "base_damage": 2.0,
          "growth_per_second": 4.0,
          "max_damage": 12.0,
          "freeze_immune_multiplier": 0.5,
          "reset_after_ticks": 10
        }
      ],
      "targeting_mode": "non_allies",
      "is_harmful": true
    }
  },
  "trigger_rate": 10.0
}
```

`trigger_rate` 为动作调用间隔；上例每 10 tick 调用一次。自定义效果每次被调用时，以施法者 UUID、目标 UUID 和技能 ID 组成 key，记录 `lastSeen` 与 `exposureTicks`：

```java
public record ExposureDamageEffect(
        Holder<DamageType> damageType,
        LevelBasedValue baseDamage,
        LevelBasedValue growthPerSecond,
        LevelBasedValue maxDamage,
        float freezeImmuneMultiplier,
        int resetAfterTicks
) implements AbilityEntityEffect {
    public static final MapCodec<ExposureDamageEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            DamageType.CODEC.fieldOf("damage_type").forGetter(ExposureDamageEffect::damageType),
            LevelBasedValue.CODEC.fieldOf("base_damage").forGetter(ExposureDamageEffect::baseDamage),
            LevelBasedValue.CODEC.fieldOf("growth_per_second").forGetter(ExposureDamageEffect::growthPerSecond),
            LevelBasedValue.CODEC.fieldOf("max_damage").forGetter(ExposureDamageEffect::maxDamage),
            Codec.FLOAT.optionalFieldOf("freeze_immune_multiplier", 0.5f)
                    .forGetter(ExposureDamageEffect::freezeImmuneMultiplier),
            Codec.INT.optionalFieldOf("reset_after_ticks", 10)
                    .forGetter(ExposureDamageEffect::resetAfterTicks)
    ).apply(instance, ExposureDamageEffect::new));

    private static final Map<Key, State> STATE = new HashMap<>();

    @Override
    public void apply(ServerPlayer dragon, DragonAbilityInstance ability, Entity entity) {
        if (!(entity instanceof LivingEntity target)) return;

        long now = dragon.serverLevel().getGameTime();
        Key key = new Key(dragon.getUUID(), target.getUUID(), ability.id());
        State old = STATE.get(key);
        int elapsed = old == null ? 0 : (int) (now - old.lastSeen());
        int exposure = old == null || elapsed > resetAfterTicks
                ? 0
                : old.exposureTicks() + Math.max(0, elapsed);
        exposure = Math.min(exposure, 20 * 10); // 10 seconds cap; make configurable if needed
        STATE.put(key, new State(now, exposure));

        float damage = Math.min(
                maxDamage.calculate(ability.level()),
                baseDamage.calculate(ability.level())
                        + growthPerSecond.calculate(ability.level()) * exposure / 20.0f
        );
        if (!target.canFreeze()) damage *= freezeImmuneMultiplier;
        if (damage > 0) target.hurt(new DamageSource(damageType, dragon), damage);
    }

    private record Key(UUID dragon, UUID target, String ability) {}
    private record State(long lastSeen, int exposureTicks) {}

    @Override
    public MapCodec<? extends AbilityEntityEffect> entityCodec() { return CODEC; }
}
```

上面是核心逻辑，实际文件还需要补齐 `Holder`、`DamageType`、`LevelBasedValue`、`AbilityEntityEffect` 等 import。`STATE` 必须在服务端 tick 中清理长期未出现的条目，或在世界卸载时清空；否则大量目标会造成无界内存增长。`resetAfterTicks` 应至少覆盖一次正常 `trigger_rate` 间隔，目标离开吐息区域超过该时间就重新从 0 tick 计算。若不希望目标短暂离开后保留进度，可把它设为略大于 `trigger_rate`，并在应用前额外检查更严格的时间差。

将自定义 CODEC 注册到 DS 的能力实体效果注册表：

```java
@SubscribeEvent
public static void register(RegisterEvent event) {
    if (event.getRegistry() == AbilityEntityEffect.REGISTRY) {
        event.register(AbilityEntityEffect.REGISTRY_KEY,
                ResourceLocation.fromNamespaceAndPath("dswof", "exposure_damage"),
                () -> ExposureDamageEffect.CODEC);
    }
}
```

该方法放在附属的 `Bus.MOD` 事件订阅类中；注册完成后，JSON 才能使用 `"effect_type": "dswof:exposure_damage"`。不要把附属效果注册成 `dragonsurvival:exposure_damage`，也不要把 Java 动态逻辑写进 `damage_type` 文件。

伤害类型本身仍是普通数据：

```text
data/dswof/damage_type/ice_breath.json
```

```json
{
  "message_id": "dswof.ice_breath",
  "scaling": "when_caused_by_living_non_player",
  "exhaustion": 0.1
}
```

如果希望 DS 的“龙息伤害”分类逻辑识别它，可向现有 DS 标签追加条目：
`data/dragonsurvival/tags/damage_type/dragon_breath.json`，内容使用 `"replace": false` 和 `"values": ["dswof:ice_breath"]`。这只影响标签查询，不会自动实现持续时间递增或冰冻免疫减伤。若要让它属于原版冻结伤害分类，还可以追加到 `minecraft:is_freezing`，但这同样不会替代 `target.canFreeze()` 的倍率判断。

实现时还要考虑原版无敌帧、护甲、抗性和 `hurt()` 返回值。`trigger_rate` 太小会让伤害频率和网络负担上升；通常 5～10 tick 一次，并设置 `max_damage`，更容易控制平衡。

## 6. 动画和模型挂点

技能动画通过 `activation.animations` 映射到动画 key 与动画层。龙息常用 `spell_charge` 作为蓄力、`breath` 作为持续喷吐，但目标体型使用的动画逻辑必须实际定义这些 key。

模型若要让 DS 自带龙息渲染器从口部发射粒子，需要提供名字准确的 `BreathSource` 骨骼。渲染器通过该骨骼的位置作为喷吐源；它不负责决定命中或伤害范围。模型设计和 DS 骨骼约定见[模型教程](03-modeling-tutorial.md)及[骨骼参考](reference/dragon-bones.md)。缺少挂点会让粒子位置错误或回退，但不会改变服务端 targeting 的效果区域。

## 7. 调试清单

- 技能文件路径是单数目录：`dragon_ability/`；ID 需加入 species `abilities` 或对应标签。
- `activation_type`、`target_type`、`effect_type` 都是注册表 ID，不是任意字符串；字段和值类型需符合当前 2.0.71 CODEC。
- `trigger_rate` 是动作间隔，值为 10 表示每秒约两次，而不是“每秒十次”；若想在蓄力阶段执行，要显式设置 `trigger_point: "charging"`。
- `dragon_breath` 射程会乘以 DS 的呼吸射程属性，并受龙体缩放影响；查看 `range_multiplier` 时要一起考虑这两项。
- 测试目标过滤、友方伤害、方块破坏、连续法力不足、施放中断与冷却；再让第二名玩家观察动画和粒子。
- 检查专用服务器加载，确保服务端技能逻辑没有引用仅客户端类。
- 新模型的 `BreathSource`、动画 key、音效和图标路径分别检查；这些是不同资源接口。

原版技能在这里用于解释实现结构。Dragon Survival 的源码、模型、动画、贴图和声音受其上游许可证约束；附属应优先引用已安装模组的资源 ID，自己制作要发布的模型与美术，并遵守[上游许可](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/LICENSE.txt)。
