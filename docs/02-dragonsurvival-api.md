# Dragon Survival 2.0.71 接口参考

## 1. 范围与可靠性

这份参考依据本工程实际加载的 Dragon Survival **2.0.71** JAR 与官方提交 `6638c759c5eb224f7de72d9b10b7bec75f9fbafc` 整理。Minecraft 1.21.1 / NeoForge 为适用平台；不能直接用于 1.19.x Forge 等旧版。

官方提供 JSON 内容文档与附属教程，但这里核查的源码没有一个独立、承诺稳定的 Java SDK 包。下面列出的 Java 方法是上游 public 实现接口；升级版本后必须重查签名、状态变化和同步行为。[官方链接和源码入口](04-sources-and-version-notes.md) 可用于继续查询。

## 2. 数据接口

Java 状态效果的注册和技能施加流程另见[DS 状态效果：注册、施加与实现](09-status-effects.md)，包含 `MobEffect`、`MobEffectInstance` 与 `DamageType` 的区别。

`dragon_ability/` 的逐字段说明已单独整理为 [JSON 参数手册](06-dragon-ability-json-reference.md)，包含激活、动画、升级、目标选择、效果与嵌套对象。

所有数据包类型的完整字段、默认值和用法见[数据包 JSON 文档总目录](datapacks/README.md)；当前依赖的全部 877 个数据 JSON 可在[逐文件索引](datapacks/10-file-index.md)按完整路径查询。

### 2.1 注册表与目录

| 注册表 ID | 新对象路径（资源根目录下） | Java 类（包前缀省略） |
| --- | --- | --- |
| `dragonsurvival:dragon_species` | `data/dswof/dragonsurvival/dragon_species/<id>.json` | `registry.dragon.DragonSpecies` |
| `dragonsurvival:dragon_body` | `data/dswof/dragonsurvival/dragon_body/<id>.json` | `registry.dragon.body.DragonBody` |
| `dragonsurvival:dragon_stage` | `data/dswof/dragonsurvival/dragon_stage/<id>.json` | `registry.dragon.stage.DragonStage` |
| `dragonsurvival:dragon_ability` | `data/dswof/dragonsurvival/dragon_ability/<id>.json` | `registry.dragon.ability.DragonAbility` |
| `dragonsurvival:dragon_penalty` | `data/dswof/dragonsurvival/dragon_penalty/<id>.json` | `registry.dragon.penalty.DragonPenalty` |
| `dragonsurvival:dragon_emote_set` | `data/dswof/dragonsurvival/dragon_emote_set/<id>.json` | `registry.dragon.body.emotes.DragonEmoteSet` |

Java 完整包前缀为 `by.dragonsurvivalteam.dragonsurvival`。以上六种内容类通过各自的 `REGISTRY` 常量暴露数据包注册表 key。

### 2.2 DragonSpecies

官方介绍：[DragonSpecies](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/DragonSpecies)。实际 `DIRECT_CODEC` 字段如下：

| 字段 | 必需 | 含义 / 缺省行为 |
| --- | --- | --- |
| `starting_growth` | 否 | 起始成长值，至少 1；缺省取该种族阶段的最小成长值 |
| `custom_stage_progression` | 否 | 阶段 HolderSet；缺省使用默认成长阶段 |
| `bodies` | 否 | 允许的体型；空时使用 `is_default: true` 的体型 |
| `abilities` | 否 | 技能集合，缺省空 |
| `penalties` | 否 | 弱点 / 惩罚集合，缺省空 |
| `unlockable_behavior` | 否 | 解锁条件与祭坛可见性 |
| `mana_handling` | 否 | 法力行为，缺省 `ManaHandling.DEFAULT` |
| `misc_resources` | **是** | 祭坛、技能栏、成长晶体、食物提示与颜色等资源 |

`bodies`、`abilities`、`penalties` 等 HolderSet 可以写 ID 数组，或标签字符串，如 `"abilities": "#dswof:sky_dragon"`。数组中的 ID 引用对象；以 `#` 开头的字符串引用对应注册表标签，不是文件系统路径。

