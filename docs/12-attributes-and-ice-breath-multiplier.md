# 属性注册、类型参数与冰息专属倍率

适用基线：Minecraft **1.21.1**、NeoForge **21.1.256**、Dragon Survival **2.0.71**、Java **21**。依据本工程依赖、DS 对应版本源码和 NeoForge sources JAR 整理。

本文提供接入步骤与可复制片段。示例尚未写入生产代码，也未经过游戏验证。当前 `DsWofAttributes` 中的 `ICE_BREATH_MULTIPLIER` 只有 `static final` 声明，没有赋值；完成初始化后才能编译。

## 1. 先区分属性、状态和修饰实例

`dswof:ice_breath_multiplier` 应注册为 **Attribute 属性**，建议使用 `PercentageAttribute`，默认值为 `1.0`。炎热时通过 DS 弱点修改它，冰息公式读取它。

| 对象 | 本例职责 | 创建方式 |
| --- | --- | --- |
| `Attribute` | 保存玩家的冰息倍率，如 1.0、0.5 | Java 注册到 `Registries.ATTRIBUTE` |
| DS `modifier` | 临时修改上述属性，并管理持续时间与移除 | 技能或弱点 JSON |
| `MobEffect` | 药水式状态，如工程中的 `dswof:frost` | Java 注册到 `Registries.MOB_EFFECT`，见[状态效果文档](09-status-effects.md) |
| `DamageType` | 决定伤害类别及其关联规则 | `data/<命名空间>/damage_type/*.json` |

本方案不需要新增 `MobEffect`、实体效果类型或伤害类型。属性是数值通道；**注册属性本身不会自动影响伤害**，必须接入冰息的读取流程。

```text
DsWofAttributes 注册属性 → 将属性添加到玩家
                                   ↓
炎热条件 → DS modifier 修改玩家属性 → 冰息表达式读取倍率 → hurt(...)
```

环境判断、削弱比例和持续时间由 JSON 控制；Java 提供注册和读取接口。

## 2. 第一步：注册并添加到玩家

### 2.1 完整属性类示例

对应工程文件：[DsWofAttributes.java](../src/main/java/org/wgx/dswof/registry/dragonsurvival/DsWofAttributes.java)。下面采用字段初始化写法，效果与 DS 反编译得到的 `static { ... }` 初始化相同；选择一种写法，避免重复注册同名属性。

```java
package org.wgx.dswof.registry.dragonsurvival;

import by.dragonsurvivalteam.dragonsurvival.common.TimeAttribute;
import by.dragonsurvivalteam.dragonsurvival.registry.datagen.Translation;
import by.dragonsurvivalteam.dragonsurvival.util.Functions;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.wgx.dswof.DsWof;

@EventBusSubscriber(modid = DsWof.MODID)
public class DsWofAttributes {
    public static final DeferredRegister<Attribute> REGISTRY =
            DeferredRegister.create(Registries.ATTRIBUTE, DsWof.MODID);

    public static final Holder<Attribute> HEAT_RESISTANCE_TIME =
            REGISTRY.register("heat_resistance_time", () ->
                    new TimeAttribute(
                            Translation.Type.ATTRIBUTE.wrap(DsWof.MODID, "heat_resistance_time"),
                            Functions.secondsToTicks(10), 0.0, 16384.0
                    ).setSyncable(true));

    public static final Holder<Attribute> ICE_BREATH_MULTIPLIER =
            REGISTRY.register("ice_breath_multiplier", () ->
                    new PercentageAttribute(
                            Translation.Type.ATTRIBUTE.wrap(DsWof.MODID, "ice_breath_multiplier"),
                            1.0, 0.0, 10.0
                    ).setSyncable(true));

    @SubscribeEvent
    public static void attachAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, HEAT_RESISTANCE_TIME);
        event.add(EntityType.PLAYER, ICE_BREATH_MULTIPLIER);
    }
}
```

如果保留当前的 `static` 块，只需在 `REGISTRY` 初始化后为 `ICE_BREATH_MULTIPLIER` 增加上述 `REGISTRY.register(...)` 赋值，并在事件方法中添加第二个 `event.add(...)`。

