# 原版 DS 技能等级、成长值与强度数值参考

本文按本工程实际依赖 **Dragon Survival 2.0.71 / Minecraft 1.21.1 / NeoForge** 的 JAR 数据和对应源码整理。这里的“原版 DS”指未被数据包、配置或其他模组改写的 DS 默认规则，不是 Minecraft 原版。

**原版没有统一的“技能强度 = 技能等级 × 成长值”公式。** 三种主吐息的直接伤害随技能等级增长；这些技能的等级由玩家当前经验等级决定。龙的成长值主要改变吐息范围、身体大小，以及部分按成长解锁的被动技能。默认成长阶段没有给主吐息再乘一个成长伤害倍率。

本文伤害单位为生命值点，2 点通常对应 1 颗心。所有伤害表列的是传入 `target.hurt(...)` 的数值；护甲、抗性、伤害类型标签、受伤无敌时间、吸收生命和其他模组事件仍会影响最终扣血。文中的每秒速率按 20 TPS 计算，未把附加状态伤害计入直接伤害。

## 1. 先区分五个数值

| 记号 | 含义 | 参与方式 |
| --- | --- | --- |
| `L` | 当前技能实例的等级 `ability.level()` | 代入技能字段的 `LevelBasedValue`，决定该字段数值 |
| `E` | 玩家当前 Minecraft 经验等级 `experienceLevel` | 原版主吐息用它判定技能等级门槛 |
| `G` | 龙的实际成长值 `getGrowth()` | 默认范围为 10～60，影响阶段、范围和身体属性 |
| `S` | `dragonsurvival:dragon_ability_damage` 属性的当前值 | 默认伤害表达式中的倍率，基础值为 1 |
| `R` | `dragonsurvival:dragon_breath_range` 属性的当前值 | 与技能的 `range_multiplier(L)` 相乘，决定标称吐息距离 |

`S` 是技能伤害倍率，不是身体缩放 `minecraft:generic.scale`。默认数据、没有额外属性修饰时，`S = 1`；该属性自身允许的范围是 0～10。

例如，同为 4 级森林吐息且 `S = 1`，成长值 10 和成长值 60 的单次直接伤害都是 8。后者的吐息范围更大，也可能有其他被动优势。

## 2. 等级如何代入数值

### 2.1 线性数值：一律从“第 1 级的值”开始理解

技能中常见配置：

```json
{
  "type": "minecraft:linear",
  "base": 2.0,
  "per_level_above_first": 2.0
}
```

当输入是技能等级 `L` 时：

```text
value(L) = base + per_level_above_first × (L - 1)
         = 2 + 2 × (L - 1)
         = 2L
```

所以 1～4 级对应 2、4、6、8。`base` 已包含第 1 级，不应再额外加一次增量。Java 中的 `LevelBasedValue.perLevel(k)` 对应 `kL`。

`minecraft:lookup` 的 `values[0]` 对应输入等级 1，下一项对应等级 2；超出表长度时由 `fallback` 使用原输入等级计算。技能等级 0 通常表示未解锁，不能把表的第一项理解为 0 级数值。

### 2.2 三种常用升级机制，含义不同

| `upgrade_type` | 原版例子 | 如何变化 |
| --- | --- | --- |
| `dragonsurvival:experience_levels` | 三种主吐息 | 检查玩家当前经验等级，自动设置满足门槛的技能等级，不消耗经验 |
| `dragonsurvival:dragon_growth` | 三种 `*_claws_and_teeth` | 检查龙成长值，自动设置满足门槛的技能等级 |
| `dragonsurvival:experience_points` | 三种 `*_magic` | 主动升级并支付经验点，经验点不同于经验等级 |

前两种会重新判定等级，并非永久累计解锁。例如主吐息在经验等级 50 时为 4 级，经验等级降到 29 后，按默认自动规则会回到 2 级。成长类技能在成长值降低时也可降级。

某字段是否随等级增长，取决于它自己的配置。提高技能等级不会自动将伤害、范围、概率、持续时间、耗蓝全部同比提高。

## 3. 原版主吐息：可直接参考的伤害表

### 3.1 等级门槛

`nether_breath`、`forest_breath`、`storm_breath` 使用相同升级配置：

