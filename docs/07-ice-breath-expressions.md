# 冰息自定义效果的伤害表达式

适用于当前工程的 Dragon Survival 2.0.71 / Minecraft 1.21.1。实现见 [IceBreathAbilityEntityEffect.java](../src/main/java/org/wgx/dswof/dragonsurvival/dragon_ability/IceBreathAbilityEntityEffect.java)，技能配置见 [ice_breath.json](../src/main/resources/data/dswof/dragonsurvival/dragon_ability/ice_breath.json)。

原版吐息的等级伤害、成长范围以及本工程当前冰息公式的数值对照，见[技能等级、成长值与强度数值参考](10-ability-scaling-and-balance.md)。

## 1. 如何接入 DS 的表达式系统

DS 内置 `DamageEffect` 使用 `by.dragonsurvivalteam.dragonsurvival.util.Expression` 求值，使用 `common.codecs.MiscCodecs.expressionCodec(...)` 从 JSON 读取并验证公式。附属可以直接复用这两个类，不需要修改 DS。

实现分为三步：

1. 给效果 record 增加 `Expression expression`；需要和内置伤害一样选择缩放属性时，再增加 `Holder<Attribute> scale`。
2. 在 MapCodec 中使用 `MiscCodecs.expressionCodec("amount", "scale", ...)` 声明全部允许的变量，再通过 `optionalFieldOf("expression", DEFAULT_EXPRESSION)` 读取公式。
3. 每次造成伤害前，使用 `expression.setVariable(name, BigDecimal.valueOf(value))` 设置全部变量，再调用 `expression.eval().floatValue()`。

关键点是：**加载时声明的名字与运行时注入的名字必须一致。** DS 内置 `damage` 当前只用 `amount`、`scale` 做加载验证，是其实现的限制。附属的 `ice_breath_damage` 使用自己的 Codec，可以声明更多变量，不受这两个名字限制；这不会修改 DS 内置 `damage` 的行为。

`Expression` 是可变对象。当前实现只在服务端主线程计算，每次求值都覆盖全部变量。不要把同一个 Expression 交给异步线程，也不要在客户端提示计算时并发修改同一对象。

## 2. JSON 用法

下面是 `entity_effect` 数组中的一项，不是完整技能文件：

```json
{
  "effect_type": "dswof:ice_breath_damage",
  "damage_type": "dswof:ice_breath",
  "base_damage": 2.0,
  "growth_per_second": 4.0,
  "growth_interval_ticks": 10,
  "max_damage": 12.0,
  "freeze_immune_multiplier": 0.5,
  "reset_after_ticks": 10,
  "expression": "MIN(max_damage, amount + growth_per_unit * exposure_ticks / growth_interval_ticks) * scale"
}
```

`scale` 配置可省略，默认使用 DS 的 `DRAGON_ABILITY_DAMAGE` 属性。它的值从施法者读取。这里的 `scale` 字段是属性 ID，公式中的 `scale` 是该属性在当前玩家身上的数值。

为了兼容工程中已有的 JSON，`growthPerUnit` 仍从历史字段 `growth_per_second` 读取。**当前默认公式把这个值解释为每 `growth_interval_ticks` 刻增加的伤害。** 因此上例每 10 tick 增加 4 点基础伤害；如果希望每秒增加 4 点，把 `growth_interval_ticks` 改成 20。这个增长间隔不控制伤害触发频率；实际调用频率仍由动作的 `trigger_rate` 决定。

省略 `expression` 时也使用上述公式。`base_damage`、`growth_per_second` 和 `max_damage` 仍可以使用 LevelBasedValue，先按当前技能等级求出数值，再传入公式。

## 3. 可用变量

| 变量 | 含义 |
| --- | --- |
| `amount` | `base_damage` 按当前技能等级算出的值 |
| `scale` | 施法者的所选属性值，默认是龙技能伤害属性 |
| `ability_level` | 当前技能等级 |
| `experience_level` | 施法者当前经验等级 |
| `experience_points` | 根据施法者当前等级和经验进度计算的经验总点数，与 DS 内置伤害一致 |
| `growth` | 施法者的龙成长值 |
| `current_movement_speed` | 施法者当前位移速度向量的长度，单位约为格/tick，和内置伤害一致 |
| `growth_per_unit` | JSON 的 `growth_per_second` 按技能等级算出的值，当前作为每增长间隔增加的伤害 |
| `growth_interval_ticks` | 一个增长间隔的 tick 数，至少为 1 |
| `max_damage` | JSON 的 `max_damage` 按技能等级算出的值 |
| `exposure_ticks` | 记录的连续接触时间，首次检测命中为 0 |
| `exposure_seconds` | `exposure_ticks / 20`，按游戏刻换算的秒数 |

前七个变量与 DS 内置 `DamageEffect` 的运行时含义一致。其余变量由附属提供。

目标检测是按 `trigger_rate` 采样的；当前计时会容忍不超过 `reset_after_ticks` 的检测间隔。它不是对每个粒子碰撞的精确计时。计时不会再被截断到一个 `growth_interval_ticks`，因此长时间接触可以继续影响公式。

## 4. 改写公式

只按基础伤害和技能属性计算：

```json
"expression": "amount * scale"
```