`1.0` 表示正常倍率，`0.5` 表示一半，`0.0` 表示该公式乘出的直接伤害为零。上限 `10.0` 是本示例的设计选择，允许未来强化到十倍；若只允许削弱，可将上限设为 `1.0`。属性范围不是伤害点数范围。

这两项会添加到所有玩家；是否承受炎热弱点，由龙种的 `penalties` 与弱点条件决定。只给玩家添加即可，因为这里读取的是施法者，而非吐息目标。

### 2.2 将 DeferredRegister 接入模组事件总线

[DsWof.java](../src/main/java/org/wgx/dswof/DsWof.java) 中已经存在这一行，保留即可：

```java
DsWofAttributes.REGISTRY.register(modEventBus);
```

`EntityAttributeModificationEvent` 也属于模组事件总线。本工程当前加载器支持根据事件类型自动选择总线，沿用上述 `@EventBusSubscriber(modid = DsWof.MODID)` 即可。无需额外把它注册到 `NeoForge.EVENT_BUS`，也不要再手动添加同一个监听器。

两步的职责不同：`REGISTRY.register(modEventBus)` 让属性类型进入全局注册表；`event.add(EntityType.PLAYER, holder)` 让玩家真正拥有该属性。漏掉后者，即使 JSON 认识 ID，玩家也没有该属性实例，DS 的修饰流程会跳过它。

### 2.3 补充翻译

合并到 `assets/dswof/lang/zh_cn.json`，不要用下面片段覆盖已有内容：

```json
{
  "attribute.dswof.ice_breath_multiplier": "冰息伤害倍率",
  "attribute.dswof.heat_resistance_time": "耐热时间"
}
```

当前类使用的单参数 `Translation.Type.ATTRIBUTE.wrap("heat_resistance_time")` 会生成 **`attribute.dragonsurvival.heat_resistance_time`**。DS 的包装工具默认使用自己的命名空间，附属应调用双参数版本 `wrap(DsWof.MODID, "heat_resistance_time")`，或直接填写 `"attribute.dswof.heat_resistance_time"`。

注册 ID 和翻译键是独立参数：翻译键填错不会改变 `dswof:heat_resistance_time` 的注册 ID，但会导致名称无法按预期翻译。DS 的 `@Translation` 注解也不是附属自动生成语言文件的保证；本例直接维护语言 JSON。

## 3. 第二步：冰息同时读取通用属性与专属属性

### 3.1 现有 scale 可以做什么

当前 [IceBreathAbilityEntityEffect.java](../src/main/java/org/wgx/dswof/dragonsurvival/dragon_ability/IceBreathAbilityEntityEffect.java) 已有 `Holder<Attribute> scale`，默认读取 `dragonsurvival:dragon_ability_damage`。

注册完成后，仅在冰息效果中写 `"scale": "dswof:ice_breath_multiplier"`，现有 Java 就能通过 `getAttributeValue(scale)` 读取专属倍率。但这会替换原有的通用伤害属性，当前公式里的 `scale` 将不再包含 `dragon_ability_damage`。其他模组通过伤害事件追加的处理仍可能生效。

如果希望继续保留 DS 通用技能伤害加成，推荐新增第二个可配置属性入口 `multiplier`：

```text
表达式请求伤害 = 原有基础/接触时间公式 × scale × multiplier
scale       = 玩家 dragonsurvival:dragon_ability_damage
multiplier  = 玩家 dswof:ice_breath_multiplier
```

下面是需要依次合并到现有 record 的片段，不是另建一个类。

### 3.2 增加 record 字段和属性 Codec

补充 import：

```java
import org.wgx.dswof.registry.dragonsurvival.DsWofAttributes;
```

在现有 record 参数的 `Holder<Attribute> scale` 与 `Expression expression` 之间插入：

```java
Holder<Attribute> multiplier,
```

在 `RecordCodecBuilder` 的 `scale` 和 `expression` 对应项之间插入：

```java
Attribute.CODEC.optionalFieldOf("multiplier", DsWofAttributes.ICE_BREATH_MULTIPLIER)
        .forGetter(IceBreathAbilityEntityEffect::multiplier),
```

