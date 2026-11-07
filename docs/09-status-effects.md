# DS 状态效果：注册、施加与实现

适用版本：Minecraft **1.21.1**、NeoForge **21.1.256**、Dragon Survival **2.0.71**。本文依据本工程的 DS 源码和 Minecraft 开发依赖整理。

本文解释 DS 原版实现，并给出附属的教学示例。文中的 `ModEffects`、`FrostEffect` 和 `dswof:stacking_frost` 是建议新增的内容，本文没有将它们接入游戏；现有冰息仍以 `src/main/` 实际代码为准。

## 1. 先区分三个概念

| 概念 | 示例 | 定义方式 | 职责 |
| --- | --- | --- | --- |
| 状态类型 `MobEffect` | `dragonsurvival:drain` | Java 注册到 `minecraft:mob_effect` | 定义一种状态的行为、属性修饰、颜色和解除规则 |
| 某个目标的状态实例 `MobEffectInstance` | 某只牛身上的Ⅰ级 Drain，剩余 200 tick | 运行时创建并交给目标 | 保存该目标的剩余时间、等级、粒子和图标开关 |
| 伤害类型 `DamageType` | `dragonsurvival:forest_breath` | `data/<命名空间>/damage_type/<名称>.json` | 定义伤害分类的元数据；伤害标签另行配置 |

`dragonsurvival:drain` 来自 Java 状态注册，不是 `damage_type` 下的 JSON。状态可以造成伤害，也可以只减速；创建状态本身不要求创建伤害类型。只有实际调用伤害逻辑时，才需要选择相应的伤害类型。

另外，`AbilityEntityEffect` 是“技能对选中的实体执行什么操作”的接口，与 `MobEffect` 是两个不同的概念。一次实体效果可以造成直接伤害、点燃目标，也可以给目标添加状态。

## 2. DS 怎样注册状态

源码：[DSEffects.java](../build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/DSEffects.java)。以下摘录省略了翻译注解和其他注册项：

```java
public class DSEffects {
    public static final DeferredRegister<MobEffect> REGISTRY =
            DeferredRegister.create(
                    BuiltInRegistries.MOB_EFFECT,
                    DragonSurvival.MODID
            );

    public static Holder<MobEffect> DRAIN = REGISTRY.register(
            "drain",
            () -> new DrainEffect(MobEffectCategory.HARMFUL, 0x0, false)
    );
}
```

各部分的含义：

| 代码 | 作用 |
| --- | --- |
| `BuiltInRegistries.MOB_EFFECT` | 注册目标是状态效果注册表 |
| `DragonSurvival.MODID` | 使用 `dragonsurvival` 命名空间 |
| `register("drain", ...)` | 注册名称为 `drain`，完整 ID 为 `dragonsurvival:drain` |
| `() -> new DrainEffect(...)` | 在注册阶段创建状态类型对象 |
| `Holder<MobEffect>` | 对已注册状态的引用，可用于构造实例和查询目标状态 |

只声明这个类还不够。DS 的 [DragonSurvival.java](../build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/DragonSurvival.java) 构造函数还执行了：

```java
DSEffects.REGISTRY.register(bus);
```

这里的 `bus` 是模组事件总线。它把延迟注册器连接到加载流程；不是每次施放技能时重新注册，也不能靠 `/reload` 新增 Java 状态类型。

## 3. ModifiableMobEffect 增加了什么

源码：[ModifiableMobEffect.java](../build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/effects/ModifiableMobEffect.java)。核心结构为：

```java
public class ModifiableMobEffect extends MobEffect {
    private final boolean incurable;

    public ModifiableMobEffect(MobEffectCategory type, int color, boolean incurable) {
        super(type, color);
        this.incurable = incurable;
    }

    @Override
    public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance instance) {
        if (incurable) {
            cures.clear();
        } else {
            super.fillEffectCures(cures, instance);
        }
    }
}
```

构造参数的含义：

| 参数 | 含义 |
| --- | --- |
| `type` | 状态类别：`HARMFUL` 有害、`BENEFICIAL` 有益、`NEUTRAL` 中立 |
| `color` | 状态颜色，通常写 RGB 十六进制整数；不会自动生成图标或屏幕特效 |
| `incurable` | 是否清空常规解除途径，例如牛奶；`true` 不等于无法被命令或 Java 移除 |

