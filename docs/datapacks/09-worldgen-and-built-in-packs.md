# 世界生成、内置数据包与遗留文件

[返回总目录](README.md)。DS 2.0.71 的世界生成 JSON 共 59 个：12 个 structure、6 个 structure_set、31 个 template_pool、10 个 processor_list。具体名称和用途见[逐文件索引](10-file-index.md)。结构建筑本体是 `data/<ns>/structure/*.nbt`，不属于 JSON。

## 四种文件的连接关系

structure_set 决定在哪些区块尝试放置哪些 structure；structure 决定群系、高度与起始 template_pool；template_pool 决定选哪个 NBT 建筑及后续拼图；processor_list 在放置 NBT 方块时替换 / 老化方块。结构内的箱子可引用 loot_table。

修改通常只影响新生成区块。测试用新世界或尚未生成的区域，不能靠修改 JSON 重建已有建筑。

## worldgen/structure：结构定义

DS 此版本全部使用 `type: minecraft:jigsaw`，路径 `data/<ns>/worldgen/structure/<id>.json`。

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `type` | ID；必填 | 此处为 minecraft:jigsaw |
| `biomes` | 生物群系集合；必填 | 允许生成的群系，常引用 #dragonsurvival:is_*_spawn |
| `step` | 枚举；必填 | 生成步骤，DS 使用 surface_structures / underground_decoration 等 |
| `spawn_overrides` | 对象；必填 | 生物类别 → 刷怪覆盖；可用空对象表示不覆盖 |
| `terrain_adaptation` | 枚举；可选；none | none / bury / beard_thin / beard_box / encapsulate，控制周围地形适配 |
| `start_pool` | 模板池 ID；必填 | 起始建筑候选池 |
| `start_jigsaw_name` | ID；可选 | 指定起始拼图连接点名称 |
| `size` | 整数 0～20；必填 | 拼图扩展深度，不是建筑方块尺寸 |
| `start_height` | HeightProvider；必填 | 起始高度，见下文 |
| `use_expansion_hack` | 布尔；必填 | 拼图生成时的空间扩展兼容行为 |
| `project_start_to_heightmap` | Heightmap 枚举；可选 | 如 WORLD_SURFACE_WG；将起点投影到高度图 |
| `max_distance_from_center` | 整数 1～128；必填 | 最大中心扩展距离，单位方块；与地形适配还有联合限制 |
| `pool_aliases` | 数组；可选；空 | 本次生成时临时重映射模板池，DS 原文件未配置 |
| `dimension_padding` | 非负整数或对象；可选；0 | 与维度上下边界的留白；对象 bottom/top 各默认 0 |
| `liquid_settings` | 枚举；可选；apply_waterlogging | apply_waterlogging / ignore_waterlogging；DS 多数显式忽略含水状态 |

spawn_overrides 的键为 monster、creature、ambient、axolotls、underground_water_creature、water_creature、water_ambient、misc 等类别。每项必填 `bounding_box`（full / piece）及 `spawns` 数组；spawns 每项用 `type` 实体 ID、`weight` 权重、`minCount` / `maxCount` 数量。空 spawns 表示该类别在覆盖范围内不使用常规生成列表，不是“不覆盖”。

start_height 的常用格式：固定高度 `{"absolute":0}`；均匀高度 `{"type":"minecraft:uniform","min_inclusive":{"absolute":-30},"max_inclusive":{"absolute":-10}}`。高度锚点可用 absolute、above_bottom、below_top 三选一。投影高度图后 start_height 作为相应偏移参与定位，不能只看 absolute:0 就认为建筑一定放在 Y=0。

pool_aliases 每项按 type 分派：`minecraft:direct` 必填 alias、target（模板池 ID）；`minecraft:random` 必填 alias、targets（`{data:池ID,weight:正整数}` 数组）；`minecraft:random_group` 必填 groups（`{data:别名绑定数组,weight:正整数}` 数组）。DS 默认不需要这些扩展。

```json
{
  "type": "minecraft:jigsaw",
  "biomes": "#minecraft:is_overworld",
  "step": "surface_structures",
  "spawn_overrides": {},
  "terrain_adaptation": "beard_thin",
  "start_pool": "dswof:sky_shrine",
  "size": 1,
  "start_height": {"absolute": 0},
  "project_start_to_heightmap": "WORLD_SURFACE_WG",
  "max_distance_from_center": 64,
  "use_expansion_hack": false
}
```

此例需另定义 dswof:sky_shrine 模板池并提供 NBT 结构；不构成可单独加载的完整建筑包。

## worldgen/template_pool：拼图模板池

路径 `data/<ns>/worldgen/template_pool/<id>.json`。

| 字段 | 类型 / 必填 | 作用 |
| --- | --- | --- |
| `fallback` | 模板池 ID；必填 | 正常候选失败时使用的回退池，常用 minecraft:empty |
| `elements` | 数组；必填 | 各项必填 weight（1～150）与 element |

原文件的顶层 `name` 不被当前 CODEC 读取，实际 ID 由文件路径决定。

DS 用的 element_type 是 `minecraft:single_pool_element`：

