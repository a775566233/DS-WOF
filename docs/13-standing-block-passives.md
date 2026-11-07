# 站在特定方块上的被动：原版实现与冰翼龙复用

核对基线：本工程依赖 Dragon Survival **2.0.71 / Minecraft 1.21.1**。原版确实有“站在某类方块上获得加成”的被动，可以复用其 JSON 结构。此前回答遗漏了这些技能，并给出了错误的 `location_check` 字段；本篇更正这些内容。

文中的路径以 `src/main/resources/` 为根。当前工程已经接入 `ice_magic`、`ice_blocks` 和 `on_ice`；下文站冰抵消环境弱点的配置仍是可选示例，尚未应用到生产弱点。细雪内触发与自带天赋的最新接入见 [冰雪适应与细雪行动](14-ice-adaptation-and-powder-snow.md)。

## 1. 原版两个被动怎么实现

| 技能 | 技能 ID | 条件与效果 |
| --- | --- | --- |
| 炽焰魔法 | `dragonsurvival:cave_magic` | 一个动作常驻增加法力上限；另一个动作在热方块等条件下增加自然回蓝属性 |
| 洞穴健将 | `dragonsurvival:cave_athletics` | 检查脚下是否属于特定方块标签，每 20 tick 施加速度效果 |

实际 JAR 内的文件是：

- `data/dragonsurvival/dragonsurvival/dragon_ability/cave_magic.json`
- `data/dragonsurvival/dragonsurvival/dragon_ability/cave_athletics.json`

数据生成源码为 [CaveDragonAbilities.java](../build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/datagen/abilities/CaveDragonAbilities.java)，分别搜索 `context.register(CAVE_MAGIC` 和 `context.register(CAVE_ATHLETICS`。源码快照提交为 `6638c759c5eb224f7de72d9b10b7bec75f9fbafc`；以上 JSON 已与本地实际依赖 JAR 核对。

### 1.1 炽焰魔法

这是可升级至 10 级的被动，每级对应经验成本为 `36 × L`。它的两个动作分别为：

1. 不附加站方块条件，以 `add_value` 增加 `dragonsurvival:mana`，数值为 `1 × L`。
2. 符合热环境条件时，以 `add_value` 增加 `dragonsurvival:mana_regeneration`，数值为 `0.03 × L`。

第二个动作的条件是 `minecraft:any_of`，包含脚下为 `#dragonsurvival:warm_blocks`、实体位置为该类方块、身处岩浆、脚下为点燃的营火或指定炉类等。它的关键判断结构是：

```json
{
  "condition": "minecraft:entity_properties",
  "entity": "this",
  "predicate": {
    "stepping_on": {
      "block": {
        "blocks": "#dragonsurvival:warm_blocks"
      }
    }
  }
}
```

条件放在 `actions[].target_selection.applied_effects.target_conditions`。目标为 `dragonsurvival:self`，因此这里的 `this` 检查玩家自己。

回蓝效果的类型是 `dragonsurvival:modifier`，修改 **`mana_regeneration` 属性**。它没有用定期直接恢复法力的 `mana_recovery` 效果，也不需要附属新增 Java tick 监听器。

### 1.2 洞穴健将

同样是 `self` 目标，被动动作的 `trigger_rate` 为 `20.0`。条件使用相同的 `stepping_on` 结构，只把标签换为 `#dragonsurvival:speeds_up_cave_dragon`。

效果类型为 `dragonsurvival:potion`，`potion.effects` 为 `minecraft:speed`。实际 JSON 中，amplifier 的线性参数为 `base: 0.2`、`per_level_above_first: 0.2`，duration 的线性参数为 `base: 20.0`、`per_level_above_first: 20.0`。药水等级最终涉及整数处理，不应把该配置直接解释成“每级增加 20% 速度”。

### 1.3 条件离开后如何移除

原版回蓝 modifier 使用固定 ID `dragonsurvival:good_mana_condition`，并设置 `should_remove_automatically: true`。每 tick 维持同一修饰，不会每 tick 无限叠加。

[SelfTarget.java](../build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/targeting/SelfTarget.java) 中，持续被动条件不成立时会调用效果的移除方法。仿照原版保留自动移除配置，即可在离开对应方块后取消属性加成。

## 2. 冰翼龙：站冰加快自然回蓝

### 2.1 定义冰方块标签

文件：`data/dswof/tags/block/ice_blocks.json`。1.21.1 使用单数目录 `block`。

```json
{
  "replace": false,
  "values": [
    "minecraft:ice",
    "minecraft:packed_ice",
    "minecraft:blue_ice",
    "minecraft:frosted_ice"
  ]
}
```

### 2.2 定义共享的站冰条件

