# 进度、附魔与条件

[返回总目录](README.md)。适用 DS 2.0.71 / Minecraft 1.21.1。文件清单见[逐文件索引](10-file-index.md)。

## advancement：进度

路径：`data/<ns>/advancement/<id>.json`。定义目标、显示和奖励，不负责注册技能本身。DS 当前有 153 个标准路径进度，另有 2 个未加载的遗留文件。

| 顶层字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `parent` | ID；可选 | 父进度，决定树中位置 |
| `display` | 对象；可选 | 不提供时作为隐藏的逻辑 / 配方解锁进度 |
| `criteria` | 非空对象；必填 | 自定义条件名 → trigger 对象 |
| `requirements` | 字符串二维数组；可选 | 外层 AND、内层 OR；省略时每个 criterion 都要达成 |
| `rewards` | 对象；可选；空 | 奖励 |
| `sends_telemetry_event` | 布尔；可选；false | 原版遥测标记 |

display：`icon` 必填 ItemStack（`id` 必填，`count` 默认 1，`components` 可选）；`title`、`description` 必填文本组件；`background` 可选资源 ID；`frame` 默认 `task`，另可 `goal` / `challenge`；`show_toast`、`announce_to_chat` 默认 true；`hidden` 默认 false。文本可以是字符串或 `{"translate":"你的翻译键"}`。

rewards：`experience` 可选整数，默认 0；`loot`、`recipes` 可选 ID 数组，默认空；`function` 可选函数 ID。奖励物品应通过 loot table 提供。

每个 criteria 成员为 `{"trigger":"命名空间:类型","conditions":{...}}`。trigger 必填，conditions 可省略。requirements 中使用的是 criteria 的键名，不是 trigger ID。

### DS 注册的全部 8 种 trigger

以下 ID 均加 `dragonsurvival:` 前缀。全部支持可选 `player`，格式为 EntityPredicate 或 loot condition 数组，检查完成行为的玩家。

| trigger | 附加参数 | 触发行为 |
| --- | --- | --- |
| `be_dragon` | 无 | 变为龙，由 DS 的行为调用触发 |
| `sleep_on_treasure` | `nearby_treasure_amount` 可选整数 | 睡在宝藏上；数量要求为实际数量 ≥ 配置值 |
| `mine_block_under_lava` | `block` 可选方块注册表集合 | 在岩浆下挖掘对应方块 |
| `use_dragon_soul` | 无 | 使用龙魂 |
| `stop_natural_growth` | 无 | 停止自然成长 |
| `upgrade_ability` | `ability` 可选技能 ID；`level` 可选整数 | 升级指定技能；level 是精确相等，不是至少 |
| `convert_item_from_ability` | `item_from`、`item_to` 必填非空气物品 ID | 技能转换物品，检查输入和输出物品类型 |
| `steal_from_villager` | 无 | 从村民处偷取 |

trigger 是事件入口，配置 predicate 不会主动制造这个事件。例如 be_dragon 不是每 tick 自动检查一次。

```json
{
  "display": {
    "icon": {"id": "minecraft:dragon_breath"},
    "title": "掌握吐息",
    "description": "将火焰吐息升级到指定等级",
    "frame": "task"
  },
  "criteria": {
    "learn": {
      "trigger": "dragonsurvival:upgrade_ability",
      "conditions": {"ability": "dragonsurvival:nether_breath", "level": 2}
    }
  },
  "rewards": {"experience": 10}
}
```

### DS 文件使用的原版 trigger

以下条件字段均可选，省略表示不限制相应方面；有行为事件才会检查。可选 player 规则同上。