| element 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `element_type` | ID；必填 | 元素分派类型 |
| `location` | 结构 NBT ID；必填 | dswof:sky_shrine/main 对应 data/dswof/structure/sky_shrine/main.nbt |
| `processors` | 处理器列表 ID 或直接定义；必填 | 如 minecraft:empty 或 dragonsurvival:mossy_age |
| `projection` | 枚举；必填 | rigid 固定建筑几何；terrain_matching 随地形调整 |
| `override_liquid_settings` | 枚举；可选 | 对这个元素覆盖结构的液体处理方式 |

weight 是同池候选的相对权重。模板内部拼图方块的 pool / name / target / joint 决定后续连接；JSON 本身不包含所有房间的几何信息。

## worldgen/processor_list：放置时方块替换

路径 `data/<ns>/worldgen/processor_list/<id>.json`。顶层 `processors` 必填数组。DS 实际只用 `processor_type: minecraft:rule`，其 `rules` 必填规则数组。

| rules 每项字段 | 类型 / 必填 / 默认 | 含义 |
| --- | --- | --- |
| `input_predicate` | RuleTest；必填 | 匹配 NBT 模板中的原始方块 |
| `location_predicate` | RuleTest；必填 | 匹配世界当前位置原有方块 |
| `position_predicate` | PosRuleTest；可选；always_true | 按模板相对位置判断；注意名称不是 pos_predicate |
| `output_state` | BlockState；必填 | 输出方块，Name 必填方块 ID，Properties 可选状态键 → 字符串 |
| `block_entity_modifier` | 对象；可选；passthrough | 处理输出方块实体数据；DS 当前文件未使用 |

DS 使用的 RuleTest：`{"predicate_type":"minecraft:random_block_match","block":"minecraft:stone","probability":0.2}`，检查原方块并按概率匹配；`{"predicate_type":"minecraft:always_true"}` 无附加参数。position_predicate 的默认类型也叫 minecraft:always_true，但属于另一个分派注册表。

规则按顺序尝试，第一条匹配后采用其输出。若同一原方块先写 90% 删除、后写 10% 替换，后者只会在先前未匹配时尝试，不能把每条概率当成独立最终占比。

## worldgen/structure_set：结构分布

路径 `data/<ns>/worldgen/structure_set/<id>.json`。顶层必填 `structures` 与 `placement`。structures 每项必填 `structure`（结构 ID）和 `weight`（正整数），在候选位置按权重尝试结构。

### minecraft:random_spread

placement.type 必填此 ID。所有字段：

| 字段 | 类型 / 必填 / 默认 | 含义 |
| --- | --- | --- |
| `salt` | 非负整数；必填 | 给分布随机种子加扰；不是世界种子 |
| `spacing` | 整数 0～4096；必填 | 分布区域尺寸，单位区块 |
| `separation` | 整数 0～4096；必填 | 区域内候选位置的间隔参数；必须 spacing > separation |
| `spread_type` | 枚举；可选；linear | linear / triangular，后者更偏区域中部 |
| `locate_offset` | 三整数数组；可选；[0,0,0] | locate 定位点偏移；各分量绝对值不超过 16 |
| `frequency` | float 0～1；可选；1 | 候选生成频率 |
| `frequency_reduction_method` | 枚举；可选；default | default / legacy_type_1 / legacy_type_2 / legacy_type_3 |
| `exclusion_zone` | 对象；可选 | other_set 必填单个 structure_set ID；chunk_count 必填 1～16；排除附近其他集合 |

spacing/separation 是候选布局参数，不保证最终所有建筑之间都有精确固定距离。群系不符、地形失败、其他约束都可能使候选缺失。

### dragonsurvival:advanced_random_spread

DS 注册的扩展 placement。包含上述共同字段，但 spacing、separation 允许 0～2147483647；仍必须 spacing > separation。额外字段：

| 字段 | 类型 / 默认 | 实际行为 |
| --- | --- | --- |
| `min_distance_from_world_origin` | 可选非负整数 | 距 X=0、Z=0 的最小水平距离，单位方块 |
| `super_exclusion_zone` | 可选对象 | 支持排除多个结构集合 |

super_exclusion_zone：`other_set` 必填结构集合的注册表集合（可为 #标签）；`chunk_count` 必填 ≥1 的整数；`allowed_chunk_count` 可选 ≥1 的整数。当前实现只对 chunk_count 范围内的其他集合执行排除；allowed_chunk_count 后续分支最终都返回 false，不能把它当成有效的外圈限定功能。

**原版数据中的无效字段：** 当前 JAR 部分文件使用 `type: minecraft:random_spread`，却带 `super_exclusion_zone`。原版 CODEC 不读取这个扩展字段，这些文件中的该字段不会产生 DS 扩展排除效果。必须由正确的 type 选中 DS CODEC；仅写字段名不能启用功能。

不要直接把全部原文件改成 advanced 类型：排除规则会改变结构密度，应先在新世界验证期望结果。

## 六个内置数据包

这些文件位于 JAR 的 `data/dragonsurvival/datapacks/<包名>/data/...`。该目录中的内层 data 只有在对应包被选入时才参与资源加载；不是外层普通目录递归加载。pack.mcmeta 的 pack_format 为 48。