这个父类没有实现“时间叠加”“强度叠加”“冻结伤害”。需要什么行为，就在子类或施加效果的代码中实现。附属可以继承它，也可以直接继承原版 `MobEffect`。

## 4. DS 吐息怎样施加 Drain

森林吐息中的两个对象是两项独立操作：

```json
{
  "entity_effect": [
    {
      "effect_type": "dragonsurvival:damage",
      "damage_type": "dragonsurvival:forest_breath",
      "amount": {
        "type": "minecraft:linear",
        "base": 2.0,
        "per_level_above_first": 2.0
      }
    },
    {
      "effect_type": "dragonsurvival:potion",
      "potion": {
        "effects": "dragonsurvival:drain",
        "amplifier": 0.0,
        "duration": 200.0,
        "probability": 0.3
      }
    }
  ]
}
```

第一个对象请求一次直接伤害。第二个对象尝试附加状态；之后的 Drain 伤害由状态自身执行。它们不共享同一个伤害公式。

完整流程：

```text
龙息目标选择找到实体
  → applied_effects.entity_effect 按配置执行
  → effect_type 为 dragonsurvival:potion
  → PotionEffect.apply(dragon, ability, target)
  → PotionData.apply(dragon, ability.level(), target)
  → target.addEffect(new MobEffectInstance(...), dragon)
  → Minecraft 保存实例、倒计时，并调用状态行为
```

[PotionEffect.java](../build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/entity_effects/PotionEffect.java) 是桥接层：通过 `PotionData.CODEC` 读取 `potion`，再在 `apply()` 中转交 `potion.apply(...)`。

[PotionData.java](../build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/codecs/PotionData.java) 负责以下逻辑：

1. 只处理 `LivingEntity`，对 `effects` 中的每一种状态分别处理。
2. 根据技能等级计算 `amplifier`、`duration`、`probability`。
3. 如果已有状态的等级和剩余时间都不少于本次配置值，就跳过。
4. 进行概率检查。
5. 瞬时状态调用 `applyInstantenousEffect(...)`；普通持续状态调用 `addEffect(...)`。

### 4.1 potion 参数

| 字段 | 类型 / 默认值 | 含义 |
| --- | --- | --- |
| `effects` | 必填，状态 ID、ID 数组或状态标签 | 指向已注册的 `MobEffect`，不是伤害类型或药水物品 |
| `amplifier` | 必填，`LevelBasedValue` | 0 对应Ⅰ级，1 对应Ⅱ级；计算后转为整数 |
| `duration` | 必填，`LevelBasedValue` | 持续时间，单位 tick；200 是 10 秒，计算后转为整数 |
| `probability` | `LevelBasedValue`，默认 1 | 每次执行的施加概率；正常使用范围为 0～1 |
| `effect_particles` | 布尔值，默认 `false` | 控制状态实例的普通状态粒子显示 |
| `show_icon` | 布尔值，默认 `true` | 控制状态实例的图标显示；Jade 还受其自身设置影响 |

前三种数值配置和 `probability` 使用等级数值 Codec，可以写常量或 `minecraft:linear` 等对象，不会因为它们是数值字段就自动支持公式字符串。JSON 的 `duration` 单位是 tick；DS 的 Java `PotionData.Builder.duration(int)` 接受秒，两者不要混淆。

### 4.2 这里不会自动叠加时间或等级

假设已有Ⅰ级状态、剩余 150 tick，再次提交Ⅰ级、200 tick 的实例：普通合并通常将其刷新到 200 tick，而不是增加到 350 tick。再次提交Ⅰ级状态也不会变成Ⅱ级。

不同强度和时长还可能触发 Minecraft 的隐藏状态合并规则。因此，仅修改 JSON 的 `effects` 为自定义状态 ID，不能实现“每次命中加时间、加等级”。

## 5. DrainEffect 怎样执行状态行为

源码：[DrainEffect.java](../build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/effects/DrainEffect.java)。核心方法：

```java
@Override
public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
    return duration % 20 == 0;
}

@Override
public boolean applyEffectTick(LivingEntity entity, int amplifier) {
    // DS 在这里处理粒子、施加者、PVP 检查和伤害。
    // 具体实现见上游 DrainEffect.java。
    return super.applyEffectTick(entity, amplifier);
}
```

