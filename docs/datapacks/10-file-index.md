# Dragon Survival 数据包 JSON 逐文件索引

[返回总目录](README.md)。基线为本工程实际依赖 Dragon Survival **2.0.71** JAR。

共 **877 个** `data/**/*.json`，包含所有命名空间、六个内置包的 28 个 JSON，以及 5 个未加载的遗留 JSON。每个完整路径只登记一次。

每行给出具体文件的用途 / 当前配置摘要及完整参数手册入口。同格式文件共享字段说明；摘要不是完整配置，也不将文件名或翻译名称视为实际行为的保证。

内置包中的路径需要启用相应包才参与加载，详见[启用策略与遗留说明](09-worldgen-and-built-in-packs.md)。

索引源 JAR SHA-256：`22406fdf0dd16063826f79a737cc6371c7faf6e07cb8369b379c61032d4cc190`。

## 数量统计

| 分组 | JSON 数 |
| --- | ---: |
| Data Map / data_maps | 5 |
| 世界生成 / worldgen | 59 |
| 伤害类型 / damage_type | 16 |
| 体型 / dragon_body | 6 |
| 全局掉落 / loot_modifiers | 4 |
| 内置包 / ancient_stage | 1 |
| 内置包 / ancient_stage_no_crushing | 1 |
| 内置包 / create | 1 |
| 内置包 / no_penalties | 6 |
| 内置包 / silentgems | 16 |
| 内置包 / unlock_wings | 3 |
| 弱点 / dragon_penalty | 9 |
| 成长阶段 / dragon_stage | 3 |
| 战利品表 / loot_table | 226 |
| 技能 / dragon_ability | 61 |
| 投射物 / projectile_data | 4 |
| 未加载遗留进度 | 2 |
| 未加载遗留附魔 | 3 |
| 标签 / tags | 116 |
| 表情集 / dragon_emote_set | 1 |
| 进度 / advancement | 153 |
| 配方 / recipe | 166 |
| 附魔 / enchantment | 12 |
| 龙种 / dragon_species | 3 |

可在编辑器搜索完整文件名、ID 路径或中文译名。

## Data Map / data_maps

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/data_maps/dragonsurvival/dragon_body/body_icons.json` | 体型图标；配置键：dragonsurvival:center、dragonsurvival:east、dragonsurvival:north、dragonsurvival:south、dragonsurvival:west | [Data Map参数](06-data-maps.md) |
| `data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json` | 龙种饮食；配置键：dragonsurvival:cave_dragon、dragonsurvival:forest_dragon、dragonsurvival:sea_dragon | [Data Map参数](06-data-maps.md) |
| `data/dragonsurvival/data_maps/dragonsurvival/dragon_species/dragon_beacon_data.json` | 龙种信标效果与支付；配置键：#dragonsurvival:all、dragonsurvival:cave_dragon、dragonsurvival:forest_dragon、dragonsurvival:sea_dragon | [Data Map参数](06-data-maps.md) |
| `data/dragonsurvival/data_maps/dragonsurvival/dragon_species/end_platforms.json` | 龙种末地平台；配置键：dragonsurvival:cave_dragon、dragonsurvival:forest_dragon、dragonsurvival:sea_dragon | [Data Map参数](06-data-maps.md) |
| `data/dragonsurvival/data_maps/dragonsurvival/dragon_species/stage_resources.json` | 龙种各成长阶段贴图；配置键：dragonsurvival:cave_dragon、dragonsurvival:forest_dragon、dragonsurvival:sea_dragon | [Data Map参数](06-data-maps.md) |

## 世界生成 / worldgen

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/worldgen/processor_list/dragon_skeletons_random.json` | 放置时替换方块；输出：minecraft:air、dragonsurvival:skeleton_dragon_chest_skin4、dragonsurvival:skeleton_dragon_leg_1_skin4、dragonsurvival:skeleton_dragon_leg_3_skin4、dragonsurvival:skeleton_dragon_neck_3_skin4 等 8 项 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/processor_list/end_replace.json` | 放置时替换方块；输出：minecraft:air | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/processor_list/mossy_age.json` | 放置时替换方块；输出：minecraft:mossy_stone_bricks、minecraft:cracked_stone_bricks、minecraft:cracked_deepslate_bricks、minecraft:cobblestone | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/processor_list/obsidian_replace_cave.json` | 放置时替换方块；输出：minecraft:diamond_block、minecraft:redstone_block、minecraft:raw_iron_block、minecraft:raw_copper_block、minecraft:obsidian 等 7 项 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/processor_list/obsidian_replace_cave_nether.json` | 放置时替换方块；输出：minecraft:diamond_block、minecraft:ancient_debris、minecraft:air | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/processor_list/obsidian_replace_forest.json` | 放置时替换方块；输出：minecraft:emerald_block、minecraft:diamond_block、minecraft:slime_block、minecraft:honey_block、minecraft:spore_blossom 等 8 项 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/processor_list/obsidian_replace_forest_nether.json` | 放置时替换方块；输出：minecraft:emerald_block、minecraft:diamond_block、minecraft:ancient_debris、minecraft:air | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/processor_list/obsidian_replace_sea.json` | 放置时替换方块；输出：minecraft:gold_block、minecraft:diamond_block、minecraft:lapis_block、minecraft:budding_amethyst、minecraft:blue_ice 等 8 项 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/processor_list/obsidian_replace_sea_nether.json` | 放置时替换方块；输出：minecraft:gold_block、minecraft:diamond_block、minecraft:budding_amethyst、minecraft:raw_gold_block、minecraft:ancient_debris 等 6 项 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/processor_list/sand_age.json` | 放置时替换方块；输出：minecraft:mossy_stone_bricks、minecraft:cracked_stone_bricks、minecraft:cracked_deepslate_bricks、minecraft:sandstone、minecraft:sand | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure/dragon_hunters_castle.json` | 拼图结构；群系=#dragonsurvival:is_dragon_hunters_castle_spawn；起始池=dragonsurvival:dragon_hunters_castle_spawn_pool | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure/dragon_skeleton_cave.json` | 拼图结构；群系=#dragonsurvival:is_dragon_skeleton_cave_spawn；起始池=dragonsurvival:dragon_skeleton_cave_spawn_pool | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure/dragon_skeleton_forest.json` | 拼图结构；群系=#dragonsurvival:is_dragon_skeleton_forest_spawn；起始池=dragonsurvival:dragon_skeleton_forest_spawn_pool | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure/dragon_skeleton_sea.json` | 拼图结构；群系=#dragonsurvival:is_dragon_skeleton_sea_spawn；起始池=dragonsurvival:dragon_skeleton_sea_spawn_pool | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure/treasure_angry_cave.json` | 拼图结构；群系=#dragonsurvival:is_treasure_angry_cave_spawn；起始池=dragonsurvival:treasure_angry_cave_spawn_pool | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure/treasure_angry_forest.json` | 拼图结构；群系=#dragonsurvival:is_treasure_angry_forest_spawn；起始池=dragonsurvival:treasure_angry_forest_spawn_pool | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure/treasure_angry_sea.json` | 拼图结构；群系=#dragonsurvival:is_treasure_angry_sea_spawn；起始池=dragonsurvival:treasure_angry_sea_spawn_pool | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure/treasure_end.json` | 拼图结构；群系=#dragonsurvival:is_treasure_end_spawn；起始池=dragonsurvival:treasure_end_spawn_pool | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure/treasure_friendly_cave.json` | 拼图结构；群系=#dragonsurvival:is_treasure_friendly_cave_spawn；起始池=dragonsurvival:treasure_friendly_cave_spawn_pool | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure/treasure_friendly_forest.json` | 拼图结构；群系=#dragonsurvival:is_treasure_friendly_forest_spawn；起始池=dragonsurvival:treasure_friendly_forest_spawn_pool | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure/treasure_friendly_sea.json` | 拼图结构；群系=#dragonsurvival:is_treasure_friendly_sea_spawn；起始池=dragonsurvival:treasure_friendly_sea_spawn_pool | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure/treasure_hunters.json` | 拼图结构；群系=#dragonsurvival:is_dragon_hunters_castle_spawn；起始池=dragonsurvival:treasure_dungeon_hunters_spawn_pool | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure_set/dragon_hunters_castle_with_all.json` | 分布 dragonsurvival:dragon_hunters_castle；spacing=36、separation=30 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure_set/dragon_skeleton_with_all.json` | 分布 dragonsurvival:dragon_skeleton_cave、dragonsurvival:dragon_skeleton_forest、dragonsurvival:dragon_skeleton_sea；spacing=36、separation=1；注意：super_exclusion_zone 被原版类型忽略 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure_set/treasure_angry_with_all.json` | 分布 dragonsurvival:treasure_angry_cave、dragonsurvival:treasure_angry_forest、dragonsurvival:treasure_angry_sea；spacing=36、separation=20；注意：super_exclusion_zone 被原版类型忽略 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure_set/treasure_end_with_all.json` | 分布 dragonsurvival:treasure_end；spacing=36、separation=10；注意：super_exclusion_zone 被原版类型忽略 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure_set/treasure_friendly_with_all.json` | 分布 dragonsurvival:treasure_friendly_cave、dragonsurvival:treasure_friendly_forest、dragonsurvival:treasure_friendly_sea；spacing=36、separation=10；注意：super_exclusion_zone 被原版类型忽略 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/structure_set/treasure_hunters_with_all.json` | 分布 dragonsurvival:treasure_hunters；spacing=36、separation=30；注意：super_exclusion_zone 被原版类型忽略 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/dragon_hunters_castle_additions_pool_1.json` | 拼图候选池；3 项；NBT：dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_additions_1_1、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_additions_1_2、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_additions_1_3 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/dragon_hunters_castle_additions_pool_2.json` | 拼图候选池；3 项；NBT：dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_additions_2_1、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_additions_2_2、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_additions_2_3 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/dragon_hunters_castle_additions_pool_3.json` | 拼图候选池；3 项；NBT：dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_additions_3_1、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_additions_3_2、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_additions_3_3 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/dragon_hunters_castle_house_pool_1.json` | 拼图候选池；3 项；NBT：dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_house_1_1、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_house_1_2、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_house_1_3 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/dragon_hunters_castle_house_pool_2.json` | 拼图候选池；3 项；NBT：dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_house_2_1、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_house_2_2、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_house_2_3 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/dragon_hunters_castle_house_pool_3.json` | 拼图候选池；3 项；NBT：dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_house_3_1、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_house_3_2、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_house_3_3 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/dragon_hunters_castle_spawn_pool.json` | 拼图候选池；3 项；NBT：dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_spawn_1_1、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_spawn_1_2、dragonsurvival:dragon_hunters_castle/dragon_hunters_castle_spawn_1_3 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/dragon_skeleton_cave_spawn_pool.json` | 拼图候选池；9 项；NBT：dragonsurvival:dragon_skeletons/cave_dragon_skeleton_1、dragonsurvival:dragon_skeletons/cave_dragon_skeleton_2、dragonsurvival:dragon_skeletons/cave_dragon_skeleton_3、dragonsurvival:dragon_skeletons/cave_dragon_skeleton_4、dragonsurvival:dragon_skeletons/cave_dragon_skeleton_5 等 9 项 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/dragon_skeleton_forest_spawn_pool.json` | 拼图候选池；9 项；NBT：dragonsurvival:dragon_skeletons/forest_dragon_skeleton_1、dragonsurvival:dragon_skeletons/forest_dragon_skeleton_2、dragonsurvival:dragon_skeletons/forest_dragon_skeleton_3、dragonsurvival:dragon_skeletons/forest_dragon_skeleton_4、dragonsurvival:dragon_skeletons/forest_dragon_skeleton_5 等 9 项 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/dragon_skeleton_sea_spawn_pool.json` | 拼图候选池；8 项；NBT：dragonsurvival:dragon_skeletons/sea_dragon_skeleton_1、dragonsurvival:dragon_skeletons/sea_dragon_skeleton_2、dragonsurvival:dragon_skeletons/sea_dragon_skeleton_3、dragonsurvival:dragon_skeletons/sea_dragon_skeleton_4、dragonsurvival:dragon_skeletons/sea_dragon_skeleton_5 等 8 项 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/hunter_hound_pool.json` | 拼图候选池；14 项；NBT：dragonsurvival:entity/hunter_hound_1、dragonsurvival:entity/hunter_hound_2、dragonsurvival:entity/hunter_hound_3、dragonsurvival:entity/hunter_hound_4、dragonsurvival:entity/hunter_hound_5 等 14 项 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/hunter_knight_pool.json` | 拼图候选池；1 项；NBT：dragonsurvival:entity/hunter_knight | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/hunter_leader_pool.json` | 拼图候选池；1 项；NBT：dragonsurvival:entity/hunter_leader | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/hunter_spearman_pool.json` | 拼图候选池；1 项；NBT：dragonsurvival:entity/hunter_spearman | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/random_villager_pool.json` | 拼图候选池；1 项；NBT：dragonsurvival:entity/random_villager | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_angry_cave_spawn_pool.json` | 拼图候选池；5 项；NBT：dragonsurvival:treasure_angry/treasure_angry_center_cave、dragonsurvival:treasure_angry/treasure_angry_western_cave、dragonsurvival:treasure_angry/treasure_angry_eastern_cave、dragonsurvival:treasure_angry/treasure_angry_northern_cave、dragonsurvival:treasure_angry/treasure_angry_southern_cave | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_angry_forest_spawn_pool.json` | 拼图候选池；5 项；NBT：dragonsurvival:treasure_angry/treasure_angry_center_forest、dragonsurvival:treasure_angry/treasure_angry_western_forest、dragonsurvival:treasure_angry/treasure_angry_eastern_forest、dragonsurvival:treasure_angry/treasure_angry_northern_forest、dragonsurvival:treasure_angry/treasure_angry_southern_forest | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_angry_sea_spawn_pool.json` | 拼图候选池；5 项；NBT：dragonsurvival:treasure_angry/treasure_angry_center_sea、dragonsurvival:treasure_angry/treasure_angry_western_sea、dragonsurvival:treasure_angry/treasure_angry_eastern_sea、dragonsurvival:treasure_angry/treasure_angry_northern_sea、dragonsurvival:treasure_angry/treasure_angry_southern_sea | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_corner_hunters_left_pool_1.json` | 拼图候选池；1 项；NBT：dragonsurvival:treasure_hunters/treasure_corner_hunters_left_1 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_corner_hunters_left_pool_2.json` | 拼图候选池；1 项；NBT：dragonsurvival:treasure_hunters/treasure_corner_hunters_left_2 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_corner_hunters_right_pool_1.json` | 拼图候选池；1 项；NBT：dragonsurvival:treasure_hunters/treasure_corner_hunters_right_1 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_corner_hunters_right_pool_2.json` | 拼图候选池；1 项；NBT：dragonsurvival:treasure_hunters/treasure_corner_hunters_right_2 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_dungeon_hunters_pool_1.json` | 拼图候选池；1 项；NBT：dragonsurvival:treasure_hunters/treasure_dungeon_hunters_1 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_dungeon_hunters_pool_2.json` | 拼图候选池；1 项；NBT：dragonsurvival:treasure_hunters/treasure_dungeon_hunters_2 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_dungeon_hunters_pool_3.json` | 拼图候选池；1 项；NBT：dragonsurvival:treasure_hunters/treasure_dungeon_hunters_3 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_dungeon_hunters_pool_4.json` | 拼图候选池；3 项；NBT：dragonsurvival:treasure_hunters/treasure_dungeon_hunters_4_1、dragonsurvival:treasure_hunters/treasure_dungeon_hunters_4_2、dragonsurvival:treasure_hunters/treasure_dungeon_hunters_4_3 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_dungeon_hunters_spawn_pool.json` | 拼图候选池；4 项；NBT：dragonsurvival:treasure_hunters/treasure_spawn_hunters_1、dragonsurvival:treasure_hunters/treasure_spawn_hunters_2、dragonsurvival:treasure_hunters/treasure_spawn_hunters_3、dragonsurvival:treasure_hunters/treasure_spawn_hunters_4 | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_end_spawn_pool.json` | 拼图候选池；2 项；NBT：dragonsurvival:treasure_end/treasure_end_spin、dragonsurvival:treasure_end/treasure_end_soul | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_friendly_cave_spawn_pool.json` | 拼图候选池；5 项；NBT：dragonsurvival:treasure_friendly/treasure_friendly_center_cave、dragonsurvival:treasure_friendly/treasure_friendly_western_cave、dragonsurvival:treasure_friendly/treasure_friendly_eastern_cave、dragonsurvival:treasure_friendly/treasure_friendly_northern_cave、dragonsurvival:treasure_friendly/treasure_friendly_southern_cave | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_friendly_forest_spawn_pool.json` | 拼图候选池；5 项；NBT：dragonsurvival:treasure_friendly/treasure_friendly_center_forest、dragonsurvival:treasure_friendly/treasure_friendly_western_forest、dragonsurvival:treasure_friendly/treasure_friendly_eastern_forest、dragonsurvival:treasure_friendly/treasure_friendly_northern_forest、dragonsurvival:treasure_friendly/treasure_friendly_southern_forest | [世界生成参数](09-worldgen-and-built-in-packs.md) |
| `data/dragonsurvival/worldgen/template_pool/treasure_friendly_sea_spawn_pool.json` | 拼图候选池；5 项；NBT：dragonsurvival:treasure_friendly/treasure_friendly_center_sea、dragonsurvival:treasure_friendly/treasure_friendly_western_sea、dragonsurvival:treasure_friendly/treasure_friendly_eastern_sea、dragonsurvival:treasure_friendly/treasure_friendly_northern_sea、dragonsurvival:treasure_friendly/treasure_friendly_southern_sea | [世界生成参数](09-worldgen-and-built-in-packs.md) |

