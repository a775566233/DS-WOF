# 标签、伤害类型、配方与掉落

[返回总目录](README.md)。这里说明 DS JAR 实际使用的 Minecraft / NeoForge 数据格式及 DS 扩展；全部具体文件见[逐文件索引](10-file-index.md)。参数以 1.21.1 为准。

## tags：给注册表条目分组

路径为 `data/<ns>/tags/<注册表路径>/<tag>.json`。例如物品用 `tags/item/`；技能用 `tags/dragonsurvival/dragon_ability/`；生物群系用 `tags/worldgen/biome/`。

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `values` | 数组；必填 | 成员 ID、以 `#` 开头的同注册表标签，或下述可选条目 |
| `replace` | 布尔；可选；false | true 时替换低优先级包中的同名标签成员；否则追加 |
| `remove` | 数组；NeoForge 扩展，可选 | 从先前包贡献的成员中移除指定条目；格式与 values 相同 |

values 的对象形式：`{"id":"othermod:optional_item","required":false}`。id 必填；required 默认 true，false 表示未安装对应内容时允许跳过。标签不执行 Java 行为；只有代码、谓词或其他数据引用这个标签，成员才会影响相应系统。

```json
{
  "replace": false,
  "values": [
    "dswof:sky_dragon",
    {"id": "othermod:another_dragon", "required": false}
  ]
}
```

例如把它保存到 `data/dragonsurvival/tags/dragonsurvival/dragon_species/true_dragons.json`，就向 DS 的真龙分组追加龙种。不要把物品 ID 写进龙种标签。

### DS 标签在各系统中的作用

| 标签 / 标签组 | 用途 |
| --- | --- |
| `dragon_species/all`、`cave_dragons`、`forest_dragons`、`sea_dragons`、`true_dragons`、`none` | 龙种分组，供能力、谓词、兼容数据引用；none 默认空集合 |
| 各注册表的 `order` | 界面排序提示；新增条目应按需要加入，而非把 order 当作注册声明 |
| `dragon_ability/cave_dragon`、`forest_dragon`、`sea_dragon` | 各原版龙种的技能集合；species 引用它们 |
| `dragon_ability/test_abilities` | 测试能力集合；存在不代表默认给玩家 |
| `dragon_penalty/cave_dragon`、`forest_dragon`、`sea_dragon` | 各原版龙种的弱点集合 |
| block 的 `speeds_up_*_dragon`、`is_wet`、`is_grassy`、`warm_blocks` | 技能 / 弱点谓词中的地面、潮湿、草木、温暖分组 |
| block 的 `dragon_ore_drop`、`general_ores` | 龙矿物额外掉落与矿物相关规则 |
| `large_dragon_destructible` | 大龙踩踏破坏的可破坏方块集合；仍受阶段条件约束 |
| `end_platform_non_replaceable` | 末地平台放置时保护的方块 |
| block / item 的 `dragon_altars`、`dragon_treasures`、`dragon_bones`、`wooden_dragon_doors` 等 | 对应方块 / 物品家族识别，供配方、进度、交互与兼容使用 |
| `enables_hunter_effect` | 启用猎手相关效果的方块条件 |
| item 的 `*_dragon_food`、`charred_food`、`cold_items`、`cooked_mushrooms`、`cooked_vegetables`、`dragon_corals` | 饮食 / 转换 / 条件分组；实际可食属性以 diet_entries 为准 |
| `claw_weapons`、`light_source`、`activates_dragon_beacon`、`primordial_anchor_fuel` | 爪槽武器、光源、信标激活物、锚燃料 |
| `dark_armor`、`light_armor`、`uncommon_armor`、`rare_armor`、`epic_armor` | 护甲系列与品质分组；空标签可以作为扩展入口 |
| item 的 `enchantable/chest_armor_and_elytra` | 附魔可应用的胸甲 / 鞘翅集合 |
| entity_type 的 `animal_avoid_blacklist` | 不受常规动物畏龙避让影响的实体 |
| `can_fly`、`charged_spread_blacklist`、`dragons` | 飞行实体、感电传播排除、龙实体分组；玩家龙种另由 DragonPredicate 判断 |
| `drops_dragon_heart_shard`、`drops_weak_dragon_heart`、`drops_elder_dragon_heart` | 龙心掉落过滤，白名单 / 黑名单方向由配置决定 |
| `hunter_faction`、`hunter_targets`、`vehicle_whitelist` | 猎人阵营、猎物、允许骑乘的载具 |
| enchantment 的 `exclusive_set/*`、`leader_books` | 互斥附魔组与首领交易书籍集合 |
| mob_effect 的 `overwhelming_might_blacklist`、`unbreakable_spirit_blacklist` | 排除对应附魔对药效强度的修改 |
| villager_profession 的 `pillage_blacklist` | 掠夺相关交互排除的村民职业 |
| biome 的 `is_*_spawn`、`compat_is_wythers` | 结构允许生成的群系 / 兼容集合 |
| structure 的 `dark_treasure`、`light_treasure`、`dragon_skeletons`、`hunter_treasure`、`treasure_end`、`on_dragon_hunter_maps` | 结构类别与藏宝图目标 |
| structure_set 的 `all_friendly_treasure`、`underground_avoid*` | 世界生成间距 / 排除引用集合；是否生效由 placement 类型决定 |
| damage_type 的 `dragon_breath`、`dragon_magic`、`is_electric`、`water_burn`、`anti_dragon` | DS 的伤害分类，供免疫、加成和条件引用 |