`misc_resources` 必需字段包括 `altar_banner`、`ability_bar`、`growth_crystal`（`empty` / `full`）、`food_tooltip`（`nutrition_icon` / `saturation_icon`）、`primary_color` 和 `secondary_color`。其他资源如法力图标、食物精灵图、成长箭头有各自缺省或可选处理，详见当前 `MiscResources.CODEC`。

不要使用旧 Wiki 的 `starting_size`。当前 species 没有顶层 `diet` 和 `modifiers` 字段；食谱使用 data map，属性主要通过 body、stage、能力等接口定义。缺省字段与无效引用是两件事：某个字段可省略不意味着可以填不存在的 ID。

### 2.3 DragonBody

官方介绍：[DragonBody](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/DragonBody)。有模型体型的最小完整定义见 [sky_body.json](examples/prototype-resources/data/dswof/dragonsurvival/dragon_body/sky_body.json)。

| 字段 | 有模型分支必需 | 含义 / 缺省值 |
| --- | --- | --- |
| `modifiers` | **是** | 属性修改列表，可以是 `[]` |
| `animation` | **是** | 动画逻辑 ID，例如 `dragonsurvival:dragon_center` |
| `emotes` | **是** | 表情集合 Holder，例如 `dragonsurvival:default_emotes` |
| `scaling_proportions` | **是** | `width`、`height`、`eye_height` 必需 |
| `crouch_height_ratio` | **是** | 潜行高度比例 |
| `is_default` | 否 | 默认 `false`；设置 `true` 会影响使用默认体型集合的种族 |
| `model` | 否 | 默认 `dragonsurvival:dragon_model` |
| `texture_size` | 否 | 默认 `{"width":512,"height":512}` |
| `can_hide_wings` | 否 | 默认 `true` |
| `bones_to_hide_for_toggle` | 否 | 默认四个翼骨：`WingLeft`、`WingRight`、`SmallWingLeft`、`SmallWingRight` |
| `default_icon` | 否 | 体型默认图标的完整资源位置 |
| `unlockable_behavior` | 否 | 解锁与可见性 |
| `backpack_offset` | 否 | 背包位置、旋转、缩放偏移 |
| `bettercombat_weapon_offset` | 否 | Better Combat 武器偏移，默认 0 |
| `rideable` | 否 | 默认 `true`，独立模型需满足骑乘骨骼要求 |

`scaling_proportions` 还支持 `scale_multiplier`、`shadow_multiplier` 和 `ceiling_climbing_offset_multiplier`，默认均为 1。没有旧 Wiki 示意中的 `offset`、`mounting_offset`。

该类使用 `MODEL_CODEC` 与 `NO_MODEL_CODEC` 二选一。**漏填有模型分支的必需字段可能被解析为无龙模型体型**，不一定表现为 JSON 报错。出现人形或不渲染时，先核对这五项必需字段。

模型和动画写逻辑 ID：

```json
"model": "dswof:sky_dragon",
"animation": "dswof:sky_dragon"
```

DS 会分别解析为 `assets/dswof/geo/sky_dragon.geo.json` 与 `assets/dswof/animations/sky_dragon.json`。这些字段不能填 `dswof:geo/sky_dragon.geo.json` 或动画完整路径。贴图和图标字段则使用包含 `textures/` 与 `.png` 的完整位置。

### 2.4 DragonStage

官方介绍：[DragonStage](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/DragonStage)。

| 字段 | 必需 | 含义 |
| --- | --- | --- |
| `growth_range` | **是** | `min`、`max` 的成长范围 |
| `ticks_until_grown` | **是** | 本阶段成长耗时，1 到 365 个游戏日的 tick 数 |
| `is_default` | 否 | 是否进入默认阶段集合，默认 false |
| `modifiers` | 否 | 属性修改，默认空 |
| `growth_items` | 否 | 成长物品列表，默认空 |
| `is_natural_growth_stopped` | 否 | 阻止自然成长的实体 predicate |
| `destruction_data` | 否 | 破坏 / 碾压等成长相关配置 |

