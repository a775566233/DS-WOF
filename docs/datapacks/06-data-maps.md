# Data Map：饮食、外观、信标与末地平台

[返回总目录](README.md)。Data Map 把额外数据附着到已有龙种 / 体型。它有固定文件名；不是随意新建 `ice_food.json` 就能读取。

## 五个固定路径

以下路径都以 `data/dragonsurvival/data_maps/dragonsurvival/` 开头，即使内容属于 dswof，也保持此前缀：

| 文件后半段 | values 的外层键 | 每个值 | 合并 |
| --- | --- | --- | --- |
| `dragon_species/diet_entries.json` | 龙种 ID / `#` 龙种标签 | DietEntry 数组 | 按 items 字符串替换同名条目，再追加新条目 |
| `dragon_species/stage_resources.json` | 龙种 ID / 标签 | 阶段 ID → StageResource | 合并内部 map；同阶段由后加载值覆盖 |
| `dragon_species/end_platforms.json` | 龙种 ID / 标签 | EndPlatform | 整值替换 |
| `dragon_species/dragon_beacon_data.json` | 龙种 ID / 标签 | DragonBeaconData | 整值替换 |
| `dragon_body/body_icons.json` | 体型 ID / 标签 | 龙种 ID → 图标资源 ID | 合并内部 map；同龙种由后加载值覆盖 |

## 通用文件结构与覆盖

| 字段 | 必填 / 默认 | 作用 |
| --- | --- | --- |
| `values` | 必填对象 | 键为被附着的注册表 ID / 标签，值由对应 Data Map 决定 |
| `replace` | 可选布尔；false | true 时清除先前包对此 map 的贡献，再处理本文件 |
| `remove` | 可选；`[]` | 删除之前的附着数据；格式见下文 |

也可以仅替换单个键：将其值包装为 `{"value":实际值,"replace":true}`。这个 value 与条件包装的 `neoforge:value` 是两个不同层次。

条件使用 `neoforge:conditions`：对象可附加此字段；数组值可包装为 `{"neoforge:conditions":[条件],"neoforge:value":[条目]}`。条件不成立会跳过该值。DS 原版常用 `dragonsurvival:registered` 判断龙种是否注册，字段见[条件手册](08-advancements-enchantments-and-conditions.md)。

普通 remove 可以写 `"remove":["dswof:sky_dragon"]` 删除整个键。三个高级 map（diet_entries、stage_resources、body_icons）还支持 `"remove":[{"key":"dswof:sky_dragon","remover":[内部键]}]`，只删除值的一部分。内部键分别是 items 原字符串、阶段 ID、龙种 ID。

例如 diet_entries 删除一个饮食定义：

```json
{
  "values": {},
  "remove": [
    {
      "key": "dswof:sky_dragon",
      "remover": ["#minecraft:fishes"]
    }
  ]
}
```

remover 精确匹配 `items` 字符串；它不会展开这个标签再逐项删除其他条目。相同 items 会被后加载条目替换；两个不同标签即使最终包含同一物品，也不是“同一个饮食条目”，最终食物映射受条目处理顺序影响。

## diet_entries：全部字段

每个 DietEntry：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `items` | 单个字符串；必填 | 物品 ID、`#` 物品标签或 DS 资源正则；不是 ID 数组 |
| `properties` | FoodProperties；可选；原物品食物属性 | 自定义营养、饱和度、食用耗时与药效 |
| `retain_effects` | 布尔或 RetainEffects；可选；无 | 选择是否保留原物品药效 |

RetainEffects 是对象，`beneficial`、`neutral`、`harmful` 均为可选布尔，默认 false，分别保留有益、中性、有害药效。true 全保留，false 全不保留。

| properties | retain_effects | 结果 |
| --- | --- | --- |
| 无 | 无 | 使用原食物基础属性，清除原药效 |
| 无 | 有 | 使用原食物基础属性，只保留选中的原药效 |
| 有 | 无 / false | 使用自定义属性与自定义药效 |
| 有 | true / 筛选对象 | 使用自定义属性，并追加选中的原药效 |

原物品不是食物且没有 properties 时，该条目不会令其可食用，DS 会记录警告。自定义 properties 不是对原属性逐字段补丁，必须给齐必填字段。

