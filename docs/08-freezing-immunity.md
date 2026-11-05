# 冰翼龙：冻结免疫与冰息减伤

适用版本：Minecraft 1.21.1、NeoForge 21.1.256、Dragon Survival 2.0.71。

## 预期行为

仅当玩家当前处于 `dswof:ice_wings_dragon` 龙形态时生效。切回人类或其他龙种后，恢复原有规则。

| 来源 | 冰翼龙的表现 |
| --- | --- |
| 细雪或技能写入原版冻结积累值 `ticksFrozen` | 积累值保持 0，不产生由该值驱动的冰霜画面和冻结减速 |
| 原版 `minecraft:freeze`，以及其他带 `minecraft:is_freezing` 标签的伤害 | 取消伤害；下述冰息直接伤害除外 |
| 本附属 `dswof:ice_breath` 的直接伤害 | 在技能公式计算后乘以 `freeze_immune_multiplier`，当前为 0.5；之后仍正常经过护甲等伤害处理 |
| 冰息附带的原版冻结积累 | 免疫 |
| 其他模组自行实现的冰冻 MobEffect、禁锢或画面 | 不会仅因名称包含“冰冻”而自动免疫，需按该模组的实际效果另行兼容 |

例如，同样条件下冰息公式算出 10 点伤害，普通可冻结目标进入伤害流程时为 10 点，冰翼龙为 5 点。最终扣血还取决于护甲、抗性、难度及其他模组。

## 实现分工

1. `src/main/java/org/wgx/dswof/dragonsurvival/IceWingFreezingImmunity.java` 统一检查玩家的当前龙种，并通过 `LivingIncomingDamageEvent` 取消冻结标签伤害。对 `dswof:ice_breath` 明确放行，减伤由冰息技能执行，避免重复减半。
2. `src/main/java/org/wgx/dswof/mixin/LivingEntityMixin.java` 使冰翼龙的 `canFreeze()` 返回 `false`。细雪无法正常积累冻结值，冰息进入已有的免疫目标减伤分支。
3. `src/main/java/org/wgx/dswof/mixin/EntityMixin.java` 将冰翼龙通过 `setTicksFrozen(int)` 写入的值改为 0，覆盖技能直接设置冻结值的情况。变身前已有的冻结值也会在原版下一次更新时清除，原版随后更新冻结减速。
4. `IceBreathAbilityEntityEffect.apply()` 已有 `!target.canFreeze()` 判断：伤害乘以倍率，并跳过增加冻结积累值。倍率在 `data/dswof/dragonsurvival/dragon_ability/ice_breath.json` 配置。

## 伤害标签的选择

本需求下，保持 `dswof:ice_breath` 为独立伤害类型，不把它加入 `minecraft:is_freezing` 标签。先前为 CelestialArtifacts 冰冻之戒提出的加标签方案，适用于“戒指完全免疫冰息直接伤害”的另一种需求。

冰冻之戒按标签取消伤害，不会自动阻止原版冻结积累，也不会自动使 `canFreeze()` 为 false。本附属的冰翼龙使用自己的龙种免疫判断，不依赖该戒指或 L2DamageTracker。

如果外部数据包仍为冰息添加冻结标签，本附属自己的免疫事件会放行冰息，但其他模组（例如冰冻之戒）仍可能取消它。

## 游戏内验收

使用生存模式测试；创造、旁观、皮革护甲及其他防御饰品可能影响结果。比较冰息时保持施法者、技能等级、属性、连续命中时长、护甲和抗性一致。

1. 人类形态进入细雪，应能正常积累冻结值；变为冰翼龙后，冻结值应在下一次更新时归零，冰霜画面与冻结减速随之解除。
2. 冰翼龙站在细雪中，应不再积累冻结值。执行 `/damage @s 4 minecraft:freeze` 应不扣血。
3. 让另一名玩家分别对无护甲的人类目标、冰翼龙目标释放相同冰息；冰翼龙保留直接伤害，其技能传入伤害流程的值为一半，且不积累冻结。
4. 切回人类或其他龙种，重复细雪和冻结伤害测试，确认不再享有冰翼龙的免疫。
5. 在专用服务器连接客户端复测，以检查冻结值同步和画面表现。

编译与资源处理只能检查代码和资源打包；以上游戏行为需要启动游戏验收。