| trigger（minecraft:） | 相关条件字段 | 用途 |
| --- | --- | --- |
| `consume_item` | `item`：ItemPredicate | 吃 / 使用消耗物 |
| `inventory_changed` | `items`：ItemPredicate 数组；`slots`：可选对象，含 occupied/full/empty 整数范围 | 背包变化后的物品检查 |
| `effects_changed` | `effects`：效果 ID → 条件对象；`source`：实体条件 | 药效改变；效果条件可含 amplifier/duration 范围、ambient/visible 布尔 |
| `item_used_on_block`、`placed_block` | `location`：loot condition 数组 | 使用物品于方块 / 放置方块；用 location_check、match_tool 等检查上下文 |
| `location` | `player` | 周期检查玩家的位置 / 状态 |
| `shot_crossbow` | `item`：ItemPredicate | 发射弩箭 |
| `player_interacted_with_entity` | `entity`：实体条件；`item`：ItemPredicate | 用某物品交互实体 |
| `recipe_crafted` | `recipe_id`：必填配方 ID；`ingredients`：可选 ItemPredicate 数组 | 实际合成 |
| `recipe_unlocked` | `recipe`：必填配方 ID | 解锁配方 |
| `tick` | `player` | 每 tick 检查，可用于状态进度 |

ItemPredicate 常用 `items`（物品集合）、`count`（整数范围）、`components`（组件精确值）、`predicates`（组件子谓词对象）。例如附魔检查是 `predicates.minecraft:enchantments` 数组，各项用 `enchantments` 集合及 `levels` 范围；不能使用旧版 NBT 字符串代替组件格式。

EntityPredicate 常用 `type`（实体类型集合）、`distance`（含 absolute/horizontal/x/y/z 数值范围）、`location`（位置谓词）、`effects`、`equipment`（槽位 → ItemPredicate）、`flags`、`type_specific`、`vehicle`、`passenger`、`targeted_entity`。其中原版 location 的结构不等同于世界生成 BlockPredicate。位置谓词常用 dimension、biomes、structures、position{x/y/z 范围}、block{blocks,state}、fluid{fluids,state}、light{light 范围}。

## DS 的实体子谓词

放在 `EntityPredicate.type_specific` 下，由其中的 `type` 分派；它们不是独立 JSON 文件。

### dragonsurvival:dragon_predicate

只匹配已变龙的服务端玩家。普通末影龙、生物龙不属于这个判断，需要另用实体类型或标签。

| 字段 | 类型 / 默认 | 判断 |
| --- | --- | --- |
| `dragon_species` | 龙种集合；可选 | 玩家龙种 |
| `stage_specific` | 阶段谓词；可选 | 阶段与成长进度，见下文 |
| `dragon_body` | 体型集合；可选 | 玩家体型 |
| `ability_levels` | 数组；可选 | 每项必填 ability（技能 ID）、level（整数范围）；所有项都必须满足 |
| `is_growth_stopped` | 可选布尔 | 是否停止自然成长 |
| `marked_by_ender_dragon` | 可选布尔 | DS 的末影龙标记 |
| `flight_was_granted` | 可选布尔 | 是否已授予飞行 |
| `spin_was_granted` | 可选布尔 | 是否已授予旋转 |
| `is_flying` | 可选布尔 | 当前是否处于 DS 飞行状态 |

stage_specific 所有字段可选：`dragon_stage` 是阶段集合；`growth_percentage` 是 0～1 范围；`growth` 是成长值范围。范围可写精确数值或 `{"min":0.5,"max":1.0}`，省略一端代表不限制该端。

阶段有完成兼容规则：当前阶段不在指定集合时，如果 growth_percentage 包含 1.0，且玩家龙种中某个指定阶段已经完成，仍可能匹配；所以“某阶段完成 100%”不必只限仍停留在该阶段的玩家。

```json
{
  "condition": "minecraft:entity_properties",
  "entity": "this",
  "predicate": {
    "type_specific": {
      "type": "dragonsurvival:dragon_predicate",
      "dragon_species": "#dragonsurvival:true_dragons",
      "is_flying": true,
      "ability_levels": [
        {"ability": "dragonsurvival:nether_breath", "level": {"min": 2}}
      ]
    }
  }
}
```

### dragonsurvival:entity_check_predicate

`check_for` 可选，取 `living_entity`、`enemy`、`tamed`、`animal`、`item`、`experience_orb`。按 Java 实体类别检测，适合技能目标过滤；省略时不加该类别限制。

### dragonsurvival:custom_predicates

