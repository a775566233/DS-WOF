# dragon_penalty：弱点与惩罚参数

[返回总目录](README.md)。路径：`data/<ns>/dragonsurvival/dragon_penalty/<id>.json`。定义环境弱点、装备禁用、动物避让等行为；通过 species 的 `penalties` 引用才会作用于该龙种，服务端 `ENABLE_PENALTIES` 配置也会控制执行。

## 顶层字段

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `icon` | 资源 ID；可选；无 | 弱点界面图标 |
| `condition` | loot condition；可选；无 | 条件成立允许触发惩罚；省略视为成立。支持 DS conditional 包装 |
| `effect` | PenaltyEffect；必填 | 实际施加的惩罚 |
| `trigger` | PenaltyTrigger；必填 | 触发时机和抗性储备规则 |

`condition` 的实体上下文是受惩罚玩家。它不是“满足时免疫”；要表达有某效果就免疫，需使用 `minecraft:inverted`。

地狱掉血、禁用冰息和炎热环境减伤的组合示例，见[冰翼龙环境弱点接入](../11-environment-penalties.md)。其中区分了通用技能属性减伤和只影响冰息的实现。

## effect：全部 9 类

分派字段为 `penalty_type`，下表类型前缀均为 `dragonsurvival:`。标为必填的字段位于同一个 effect 对象内。

| 类型 | 字段 | 行为 |
| --- | --- | --- |
| `take_damage` | `damage_type`：必填 DamageType ID；`amount`：必填 float | 直接伤害玩家；amount 是常数，不能填 LV 等级公式 |
| `mob_effect` | `potion`：必填 PotionData | 施加状态效果 |
| `item_blacklist` | `items`：可选字符串数组，默认 `[]`；`predicate`：可选 ItemPredicate | 两者任一匹配即禁止装备 / 使用；执行时丢出相应主手、护甲、副手、爪槽以及兼容饰品槽物品 |
| `damage_modification` | `modification`：必填 DamageModification；`duration`：必填整数，至少 -1 | 持续修改受伤 / 伤害规则；外层 duration 是实际创建实例时长，-1 无限 |
| `fear` | `fears`：必填 Fear 数组 | 使符合条件的周边生物避让玩家 |
| `informational` | 无额外参数 | 仅描述信息，不施加额外效果 |
| `modifier` | `modifiers`：必填 ModifierWithDuration 数组 | 修改玩家属性 |
| `effect_modification` | `modifications`：必填 EffectModification 数组 | 修改状态效果持续时间 / 强度 |
| `run_function` | `function`：必填函数 ID | 执行数据包函数；文件放 `data/<ns>/function/<path>.mcfunction` |

