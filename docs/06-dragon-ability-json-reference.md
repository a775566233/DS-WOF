# dragon_ability JSON 参数手册

本文单独解释 `dragon_ability/` 中技能定义的参数，适用于 **Minecraft 1.21.1、NeoForge、Dragon Survival 2.0.71**。字段、必填项与默认值以本工程依赖对应源码的 CODEC 为准。官方 Wiki 用来辅助理解，旧版字段不能直接混用。

如果你正在制作“保留原版吐息逻辑与动画，只修改伤害和贴图”的技能，先读第 1～7 节、第 9.1～9.3 节及第 13 节。粒子贴图的 Java 接入另见[原版技能文档第 2.1 节](05-original-abilities.md#21-本工程路线相同吐息逻辑相同动画独立外观与伤害)。

## 目录

1. [文件位置与阅读约定](#1-文件位置与阅读约定)
2. [通用数值与单位](#2-通用数值与单位)
3. [顶层字段](#3-顶层字段)
4. [activation：激活方式](#4-activation激活方式)
5. [声音、动画与提示](#5-声音动画与提示)
6. [actions：动作与执行时点](#6-actions动作与执行时点)
7. [target_selection：选择目标](#7-target_selection选择目标)
8. [upgrade 与 icon：升级和图标](#8-upgrade-与-icon升级和图标)
9. [实体效果参数](#9-实体效果参数)
10. [方块效果参数](#10-方块效果参数)
11. [共用与嵌套对象](#11-共用与嵌套对象)
12. [条件与 Minecraft 对象](#12-条件与-minecraft-对象)
13. [完整吐息示例与修改位置](#13-完整吐息示例与修改位置)
14. [常见错误及源码索引](#14-常见错误及源码索引)

## 1. 文件位置与阅读约定

在附属资源根目录下创建：

```text
src/main/resources/data/dswof/dragonsurvival/dragon_ability/sky_breath.json
```

该文件的技能 ID 是 `dswof:sky_breath`。子目录会成为 ID 路径的一部分，例如 `breath/sky.json` 对应 `dswof:breath/sky`。文件中不用再写一个顶层 `id`。

技能还需要由龙种的 `abilities` 引用；仅创建 JSON 不会自动将它加入玩家技能栏。数据目录是单数 `dragon_ability`，技能定义属于 `data/`，图片、动画、声音属于 `assets/`。

表格约定：

| 标记 | 含义 |
| --- | --- |
| 必填 | JSON 必须提供该字段 |
| 可选；默认某值 | 省略后使用 CODEC 的默认值 |
| 可选；无 | 省略代表该项未配置，不等同于数值 0 |
| LV | `LevelBasedValue`：常数或随技能等级变化的数值对象，见第 2 节 |
| ID | `命名空间:路径`，如 `dragonsurvival:fire_breath` |
| 集合 | 注册表 ID、ID 数组或 `#命名空间:标签`；具体字段决定引用哪个注册表 |

下文效果类型表中的短名都带 `dragonsurvival:` 前缀，例如 `damage` 在 JSON 中应写成 `"effect_type": "dragonsurvival:damage"`。不要因为省略命名空间而误用默认的 `minecraft:`。

## 2. 通用数值与单位

需要对照原版实际数值时，见[技能等级、成长值与强度数值参考](10-ability-scaling-and-balance.md)，其中区分技能等级、经验等级、龙成长值，并给出分级伤害表和范围公式。

### 2.1 技能等级与 LevelBasedValue

这里的 `level` 通常是**该技能的等级**，与玩家经验等级、龙的成长值分别计算。技能实例等级范围为 `0～255`，0 通常表示未解锁；有效效果通常从 1 级计算。

LV 可以直接写数字，例如 `"cooldown": 40`；也可以写对象：

```json
{
  "type": "minecraft:linear",
  "base": 3,
  "per_level_above_first": 3
}
```

计算公式为 `base + per_level_above_first × (技能等级 - 1)`，上例在 1、2、3、4 级分别为 3、6、9、12。

Minecraft 1.21.1 内置的 LV 对象如下；表中列出的子字段都必填：

| `type` | 子字段 | 计算方式 |
| --- | --- | --- |
| `minecraft:linear` | `base`、`per_level_above_first`：数值 | 以上线性公式 |
| `minecraft:lookup` | `values`：数值数组；`fallback`：LV | 1 级取 `values[0]`，2 级取 `values[1]`；超出数组长度时用 `fallback`，仍传入原技能等级 |
| `minecraft:clamped` | `value`：LV；`min`、`max`：数值 | 把结果限制在区间内；要求 `max > min` |
| `minecraft:fraction` | `numerator`、`denominator`：LV | 分子除以分母；分母为 0 时返回 0 |
| `minecraft:levels_squared` | `added`：数值 | `技能等级² + added` |

固定值直接写数字即可。不要自行添加 `minecraft:constant` 类型：当前默认注册的对象类型中没有它。

`lookup.values` 的第一项对应 **1 级**，不是 0 级。DS 只在部分客户端信息显示场景对 0 级查询做保护，不应依赖它让服务端在 0 级执行查表效果。

### 2.2 单位

| 数值类别 | 常用单位与示例 |
| --- | --- |
| `cast_time`、`cooldown`、`max_duration`、大多数 `duration`、`ignite_ticks` | tick；正常 20 tick = 1 秒，40 = 2 秒 |
| `trigger_rate` | 动作执行间隔，单位 tick；10 通常表示每 0.5 秒一次 |
| `probability`、`ignite_probability` | 概率；`0.3` 是 30%，`1` 是 100%，不要填 30 表示 30% |
| `percentage`、`percent` 调整的 `amount` | 比例；`0.25` 是 25% |
| 生命伤害 | 生命值点；2 点通常对应 1 颗心，最终受属性、护甲、抗性等影响 |
| `radius`、`range` | 通常为方块距离；`range_multiplier` 是倍率，不能当固定距离 |
| 药水 `amplifier` | 从 0 开始，0 = I 级，1 = II 级 |

JSON 中的 tick 值与某些 Java Builder 方法的单位不同。例如 `PotionData.Builder.duration(10)` 接受秒，而 JSON 的 `"duration": 10` 是 10 tick。

## 3. 顶层字段

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `activation` | 对象；必填 | 激活类型、耗蓝、施法时间、冷却等 |
| `icon` | LevelBasedResource；必填 | 按技能等级选择界面图标 |
| `upgrade` | 对象；可选；无 | 升级规则；不配置时最高等级为 1 |
| `usage_blocked` | loot condition；可选；无 | 条件为 **true 时禁止使用**；并非满足后允许使用 |
| `actions` | 动作数组；可选；`[]` | 每个动作定义执行时点、目标、效果 |
| `can_be_manually_disabled` | 布尔；可选；`true` | 是否允许玩家手动禁用技能；不是技能的初始启用状态 |

最小结构如下。它可以表达一个没有实际效果的被动技能，仅用于说明顶层层级：

```json
{
  "activation": {
    "activation_type": "dragonsurvival:passive"
  },
  "icon": {
    "texture_entries": [
      {
        "from_level": 0,
        "texture_resource": "dragonsurvival:abilities/cave/nether_breath_1"
      }
    ]
  },
  "actions": []
}
```

## 4. activation：激活方式

`activation.activation_type` 必填。当前有三种内置类型，各自接受的字段不同。

### 4.1 `dragonsurvival:simple`：单次施放

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `activation_type` | ID；必填 | `dragonsurvival:simple` |
| `initial_mana_cost` | LV；可选；0 | 完成蓄力进入生效阶段时扣除的一次性法力 |
| `cast_time` | LV；可选；0 | 生效前的蓄力时长，tick；运行时转为整数 |
| `cooldown` | LV；可选；0 | 施放结束后的冷却时长，tick |
| `can_move_while_casting` | 布尔；可选；`true` | 施法期间能否移动 |
| `notification` | 对象；可选；默认提示 | 法力不足、禁用条件的提示，见第 5.3 节 |
| `sound` | 对象；可选；无 | 声音配置；此类型不允许配置 `looping` |
| `animations` | 对象；可选；无 | 动画配置；此类型不允许配置 `looping` |

完成蓄力后执行普通动作，随后结束。`cast_time` 为 0 表示没有配置蓄力等待；2.0.71 的运行器先递增计数，部分开始反馈和初始耗蓝分支使用“计数恰好等于 cast_time”，因此有一次性耗蓝或阶段反馈需求时，应在游戏内核验 0 tick 的表现。

### 4.2 `dragonsurvival:channeled`：持续引导

支持上一节的同名字段及默认值；`activation_type` 改为 `dragonsurvival:channeled`，并额外支持：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `continuous_mana_cost` | ManaCost；可选；无 | 持续耗蓝；该类型只接受 `type: ticking` |
| `max_duration` | LV；可选；无 | 蓄力之后的引导最长时长，tick；省略表示没有配置时长上限 |

允许 `sound.looping` 和 `animations.looping`。吐息一般使用此类型。

持续耗蓝对象示例：

```json
{
  "type": "ticking",
  "amount": 0.025
}
```

`amount` 是**每 tick**的法力值。`0.025` 在正常 20 TPS 下约为每秒 0.5 法力，不是每秒 0.025。

`max_duration` 从蓄力完成后计时。当前判断为“已引导 tick **大于**配置时长”，达到退出条件时才执行 `channel_completion` 动作；不要把这个动作当作所有中断情形都会运行的结束回调。

### 4.3 `dragonsurvival:passive`：被动

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `activation_type` | ID；必填 | `dragonsurvival:passive` |
| `continuous_mana_cost` | ManaCost；可选；无 | 持续耗蓝或预留法力 |
| `cooldown` | LV；可选；0 | 被动触发后的冷却；有冷却时会在动作执行后停止本次触发 |
| `trigger` | 触发对象；可选；constant | 什么事件触发被动 |

被动类型的 CODEC 不读取 `initial_mana_cost`、`cast_time`、`sound`、`animations` 等主动技能字段。

### 4.4 ManaCost 的所有字段

| 字段 | 类型 / 必填 | 作用 |
| --- | --- | --- |
| `type` | 字符串；必填 | `ticking`：逐 tick 扣除；`reserved`：预留法力，减少可用于其他技能的法力预算 |
| `amount` | LV；必填 | 消耗或预留的法力值 |

`reserved` 不等于每次触发扣同样数值，也不是提高最大法力。引导类型会拒绝它；主要用于被动技能。预留能否成立还取决于玩家当前可用法力和技能启用状态。

### 4.5 passive.trigger 的所有内置类型

`trigger_type` 必填。除下表额外字段外，不需要再写一个事件名称。

| `trigger_type`（加 `dragonsurvival:`） | 额外字段 | 触发含义 |
| --- | --- | --- |
| `constant` | 无 | 持续尝试生效，仍受冷却、条件、法力约束 |
| `on_self_hit` | `condition`：可选 loot condition | 自己受击时 |
| `on_target_hit` | `condition`：可选 loot condition | 自己命中目标时 |
| `on_target_killed` | `condition`：可选 loot condition | 自己击杀目标时 |
| `on_death` | 无 | 自己死亡时 |
| `on_block_break` | `condition`：可选 loot condition | 自己破坏方块时 |
| `on_key_pressed` | `keys`：必填字符串数组 | 指定按键按下时 |
| `on_key_released` | `keys`：必填字符串数组 | 指定按键松开时 |

`keys` 使用输入键标识，例如 `["key.keyboard.g"]`，不是界面显示的“G”，也不是整数键码。触发器的 `condition` 测试的是对应事件上下文；它不替代动作中的目标选择。

## 5. 声音、动画与提示

### 5.1 activation.sound

四个字段均为**可选 SoundEvent ID**，未填写就没有该阶段的声音。

| 字段 | 作用 |
| --- | --- |
| `charging` | 蓄力阶段声音 |
| `start` | 蓄力完成、开始生效时播放；不是刚按键就一定播放 |
| `looping` | 引导阶段的循环声音；`simple` 禁止使用 |
| `end` | 结束施放时的声音 |

例如 `dragonsurvival:fire_breath_loop` 引用已注册的声音事件，不是 `.ogg` 文件路径。

### 5.2 activation.animations

| 字段 | 类型 / 默认 | 作用 |
| --- | --- | --- |
| `start_and_charging` | 可选简单动画或复合动画；无 | 施法开始、蓄力阶段 |
| `looping` | 可选简单动画；无 | 持续引导阶段；`simple` 禁止使用 |
| `end` | 可选简单动画；无 | 结束阶段 |

**简单动画对象**：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `animation_key` | 字符串；必填 | 体型动画逻辑中的 key，例如 `spell_charge`、`breath`；不是动画 JSON 文件路径 |
| `layer` | 字符串；必填 | `BASE`、`BREATH`、`BITE`，必须保持大写 |
| `transition_length` | 整数；可选；0 | 动画切换过渡长度，tick |
| `locks_neck` | 布尔；必填 | 是否锁定颈部，影响通常的头颈追踪 |
| `locks_tail` | 布尔；必填 | 是否锁定尾部，影响通常的尾部运动 |

**复合动画对象**仅用于 `start_and_charging`：把简单动画的 `animation_key` 换成两个必填字符串 `starting_animation_key` 和 `looping_animation_key`，分别播放一次起始动作及后续循环蓄力动作；`layer`、`transition_length`、`locks_neck`、`locks_tail` 的要求相同。

保留原版龙身体的吐息动作时，通常保留 `spell_charge`、`breath` 和 `BREATH` 层。体型仍须提供兼容的动画逻辑、骨骼与动画资源。

### 5.3 activation.notification

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `not_enough_mana` | Minecraft 文本组件；可选；DS 默认法力不足提示 | 法力不足时的提示 |
| `usage_blocked` | Minecraft 文本组件；可选；无 | 顶层禁用条件阻止使用时的提示 |

例如 `{"text":"当前无法吐息"}`，或使用 `{"translate":"ability.dswof.sky_breath.blocked"}` 配合语言文件。这里的 `usage_blocked` 是提示文本，顶层同名字段才是条件，二者类型不同。

## 6. actions：动作与执行时点

每个 `actions[]` 元素有三个字段：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `target_selection` | 目标对象；必填 | 选取目标并应用效果 |
| `trigger_point` | 字符串；可选；`default` | 执行动作的阶段 |
| `trigger_rate` | LV；可选；1 | 每隔多少 tick 执行 |

`trigger_point` 的取值：

| 值 | 什么时候执行 | 限制 |
| --- | --- | --- |
| `charging` | 计数小于 `cast_time` 的蓄力阶段 | 被动技能不允许 |
| `default` | 蓄力完成进入生效阶段；引导时持续尝试执行 | 被动技能只允许此值 |
| `channel_completion` | 引导超过 `max_duration`、自动结束的那个分支 | 仅 `channeled`；仍受 `trigger_rate` 过滤 |

间隔计算使用：`actualTick = currentTick - cast_time`，当 `actualTick % trigger_rate == 0` 才执行。因而配置 `cast_time: 20`、`trigger_rate: 10` 时，普通动作可在计数 20、30、40……执行。

`trigger_rate` 建议使用正整数。它不是每秒次数，也不是概率。当前源码在值为 0 或负数时跳过间隔过滤，**不会据此禁用动作**；小数间隔还会受浮点取余影响。`charging` 也使用上述减去施法时长后的计数，不能简单理解为从蓄力第一刻独立计时。

若希望最大引导结束时一定尝试执行完成动作，给那一项用 `trigger_rate: 1`。提前松键、法力不足等停止施法的情形不能保证调用完成动作。

## 7. target_selection：选择目标

### 7.1 所有内置目标类型

`target_type` 和 `applied_effects` 均必填；其余参数取决于类型。

| `target_type`（加 `dragonsurvival:`） | 专用参数 | 范围含义 |
| --- | --- | --- |
| `self` | 无 | 施法者自身 |
| `area` | `radius`：必填 LV | 以施法位置为中心的 AABB 区域；当前实现是包围盒，不是精确球体 |
| `looking_at` | `range`：必填 LV | 按视线选取距离内目标 / 方块 |
| `dragon_breath` | `range_multiplier`：必填 LV | 视线方向派生的前方 AABB；射程是该值乘 DS 的 `DRAGON_BREATH_RANGE` 属性，宽度等还受龙的缩放影响 |
| `disc` | `radius`：必填 LV；`height`：可选 LV，默认 1；`height_starts_below`：可选布尔，默认 false | 扁平区域也按 AABB 处理，半径和高度计算结果转为整数；是否从脚下低一格开始计算 |

`disc` 的垂直边界：`height_starts_below: false` 时从当前位置 Y 到 `Y + height`；为 true 时从 `Y - 1` 到 `Y + height - 1`。

吐息粒子的视觉轮廓、`dragon_breath` 的伤害包围盒、目标条件是不同层。改变粒子寿命或速度不会自动改变伤害射程；也不要假定这种范围具备精确锥体或逐粒子遮挡判定。

### 7.2 applied_effects：实体分支

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `entity_effect` | 实体效果数组；必填 | 对每个通过筛选的目标执行的效果；类型见第 9 节 |
| `targeting_mode` | 字符串；必填 | 关系筛选模式，见下表 |
| `target_conditions` | loot condition；可选；无 | 对选中的目标做进一步筛选 |
| `is_harmful` | 布尔；可选；false | 是否按有害行为处理玩家关系；它本身不造成伤害 |

| `targeting_mode` | 通常含义 |
| --- | --- |
| `all` | 全部候选目标 |
| `allies` | 友方，通常排除自己 |
| `allies_and_self` | 友方与自己 |
| `non_allies` | 非友方 |
| `non_enemies` | 非敌方 |
| `neutral` | 中立 |
| `enemies` | 敌方 |
| `items` | 掉落物实体 |
| `experience_orbs` | 经验球实体 |
| `all_except_self` | 自己以外 |

关系判断受队伍、驯服关系以及 DS 服务端玩家攻击设置影响。尤其是中立玩家且 `is_harmful: true` 的分支会直接采用玩家可攻击性判断，不能把上表视为完全独立于 PVP 配置的强制过滤器。`self` 类型直接对自己测试条件、应用效果，不按这些关系枚举重新筛选，但其 CODEC 仍要求填写 `targeting_mode`，通常写 `all`。

### 7.3 applied_effects：方块分支

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `block_effect` | 方块效果数组；必填 | 对选中的方块执行效果，见第 10 节 |
| `target_conditions` | loot condition；可选；无 | 对候选方块及其上下文进一步筛选 |

**同一个 `applied_effects` 只选择一个分支。** 不要把 `entity_effect` 和 `block_effect` 同时塞进一个对象；CODEC 是二选一结构，可能只按其中一支解码而忽略另一支。吐息同时伤害实体、点燃方块时，拆成两个动作；粒子通常再用第三个 `self` 动作。

## 8. upgrade 与 icon：升级和图标

### 8.1 upgrade 的所有内置类型

每种升级对象都必填 `upgrade_type`。

| `upgrade_type`（加 `dragonsurvival:`） | 参数与默认值 | 行为 |
| --- | --- | --- |
| `experience_points` | `maximum_level`：必填整数，0～255；`experience_cost`：必填 LV | 玩家手动消耗经验点升级；区别于经验等级门槛 |
| `experience_levels` | `maximum_level`：必填整数，0～255；`level_requirement`：必填 LV | 根据当前玩家经验等级推导技能等级；门槛降低时也可能降级，不是消费经验升级 |
| `dragon_growth` | `maximum_level`：必填整数，0～255；`growth_requirement`：必填 LV | 根据龙成长值决定等级 |
| `item_based` | `items_per_level`：必填集合数组；`downgrade_items`：必填物品集合 | 外层数组第 1 项对应从 0 升 1 级的物品，第 2 项对应 1 升 2；最大等级取数组长度，不填 `maximum_level` |
| `condition_based` | `conditions`：必填 loot condition 数组；`require_previous`：可选布尔，默认 true | 最大等级取条件数量；第 1 个条件对应 1 级，不填 `maximum_level` |

`experience_cost` 按要达到的升级等级计算；经验点升级的退级/返还还受 DS 当前升级实现和配置影响。

`condition_based.require_previous: true` 要求从低到高连续满足，遇到第一个不满足的条件即停止；false 则采用所满足的最高条件位置，不要求前置条件成立。`item_based` 的空集合条目在 DS 的自动升级检查中可自动提升一级，并不是“此级永远无法解锁”。

### 8.2 icon / LevelBasedResource

| 字段 | 类型 / 必填 | 作用 |
| --- | --- | --- |
| `texture_entries` | 对象数组；必填 | 各等级阈值对应资源；应至少填一项 |
| `texture_entries[].from_level` | 整数；必填，0～255 | 从哪个技能等级开始使用该资源 |
| `texture_entries[].texture_resource` | ID；必填 | 资源标识 |

加载时按 `from_level` 降序排列，取“不大于当前等级”的最高阈值。不需要按顺序书写，但推荐顺序清晰且阈值唯一；若没有命中阈值，会回退到排序后的第一项。空数组可能在取图标时出错。

技能 `icon` 使用 GUI sprite，例如 `dswof:abilities/sky_breath_1` 对应：

```text
assets/dswof/textures/gui/sprites/abilities/sky_breath_1.png
```

此处通常不写 `textures/` 和 `.png`。同一个 LevelBasedResource 结构也用于其他资源场景，实际资源路径规则取决于使用它的渲染器，不能把所有图标、投射物图片都按技能 GUI sprite 规则处理。

## 9. 实体效果参数

每项位于 `target_selection.applied_effects.entity_effect[]`，**必填 `effect_type`**。以下覆盖此版本注册的全部 32 种实体效果；共用类型的字段在第 11 节。

### 9.1 damage：直接伤害

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `damage_type` | DamageType ID；必填 | 伤害分类、死亡消息及相关伤害标签行为 |
| `amount` | LV；必填 | 基础伤害数值 |
| `scale` | 属性 ID；可选；DS 的 `DRAGON_ABILITY_DAMAGE` | 从施法者读取哪个属性作为伤害缩放值 |
| `expression` | 表达式字符串；可选；`amount * scale` | 实际伤害计算表达式 |
| `use_claw` | 布尔；可选；false | 计算/攻击期间临时使用龙爪栏剑类装备的切换逻辑 |

例如 `amount: 6` 代表基础 6 点；默认还会乘 `scale` 属性值。若确实需要不经该属性缩放的计算，可显式配置 `expression: "amount"`，仍会受到后续伤害处理影响。

**表达式的版本限制：**运行时虽然设置了 `ability_level`、`experience_level`、`experience_points`、`growth`、`current_movement_speed`，但当前 JSON CODEC 只用 `amount` 和 `scale` 验证表达式。不要直接把其他运行时变量作为可用 JSON 参数，可能在加载时就验证失败；等级伤害优先通过 `amount` 的 LV 表达。

附属可以在自己的效果中复用 DS 的 `Expression`，并通过自己的 `expressionCodec(...)` 声明更多变量。当前工程冰息效果的实现、变量表与 JSON 示例见[冰息伤害表达式](07-ice-breath-expressions.md)。

### 9.2 breath_particles：原版吐息粒子发射

| 字段 | 类型 / 必填 | 作用 |
| --- | --- | --- |
| `spread` | 浮点数；必填 | 发射方向的随机角度散布系数，越大越分散；不是伤害范围，也不是度数 |
| `speed_per_growth` | 浮点数；必填 | 龙成长值对应的粒子速度系数；客户端按成长值计算发射速度 |
| `main_particle` | ParticleOptions；必填 | 第一组粒子类型及其选项 |
| `secondary_particle` | ParticleOptions；必填 | 第二组粒子类型及其选项 |

这是视觉效果，通常对 `self` 执行。其粒子数量由 DS 按成长值计算；当前逻辑不是“main 一定比 secondary 多”，第二组发射次数反而更多。没有 `particle_count`、`texture`、`damage` 等额外字段。

DS 火焰、毒雾、闪电等龙息粒子选项通常为：

| 字段 | 类型 / 必填 | 作用 |
| --- | --- | --- |
| `type` | 已注册粒子 ID；必填 | 例如 `dragonsurvival:fire`、`dragonsurvival:large_fire` |
| `duration` | 浮点数；必填 | 粒子寿命；DS DragonParticle 转为整数 tick |
| `swirls` | 布尔；必填 | 是否使用旋转 / 环绕运动 |

换成其他粒子类型后，选项由该粒子自己的 CODEC 决定，不保证仍支持 `duration`、`swirls`。要让附属拥有独立吐息 PNG，需要注册自己的粒子类型与客户端 provider，再把这里的 `type` 改成自己的 ID；没有在此直接填 PNG 路径的通用字段。

### 9.3 ignite、potion、on_attack

| `effect_type` 短名 | 字段 | 作用 |
| --- | --- | --- |
| `ignite` | `ignite_ticks`：必填 LV | 点燃目标的时长，tick；与方块起火效果 `fire` 分开 |
| `potion` | `potion`：必填 PotionData 对象 | 立即给目标施加指定状态效果 |
| `on_attack` | `potion`：必填 PotionData 对象 | 给目标设置攻击时施加药水效果的能力；不是立即对周围敌人施加 |

PotionData 的所有字段：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `effects` | MobEffect 集合；必填 | 状态效果，例如 `minecraft:poison`；不是酿造药水物品 ID |
| `amplifier` | LV；必填 | 强度，从 0 开始 |
| `duration` | LV；必填 | 持续时间，tick；`-1` 表示无限时长 |
| `probability` | LV；可选；1 | 每种效果尝试施加的概率 |
| `effect_particles` | 布尔；可选；false | 是否显示药水粒子 |
| `show_icon` | 布尔；可选；true | 是否显示状态效果图标 |

### 9.4 回复、资源与位移

| `effect_type` 短名 | 字段与默认值 | 作用 |
| --- | --- | --- |
| `heal` | `percentage`：必填 LV | 按目标最大生命值比例治疗，0.1 = 10% |
| `mana_recovery` | `action_type`：必填 `set` / `add`；`adjustment_type`：必填 `flat` / `percent`；`amount`：必填 LV；`probability`：可选 LV，默认 1 | 设置或增加当前法力；percent 的基准是目标最大法力 |
| `cooldown_recovery` | `abilities`：可选技能集合，默认目标的所有已知技能；`action_type`：必填 `set` / `reduce`；`adjustment_type`：必填 `flat` / `percent`；`amount`：必填 LV；`probability`：可选 LV，默认 1；`exclude_this`：可选布尔，默认 true | 调整技能剩余冷却；flat 单位 tick，percent 按该技能配置的总冷却计算，非当前剩余时间 |
| `dragon_growth` | `growth_type`：必填 `set` / `add`；`action_type`：必填 `flat` / `percent`；`amount`：必填 LV；`probability`：可选 LV，默认 1 | 调整目标龙的成长；percent 依据当前阶段成长区间，set percent 从该阶段最小成长值起算 |
| `experience` | `experience_type`：必填 `points` / `levels`；`amount`：必填 LV；`probability`：可选 LV，默认 1 | 调整经验点；levels 当前实现先把数值换成“从 0 到该等级所需的经验点”再加给玩家，不是简单在当前等级上加 N |
| `push` | `target_direction`：必填方向对象；`push_force`：必填 LV | 沿指定方向增加速度；方向对象见第 11.3 节 |
| `teleport` | `target_direction`：必填方向对象；`range`：必填 LV | 沿指定方向尝试传送的最大距离 |

`hunger` 的四个字段均为可选 LV：

| 字段 | 省略行为 / 作用 |
| --- | --- |
| `hunger_gain` | 默认按 0 处理；正数恢复饥饿值。负数分支当前传入负 exhaustion，不能当作可靠的扣饥饿手段 |
| `saturation_gain` | 默认按 0 处理；增加饱和度 |
| `maximum_saturation` | 无配置上限；配置后限制本效果增加饱和度时的上限 |
| `conversion_rate` | 不转换；配置后将超出 20 点饥饿值的部分按比例转入饱和度 |

`hunger` 的负数和溢出转换实现有边界问题，设计需要扣饥饿或精确转换比例的技能时应额外核验，不要仅按字段名称推断。

### 9.5 飞行、游泳与攀爬

| `effect_type` 短名 | 字段与默认值 | 作用 |
| --- | --- | --- |
| `flight` | `level_requirement`：必填整数；`icon`：可选资源 ID，默认 `dragonsurvival:textures/ability_effect/generic_icons/dragon_wings.png` | 技能等级达到门槛后允许飞行；icon 是飞行效果提示图，路径规则不同于顶层 GUI sprite |
| `spin` | `level_requirement`：必填整数；`fluid_types`：可选 NeoForge FluidType 集合，无则不限定该列表 | 技能等级达到门槛后允许旋转动作，并配置流体环境 |
| `swim` | `max_oxygen`：可选 LV，默认 0；`has_stable_swim`：可选 LevelBasedBoolean，默认恒 false；`fluid_type`：必填 NeoForge FluidType ID | 对指定流体配置氧气与稳定游泳 |
| `climbable` | `climbables`：必填 Climbable 对象数组 | 提供攀爬墙面 / 天花板能力；详见第 11.6 节 |

`flight.level_requirement`、`spin.level_requirement` 指**技能等级门槛**，不要与升级配置中的玩家经验等级门槛混淆。`has_stable_swim` 是对象，不能直接填 JSON 布尔值。

### 9.6 持续效果、属性与物品

| `effect_type` 短名 | 字段与默认值 | 作用 |
| --- | --- | --- |
| `modifier` | `modifiers`：必填 ModifierWithDuration 数组 | 持续修改实体属性；第 11.2 节 |
| `damage_modification` | `modifications`：必填 DamageModification 数组 | 按伤害类型修改受到的伤害倍率；第 11.6 节 |
| `effect_modification` | `modifications`：必填 EffectModification 数组 | 修改指定状态效果的持续时间 / 强度；第 11.6 节 |
| `harvest_bonus` | `harvest_bonuses`：必填 HarvestBonus 数组 | 挖掘速度与采掘加成；第 11.6 节 |
| `oxygen_bonus` | `bonuses`：必填 OxygenBonus 数组 | 额外氧气；第 11.6 节 |
| `glow` | `glows`：必填 Glow 数组 | 带颜色的发光显示；第 11.6 节 |
| `block_vision` | `block_visions`：必填 BlockVision 数组 | 高亮附近指定方块；第 11.7 节 |
| `effect_removal` | `categories`：可选字符串数组；`valid_effects`：可选 MobEffect 集合；`max_amount`：可选 LV；`maximum_effect_level`：可选 LV | 移除状态效果；省略的筛选项不限制。categories 使用大写 `BENEFICIAL` / `HARMFUL` / `NEUTRAL`；maximum_effect_level 比较从 0 开始的 amplifier |
| `smelting` | `item_predicate`：可选物品谓词；`progress`：可选 LV；`grants_experience`：可选布尔，默认 true | 熔炼掉落物；省略 progress 为立即熔炼，提供时每次执行累计进度，按配方时间和物品数量判断完成 |
| `item_conversion` | `item_conversions`：必填转换数组；`probability`：必填 LV | 将匹配的掉落物转换为加权结果，嵌套格式见第 11.5 节 |
| `use_item` | `item`：必填 ItemStack；`probability`：可选 LV，默认 1；`sound`：可选 SoundEvent ID；`valid_entities`：可选 EntityPredicate | 模拟对生物使用指定物品，例如物品对生物的交互行为 |

当前版本实体 `use_item` 的实现连续执行两次独立概率检查，因此 `probability: 0.3` 的实际通过率约为 9%，不是 30%；默认 1 不受这个问题影响。

### 9.7 projectile 与共用效果

`projectile` 支持：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `projectile_data` | DS ProjectileData 注册表 ID；可选 | 引用单独配置的 DS 投射物数据，不是在这里内嵌整个数据对象 |
| `projectile_type` | EntityType ID；可选 | 直接创建投射物实体类型；必须实际是受支持的 Projectile |
| `target_direction` | 方向对象；必填 | 发射方向 |
| `number_of_projectiles` | LV；必填 | 发射数量 |
| `projectile_spread` | LV；可选；0 | 投射物散布值，传入发射逻辑 |
| `speed` | LV；必填 | 发射速度 |

`projectile_data` 和 `projectile_type` 至少提供一个。通常只选一项；两项都填时当前代码会分别执行两条生成路径。ProjectileData 自身属于另一注册表的 JSON 契约，本手册在此解释引用入口，不展开它的全部命中效果系统。

实体效果还支持 `particle`、`run_function`、`summon_entity` 三种共用效果，所有字段见第 11.4 节。

## 10. 方块效果参数

每项位于 `target_selection.applied_effects.block_effect[]`，**必填 `effect_type`**。以下是全部内置方块效果；`particle`、`run_function`、`summon_entity` 与实体分支共用。

| `effect_type` 短名 | 字段与默认值 | 作用 |
| --- | --- | --- |
| `bonemeal` | `attempts`：必填 LV；`probability`：必填 LV | 对目标位置尝试骨粉催熟，attempts 为尝试次数 |
| `fire` | `ignite_probability`：必填 LV | 在目标位置尝试产生火焰的概率 |
| `block_break` | `valid_blocks`：必填世界生成 BlockPredicate；`probability`：必填 LV；`drop_loot`：可选布尔，默认 false | 破坏匹配方块；默认不掉落战利品 |
| `block_harvest` | `valid_blocks`：必填世界生成 BlockPredicate；`probability`：可选 LV，默认 1；`tool`：可选 ItemStack | 按采收逻辑处理匹配方块；可指定用于处理掉落等行为的工具 |
| `conversion` | `conversion_data`：必填转换数组；`probability`：必填 LV | 将匹配方块替换为加权选择的方块状态，详见第 11.5 节 |
| `explosion` | `probability`：可选 LV，默认 1；`power`：必填 LV；`fire`：可选布尔，默认 true；`damage_type`：必填 DamageType ID | 在位置产生爆炸，配置强度、起火及伤害类型 |
| `use_item` | `item`：必填 ItemStack；`probability`：可选 LV，默认 1；`sound`：可选 SoundEvent ID；`valid_blocks`：可选世界生成 BlockPredicate | 模拟对方块使用物品 |
| `area_cloud` | `potion`：必填 PotionData；`duration`：必填 LV；`probability`：必填 LV；`delay`：可选 LV；`radius`：可选 LV；`particle`：必填 ParticleOptions | 生成区域药水云；duration / delay 为云的生存 / 等待 tick，potion.duration 是目标中状态效果的时长，二者不同 |
| `particle` | `particle_data`、`particle_count`：必填 | 位置粒子，见第 11.4 节 |
| `run_function` | `function`：必填函数 ID | 执行数据包函数，见第 11.4 节 |
| `summon_entity` | 见第 11.4 节 | 在目标位置召唤实体 |

`area_cloud.delay` / `radius` 省略时不覆盖原版 AreaEffectCloud 的对应默认值。方块效果会在范围内对多个位置分别尝试，`probability: 0.1` 不是“整次吐息只做一次 10% 抽签”。

方块 `use_item` 虽然读取 ItemStack，执行时却按物品类型新建一个栈；配置的数量与组件不会随之保留。它与实体分支使用物品栈的方式不同。

## 11. 共用与嵌套对象

### 11.1 base：持续实例基础数据

属性、免伤、氧气、高亮等持续效果通常包含 `base` 对象：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `id` | ID；必填 | 持续效果实例的标识；建议使用附属命名空间，并避免不同用途意外共用同一个 ID |
| `duration` | LV；可选；-1 | 存续 tick；-1 表示无限时长 |
| `should_remove_automatically` | 布尔；可选；false | 启用额外的来源、技能状态、距离等自动清理检查 |
| `early_removal_condition` | loot condition；可选；无 | 满足时提前移除，以持有该效果的实体作为检查上下文 |
| `custom_icon` | 资源 ID；可选；无 | 持续效果提示的自定义图标 |
| `is_hidden` | 布尔；可选；false | 隐藏持续效果的提示显示 |

自动清理并不简单等同于“施法结束就移除”：当前主动技能的检查采用技能是否启用，被动技能则检查是否正在应用效果。需要精确时长时填写正的 `duration`；不建议用 0 表示立即移除，运行器的倒计时实现先递减再判断是否等于 0。

这是配置结构。源码中保存运行状态的 `base_data`、`common_data`、`current_duration`、`entity_uuid` 等序列化字段，不能照搬到技能 JSON 的 base 中。

### 11.2 ModifierWithDuration 与 Modifier

`modifier.modifiers[]` 每项包含两个必填字段：`base`（上一节结构）、`modifiers`（下表属性修饰器数组）。注意存在两层名为 `modifiers` 的数组。

| 内层字段 | 类型 / 必填 | 作用 |
| --- | --- | --- |
| `attribute` | 属性 ID；必填 | 要调整的属性 |
| `amount` | LV 或 PreciseLevelBasedValue；必填 | 属性变化值 |
| `operation` | 字符串；必填 | `add_value`：加固定量；`add_multiplied_base`：按基础属性比例增加；`add_multiplied_total`：总值倍率修饰 |

PreciseLevelBasedValue 的 `precise_base` 和 `precise_amount` 都是必填浮点数，计算为 `precise_base + precise_amount × level`；它与 linear 的“等级减 1”公式不同。

结构片段示例：

```json
{
  "effect_type": "dragonsurvival:modifier",
  "modifiers": [
    {
      "base": {
        "id": "dswof:sky_health_bonus",
        "should_remove_automatically": true
      },
      "modifiers": [
        {
          "attribute": "minecraft:generic.max_health",
          "amount": 4,
          "operation": "add_value"
        }
      ]
    }
  ]
}
```

### 11.3 TargetDirection

`target_direction` 是对象，其中必填字符串 `direction`。可用值：

| 值 | 含义 |
| --- | --- |
| `looking_at` | 使用相关实体的朝向 |
| `towards_entity` | 从施法者向本次选中的目标 |
| `down`、`up`、`north`、`south`、`west`、`east` | 固定世界方向 |

例如 `"target_direction": {"direction":"looking_at"}`。不要把它写成没有外层对象的字符串。

### 11.4 particle、run_function、summon_entity

**particle**：`particle_data` 为必填 SpawnParticles 对象，`particle_count` 为必填 LV。SpawnParticles 的参数：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `particle` | ParticleOptions；必填 | 粒子类型及专用选项 |
| `horizontal_position` | PositionSource；必填 | X/Z 位置来源 |
| `vertical_position` | PositionSource；必填 | Y 位置来源 |
| `horizontal_velocity` | VelocitySource；必填 | X/Z 速度来源 |
| `vertical_velocity` | VelocitySource；必填 | Y 速度来源 |
| `speed` | Minecraft FloatProvider；可选；0 | 发送粒子时的速度参数；不是 LV |

PositionSource：`type` 必填，可为 `entity_position` / `in_bounding_box`；`offset` 可选默认 0，表示坐标偏移；`scale` 可选默认 1，必须为正，缩放随机包围盒范围。`entity_position` 不允许把 scale 改成非 1 值。

VelocitySource：`movement_scale` 可选浮点数默认 0，表示继承目标运动的倍率；`base` 可选 FloatProvider 默认 0，表示额外基础速度。FloatProvider 可以写常数或 Minecraft 的随机浮点提供器对象，不能写 LV 的 `minecraft:linear`。

**run_function**：只需额外必填 `function` ID，例如 `dswof:ability/sky_breath_hit`。对应 Minecraft 1.21.1 文件 `data/dswof/function/ability/sky_breath_hit.mcfunction`，不是 `.json` 路径。实体分支以选中的目标为执行实体，并使用其位置、朝向，因此 `@s` 是目标；方块分支使用方块中心作为执行位置，没有实体执行者，不能假定 `@s` 是施法者。

**summon_entity**：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `base` | DurationInstanceBase；必填 | 召唤实例的标识和时限等 |
| `entities` | 实体类型集合或加权数组；必填 | 可召唤的实体类型；普通集合例 `["minecraft:wolf"]`，加权数组条目包含 `data`（实体 ID）与 `weight`（整数权重） |
| `max_summons` | LV；必填 | 该召唤实例允许维持的最大数量 |
| `attribute_scales` | 对象数组；可选；`[]` | 每项必填 `attributes`（属性集合）与 `scale`（LV）；以 ADD_MULTIPLIED_BASE 方式调整召唤物属性，0.2 表示增加基础值的 20% |
| `nbt` | CompoundTag；可选；无 | 额外实体 NBT，遵循 Minecraft 的 CompoundTag CODEC |
| `is_allied` | 布尔；可选；true | 是否将召唤物视为施法者友方 |

### 11.5 物品 / 方块转换条目

**item_conversion.item_conversions[]**：每项必填 `item_predicate`（Minecraft ItemPredicate）和 `items_to`（加权结果数组）。每个结果对象：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `item` | 物品 ID；必填 | 转换后的物品 |
| `conversion_rate` | double；可选；1.0 | 输入数量向输出数量转换的倍率 |
| `weight` | 整数；必填 | 随机结果权重；它是相对权重，不是百分比 |
| `particles` | ParticleData；可选；无 | 转换时的粒子反馈 |

**conversion.conversion_data[]**：每项必填 `from_predicate`（世界生成 BlockPredicate）和 `blocks_to`（加权结果数组）。结果必填 `state`（BlockState）与 `weight`（整数），可选 `particles`（ParticleData）。BlockState 例：`{"Name":"minecraft:stone"}`；有状态属性时用 `Properties`。

此处 ParticleData 与 `particle` 效果使用相同的两个必填字段：`particle_data`（SpawnParticles）、`particle_count`（LV）。不要把它误写成只有 `type` 的 ParticleOptions。

### 11.6 其余持续效果条目

下表中的每个对象都**必填 `base`**，再填写对应专用字段：

| 对象 | 专用参数与默认值 | 含义 |
| --- | --- | --- |
| DamageModification | `damage_types`：可选 DamageType 集合，省略不按类型限制；`multiplier`：必填 LV | 受到的对应伤害倍率；0 表示免疫，0.5 为一半，2 为两倍 |
| EffectModification | `effects`：必填 MobEffect 集合；`duration_modification`、`amplifier_modification`：可选 Modification，默认无变化 | 调整状态效果持续时间、强度 |
| HarvestBonus | `blocks`：可选 Block 集合；`base_speed`：可选 LevelBasedTier；`harvest_bonus`：可选 LV 默认 0；`break_speed_multiplier`：可选 LV 默认 0 | 限定方块、工具档位基础挖掘速度及额外采掘 / 破坏速度加成；两项数值的默认 0 表示没有该项加成 |
| OxygenBonus | `fluid_types`：可选 NeoForge FluidType 集合；`oxygen_bonus`：必填 LV | 指定流体环境中的额外氧气；支持 -1 表示无限氧气 |
| Glow | `color`：必填 TextColor | 显示发光的颜色，例如 `#88ccff` |
| Climbable | `blocks`：必填 LevelBasedBlockPredicate；`can_stick_to_walls`：可选 LevelBasedBoolean 默认恒 false；`can_climb_ceilings`：同前 | 允许攀爬的方块，以及是否可贴墙 / 爬天花板 |

Modification 有两个必填字段：`type` 为 `additive`（增加数值）或 `multiplicative`（乘倍率），`amount` 为 LV。注意默认“无变化”不等于任意类型的 amount 都填 0：乘法的恒等值为 1。

等级分段对象的格式：

| 对象 | 字段 | 选择规则 |
| --- | --- | --- |
| LevelBasedBoolean | `fallback`：可选布尔默认 false；`entries`：必填数组；每项必填 `from_level`（0～255）和 `value`（布尔） | 取不大于技能等级的最高阈值条目，未匹配取 fallback |
| LevelBasedBlockPredicate | `fallback`：可选世界生成 BlockPredicate，默认永真；`entries`：必填数组；每项必填 `from_level`（0～255）和 `value`（世界生成 BlockPredicate） | 同上 |
| LevelBasedTier | `tiers`：必填数组；每项必填 `from_level`（0～255）和 `tier` | 同上；未匹配时没有该工具档位。tier 使用大写 `WOOD`、`STONE`、`IRON`、`DIAMOND`、`GOLD`、`NETHERITE` |

例如恒真的 LevelBasedBoolean 是 `{"fallback":true,"entries":[]}`，不是直接的 `true`。

### 11.7 BlockVision

`block_visions[]` 每项的完整字段：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `base` | DurationInstanceBase；必填 | 持续实例信息 |
| `blocks` | Block 集合；必填 | 显示的方块种类 |
| `range` | LV；必填 | 搜索距离 |
| `display_type` | 字符串；必填 | `outline`、`particles`、`simple_shader`、`none` |
| `colors` | 颜色数组或 ColorEntry 数组；必填 | 颜色，例如 `["#88ccff"]`，或 `[{"color":"#88ccff","alpha":0.3}]` |
| `particle_rate` | 整数；可选；10 | 粒子显示更新间隔；至少为 1 |
| `color_shift_rate` | double；可选；1 | 多颜色切换速率；范围 0～10 |

ColorEntry 的 `color` 必填，`alpha` 可选默认 0.3，表示透明度。颜色列表应保持一种条目结构。

## 12. 条件与 Minecraft 对象

### 12.1 条件的方向与上下文

`usage_blocked`、触发器 `condition`、`target_conditions`、`early_removal_condition` 使用 Minecraft 的 LootItemCondition，但检查对象不同：

| 放置位置 | 条件为 true 的含义 |
| --- | --- |
| 顶层 `usage_blocked` | 禁止使用技能 |
| `trigger.condition` | 事件允许触发 |
| `applied_effects.target_conditions` | 这个候选目标允许应用效果 |
| `base.early_removal_condition` | 提前移除持续效果 |

常用组合是 `condition: minecraft:any_of` 加 `terms`（任一成立）、`minecraft:all_of` 加 `terms`（全部成立）、`minecraft:inverted` 加 `term`（取反）、`minecraft:entity_properties` 加 `entity` / `predicate`（实体条件）。这些是带 Minecraft 自身结构的条件对象，不是任意字符串表达式。

技能条件不一定具有战利品表的全部参数，例如工具、伤害源、方块状态是否存在取决于实际事件 / targeting 创建的上下文。将某个条件从“受击事件”复制到“禁用使用”时需要重新检查它依赖什么上下文。

### 12.2 原版技能常用的 DS 实体子谓词

它们放在 `minecraft:entity_properties` 的 `predicate.type_specific` 中。

**`type: dragonsurvival:entity_check_predicate`**：`check_for` 可选，省略不做该种类限制；可选值为 `living_entity`、`enemy`、`tamed`、`animal`、`item`、`experience_orb`。

**`type: dragonsurvival:custom_predicates`** 的字段均可选，未填写就不检查该项：

| 字段 | 类型 | 检查内容 |
| --- | --- | --- |
| `eye_in_fluid` | NeoForge FluidType 集合 | 眼睛是否在对应流体中 |
| `weather_predicate` | WeatherPredicate | 所处位置的天气 |
| `sun_light_level` | 整数或 `{min,max}` 边界 | 日光等级 |
| `has_duration_effect` | ID | 是否持有对应 ID 的 DS 持续效果 |
| `is_nearby_entity` | 对象 | 必填 `entity_types`（EntityType 集合）和 `radius`（整数）；检查附近 AABB 内是否有匹配实体 |
| `player_hunger` | 整数或 `{min,max}` 边界 | 玩家饥饿值 |
| `health_percentage` | 数值或 `{min,max}` 边界 | 生命比例，0.5 表示一半 |
| `has_uuid` | UUID | 是否匹配指定实体 UUID |
| `looking_at_block` | 对象 | 必填 `predicate`（世界生成 BlockPredicate）和 `distance`（double）；检查视线中的方块 |

WeatherPredicate 有四个可选布尔字段：`is_raining`、`is_thundering`、`is_snowing`、`is_raining_or_snowing`。true 表示要求是该天气，false 表示要求不是，省略不检查。`weather_predicate: {}` 本身不表示“正在下雨”。

### 12.3 不同的“类型”和“谓词”不能互换

| 对象 | 格式与作用 |
| --- | --- |
| 世界生成 BlockPredicate | 本文 `valid_blocks`、`from_predicate`、攀爬条件等使用的方块谓词，例 `{"type":"minecraft:matching_blocks","blocks":["minecraft:stone"]}` |
| EntityPredicate | 进度系统的实体谓词，如 use_item 的 `valid_entities`；没有 loot condition 的外层 `condition` |
| ItemPredicate | 匹配输入物品的条件，不是一个 ItemStack |
| ItemStack | 实际物品栈，例如 `{"id":"minecraft:bone_meal","count":1}`；还可使用当前版本的组件结构 |
| ParticleOptions | `{"type":"minecraft:flame"}` 或带该粒子专属参数的对象；具体 CODEC 由 type 决定 |
| TextColor | 颜色名称或 `#RRGGBB` |
| 注册表集合 | 如 `["minecraft:stone","minecraft:deepslate"]` 或 `"#minecraft:logs"`；标签必须属于该字段对应的注册表 |

本手册完整列出 DS 技能自有字段；Minecraft 通用谓词、物品组件、NBT 与第三方注册的条件 / 粒子类型可继续扩展各自的结构，不存在一张对所有模组都适用的固定子参数表。遇到扩展类型时应查它自己的 CODEC。

NeoForge 还支持数据加载条件。此版本的 `actions` 列表元素和顶层 `usage_blocked` 使用了条件解码包装；这类加载时条件与游戏运行时的 loot condition 是两套机制。常规技能先按本文不带额外加载条件的结构编写即可。

## 13. 完整吐息示例与修改位置

以下是教学用的火息变体技能，使用 DS 已有的类型、粒子、伤害和图标，保留蓄力与吐息动画，并单独设置基础伤害。它省略了原版的天气禁用、Burn、方块破坏等分支，用于展示字段位置，不是原版 Nether Breath 的完整复制。

保存为第 1 节的 `sky_breath.json`，并在龙种 `abilities` 中引用 `dswof:sky_breath`。数字是示例平衡值。

```json
{
  "activation": {
    "activation_type": "dragonsurvival:channeled",
    "cast_time": 20,
    "cooldown": 40,
    "continuous_mana_cost": {
      "type": "ticking",
      "amount": 0.025
    },
    "sound": {
      "start": "dragonsurvival:fire_breath_start",
      "looping": "dragonsurvival:fire_breath_loop",
      "end": "dragonsurvival:fire_breath_end"
    },
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
  "icon": {
    "texture_entries": [
      {
        "from_level": 0,
        "texture_resource": "dragonsurvival:abilities/cave/nether_breath_1"
      }
    ]
  },
  "upgrade": {
    "upgrade_type": "dragonsurvival:experience_levels",
    "maximum_level": 4,
    "level_requirement": {
      "type": "minecraft:lookup",
      "values": [0, 10, 30, 50],
      "fallback": 50
    }
  },
  "actions": [
    {
      "trigger_rate": 10,
      "target_selection": {
        "target_type": "dragonsurvival:dragon_breath",
        "range_multiplier": 1,
        "applied_effects": {
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
          },
          "entity_effect": [
            {
              "effect_type": "dragonsurvival:damage",
              "damage_type": "dragonsurvival:fire_breath",
              "amount": {
                "type": "minecraft:linear",
                "base": 4,
                "per_level_above_first": 2
              }
            }
          ]
        }
      }
    },
    {
      "trigger_rate": 1,
      "target_selection": {
        "target_type": "dragonsurvival:self",
        "applied_effects": {
          "targeting_mode": "all",
          "entity_effect": [
            {
              "effect_type": "dragonsurvival:breath_particles",
              "spread": 0.04,
              "speed_per_growth": 0.02,
              "main_particle": {
                "type": "dragonsurvival:fire",
                "duration": 37,
                "swirls": true
              },
              "secondary_particle": {
                "type": "dragonsurvival:large_fire",
                "duration": 37,
                "swirls": false
              }
            }
          ]
        }
      }
    }
  ]
}
```

按你的开发目标，通常调整这些位置：

| 要改的内容 | JSON / 资源入口 |
| --- | --- |
| 每次伤害 | `actions[0].target_selection.applied_effects.entity_effect[0].amount`，当前 1～4 级为 4、6、8、10，再参与属性缩放 |
| 伤害类别 | 上述效果的 `damage_type`；自定义类型还要提供 `data/dswof/damage_type/<id>.json` 及所需标签 / 语言资源 |
| 伤害频率 | `actions[0].trigger_rate`；改变频率会影响每秒总伤害，实际还受受伤冷却等机制影响 |
| 伤害射程 | `actions[0].target_selection.range_multiplier`，以及 DS 相关属性 |
| 粒子外观 | `actions[1]` 中两项粒子的 `type`；自己的类型须先完成 Java 注册与 provider，贴图资源放在自己的命名空间 |
| 粒子运动与寿命 | `spread`、`speed_per_growth`、粒子 `duration` / `swirls` |
| 龙身体动画 | 保留 `activation.animations`，并继续使用兼容体型资源 |
| 单次耗蓝 / 持续耗蓝 | `initial_mana_cost` / `continuous_mana_cost.amount`；后者是每 tick，正常每秒消耗约为该值乘 20 |

若要完整保留原版其他行为，应以已选原版技能 JSON 为底稿保留其附加动作与条件，再替换上述字段。修改 `damage_type` 不会自动修改独立的 `ignite`、`potion`、`fire`、方块破坏等效果。

## 14. 常见错误及源码索引

### 14.1 常见错误

| 现象 / 写法 | 检查位置 |
| --- | --- |
| 添加 JSON 后龙没有新技能 | 检查 species 的 `abilities` 是否引用技能，以及升级门槛是否满足 |
| 写 `"texture":"dswof:...png"` 期望换吐息图 | `breath_particles` 没有此字段，应换注册粒子类型及其资源 |
| 写了字段但没有变化 | CODEC 可能忽略不属于该类型的未知字段；语法正确不代表字段被读取 |
| 将 `entity_effect` 改成复数 `entity_effects` | 当前是单数键名，值为数组；方块同理 |
| 在一个 applied_effects 中混写两类效果 | 拆成不同 actions |
| `duration: 10` 以为持续 10 秒 | 此处通常是 10 tick，10 秒应写 200 |
| 想要毒 II 却填 `amplifier: 2` | 应填 1；2 对应 III |
| 把触发间隔填 0 以关闭动作 | 当前 0 会绕过间隔过滤；应删除动作或设置正确条件 |
| 把完成动作当作松键回调 | 该时点只在达到最大引导时长的分支运行，且受 trigger_rate 过滤 |
| 图标正常但技能没有伤害 | 检查激活、升级等级、目标条件、关系 / PVP 配置与服务端日志 |
| 只检查 JSON 语法就认为可以上线 | 还需检查注册表 ID、CODEC 解码、资源存在性和客户端 / 服务端实际行为 |

### 14.2 核对依据

上游固定提交：`6638c759c5eb224f7de72d9b10b7bec75f9fbafc`。以下为本文主要源码入口：

- [DragonAbility：顶层字段](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/DragonAbility.java)
- [激活方式及被动触发器](https://github.com/DragonSurvivalTeam/DragonSurvival/tree/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/activation)
- [ActionContainer：时点与间隔](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/codecs/ability/ActionContainer.java)
- [DragonAbilityInstance：计时、耗蓝与执行](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/DragonAbilityInstance.java)
- [目标类型与关系判断](https://github.com/DragonSurvivalTeam/DragonSurvival/tree/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/targeting)
- [实体效果及其类型注册](https://github.com/DragonSurvivalTeam/DragonSurvival/tree/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/entity_effects)
- [方块效果及其类型注册](https://github.com/DragonSurvivalTeam/DragonSurvival/tree/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/block_effects)
- [共用效果](https://github.com/DragonSurvivalTeam/DragonSurvival/tree/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/common_effects)
- [升级规则](https://github.com/DragonSurvivalTeam/DragonSurvival/tree/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/upgrade)
- [嵌套 CODEC、粒子数据与谓词](https://github.com/DragonSurvivalTeam/DragonSurvival/tree/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/codecs)

官方辅助教程：[Dragon Ability](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/Dragon-Ability)、[Activation](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/Activation)、[ActionContainer](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/ActionContainer)。更广泛的工程接口见[接口参考](02-dragonsurvival-api.md)，技能执行原理见[原版龙技能实现参考](05-original-abilities.md)。

本文示例做静态语法与字段核对；尚未作为新技能在游戏中加载并验证全部行为。
