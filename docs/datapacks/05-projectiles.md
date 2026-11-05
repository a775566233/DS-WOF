# projectile_data：投射物参数

[返回总目录](README.md)。路径：`data/<ns>/dragonsurvival/projectile_data/<id>.json`。定义 DS 通用球体 / 箭矢的外观、运动与效果。它不是新 EntityType；技能的 `projectile` 效果引用本条目后，由通用实体承载。

## 顶层与 general_data

顶层只有两个必填对象：`general_data`、`type_data`。type_data 先尝试球体格式，再尝试箭矢格式，**没有额外的 type 分派键**；不要同时填写两套格式。

general_data 的全部字段：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `name` | ID；必填 | 投射物的名称资源标识 |
| `is_impact_projectile` | 布尔；可选；false | 是否具有 impact projectile 属性；与允许投射物破坏方块的游戏规则共同决定相关交互 |
| `entity_hit_condition` | loot condition；可选；无 | 判断目标是否接受实体命中效果 |
| `ticking_effects` | ProjectileTargeting 数组；必填 | 飞行 / 存在期间执行的效果，可填 `[]` |
| `common_hit_effects` | ProjectileTargeting 数组；必填 | 命中时执行的通用效果，可填 `[]` |
| `entity_hit_effects` | 实体效果数组；必填 | 直接对被击中实体执行，可填 `[]` |
| `block_hit_effects` | 方块效果数组；必填 | 对命中方块执行，可填 `[]` |

## type_data：球体

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `resources` | LevelBasedResource；必填 | 按投射物等级选择资源基名；模型与贴图共用基名，由渲染器解析 |
| `trail_particle` | ParticleOptions；可选；无 | 运动轨迹粒子 |
| `on_destroy_effects` | ProjectileTargeting 数组；可选；`[]` | 销毁时执行的效果 |
| `behaviour_data` | 下表对象；必填 | 尺寸、弹跳、停留与寿命 |

LevelBasedResource 的格式见[技能手册 8.2](../06-dragon-ability-json-reference.md)。资源基名 `dswof:ice_ball` 对应 `assets/dswof/geo/projectiles/ice_ball.geo.json`、`textures/entity/projectiles/ice_ball.png`、`animations/projectiles/ice_ball.animation.json`。不要把 resources 写成完整 PNG 路径。

behaviour_data 中每项均为 LV：

| 字段 | 必填 / 默认 | 作用与单位 |
| --- | --- | --- |
| `width` | 必填 | 碰撞箱宽，方块单位 |
| `height` | 必填 | 碰撞箱高，方块单位 |
| `max_bounces` | 可选；0 | 最大弹跳次数 |
| `max_lingering_ticks` | 可选；0 | 命中后最多停留 tick |
| `max_movement_distance` | 必填 | 最大移动距离，方块单位 |
| `max_lifespan` | 必填 | 最长存活 tick |

LV 使用投射物保存的等级，通常由发射它的技能传入。寿命、停留、弹跳等最终按实体代码转换为整数。

## type_data：箭矢

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `texture` | LevelBasedResource；必填 | 按等级选择箭矢贴图资源 |
| `piercing_level` | LV；可选；0 | 穿透等级 |

箭矢的模型 / 物理行为由通用箭实体实现，不使用球体的 behaviour_data。

## ProjectileTargeting：效果位置和间隔

每项有必填 `target_type` 与必填 `general_data`。注意这里的 general_data 是“目标配置”，不是顶层同名对象。

| target_type | 额外字段 | 目标 |
| --- | --- | --- |
| `dragonsurvival:point` | 无 | 投射物当前位置的 BlockPos；不会自动生成实体目标 |
| `dragonsurvival:area` | `radius`：必填 LV；`particle_trail`：可选 ParticleOptions | AABB 范围里的方块位置与实体；不是球形距离过滤 |

目标配置 general_data：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `effects` | ConditionalEffect 数组；必填 | 每项含必填 `effect`（下文通用效果）及可选 `condition`（loot condition） |
| `tick_rate` | 整数；可选；1 | 按世界 gameTime 取模决定执行间隔 |
| `chance` | double；可选；1.0 | 间隔符合时的执行概率，建议 0～1 |