PotionData、DamageModification、ModifierWithDuration、EffectModification 与[技能手册第 9、11 节](../06-dragon-ability-json-reference.md#11-共用与嵌套对象)共用同一 CODEC。`damage_modification` 在该版本同时存在外层 duration 与内部基础数据中的 duration；不能因为内部写了时长就省略外层字段。

`item_blacklist.items` 每项支持完整物品 ID、`#` 物品标签、Java 正则形式的 `namespace:path`，例如 `.*:.*_bow`。它使用正则而非 glob；`*` 不能直接表示任意字符串。如果字符串本身是合法资源 ID，会优先按精确 ID 处理，因此 `minecraft:bow.` 不会按正则展开。`predicate` 是原版物品谓词，可同时检查组件、数量等。

### Fear 的全部字段

| 字段 | 类型 / 默认 | 作用 |
| --- | --- | --- |
| `base` | 必填 DurationInstanceBase | 实例 ID、时限、图标和自动移除等；同技能手册 11.1 |
| `entity_condition` | 可选 loot condition | 筛选被吓跑的生物，不是筛选龙玩家 |
| `distance` | 必填 LV | 逃离 / 避让检测距离 |
| `walk_speed` | 可选 LV；1 | 普通避让速度参数 |
| `sprint_speed` | 可选 LV；1.3 | 紧急避让速度参数 |

惩罚提供共用效果时采用其运行逻辑提供的等级；不是可升级技能的等级。这里只配置 Fear 的定义，不要把玩家存档里生成的 Instance 字段复制进来。

## trigger：全部 5 类

分派字段为 `penalty_trigger`，类型前缀均为 `dragonsurvival:`。

| 类型 | 字段 | 触发规则 |
| --- | --- | --- |
| `instant` | `trigger_rate`：必填整数 | 世界时间能被此值整除且 condition 成立时执行。请使用正数；0 虽能解码，但取模会出错 |
| `supply` | 见下表 | 先消耗抗性储备，耗尽后按间隔惩罚 |
| `item_used` | `item_predicates`：必填 ItemPredicate 数组 | 使用物品事件中任一谓词匹配时触发 |
| `hit_by_projectile` | `projectiles`：必填 EntityType 集合 | 被指定投射物击中时触发 |
| `hit_by_water_potion` | 无额外参数 | 被水药水影响时触发 |

### supply 的全部字段

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `supply_type` | ID；必填 | 玩家抗性储备的存储键；不同储备使用不同 ID |
| `attribute` | Attribute ID；可选；`dragonsurvival:penalty_resistance_time` | 决定储备上限的属性 |
| `trigger_rate` | 整数 >= 1；必填 | 储备耗尽后的惩罚检查间隔 tick |
| `reduction_rate` | float；必填 | 不利条件下每 tick 减少多少储备 |
| `regeneration_rate` | float；必填 | 条件不成立时每 tick 恢复多少储备 |
| `recovery_items` | 数组；可选；`[]` | 消耗指定物品时恢复储备 |
| `display_like_hunger_bar` | 布尔；可选；false | 使用类似饥饿条的显示形式 |
| `particles_on_trigger` | ParticleOptions；可选；无 | 不利条件中到达检查间隔时生成粒子；不等同于伤害粒子 |

每个 recovery_items 条目必须有 `item_predicates`（ItemPredicate 数组，任一匹配）和 `percent_restored`（float；0.5 表示上限的一半）。

运行顺序是每 tick 推进计数，再根据 condition 消耗或恢复。条件不成立时恢复并直接退出；条件成立时到达间隔才检查是否耗尽。`trigger_rate` 不控制储备每 tick 的消耗速率。以最大储备 200、reduction_rate 1 为例，连续处于不利环境约 10 秒后才开始进入可受罚状态。

## 完整示例：在雨中受伤

保存到 `data/dswof/dragonsurvival/dragon_penalty/rain_weakness.json`，再把 `dswof:rain_weakness` 加到目标 species.penalties。

```json
{
  "condition": {
    "condition": "minecraft:entity_properties",
    "entity": "this",
    "predicate": {
      "type_specific": {
        "type": "dragonsurvival:custom_predicates",
        "weather_predicate": {"is_raining": true}
      }
    }
  },
  "effect": {
    "penalty_type": "dragonsurvival:take_damage",
    "damage_type": "dragonsurvival:rain_burn",
    "amount": 1.0
  },
  "trigger": {
    "penalty_trigger": "dragonsurvival:supply",
    "supply_type": "dswof:rain_supply",
    "trigger_rate": 40,
    "reduction_rate": 1.0,
    "regeneration_rate": 0.013
  }
}
```

天气谓词检查的是玩家所处环境的 DS 判断，具体规则见[条件文档](08-advancements-enchantments-and-conditions.md)。

源码：`registry/dragon/penalty/` 全目录、`registry/attachments/PenaltySupply.java`、`registry/attachments/FearData.java`、`common/codecs/Fear.java`、`common/codecs/ResourceLocationWrapper.java`。版本与源码链接见[总目录](README.md#核对来源与验证范围)。