record 参数顺序与 `.group(...)` 中的 Codec 顺序必须一致。保留末尾 `.apply(instance, IceBreathAbilityEntityEffect::new)`；如果工程中还有直接 `new IceBreathAbilityEntityEffect(...)` 的调用，也要补传相应 Holder。

JSON 的 `multiplier` 是**属性 ID**，表达式中的 `multiplier` 是**该玩家当前的属性值**；它们和现有 `scale` 的配置方式一致。

### 3.3 声明表达式变量并注入当前属性值

将现有 `EXPRESSION_CODEC` 改为包含新变量的完整列表：

```java
public static final Codec<Expression> EXPRESSION_CODEC = MiscCodecs.expressionCodec(
        "amount", "scale", "multiplier", "ability_level", "experience_level", "experience_points",
        "growth", "current_movement_speed", "growth_per_unit", "growth_interval_ticks",
        "max_damage", "exposure_ticks", "exposure_seconds"
);
```

在 `calculateDamage(...)` 中、`expression.eval()` 之前，每次都设置：

```java
expression.setVariable("multiplier", BigDecimal.valueOf(dragon.getAttributeValue(multiplier)));
```

读取 `getAttributeValue(...)`，才能包含弱点、被动和装备等修饰。`getAttributeBaseValue(...)` 只读基础值，无法获取本方案的环境削弱结果。

### 3.4 更新默认表达式，以及当前 JSON 已写出的表达式

建议默认值改为：

```java
public static final Expression DEFAULT_EXPRESSION = new Expression(
        "MIN(max_damage, amount + growth_per_unit * exposure_ticks / growth_interval_ticks) * scale * multiplier"
);
```

**当前 `ice_breath.json` 已显式填写 `expression`，所以只改默认值不会改变该技能。** 需要给现有表达式也乘上 `multiplier`。以下是该 JSON 中单个伤害效果条目的接入示例；其他动画、目标选择和状态效果配置仍由技能文件提供：

```json
{
  "effect_type": "dswof:ice_breath_damage",
  "damage_type": "dswof:ice_breath",
  "base_damage": {
    "type": "minecraft:linear",
    "base": 2.0,
    "per_level_above_first": 2.0
  },
  "growth_per_second": 4.0,
  "growth_interval_ticks": 10,
  "max_damage": {
    "type": "minecraft:linear",
    "base": 12.0,
    "per_level_above_first": 1.0
  },
  "freeze_immune_multiplier": 0.5,
  "reset_after_ticks": 10,
  "scale": "dragonsurvival:dragon_ability_damage",
  "multiplier": "dswof:ice_breath_multiplier",
  "expression": "MIN((max_damage + (max_damage * ability_level * 0.5 + growth * 0.35)), (amount + amount * growth * 0.01) + growth_per_unit * exposure_ticks / growth_interval_ticks) * scale * multiplier"
}
```

这里的 `multiplier` 是**完成本节 Java 接入后新增的字段和变量**，当前未接入的效果类不支持它。已有 JSON 不声明新字段时可以使用 Codec 默认属性，但自定义公式依然必须引用新变量，才能产生倍率作用。

“炎热时 0.5”由弱点 JSON 决定；Java 只读取可配置属性并供公式使用。以后可以通过数据包换属性、改倍率或改公式，无需在伤害计算中写环境判断。

## 4. 第三步：弱点只修改冰息倍率

可以继续使用 `hot_ice_breath.json` 的条件和持续实例结构，将内层 `attribute` 换成 `dswof:ice_breath_multiplier`。文件名不决定影响范围，实际范围取决于修改哪个属性、哪些技能读取它。

为了让含义清楚，也可以新建 `data/dswof/dragonsurvival/dragon_penalty/hot_ice_breath.json`：

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
          "id": "dswof:hot_ice_breath",
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
            "attribute": "dswof:ice_breath_multiplier",
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

这里复用工程已有的 `dswof:in_hot_environment` predicate。冰翼龙的 `penalties` 应引用 `["dswof:nether_heat", "dswof:hot_ice_breath"]`；仅创建新弱点文件不会触发它。