```json
{
  "upgrade_type": "dragonsurvival:experience_levels",
  "maximum_level": 4,
  "level_requirement": {
    "type": "minecraft:lookup",
    "values": [0.0, 10.0, 30.0, 50.0],
    "fallback": {
      "type": "minecraft:linear",
      "base": 15.0,
      "per_level_above_first": 15.0
    }
  }
}
```

| 玩家当前经验等级 `E` | 主吐息等级 `L` |
| --- | --- |
| 0～9 | 1 |
| 10～29 | 2 |
| 30～49 | 3 |
| 50 及以上 | 4 |

这里的 10、30、50 是经验等级门槛，与龙成长值 10、25、40、60 没有对应关系。`maximum_level = 4` 已封顶，不能根据 `fallback` 推断默认还有第 5 级。

### 3.2 单次直接伤害

原版 `DamageEffect` 的默认计算为：

```text
D = amount(L) × S
```

以下取 `S = 1`：

| 技能 ID | `amount(L)` | 1 级 | 2 级 | 3 级 | 4 级 | 伤害动作间隔 |
| --- | --- | --- | --- | --- | --- | --- |
| `nether_breath`：洞穴龙火息 | `3L` | 3 | 6 | 9 | 12 | 10 tick |
| `forest_breath`：森林龙毒息 | `2L` | 2 | 4 | 6 | 8 | 10 tick |
| `storm_breath`：海龙闪电息 | `L` | 1 | 2 | 3 | 4 | 10 tick |
| `furnace_heat`：炉息 | `0.5L` | 0.5 | 1 | 1.5 | 2 | 10 tick |

炉息单独使用经验等级门槛 **0、12、32、64**，同样最高 4 级。

假设额外效果将 `S` 提高到 1.5，4 级火息提交伤害就是 `12 × 1.5 = 18`。默认公式不直接读取 `minecraft:generic.attack_damage`；武器或其他模组仍可能通过属性修饰、伤害事件等途径影响结果，不能只看武器面板预测技能扣血。

### 3.3 稳态每秒直接伤害提交速率

完成蓄力、持续命中同一目标时：

```text
每秒直接伤害提交速率 = D × 20 / trigger_rate
```

每 10 tick 一次，即每秒约 2 次：

| 技能 | 1 级 | 2 级 | 3 级 | 4 级 |
| --- | --- | --- | --- | --- |
| 火息 | 6 | 12 | 18 | 24 |
| 森林毒息 | 4 | 8 | 12 | 16 |
| 闪电息 | 2 | 4 | 6 | 8 |
| 炉息 | 1 | 2 | 3 | 4 |

这张表不是实战 DPS 保证。首次伤害动作在达到 `cast_time` 时执行；表中没有摊入蓄力、停手冷却、目标离开范围、伤害免疫或附加状态。持续状态与直接伤害也可能互相受到受伤无敌时间影响。

三种主吐息的默认 `cast_time = 20 tick`（1 秒）、`cooldown = 40 tick`（结束后冷却 2 秒），`continuous_mana_cost = 0.025/tick`（持续扣费速率 0.5 法力/秒）。炉息对应持续扣费为 0.03/tick，即 0.6 法力/秒。以上参数都不是按技能等级递增的 LV。

### 3.4 附加效果也属于技能强度

| 吐息 | 原版附加效果 | 是否随吐息等级改变 |
| --- | --- | --- |
| 火息 | 点燃 `100L tick`，即 5、10、15、20 秒；每次伤害动作另有 30% 概率施加 10 秒 Burn I | 点燃时间随等级增长；Burn 的概率、药水等级、持续时间固定 |
| 森林毒息 | 每次伤害动作有 30% 概率施加 10 秒 Drain I | 这些状态参数固定 |
| 闪电息 | 每次伤害动作有 50% 概率施加 30 秒 Charged I；另有命中方块生成区域云的动作 | 这些状态参数固定，连锁影响总输出 |
| 炉息 | 点燃 `20L tick`，即 1～4 秒；同时具有冶炼用途 | 点燃与冶炼进度随等级增长 |

这些“30%”“50%”是相应动作对目标执行时的概率，不是每秒概率；状态刷新也不等于每次都叠加一个独立的持续伤害实例。