| 包名 | JSON 数 | 内容 | 注册与启用策略 |
| --- | --- | --- | --- |
| ancient_stage | 1 | 新增古龙阶段 | PackSource.DEFAULT，允许禁用；来源策略支持自动加入 |
| ancient_stage_no_crushing | 1 | 古龙阶段的无踩踏版本 | PackSource.FEATURE，允许禁用；来源策略不自动加入 |
| unlock_wings | 3 | 覆盖三龙种翅膀技能的解锁配置 | DEFAULT，允许禁用；来源策略支持自动加入 |
| no_penalties | 6 | 覆盖三个技能标签、三个弱点标签 | FEATURE，允许禁用；来源策略不自动加入 |
| create | 1 | Create 巧克力龙宝藏掉落 | 仅安装 create 时注册；BUILT_IN，alwaysActive=true |
| silentgems | 16 | 宝石龙宝藏掉落 | 仅安装 silentgems 时注册；BUILT_IN，alwaysActive=true |

以上均用 Pack.Position.TOP 注册。`alwaysActive=false` 意味着可禁用，**不等于默认关闭**；DEFAULT 和 BUILT_IN 的 shouldAddAutomatically 为 true，FEATURE 为 false。已有世界保存的数据包选择会影响实际状态，请用 `/datapack list enabled` 与 `/datapack list available` 查看。

两个古龙包修改相同 ID，启用两者会按优先级覆盖，不会合成两种古龙阶段。no_penalties 的效果通过标签替换实现；其他包或附属若又往同标签追加，最终结果还受加载优先级影响。

查这些包内文件的参数时，按内层目录查同一份手册：ancient → [成长阶段](03-stages.md)，wings → [技能](../06-dragon-ability-json-reference.md)，标签 → [标签文档](07-tags-recipes-and-loot.md)，兼容掉落 → [战利品文档](07-tags-recipes-and-loot.md)。

## 附属内置数据包的注册

在模组资源中放置 `data/<namespace>/datapacks/<包名>/pack.mcmeta` 不会自动注册独立数据包。DS 在 MOD 事件总线的 `AddPackFindersEvent` 中显式注册了自己的包；它不会扫描所有模组里的同名目录。把附属目录改为 `dragonsurvival` 也不会自动加入 DS 的包，因为包发现器从所属模组文件中读取指定目录。

本工程由 `DsWof` 构造器注册 `DsWofDataPacks::addPackFinders`，调用：

```java
event.addPackFinders(
    ResourceLocation.fromNamespaceAndPath("dswof", "data/dswof/datapacks/unlock_wings"),
    PackType.SERVER_DATA,
    Component.literal("DS-WOF - Unlock Wings"),
    PackSource.DEFAULT,
    false,
    Pack.Position.TOP
);
```

第一个参数的 namespace 标识存放数据包的模组，path 是相对该模组资源根目录的包目录。内层 `data/dswof/dragonsurvival/dragon_ability/ice_wings.json` 才决定要覆盖的技能 ID 为 `dswof:ice_wings`；`pack.mcmeta` 的 description 仅控制说明文字，改它不会改变包归属或覆盖对象。这里 `PackSource.DEFAULT` 支持自动加入，`false` 表示允许禁用，`TOP` 让覆盖包的默认优先级高于基础数据。

注册后的包 ID 为 `mod/dswof:data/dswof/datapacks/unlock_wings`，与原版 DS 的包分别启用。重启开发客户端后检查 `/datapack list enabled`；若仅在 available 中，执行 `/datapack enable "mod/dswof:data/dswof/datapacks/unlock_wings" last`，再退出并重新进入世界以加载 DS 动态注册表。已有技能实例的等级处理见[技能实例调试](../15-development-validation.md#5-技能定义与玩家技能实例)。

无需 Java 注册的独立数据包，应将含 `pack.mcmeta` 和 `data/` 的完整包目录放在世界的 `datapacks/` 下，再在该世界启用。

## 未加载的遗留目录

| 路径 | 文件 | 状态 |
| --- | --- | --- |
| data/dragonsurvival/advancement_unused/ | fear_me.json、fly_high.json | 不在 advancement 标准路径，不会按普通进度自动加载 |
| data/dragonsurvival/unused/enchantment/ | dragonsbonk.json、dragonsboon.json、shrink.json | 不在 enchantment 标准路径，不是当前有效附魔 |

逐文件索引也保留这 5 个 JSON，避免把“JAR 存在”误认为“游戏加载”。它们的历史字段不代表当前 CODEC 支持；若移入正式目录，须按当前进度 / 附魔格式重新核对，不能直接视为有效范例。

源码核对：DS `registry/DSStructurePlacementTypes.java`、`registry/datagen/AdvancedRandomSpread.java`、`registry/datagen/DataGeneration.java`；Minecraft `JigsawStructure`、`StructureSettings`、`StructureTemplatePool`、`SinglePoolElement`、`ProcessorRule`、`RandomSpreadStructurePlacement`、`PackSource`。固定源码入口见[总目录](README.md#核对来源与验证范围)。