不要将 `dswof:in_hot_environment` 填入龙种的 `penalties`：它定义在 `data/dswof/predicate/`，是条件，不是龙弱点。`penalties` 中的 ID 必须能在 `data/<命名空间>/dragonsurvival/dragon_penalty/` 中找到对应 JSON，否则会出现 `Unbound values in registry ... dragonsurvival:dragon_penalty`。正确的引用关系是：龙种 → `dswof:hot_ice_breath` 弱点 → `dswof:in_hot_environment` 条件。

`add_multiplied_total` 的含义是乘以 **`1 + amount`**，所以减半填 `-0.5`；填 `0.5` 会变成 1.5 倍。`duration = 40` 与每 20 tick 的刷新间隔配合维持修饰，离开环境时由提前移除条件清除；检测与更新按服务器 tick 进行。

同一持续实例 ID 由 DS 刷新与移除管理，不应在每次触发时自己追加一个随机 Java 修饰。不同弱点若各自施加 `-0.5` 的总倍率，它们会叠乘成 0.25；若同一冰息还受到旧的通用减半弱点，两项也会同时生效。迁移时需替换龙种引用或原文件内容，避免非预期双重减半。

若希望耐热被动延迟这项惩罚，可将这里的 `instant` 换成[环境弱点文档](11-environment-penalties.md)介绍的 `supply`，读取 `dswof:heat_resistance_time`。该时间属性控制何时惩罚，`ice_breath_multiplier` 控制惩罚强度，两者职责独立。多个每 tick 推进的弱点应使用各自的 `supply_type`，不要让地狱掉血和热环境减伤重复消耗同一个储备池。

## 5. REGISTRY.register 各个参数是什么

```java
DeferredRegister<Attribute> registry = DeferredRegister.create(Registries.ATTRIBUTE, DsWof.MODID);
Holder<Attribute> holder = registry.register("ice_breath_multiplier", () ->
        new PercentageAttribute("attribute.dswof.ice_breath_multiplier", 1.0, 0.0, 10.0)
                .setSyncable(true));
```

| 部分 | Java 类型/含义 | 本例结果 |
| --- | --- | --- |
| `Registries.ATTRIBUTE` | 属性注册表的键 | 声明注册目标是属性 |
| `DsWof.MODID` | `String`，命名空间 | `dswof` |
| `"ice_breath_multiplier"` | `String`，资源路径 | 与命名空间组成 `dswof:ice_breath_multiplier`；这里不填冒号前缀 |
| `() -> ...` | 产生属性对象的 Supplier | 在注册时创建对象；不要在此读取玩家或世界 |
| `new PercentageAttribute(...)` | 属性定义对象 | 提供名称键、默认值、范围和显示方式 |
| `.setSyncable(true)` | `boolean`，是否允许向客户端同步实体的属性实例值 | 便于客户端 UI 使用；不负责伤害计算，也不会自行创建 UI |
| 返回值 | 实际是可延迟绑定的 `DeferredHolder<Attribute, ...>`，可赋给 `Holder<Attribute>` | 提供属性引用，供 `event.add`、Codec 和实体查询使用 |

`Holder<Attribute>` 是引用类型，不是每名玩家的实时数值。玩家自己的 `AttributeInstance` 保存基础值和修饰；`dragon.getAttributeValue(holder)` 返回合并后的 `double`。在注册完成前，不要主动调用 Holder 的 `value()` 读取尚未绑定的对象。

`.setSyncable(true)` 返回 `Attribute`，因此将整个结果保存为 `Holder<Attribute>` 很合适；不要据此以为运行时对象不再是 `PercentageAttribute`。Java 静态字段内联初始化和 `static` 块都可以使用这一注册流程。

## 6. Attribute、RangedAttribute、PercentageAttribute、TimeAttribute

DSAttributes 在此版本实际使用的具体类型只有后三种。`PercentageAttribute` 和 `TimeAttribute` 都继承 `RangedAttribute`，使用相同的属性修饰计算。