默认配置下，Drain 每 20 tick 尝试造成 1 点伤害；Charged 每 20 tick 对主目标尝试造成 `1 × (amplifier + 1)` 点伤害，并有连锁机制。它们没有直接套用吐息的 `amount(L) × S`。

Burn 的一次伤害与两次记录位置的距离平方有关：`(amplifier + 1) × clamp(移动距离平方, 0, 10)`，默认每 20 tick 判定。它不是“接触吐息越久伤害越高”的机制。

因此，闪电息直接伤害较低，不代表其包含连锁的总战斗效果必然最低。状态效果的实现与配置入口见[状态效果文档](09-status-effects.md)。

## 4. 成长值如何改变范围

### 4.1 阶段与自然成长速度

| 默认阶段 | 成长值 `G` | `ticks_until_grown` | 20 TPS 下长满当前阶段的时间 |
| --- | --- | --- | --- |
| `newborn` | 10 ≤ G < 25 | 216000 | 3 小时 |
| `young` | 25 ≤ G < 40 | 432000 | 6 小时 |
| `adult` | 40 ≤ G ≤ 60 | 1728000 | 24 小时 |

阶段内的增长换算为：

```text
增加的成长值 = (阶段上限 - 阶段下限) / ticks_until_grown × 成长 tick 数
```

从 10 自然长到 60 理想情况下合计 33 小时，前提是自然成长一直允许、服务器保持 20 TPS。停止成长、空间限制和成长物品等会改变过程；成长值并不是累计 tick 数。

### 4.2 阶段属性的输入是“阶段内成长差”

`DSModifiers.updateGrowthModifiers` 传给阶段修饰器的输入是：

```text
x = G - 当前阶段的最小成长值
```

普通 LV 修饰器会先将 `x` 转为整数。在默认阶段范围内 `x` 非负，所以等价于向下取整，记为 `q = floor(x)`。阶段范围属性使用线性 LV，公式仍是 `base + step × (q - 1)`。注意这里输入为 0 是允许的，不能把技能中“1 级起算”的理解直接套成 `base + step × q`。

范围属性本身基础值为 3，再加上当前阶段的 `add_value` 修饰：

```text
newborn：R = 3 + 1.55 + 0.05 × (floor(G - 10) - 1)
           = 4.50 + 0.05 × floor(G - 10)

young：  R = 3 + 3.30 + 0.05 × (floor(G - 25) - 1)
           = 6.25 + 0.05 × floor(G - 25)

adult：  R = 3 + 5.55 + 0.05 × (floor(G - 40) - 1)
           = 8.50 + 0.05 × floor(G - 40)
```

阶段切换会替换修饰器，因此门槛处可以跳变，不是全程一条连续直线。

| 成长值 `G` | 当前阶段 | 范围属性 `R`，约方块 | 同为 4 级森林吐息的单次直接伤害，`S = 1` |
| --- | --- | --- | --- |
| 10 | newborn | 4.50 | 8 |
| 20 | newborn | 5.00 | 8 |
| 25 | young | 6.25 | 8 |
| 30 | young | 6.50 | 8 |
| 40 | adult | 8.50 | 8 |
| 50 | adult | 9.00 | 8 |
| 60 | adult | 9.50 | 8 |

三种主吐息默认 `range_multiplier = 1`，所以标称距离就是 `R`。一般公式为：

```text
标称吐息距离 = range_multiplier(L) × R
```

实际选目标使用视线方向派生的 AABB 包围盒，身体缩放和眼高还会改变盒子的形状。标称距离不应理解为任意方向的精确最远碰撞距离，也不需要再简单乘一次身体缩放。

### 4.3 成长带来的其他战力不等于主吐息增伤

默认阶段还会改变最大生命、身体缩放等属性，并给 `minecraft:generic.attack_damage` 添加 1、2、3 点阶段修饰。它们影响生存或普通攻击；默认主吐息表达式没有将该普攻属性代入伤害。

当前默认阶段、龙种和体型数据没有提供随成长提高 `dragon_ability_damage` 的额外修饰。因此应分别调整“吐息单次伤害”“覆盖区域”“生存能力”，避免把它们合成一个不存在的统一强度倍率。

## 5. 被动和辅助技能：升级不一定增加伤害

### 5.1 爪牙被动：按成长解锁采集能力

三种 `*_claws_and_teeth` 的成长门槛为 0、25、40、60，最高 4 级：

