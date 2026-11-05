# dragon_species：龙种参数

[返回总目录](README.md)。路径：`data/<ns>/dragonsurvival/dragon_species/<id>.json`。一个文件定义一种龙的玩法组合，关联体型、技能、弱点与界面资源。饮食、阶段皮肤和信标等由 [Data Map](06-data-maps.md) 单独挂载。

## 顶层全部字段

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `starting_growth` | double；可选；阶段链起点；至少 1 | 新变成该龙种时的初始成长值，建议落在所用阶段范围内 |
| `unlockable_behavior` | 对象；可选；不设解锁门槛 | 解锁条件与祭坛可见性，见下文 |
| `mana_handling` | 对象；可选；DS 默认配置 | 经验换法力、经验等级提供的额外最大法力 |
| `custom_stage_progression` | DragonStage 集合；可选；默认阶段链 | 自定义阶段列表，各相邻阶段成长区间必须衔接 |
| `bodies` | DragonBody 集合；可选；空集合 | 额外适用体型；默认体型的参与还受 body 的 `is_default` 规则影响 |
| `abilities` | DragonAbility 集合；可选；空集合 | 该龙种具备的技能，可直接列 ID 或引用技能标签 |
| `penalties` | DragonPenalty 集合；可选；空集合 | 该龙种的弱点 / 惩罚 |
| `misc_resources` | 对象；必填 | 龙种界面图片、颜色和食物提示 |

不要添加旧版的顶层 `food`、`skin`、`base_health` 等自造字段来替代这套结构。当前 species 没有直接读取顶层属性修饰器的字段；属性通常来自体型、阶段、技能或其他系统。

## unlockable_behavior

| 字段 | 类型 / 默认 | 作用 |
| --- | --- | --- |
| `unlock_condition` | loot condition；可选；视为满足 | 检查玩家能否解锁 |
| `visibility` | 字符串；可选；按当前解锁状态处理 | 祭坛里的展示策略 |

`visibility` 只接受小写：`always_visible`、`always_hidden`、`visible_if_locked`。前两者使祭坛条目始终展示 / 隐藏；`visible_if_locked` 会让尚未解锁的条目也参与祭坛显示。该字段用于可见性，不能替代真正的解锁条件。龙种选择页与祭坛的筛选分支不同，不能假定隐藏策略在所有界面完全一致。

## mana_handling

| 字段 | 类型 / 省略字段时默认值 | 作用 |
| --- | --- | --- |
| `mana_xp_conversion` | 非负 double；0 | 每经验点可折算的法力；例如 0.1 对应约 10 经验点换 1 法力，实际扣点取整 |
| `mana_per_level` | 非负 double；0 | 每个玩家经验等级增加的最大法力 |
| `max_mana_from_levels` | 非负 double；0 | 经验等级所提供额外最大法力的上限 |

**省略整个对象与写空对象不同。** 不写 `mana_handling` 使用 DS 默认 `(0.1, 0.25, 9)`；写 `{}` 则三个字段均为 0。额外最大法力按 `min(max_mana_from_levels, 玩家经验等级 × mana_per_level)` 计算，不是技能等级。经验补蓝还受玩家当前模式、现有经验和法力系统控制。

## misc_resources 的全部字段

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `food_sprites` | 资源 ID；可选；无 | 龙种饥饿条图片 |
| `mana_sprites` | ManaSprites；可选；无 | 法力条四种状态图片，未配置走渲染器回退 |
| `altar_banner` | 资源 ID；必填 | 祭坛里的龙种横幅 |
| `ability_bar` | 资源 ID；必填 | 施法蓄力条 |
| `growth_left_arrow` | HoverIcon；可选；DS 默认左箭头 | 成长页左箭头 |
| `growth_right_arrow` | HoverIcon；可选；DS 默认右箭头 | 成长页右箭头 |
| `growth_crystal` | FillIcon；必填 | 成长页水晶空 / 满状态 |
| `food_tooltip` | FoodTooltip；必填 | 饥饿和饱和度提示中的字体与字符 |
| `primary_color` | TextColor；必填 | 主要界面颜色 |
| `secondary_color` | TextColor；必填 | 次要界面颜色 |
| `claw_texture_slot` | 字符串；可选；`pickaxe` | 龙爪物品栏用于纹理显示的槽位；可选 `sword`、`pickaxe`、`axe`、`shovel`，必须小写 |
| `custom_growth_info` | 文本组件；可选；无 | 自定义成长提示 |

嵌套结构：

| 对象 | 字段 |
| --- | --- |
| HoverIcon | 必填资源 ID `icon`（正常）与 `hover_icon`（悬停） |
| FillIcon | 必填资源 ID `empty`、`full` |
| ManaSprites | 必填资源 ID `full`、`reserved`、`recovery`、`empty` |
| FoodTooltip | `font`：可选字体 ID，默认 `dragonsurvival:food_tooltip_icon_font`；必填字符串 `nutrition_icon`、`saturation_icon`；可选 TextColor `color` |

这里原版界面资源通常写 `dragonsurvival:textures/...png` 完整纹理路径；与技能 `icon` 的 GUI sprite 路径不同。FoodTooltip 的字符需要字体提供对应字形；复制一个私用区字符但没有对应字体定义，可能显示方框。

## 示例

以下使用已有 DS 图片，表示结构可用的开发起点；自己的图像逐步替换到 `assets/dswof/`。

```json
{
  "starting_growth": 14,
  "bodies": ["dragonsurvival:west"],
  "abilities": ["dragonsurvival:nether_breath"],
  "penalties": [],
  "mana_handling": {
    "mana_xp_conversion": 0.1,
    "mana_per_level": 0.25,
    "max_mana_from_levels": 9
  },
  "misc_resources": {
    "altar_banner": "dragonsurvival:textures/gui/custom/altar/cave/altar_icon.png",
    "ability_bar": "dragonsurvival:textures/gui/custom/casting_bars/cave/cast_bar.png",
    "growth_crystal": {
      "empty": "dragonsurvival:textures/gui/custom/stage/cave/point_main.png",
      "full": "dragonsurvival:textures/gui/custom/stage/cave/point_hover.png"
    },
    "food_tooltip": {
      "nutrition_icon": "\uEA02",
      "saturation_icon": "\uEA05"
    },
    "primary_color": "#88ccff",
    "secondary_color": "#225588"
  }
}
```

还需给新龙种配置 `stage_resources`，并检查祭坛排序标签、体型可见性和技能门槛。`bodies` 列出某体型不代表自动获得该体型所有贴图。

源码：`registry/dragon/DragonSpecies.java`，`common/codecs/UnlockableBehavior.java`、`ManaHandling.java`、`MiscResources.java`，`common/handlers/magic/ManaHandler.java`；源码链接基址见[总目录](README.md#核对来源与验证范围)。