### FoodProperties 的全部字段

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `nutrition` | 非负整数；必填 | 恢复的饥饿值；2 点等于一个完整鸡腿图标 |
| `saturation` | float；必填 | 最终饱和度数值；不是 Java Builder 的 saturationModifier |
| `can_always_eat` | 布尔；可选；false | 满饥饿值仍可吃 |
| `eat_seconds` | 正 float；可选；1.6 | 食用耗时，单位秒 |
| `using_converts_to` | 单个 ItemStack；可选；无 | 食用后的剩余物，如碗或瓶 |
| `effects` | 数组；可选；`[]` | 每项必填 `effect`（原版 MobEffectInstance），可选 `probability`（0～1，默认 1） |

effect 采用原版实例格式：`id` 必填 MobEffect ID；`amplifier` 为 0～255，默认 0（0 是 I 级），`duration` 默认 0 tick（-1 无限），`ambient` 默认 false，`show_particles` 默认 true，`show_icon` 默认跟随 show_particles。`hidden_effect` 可递归包含同一效果的 Details（amplifier、duration 等，不再写 id）；NeoForge 还提供可选 `neoforge:cures` 字符串数组，指定解药类型，如 milk / honey，省略采用效果自身默认解药。它不是技能的 PotionData。

完整饮食示例，保存到固定 diet_entries 路径：

```json
{
  "values": {
    "dswof:sky_dragon": [
      {
        "items": "minecraft:cooked_beef",
        "properties": {
          "nutrition": 8,
          "saturation": 12.8,
          "eat_seconds": 1.6
        },
        "retain_effects": false
      },
      {"items": "#minecraft:fishes", "retain_effects": true}
    ]
  }
}
```

## stage_resources：每阶段外观

value 是“阶段 ID → 下表对象”，例如 `"dragonsurvival:newborn": {...}`。每阶段必填两个对象：

| 对象 | 必填字段 | 作用 |
| --- | --- | --- |
| `growth_icon` | `icon`、`hover_icon`：资源 ID | 成长页正常 / 悬停图标 |
| `default_skin` | `skin`、`glow_skin`：资源 ID | 默认皮肤与发光皮肤 |

必须覆盖该龙种实际使用的阶段，新增古龙阶段也要补外观。完整皮肤例子见[工程原型](../examples/README.md)；用完整 `textures/...png` 路径，并确保客户端存在这些资源。

## body_icons：体型选择图标

values 外层是体型，内层是龙种；例如：

```json
{
  "values": {
    "dswof:sky_body": {
      "dswof:sky_dragon": "dswof:textures/gui/body/sky.png"
    }
  }
}
```

没有专用图标时体型可使用 `dragon_body.default_icon` 回退。添加 body_icons 不会自动把该体型加入 species.bodies。

## end_platforms：末地出生平台

| 字段 | 类型 / 必填 | 作用 |
| --- | --- | --- |
| `structure` | 结构模板 ID；必填 | 对应 `data/<ns>/structure/<path>.nbt`，不是 worldgen/structure JSON |
| `spawn_position` | BlockPos 数组；必填 | `[x,y,z]`，末地的绝对出生位置 |

模板首次按尺寸居中放置；处理器按 structure ID 记录平台是否已经生成。spawn_position 不是相对模板偏移。多个龙种共用相同模板 ID 时，应理解其“已生成”状态也以模板 ID 区分。

## dragon_beacon_data：龙信标

| 字段 | 类型 / 必填 | 作用 |
| --- | --- | --- |
| `effects` | 效果数组；必填 | 信标给予的效果 |
| `payment_data` | 支付配置；必填 | 经验支付后的强化规则 |

effects 每项：`effect` 必填 MobEffect ID；`duration` 可选整数，默认 -1、至少 -1，单位 tick；`amplifier` 可选非负整数，默认 0。

payment_data：`experience_cost` 可选非负整数，默认 0，消耗的是**经验点**而非经验等级；`duration_multiplier` 可选非负整数，默认 1；`amplifier_modification` 可选整数，默认 0。支付后时长乘倍率、强度加修正。未支付的常规范围赋予效果不应用支付强化。无限时长 -1 与倍率组合应实际测试，不把负时长乘法当成通用永久规则。

这些数据用于配置 / 创建信标；已放置方块实体会保存自己的 data，修改 Data Map 后不能假定全部旧信标都会即时刷新。

源码：`registry/DSDataMaps.java`、`registry/data_maps/`、`common/codecs/DietEntry.java`、`StageResources.java`、`DragonBeaconData.java`、`server/handlers/EndPlatformHandler.java`、`common/blocks/DragonBeacon.java`。NeoForge 外壳以 `DataMapFile`、`DataMapEntry` 为准。