| 类型及来源 | 构造参数 | 适用情况 |
| --- | --- | --- |
| `Attribute`，Minecraft | 基类构造器是 `protected Attribute(String descriptionId, double defaultValue)` | 属性定义基类，通常用具体子类注册 |
| `RangedAttribute`，Minecraft | `String descriptionId, double defaultValue, double min, double max` | 普通数值，如范围、速度参数、法力容量 |
| `PercentageAttribute`，NeoForge | 上述四项；另有第五项 `double scaleFactor` 的重载 | 百分比或倍率，如伤害倍率、概率 |
| `TimeAttribute`，Dragon Survival | 与 `RangedAttribute` 相同的四项 | 底层按 tick 存储、提示中以秒显示的时间数值 |

### 6.1 共同的四个构造参数

| 参数 | 作用 | 常见错误 |
| --- | --- | --- |
| `descriptionId` | 属性名称的翻译键，例如 `attribute.dswof.ice_breath_multiplier` | 当成属性 ID，或使用错误命名空间 |
| `defaultValue` | 没有为实体指定其他基础值时采用的默认基础值 | 倍率默认写成 0，导致乘法伤害归零 |
| `min` | 合并修饰后有效属性值的下限 | 设为 1 后期待削弱到 0.5，结果被限制到 1 |
| `max` | 合并修饰后有效属性值的上限 | 把时间的秒值当成 tick，或把倍率上限当伤害上限 |

构造时要求 `min <= defaultValue <= max`。正常使用有限数值；`RangedAttribute` 在提供有效值时对计算结果做范围限制。该范围约束的是**合并后的属性值**，不要求每条 modifier 的 `amount` 都为正，也不是给传入 `hurt()` 的伤害封顶。

### 6.2 RangedAttribute：限定范围的普通数值

```java
new RangedAttribute("attribute.dswof.example_range", 3.0, 0.0, 64.0)
```

本例默认值为 3，允许 0～64。它没有内置“格”“伤害”“概率”等玩法语义，实际单位取决于读取它的逻辑。DS 的吐息范围会读取范围属性，而伤害不会因为属性名字中有 `damage` 就自动采用它。

在 NeoForge 的属性提示体系中，普通数值和 `ADD_VALUE` 默认按数值显示，乘法修饰仍会按百分比显示。因此是否显示百分比，与使用哪种修饰运算也有关。

### 6.3 PercentageAttribute：百分比显示，不会自动除以 100

```java
new PercentageAttribute("attribute.dswof.ice_breath_multiplier", 1.0, 0.0, 10.0)
```

默认四参数构造器使用 `scaleFactor = 100`：原始值 `1.0` 显示为 100%，`0.5` 显示为 50%，`1.5` 显示为 150%。Java 与 JSON 中仍使用 `1.0`、`0.5`、`1.5`；若填 50，它表示原始数值 50，还会受到 `max` 限制。

五参数重载的 `scaleFactor` 只用于原始值或 `ADD_VALUE` 的显示换算。乘法修饰显示仍乘 100。**它不会改变属性计算、不会自动让概率生效，也不会自动把伤害乘上这个数。**

DS 的 `armor_ignore_chance` 用范围 0～1 表示概率；`dragon_ability_damage` 用范围 0～10 表示倍率。同为 PercentageAttribute，玩法语义仍由读取代码决定。

### 6.4 TimeAttribute：tick 存储，秒显示

```java
new TimeAttribute("attribute.dswof.heat_resistance_time",
        Functions.secondsToTicks(10), 0.0, 16384.0)
```

`Functions.secondsToTicks(10)` 返回 200；整个属性的单位为 tick。上限 16384 也是 tick，即 819.2 秒。JSON 的 `ADD_VALUE +200` 增加 200 tick，不是 200 秒。

DS 的实现只重写显示方法：原始值或 `ADD_VALUE` 先转 `int`，再通过 `ticksToSeconds(...)` 换成秒；乘法修饰沿用父类百分比显示。底层仍可保存 `double`。它不会自动倒计时、消耗储备或触发伤害，剩余储备及更新由 `SupplyTrigger` 等消费者管理。耐受时长还取决于每 tick 的消耗率，详见[环境弱点文档](11-environment-penalties.md)。