Minecraft 在服务端推进持续状态时，先询问 `shouldApplyEffectTickThisTick(...)`，返回 `true` 才调用 `applyEffectTick(...)`。当前版本 `applyEffectTick` 返回 `false` 会请求移除状态；返回 `true` 保留状态并继续正常倒计时。纯属性减速不要求重写这两个方法。

原版 Drain 的具体行为如下：

- 以剩余时间 `% 20` 判断执行时点；正常倒计时约每秒一次。重新刷新持续时间也可能改变执行节奏，不是一个与刷新无关的独立计时器。
- 对非龙目标发送自定义毒气粒子。这是类里额外写的粒子行为，不由 `effect_particles` 完全控制。
- 通过 `AdditionalEffectData` 尝试取得施加者，并检查玩家之间的 PVP 规则。
- 使用 `DSDamageTypes.DRAIN` 构造伤害来源，调用 `entity.hurt(...)`。
- 当前默认 `damage` 配置为 1，实际伤害代码没有按 `amplifier` 乘倍数；不是所有状态的等级都会自动放大伤害。

这里体现了两个独立的 ID：`DSEffects.DRAIN` 引用状态，`DSDamageTypes.DRAIN` 引用伤害类型。即使资源位置碰巧都叫 `dragonsurvival:drain`，它们也属于不同注册表。

### 5.1 施加者从哪里来

`PotionData` 使用的是双参数版本：

```java
target.addEffect(instance, dragon);
```

DS 的 [EffectHandler.java](../build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/handlers/magic/EffectHandler.java) 监听 `MobEffectEvent.Added`，通过 `AdditionalEffectData` 给传入实例记录事件中的来源。`DrainEffect` 再读取来源，用于后续伤害。

附属施加状态时也应传入 `dragon`。但同一种状态被多次合并、多个玩家共同叠加以及保存读档后的归属，不应仅凭这个参数就假定始终正确。如果自定义持续伤害要求严格的击杀归属，需要检查合并后目标实际持有的实例，并明确来源覆盖规则。

## 6. 在附属中仿写状态注册

下面是教学用的最小类；包名按本工程组织。它们需要手动创建后才能使用。

### 6.1 ModEffects：注册状态类型

建议路径：`src/main/java/org/wgx/dswof/registry/ModEffects.java`。

```java
package org.wgx.dswof.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.wgx.dswof.DsWof;
import org.wgx.dswof.effect.FrostEffect;

public final class ModEffects {
    public static final DeferredRegister<MobEffect> REGISTRY =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, DsWof.MODID);

    public static final DeferredHolder<MobEffect, FrostEffect> FROST =
            REGISTRY.register("frost", FrostEffect::new);

    private ModEffects() {}
}
```

在 `DsWof` 的模组构造函数中连接注册器。下方只展示需要加入的构造逻辑，导入 `IEventBus` 和 `ModEffects` 后合并到现有类中：

```java
public DsWof(IEventBus modEventBus) {
    ModEffects.REGISTRY.register(modEventBus);
}
```

最终状态 ID 为 `dswof:frost`。若类中已有模组构造函数，把注册语句加进去即可。

### 6.2 FrostEffect：定义状态自身的减速

建议路径：`src/main/java/org/wgx/dswof/effect/FrostEffect.java`。

```java
package org.wgx.dswof.effect;

import by.dragonsurvivalteam.dragonsurvival.common.effects.ModifiableMobEffect;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.wgx.dswof.DsWof;

public final class FrostEffect extends ModifiableMobEffect {
    public FrostEffect() {
        super(MobEffectCategory.HARMFUL, 0x8BD9FF, false);

        addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                ResourceLocation.fromNamespaceAndPath(DsWof.MODID, "frost_slow"),
                -0.10,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );
    }
}
```

原版状态属性模板按 `amplifier + 1` 缩放基础修饰量，所以Ⅰ级对应 -10%，Ⅴ级对应 -50%。同一种状态升级时会替换其同 ID 修饰，不是再把Ⅰ～Ⅴ级所有修饰重复叠上去。最终移动速度仍受其他属性修饰影响。

状态的注册对象由所有目标共用。不要在 `FrostEffect` 中声明可变的 `remainingTicks`、`currentLevel` 或 `target` 来记录每个目标；这些信息属于目标的 `MobEffectInstance` 或专门的实体数据。

### 6.3 普通施加：可直接复用 DS potion