该版本 `tick_rate: 0` 会跳过间隔和概率检查；它不表示禁用效果。区域效果会遍历多个目标，世界效果可能被执行多次；不要把区域爆炸理解成“无论有多少目标都只爆一次”。直接 entity_hit_effects / block_hit_effects 不带这层 ConditionalEffect 包装。

## 全部实体、方块与世界效果

**分派字段与技能 JSON 不同**：实体使用 `entity_effect`，方块使用 `block_effect`，世界使用 `world_effect`；不能填技能的 `effect_type`。以下类型值均加 `dragonsurvival:` 前缀；除注明默认外均必填。

### 实体效果：6 类

| 类型 | 全部字段 | 行为 |
| --- | --- | --- |
| `damage` | `damage_type`：DamageType ID；`amount`：LV | 命中伤害。玩家主人存在时乘龙技能伤害属性，并执行玩家互伤检查；没有技能 damage 的 scale / expression 字段 |
| `potion` | `potion`：PotionData | 施加状态效果 |
| `lightning` | `data`：LightningData | 雷击 |
| `particle` | `particle_data`：SpawnParticles；`particle_count`：LV | 生成粒子 |
| `run_function` | `function`：函数 ID | 执行函数 |
| `push` | `target_direction`：TargetDirection；`push_force`：LV | 推动目标 |

### 方块效果：3 类

| 类型 | 全部字段 | 行为 |
| --- | --- | --- |
| `particle` | `particle_data`：SpawnParticles；`particle_count`：LV | 在目标方块处生成粒子 |
| `run_function` | `function`：函数 ID | 在该位置执行函数 |
| `area_cloud` | `potion`：PotionData；`duration`、`probability`：LV；`particle`：ParticleOptions；`delay`：可选 LV，默认 0；`radius`：可选 LV，默认 1 | 生成药水云；duration / delay 是云时长 / 等待 tick，potion.duration 是目标中状态时长 |

### 世界效果：4 类

| 类型 | 全部字段 | 行为 |
| --- | --- | --- |
| `explosion` | `damage_type`：DamageType ID；`explosion_power`：LV；`fire`、`break_blocks`、`can_damage_self`：布尔 | 爆炸；三个布尔都必须提供 |
| `lightning` | `data`：LightningData | 在效果位置雷击 |
| `particle` | `particle_data`：SpawnParticles；`particle_count`：LV | 在效果位置生成粒子 |
| `run_function` | `function`：函数 ID | 在效果位置执行函数 |

LightningData 有且仅有三个必填布尔：`ignores_items_and_experience`（忽略掉落物与经验球）、`spawns_fire`（生成火焰）、`can_hurt_self`（可伤害施法者）。PotionData、SpawnParticles、TargetDirection 的全字段与[技能手册第 9、11 节](../06-dragon-ability-json-reference.md#11-共用与嵌套对象)共用。

“通用效果”解码时尝试方块 / 实体 / 世界三个分支；执行时只把与目标 Java 类型相符的分支应用过去。point 中写 entity_effect 不会因此对当前位置的所有实体造成伤害，应使用 area 或 entity_hit_effects。

## 示例：复用原版球体，修改命中伤害

最稳妥的做法是从[逐文件索引](10-file-index.md)找到现有 projectile_data，复制完整文件，保留 type_data 和所有效果数组，再替换 entity_hit_effects 中的这一项：

```json
{
  "entity_effect": "dragonsurvival:damage",
  "damage_type": "dragonsurvival:fire_breath",
  "amount": 6.0
}
```

上面是一个数组元素，不是完整投射物文件。DamageType 必须存在；原版龙息通常是范围与粒子效果组合，**复用吐息通常改 dragon_ability，不是强行改成 projectile_data**，详见[吐息示例](../06-dragon-ability-json-reference.md#13-完整吐息示例与修改位置)。

实体命中谓词上下文中 THIS_ENTITY 是被击中目标、ATTACKING_ENTITY 是投射物；位置上下文中 THIS_ENTITY 是投射物并提供 ORIGIN / BLOCK_STATE。引用攻击者条件时要区分“投射物实体”和“主人玩家”。

源码：`registry/projectile/`、`common/entity/projectiles/GenericBallEntity.java`、`GenericArrowEntity.java`、`server/handlers/LightningHandler.java`。版本与源码链接见[总目录](README.md#核对来源与验证范围)。
