# dragon_body 与 dragon_emote_set：体型和表情

[返回总目录](README.md)。体型负责龙的模型、骨骼动画接入、尺寸比例和属性；表情集负责玩家主动播放的动作。模型制作过程见[Blockbench 教程](../03-modeling-tutorial.md)。

## dragon_body 路径和两种结构

路径：`data/<ns>/dragonsurvival/dragon_body/<id>.json`。CODEC 尝试“带模型体型”和“无独立模型体型”两种结构。不是通过 `type` 字段切换。

两者共用：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `is_default` | 布尔；可选；false | 是否作为默认可用体型参与选择 |
| `unlockable_behavior` | 对象；可选；无 | 与[龙种解锁对象](01-species.md#unlockable_behavior)相同 |
| `modifiers` | Modifier 数组；必填 | 体型属性修饰；空数组表示没有加成 |
| `default_icon` | 资源 ID；可选；无 | 没有龙种专用图标时的回退图标 |

带模型结构的其余参数：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `can_hide_wings` | 布尔；可选；true | 是否支持隐藏翅膀 |
| `model` | 资源 ID；可选；`dragonsurvival:dragon_model` | 几何模型资源 |
| `texture_size` | 对象；可选；512 × 512 | 两个必填整数 `width`、`height`，用于模型纹理尺寸 |
| `animation` | 资源 ID；必填 | 动画文件资源 |
| `bones_to_hide_for_toggle` | 字符串数组；可选 | 默认 `WingLeft`、`WingRight`、`SmallWingLeft`、`SmallWingRight`，隐藏翅膀时处理这些骨骼 |
| `emotes` | DragonEmoteSet ID；必填 | 引用表情集，不是在这里内嵌表情数组 |
| `scaling_proportions` | 对象；必填 | 碰撞箱、视点与模型比例，见下表 |
| `crouch_height_ratio` | double；必填；范围 0～100 | 蹲伏高度比例；常用 0～1 的比例，CODEC 允许范围更宽 |
| `backpack_offset` | 对象；可选；无 | 背包兼容渲染偏移 |
| `bettercombat_weapon_offset` | double；可选；0 | Better Combat 武器位置兼容偏移 |
| `rideable` | 布尔；可选；true | 该体型是否允许骑乘 |

无独立模型结构只读取上面的四个共用字段。**带模型的必填项写错或缺失，可能回退成无模型结构，而非整条报错。** 修改模型无效时要先排查 `animation`、`emotes`、`scaling_proportions`、`crouch_height_ratio`。

`scaling_proportions`：

| 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `width` | 非负 double；必填 | 碰撞箱宽度基准 |
| `height` | 非负 double；必填 | 碰撞箱高度基准 |
| `eye_height` | 非负 double；必填 | 视点高度基准 |
| `scale_multiplier` | 非负 double；可选；1 | 模型缩放倍率 |
| `shadow_multiplier` | 非负 double；可选；1 | 阴影尺寸倍率 |
| `ceiling_climbing_offset_multiplier` | double；可选；1；-10～10 | 天花板攀爬时模型偏移倍率 |

`backpack_offset` 三项都可选，使用 Vec3 数组 `[x,y,z]`：`position_offset` 默认 `[0,0,0]`、`rotation_offset` 默认 `[0,0,0]`、`scale` 默认 `[1,1,1]`。这是兼容渲染参数，不会移动实体碰撞箱。

Modifier 的每项必填 `attribute`（属性 ID）、`amount`（LV 或精确公式对象）、`operation`（`add_value` / `add_multiplied_base` / `add_multiplied_total`）。精确对象是 `{"precise_base":0,"precise_amount":0.1}`，按 `precise_base + precise_amount × level` 计算；体型一般以 1 作为该修饰器的输入。它不需要技能持续效果那层 `base` / `modifiers` 包装。

## dragon_emote_set

路径：`data/<ns>/dragonsurvival/dragon_emote_set/<id>.json`。顶层只有必填 `emotes` 数组；条目顺序决定表情列表顺序。

| `emotes[]` 字段 | 类型 / 必填 / 默认 | 作用 |
| --- | --- | --- |
| `animation_key` | 字符串；必填 | 体型动画逻辑中的动作 key |
| `translation_override` | 字符串；可选；无 | 自定义显示名称的翻译键 |
| `speed` | double；可选；1 | 动画播放倍率 |
| `duration` | 整数；可选；-1 | 指定表情持续 tick；-1 使用无指定时长模式 |
| `loops` | 布尔；可选；false | 是否循环动作 |
| `blend` | 布尔；可选；false | 是否作为混合动作播放 |
| `locks_head` | 布尔；可选；false | 锁定头部动作 |
| `locks_tail` | 布尔；可选；false | 锁定尾部动作 |
| `third_person` | 布尔；可选；false | 播放时使用第三人称展示 |
| `can_move` | 布尔；可选；false | 播放时是否允许移动 |
| `sound` | 对象；可选；无 | 表情声音 |

注意表情用 `locks_head`；技能动画对象用 `locks_neck`，字段不能互换。

`sound` 内必填 `sound_event`（已注册 SoundEvent ID）与 `interval`（整数 tick 间隔），可选 `volume`（float，默认 1）、`pitch`（float，默认 1）。

```json
{
  "emotes": [
    {
      "animation_key": "sit",
      "loops": true,
      "can_move": false,
      "third_person": true
    },
    {
      "animation_key": "spell_charge",
      "blend": true,
      "duration": 40,
      "can_move": true
    }
  ]
}
```

以上文件定义表情，不负责真正实现 `sit` 的骨骼动作；体型动画资源仍须包含兼容动作。技能继续复用吐息动画时，应保留技能 `activation.animations` 与体型资源的对应关系。

源码：`registry/dragon/body/DragonBody.java`、`registry/dragon/body/emotes/DragonEmote.java`、`DragonEmoteSet.java`、`registry/dragon/AttributeModifierSupplier.java`。资源默认值与模型接入见[模型教程](../03-modeling-tutorial.md)。