JAR 同时向 `minecraft` 和 `c` 命名空间追加标签，效果取决于各自原版 / 通用标签的消费者。逐文件索引列出这些文件，不能只检索 `data/dragonsurvival/`。

## damage_type：伤害类型

路径 `data/<ns>/damage_type/<id>.json`。DS 当前有 16 个定义，均采用原版格式。

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `message_id` | 字符串；必填 | 死亡消息翻译键的中间部分，如 `death.attack.<message_id>` |
| `scaling` | 字符串；必填 | 难度缩放：`never`、`always`、`when_caused_by_living_non_player` |
| `exhaustion` | float；必填 | 受伤产生的饥饿消耗 |
| `effects` | 字符串；可选；`hurt` | 受伤音效 / 反馈类别：`hurt`、`thorns`、`drowning`、`burning`、`poking`、`freezing` |
| `death_message_type` | 字符串；可选；`default` | `default`、`fall_variants`、`intentional_game_design` |

damage_type 文件只描述伤害元数据；是否绕甲、火焰伤害、无敌帧等主要由 `tags/damage_type/` 决定。命名为 ice 或 fire 不会自动冻结、点燃或绕甲。技能中要显式引用这个 ID，并另外配置需要的点燃 / 药水效果。

## recipe：配方

路径 `data/<ns>/recipe/<id>.json`。DS 此版本的配方均使用原版 serializer。通用 `type` 必填；`group` 可选，默认空，用于配方书分组；`category` 可选，决定配方书分类；`neoforge:conditions` 是加载条件。

1.21.1 的输出 ItemStack 使用 `{"id":"minecraft:stone","count":1}`；count 默认 1，components 可选。输入 Ingredient 使用 `{"item":"minecraft:stone"}`、`{"tag":"c:stones"}`，或 Ingredient 数组表示候选之一。原版 Ingredient 对象中的 `count` 不是消耗数量参数；部分旧文件虽然写了 count，也不会让一个槽消耗多份材料。