当前字段是 `growth_range`，不是 `size_range`；当前 CODEC 没有 Wiki 所列的 `grow_into_requirements`。自定义阶段需要连续的成长范围，并满足 DS 的阶段验证。不要为了新增龙种就复制并覆盖所有默认阶段。

### 2.5 DragonAbility

官方介绍：[Dragon Ability](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/Dragon-Ability)。

| 字段 | 必需 | 含义 |
| --- | --- | --- |
| `activation` | **是** | 激活方式；分派字段为 `activation_type` |
| `icon` | **是** | `LevelBasedResource`，按技能等级选择图标 |
| `upgrade` | 否 | 升级规则；缺省保持等级 1 |
| `usage_blocked` | 否 | 禁止使用的 loot predicate |
| `actions` | 否 | **ActionContainer 数组**，缺省空 |
| `can_be_manually_disabled` | 否 | 是否允许玩家禁用，默认 true |

此处仅给技能顶层契约。activation、目标与效果各有子类型 CODEC；不能把全部技能都写成同一组参数。当前实现还检查：被动技能不能使用非默认的动作触发时点；非引导技能不能使用引导完成触发时点。

### 2.6 Data maps 与标签

| data map 文件（位于 `data/dragonsurvival/`） | key | value 用途 |
| --- | --- | --- |
| `data_maps/dragonsurvival/dragon_species/stage_resources.json` | 龙种 ID | 阶段到皮肤、成长图标的映射 |
| `data_maps/dragonsurvival/dragon_species/diet_entries.json` | 龙种 ID | 食谱列表 |
| `data_maps/dragonsurvival/dragon_species/dragon_beacon_data.json` | 龙种 ID | 龙信标配置 |
| `data_maps/dragonsurvival/dragon_species/end_platforms.json` | 龙种 ID | 末地平台配置 |
| `data_maps/dragonsurvival/dragon_body/body_icons.json` | 体型 ID | 龙种到体型图标的映射 |

文件以 `{"values": {...}}` 包装。只添加自己的 ID，避免顶层整表替换。阶段资源 map 使用 map merger，食谱使用专门的 diet merger；修改已有条目时仍需理解它们的合并行为。

每个启用阶段的资源 value 都需要：

```json
{
  "default_skin": {
    "skin": "dswof:textures/dragon/sky_dragon/newborn.png",
    "glow_skin": "dswof:textures/dragon/sky_dragon/newborn_glow.png"
  },
  "growth_icon": {
    "icon": "dswof:textures/gui/sky_newborn.png",
    "hover_icon": "dswof:textures/gui/sky_newborn_hover.png"
  }
}
```

`glow_skin` 是必需字段。没有发光区域时，可以提供同尺寸透明 PNG。上面自有贴图路径是教学目标，实际文件必须由你制作；[原型资源](examples/prototype-resources/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/stage_resources.json) 使用已经存在的 DS 图片 ID。

食谱的 `DietEntry` 必需 `items`（物品 ID 或 `#` 标签），可选 `properties`（食物属性）、`retain_effects`（布尔或效果筛选）。省略 `properties` 时读取物品原有食物属性；没有原有属性也没有自定义属性的物品不会变得可食用。详见 [DietEntry](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/DietEntry)。

显示排序标签可放在 `data/dragonsurvival/tags/dragonsurvival/dragon_species/order.json`、`.../dragon_body/order.json`。用 `replace: false` 和自己的 ID 增量添加。标签决定显示组织，不替代 registry 对象的定义。

翻译 key 示例：`dragon_species.dswof.sky_dragon`、`dragon_species.dswof.sky_dragon.altar.desc`、`dragon_body.dswof.sky_body`、`dragon_body.dswof.sky_body.desc`，文件放 `assets/dswof/lang/zh_cn.json`。

## 3. Java 状态接口

### 3.1 取得数据