## 7. DSAttributes 全部注册值速查

下面的 ID 均以 `dragonsurvival:` 为命名空间；全部调用 `.setSyncable(true)`。默认值来自注册定义，玩家实际值还可能受龙种、体型、成长、技能、装备和其他修饰影响。

| Java 字段 | 注册路径 | 类型 | 默认值 | 最小值 | 最大值 | 用途/单位 |
| --- | --- | --- | --- | --- | --- | --- |
| `FLIGHT_STAMINA_COST` | `flight_stamina` | RangedAttribute | 1 | 0 | 5 | 飞行饥饿消耗相关参数 |
| `LAVA_SWIM_SPEED` | `lava_swim_speed` | RangedAttribute | 1 | 0 | 1024 | 岩浆游泳速度倍率 |
| `FLIGHT_SPEED` | `flight_speed` | RangedAttribute | 1 | 0 | 1024 | 飞行速度倍率 |
| `MANA` | `mana` | RangedAttribute | 1 | 0 | 1024 | 法力容量相关数值，当前法力由 MagicData 管理 |
| `MANA_REGENERATION` | `mana_regeneration` | RangedAttribute | 0.004 | 0 | 1024 | 每 tick 法力恢复量 |
| `EXPERIENCE` | `experience` | PercentageAttribute | 1 | 0 | 1024 | 掉落经验倍率 |
| `DRAGON_BREATH_RANGE` | `dragon_breath_range` | RangedAttribute | 3 | 0 | 1024 | 吐息范围基础数值，单位格；还受技能范围配置影响 |
| `BLOCK_BREAK_RADIUS` | `block_break_radius` | RangedAttribute | 0 | 0 | 16 | 挖掘额外破坏半径 |
| `PENALTY_RESISTANCE_TIME` | `penalty_resistance_time` | TimeAttribute | 200 | 0 | 16384 | 弱点耐受上限，单位 tick |
| `ARMOR_IGNORE_CHANCE` | `armor_ignore_chance` | PercentageAttribute | 0 | 0 | 1 | 攻击忽略护甲的概率 |
| `HUNTER_FACTION_DAMAGE` | `hunter_faction_damage` | PercentageAttribute | 1 | 0 | 10 | 对猎人阵营的伤害倍率 |
| `DRAGON_ABILITY_DAMAGE` | `dragon_ability_damage` | PercentageAttribute | 1 | 0 | 10 | 龙技能伤害倍率；需伤害逻辑实际读取 |

DS 将上述属性添加到玩家；`LAVA_SWIM_SPEED` 还会通过 `event.getTypes()` 添加到事件列出的已有属性供应器的生物类型。附属的冰息倍率无需照搬这一扩大范围的处理。

## 8. modifier 的三种 operation

属性类决定范围与显示；`operation` 决定如何把修饰合并到属性。以下三种运算对上述属性子类都适用：

| JSON operation | Java 枚举 | 计算阶段 | 对冰息倍率的例子 |
| --- | --- | --- | --- |
| `add_value` | `ADD_VALUE` | 先将数值直接相加 | 基础 1，加 -0.5 后为 0.5 |
| `add_multiplied_base` | `ADD_MULTIPLIED_BASE` | 按完成 ADD_VALUE 后的值，累加各项比例 | 单独 -0.5 时也为 0.5；多个同类比例先相加 |
| `add_multiplied_total` | `ADD_MULTIPLIED_TOTAL` | 依次乘上每条修饰的 `1 + amount` | -0.5 使当前结果乘 0.5 |

用 `B` 表示实体基础值，`a` 为 ADD_VALUE，`b` 为 ADD_MULTIPLIED_BASE，`c` 为 ADD_MULTIPLIED_TOTAL，则：

```text
X = B + Σa
Y = X × (1 + Σb)
最终有效属性值 = 限制到[min, max](Y × Π(1 + c))
```

冰息热环境削弱推荐使用 `add_multiplied_total: -0.5`：即使未来装备把冰息倍率提高到 1.2，热环境仍能让其减半到 0.6。若用 `add_value: -0.5`，则变成 0.7，含义是减去 50 个百分点。