| type | 全部类型字段 | 说明 |
| --- | --- | --- |
| `minecraft:crafting_shaped` | `pattern`：必填字符串数组；`key`：必填符号 → Ingredient；`result`：必填 ItemStack；`show_notification`：可选布尔，默认 true | 有序合成；pattern 各行等宽，最多 3×3；空格表示空槽，key 不定义空格。category 为 building / redstone / equipment / misc，默认 misc |
| `minecraft:crafting_shapeless` | `ingredients`：必填非空 Ingredient 数组，最多 9 项；`result`：必填 ItemStack | 无序合成；多个输入槽要重复写条目。category 同上 |
| `minecraft:smithing_transform` | `template`、`base`、`addition`：必填 Ingredient；`result`：必填 ItemStack | 锻造模板、主体、附加物。保留 / 合并主体组件的行为由锻造逻辑决定；不使用 crafting category/group |
| `minecraft:smelting` | `ingredient`：必填 Ingredient；`result`：必填 ItemStack；`experience`：可选 float，默认 0；`cookingtime`：可选整数，默认 200 | 熔炉；category 为 food / blocks / misc，默认 misc |
| `minecraft:smoking` | 同上；cookingtime 默认 100 | 烟熏炉 |
| `minecraft:campfire_cooking` | 同上；cookingtime 默认 600 | 营火 |

type 不写命名空间会按 minecraft 解析，JAR 中有一个 `crafting_shapeless` 采用这种简写；新文件建议写完整 ID。配方 ID 决定覆盖关系，不由输出物品决定：两个不同 ID 的配方可产出同一物品。

```json
{
  "type": "minecraft:crafting_shapeless",
  "category": "misc",
  "ingredients": [
    {"item": "minecraft:coal"},
    {"item": "minecraft:blaze_powder"}
  ],
  "result": {"id": "minecraft:fire_charge", "count": 1}
}
```

## loot_table：战利品表

路径 `data/<ns>/loot_table/<id>.json`。DS 使用 block、entity、chest、generic 上下文，分别用于方块、生物、箱子及通用表；上下文决定哪些条件参数可用。

| 顶层字段 | 类型 / 默认 | 作用 |
| --- | --- | --- |
| `type` | LootContextParamSet ID；可选；`minecraft:generic` | 上下文参数集合，常见 `minecraft:block` / `entity` / `chest` |
| `pools` | 数组；可选；`[]` | 各奖池独立抽取 |
| `functions` | LootItemFunction 数组；可选；`[]` | 对整表生成的物品应用函数 |
| `random_sequence` | ID；可选 | 指定随机序列 |

每个 pool：`rolls` 必填 NumberProvider；`bonus_rolls` 可选 NumberProvider，默认 0，与运气影响相关；`entries` 必填条目数组；`conditions` 可选条件数组，默认 `[]`；`functions` 可选函数数组，默认 `[]`。

NumberProvider 可以是常数；DS 使用的均匀分布写为 `{"type":"minecraft:uniform","min":1,"max":3}`，min/max 本身也可为数值提供器。抽取整数 / 浮点取决于消费字段，不是所有 uniform 都按浮点数使用。

### DS 文件实际使用的 entry

| type | 字段 | 用途 |
| --- | --- | --- |
| `minecraft:item` | `name` 必填物品 ID；`weight` 可选整数，默认 1；`quality` 可选整数，默认 0；可选 conditions / functions | 参与权重抽取并产出物品 |
| `minecraft:empty` | 可选 weight / quality / conditions / functions | 抽中不产出物品 |
| `minecraft:alternatives` | `children` 必填条目数组；可选 conditions | 按顺序尝试子条目，使用第一个成功展开的分支；不是随机从 children 等概率选择 |

所有 conditions 都要成立。weight 是相对权重，不能直接当成百分比；quality 会让运气影响权重。

### DS 文件实际使用的 condition

| condition | 字段 | 判断 |
| --- | --- | --- |
| `minecraft:survives_explosion` | 无额外字段 | 爆炸掉落存活测试 |
| `minecraft:block_state_property` | `block` 必填方块 ID；`properties` 可选状态匹配对象 | 方块状态，如上下半、层数；属性精确值用字符串，范围用 min/max 字符串 |
| `minecraft:killed_by_player` | 无额外字段 | 玩家击杀 |
| `minecraft:random_chance` | `chance` 必填 NumberProvider | 概率测试；常数 0～1，亦可引用附魔等级等数值提供器 |
| `minecraft:entity_properties` | `entity` 必填上下文目标；`predicate` 可选 EntityPredicate | 目标实体检测；缺少被引用的上下文时不能假定匹配 |