全部字段及嵌套结构已列在[技能手册第 12 节](../06-dragon-ability-json-reference.md#12-条件与-minecraft-对象)：eye_in_fluid、weather_predicate、sun_light_level、has_duration_effect、is_nearby_entity、player_hunger、health_percentage、has_uuid、looking_at_block。它们可用于接受 EntityPredicate 的位置；仍受该系统提供的上下文约束。

## enchantment：附魔

路径：`data/<ns>/enchantment/<id>.json`。1.21.1 使用数据驱动附魔，顶层不写 type。附魔能否通过附魔台 / 交易 / 战利品获取还取决于相关标签和来源规则。

| 字段 | 类型 / 必填 / 默认 | 含义 |
| --- | --- | --- |
| `description` | 文本组件；必填 | 显示名称 |
| `supported_items` | 物品集合；必填 | 可以承载该附魔的物品 |
| `primary_items` | 物品集合；可选 | 附魔台主要适用物品，缺省使用 supported_items |
| `weight` | 整数 1～1024；必填 | 选取权重 |
| `max_level` | 整数 1～255；必填 | 最高等级 |
| `min_cost`、`max_cost` | 对象；必填 | 每项必填整数 base、per_level_above_first；等级 L 时 base+(L−1)×增量 |
| `anvil_cost` | 非负整数；必填 | 铁砧成本系数 |
| `slots` | 字符串数组；必填 | 生效装备槽：any/mainhand/offhand/hand/feet/legs/chest/head/armor/body |
| `exclusive_set` | 附魔集合；可选；空 | 互斥附魔，引用附魔标签时加 # |
| `effects` | 效果组件对象；可选；空 | 以下组件 ID → 配置数组 |

原文件中的 comment 是被 CODEC 忽略的说明字段，不是行为参数。max_cost 与 min_cost 的 cost 指附魔选择成本，不是直接扣除这么多经验点。

### DS 实际使用的效果组件

| effects 的键 | 数组元素字段 | 作用 |
| --- | --- | --- |
| `minecraft:attributes` | id：必填属性修饰 ID；attribute：必填属性 ID；operation：必填操作；amount：必填 LV | 加属性；operation 为 add_value / add_multiplied_base / add_multiplied_total |
| `minecraft:damage` | effect 必填数值效果；requirements 可选条件 | 修改攻击伤害 |
| `minecraft:damage_protection` | 同上 | 增加原版伤害保护值；不是直接减去同等生命值 |
| `minecraft:post_attack` | enchanted、affected、effect 必填；requirements 可选 | enchanted 指附魔持有者，affected 指受效果者；角色可用 attacker / damaging_entity / victim |
| `minecraft:tick` | effect 必填实体效果；requirements 可选 | 装备生效时每 tick 检查 |

requirements 接受一个 loot condition 或条件数组（数组视为 AND）。附魔提供的 this 等上下文随组件变化，不能照搬龙技能中 this 一定是施法者的假设。

### DS 文件使用的全部嵌套效果类型

以下 type 均加 `minecraft:`；未标可选的字段都必填。LV 的完整类型见[技能手册](../06-dragon-ability-json-reference.md#2-通用数值与单位)。

| type | 参数 | 用途 |
| --- | --- | --- |
| `add` | value：LV | 数值加法 |
| `multiply` | factor：LV | 数值乘法 |
| `all_of` | effects：实体效果数组 | 顺序执行多效果 |
| `apply_mob_effect` | to_apply：效果集合；min_duration、max_duration、min_amplifier、max_amplifier：LV | 随机时长和强度；**duration 单位秒**，amplifier 0 表示 I 级 |
| `damage_entity` | min_damage、max_damage：LV；damage_type：伤害 ID | 伤害实体 |
| `damage_item` | amount：LV | 消耗附魔物品耐久 |
| `play_sound` | sound：声音 ID 或直接定义；volume、pitch：FloatProvider | 播音；音量范围 (0,10]，音调 (0,2]；常数可直接写数字 |

常见 LV：`{"type":"minecraft:linear","base":2.5,"per_level_above_first":2.5}`；限幅为 `{"type":"minecraft:clamped","value":公式,"min":0,"max":1}`。NumberProvider 的 `{"type":"minecraft:enchantment_level","amount":LV}` 把当前附魔等级代入 LV，可用于 random_chance.chance。

### 12 个附魔的实际分工

| 文件名 | 作用 / 实现位置 |
| --- | --- |
| aerodynamic_mastery | JSON 增加 flight_stamina 属性；comment 中的飞行速度不是实际效果 |
| blood_siphon | 受击概率对攻击者施加 blood_siphon，并消耗附魔装备耐久；吸血由 Java 处理效果 |
| bolas | JSON 无 effects；Java 把弩的箭类装填物换成 bolas |
| combat_recovery | 受击概率赋予生命恢复和 regeneration_delay；延迟药效防止连续触发 |
| curse_of_kindness | JSON 降低 hunter_faction_damage 属性 |
| curse_of_outlaw | JSON 每 tick 施加 hunter_omen |
| draconic_superiority | JSON 乘攻击伤害；Java 另处理 dragon_magic 伤害 |
| dragonsbane | JSON 对龙增伤、减速，龙玩家持有时周期自伤 / 磨耐久 / 播音；Java 另处理特定击杀时的成长转移 |
| murderers_cunning | effects 空；Java 对满血目标加伤 |
| overwhelming_might | effects 空；Java 提高施加给其他玩家的有害效果强度，受黑名单限制 |
| sacred_scales | JSON 按概率增加 damage_protection |
| unbreakable_spirit | effects 空；Java 降低其他来源施加的有害效果强度，受黑名单限制 |

因此复制一个空 effects 的附魔并换 ID，不会自动复用 Java 对特定原版附魔 ID 的处理。

## 共用嵌套类型：方块谓词与粒子

### 世界生成 BlockPredicate

这些对象除了用于世界生成，也被 DS 的技能、踩踏等字段复用。以下覆盖当前 JAR 使用的类型，由 `type` 分派；**与进度 LocationPredicate 中的 `block` 对象格式不同**。

| type（minecraft:） | 参数 | 判断 |
| --- | --- | --- |
| `matching_blocks` | `blocks`：必填方块集合；`offset`：可选整数三元数组，默认 [0,0,0] | 对应位置的方块属于集合 |
| `matching_block_tag` | `tag`：必填方块标签 ID，不加 #；`offset` 同上 | 对应位置的方块属于标签 |
| `matching_fluids` | `fluids`：必填原版流体注册表集合；`offset` 同上 | 对应位置的流体属于集合；不是 NeoForge FluidType 集合 |
| `solid` | `offset` 同上 | 方块状态在当前位置被判为 solid |
| `not` | `predicate`：必填子 BlockPredicate | 子判断不成立 |
| `all_of` | `predicates`：必填 BlockPredicate 数组 | 所有子判断成立；空数组为 true |

offset 在当前原版源码中要求每个分量绝对值 **小于 16**，即整数 −15～15；不是任意坐标。复合谓词自身不提供 offset，应写在其子谓词中。

```json
{
  "type": "minecraft:all_of",
  "predicates": [
    {"type": "minecraft:solid", "offset": [0, -1, 0]},
    {"type": "minecraft:not", "predicate": {
      "type": "minecraft:matching_block_tag", "tag": "minecraft:logs"
    }}
  ]
}
```

### ParticleOptions：DS 粒子选项

ParticleOptions 是技能或投射物中嵌套的粒子对象，不是 `assets/.../particles/` 中的贴图列表。对象必填 `type`，其余字段由注册的粒子类型决定；以下类型均加 `dragonsurvival:` 前缀。

| type | 必填参数 | 作用 |
| --- | --- | --- |
| `fire`、`large_fire` | `duration`：float；`swirls`：布尔 | 小 / 大火焰吐息粒子 |
| `poison`、`large_poison` | 同上 | 小 / 大毒雾吐息粒子 |
| `sun`、`large_sun` | 同上 | 小 / 大阳光吐息粒子 |
| `lightning`、`large_lightning` | 同上 | 小 / 大闪电吐息粒子 |
| `treasure` | `red`、`green`、`blue`、`scale`：float | RGB 颜色通常为 0～1；scale 同时影响尺寸和随机寿命 |
| `sea_sweep` | `quadSize`：double，注意大小写 | 海龙横扫粒子；渲染半尺寸计算为 1−quadSize×0.5，并非直接使用该值；寿命参数固定为 4 |
| `netherite_particle`、`diamond_particle`、`gold_particle`、`glow` | 无额外参数 | 三种信标粒子和发光粒子 |

上述必填参数没有默认值，也没有在这些 CODEC 中限制数值范围。duration 被转换为整数并写入客户端粒子的 lifetime，以 tick 为标尺；该基类每次 tick 在父类更新后又递增 age，实际显示时间不能直接按 duration÷20 精确计算。swirls 控制随年龄变化的旋绕偏移。建议 duration 使用正整数、scale 使用正数。

```json
{
  "type": "dragonsurvival:lightning",
  "duration": 37,
  "swirls": true
}
```

粒子的生成数量、位置和速度由外层效果控制，见[技能手册第 11.4 节](../06-dragon-ability-json-reference.md#114-particlerun_functionsummon_entity)；吐息的发射规则见[第 9.2 节](../06-dragon-ability-json-reference.md#92-breath_particles原版吐息粒子发射)。改此处的 ParticleOptions 不会改变伤害；粒子贴图仍由客户端资源决定。

源码：DS `registry/DSParticles.java`、`common/particles/*ParticleOption.java`、`client/particles/`；原版 `world/level/levelgen/blockpredicates/`、`Vec3i.offsetCodec`。

## 条件：运行判断与加载判断

运行时 loot condition 用 `condition` 分派。DS 文件还使用这些组合 / 上下文条件：

| condition（minecraft:） | 参数 | 含义 |
| --- | --- | --- |
| `all_of`、`any_of` | terms：必填条件数组 | 全部 / 任一成立 |
| `inverted` | term：必填条件 | 取反 |
| `entity_properties` | entity：必填目标名；predicate：可选实体谓词 | 检查 this / attacker / direct_attacker / attacking_player 等上下文目标 |
| `damage_source_properties` | predicate：可选伤害源谓词 | 可含 is_direct、source_entity、direct_entity、tags[{id,expected}] |
| `random_chance` | chance：必填 NumberProvider | 概率 |
| `time_check` | value：必填整数范围；period：可选整数 | 对世界时间或时间模 period 后的值判断；period=100、value=0 表示每 100 tick 的整点 |
| `weather_check` | raining、thundering：可选布尔 | 检查世界是否下雨 / 雷暴；省略的状态不限制；字段名不同于 DS 自定义天气谓词 |
| `location_check` | predicate：可选 LocationPredicate；offsetX/offsetY/offsetZ：可选整数，默认 0 | 对上下文原点偏移后检查位置 |
| `match_tool` | predicate：可选 ItemPredicate | 检查工具 / 交互物品上下文 |

加载时条件用 `neoforge:conditions` 数组，数组中每项由 `type` 分派。它决定整个资源是否存在，不是每次技能施放时运行。

| type | 参数 | 判断 |
| --- | --- | --- |
| `dragonsurvival:registered` | registry、value：必填注册表 ID 与条目 ID | 对应条目是否已注册，例如龙种注册表 |
| `neoforge:mod_loaded` | modid：必填字符串 | 模组已加载 |
| `neoforge:item_exists` | item：必填物品 ID | 物品存在 |
| `neoforge:tag_empty` | tag：必填物品标签 ID，不加 # | 物品标签为空 |
| `neoforge:and`、`neoforge:or` | values：必填加载条件数组 | 全部 / 任一成立 |
| `neoforge:not` | value：必填加载条件对象 | 取反 |
| `neoforge:true`、`neoforge:false` | 无 | 常量判断 |

这些条件必须放在支持 NeoForge 条件解码的位置；不能任意在嵌套对象里增加 neoforge:conditions。Data Map 的条件包装形式见[Data Map 文档](06-data-maps.md)。

源码核对：DS 的 `registry/DSAdvancementTriggers.java`、`common/criteria/`、`common/codecs/predicates/`、`common/handlers/EnchantmentEffectHandler.java`、`registry/datagen/data_maps/RegisteredCondition.java`；原版 `Advancement`、`DisplayInfo`、`Enchantment` 及各效果 CODEC。固定提交和源码入口见[总目录](README.md#核对来源与验证范围)。