| 成长值 | 技能等级 | `break_speed_multiplier` 附加项 |
| --- | --- | --- |
| 默认起点 10～<25 | 1 | 0.25 |
| 25～<40 | 2 | 0.50 |
| 40～<60 | 3 | 0.75 |
| 60 | 4 | 1.00 |

这项附加量对应自身机制中的 `1 + 附加项`，即 1.25、1.50、1.75、2.00 的破坏速度系数，实际挖掘还受其他规则影响。洞穴龙针对镐类方块，森林龙针对斧类，海龙针对锹类。1 级基础工具档位为 WOOD，2 级及以上为 STONE。

`harvest_bonus` 虽配置为 1、1.5、2、2.5，运行时会转为整数，实际该项返回 1、1、2、2。它不是吐息伤害倍率，也不是按这个数保证增加掉落数量。

### 5.2 魔法被动：花经验点换法力续航

`cave_magic`、`forest_magic`、`sea_magic` 最高 10 级。升到第 `L` 级的单次费用为 `36L` 经验点；从 0 升到 `N` 级总费用为 `18N(N + 1)`，升满 10 级合计 1980 点。

| 每种魔法被动的加成 | 公式 | 10 级加成 |
| --- | --- | --- |
| 最大法力附加量 | `L` | +10 法力 |
| 洞穴龙满足自身环境条件时的回蓝附加量 | `0.03L/tick` | +6 法力/秒 |
| 森林龙满足自身环境条件时的回蓝附加量 | `0.01L/tick` | +2 法力/秒 |
| 海龙满足自身环境条件时的回蓝附加量 | `0.02L/tick` | +4 法力/秒 |

以上是附加量；环境判定各不相同。默认 `ManaHandler` 在施法中跳过自然回蓝，不能直接用上述速率抵掉吐息耗蓝来推算无限持续施法。升级还会消耗玩家经验，可能间接使按经验等级判定的主吐息降级。

### 5.3 其他主动技能体现不同成长维度

| 技能 | 经验等级门槛 | 随技能等级变化的典型字段 |
| --- | --- | --- |
| `spike` | 0、20、30、40 | 投射物数量 `L`，1～4 发；不能据此推断单发伤害也乘 `L` |
| `sturdy_skin` | 0、15、35 | 状态持续 `1200L tick`，即 60、120、180 秒；`amplifier = 0` 固定 |

设计附属时可以沿用这种分工：伤害、持续时间、弹数、采集、法力各按自己的字段成长。

## 6. 对照本工程当前冰息公式

本节是编写文档时 [ice_breath.json](../src/main/resources/data/dswof/dragonsurvival/dragon_ability/ice_breath.json) 的配置快照；后续修改请以实际文件为准。它使用自定义 `dswof:ice_breath_damage`，不是原版 `DamageEffect`。

当前 `base_damage = 2L`、`max_damage = 11 + L`，公式可整理为：

```text
A = 2L
M = 11 + L
B = A × (1 + 0.05G)
C = M × (1 + 0.5L) + 0.35G

D(t) = min(C, B + 8t) × S
```

其中 `t` 是自定义效果记录的累计接触秒数，首击为 0。由于 `growth_per_second = 4`、`growth_interval_ticks = 10`，当前解释是每 10 tick 增加 4，即每秒增加 8；字段历史名称中的 `per_second` 不代表当前每秒只加 4。表达式算完后，`!target.canFreeze()` 的目标再乘 0.5。

下面取 `S = 1`、目标可冻结，只对比直接伤害，不含 `dswof:frost` 状态：

| 假设技能等级 | 成长值 | 冰息首击 | 持续接触后的单次上限 | 同等级原版森林息 / 火息单次 |
| --- | --- | --- | --- | --- |
| 1 | 10 | 3 | 21.5 | 2 / 3 |
| 1 | 40 | 6 | 32 | 2 / 3 |
| 1 | 60 | 8 | 39 | 2 / 3 |
| 4 | 10 | 12 | 48.5 | 8 / 12 |
| 4 | 40 | 24 | 59 | 8 / 12 |
| 4 | 60 | 32 | 66 | 8 / 12 |