更复杂的 all_of / any_of / inverted 及 DS 子谓词见[条件文档](08-advancements-enchantments-and-conditions.md)。

### DS 文件实际使用的 function

下表每项必须有 `function` 类型 ID；均可附可选 conditions 数组。

| function | 字段 / 默认 | 用途 |
| --- | --- | --- |
| `minecraft:set_count` | `count` 必填 NumberProvider；`add` 可选 false | 设置 / 累加堆叠数 |
| `minecraft:explosion_decay` | 无额外字段 | 按爆炸半径衰减堆叠数量 |
| `minecraft:copy_components` | `source` 必填，此版支持 `block_entity`；`include`、`exclude` 可选组件 ID 列表 | 复制方块实体组件；include 缺省表示不限制包含集合，exclude 用于排除 |
| `minecraft:enchant_randomly` | `options` 可选附魔集合；`only_compatible` 可选 true | 随机附魔；缺省 options 从相应附魔候选中选择 |
| `minecraft:exploration_map` | `destination` 可选结构标签，默认 `minecraft:on_treasure_maps`；`decoration` 可选图标 ID，默认 `minecraft:mansion`；`zoom` 可选 byte，通常 0～4，默认 2；`search_radius` 可选整数，默认 50；`skip_existing_chunks` 可选 true | 将地图变成指向结构的探索图；destination 是标签 ID，字段值不加 # |
| `minecraft:set_name` | `name` 可选文本组件；`entity` 可选解析上下文目标；`target` 可选 `custom_name`（另可 `item_name`） | 设置物品名称；无 name 时清除相应名称组件 |
| `minecraft:set_potion` | `id` 必填 Potion ID | 设置药水内容；不是 MobEffect ID |

同一个词在不同结构中的含义不同：掉落函数 `conditions` 是生成时判断；`neoforge:conditions` 是加载时判断；物品 `components` 是输出数据。

## loot_modifiers：DS 全局掉落修饰器

定义放 `data/<ns>/loot_modifiers/<id>.json`；还需在固定文件 `data/neoforge/loot_modifiers/global_loot_modifiers.json` 的 entries 中登记 ID。全局登记文件必填 `entries`（ID 数组），`replace` 布尔控制是否清空已有登记；附属通常使用 false。

DS 定义的全部 3 类：

| type | 参数 | 行为 |
| --- | --- | --- |
| `dragonsurvival:add_table_loot_extended` | `conditions` 必填 loot condition 数组；`table` 必填额外 LootTable ID；`tables_to_apply` 必填字符串数组；`blacklist` 可选 false | 匹配目标表时追加 table 的掉落；不替换已生成物品。字符串支持精确完整表 ID 或 Java 正则 |
| `dragonsurvival:dragon_heart` | `conditions` 必填数组 | 按代码、配置、实体最大生命及过滤标签追加龙心类物品；没有 JSON 的 chance / health_threshold 参数 |
| `dragonsurvival:dragon_ore` | `conditions` 必填数组 | 按矿物标签、是否龙玩家、时运与代码配置追加龙尘 / 龙骨；没有 JSON 的 chance / count 参数 |

add_table_loot_extended 的 blacklist false 表示只处理列表匹配的表；true 表示排除匹配的表。原版用 `minecraft:chests/.*` 给原版箱子表追加 `dragonsurvival:chests/dragon_chest_loot`。正则匹配完整 ID，不是路径片段。新增规则应排除自己的追加表，避免循环调用。

`_comment`、`_comment2` 等是原版文件里的说明性额外键，不是游戏参数；JSON 标准不支持 `//` 注释。

源码：`registry/datagen/tags/`、`registry/datagen/DSRecipes.java`、`registry/datagen/loot/`、`DSDamageTypes.java`；通用字段核对 Minecraft 的 recipe / loot CODEC 与 NeoForge Global Loot Modifier、Tag 扩展。