`Attribute` 的 `setSentiment(...)` 可调整“增加是否有益”的提示配色，默认按正向属性处理；它不反转运算或自动修改玩法。上述 DSAttributes 注册均使用默认设置。

## 9. 验证顺序与常见排查

Java 属性注册变更需要重新构建并重启游戏/服务端，`/reload` 无法新建 Java 属性。属性已存在后，技能与弱点配置可按 DS 数据包重载流程更新。

1. 在允许命令的测试世界，用 `/attribute @s dswof:ice_breath_multiplier get` 确认玩家有该属性且初始为 1。
2. 临时执行下面的 modifier 命令，确认值变成 0.5，并测试冰息公式是否读取成功；完成后删除测试修饰。
3. 切换到生存模式和目标龙种，进入炎热群系，确认专属属性变成 0.5，而 `dragonsurvival:dragon_ability_damage` 保持预期值。
4. 离开炎热环境，确认专属属性恢复。用相同技能等级、成长、接触时间及装备对照伤害；冻结免疫倍率和 `hurt()` 后续处理要保持一致。

```mcfunction
/attribute @s dswof:ice_breath_multiplier modifier add dswof:test_ice_half -0.5 add_multiplied_total
/attribute @s dswof:ice_breath_multiplier get
/attribute @s dswof:ice_breath_multiplier modifier remove dswof:test_ice_half
```

例如基础/接触时间公式结果为 12，通用技能属性为 1.8，热环境专属倍率为 0.5，目标冻结免疫倍率为 0.5，则传给 `hurt()` 的请求伤害为 `12 × 1.8 × 0.5 × 0.5 = 5.4`。实际扣血还会经过护甲、无敌帧、DS 和其他模组伤害处理。

本倍率仅影响引用它的直接伤害表达式。当前冰息还有单独的 `dragonsurvival:potion` 动作；倍率为零不会自动禁止施放、减少法力消耗、取消该状态或降低状态自身的伤害。其他技能只有主动读取该属性时才会受它影响。

| 现象 | 优先检查 |
| --- | --- |
| 编译报 final 字段未初始化 | 是否实际为 `ICE_BREATH_MULTIPLIER` 赋值 |
| JSON 报未知属性 | ID 拼写、模组总线注册、是否已重启 |
| 命令显示玩家没有该属性 | `EntityAttributeModificationEvent` 是否添加了它 |
| 属性变为 0.5，冰息伤害不变 | 运行时注入和当前 JSON 的显式表达式是否都使用 `multiplier` |
| 表达式加载失败 | `EXPRESSION_CODEC` 是否声明了 `multiplier`，以及 record/Codec 顺序是否一致 |
| 属性名显示原始翻译键 | 是否使用了 `wrap(DsWof.MODID, path)`，语言 JSON 是否包含对应键 |
| 热环境未触发 | predicate 是否匹配、龙种是否引用弱点、是否处于生存/冒险模式，以及 `enable_penalties` 是否开启 |
| 恰好变成四分之一 | 旧的通用减半与新的专属减半是否同时存在、测试修饰是否尚未移除、表达式是否重复乘了 `multiplier` |

### 9.1 在游戏内确认当前群系、条件与有效属性

F3 中 `Biome: minecraft:savanna` 表示玩家所在的群系；右侧 `Targeted Block` 下的标签属于准星指向的方块，不能据此判断群系标签。当前工程的 `hot_environment.json` 包含 `minecraft:desert`、`minecraft:badlands`、`minecraft:savanna`、`minecraft:savanna_plateau`，所以热带草原应当匹配。

文件内容只能说明工程配置。要确认正在运行的服务器实际加载了该标签，在游戏聊天框执行以下命令（需要命令权限）：

```mcfunction
/execute at @s if biome ~ ~ ~ #dswof:hot_environment run say 当前群系属于炎热标签
/execute at @s unless biome ~ ~ ~ #dswof:hot_environment run say 当前群系不属于炎热标签
/execute as @s at @s if predicate dswof:in_hot_environment run say 炎热条件成立
/execute as @s at @s unless predicate dswof:in_hot_environment run say 炎热条件不成立
```