## 伤害类型 / damage_type

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/damage_type/anti_dragon.json` | anti_dragon 伤害元数据；消息=dragonsurvival.anti_dragon；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/ball_lightning.json` | ball_lightning 伤害元数据；消息=dragonsurvival.ball_lightning；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/blast_dust.json` | blast_dust 伤害元数据；消息=dragonsurvival.blast_dust；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/burn.json` | burn 伤害元数据；消息=dragonsurvival.burn；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/crushed.json` | crushed 伤害元数据；消息=dragonsurvival.crushed；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/dehydration.json` | dehydration 伤害元数据；消息=dragonsurvival.dehydration；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/dragon_breath.json` | dragon_breath 伤害元数据；消息=dragonsurvival.dragon_breath；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/drain.json` | drain 伤害元数据；消息=dragonsurvival.drain；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/electric.json` | electric 伤害元数据；消息=dragonsurvival.electric；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/fire_breath.json` | fire_breath 伤害元数据；消息=dragonsurvival.fire_breath；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/forest_breath.json` | forest_breath 伤害元数据；消息=dragonsurvival.forest_breath；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/lightning_breath.json` | lightning_breath 伤害元数据；消息=dragonsurvival.lightning_breath；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/mirror_curse.json` | mirror_curse 伤害元数据；消息=dragonsurvival.mirror_curse；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/rain_burn.json` | rain_burn 伤害元数据；消息=dragonsurvival.rain_burn；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/spike.json` | spike 伤害元数据；消息=dragonsurvival.spike；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/damage_type/water_burn.json` | water_burn 伤害元数据；消息=dragonsurvival.water_burn；缩放=when_caused_by_living_non_player；exhaustion=0.1 | [伤害类型参数](07-tags-recipes-and-loot.md) |