每接触一秒增加 2 点，最多增加到 `max_damage`，再乘伤害属性：

```json
"expression": "MIN(max_damage, amount + exposure_seconds * 2) * scale"
```

每满一个间隔才增加一次，而不是在两次间隔之间按比例增加：

```json
"expression": "MIN(max_damage, amount + growth_per_unit * FLOOR(exposure_ticks / growth_interval_ticks)) * scale"
```

按技能等级和成长值增加伤害：

```json
"expression": "(amount + ability_level * 0.5 + growth * 0.01) * scale"
```

这里使用 DS 的表达式语法，支持 `MIN`、`MAX`、`FLOOR` 等函数，不是 Java 代码；不能写 `Math.min(...)`、调用目标方法或自行访问未注入的变量。

## 5. 公式和 Java 的责任

当前伤害处理顺序：

```text
接触时间与等级参数 → expression 求值 → 冰冻免疫倍率 → target.hurt(...)
```

`max_damage` 和 `scale` 是供公式使用的变量。自定义公式若没有引用它们，就不会自动获得封顶和属性缩放。默认公式在乘 `scale` 之前封顶；如果希望封顶包含缩放结果，写成 `MIN(max_damage, (amount + growth_per_unit * exposure_ticks / growth_interval_ticks) * scale)`。

冻结免疫倍率由 Java 在公式之后统一应用：`!target.canFreeze()` 时乘 `freeze_immune_multiplier`。因此，即使公式只写 `amount`，减半规则仍会生效。这里判断的是目标当前能否被原版冻结，不是普通 MobEffect。

表达式的结果是传给 `hurt()` 的请求伤害，护甲、无敌帧、抗性、DS 伤害修改和其他事件仍可能影响实际扣血量。时间按处于吐息检测范围计算，不取决于 `hurt()` 本次是否成功扣血。

DS 的 `expressionCodec` 在加载时给变量临时赋随机值并尝试求值，因此通过加载验证不保证运行时一定可计算。例如 `amount / exposure_ticks` 在首次命中时会除零。需要安全分母时可以写 `amount / MAX(1, exposure_ticks)`。当前实现遇到求值异常会跳过本次伤害，并在本次服务器运行中对同一公式记录一次警告；非有限值和非正数不会传给 `hurt()`。

## 6. 相关修正

`entityCodec()` 必须返回效果的 `CODEC`，以便 DS 编码、同步效果配置。`ServerLevel` 由服务器管理，读取世界时不能使用 try-with-resources，否则会调用世界的 `close()`。自定义效果的注册事件属于 MOD 总线，接触状态的 tick 清理属于 GAME 总线；当前加载器会按事件类型自动选择总线，使用 `@EventBusSubscriber(modid = DsWof.MODID)` 即可，无需显式设置已弃用的 `bus`。

## 7. 原版冻结、Jade 与假人验证

原版冻结使用实体的 `TicksFrozen`，不是药水效果。当前使用的 Jade 版本中，`StatusEffectsProvider` 读取 `getActiveEffects()`，因此不会自动在药水状态列表里显示这种冻结。普通生物也不会因为设置冻结值就出现玩家视角的屏幕冰霜覆盖。

当前冰息每次命中增加 30 点冻结值，上限为 `getTicksRequiredToFreeze() + 40`（通常为 180）；不会降低其他效果已设置的更高冻结值。离开细雪时，原版 `LivingEntity.aiStep()` 每 tick 先减少 2 点冻结值，然后在实体 `tickCount % 40 == 0`、完全冻结且 `canFreeze()` 时尝试造成 1 点原版冻结伤害。如果只封顶到完全冻结阈值 140，减少后立即低于阈值，就可能一直没有额外冻结伤害。额外 40 点用于覆盖当前每 10 tick 一次的命中间隔；以后修改触发间隔时需要重新考虑增长量与上限。

这部分原版冻结伤害使用 `minecraft:freeze`，独立于 `dswof:ice_breath` 的表达式、伤害上限和施法者归属，并仍会受到无敌帧及其他伤害处理的影响；持续冰息造成的无敌帧也可能挡住这笔较小伤害。

本地 MMM 假人类没有重写 `canFreeze()`，但装备、实体类型标签和其他模组仍可能影响结果。建议先用没有装备的牛排查。开启命令权限后，在附近执行：

```mcfunction
/summon minecraft:cow ~ ~ ~ {Tags:["dswof_freeze_test"],PersistenceRequired:1b}
/data get entity @e[type=minecraft:cow,tag=dswof_freeze_test,sort=nearest,limit=1] TicksFrozen
```

持续吐息数秒后再次查询，应看到冻结值积累；停止命中后会逐渐衰减。需要隔离测试原版冻结伤害时，可在停止吐息后执行：

```mcfunction
/data merge entity @e[type=minecraft:cow,tag=dswof_freeze_test,sort=nearest,limit=1] {TicksFrozen:220}
```

观察后续约两秒的生命变化，排除持续吐息无敌帧的干扰。需要在 Jade 显示“冻结值/完全冻结”时，应另写 Jade 实体信息提供器读取 `getTicksFrozen()`，不能通过修改 `dragonsurvival:potion` 的 ID 实现。