前两条检查当前坐标处的群系是否属于服务器实际加载的标签；后两条检查弱点引用的完整条件。如果提示未知标签或未知 predicate，应检查运行目录的资源是否更新、模组/数据包是否启用，以及服务器日志中的加载错误。

**DS 的 `DragonPenaltyHandler.applyPenalties` 会直接跳过创造模式和旁观模式玩家。** 即使以上条件成立，创造模式下也不会通过 tick 为玩家添加 `hot_ice_breath` 修饰。该限制来自 DS，不是附属的标签条件。

切换为冰翼龙，使用生存模式，在炎热群系内让游戏实际运行至少 20 tick（约 1 秒；单人游戏暂停时不计时），再查询有效属性：

```mcfunction
/gamemode survival
/attribute @s dswof:ice_breath_multiplier get
```

默认基础值为 1 且没有其他修饰时，应得到 `0.5`。离开标签中的群系后等待最多约 40 tick（2 秒），应恢复到 `1`。可用 `/attribute @s dswof:ice_breath_multiplier base get` 核对基础值；弱点修改的是有效值，不会把基础值从 1 改成 0.5。若实际值不同，还要检查其他修饰和属性范围限制。

若 predicate 为真但有效属性仍为 1，依次检查：玩家确实是 `dswof:ice_wings_dragon`、该龙种引用 `dswof:hot_ice_breath`、DS 服务端 `penalties.enable_penalties` 开启、没有暂停、当前运行的资源包含改动。本工程配置位于 `run/config/dragonsurvival-server.toml`；其他启动器/服务端应检查各自的运行目录。修改 Java 后重新构建并重启；修改 DS 龙种、技能、弱点这些动态注册表 JSON 后也应重新进入世界/重启服务端加载，不能仅凭 `/reload` 判定它们已更新。

若属性已经变为 0.5，但伤害仍不符合预期，应检查表达式。让 `multiplier` 只在完整伤害计算后乘一次，例如当前工程保留其 `scale` 在上限内侧的布局：

```text
MIN(max_damage + max_damage * ability_level * 0.5 + growth * 0.35,
    (amount + amount * growth * 0.01 + growth_per_unit * exposure_ticks / growth_interval_ticks) * scale) * multiplier
```

不要再给 `MIN` 的上限分支额外乘一次 `multiplier`；否则 `multiplier = 0.5` 时，上限分支可能累计得到四分之一。第 4 节示例将 `scale` 放在 `MIN` 外，表示先限制基础伤害，再应用通用倍率；当前工程公式先应用通用倍率再限制伤害。两种布局对上限的含义不同，应按数值设计选择，但环境倍率均在最后仅乘一次。

对比伤害时固定技能等级、成长、装备与接触时长。当前冰息会随着连续接触而增伤，应至少中断超过 `reset_after_ticks`（当前为 10 tick）后再比较下一次首击。`dswof:frost` 是独立的状态效果动作，不能仅凭试验假人的累计显示推断直接冰息伤害是否减半。

## 10. 核查来源

- [DSAttributes：固定版本源码](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/DSAttributes.java)：全部注册参数及实体添加范围。
- [TimeAttribute：固定版本源码](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/TimeAttribute.java)：tick 转秒的显示实现。
- [Translation：固定版本源码](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/datagen/Translation.java)：`wrap` 的命名空间处理。
- [DragonPenaltyHandler：固定版本源码](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/handlers/DragonPenaltyHandler.java)：tick 弱点跳过创造模式和旁观模式。
- NeoForge 21.1.256 sources JAR 中的 `net/neoforged/neoforge/common/PercentageAttribute.java`、`common/extensions/IAttributeExtension.java` 和 `event/entity/EntityAttributeModificationEvent.java`：百分比构造器、显示换算和模组事件总线定义。
- 本工程依赖中的 `RangedAttribute`、`AttributeInstance`：构造范围约束和三种属性修饰的计算阶段。

相关文档：[冰息表达式](07-ice-breath-expressions.md)、[技能数值](10-ability-scaling-and-balance.md)、[环境弱点与多种耐受](11-environment-penalties.md)。