```java
import by.dragonsurvivalteam.dragonsurvival.common.capability.DragonStateProvider;

var state = DragonStateProvider.getData(player);
if (state.isDragon()) {
    var speciesId = state.speciesId();
    var bodyId = state.bodyId();
    var stageId = state.stageId();
    double growth = state.getGrowth();
}
```

`DragonStateProvider`：

```java
static DragonStateHandler getData(Player player);
static Optional<DragonStateHandler> getOptional(Entity entity);
static boolean isDragon(Entity entity);
```

类名保留了 capability 字样，但当前 `getData(Player)` 底层取的是 NeoForge `DRAGON_HANDLER` attachment。不要照抄旧版 `LazyOptional` capability 教程。

`getOptional()` 有值不等于龙形态；人类玩家也可能持有状态数据。`isDragon()` 要求 species、body、stage 都已存在；未确认前读取它们的 ID 可能空指针。

### 3.2 DragonStateHandler 常用读取

| 方法 | 返回含义 |
| --- | --- |
| `isDragon()` | 当前是否完整龙形态 |
| `species()` / `body()` / `stage()` | 对应对象的 Holder；非龙状态下可能为空 |
| `speciesKey()` / `bodyKey()` / `stageKey()` | 对应 ResourceKey |
| `speciesId()` / `bodyId()` / `stageId()` | 对应 ResourceLocation |
| `getGrowth()` | 实际成长值 |
| `getDesiredGrowth()` | 希望插值到的目标成长值 |
| `getModel()` | 当前模型逻辑 ID |

### 3.3 常用修改与行为边界

以下省略注解，保留参数含义：

```java
void setSpecies(Player player, Holder<DragonSpecies> species);
void setSpecies(Player player, Holder<DragonSpecies> species, boolean savedForSoul);
void setBody(Player player, Holder<DragonBody> body);
void setStage(Player player, Holder<DragonStage> stage);
void setGrowth(Player player, double growth);
void setGrowth(Player player, double growth, boolean forceUpdate);
void setDesiredGrowth(Player player, double growth);
void revertToHumanForm(Player player, boolean isDragonSoul);
CompoundTag serializeNBT(HolderLookup.Provider provider);
void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag);
```

- `setSpecies` 会处理合法体型选择、已有年龄以及部分技能和属性刷新，但不是完整的祭坛变身与全量同步流程。
- `setBody` **不自动同步到客户端**。应检查该体型对当前种族合法，还要按你的入口规则检查解锁、费用等条件。
- `setStage` 会把目标成长值调到阶段合法范围；已有阶段时通常走渐变，不保证调用后立即变成该阶段。
- `setDesiredGrowth` 更新并同步目标值，正常 tick 逐步调整实际成长，仍可能受成长限制影响。
- `setGrowth` 更新实际成长、阶段和尺寸相关状态，并在需要时发成长同步；只改实际值可能下一 tick 又向旧目标插值。
- 若业务确需立即调整，先校验许可与业务边界，再 `setDesiredGrowth`，随后用 `getDesiredGrowth()` 调用 `setGrowth(..., true)`。这类管理员式操作不替代自然成长机制。
- NBT 方法面向持久化与内部同步，不建议拿它们做随意改变龙种的业务接口。

## 4. 动态注册表查询

例如查找一个体型：

```java
ResourceKey<DragonBody> key = ResourceKey.create(
        DragonBody.REGISTRY, ResourceLocation.fromNamespaceAndPath("dswof", "sky_body"));

Optional<Holder.Reference<DragonBody>> body = player.registryAccess()
        .registryOrThrow(DragonBody.REGISTRY)
        .getHolder(key);
```

使用当前玩家的 registry，处理未找到的情况。不要从 `BuiltInRegistries` 查询这些数据包对象，也不要使用普通物品注册的 `DeferredRegister<DragonSpecies>` 代替 JSON。

`util.ResourceHelper` 提供：

```java
static <T> Optional<Holder.Reference<T>> get(HolderLookup.Provider provider, ResourceKey<T> key);
static <T> List<Holder.Reference<T>> all(HolderLookup.Provider provider, ResourceKey<Registry<T>> registry);
```