## 体型 / dragon_body

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/dragonsurvival/dragon_body/center.json` | 标准龙；模型=默认 dragon_model；动画=dragonsurvival:dragon_center | [体型参数](02-body-and-emotes.md) |
| `data/dragonsurvival/dragonsurvival/dragon_body/east.json` | 东方龙；模型=默认 dragon_model；动画=dragonsurvival:dragon_east | [体型参数](02-body-and-emotes.md) |
| `data/dragonsurvival/dragonsurvival/dragon_body/no_model.json` | no_model；模型=默认 dragon_model；动画=使用对象分支/缺省 | [体型参数](02-body-and-emotes.md) |
| `data/dragonsurvival/dragonsurvival/dragon_body/north.json` | 北方龙；模型=默认 dragon_model；动画=dragonsurvival:dragon_north | [体型参数](02-body-and-emotes.md) |
| `data/dragonsurvival/dragonsurvival/dragon_body/south.json` | 南方龙；模型=默认 dragon_model；动画=dragonsurvival:dragon_south | [体型参数](02-body-and-emotes.md) |
| `data/dragonsurvival/dragonsurvival/dragon_body/west.json` | 西方龙；模型=默认 dragon_model；动画=dragonsurvival:dragon_west | [体型参数](02-body-and-emotes.md) |

## 全局掉落 / loot_modifiers

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/loot_modifiers/dragon_chest_loot.json` | dragonsurvival:add_table_loot_extended；附加表=dragonsurvival:chests/dragon_chest_loot；目标=minecraft:chests/.* | [全局掉落参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_modifiers/dragon_heart.json` | dragonsurvival:dragon_heart；附加表=由类型决定；目标= | [全局掉落参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_modifiers/dragon_ore.json` | dragonsurvival:dragon_ore；附加表=由类型决定；目标= | [全局掉落参数](07-tags-recipes-and-loot.md) |
| `data/neoforge/loot_modifiers/global_loot_modifiers.json` | 登记全局掉落修饰器：dragonsurvival:dragon_chest_loot、dragonsurvival:dragon_ore、dragonsurvival:dragon_heart | [全局掉落参数](07-tags-recipes-and-loot.md) |

## 内置包 / ancient_stage

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/datapacks/ancient_stage/data/dragonsurvival/dragonsurvival/dragon_stage/ancient.json` | 远古；成长区间={'max': 300.0, 'min': 60.0}；自然成长时长=69120000 tick | [成长阶段参数](03-stages.md) |

## 内置包 / ancient_stage_no_crushing

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/datapacks/ancient_stage_no_crushing/data/dragonsurvival/dragonsurvival/dragon_stage/ancient.json` | 远古；成长区间={'max': 300.0, 'min': 60.0}；自然成长时长=69120000 tick | [成长阶段参数](03-stages.md) |

## 内置包 / create

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/datapacks/create/data/dragonsurvival/loot_table/blocks/chocolate_dragon_treasure.json` | blocks/chocolate_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:chocolate_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |

## 内置包 / no_penalties

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/datapacks/no_penalties/data/dragonsurvival/tags/dragonsurvival/dragon_ability/cave_dragon.json` | dragonsurvival/dragon_ability/cave_dragon 分组；追加 0 项：空集合 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/no_penalties/data/dragonsurvival/tags/dragonsurvival/dragon_ability/forest_dragon.json` | dragonsurvival/dragon_ability/forest_dragon 分组；追加 0 项：空集合 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/no_penalties/data/dragonsurvival/tags/dragonsurvival/dragon_ability/sea_dragon.json` | dragonsurvival/dragon_ability/sea_dragon 分组；追加 0 项：空集合 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/no_penalties/data/dragonsurvival/tags/dragonsurvival/dragon_penalty/cave_dragon.json` | dragonsurvival/dragon_penalty/cave_dragon 分组；替换 0 项：空集合 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/no_penalties/data/dragonsurvival/tags/dragonsurvival/dragon_penalty/forest_dragon.json` | dragonsurvival/dragon_penalty/forest_dragon 分组；替换 0 项：空集合 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/no_penalties/data/dragonsurvival/tags/dragonsurvival/dragon_penalty/sea_dragon.json` | dragonsurvival/dragon_penalty/sea_dragon 分组；替换 0 项：空集合 | [标签参数](07-tags-recipes-and-loot.md) |

## 内置包 / silentgems

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/alexandrite_dragon_treasure.json` | blocks/alexandrite_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:alexandrite_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/ammolite_dragon_treasure.json` | blocks/ammolite_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:ammolite_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/black_diamond_dragon_treasure.json` | blocks/black_diamond_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:black_diamond_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/carnelian_dragon_treasure.json` | blocks/carnelian_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:carnelian_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/citrine_dragon_treasure.json` | blocks/citrine_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:citrine_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/heliodor_dragon_treasure.json` | blocks/heliodor_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:heliodor_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/iolite_dragon_treasure.json` | blocks/iolite_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:iolite_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/kyanite_dragon_treasure.json` | blocks/kyanite_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:kyanite_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/moldavite_dragon_treasure.json` | blocks/moldavite_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:moldavite_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/peridot_dragon_treasure.json` | blocks/peridot_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:peridot_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/rose_quartz_dragon_treasure.json` | blocks/rose_quartz_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:rose_quartz_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/ruby_dragon_treasure.json` | blocks/ruby_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:ruby_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/sapphire_dragon_treasure.json` | blocks/sapphire_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:sapphire_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/topaz_dragon_treasure.json` | blocks/topaz_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:topaz_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/turquoise_dragon_treasure.json` | blocks/turquoise_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:turquoise_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/datapacks/silentgems/data/dragonsurvival/loot_table/blocks/white_diamond_dragon_treasure.json` | blocks/white_diamond_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:white_diamond_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |

## 内置包 / unlock_wings

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/datapacks/unlock_wings/data/dragonsurvival/dragonsurvival/dragon_ability/cave_wings.json` | 炽焰之翼；激活=dragonsurvival:passive；效果：dragonsurvival:flight | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/datapacks/unlock_wings/data/dragonsurvival/dragonsurvival/dragon_ability/forest_wings.json` | 自然之翼；激活=dragonsurvival:passive；效果：dragonsurvival:flight | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/datapacks/unlock_wings/data/dragonsurvival/dragonsurvival/dragon_ability/sea_wings.json` | 幽雷之翼；激活=dragonsurvival:passive；效果：dragonsurvival:flight | [技能参数](../06-dragon-ability-json-reference.md) |

## 弱点 / dragon_penalty

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/dragonsurvival/dragon_penalty/cold_weakness.json` | 畏寒；效果=dragonsurvival:take_damage；触发=dragonsurvival:supply | [弱点参数](04-penalties.md) |
| `data/dragonsurvival/dragonsurvival/dragon_penalty/fear.json` | 龙威；效果=dragonsurvival:fear；触发=dragonsurvival:instant | [弱点参数](04-penalties.md) |
| `data/dragonsurvival/dragonsurvival/dragon_penalty/fear_of_darkness.json` | 厌暗；效果=dragonsurvival:mob_effect；触发=dragonsurvival:supply | [弱点参数](04-penalties.md) |
| `data/dragonsurvival/dragonsurvival/dragon_penalty/item_blacklist.json` | 物品限制；效果=dragonsurvival:item_blacklist；触发=dragonsurvival:instant | [弱点参数](04-penalties.md) |
| `data/dragonsurvival/dragonsurvival/dragon_penalty/snowball_weakness.json` | snowball_weakness；效果=dragonsurvival:take_damage；触发=dragonsurvival:hit_by_projectile | [弱点参数](04-penalties.md) |
| `data/dragonsurvival/dragonsurvival/dragon_penalty/thin_skin.json` | 纤薄之肤；效果=dragonsurvival:take_damage；触发=dragonsurvival:supply | [弱点参数](04-penalties.md) |
| `data/dragonsurvival/dragonsurvival/dragon_penalty/water_potion_weakness.json` | water_potion_weakness；效果=dragonsurvival:take_damage；触发=dragonsurvival:item_used | [弱点参数](04-penalties.md) |
| `data/dragonsurvival/dragonsurvival/dragon_penalty/water_splash_potion_weakness.json` | water_splash_potion_weakness；效果=dragonsurvival:take_damage；触发=dragonsurvival:hit_by_water_potion | [弱点参数](04-penalties.md) |
| `data/dragonsurvival/dragonsurvival/dragon_penalty/water_weakness.json` | 畏水；效果=dragonsurvival:take_damage；触发=dragonsurvival:instant | [弱点参数](04-penalties.md) |

## 成长阶段 / dragon_stage

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/dragonsurvival/dragon_stage/adult.json` | 成年；成长区间={'max': 60.0, 'min': 40.0}；自然成长时长=1728000 tick | [成长阶段参数](03-stages.md) |
| `data/dragonsurvival/dragonsurvival/dragon_stage/newborn.json` | 新生；成长区间={'max': 25.0, 'min': 10.0}；自然成长时长=216000 tick | [成长阶段参数](03-stages.md) |
| `data/dragonsurvival/dragonsurvival/dragon_stage/young.json` | 幼年；成长区间={'max': 40.0, 'min': 25.0}；自然成长时长=432000 tick | [成长阶段参数](03-stages.md) |

## 战利品表 / loot_table

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/loot_table/blocks/acacia_dragon_altar.json` | blocks/acacia_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:acacia_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/acacia_dragon_door.json` | blocks/acacia_dragon_door；上下文=minecraft:block；产物：dragonsurvival:acacia_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/amethyst_dragon_altar.json` | blocks/amethyst_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:amethyst_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/bamboo_dragon_altar.json` | blocks/bamboo_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:bamboo_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/birch_dragon_altar.json` | blocks/birch_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:birch_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/birch_dragon_door.json` | blocks/birch_dragon_door；上下文=minecraft:block；产物：dragonsurvival:birch_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/black_knight_helmet.json` | blocks/black_knight_helmet；上下文=minecraft:block；产物：dragonsurvival:black_knight_helmet | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/blackstone_dragon_altar.json` | blocks/blackstone_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:blackstone_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/bone_dragon_altar.json` | blocks/bone_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:bone_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/cave_dragon_door.json` | blocks/cave_dragon_door；上下文=minecraft:block；产物：dragonsurvival:cave_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/cave_dragon_pressure_plate.json` | blocks/cave_dragon_pressure_plate；上下文=minecraft:block；产物：dragonsurvival:cave_dragon_pressure_plate | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/cave_source_of_magic.json` | blocks/cave_source_of_magic；上下文=minecraft:block；产物：dragonsurvival:cave_source_of_magic | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/cherry_dragon_altar.json` | blocks/cherry_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:cherry_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/copper_dragon_treasure.json` | blocks/copper_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:copper_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/crimson_dragon_altar.json` | blocks/crimson_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:crimson_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/crimson_dragon_door.json` | blocks/crimson_dragon_door；上下文=minecraft:block；产物：dragonsurvival:crimson_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/dark_oak_dragon_altar.json` | blocks/dark_oak_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:dark_oak_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/dark_oak_dragon_door.json` | blocks/dark_oak_dragon_door；上下文=minecraft:block；产物：dragonsurvival:dark_oak_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/dark_vault.json` | blocks/dark_vault；上下文=minecraft:block；产物：无直接物品条目 | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/debris_dragon_treasure.json` | blocks/debris_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:debris_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/deepslate_dragon_altar.json` | blocks/deepslate_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:deepslate_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/diamond_dragon_treasure.json` | blocks/diamond_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:diamond_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/dragon_beacon.json` | blocks/dragon_beacon；上下文=minecraft:block；产物：dragonsurvival:dragon_beacon | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/dragon_memory_block.json` | blocks/dragon_memory_block；上下文=minecraft:block；产物：dragonsurvival:dragon_memory_block | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/dragon_pressure_plate.json` | blocks/dragon_pressure_plate；上下文=minecraft:block；产物：dragonsurvival:dragon_pressure_plate | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/dragon_rider_workbench.json` | blocks/dragon_rider_workbench；上下文=minecraft:block；产物：dragonsurvival:dragon_rider_workbench | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/dragon_soul.json` | blocks/dragon_soul；上下文=minecraft:block；产物：dragonsurvival:dragon_soul | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/emerald_dragon_treasure.json` | blocks/emerald_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:emerald_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/endstone_dragon_altar.json` | blocks/endstone_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:endstone_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/forest_dragon_door.json` | blocks/forest_dragon_door；上下文=minecraft:block；产物：dragonsurvival:forest_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/forest_dragon_pressure_plate.json` | blocks/forest_dragon_pressure_plate；上下文=minecraft:block；产物：dragonsurvival:forest_dragon_pressure_plate | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/forest_source_of_magic.json` | blocks/forest_source_of_magic；上下文=minecraft:block；产物：dragonsurvival:forest_source_of_magic | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/gold_dragon_treasure.json` | blocks/gold_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:gold_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/golden_knight_helmet.json` | blocks/golden_knight_helmet；上下文=minecraft:block；产物：dragonsurvival:golden_knight_helmet | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/gothic_dragon_door.json` | blocks/gothic_dragon_door；上下文=minecraft:block；产物：dragonsurvival:gothic_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/gray_knight_helmet.json` | blocks/gray_knight_helmet；上下文=minecraft:block；产物：dragonsurvival:gray_knight_helmet | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/human_pressure_plate.json` | blocks/human_pressure_plate；上下文=minecraft:block；产物：dragonsurvival:human_pressure_plate | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/hunter_vault.json` | blocks/hunter_vault；上下文=minecraft:block；产物：无直接物品条目 | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/ice_dragon_altar.json` | blocks/ice_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:ice_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/iron_dragon_door.json` | blocks/iron_dragon_door；上下文=minecraft:block；产物：dragonsurvival:iron_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/iron_dragon_treasure.json` | blocks/iron_dragon_treasure；上下文=minecraft:block；产物：dragonsurvival:iron_dragon_treasure | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/jungle_dragon_altar.json` | blocks/jungle_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:jungle_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/jungle_dragon_door.json` | blocks/jungle_dragon_door；上下文=minecraft:block；产物：dragonsurvival:jungle_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/legacy_dragon_door.json` | blocks/legacy_dragon_door；上下文=minecraft:block；产物：dragonsurvival:legacy_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/light_vault.json` | blocks/light_vault；上下文=minecraft:block；产物：无直接物品条目 | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/mangrove_dragon_altar.json` | blocks/mangrove_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:mangrove_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/mossy_dragon_altar.json` | blocks/mossy_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:mossy_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/mudbrick_dragon_altar.json` | blocks/mudbrick_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:mudbrick_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/nether_brick_dragon_altar.json` | blocks/nether_brick_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:nether_brick_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/netherrack_dragon_altar.json` | blocks/netherrack_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:netherrack_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/oak_dragon_altar.json` | blocks/oak_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:oak_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/oak_dragon_door.json` | blocks/oak_dragon_door；上下文=minecraft:block；产物：dragonsurvival:oak_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/obsidian_dragon_altar.json` | blocks/obsidian_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:obsidian_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/pale_oak_dragon_altar.json` | blocks/pale_oak_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:pale_oak_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/primordial_anchor.json` | blocks/primordial_anchor；上下文=minecraft:block；产物：无直接物品条目 | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/prismarine_dragon_altar.json` | blocks/prismarine_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:prismarine_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/purpur_dragon_altar.json` | blocks/purpur_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:purpur_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/quartz_dragon_altar.json` | blocks/quartz_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:quartz_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/red_nether_brick_dragon_altar.json` | blocks/red_nether_brick_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:red_nether_brick_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/red_sandstone_dragon_altar.json` | blocks/red_sandstone_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:red_sandstone_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/sandstone_dragon_altar.json` | blocks/sandstone_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:sandstone_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/sea_dragon_door.json` | blocks/sea_dragon_door；上下文=minecraft:block；产物：dragonsurvival:sea_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/sea_dragon_pressure_plate.json` | blocks/sea_dragon_pressure_plate；上下文=minecraft:block；产物：dragonsurvival:sea_dragon_pressure_plate | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/sea_source_of_magic.json` | blocks/sea_source_of_magic；上下文=minecraft:block；产物：dragonsurvival:sea_source_of_magic | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_chest_skin1.json` | blocks/skeleton_dragon_chest_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_chest_skin2.json` | blocks/skeleton_dragon_chest_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_chest_skin3.json` | blocks/skeleton_dragon_chest_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_chest_skin4.json` | blocks/skeleton_dragon_chest_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_chest_skin5.json` | blocks/skeleton_dragon_chest_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_chest_skin6.json` | blocks/skeleton_dragon_chest_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_chest_skin7.json` | blocks/skeleton_dragon_chest_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_chest_skin8.json` | blocks/skeleton_dragon_chest_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_full_skin1.json` | blocks/skeleton_dragon_full_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_full_skin2.json` | blocks/skeleton_dragon_full_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_full_skin3.json` | blocks/skeleton_dragon_full_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_full_skin4.json` | blocks/skeleton_dragon_full_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_full_skin5.json` | blocks/skeleton_dragon_full_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_full_skin6.json` | blocks/skeleton_dragon_full_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_full_skin7.json` | blocks/skeleton_dragon_full_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_full_skin8.json` | blocks/skeleton_dragon_full_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_1_skin1.json` | blocks/skeleton_dragon_leg_1_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_1_skin2.json` | blocks/skeleton_dragon_leg_1_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_1_skin3.json` | blocks/skeleton_dragon_leg_1_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_1_skin4.json` | blocks/skeleton_dragon_leg_1_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_1_skin5.json` | blocks/skeleton_dragon_leg_1_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_1_skin6.json` | blocks/skeleton_dragon_leg_1_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_1_skin7.json` | blocks/skeleton_dragon_leg_1_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_1_skin8.json` | blocks/skeleton_dragon_leg_1_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_2_skin1.json` | blocks/skeleton_dragon_leg_2_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_2_skin2.json` | blocks/skeleton_dragon_leg_2_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_2_skin3.json` | blocks/skeleton_dragon_leg_2_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_2_skin4.json` | blocks/skeleton_dragon_leg_2_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_2_skin5.json` | blocks/skeleton_dragon_leg_2_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_2_skin6.json` | blocks/skeleton_dragon_leg_2_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_2_skin7.json` | blocks/skeleton_dragon_leg_2_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_2_skin8.json` | blocks/skeleton_dragon_leg_2_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_3_skin1.json` | blocks/skeleton_dragon_leg_3_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_3_skin2.json` | blocks/skeleton_dragon_leg_3_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_3_skin3.json` | blocks/skeleton_dragon_leg_3_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_3_skin4.json` | blocks/skeleton_dragon_leg_3_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_3_skin5.json` | blocks/skeleton_dragon_leg_3_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_3_skin6.json` | blocks/skeleton_dragon_leg_3_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_3_skin7.json` | blocks/skeleton_dragon_leg_3_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_leg_3_skin8.json` | blocks/skeleton_dragon_leg_3_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_1_skin1.json` | blocks/skeleton_dragon_neck_1_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_1_skin2.json` | blocks/skeleton_dragon_neck_1_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_1_skin3.json` | blocks/skeleton_dragon_neck_1_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_1_skin4.json` | blocks/skeleton_dragon_neck_1_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_1_skin5.json` | blocks/skeleton_dragon_neck_1_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_1_skin6.json` | blocks/skeleton_dragon_neck_1_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_1_skin7.json` | blocks/skeleton_dragon_neck_1_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_1_skin8.json` | blocks/skeleton_dragon_neck_1_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_2_skin1.json` | blocks/skeleton_dragon_neck_2_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_2_skin2.json` | blocks/skeleton_dragon_neck_2_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_2_skin3.json` | blocks/skeleton_dragon_neck_2_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_2_skin4.json` | blocks/skeleton_dragon_neck_2_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_2_skin5.json` | blocks/skeleton_dragon_neck_2_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_2_skin6.json` | blocks/skeleton_dragon_neck_2_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_2_skin7.json` | blocks/skeleton_dragon_neck_2_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_2_skin8.json` | blocks/skeleton_dragon_neck_2_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_3_skin1.json` | blocks/skeleton_dragon_neck_3_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_3_skin2.json` | blocks/skeleton_dragon_neck_3_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_3_skin3.json` | blocks/skeleton_dragon_neck_3_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_3_skin4.json` | blocks/skeleton_dragon_neck_3_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_3_skin5.json` | blocks/skeleton_dragon_neck_3_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_3_skin6.json` | blocks/skeleton_dragon_neck_3_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_3_skin7.json` | blocks/skeleton_dragon_neck_3_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_neck_3_skin8.json` | blocks/skeleton_dragon_neck_3_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_pelvis_skin1.json` | blocks/skeleton_dragon_pelvis_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_pelvis_skin2.json` | blocks/skeleton_dragon_pelvis_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_pelvis_skin3.json` | blocks/skeleton_dragon_pelvis_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_pelvis_skin4.json` | blocks/skeleton_dragon_pelvis_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_pelvis_skin5.json` | blocks/skeleton_dragon_pelvis_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_pelvis_skin6.json` | blocks/skeleton_dragon_pelvis_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_pelvis_skin7.json` | blocks/skeleton_dragon_pelvis_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_pelvis_skin8.json` | blocks/skeleton_dragon_pelvis_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_1_skin1.json` | blocks/skeleton_dragon_skull_1_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_1_skin2.json` | blocks/skeleton_dragon_skull_1_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_1_skin3.json` | blocks/skeleton_dragon_skull_1_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_1_skin4.json` | blocks/skeleton_dragon_skull_1_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_1_skin5.json` | blocks/skeleton_dragon_skull_1_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_1_skin6.json` | blocks/skeleton_dragon_skull_1_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_1_skin7.json` | blocks/skeleton_dragon_skull_1_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_1_skin8.json` | blocks/skeleton_dragon_skull_1_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_2_skin1.json` | blocks/skeleton_dragon_skull_2_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_2_skin2.json` | blocks/skeleton_dragon_skull_2_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_2_skin3.json` | blocks/skeleton_dragon_skull_2_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_2_skin4.json` | blocks/skeleton_dragon_skull_2_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_2_skin5.json` | blocks/skeleton_dragon_skull_2_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_2_skin6.json` | blocks/skeleton_dragon_skull_2_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_2_skin7.json` | blocks/skeleton_dragon_skull_2_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_skull_2_skin8.json` | blocks/skeleton_dragon_skull_2_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_1_skin1.json` | blocks/skeleton_dragon_tail_1_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_1_skin2.json` | blocks/skeleton_dragon_tail_1_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_1_skin3.json` | blocks/skeleton_dragon_tail_1_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_1_skin4.json` | blocks/skeleton_dragon_tail_1_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_1_skin5.json` | blocks/skeleton_dragon_tail_1_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_1_skin6.json` | blocks/skeleton_dragon_tail_1_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_1_skin7.json` | blocks/skeleton_dragon_tail_1_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_1_skin8.json` | blocks/skeleton_dragon_tail_1_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_2_skin1.json` | blocks/skeleton_dragon_tail_2_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_2_skin2.json` | blocks/skeleton_dragon_tail_2_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_2_skin3.json` | blocks/skeleton_dragon_tail_2_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_2_skin4.json` | blocks/skeleton_dragon_tail_2_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_2_skin5.json` | blocks/skeleton_dragon_tail_2_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_2_skin6.json` | blocks/skeleton_dragon_tail_2_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_2_skin7.json` | blocks/skeleton_dragon_tail_2_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_2_skin8.json` | blocks/skeleton_dragon_tail_2_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_3_skin1.json` | blocks/skeleton_dragon_tail_3_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_3_skin2.json` | blocks/skeleton_dragon_tail_3_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_3_skin3.json` | blocks/skeleton_dragon_tail_3_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_3_skin4.json` | blocks/skeleton_dragon_tail_3_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_3_skin5.json` | blocks/skeleton_dragon_tail_3_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_3_skin6.json` | blocks/skeleton_dragon_tail_3_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_3_skin7.json` | blocks/skeleton_dragon_tail_3_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_3_skin8.json` | blocks/skeleton_dragon_tail_3_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_4_skin1.json` | blocks/skeleton_dragon_tail_4_skin1；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_4_skin2.json` | blocks/skeleton_dragon_tail_4_skin2；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_4_skin3.json` | blocks/skeleton_dragon_tail_4_skin3；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_4_skin4.json` | blocks/skeleton_dragon_tail_4_skin4；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_4_skin5.json` | blocks/skeleton_dragon_tail_4_skin5；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_4_skin6.json` | blocks/skeleton_dragon_tail_4_skin6；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_4_skin7.json` | blocks/skeleton_dragon_tail_4_skin7；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skeleton_dragon_tail_4_skin8.json` | blocks/skeleton_dragon_tail_4_skin8；上下文=minecraft:block；产物：dragonsurvival:star_bone | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/skyrim_dragon_door.json` | blocks/skyrim_dragon_door；上下文=minecraft:block；产物：dragonsurvival:skyrim_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_acacia_dragon_door.json` | blocks/small_acacia_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_acacia_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_birch_dragon_door.json` | blocks/small_birch_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_birch_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_cave_dragon_door.json` | blocks/small_cave_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_cave_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_crimson_dragon_door.json` | blocks/small_crimson_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_crimson_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_dark_oak_dragon_door.json` | blocks/small_dark_oak_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_dark_oak_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_forest_dragon_door.json` | blocks/small_forest_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_forest_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_gothic_dragon_door.json` | blocks/small_gothic_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_gothic_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_iron_dragon_door.json` | blocks/small_iron_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_iron_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_jungle_dragon_door.json` | blocks/small_jungle_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_jungle_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_oak_dragon_door.json` | blocks/small_oak_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_oak_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_sea_dragon_door.json` | blocks/small_sea_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_sea_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_skyrim_dragon_door.json` | blocks/small_skyrim_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_skyrim_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_spruce_dragon_door.json` | blocks/small_spruce_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_spruce_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_stone_dragon_door.json` | blocks/small_stone_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_stone_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/small_warped_dragon_door.json` | blocks/small_warped_dragon_door；上下文=minecraft:block；产物：dragonsurvival:small_warped_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/spruce_dragon_altar.json` | blocks/spruce_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:spruce_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/spruce_dragon_door.json` | blocks/spruce_dragon_door；上下文=minecraft:block；产物：dragonsurvival:spruce_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/stone_dragon_altar.json` | blocks/stone_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:stone_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/stone_dragon_door.json` | blocks/stone_dragon_door；上下文=minecraft:block；产物：dragonsurvival:stone_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/tuff_dragon_altar.json` | blocks/tuff_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:tuff_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/warped_dragon_altar.json` | blocks/warped_dragon_altar；上下文=minecraft:block；产物：dragonsurvival:warped_dragon_altar | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/blocks/warped_dragon_door.json` | blocks/warped_dragon_door；上下文=minecraft:block；产物：dragonsurvival:warped_dragon_door | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/chests/additional_end_loot.json` | chests/additional_end_loot；上下文=minecraft:chest；产物：dragonsurvival:dragon_soul | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/chests/dark_cave.json` | chests/dark_cave；上下文=minecraft:chest；产物：dragonsurvival:elder_dragon_heart、dragonsurvival:charged_coal、minecraft:redstone、minecraft:potion、dragonsurvival:charged_soup 等 8 项 | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/chests/dark_forest.json` | chests/dark_forest；上下文=minecraft:chest；产物：dragonsurvival:elder_dragon_heart、minecraft:enchanted_book、minecraft:rotten_flesh、minecraft:spider_eye、minecraft:glow_berries 等 11 项 | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/chests/dark_sea.json` | chests/dark_sea；上下文=minecraft:chest；产物：dragonsurvival:elder_dragon_heart、minecraft:enchanted_book、minecraft:ink_sac、minecraft:glow_ink_sac、dragonsurvival:frozen_raw_fish 等 8 项 | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/chests/dragon_chest_loot.json` | chests/dragon_chest_loot；上下文=minecraft:chest；产物：dragonsurvival:elder_dragon_bone、dragonsurvival:star_bone、dragonsurvival:elder_dragon_dust | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/chests/end_spin.json` | chests/end_spin；上下文=minecraft:chest；产物：dragonsurvival:wing_grant、dragonsurvival:spin_grant | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/chests/hunter_chest_loot.json` | chests/hunter_chest_loot；上下文=minecraft:chest；产物：dragonsurvival:elder_dragon_bone、minecraft:iron_ingot、minecraft:gold_ingot、minecraft:copper_ingot | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/chests/light_cave.json` | chests/light_cave；上下文=minecraft:chest；产物：dragonsurvival:elder_dragon_heart、dragonsurvival:charged_coal、minecraft:redstone、minecraft:potion、dragonsurvival:charged_soup | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/chests/light_forest.json` | chests/light_forest；上下文=minecraft:chest；产物：dragonsurvival:elder_dragon_heart、minecraft:rotten_flesh、minecraft:spider_eye、minecraft:glow_berries、minecraft:sweet_berries 等 8 项 | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/chests/light_sea.json` | chests/light_sea；上下文=minecraft:chest；产物：dragonsurvival:elder_dragon_heart、minecraft:ink_sac、minecraft:glow_ink_sac、dragonsurvival:frozen_raw_fish、minecraft:pufferfish | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/entities/hunter_ambusher.json` | entities/hunter_ambusher；上下文=minecraft:entity；产物：minecraft:emerald、minecraft:goat_horn、minecraft:arrow | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/entities/hunter_griffin.json` | entities/hunter_griffin；上下文=minecraft:entity；产物：minecraft:feather | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/entities/hunter_knight.json` | entities/hunter_knight；上下文=minecraft:entity；产物：minecraft:emerald、minecraft:iron_ingot、dragonsurvival:gray_knight_helmet、dragonsurvival:golden_knight_helmet、dragonsurvival:black_knight_helmet 等 6 项 | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/entities/hunter_leader.json` | entities/hunter_leader；上下文=minecraft:entity；产物：minecraft:paper、minecraft:feather | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/generic/dark_vault.json` | generic/dark_vault；上下文=minecraft:generic；产物：dragonsurvival:elder_dragon_heart、dragonsurvival:dark_upgrade、minecraft:netherite_scrap | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/generic/hunter_fortress_map.json` | generic/hunter_fortress_map；上下文=minecraft:chest；产物：minecraft:map | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/generic/hunter_vault.json` | generic/hunter_vault；上下文=minecraft:generic；产物：minecraft:gold_ingot、dragonsurvival:dragon_hunter_sword、minecraft:diamond | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/loot_table/generic/light_vault.json` | generic/light_vault；上下文=minecraft:generic；产物：minecraft:gold_ingot、dragonsurvival:elder_dragon_heart、dragonsurvival:light_upgrade、minecraft:diamond | [战利品表参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/loot_table/chest/hunter_chest_loot.json` | chest/hunter_chest_loot；上下文=minecraft:chest；产物：dragonsurvival:elder_dragon_bone、minecraft:iron_ingot、minecraft:gold_ingot、minecraft:copper_ingot | [战利品表参数](07-tags-recipes-and-loot.md) |

## 技能 / dragon_ability

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/dragonsurvival/dragon_ability/amphibious.json` | 水生适应；激活=dragonsurvival:passive；效果：dragonsurvival:swim、dragonsurvival:modifier | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/ball_lightning.json` | 龙霆弹；激活=dragonsurvival:simple；效果：dragonsurvival:projectile | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/burn.json` | 灼烙之痕；激活=dragonsurvival:passive；效果：dragonsurvival:on_attack | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/cave_athletics.json` | 洞穴健将；激活=dragonsurvival:passive；效果：dragonsurvival:potion | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/cave_claws_and_teeth.json` | 利爪与獠牙（洞穴龙）；激活=dragonsurvival:passive；效果：dragonsurvival:harvest_bonus | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/cave_magic.json` | 炽焰魔法；激活=dragonsurvival:passive；效果：dragonsurvival:modifier | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/cave_spin.json` | 炽焰旋冲；激活=dragonsurvival:passive；效果：dragonsurvival:spin | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/cave_wings.json` | 炽焰之翼；激活=dragonsurvival:passive；效果：dragonsurvival:flight | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/cliffhanger.json` | 峭壁探险家；激活=dragonsurvival:passive；效果：dragonsurvival:modifier | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/contrast_shower.json` | 炽寒适应；激活=dragonsurvival:passive；效果：dragonsurvival:modifier | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/diver.json` | 深潜者；激活=dragonsurvival:passive；效果：dragonsurvival:modifier | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/electric_immunity.json` | 幽雷庇护；激活=dragonsurvival:passive；效果：dragonsurvival:damage_modification | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/fire_ball.json` | 龙焰弹；激活=dragonsurvival:simple；效果：dragonsurvival:projectile | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/fire_immunity.json` | 炽焰庇护；激活=dragonsurvival:passive；效果：dragonsurvival:damage_modification | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/forest_athletics.json` | 森林健将；激活=dragonsurvival:passive；效果：dragonsurvival:potion | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/forest_breath.json` | 瘴疬之息；激活=dragonsurvival:channeled；效果：dragonsurvival:damage、dragonsurvival:potion、dragonsurvival:breath_particles | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/forest_claws_and_teeth.json` | 利爪与獠牙（森林龙）；激活=dragonsurvival:passive；效果：dragonsurvival:harvest_bonus | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/forest_immunity.json` | 枝叶庇护；激活=dragonsurvival:passive；效果：dragonsurvival:damage_modification | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/forest_magic.json` | 自然魔法；激活=dragonsurvival:passive；效果：dragonsurvival:modifier | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/forest_spin.json` | 自然旋冲；激活=dragonsurvival:passive；效果：dragonsurvival:spin | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/forest_wings.json` | 自然之翼；激活=dragonsurvival:passive；效果：dragonsurvival:flight | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/friendly_fire.json` | 友火庇护；激活=dragonsurvival:simple；效果：dragonsurvival:potion、dragonsurvival:particle | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/furnace_heat.json` | 熔炼之火；激活=dragonsurvival:channeled；效果：dragonsurvival:smelting、dragonsurvival:damage、dragonsurvival:ignite、dragonsurvival:breath_particles | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/hunter.json` | 猎影潜行；激活=dragonsurvival:simple；效果：dragonsurvival:potion、dragonsurvival:particle | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/hydration.json` | 水分储备；激活=dragonsurvival:passive；效果：dragonsurvival:modifier | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/inspiration.json` | 灵感激发；激活=dragonsurvival:simple；效果：dragonsurvival:potion、dragonsurvival:particle | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/lava_swimming.json` | 畅游熔岩；激活=dragonsurvival:passive；效果：dragonsurvival:swim | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/lava_vision.json` | 熔岩视界；激活=dragonsurvival:simple；效果：dragonsurvival:potion | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/light_in_darkness.json` | 光明长存；激活=dragonsurvival:passive；效果：dragonsurvival:modifier | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/nether_breath.json` | 炽焰之息；激活=dragonsurvival:channeled；效果：dragonsurvival:damage、dragonsurvival:ignite、dragonsurvival:potion、dragonsurvival:fire、dragonsurvival:block_break 等 6 项 | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/ore_glow.json` | 矿脉幽光；激活=dragonsurvival:simple；效果：dragonsurvival:block_vision | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/sea_athletics.json` | 海洋健将；激活=dragonsurvival:passive；效果：dragonsurvival:potion | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/sea_claws_and_teeth.json` | 利爪与獠牙（海洋龙）；激活=dragonsurvival:passive；效果：dragonsurvival:harvest_bonus | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/sea_eyes.json` | 深海视界；激活=dragonsurvival:simple；效果：dragonsurvival:potion | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/sea_magic.json` | 幽雷魔法；激活=dragonsurvival:passive；效果：dragonsurvival:modifier | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/sea_spin.json` | 幽雷旋冲；激活=dragonsurvival:passive；效果：dragonsurvival:spin | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/sea_wings.json` | 幽雷之翼；激活=dragonsurvival:passive；效果：dragonsurvival:flight | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/soul_revelation.json` | 灵魂启示；激活=dragonsurvival:simple；效果：dragonsurvival:modifier、dragonsurvival:particle | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/spectral_impact.json` | 幽光震荡；激活=dragonsurvival:passive；效果：dragonsurvival:modifier | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/spike.json` | 飞刺；激活=dragonsurvival:simple；效果：dragonsurvival:projectile | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/storm_breath.json` | 幽雷之息；激活=dragonsurvival:channeled；效果：dragonsurvival:damage、dragonsurvival:potion、dragonsurvival:area_cloud、dragonsurvival:breath_particles | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/sturdy_skin.json` | 刚鳞铠护；激活=dragonsurvival:simple；效果：dragonsurvival:potion、dragonsurvival:particle | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/sun_breath.json` | 太阳之息；激活=dragonsurvival:channeled；效果：dragonsurvival:item_conversion、dragonsurvival:bonemeal、dragonsurvival:conversion、dragonsurvival:breath_particles | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_climbable.json` | test_climbable；激活=dragonsurvival:passive；效果：dragonsurvival:climbable | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_convert_items.json` | test_convert_items；激活=dragonsurvival:simple；效果：dragonsurvival:item_conversion | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_cooldown_recovery.json` | test_cooldown_recovery；激活=dragonsurvival:simple；效果：dragonsurvival:cooldown_recovery | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_damage_expression.json` | test_damage_expression；激活=dragonsurvival:passive；效果：dragonsurvival:damage | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_entity_projectile.json` | test_entity_projectile；激活=dragonsurvival:simple；效果：dragonsurvival:projectile | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_experience_points_add.json` | test_experience_points_add；激活=dragonsurvival:passive；效果：dragonsurvival:experience | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_function.json` | test_function；激活=dragonsurvival:passive；效果：dragonsurvival:run_function | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_glow.json` | test_glow；激活=dragonsurvival:passive；效果：dragonsurvival:glow | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_heal.json` | test_heal；激活=dragonsurvival:passive；效果：dragonsurvival:heal | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_on_key_pressed.json` | test_on_key_pressed；激活=dragonsurvival:passive；效果：dragonsurvival:explosion | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_ore_vision_simple_shader.json` | test_ore_vision_simple_shader；激活=dragonsurvival:passive；效果：dragonsurvival:block_vision | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_oxygen_bonus.json` | test_oxygen_bonus；激活=dragonsurvival:passive；效果：dragonsurvival:oxygen_bonus | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_reserve_mana.json` | test_reserve_mana；激活=dragonsurvival:passive；效果：dragonsurvival:damage_modification | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_reserve_mana_second.json` | test_reserve_mana_second；激活=dragonsurvival:passive；效果：dragonsurvival:damage_modification | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_smelt.json` | test_smelt；激活=dragonsurvival:passive；效果：dragonsurvival:smelting | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_summon.json` | test_summon；激活=dragonsurvival:simple；效果：dragonsurvival:summon_entity | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_teleport.json` | test_teleport；激活=dragonsurvival:simple；效果：dragonsurvival:teleport、dragonsurvival:particle | [技能参数](../06-dragon-ability-json-reference.md) |
| `data/dragonsurvival/dragonsurvival/dragon_ability/test_teleport_directional.json` | test_teleport_directional；激活=dragonsurvival:simple；效果：dragonsurvival:teleport、dragonsurvival:particle | [技能参数](../06-dragon-ability-json-reference.md) |

## 投射物 / projectile_data

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/dragonsurvival/projectile_data/ball_lightning.json` | ball_lightning；见球体/箭矢分支；命中/持续效果：dragonsurvival:damage、dragonsurvival:potion、dragonsurvival:lightning、dragonsurvival:particle、dragonsurvival:explosion | [投射物参数](05-projectiles.md) |
| `data/dragonsurvival/dragonsurvival/projectile_data/bouncy_ball_lightning.json` | bouncy_ball_lightning；见球体/箭矢分支；命中/持续效果：dragonsurvival:damage、dragonsurvival:potion、dragonsurvival:lightning、dragonsurvival:particle、dragonsurvival:explosion | [投射物参数](05-projectiles.md) |
| `data/dragonsurvival/dragonsurvival/projectile_data/fireball.json` | fireball；见球体/箭矢分支；命中/持续效果：dragonsurvival:damage、dragonsurvival:explosion | [投射物参数](05-projectiles.md) |
| `data/dragonsurvival/dragonsurvival/projectile_data/spike.json` | spike；见球体/箭矢分支；命中/持续效果：dragonsurvival:damage | [投射物参数](05-projectiles.md) |

## 未加载遗留进度

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/advancement_unused/fear_me.json` | **未加载**；ds.advancements.fear_me.title；触发：minecraft:player_interacted_with_entity | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement_unused/fly_high.json` | **未加载**；ds.advancements.fly_high.title；触发：minecraft:location | [进度参数](08-advancements-enchantments-and-conditions.md) |

## 未加载遗留附魔

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/unused/enchantment/dragonsbonk.json` | **未加载**；dragonsbonk；最高等级=5；组件=minecraft:post_attack | [附魔参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/unused/enchantment/dragonsboon.json` | **未加载**；dragonsboon；最高等级=5；组件=minecraft:damage、minecraft:post_attack | [附魔参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/unused/enchantment/shrink.json` | **未加载**；shrink；最高等级=5；组件=minecraft:attributes | [附魔参数](08-advancements-enchantments-and-conditions.md) |

## 标签 / tags

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/c/tags/item/hidden_from_recipe_viewers.json` | item/hidden_from_recipe_viewers 分组；追加 134 项：dragonsurvival:magic_stick、dragonsurvival:bolas、dragonsurvival:dragon_hunting_mesh、dragonsurvival:lightning、dragonsurvival:forest_icon 等 134 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/dragon_altars.json` | block/dragon_altars 分组；追加 31 项：dragonsurvival:stone_dragon_altar、dragonsurvival:sandstone_dragon_altar、dragonsurvival:red_sandstone_dragon_altar、dragonsurvival:purpur_dragon_altar、dragonsurvival:oak_dragon_altar 等 31 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/dragon_bones.json` | block/dragon_bones 分组；追加 120 项：dragonsurvival:skeleton_dragon_chest_skin1、dragonsurvival:skeleton_dragon_full_skin1、dragonsurvival:skeleton_dragon_leg_1_skin1、dragonsurvival:skeleton_dragon_leg_2_skin1、dragonsurvival:skeleton_dragon_leg_3_skin1 等 120 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/dragon_ore_drop.json` | block/dragon_ore_drop 分组；追加 7 项：#c:ores/quartz、#c:ores/coal、#c:ores/redstone、#c:ores/lapis、minecraft:nether_gold_ore 等 7 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/dragon_treasures.json` | block/dragon_treasures 分组；追加 25 项：dragonsurvival:debris_dragon_treasure、dragonsurvival:diamond_dragon_treasure、dragonsurvival:emerald_dragon_treasure、dragonsurvival:copper_dragon_treasure、dragonsurvival:gold_dragon_treasure 等 25 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/enables_hunter_effect.json` | block/enables_hunter_effect 分组；追加 16 项：#minecraft:flowers、#minecraft:saplings、minecraft:warped_nylium、minecraft:crimson_nylium、minecraft:grass_block 等 16 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/end_platform_non_replaceable.json` | block/end_platform_non_replaceable 分组；追加 1 项：minecraft:snow | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/general_ores.json` | block/general_ores 分组；追加 2 项：#c:ores、minecraft:gilded_blackstone | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/is_grassy.json` | block/is_grassy 分组；追加 25 项：#minecraft:flowers、#minecraft:leaves、minecraft:brown_mushroom_block、minecraft:red_mushroom_block、minecraft:sweet_berry_bush 等 25 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/is_wet.json` | block/is_wet 分组；追加 34 项：#minecraft:snow、#minecraft:ice、minecraft:water_cauldron、minecraft:wet_sponge、minecraft:muddy_mangrove_roots 等 34 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/large_dragon_destructible.json` | block/large_dragon_destructible 分组；追加 4 项：#minecraft:leaves、#minecraft:mineable/axe、#minecraft:flowers、#minecraft:replaceable | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/small_wooden_dragon_doors.json` | block/small_wooden_dragon_doors 分组；追加 9 项：dragonsurvival:small_oak_dragon_door、dragonsurvival:small_spruce_dragon_door、dragonsurvival:small_acacia_dragon_door、dragonsurvival:small_birch_dragon_door、dragonsurvival:small_jungle_dragon_door 等 9 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/speeds_up_cave_dragon.json` | block/speeds_up_cave_dragon 分组；追加 34 项：#minecraft:base_stone_overworld、minecraft:netherite_block、minecraft:nether_brick_fence、minecraft:nether_bricks、minecraft:cracked_nether_bricks 等 34 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/speeds_up_forest_dragon.json` | block/speeds_up_forest_dragon 分组；追加 6 项：#minecraft:wooden_slabs、#minecraft:planks、#minecraft:logs、#minecraft:dirt、#dragonsurvival:is_grassy 等 6 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/speeds_up_sea_dragon.json` | block/speeds_up_sea_dragon 分组；追加 21 项：#minecraft:coral_blocks、#minecraft:impermeable、#minecraft:sand、#c:sandstone/blocks、#c:sands 等 21 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/warm_blocks.json` | block/warm_blocks 分组；追加 7 项：#minecraft:fire、minecraft:magma_block、dragonsurvival:cave_source_of_magic、#immersive_weathering:charred_blocks、#regions_unexplored:ash 等 7 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/block/wooden_dragon_doors.json` | block/wooden_dragon_doors 分组；追加 10 项：dragonsurvival:oak_dragon_door、dragonsurvival:spruce_dragon_door、dragonsurvival:acacia_dragon_door、dragonsurvival:birch_dragon_door、dragonsurvival:jungle_dragon_door 等 10 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/damage_type/anti_dragon.json` | damage_type/anti_dragon 分组；追加 1 项：dragonsurvival:anti_dragon | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/damage_type/dragon_breath.json` | damage_type/dragon_breath 分组；追加 4 项：dragonsurvival:dragon_breath、dragonsurvival:burn、dragonsurvival:drain、dragonsurvival:lightning_breath | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/damage_type/dragon_magic.json` | damage_type/dragon_magic 分组；追加 8 项：dragonsurvival:fire_breath、dragonsurvival:burn、dragonsurvival:forest_breath、dragonsurvival:drain、dragonsurvival:lightning_breath 等 8 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/damage_type/is_electric.json` | damage_type/is_electric 分组；追加 4 项：dragonsurvival:lightning_breath、dragonsurvival:ball_lightning、dragonsurvival:electric、minecraft:lightning_bolt | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/damage_type/water_burn.json` | damage_type/water_burn 分组；追加 2 项：dragonsurvival:water_burn、dragonsurvival:rain_burn | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_ability/cave_dragon.json` | dragonsurvival/dragon_ability/cave_dragon 分组；追加 15 项：dragonsurvival:nether_breath、dragonsurvival:fire_ball、dragonsurvival:furnace_heat、dragonsurvival:lava_vision、dragonsurvival:sturdy_skin 等 15 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_ability/forest_dragon.json` | dragonsurvival/dragon_ability/forest_dragon 分组；追加 13 项：dragonsurvival:forest_breath、dragonsurvival:spike、dragonsurvival:hunter、dragonsurvival:sun_breath、dragonsurvival:inspiration 等 13 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_ability/order.json` | dragonsurvival/dragon_ability/order 分组；追加 43 项：dragonsurvival:nether_breath、dragonsurvival:fire_ball、dragonsurvival:furnace_heat、dragonsurvival:lava_vision、dragonsurvival:sturdy_skin 等 43 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_ability/sea_dragon.json` | dragonsurvival/dragon_ability/sea_dragon 分组；追加 15 项：dragonsurvival:storm_breath、dragonsurvival:ball_lightning、dragonsurvival:sea_eyes、dragonsurvival:ore_glow、dragonsurvival:soul_revelation 等 15 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_ability/test_abilities.json` | dragonsurvival/dragon_ability/test_abilities 分组；追加 18 项：dragonsurvival:test_climbable、dragonsurvival:test_convert_items、dragonsurvival:test_cooldown_recovery、dragonsurvival:test_damage_expression、dragonsurvival:test_entity_projectile 等 18 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_body/order.json` | dragonsurvival/dragon_body/order 分组；追加 5 项：dragonsurvival:center、dragonsurvival:east、dragonsurvival:north、dragonsurvival:south、dragonsurvival:west | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_penalty/cave_dragon.json` | dragonsurvival/dragon_penalty/cave_dragon 分组；追加 7 项：dragonsurvival:cold_weakness、dragonsurvival:water_weakness、dragonsurvival:item_blacklist、dragonsurvival:water_potion_weakness、dragonsurvival:snowball_weakness 等 7 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_penalty/forest_dragon.json` | dragonsurvival/dragon_penalty/forest_dragon 分组；追加 3 项：dragonsurvival:fear_of_darkness、dragonsurvival:item_blacklist、dragonsurvival:fear | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_penalty/sea_dragon.json` | dragonsurvival/dragon_penalty/sea_dragon 分组；追加 3 项：dragonsurvival:thin_skin、dragonsurvival:item_blacklist、dragonsurvival:fear | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_species/all.json` | dragonsurvival/dragon_species/all 分组；追加 3 项：dragonsurvival:cave_dragon、dragonsurvival:forest_dragon、dragonsurvival:sea_dragon | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_species/cave_dragons.json` | dragonsurvival/dragon_species/cave_dragons 分组；追加 1 项：dragonsurvival:cave_dragon | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_species/forest_dragons.json` | dragonsurvival/dragon_species/forest_dragons 分组；追加 1 项：dragonsurvival:forest_dragon | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_species/none.json` | dragonsurvival/dragon_species/none 分组；追加 0 项：空集合 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_species/order.json` | dragonsurvival/dragon_species/order 分组；追加 3 项：dragonsurvival:cave_dragon、dragonsurvival:forest_dragon、dragonsurvival:sea_dragon | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_species/sea_dragons.json` | dragonsurvival/dragon_species/sea_dragons 分组；追加 1 项：dragonsurvival:sea_dragon | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/dragonsurvival/dragon_species/true_dragons.json` | dragonsurvival/dragon_species/true_dragons 分组；追加 3 项：dragonsurvival:cave_dragon、dragonsurvival:sea_dragon、dragonsurvival:forest_dragon | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/enchantment/exclusive_set/anti_dragon.json` | enchantment/exclusive_set/anti_dragon 分组；追加 1 项：dragonsurvival:dragonsbane | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/enchantment/exclusive_set/dark_dragon.json` | enchantment/exclusive_set/dark_dragon 分组；追加 5 项：dragonsurvival:blood_siphon、dragonsurvival:draconic_superiority、dragonsurvival:murderers_cunning、dragonsurvival:overwhelming_might、dragonsurvival:curse_of_outlaw | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/enchantment/exclusive_set/light_dragon.json` | enchantment/exclusive_set/light_dragon 分组；追加 5 项：dragonsurvival:aerodynamic_mastery、dragonsurvival:combat_recovery、dragonsurvival:sacred_scales、dragonsurvival:unbreakable_spirit、dragonsurvival:curse_of_kindness | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/enchantment/leader_books.json` | enchantment/leader_books 分组；追加 2 项：dragonsurvival:dragonsbane、dragonsurvival:bolas | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/entity_type/animal_avoid_blacklist.json` | entity_type/animal_avoid_blacklist 分组；追加 6 项：minecraft:wolf、minecraft:hoglin、minecraft:bee、cobblemon:pokemon、bee_queen_ds:bee_guard 等 6 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/entity_type/can_fly.json` | entity_type/can_fly 分组；追加 10 项：minecraft:allay、minecraft:bat、minecraft:bee、minecraft:blaze、minecraft:ender_dragon 等 10 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/entity_type/charged_spread_blacklist.json` | entity_type/charged_spread_blacklist 分组；追加 8 项：minecraft:armor_stand、minecraft:cat、minecraft:minecart、minecraft:guardian、minecraft:elder_guardian 等 8 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/entity_type/dragons.json` | entity_type/dragons 分组；追加 1 项：minecraft:ender_dragon | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/entity_type/drops_dragon_heart_shard.json` | entity_type/drops_dragon_heart_shard 分组；追加 1 项：minecraft:armor_stand | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/entity_type/drops_elder_dragon_heart.json` | entity_type/drops_elder_dragon_heart 分组；追加 1 项：minecraft:armor_stand | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/entity_type/drops_weak_dragon_heart.json` | entity_type/drops_weak_dragon_heart 分组；追加 1 项：minecraft:armor_stand | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/entity_type/hunter_faction.json` | entity_type/hunter_faction 分组；追加 8 项：minecraft:villager、minecraft:iron_golem、dragonsurvival:hunter_ambusher、dragonsurvival:hunter_griffin、dragonsurvival:hunter_hound 等 8 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/entity_type/hunter_targets.json` | entity_type/hunter_targets 分组；追加 22 项：minecraft:evoker、minecraft:pillager、minecraft:vindicator、minecraft:stray、minecraft:skeleton 等 22 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/entity_type/vehicle_whitelist.json` | entity_type/vehicle_whitelist 分组；追加 9 项：#c:boats、#c:minecarts、littlelogistics:seater_barge、create:seat、create:contraption 等 9 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/activates_dragon_beacon.json` | item/activates_dragon_beacon 分组；追加 1 项：dragonsurvival:beacon_activator | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/cave_dragon_food.json` | item/cave_dragon_food 分组；追加 32 项：#minecraft:coals、dragonsurvival:charged_coal、dragonsurvival:charred_meat、dragonsurvival:cave_dragon_treat、dragonsurvival:charred_seafood 等 32 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/charred_food.json` | item/charred_food 分组；追加 6 项：dragonsurvival:charged_coal、dragonsurvival:charged_soup、dragonsurvival:charred_meat、dragonsurvival:charred_mushroom、dragonsurvival:charred_seafood 等 6 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/claw_weapons.json` | item/claw_weapons 分组；追加 2 项：#minecraft:swords、#c:tools/melee_weapon | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/cold_items.json` | item/cold_items 分组；追加 7 项：minecraft:snowball、minecraft:ice、minecraft:packed_ice、minecraft:snow、minecraft:snow_block 等 7 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/cooked_mushrooms.json` | item/cooked_mushrooms 分组；追加 27 项：minecraft:mushroom_stew、minecraft:suspicious_stew、simplefarming:mushroom_barley、undergarden:veiled_stew、undergarden:bloody_stew 等 27 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/cooked_vegetables.json` | item/cooked_vegetables 分组；追加 93 项：minecraft:pumpkin_pie、minecraft:baked_potato、minecraft:beetroot_soup、minecraft:dried_kelp_block、simplefarming:veggie_burger 等 93 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/dark_armor.json` | item/dark_armor 分组；追加 4 项：dragonsurvival:dark_dragon_helmet、dragonsurvival:dark_dragon_chestplate、dragonsurvival:dark_dragon_leggings、dragonsurvival:dark_dragon_boots | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/dragon_altars.json` | item/dragon_altars 分组；追加 31 项：dragonsurvival:stone_dragon_altar、dragonsurvival:sandstone_dragon_altar、dragonsurvival:red_sandstone_dragon_altar、dragonsurvival:purpur_dragon_altar、dragonsurvival:oak_dragon_altar 等 31 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/dragon_corals.json` | item/dragon_corals 分组；追加 25 项：minecraft:tube_coral_fan、minecraft:brain_coral_fan、minecraft:bubble_coral_fan、minecraft:fire_coral_fan、minecraft:horn_coral_fan 等 25 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/dragon_treasures.json` | item/dragon_treasures 分组；追加 25 项：dragonsurvival:debris_dragon_treasure、dragonsurvival:diamond_dragon_treasure、dragonsurvival:emerald_dragon_treasure、dragonsurvival:copper_dragon_treasure、dragonsurvival:gold_dragon_treasure 等 25 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/enchantable/chest_armor_and_elytra.json` | item/enchantable/chest_armor_and_elytra 分组；追加 2 项：#minecraft:enchantable/chest_armor、minecraft:elytra | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/epic_armor.json` | item/epic_armor 分组；追加 0 项：空集合 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/forest_dragon_food.json` | item/forest_dragon_food 分组；追加 107 项：#c:foods/raw_meat、#c:foods/berry、#c:mushrooms、minecraft:sweet_berries、minecraft:rotten_flesh 等 107 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/light_armor.json` | item/light_armor 分组；追加 4 项：dragonsurvival:light_dragon_helmet、dragonsurvival:light_dragon_chestplate、dragonsurvival:light_dragon_leggings、dragonsurvival:light_dragon_boots | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/light_source.json` | item/light_source 分组；追加 4 项：#c:dusts/glowstone、minecraft:torch、minecraft:lantern、minecraft:glowstone | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/primordial_anchor_fuel.json` | item/primordial_anchor_fuel 分组；追加 1 项：minecraft:ender_pearl | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/rare_armor.json` | item/rare_armor 分组；追加 0 项：空集合 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/sea_dragon_food.json` | item/sea_dragon_food 分组；追加 160 项：#c:foods/raw_fish、minecraft:kelp、minecraft:pufferfish、dragonsurvival:sea_dragon_treat、dragonsurvival:seasoned_fish 等 160 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/small_wooden_dragon_doors.json` | item/small_wooden_dragon_doors 分组；追加 9 项：dragonsurvival:small_oak_dragon_door、dragonsurvival:small_spruce_dragon_door、dragonsurvival:small_acacia_dragon_door、dragonsurvival:small_birch_dragon_door、dragonsurvival:small_jungle_dragon_door 等 9 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/uncommon_armor.json` | item/uncommon_armor 分组；追加 0 项：空集合 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/item/wooden_dragon_doors.json` | item/wooden_dragon_doors 分组；追加 10 项：dragonsurvival:oak_dragon_door、dragonsurvival:spruce_dragon_door、dragonsurvival:acacia_dragon_door、dragonsurvival:birch_dragon_door、dragonsurvival:jungle_dragon_door 等 10 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/mob_effect/overwhelming_might_blacklist.json` | mob_effect/overwhelming_might_blacklist 分组；追加 0 项：空集合 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/mob_effect/unbreakable_spirit_blacklist.json` | mob_effect/unbreakable_spirit_blacklist 分组；追加 0 项：空集合 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/villager_profession/pillage_blacklist.json` | villager_profession/pillage_blacklist 分组；追加 0 项：空集合 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/compat_is_wythers.json` | worldgen/biome/compat_is_wythers 分组；追加 3 项：#c:is_underground、wythers:is_underground、#forge:is_underground | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/is_desert_dragon_hunters_castle_spawn.json` | worldgen/biome/is_desert_dragon_hunters_castle_spawn 分组；追加 14 项：#minecraft:is_desert、#minecraft:is_badlands、minecraft:desert、minecraft:badlands、minecraft:wooded_badlands 等 14 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/is_dragon_hunters_castle_spawn.json` | worldgen/biome/is_dragon_hunters_castle_spawn 分组；追加 10 项：#minecraft:is_taiga、minecraft:taiga、minecraft:old_growth_pine_taiga、minecraft:old_growth_spruce_taiga、minecraft:dark_forest 等 10 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/is_dragon_skeleton_cave_spawn.json` | worldgen/biome/is_dragon_skeleton_cave_spawn 分组；追加 14 项：#minecraft:is_desert、#minecraft:is_badlands、minecraft:desert、minecraft:badlands、minecraft:wooded_badlands 等 14 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/is_dragon_skeleton_forest_spawn.json` | worldgen/biome/is_dragon_skeleton_forest_spawn 分组；追加 25 项：#minecraft:is_jungle、minecraft:jungle、minecraft:sparse_jungle、minecraft:bamboo_jungle、#forge:is_jungle 等 25 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/is_dragon_skeleton_sea_spawn.json` | worldgen/biome/is_dragon_skeleton_sea_spawn 分组；追加 7 项：#minecraft:is_beach、#forge:is_snowy、#neoforge:is_snowy、#c:is_snowy、#byg:is_snowy 等 7 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/is_treasure_angry_cave_spawn.json` | worldgen/biome/is_treasure_angry_cave_spawn 分组；追加 2 项：minecraft:nether_wastes、minecraft:basalt_deltas | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/is_treasure_angry_forest_spawn.json` | worldgen/biome/is_treasure_angry_forest_spawn 分组；追加 2 项：minecraft:warped_forest、minecraft:crimson_forest | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/is_treasure_angry_sea_spawn.json` | worldgen/biome/is_treasure_angry_sea_spawn 分组；追加 1 项：minecraft:soul_sand_valley | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/is_treasure_end_spawn.json` | worldgen/biome/is_treasure_end_spawn 分组；追加 2 项：minecraft:end_highlands、minecraft:end_midlands | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/is_treasure_friendly_cave_spawn.json` | worldgen/biome/is_treasure_friendly_cave_spawn 分组；追加 17 项：#minecraft:is_desert、#minecraft:is_badlands、minecraft:desert、minecraft:badlands、minecraft:wooded_badlands 等 17 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/is_treasure_friendly_forest_spawn.json` | worldgen/biome/is_treasure_friendly_forest_spawn 分组；追加 27 项：#minecraft:is_jungle、minecraft:jungle、minecraft:sparse_jungle、minecraft:bamboo_jungle、#forge:is_jungle 等 27 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/is_treasure_friendly_sea_spawn.json` | worldgen/biome/is_treasure_friendly_sea_spawn 分组；追加 19 项：#minecraft:is_river、minecraft:river、#forge:is_river、#minecraft:is_beach、#byg:is_river 等 19 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/biome/is_treasure_hunters_spawn.json` | worldgen/biome/is_treasure_hunters_spawn 分组；追加 12 项：#minecraft:is_taiga、minecraft:taiga、minecraft:old_growth_pine_taiga、minecraft:old_growth_spruce_taiga、minecraft:dark_forest 等 12 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/structure/dark_treasure.json` | worldgen/structure/dark_treasure 分组；追加 3 项：dragonsurvival:treasure_angry_cave、dragonsurvival:treasure_angry_forest、dragonsurvival:treasure_angry_sea | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/structure/dragon_skeletons.json` | worldgen/structure/dragon_skeletons 分组；追加 3 项：dragonsurvival:dragon_skeleton_cave、dragonsurvival:dragon_skeleton_sea、dragonsurvival:dragon_skeleton_forest | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/structure/hunter_treasure.json` | worldgen/structure/hunter_treasure 分组；追加 1 项：dragonsurvival:treasure_hunters | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/structure/light_treasure.json` | worldgen/structure/light_treasure 分组；追加 3 项：dragonsurvival:treasure_friendly_cave、dragonsurvival:treasure_friendly_forest、dragonsurvival:treasure_friendly_sea | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/structure/on_dragon_hunter_maps.json` | worldgen/structure/on_dragon_hunter_maps 分组；追加 1 项：dragonsurvival:dragon_hunters_castle | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/structure/treasure_end.json` | worldgen/structure/treasure_end 分组；追加 1 项：dragonsurvival:treasure_end | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/structure_set/all_friendly_treasure.json` | worldgen/structure_set/all_friendly_treasure 分组；追加 1 项：dragonsurvival:treasure_friendly_with_all | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/structure_set/underground_avoid.json` | worldgen/structure_set/underground_avoid 分组；追加 3 项：minecraft:strongholds、minecraft:ancient_cities、minecraft:trail_ruins | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/structure_set/underground_avoid_end.json` | worldgen/structure_set/underground_avoid_end 分组；追加 1 项：minecraft:end_cities | [标签参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/tags/worldgen/structure_set/underground_avoid_nether.json` | worldgen/structure_set/underground_avoid_nether 分组；追加 1 项：minecraft:nether_complexes | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/block/mineable/axe.json` | block/mineable/axe 分组；追加 15 项：#dragonsurvival:wooden_dragon_doors、dragonsurvival:oak_dragon_altar、dragonsurvival:birch_dragon_altar、dragonsurvival:pale_oak_dragon_altar、dragonsurvival:crimson_dragon_altar 等 15 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/block/mineable/pickaxe.json` | block/mineable/pickaxe 分组；追加 49 项：dragonsurvival:stone_dragon_altar、dragonsurvival:sandstone_dragon_altar、dragonsurvival:red_sandstone_dragon_altar、dragonsurvival:purpur_dragon_altar、dragonsurvival:nether_brick_dragon_altar 等 49 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/block/needs_iron_tool.json` | block/needs_iron_tool 分组；追加 2 项：dragonsurvival:dragon_beacon、dragonsurvival:dragon_memory_block | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/block/needs_stone_tool.json` | block/needs_stone_tool 分组；追加 13 项：dragonsurvival:gold_dragon_treasure、dragonsurvival:emerald_dragon_treasure、dragonsurvival:diamond_dragon_treasure、dragonsurvival:debris_dragon_treasure、dragonsurvival:iron_dragon_door 等 13 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/damage_type/bypasses_armor.json` | damage_type/bypasses_armor 分组；追加 3 项：dragonsurvival:drain、dragonsurvival:burn、dragonsurvival:dehydration | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/damage_type/bypasses_cooldown.json` | damage_type/bypasses_cooldown 分组；追加 1 项：dragonsurvival:spike | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/damage_type/is_fire.json` | damage_type/is_fire 分组；追加 2 项：dragonsurvival:burn、dragonsurvival:fire_breath | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/damage_type/is_lightning.json` | damage_type/is_lightning 分组；追加 1 项：dragonsurvival:lightning_breath | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/damage_type/no_knockback.json` | damage_type/no_knockback 分组；追加 7 项：dragonsurvival:burn、dragonsurvival:drain、dragonsurvival:crushed、dragonsurvival:dehydration、dragonsurvival:water_burn 等 7 项 | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/item/chest_armor.json` | item/chest_armor 分组；追加 2 项：dragonsurvival:light_dragon_chestplate、dragonsurvival:dark_dragon_chestplate | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/item/foot_armor.json` | item/foot_armor 分组；追加 2 项：dragonsurvival:light_dragon_boots、dragonsurvival:dark_dragon_boots | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/item/head_armor.json` | item/head_armor 分组；追加 2 项：dragonsurvival:light_dragon_helmet、dragonsurvival:dark_dragon_helmet | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/item/leg_armor.json` | item/leg_armor 分组；追加 2 项：dragonsurvival:light_dragon_leggings、dragonsurvival:dark_dragon_leggings | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/item/swords.json` | item/swords 分组；追加 4 项：dragonsurvival:dragon_hunter_sword、dragonsurvival:hunter_partisan、dragonsurvival:hunter_partisan_diamond、dragonsurvival:hunter_partisan_netherite | [标签参数](07-tags-recipes-and-loot.md) |
| `data/minecraft/tags/point_of_interest_type/acquirable_job_site.json` | point_of_interest_type/acquirable_job_site 分组；追加 1 项：dragonsurvival:dragon_rider_poi | [标签参数](07-tags-recipes-and-loot.md) |

## 表情集 / dragon_emote_set

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/dragonsurvival/dragon_emote_set/default_emotes.json` | default_emotes；62 个动作；empty、blend_angry、spell_charge、blend_sad、blend_close_eyes 等 52 项 | [表情集参数](02-body-and-emotes.md) |

## 进度 / advancement

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/advancement/be_adult_dragon.json` | 岁月砺金；触发：dragonsurvival:be_dragon | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/be_young_dragon.json` | 成长之路；触发：dragonsurvival:be_dragon | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/cave/be_dragon.json` | 浴火而生；触发：dragonsurvival:be_dragon | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/cave/be_old_dragon.json` | 熔岩之心，下界鳞甲；触发：dragonsurvival:be_dragon | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/cave/diamonds_in_lava.json` | 熔岩中的宝藏；触发：dragonsurvival:mine_block_under_lava | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/cave/go_home.json` | 熟悉的味道……家；触发：minecraft:location | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/cave/master_all_passives.json` | 灼辉炽焰；触发：dragonsurvival:upgrade_ability | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/cave/rock_eater.json` | 为火而燃；触发：minecraft:consume_item | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/cave/swim_in_lava.json` | 地板是岩浆；触发：minecraft:location | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/cave/water_safety.json` | 防水试炼；触发：minecraft:location | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/change_beacon.json` | 种族同契；触发：minecraft:item_used_on_block | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/collect_dust.json` | 古龙传说；触发：minecraft:inventory_changed | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/collect_heart_from_monster.json` | 龙心回收；触发：minecraft:inventory_changed | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/dark/affected_by_hunter_omen.json` | 猎杀标记；触发：minecraft:effects_changed | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/dark/collect_key.json` | 毁灭之路；触发：minecraft:inventory_changed | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/dark/get_armor_item.json` | 幽影恐惧；触发：minecraft:inventory_changed | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/dark/get_armor_set.json` | 征服世界；触发：minecraft:inventory_changed | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/dark/open_vault.json` | 杀戮的证明；触发：minecraft:item_used_on_block | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/dark/steal_from_villager.json` | 村民掠夺者；触发：dragonsurvival:steal_from_villager | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/find_bones.json` | 远古回响；触发：minecraft:location | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/find_end_platform.json` | 别跳进虚空！；触发：dragonsurvival:be_dragon | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/find_end_structures.json` | 龙族秘闻；触发：minecraft:location | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/find_nether_structures.json` | 平静之焰；触发：minecraft:location | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/find_overworld_structures.json` | 沉眠巨龙；触发：minecraft:location | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/forest/be_dragon.json` | 有蜜之处，必有蜂；触发：dragonsurvival:be_dragon | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/forest/be_old_dragon.json` | 史前植物繁茂；触发：dragonsurvival:be_dragon | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/forest/master_all_passives.json` | 光合作用；触发：dragonsurvival:upgrade_ability | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/forest/meat_eater.json` | 肉类爱好者；触发：minecraft:consume_item | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/forest/poisonous_potato.json` | 过量施肥；触发：dragonsurvival:convert_item_from_ability | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/forest/prevent_darkness_penalty.json` | 生物荧光；触发：minecraft:location | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/forest/stand_on_sweet_berries.json` | 甜莓试炼；触发：minecraft:location | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/forest/transplant_chorus_fruit.json` | 佳肴遍地；触发：minecraft:placed_block | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/get_all_beacons.json` | 三头巨龙之力；触发：minecraft:effects_changed | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/hunter/collect_key.json` | 猎龙者之路；触发：minecraft:inventory_changed | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/hunter/fire_bolas.json` | 巨龙捕手；触发：minecraft:shot_crossbow | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/hunter/open_vault.json` | 猎人的赏金；触发：minecraft:item_used_on_block | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/hunter/promotion.json` | 要不要做个交易？；触发：minecraft:player_interacted_with_entity | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/light/collect_key.json` | 救赎之路；触发：minecraft:inventory_changed | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/light/dragon_rider_workbench.json` | 巨龙之友；触发：minecraft:recipe_crafted | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/light/get_armor_item.json` | 光明守护者；触发：minecraft:inventory_changed | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/light/get_armor_set.json` | 巨龙圣骑士；触发：minecraft:inventory_changed | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/light/open_vault.json` | 地底馈赠；触发：minecraft:item_used_on_block | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/path_choice.json` | 村民的命运；触发：minecraft:player_interacted_with_entity | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/place_altar.json` | 巨龙入门；触发：minecraft:placed_block | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/alexandrite_dragon_treasure.json` | recipes/decorations/alexandrite_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:alexandrite_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/ammolite_dragon_treasure.json` | recipes/decorations/ammolite_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:ammolite_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/bee_honey_treasure.json` | recipes/decorations/bee_honey_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:bee_honey_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/black_diamond_dragon_treasure.json` | recipes/decorations/black_diamond_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:black_diamond_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/bottle_caps_treasure.json` | recipes/decorations/bottle_caps_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:bottle_caps_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/carnelian_dragon_treasure.json` | recipes/decorations/carnelian_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:carnelian_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/chocolate_dragon_treasure.json` | recipes/decorations/chocolate_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:chocolate_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/citrine_dragon_treasure.json` | recipes/decorations/citrine_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:citrine_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/copper_dragon_treasure.json` | recipes/decorations/copper_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:copper_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/debris_dragon_treasure.json` | recipes/decorations/debris_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:debris_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/diamond_dragon_treasure.json` | recipes/decorations/diamond_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:diamond_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/emerald_dragon_treasure.json` | recipes/decorations/emerald_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:emerald_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/gold_dragon_treasure.json` | recipes/decorations/gold_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:gold_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/heliodor_dragon_treasure.json` | recipes/decorations/heliodor_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:heliodor_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/iolite_dragon_treasure.json` | recipes/decorations/iolite_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:iolite_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/iron_dragon_treasure.json` | recipes/decorations/iron_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:iron_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/kyanite_dragon_treasure.json` | recipes/decorations/kyanite_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:kyanite_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/moldavite_dragon_treasure.json` | recipes/decorations/moldavite_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:moldavite_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/peridot_dragon_treasure.json` | recipes/decorations/peridot_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:peridot_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/rose_quartz_dragon_treasure.json` | recipes/decorations/rose_quartz_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:rose_quartz_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/ruby_dragon_treasure.json` | recipes/decorations/ruby_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:ruby_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/sapphire_dragon_treasure.json` | recipes/decorations/sapphire_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:sapphire_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/topaz_dragon_treasure.json` | recipes/decorations/topaz_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:topaz_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/turquoise_dragon_treasure.json` | recipes/decorations/turquoise_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:turquoise_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/decorations/white_diamond_dragon_treasure.json` | recipes/decorations/white_diamond_dragon_treasure；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:white_diamond_dragon_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/food/cave_dragon_treat.json` | recipes/food/cave_dragon_treat；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:cave_dragon_treat | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/food/charged_coal_from_bulb.json` | recipes/food/charged_coal_from_bulb；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:charged_coal_from_bulb | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/food/charged_coal_from_dust.json` | recipes/food/charged_coal_from_dust；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:charged_coal_from_dust | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/acacia_dragon_altar.json` | recipes/misc/acacia_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:acacia_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/amethyst_dragon_altar.json` | recipes/misc/amethyst_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:amethyst_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/bamboo_dragon_altar.json` | recipes/misc/bamboo_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:bamboo_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/birch_dragon_altar.json` | recipes/misc/birch_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:birch_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/blackstone_dragon_altar.json` | recipes/misc/blackstone_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:blackstone_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/bone_dragon_altar.json` | recipes/misc/bone_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:bone_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/charcoal_from_charred_food.json` | recipes/misc/charcoal_from_charred_food；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:charcoal_from_charred_food | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/cherry_dragon_altar.json` | recipes/misc/cherry_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:cherry_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/crimson_dragon_altar.json` | recipes/misc/crimson_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:crimson_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/dark_oak_dragon_altar.json` | recipes/misc/dark_oak_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:dark_oak_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/deepslate_dragon_altar.json` | recipes/misc/deepslate_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:deepslate_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/dragon_beacon.json` | recipes/misc/dragon_beacon；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:dragon_beacon | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/elder_dragon_dust_from_dragon_treasures.json` | recipes/misc/elder_dragon_dust_from_dragon_treasures；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:elder_dragon_dust_from_dragon_treasures | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/elder_dragon_heart.json` | recipes/misc/elder_dragon_heart；触发：minecraft:recipe_unlocked、minecraft:inventory_changed；解锁配方：dragonsurvival:elder_dragon_heart | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/endstone_dragon_altar.json` | recipes/misc/endstone_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:endstone_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/ice_dragon_altar.json` | recipes/misc/ice_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:ice_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/jungle_dragon_altar.json` | recipes/misc/jungle_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:jungle_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/mangrove_dragon_altar.json` | recipes/misc/mangrove_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:mangrove_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/mossy_dragon_altar.json` | recipes/misc/mossy_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:mossy_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/mudbrick_dragon_altar.json` | recipes/misc/mudbrick_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:mudbrick_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/nether_brick_dragon_altar.json` | recipes/misc/nether_brick_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:nether_brick_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/netherrack_dragon_altar.json` | recipes/misc/netherrack_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:netherrack_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/oak_dragon_altar.json` | recipes/misc/oak_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:oak_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/obsidian_dragon_altar.json` | recipes/misc/obsidian_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:obsidian_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/prismarine_dragon_altar.json` | recipes/misc/prismarine_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:prismarine_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/purpur_dragon_altar.json` | recipes/misc/purpur_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:purpur_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/quartz_dragon_altar.json` | recipes/misc/quartz_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:quartz_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/red_nether_brick_dragon_altar.json` | recipes/misc/red_nether_brick_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:red_nether_brick_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/red_sandstone_dragon_altar.json` | recipes/misc/red_sandstone_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:red_sandstone_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/sandstone_dragon_altar.json` | recipes/misc/sandstone_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:sandstone_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/spruce_dragon_altar.json` | recipes/misc/spruce_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:spruce_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/stone_dragon_altar.json` | recipes/misc/stone_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:stone_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/tuff_dragon_altar.json` | recipes/misc/tuff_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:tuff_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/misc/warped_dragon_altar.json` | recipes/misc/warped_dragon_altar；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:warped_dragon_altar | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/acacia_dragon_door.json` | recipes/redstone/acacia_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:acacia_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/birch_dragon_door.json` | recipes/redstone/birch_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:birch_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/cave_dragon_door.json` | recipes/redstone/cave_dragon_door；触发：minecraft:recipe_unlocked、minecraft:inventory_changed；解锁配方：dragonsurvival:cave_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/cave_dragon_pressure_plate.json` | recipes/redstone/cave_dragon_pressure_plate；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:cave_dragon_pressure_plate | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/crimson_dragon_door.json` | recipes/redstone/crimson_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:crimson_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/dark_oak_dragon_door.json` | recipes/redstone/dark_oak_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:dark_oak_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/forest_dragon_door.json` | recipes/redstone/forest_dragon_door；触发：minecraft:recipe_unlocked、minecraft:inventory_changed；解锁配方：dragonsurvival:forest_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/gothic_dragon_door.json` | recipes/redstone/gothic_dragon_door；触发：minecraft:recipe_unlocked、minecraft:inventory_changed；解锁配方：dragonsurvival:gothic_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/iron_dragon_door.json` | recipes/redstone/iron_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:iron_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/jungle_dragon_door.json` | recipes/redstone/jungle_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:jungle_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/legacy_dragon_door.json` | recipes/redstone/legacy_dragon_door；触发：minecraft:recipe_unlocked、minecraft:inventory_changed；解锁配方：dragonsurvival:legacy_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/oak_dragon_door.json` | recipes/redstone/oak_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:oak_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/sea_dragon_door.json` | recipes/redstone/sea_dragon_door；触发：minecraft:recipe_unlocked、minecraft:inventory_changed；解锁配方：dragonsurvival:sea_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/skyrim_dragon_door.json` | recipes/redstone/skyrim_dragon_door；触发：minecraft:recipe_unlocked、minecraft:inventory_changed；解锁配方：dragonsurvival:skyrim_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_acacia_dragon_door.json` | recipes/redstone/small_acacia_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_acacia_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_birch_dragon_door.json` | recipes/redstone/small_birch_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_birch_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_cave_dragon_door.json` | recipes/redstone/small_cave_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_cave_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_crimson_dragon_door.json` | recipes/redstone/small_crimson_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_crimson_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_dark_oak_dragon_door.json` | recipes/redstone/small_dark_oak_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_dark_oak_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_forest_dragon_door.json` | recipes/redstone/small_forest_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_forest_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_gothic_dragon_door.json` | recipes/redstone/small_gothic_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_gothic_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_iron_dragon_door.json` | recipes/redstone/small_iron_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_iron_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_jungle_dragon_door.json` | recipes/redstone/small_jungle_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_jungle_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_oak_dragon_door.json` | recipes/redstone/small_oak_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_oak_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_sea_dragon_door.json` | recipes/redstone/small_sea_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_sea_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_skyrim_dragon_door.json` | recipes/redstone/small_skyrim_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_skyrim_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_spruce_dragon_door.json` | recipes/redstone/small_spruce_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_spruce_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_stone_dragon_door.json` | recipes/redstone/small_stone_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:small_stone_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/small_warped_dragon_door.json` | recipes/redstone/small_warped_dragon_door；触发：minecraft:recipe_unlocked、minecraft:inventory_changed；解锁配方：dragonsurvival:small_warped_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/spruce_dragon_door.json` | recipes/redstone/spruce_dragon_door；触发：minecraft:inventory_changed、minecraft:recipe_unlocked；解锁配方：dragonsurvival:spruce_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/stone_dragon_door.json` | recipes/redstone/stone_dragon_door；触发：minecraft:recipe_unlocked、minecraft:inventory_changed；解锁配方：dragonsurvival:stone_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/recipes/redstone/warped_dragon_door.json` | recipes/redstone/warped_dragon_door；触发：minecraft:recipe_unlocked、minecraft:inventory_changed；解锁配方：dragonsurvival:warped_dragon_door | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/root.json` | 龙之生存；触发：minecraft:tick | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/sea/be_dragon.json` | 异色双瞳；触发：dragonsurvival:be_dragon | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/sea/be_old_dragon.json` | 远古守护者；触发：dragonsurvival:be_dragon | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/sea/fish_eater.json` | 没有米的寿司；触发：minecraft:consume_item | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/sea/loot_shipwreck.json` | 海洋回收者；触发：minecraft:location | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/sea/master_all_passives.json` | 深海压力；触发：dragonsurvival:upgrade_ability | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/sea/peace_in_nether.json` | 耐热魔法；触发：minecraft:location | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/sea/place_snow_in_nether.json` | 以寒克旱；触发：minecraft:placed_block | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/sea/rain_dancing.json` | 雨中起舞；触发：minecraft:location | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/sleep_on_hoard.json` | 终于，配得上我的床；触发：dragonsurvival:sleep_on_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/sleep_on_massive_hoard.json` | 龙之贪婪；触发：dragonsurvival:sleep_on_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/sleep_on_treasure.json` | 聚宝成床；触发：dragonsurvival:sleep_on_treasure | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/stop_natural_growth.json` | 永葆青春；触发：dragonsurvival:stop_natural_growth | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/use_dragon_soul.json` | 龙之魂；触发：dragonsurvival:use_dragon_soul | [进度参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/advancement/use_memory_block.json` | 知识即力量；触发：minecraft:item_used_on_block | [进度参数](08-advancements-enchantments-and-conditions.md) |

## 配方 / recipe

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/recipe/acacia_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:acacia_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/acacia_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:acacia_dragon_door ×2 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/alexandrite_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:alexandrite_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/amethyst_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:amethyst_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/ammolite_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:ammolite_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/bamboo_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:bamboo_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/beacon_activator.json` | minecraft:crafting_shapeless；产出 dragonsurvival:beacon_activator ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/bee_honey_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:bee_honey_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/birch_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:birch_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/birch_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:birch_dragon_door ×2 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/black_diamond_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:black_diamond_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/blackstone_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:blackstone_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/bone_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:bone_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/bottle_caps_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:bottle_caps_treasure ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/carnelian_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:carnelian_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/cave_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:cave_dragon_door ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/cave_dragon_pressure_plate.json` | minecraft:crafting_shapeless；产出 dragonsurvival:cave_dragon_pressure_plate ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/cave_dragon_treat.json` | minecraft:crafting_shaped；产出 dragonsurvival:cave_dragon_treat ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/charcoal_from_charred_food.json` | minecraft:crafting_shapeless；产出 minecraft:charcoal ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/charged_coal_from_bulb.json` | minecraft:crafting_shaped；产出 dragonsurvival:charged_coal ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/charged_coal_from_dust.json` | minecraft:crafting_shaped；产出 dragonsurvival:charged_coal ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/charged_soup_meat_1.json` | minecraft:crafting_shaped；产出 dragonsurvival:charged_soup ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/charged_soup_meat_2.json` | minecraft:crafting_shaped；产出 dragonsurvival:charged_soup ×2 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/charged_soup_meat_3.json` | minecraft:crafting_shaped；产出 dragonsurvival:charged_soup ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/charged_soup_seafood_1.json` | minecraft:crafting_shaped；产出 dragonsurvival:charged_soup ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/charged_soup_seafood_2.json` | minecraft:crafting_shaped；产出 dragonsurvival:charged_soup ×2 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/charged_soup_seafood_3.json` | minecraft:crafting_shaped；产出 dragonsurvival:charged_soup ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/cherry_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:cherry_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/chocolate_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:chocolate_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/citrine_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:citrine_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/copper_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:copper_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/crimson_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:crimson_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/crimson_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:crimson_dragon_door ×2 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/dark_dragon_boots.json` | minecraft:smithing_transform；产出 dragonsurvival:dark_dragon_boots ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/dark_dragon_chestplate.json` | minecraft:smithing_transform；产出 dragonsurvival:dark_dragon_chestplate ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/dark_dragon_helmet.json` | minecraft:smithing_transform；产出 dragonsurvival:dark_dragon_helmet ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/dark_dragon_leggings.json` | minecraft:smithing_transform；产出 dragonsurvival:dark_dragon_leggings ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/dark_oak_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:dark_oak_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/dark_oak_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:dark_oak_dragon_door ×2 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/dark_template_dupe.json` | minecraft:crafting_shaped；产出 dragonsurvival:dark_upgrade ×2 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/debris_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:debris_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/deepslate_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:deepslate_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/diamond_chorus.json` | minecraft:crafting_shapeless；产出 dragonsurvival:diamond_chorus ×6 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/diamond_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:diamond_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/double_quartz.json` | minecraft:crafting_shapeless；产出 dragonsurvival:double_quartz ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/dragon_beacon.json` | minecraft:crafting_shapeless；产出 dragonsurvival:dragon_beacon ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/dragon_heart_shard.json` | minecraft:crafting_shaped；产出 dragonsurvival:heart_element ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/dragon_memory_block.json` | minecraft:crafting_shaped；产出 dragonsurvival:dragon_memory_block ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/dragon_pressure_plate.json` | minecraft:crafting_shapeless；产出 dragonsurvival:dragon_pressure_plate ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/dragon_rider_workbench.json` | minecraft:crafting_shaped；产出 dragonsurvival:dragon_rider_workbench ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/elder_dragon_bone.json` | minecraft:crafting_shaped；产出 dragonsurvival:elder_dragon_bone ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/elder_dragon_bone_from_heart_element.json` | minecraft:crafting_shapeless；产出 dragonsurvival:weak_dragon_heart ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/elder_dragon_dust.json` | minecraft:crafting_shapeless；产出 dragonsurvival:elder_dragon_dust ×9 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/elder_dragon_dust_from_dragon_treasures.json` | minecraft:crafting_shapeless；产出 dragonsurvival:elder_dragon_dust ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/elder_dragon_heart.json` | minecraft:crafting_shaped；产出 dragonsurvival:elder_dragon_heart ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/emerald_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:emerald_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/endstone_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:endstone_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/explosive_copper.json` | minecraft:crafting_shapeless；产出 dragonsurvival:explosive_copper ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/forest_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:forest_dragon_door ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/forest_dragon_pressure_plate.json` | minecraft:crafting_shapeless；产出 dragonsurvival:forest_dragon_pressure_plate ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/forest_dragon_treat_0.json` | minecraft:crafting_shaped；产出 dragonsurvival:forest_dragon_treat ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/forest_dragon_treat_1.json` | minecraft:crafting_shaped；产出 dragonsurvival:forest_dragon_treat ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/forest_dragon_treat_2.json` | minecraft:crafting_shaped；产出 dragonsurvival:forest_dragon_treat ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/frozen_raw_fish.json` | minecraft:crafting_shapeless；产出 dragonsurvival:frozen_raw_fish ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/gold_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:gold_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/golden_coral_pufferfish_0.json` | minecraft:crafting_shapeless；产出 dragonsurvival:golden_coral_pufferfish ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/golden_coral_pufferfish_1.json` | minecraft:crafting_shapeless；产出 dragonsurvival:golden_coral_pufferfish ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/golden_turtle_egg.json` | minecraft:crafting_shapeless；产出 dragonsurvival:golden_turtle_egg ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/gothic_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:gothic_dragon_door ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/heliodor_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:heliodor_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/hot_dragon_rod.json` | minecraft:crafting_shapeless；产出 dragonsurvival:hot_dragon_rod ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/human_pressure_plate.json` | minecraft:crafting_shapeless；产出 dragonsurvival:human_pressure_plate ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/hunter_partisan_diamond.json` | crafting_shapeless；产出 dragonsurvival:hunter_partisan_diamond ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/hunter_partisan_netherite_smithing.json` | minecraft:smithing_transform；产出 dragonsurvival:hunter_partisan_netherite ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/ice_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:ice_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/iolite_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:iolite_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/iron_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:iron_dragon_door ×2 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/iron_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:iron_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/jungle_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:jungle_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/jungle_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:jungle_dragon_door ×2 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/kyanite_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:kyanite_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/legacy_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:legacy_dragon_door ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/light_dragon_boots.json` | minecraft:smithing_transform；产出 dragonsurvival:light_dragon_boots ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/light_dragon_chestplate.json` | minecraft:smithing_transform；产出 dragonsurvival:light_dragon_chestplate ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/light_dragon_helmet.json` | minecraft:smithing_transform；产出 dragonsurvival:light_dragon_helmet ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/light_dragon_leggings.json` | minecraft:smithing_transform；产出 dragonsurvival:light_dragon_leggings ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/light_template_dupe.json` | minecraft:crafting_shaped；产出 dragonsurvival:light_upgrade ×2 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/luminous_ointment.json` | minecraft:crafting_shapeless；产出 dragonsurvival:luminous_ointment ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/mangrove_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:mangrove_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/meat_chorus_mix.json` | minecraft:crafting_shapeless；产出 dragonsurvival:meat_chorus_mix ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/meat_wild_berries.json` | minecraft:crafting_shapeless；产出 dragonsurvival:meat_wild_berries ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/moldavite_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:moldavite_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/mossy_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:mossy_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/mudbrick_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:mudbrick_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/nether_brick_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:nether_brick_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/netherrack_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:netherrack_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/oak_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:oak_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/oak_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:oak_dragon_door ×2 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/obsidian_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:obsidian_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/peridot_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:peridot_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/prismarine_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:prismarine_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/purpur_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:purpur_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/quartz_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:quartz_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/quartz_explosive_copper.json` | minecraft:crafting_shapeless；产出 dragonsurvival:quartz_explosive_copper ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/red_nether_brick_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:red_nether_brick_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/red_sandstone_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:red_sandstone_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/rose_quartz_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:rose_quartz_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/ruby_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:ruby_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/sandstone_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:sandstone_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/sapphire_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:sapphire_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/sea_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:sea_dragon_door ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/sea_dragon_pressure_plate.json` | minecraft:crafting_shapeless；产出 dragonsurvival:sea_dragon_pressure_plate ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/sea_dragon_treat.json` | minecraft:crafting_shaped；产出 dragonsurvival:sea_dragon_treat ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/seasoned_fish_1.json` | minecraft:crafting_shapeless；产出 dragonsurvival:seasoned_fish ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/skyrim_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:skyrim_dragon_door ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_acacia_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_acacia_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_birch_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_birch_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_cave_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_cave_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_crimson_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_crimson_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_dark_oak_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_dark_oak_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_forest_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_forest_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_gothic_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_gothic_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_iron_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_iron_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_jungle_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_jungle_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_oak_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_oak_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_sea_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_sea_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_skyrim_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_skyrim_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_spruce_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_spruce_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_stone_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_stone_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/small_warped_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:small_warped_dragon_door ×3 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelly_meat_porridge_0.json` | minecraft:crafting_shapeless；产出 dragonsurvival:smelly_meat_porridge ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelly_meat_porridge_1.json` | minecraft:crafting_shapeless；产出 dragonsurvival:smelly_meat_porridge ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelting/charred_meat.json` | minecraft:smelting；产出 dragonsurvival:charred_meat ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelting/charred_meat_from_campfire_cooking.json` | minecraft:campfire_cooking；产出 dragonsurvival:charred_meat ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelting/charred_meat_from_smoking.json` | minecraft:smoking；产出 dragonsurvival:charred_meat ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelting/charred_mushroom.json` | minecraft:smelting；产出 dragonsurvival:charred_mushroom ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelting/charred_mushroom_from_campfire_cooking.json` | minecraft:campfire_cooking；产出 dragonsurvival:charred_mushroom ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelting/charred_mushroom_from_smoking.json` | minecraft:smoking；产出 dragonsurvival:charred_mushroom ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelting/charred_seafood.json` | minecraft:smelting；产出 dragonsurvival:charred_seafood ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelting/charred_seafood_from_campfire_cooking.json` | minecraft:campfire_cooking；产出 dragonsurvival:charred_seafood ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelting/charred_seafood_from_smoking.json` | minecraft:smoking；产出 dragonsurvival:charred_seafood ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelting/charred_vegetable.json` | minecraft:smelting；产出 dragonsurvival:charred_vegetable ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelting/charred_vegetable_from_campfire_cooking.json` | minecraft:campfire_cooking；产出 dragonsurvival:charred_vegetable ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/smelting/charred_vegetable_from_smoking.json` | minecraft:smoking；产出 dragonsurvival:charred_vegetable ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/source_of_magic_cave.json` | minecraft:crafting_shaped；产出 dragonsurvival:cave_source_of_magic ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/source_of_magic_forest.json` | minecraft:crafting_shaped；产出 dragonsurvival:forest_source_of_magic ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/source_of_magic_sea.json` | minecraft:crafting_shaped；产出 dragonsurvival:sea_source_of_magic ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/spearman_promotion.json` | minecraft:crafting_shapeless；产出 dragonsurvival:spearman_promotion ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/spruce_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:spruce_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/spruce_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:spruce_dragon_door ×2 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/star_bone_craft.json` | minecraft:crafting_shaped；产出 dragonsurvival:star_bone ×8 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/star_bone_dupe.json` | minecraft:crafting_shaped；产出 dragonsurvival:star_bone ×32 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/star_heart.json` | minecraft:crafting_shapeless；产出 dragonsurvival:star_heart ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/stone_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:stone_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/stone_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:stone_dragon_door ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/sweet_sour_rabbit_0.json` | minecraft:crafting_shapeless；产出 dragonsurvival:sweet_sour_rabbit ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/sweet_sour_rabbit_1.json` | minecraft:crafting_shapeless；产出 dragonsurvival:sweet_sour_rabbit ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/topaz_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:topaz_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/tuff_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:tuff_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/turquoise_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:turquoise_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/warped_dragon_altar.json` | minecraft:crafting_shapeless；产出 dragonsurvival:warped_dragon_altar ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/warped_dragon_door.json` | minecraft:crafting_shapeless；产出 dragonsurvival:warped_dragon_door ×2 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/weak_dragon_heart.json` | minecraft:crafting_shaped；产出 dragonsurvival:weak_dragon_heart ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/weak_dragon_heart_from_elder_dragon_heart.json` | minecraft:crafting_shapeless；产出 dragonsurvival:elder_dragon_bone ×9 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/weak_dragon_heart_to_dragon_shard.json` | minecraft:crafting_shapeless；产出 dragonsurvival:heart_element ×1 | [配方参数](07-tags-recipes-and-loot.md) |
| `data/dragonsurvival/recipe/white_diamond_dragon_treasure.json` | minecraft:crafting_shapeless；产出 dragonsurvival:white_diamond_dragon_treasure ×1 | [配方参数](07-tags-recipes-and-loot.md) |

## 附魔 / enchantment

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/enchantment/aerodynamic_mastery.json` | 驭风精通·圣辉；最高等级=1；组件=minecraft:attributes | [附魔参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/enchantment/blood_siphon.json` | 嗜血掠夺·黯影；最高等级=1；组件=minecraft:post_attack | [附魔参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/enchantment/bolas.json` | 流星索；最高等级=1；组件=空，Java 专用行为见参数文档 | [附魔参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/enchantment/combat_recovery.json` | 圣疗灵光·圣辉；最高等级=1；组件=minecraft:post_attack | [附魔参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/enchantment/curse_of_kindness.json` | 慈悲印记·圣辉；最高等级=1；组件=minecraft:attributes | [附魔参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/enchantment/curse_of_outlaw.json` | 永猎通缉·黯影；最高等级=1；组件=minecraft:tick | [附魔参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/enchantment/draconic_superiority.json` | 龙威浩荡·黯影；最高等级=1；组件=minecraft:damage | [附魔参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/enchantment/dragonsbane.json` | 弑龙者；最高等级=5；组件=minecraft:damage、minecraft:post_attack、minecraft:tick | [附魔参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/enchantment/murderers_cunning.json` | 猎杀诡谋·黯影；最高等级=1；组件=空，Java 专用行为见参数文档 | [附魔参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/enchantment/overwhelming_might.json` | 威压之力·黯影；最高等级=1；组件=空，Java 专用行为见参数文档 | [附魔参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/enchantment/sacred_scales.json` | 圣耀龙鳞·圣辉；最高等级=1；组件=minecraft:damage_protection | [附魔参数](08-advancements-enchantments-and-conditions.md) |
| `data/dragonsurvival/enchantment/unbreakable_spirit.json` | 不屈之魂·圣辉；最高等级=1；组件=空，Java 专用行为见参数文档 | [附魔参数](08-advancements-enchantments-and-conditions.md) |

## 龙种 / dragon_species

| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |
| --- | --- | --- |
| `data/dragonsurvival/dragonsurvival/dragon_species/cave_dragon.json` | 洞穴龙；技能=#dragonsurvival:cave_dragon；弱点=#dragonsurvival:cave_dragon | [龙种参数](01-species.md) |
| `data/dragonsurvival/dragonsurvival/dragon_species/forest_dragon.json` | 森林龙；技能=#dragonsurvival:forest_dragon；弱点=#dragonsurvival:forest_dragon | [龙种参数](01-species.md) |
| `data/dragonsurvival/dragonsurvival/dragon_species/sea_dragon.json` | 海洋龙；技能=#dragonsurvival:sea_dragon；弱点=#dragonsurvival:sea_dragon | [龙种参数](01-species.md) |

## 更新方式

从工程根目录运行 `python docs/datapacks/generate_file_index.py build/research/ds.jar`。也可传入其他 DS JAR 路径；升级版本后必须同步重新核对参数手册，不能只更新索引。

生成器只读取 ZIP 与中文语言表，写入本索引；不提取上游源码或美术，不会改动游戏资源。
