# dragon_stage：成长阶段参数

[返回总目录](README.md)。路径：`data/<ns>/dragonsurvival/dragon_stage/<id>.json`。一个文件定义一个阶段；龙种按阶段链成长，阶段皮肤由 `stage_resources` Data Map 提供。

## 全部顶层字段

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `is_default` | 布尔；可选；false | 是否加入默认成长阶段链 |
| `growth_range` | Bounds；必填 | 本阶段的成长区间 |
| `ticks_until_grown` | 整数；必填；1～630720000 | 走完整个区间所需自然成长 tick；上限由 `Functions.daysToTicks(365)` 给出，按现实日换算，不是 365 个游戏日 |
| `modifiers` | Modifier 数组；可选；`[]` | 本阶段的生命、尺寸、攻击、吐息射程等属性 |
| `growth_items` | GrowthItem 数组；可选；`[]` | 推进、倒退或切换成长行为的物品 |
| `is_natural_growth_stopped` | EntityPredicate；可选；无 | 谓词成立时停止自然成长；不是 loot condition |
| `destruction_data` | DestructionData；可选；无 | 长到指定大小后的踩踏与破坏规则 |

`growth_range` 必填 `min`、`max` 两个 double，要求 `min >= 1`、`max > min`。相邻阶段必须满足前一个 `max ==` 后一个 `min`；默认阶段会排序后检查，自定义阶段集合也要保持正确顺序。不要用 `max: 40` 接 `min: 41`。

每 tick 的自然成长增量是 `(max - min) / ticks_until_grown`。例如范围 20～40、时长 240000，则正常 TPS 下约 3 小时 20 分钟走完该阶段。

`modifiers` 的三字段格式与[体型属性](02-body-and-emotes.md)相同；公式输入为 `当前成长值 - 本阶段 growth_range.min`，阶段起点是 0，而不是技能等级 1。原版阶段使用 LV 与精确公式来随成长调整属性，修改前应比较相邻阶段边界，避免跨阶段骤增 / 骤降。

## growth_items 的每项

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `items` | Item 集合；必填 | 可以使用的物品 / 物品标签 |
| `growth_in_ticks` | 整数；必填 | 等价自然成长时长；正数推进、负数倒退，转为该阶段的成长增量 |
| `maximum_usages` | 整数；可选；-1；至少 -1 | 使用次数上限；-1 为无限 |

`growth_in_ticks` 是时间换算值，不是直接增加几格成长。原版也使用 0 的条目配合物品自身的行为；不能认定所有 0 值成长道具都没有作用。

## destruction_data 的全部字段

| 字段 | 类型 / 必填 | 作用 |
| --- | --- | --- |
| `entity_predicate` | EntityPredicate；必填 | 可以被踩踏的实体条件 |
| `block_predicate` | 世界生成 BlockPredicate；必填 | 可以被破坏的方块条件 |
| `crushing_growth` | double；必填 | 开始允许踩踏的成长值 |
| `block_destruction_growth` | double；必填 | 开始允许破坏方块的成长值 |
| `crushing_damage_scalar` | double；必填 | 踩踏伤害缩放系数 |

两项成长阈值都必须处于该阶段的闭区间 `[min,max]` 内；否则 DS 的阶段验证会失败。服务端游戏规则及模组配置仍可能影响实际破坏行为。

## 最小阶段示例

```json
{
  "is_default": false,
  "growth_range": {"min": 60, "max": 80},
  "ticks_until_grown": 240000,
  "modifiers": [],
  "growth_items": [
    {
      "items": ["dragonsurvival:elder_dragon_heart"],
      "growth_in_ticks": 24000,
      "maximum_usages": 3
    }
  ]
}
```

它定义阶段，但不会自动让全部龙种进入该阶段。可由 species 的 `custom_stage_progression` 形成完整阶段链；全局新增默认阶段则要保证默认阶段整体连续。当前内置 `newborn`、`young`、`adult` 是启动验证要求存在的条目，不应直接删除。

源码：`registry/dragon/stage/DragonStage.java`、`common/codecs/GrowthItem.java`、`MiscCodecs.Bounds`、`MiscCodecs.DestructionData`。原版古龙可选包见[内置包文档](09-worldgen-and-built-in-packs.md)。