注册成功后，下面的实体效果配置可以施加固定Ⅰ级、4 秒的自定义状态：

```json
{
  "effect_type": "dragonsurvival:potion",
  "potion": {
    "effects": "dswof:frost",
    "amplifier": 0.0,
    "duration": 80.0,
    "probability": 1.0,
    "effect_particles": true,
    "show_icon": true
  }
}
```

这能复用 DS 的普通状态施加，但不会实现你要求的双重叠加，也不会自动检查 `canFreeze()`。自定义状态叫“冰冻”，并不意味着原版或冰翼龙的冻结免疫就会自动适用于它。

## 7. 时间和等级叠加应该放在哪里

你的需求应再新增一个 `StackingFrostEntityEffect implements AbilityEntityEffect`，在它的 `apply()` 中处理叠加，然后交给 `MobEffectInstance` 管理倒计时：

```text
ModEffects：注册 dswof:frost
FrostEffect：定义每级减速幅度、状态自身行为
StackingFrostEntityEffect：每次命中增加时间和等级，并限制最大值
```

以下是 `apply()` 的核心教学片段，假设可以引用前文的 `ModEffects`、`LivingEntity` 和 `MobEffectInstance`；不是带 Codec 和注册的完整类：

```java
if (!(entity instanceof LivingEntity target) || !target.canFreeze()) {
    return;
}

MobEffectInstance old = target.getEffect(ModEffects.FROST);
// 外部命令施加的无限时长状态保留，不纳入本示例的有限叠加。
if (old != null && old.isInfiniteDuration()) {
    return;
}

int oldDuration = old == null ? 0 : old.getDuration();
int oldLevel = old == null ? 0 : old.getAmplifier() + 1;
int duration = (int) Math.min(80L, (long) oldDuration + 20);
int level = Math.min(5, oldLevel + 1);

// 通过正常 addEffect 路径刷新，避免直接改实例后遗漏属性更新和同步。
target.addEffect(new MobEffectInstance(
        ModEffects.FROST, duration, level - 1,
        false, true, true
), dragon);
```

示例约定：每次命中增加 20 tick（1 秒），最多剩余 80 tick（4 秒）；每次增加一级，最多Ⅴ级。`level` 从 1 开始，传给实例时用 `level - 1` 转成从 0 开始的 `amplifier`。正常状态都由这段逻辑创建时，等级和时间保持在上限内；外部施加的更强或更长状态仍会受到 Minecraft 原有合并规则影响，不保证强行压回上限。

本工程已通过 `canFreeze()` 兼容冰翼龙免疫，在这里检查即可阻止吐息给免疫目标叠加状态。若要求所有来源（包括命令和其他技能）都不能给冰翼龙施加 `dswof:frost`，还应在 `MobEffectEvent.Applicable` 中针对该状态统一拦截。变身前已有状态的解除也需要另行处理。

自定义实体效果需要定义自己的 `MapCodec`，再仿照现有 [AbilityRegistry.java](../src/main/java/org/wgx/dswof/registry/dragonsurvival/AbilityRegistry.java) 注册到 `AbilityEntityEffect.REGISTRY_KEY`。注册之后才能在技能 JSON 中使用对应的 `effect_type`。例如可以设计：

```json
{
  "effect_type": "dswof:stacking_frost",
  "duration_per_hit": 20.0,
  "max_duration": 80.0,
  "levels_per_hit": 1,
  "max_level": 5
}
```

这是待实现的参数设计，不是 DS 已提供的内置字段。每次命中指一次服务端目标检测，不能按客户端粒子数量叠加。多人吐息会向同一个目标状态共同叠加；如果需要每个施法者独立计时，应另存状态数据。

## 8. 显示、验证与常见误区

在 `assets/dswof/lang/zh_cn.json` 中合并翻译项：

```json
{
  "effect.dswof.frost": "冰冻"
}
```

### 8.1 `MobEffect` 状态图标

状态注册本身没有填写纹理路径的字段。Minecraft 客户端会根据状态的完整 ID，按约定查找贴图：

```text
src/main/resources/assets/dswof/textures/mob_effect/frost.png
```

它对应 Java 注册的 `dswof:frost`。通常使用带透明背景的 18×18 PNG；`new FrostEffect(..., color, ...)` 中的 `color` 只控制状态颜色，不会生成图标，也不能替代 PNG。