**当前冰息 JSON 没有配置 `upgrade`，DS 的 `DragonAbility.getMaxLevel()` 在省略时返回 1。** 上表 4 级是供设计时比较的假设数值，不代表现配置会自然升到 4 级。图标有 1～4 级图片、伤害使用线性 LV，都不会自动添加升级机制。若要和原版主吐息一样按经验等级升到 4 级，可将第 3.1 节的升级对象放入技能顶层 `upgrade`。

假设补上升级规则后达到 `L = 4、G = 60`，当前冰息首击 32、上限 66，每 0.5 秒触发一次。对应直接伤害提交速率从约 64/秒增长到 132/秒；同条件原版森林息为 16/秒，火息为 24/秒。数学上接触 4.25 秒到上限，按 10 tick 的命中采样则首次在接触约 4.5 秒时取到上限。这里没有计入首击前的蓄力时间。

这说明你现在额外叠加了技能等级、龙成长、接触时间三个增伤维度。若目标是接近原版强度，建议先拿原版同级单次伤害作为起点，再给持续接触奖励与伤害上限分配空间；控制效果强时，也应一起考虑，而不能只对比 `base_damage`。

原版内置伤害效果的表达式 Codec 当前只验证 `amount`、`scale`，不应直接把这里的 `growth`、`ability_level` 公式复制进 `dragonsurvival:damage`。本工程自定义效果已声明这些变量，接入方式见[冰息表达式文档](07-ice-breath-expressions.md)。

## 7. 调数值时建议记录哪些量

对同一目标分别固定 `L = 1/2/3/4`、`G = 10/25/40/60`，先记录单次提交伤害，再记录触发间隔、增长速度、上限与最终扣血。把主目标和范围内多个目标分开比较，避免漏算成长扩大范围带来的群体收益。

有命令权限时，可查看当前属性：

```mcfunction
/attribute @s dragonsurvival:dragon_ability_damage get
/attribute @s dragonsurvival:dragon_breath_range get
```

原版主吐息的方便参考点是：**1 级单次火息 3 / 森林息 2 / 闪电息 1；4 级为 12 / 8 / 4，伤害动作都每 10 tick 一次。** 成长主要扩大命中区域，附加状态和其他模组处理需要另测。

## 8. 数据与源码定位

默认数值取自本地实际依赖 `build/research/ds.jar`，JAR 内路径为：

```text
data/dragonsurvival/dragonsurvival/dragon_ability/<技能ID>.json
data/dragonsurvival/dragonsurvival/dragon_stage/<阶段ID>.json
```

以下路径相对于本地源码镜像 `build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/`：

| 源码位置 | 核对内容 |
| --- | --- |
| `registry/dragon/ability/entity_effects/DamageEffect.java` | 默认公式、属性读取、表达式验证和 `hurt` 调用 |
| `registry/dragon/ability/upgrade/ExperienceLevelUpgrade.java` | 根据当前经验等级重新计算技能等级 |
| `registry/dragon/ability/upgrade/DragonGrowthUpgrade.java` | 根据成长输入重新计算技能等级 |
| `registry/dragon/ability/upgrade/ExperiencePointsUpgrade.java` | 支付经验点与升级费用 |
| `registry/attachments/MagicData.java` | 经验等级的自动升级检查 |
| `registry/dragon/ability/DragonAbility.java` | 省略升级配置时默认最高等级为 1 |
| `registry/DSAttributes.java` | 技能伤害倍率基础值 1、范围基础值 3 |
| `registry/DSModifiers.java`、`common/codecs/Modifier.java` | 阶段内成长差、普通 LV 整数输入与精确修饰器 |
| `registry/dragon/stage/DragonStage.java` | 阶段成长时间换算 |
| `registry/dragon/ability/targeting/DragonBreathTarget.java` | 范围乘数与包围盒计算 |
| `common/codecs/HarvestBonus.java` | 爪牙采集加成的整数转换 |
| `common/handlers/magic/ManaHandler.java` | 施法时停止自然回蓝 |
| `common/effects/DrainEffect.java`、`BurnEffect.java`、`ChargedEffect.java` | 附加状态的独立伤害逻辑 |

本文进行了 JAR 数据与源码的静态核对和公式计算，没有将表格当作游戏实测结果。服务器配置、覆盖数据包、装备和其他模组变化后，应重新读取实际属性并实测最终扣血。