合法性与选择工具包括：

```java
DragonBody.bodyIsValidForSpecies(Holder<DragonBody> body, Holder<DragonSpecies> species);
DragonBody.getRandomUnlocked(ServerPlayer player);
DragonSpecies.getRandom(ServerPlayer player);
```

`bodyIsValidForSpecies` 只检查种族体型集合，不检查玩家是否已解锁。不要把它作为客户端请求的全部授权逻辑。

## 5. 状态同步

`server.handlers.PlayerLoginHandler`：

```java
static void syncHandler(ServerPlayer player);
static void syncComplete(Entity entity);
```

`syncHandler` 发送龙状态给本人和跟踪者，并处理飞行数据；`syncComplete` 还调用 `SyncComplete.handleDragonSync(player, true)` 刷新龙相关状态，并同步其他已有 attachment。两者作用范围不同，不能按名字当作等价函数。

官方命令变龙大致依次设置种族、合法体型、成长与骑乘状态，然后调用 `SyncComplete.handleDragonSync` 并广播 `SyncComplete`。附属若设计新的完整变身流程，还应检查祭坛、装备、成长目标和骑乘规则，参考当前 `DragonCommand`，不能认为单个 setter 完成一切。

变回人的官方命令路径使用 `revertToHumanForm(player, false)`，再 `PlayerLoginHandler.syncHandler(player)`。

示例适配类：[DragonSurvivalAccess.java](examples/java/org/wgx/dswof/compat/dragonsurvival/DragonSurvivalAccess.java)。它演示只读快照、已获授权的体型调整、成长目标和变回人，**没有注册指令或接受网络请求**。调用方仍需负责权限、解锁和消费规则。

不要每个 tick 无条件发全量同步。客户端发出请求后，应在服务端线程处理、校验和修改，再同步给相关玩家。

## 6. MagicData：法力与技能

正确包名是 `by.dragonsurvivalteam.dragonsurvival.registry.attachments.MagicData`。

```java
static MagicData getData(Player player);
float getCurrentMana();
float getAvailableMana();
float getReservedMana();
void adjustMana(Player player, float amount);
void setCurrentMana(Player player, float amount);

Map<ResourceKey<DragonAbility>, DragonAbilityInstance> getAbilities();
DragonAbilityInstance getAbility(ResourceKey<DragonAbility> key);
boolean addAbility(ServerPlayer player, Holder<DragonAbility> ability);
boolean removeAbility(Player player, ResourceKey<DragonAbility> key);
boolean attemptCast(Player player, int slot);
void stopCasting(Player player);
boolean isCasting();
void moveAbilityToSlot(ResourceKey<DragonAbility> key, int slot);
void refresh(ServerPlayer player, Holder<DragonSpecies> species);
```

使用 `getAvailableMana()` 表达可用法力，不直接把当前法力当作扣除预留后的余额。`attemptCast` 参数是技能槽位，不是技能 ID；`getAbility` 返回值应按可能不存在处理。

普通种族技能优先放入 species JSON，由 DS 管理授予和刷新。手动添加技能、移动槽位、调整法力时，要继续核查对应方法实现中的持久化、同步与校验；public 不表示它已实现你附属的全部权限规则。

## 7. 客户端资源接口

普通自定义体型由 DS 的 `client.models.DragonModel` 读取模型与动画，通常无需自己注册一个玩家实体 renderer。客户端相关类只能在客户端入口加载，不能放进通用服务端事件类的静态初始化。

默认贴图来自 species 的 `stage_resources`。默认模型 ID 为 `dragonsurvival:dragon_model`；当前在线 DragonSkins 路径仅在模型 ID 等于默认模型时启用。因此**独立模型不能直接使用官方在线自定义皮肤服务**。本地皮肤编辑器又有自己的部件资源和模型约束，不能据此推断任何自定义 UV 都受支持。

骨骼名、动画键和文件名都是接口的一部分，必须精确匹配；模型制作详见 [模型教程](03-modeling-tutorial.md)。