文件：`data/dswof/predicate/on_ice.json`。

```json
{
  "condition": "minecraft:entity_properties",
  "entity": "this",
  "predicate": {
    "stepping_on": {
      "block": {
        "blocks": "#dswof:ice_blocks"
      }
    }
  }
}
```

`stepping_on` 检查脚下方块；`location` 检查实体所在位置，两者不能直接互换。标签引用放在 `blocks` 内，带 `#` 前缀。

上面展示基本的脚下判断。当前生产 `on_ice.json` 使用 `any_of`：支撑位置匹配 `#dswof:ice_blocks`，或者实体脚部所在格匹配同一标签。这使冰块表面、雪层和进入细雪后的回蓝都能触发。标签只负责分类，不改变方块碰撞；具体采样位置见 [雪层与位置判定](14-ice-adaptation-and-powder-snow.md#11-雪层碰撞与位置判定)。

### 2.3 定义被动技能

文件：`data/dswof/dragonsurvival/dragon_ability/ice_magic.json`。

下面复用炽焰魔法的回蓝动作和升级方式；图标暂时复用原版资源。修饰 ID 使用附属自己的命名空间，避免与原版回蓝修饰冲突。

```json
{
  "activation": {
    "activation_type": "dragonsurvival:passive"
  },
  "upgrade": {
    "upgrade_type": "dragonsurvival:experience_points",
    "maximum_level": 10,
    "experience_cost": {
      "type": "minecraft:linear",
      "base": 36.0,
      "per_level_above_first": 36.0
    }
  },
  "actions": [
    {
      "target_selection": {
        "target_type": "dragonsurvival:self",
        "applied_effects": {
          "targeting_mode": "allies_and_self",
          "target_conditions": {
            "condition": "minecraft:reference",
            "name": "dswof:on_ice"
          },
          "entity_effect": [
            {
              "effect_type": "dragonsurvival:modifier",
              "modifiers": [
                {
                  "base": {
                    "id": "dswof:ice_mana_regeneration",
                    "is_hidden": true,
                    "should_remove_automatically": true
                  },
                  "modifiers": [
                    {
                      "attribute": "dragonsurvival:mana_regeneration",
                      "amount": {
                        "type": "minecraft:linear",
                        "base": 0.03,
                        "per_level_above_first": 0.03
                      },
                      "operation": "add_value"
                    }
                  ]
                }
              ]
            }
          ]
        }
      }
    }
  ],
  "icon": {
    "texture_entries": [
      {
        "from_level": 0,
        "texture_resource": "dragonsurvival:abilities/cave/cave_magic_0"
      }
    ]
  }
}
```

把 `dswof:ice_magic` 加入冰翼龙龙种文件的 `abilities`，并补充技能名称、描述的语言翻译。只创建技能文件不会让它自动出现在当前龙种中。

这个示例只增加回蓝。如果也要仿照炽焰魔法增加法力上限，应另外添加一个无站冰条件的动作，修改 `dragonsurvival:mana`；不要把整个技能禁用条件设置成“没站冰”，否则常驻的上限加成也会受影响。

### 2.4 回蓝数值与执行频率

这里省略动作 `trigger_rate`，使用默认值 1，每 tick 检查条件并维持属性。不要通过给这种属性被动设置 20 tick 冷却来实现“每秒回蓝”；恢复频率由原版 [ManaHandler.java](../build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/handlers/magic/ManaHandler.java) 控制。

原版 `mana_regeneration` 默认值为 `0.004`。没有其他属性修饰、正常 20 TPS 且不在施法时：

| 场景 | 每 tick 回蓝 | 每秒回蓝 |
| --- | --- | --- |
| 默认 | 0.004 | 0.08 |
| 站冰，技能 1 级 | 0.004 + 0.03 | 0.68 |
| 站冰，技能 10 级 | 0.004 + 0.30 | 6.08 |

线性公式为 `base + per_level_above_first × (L - 1)`，所以示例加成为 `0.03L`。原版自然回蓝会在施法时暂停；站冰提高该属性也保留这一规则。如果要求喷吐冰息期间仍然恢复法力，需要另外设计效果。

## 3. 站冰抵消地狱 nether_heat

复用同一个 `dswof:on_ice`，将 `nether_heat.json` 的顶层 `condition` 替换为下列对象，保留原来的 `effect` 与 `trigger`：

```json
{
  "condition": "minecraft:all_of",
  "terms": [
    {
      "condition": "minecraft:entity_properties",
      "entity": "this",
      "predicate": {
        "location": {
          "dimension": "minecraft:the_nether"
        }
      }
    },
    {
      "condition": "minecraft:inverted",
      "term": {
        "condition": "minecraft:reference",
        "name": "dswof:on_ice"
      }
    }
  ]
}
```

含义为“在地狱，并且没有站冰”。这里 `condition` 为假时，`SupplyTrigger` 会恢复储备并返回不触发惩罚；因此站冰会停止热储备消耗、停止该弱点的伤害并恢复储备。实现见 [SupplyTrigger.java](../build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/penalty/SupplyTrigger.java)。

当前工程的 `nether_heat` 配置中 `regeneration_rate` 为 `0.5`，表示每 tick 恢复上限的 50%，不是每秒恢复 0.5 点；正上限的空储备约 2 tick 就能回满。如果希望站冰持续降温，应按期望恢复时间调整。例如 `0.001` 是每 tick 恢复上限的 0.1%，空储备约 50 秒回满。本篇未修改该数值。

不要额外建立一个引用同一 `heat_supply` 的“冰面恢复弱点”，否则多个弱点可能重复推进同一储备。

**这条保护条件不自动依赖 `ice_magic` 是否学习。** 若希望只有学习被动后站冰才抵消地狱热量，需要额外将技能等级条件纳入保护条件。提高回蓝的被动与热量弱点是各自运行的配置，仅引用同一站冰条件不会绑定等级。

如果冰息另有地狱 `usage_blocked`，停止地狱热伤害也不会自动解除禁用。需要站冰时恢复施法，就应将该禁用条件也改成“在地狱且没有站冰”。

## 4. 炎热环境冰息减伤如何与站冰结合

可以保留“炎热群系一律减半”的设定，仅让站冰解决回蓝和地狱储备；这样不需要调整现有 `hot_ice_breath`。

如果设计为“站冰同时免除炎热减伤”，新增 `data/dswof/predicate/hot_without_ice.json`：

```json
{
  "condition": "minecraft:all_of",
  "terms": [
    {
      "condition": "minecraft:reference",
      "name": "dswof:in_hot_environment"
    },
    {
      "condition": "minecraft:inverted",
      "term": {
        "condition": "minecraft:reference",
        "name": "dswof:on_ice"
      }
    }
  ]
}
```

然后同步修改 `hot_ice_breath.json` 的两个位置：

1. 顶层 `condition` 改成引用 `dswof:hot_without_ice`。
2. `effect.modifiers[].base.early_removal_condition` 改成对同一条件取反。

第二处的完整条件对象为：

```json
{
  "condition": "minecraft:inverted",
  "term": {
    "condition": "minecraft:reference",
    "name": "dswof:hot_without_ice"
  }
}
```

这样站冰时停止施加减伤修饰，并提前移除已有修饰。若只修改顶层条件，已有的 40 tick 修饰仍可能短暂残留。

不要用另一个 `add_multiplied_total: +0.5` 来抵消原来的 `-0.5`。两个独立修饰相乘会得到 `0.5 × 1.5 = 0.75`；取消环境惩罚应通过条件阻止施加并移除原修饰。

## 5. 对之前条件示例的更正

之前提供的 `location_check` 中 `offset: [0, -1, 0]` 与 `block.tag` 写法有误。当前版本的 Loot 条件 `minecraft:location_check` 使用 `offsetX`、`offsetY`、`offsetZ`；这里的方块标签应使用 `block.blocks` 的 `#标签ID`。世界生成 BlockPredicate 与这些条件的字段不能混用。

本需求直接使用原版技能已有的 `entity_properties → predicate.stepping_on → block.blocks` 即可。数据生成中的对应工具是 [EntityCondition.java](../build/research/upstream/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/conditions/EntityCondition.java) 的 `isOnBlock(...)`；实体位置检查则是 `isInBlock(...)`。

## 6. 游戏内验证

文中 JSON 已做语法和静态结构核对，尚未在游戏中实测。实际接入后按以下顺序检查：

1. 数据加载没有未知属性、标签或 predicate 引用错误；龙种引用了新被动，并已学习至至少 1 级。
2. 用 `/attribute @s dragonsurvival:mana_regeneration get` 对比站冰、离冰、升级时的值；在停止施法后观察法力变化。
3. 地狱中分别站普通方块和冰块，检查热储备消耗、恢复，以及耗尽后的伤害。
4. 炎热群系内检查 `dswof:ice_breath_multiplier`；若采用站冰豁免方案，对比站冰、离冰时的属性和实际伤害。
5. 检查跳跃、飞行、方块边缘、冰融化、切换龙种，以及重新登录后的修饰移除与条件更新。

环境弱点的其他参数见 [冰翼龙环境弱点接入](11-environment-penalties.md)，专属伤害倍率见 [属性注册与冰息专属倍率](12-attributes-and-ice-breath-multiplier.md)。