状态名称仍需在语言文件中提供：

```json
{
  "effect.dswof.frost": "冰冻"
}
```

通过 DS 的 `potion` 实体效果施加时，`show_icon` 必须保持为 `true` 才会显示在原版状态列表中；`show_particles` 只控制粒子，不能用来指定图标。`MobEffectInstance` 的 `showIcon` 为 `false` 时，即使 PNG 存在也不会显示。

### 8.2 DS 属性实例（`ModifierEffect`）的图标

如果你说的“状态属性”是技能 JSON 中 `dragonsurvival:modifier` 创建的持续属性实例，它不是 `MobEffect`，因此不会使用 `textures/mob_effect/`。在该实例的 `base` 中填写 `custom_icon`，并确保 `is_hidden` 不是 `true`：

```json
{
  "base": {
    "id": "dswof:heat_resistance",
    "custom_icon": "dswof:textures/ability_effect/heat_resistance.png",
    "is_hidden": false,
    "should_remove_automatically": true
  },
  "modifiers": [
    {
      "attribute": "dswof:heat_resistance_time",
      "amount": 200.0,
      "operation": "add_value"
    }
  ]
}
```

对应文件为：

```text
src/main/resources/assets/dswof/textures/ability_effect/heat_resistance.png
```

`custom_icon` 是完整资源 ID，包含 `textures/` 和 `.png`。你当前示例中的 `"is_hidden": true` 会隐藏 DS 的持续效果提示；改为 `false` 后，图标才会出现在 DS 修改后的状态提示列表中。属性自身（例如 `dswof:heat_resistance_time`）没有单独的图标字段；它的名称来自 `attribute.dswof.heat_resistance_time` 翻译，图标由 `base.custom_icon` 提供。`attribute` 中填写的 ID 必须已经注册；图标不会替代属性注册。

### 8.3 技能顶层图标不是状态图标

技能 JSON 顶层的 `icon.texture_entries[].texture_resource` 用于技能菜单和技能效果提示。它采用 GUI sprite 规则，例如：

```json
"icon": {
  "texture_entries": [
    {
      "from_level": 0,
      "texture_resource": "dswof:abilities/heat_resistance_1"
    }
  ]
}
```

对应文件是 `assets/dswof/textures/gui/sprites/abilities/heat_resistance_1.png`，配置中省略 `textures/` 和 `.png`。这与 `MobEffect` 的 `textures/mob_effect/` 和持续实例的 `base.custom_icon` 是三套不同的路径规则。

自定义 `MobEffect` 可以进入 Jade 的状态列表，具体显示取决于 Jade 的设置和目标支持情况；它不会自动显示精确的减速百分比或额外屏幕特效。

注册并重启游戏后，可在允许命令的测试世界执行：

```mcfunction
/effect give @s dswof:frost 10 0
/effect clear @s dswof:frost
```

`/effect give` 的时间参数是秒，与 DS JSON 的 tick 单位不同。先确认状态名称、图标、减速和移除正常，再测试吐息叠加、最大值、脱离后倒计时、冰翼龙免疫及多人共同命中。纯文档示例没有经过游戏验证。

| 常见疑问 | 正确处理 |
| --- | --- |
| 在 `damage_type` 里添加 JSON，能创建状态吗？ | 不能；状态需 Java 注册 `MobEffect` |
| 继承 `ModifiableMobEffect` 会自动累加时间吗？ | 不会；叠加放在施加者的实体效果中 |
| `amplifier` 会自动放大所有逻辑吗？ | 不会；属性模板会按等级缩放，自定义伤害代码需显式使用等级 |
| 纯减速需要每 tick 重写速度吗？ | 不需要；优先使用状态属性修饰，原版负责更新和移除 |
| 用普通 `dragonsurvival:potion` 就能自动增加等级吗？ | 不会；需要自定义实体效果 |
| 新冰冻状态会自动产生原版冻结伤害吗？ | 不会；`MobEffect` 和 `TicksFrozen` 是独立机制 |

若后续采用自定义减速状态，应决定是否保留现有冰息写入 `TicksFrozen` 的逻辑，避免两套减速和伤害同时生效。原版冻结的排查见[冰息表达式与冻结验证](07-ice-breath-expressions.md#7-原版冻结jade-与假人验证)，冰翼龙规则见[冻结免疫文档](08-freezing-immunity.md)。
