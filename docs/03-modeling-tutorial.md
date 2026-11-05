# Dragon Survival 模型设计与 Blockbench 教程

## 1. 先选择制作路线

模型接入有三种工作量不同的路线：

| 路线 | 可以改变什么 | 适合情况 |
| --- | --- | --- |
| 官方皮肤制作 | 颜色、花纹、发光区；保留模型和 UV | 第一次制作内容，或只换龙族配色 |
| 默认模型 + 自己的体型动画 | 在同一骨架上改变姿势与动作 | 外形仍能由原版方块与骨架表达 |
| 独立 geo 模型 + 自己的动画 | 轮廓、几何、骨架、UV | 龙族有不同翼形、角、尾或四肢结构 |

官方附属教程说明：标准五种体型共享同一套模型，主要通过不同动画表达体型差异。它建议优先采用这种方式；独立模型需要补齐标准龙的动画，而且不支持官方在线自定义皮肤系统。

这里的“在线皮肤系统”与本地 PNG 默认皮肤不同。独立模型可以通过阶段资源配置自己的默认 PNG；当前 `DragonModel` 只有默认模型 ID `dragonsurvival:dragon_model` 才尝试读取在线 DragonSkins。即使两个模型几何相似，改成另一个 ID 也会绕开这条在线路径。

依据：[官方附属教程](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/RU-%E2%80%90-1.21.1-Create-your-own-Addons-for-Dragon-Survival)、[官方自定义皮肤教程](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/3.-Customization)、[GeckoLib Blockbench 教程](https://github.com/bernie-g/geckolib/wiki/Making-Your-Models-%28Blockbench%29)。下面将官方要求与当前 2.0.71 的实际加载方式结合；设计比例和练习数值是本文建议，不是官方强制标准。

## 2. 先画设计图

以天空龙原型为例，先确定三个能远距离识别的特征：较窄的身躯、明显的翼轮廓、较长的尾巴。先画侧面和正面黑色轮廓，再处理鳞片与花纹。

制作前写一张设计表：

| 项目 | 先确定的内容 |
| --- | --- |
| 身体轮廓 | 头、颈、胸、腹、尾的长度关系，站姿高度 |
| 关节 | 肩、肘、髋、膝、翼根与尾节的旋转中心 |
| 翅膀 | 收拢时不穿胸，展开时轮廓清晰，隐藏开关能控制 |
| 可交互位置 | 爪部持物点、口部持物点、背包点、骑乘点、龙息点 |
| 颜色 | 主色、腹部色、角爪色、少量强调色 |
| 年龄 | 幼年、青年、成年如何区分，哪些差异交给贴图和成长缩放 |
| 动画预算 | 行走、飞行、潜行、游泳、持物、施法和表情 |

低模 Minecraft 风格通常用少量方块构成大形体。先让纯色模型站立、走动、展开翅膀都清晰，再添加角、翼膜和装饰。不要依赖密集细碎方块补救轮廓。

## 3. 路线 A：按官方教程制作皮肤

这条路线最快，可以先给自己的龙种做默认皮肤。官方 Wiki 的要求是保持套件模型和 UV，只编辑已有贴图。

1. 安装 [Blockbench](https://www.blockbench.net/)。从官方 Wiki 给出的 [SkinMakerKit](https://drive.google.com/drive/folders/1e3cN8Z_obJbfEcr4HHD0Jl3hBrBC7BHC) 获取适合当前版本的套件，阅读其中说明。本文未下载或核验套件内的具体文件名。
2. 打开套件提供的工程 / 模型，切换到 Paint。编辑已有纹理，**不新建纹理，不修改 cubes，不移动 UV**；这是官方皮肤路线的要求。
3. 先铺身体、腹部、翅膀、角爪的基础色。通过套件现有 UV 找到每个部位，画完一块就转动模型确认位置。
4. 增加背腹过渡、眼睛、翼膜纹理和龙族标记。频繁检查左右两侧以及翅膀收拢处，避免接缝错位。
5. 分别制作幼年、青年、成年皮肤。可以先保持统一配色，通过眼睛比例感、花纹与颜色变化区分年龄；身体尺寸由成长系统处理。
6. 如果需要发光，单独制作发光图层 PNG；其余区域透明。无发光效果也准备一个同尺寸透明 PNG，以满足 `glow_skin` 配置。
7. 保存 PNG，再通过皮肤界面按 Wiki 当前说明导入，或作为附属的默认皮肤配置到 `stage_resources`。

官方社区皮肤教程建议贴图不超过 512×512，并提到 256 的使用情况。这个要求属于该皮肤工作流，不能泛化成所有自定义模型的引擎硬上限。

保留可编辑源文件，并用自己的文件命名记录版本。只有 PNG 的交付适合皮肤；要制作独立几何模型，进入下一条路线，不能同时遵守“完全不能修改 cubes”的皮肤限制。

## 4. 路线 B：新建独立 GeckoLib 模型

### 第一步：安装插件，选对工程类型

按 GeckoLib 官方教程，在 Blockbench 的 `File → Plugins → Available` 查找并安装 **GeckoLib Models & Animations**。插件名称在部分版本中可能显示为 GeckoLib Animation Utils。

使用 `File → New → GeckoLib Animated Model`，在项目设置中选择实体模型类型。已有工程可通过 `File → Convert Project → GeckoLib Animated Model` 转换。菜单文本随版本略有变化，以已安装插件为准。

不要导出 Java Entity `.java` 模型。DS 需要的是 GeckoLib 能加载的 `.geo.json`、动画 JSON 和 PNG。

### 第二步：建立骨架和旋转中心

所有 cubes 放入 group / bone；GeckoLib 动画主要作用于骨骼组。先完成层次，再把 cube 放进对应的骨骼，最后调整 pivot。

可以用下列结构理解一个新的龙骨架：

```text
Root
└── Torso
    ├── Neck
    │   └── Head
    │       ├── Jaw
    │       ├── BreathSource
    │       ├── RightItem_jaw
    │       └── LeftItem_jaw
    ├── WingLeft / WingRight
    ├── 前后肢与自己的关节层级
    │   └── RightItem / LeftItem（放在实际对应的爪部）
    ├── 尾巴分段
    ├── BackpackBone
    └── MountingBone
```

这是解释挂点位置的**原创简化设计**，不是可以替代原版骨架的兼容模板。当前默认模型有 272 个骨骼，`dragon_center` 动画引用 185 个骨骼；如果要直接复用原版动画，必须保留它依赖的骨骼名称和相关父子结构，包括各种 handler / blend / Molang 辅助骨骼。仅造几个同名挂点并不足以兼容。

官方教程要求保留以下名称，大小写要完全一致：

| 骨骼 | 用途与放置建议 |
| --- | --- |
| `Neck` | 第一人称隐藏颈头相关部分；检查头部是否在可隐藏层次中 |
| `BackpackBone` | 背包挂点，随躯干移动 |
| `RightItem` / `LeftItem` | 爪部左右持物位置 |
| `RightItem_jaw` / `LeftItem_jaw` | 口部左右持物位置 |
| `MountingBone` | 骑乘位置；当前 2.0.71 已实际使用 |

还应保留并测试 `BreathSource`，当前 renderer 用它定位龙息相关源点。默认模型还有 `MagicSource` 和大量动画辅助骨骼，参考 [完整骨骼清单](reference/dragon-bones.md)，按自己使用的动画和功能决定保留范围。

官方旧文说 `MountingBone` 还未使用，但当前源码已验证 `rideable: true` 的模型是否有该骨骼，并用它计算骑乘偏移。不要按旧文删除。

翼隐藏开关的骨骼名来自 body 的 `bones_to_hide_for_toggle`。使用原名时保留四个默认翼骨；完全自定义时也可以把该字段改成自己的翼根骨名，确保隐藏的是整组翼结构。

### 第三步：制作几何

依次做躯干、颈头、四肢、尾巴和翅膀。每完成一个关节就手动旋转测试：肩和髋旋转时是否像关节，尾巴转动是否从连接点开始，翼根是否错位。

持物、背包、龙息和骑乘点可以是没有 cube 的骨骼。它们是坐标锚点，应跟随正确的父骨，不要放在不随动作移动的根部。

模型的视觉大小与玩家碰撞箱是不同系统。`scaling_proportions` 控制相关尺寸，骨架、成长缩放和动作共同决定画面；不能只放大 mesh 就认为碰撞箱、眼高与骑乘位置已经合适。

### 第四步：展开 UV 并绘制贴图

独立模型可自行选择 UV。第一次建议使用方形 512×512 画布，保持 geo 工程纹理设置、PNG 和 body 的 `texture_size` 在这个工作流中一致，便于排查。

但不要把这个建议误用于默认模型：当前 DS 默认 geo 的纹理元数据是 256×256，默认 body 的 `texture_size` 却是 512×512。UV 归一化、皮肤合成画布和模型元数据不是同一个概念；复用原版时保持同版本模型 / 套件原有设置，不擅自改尺寸。

给 UV 岛留出间隙，检查翼膜边缘和尾节接缝。左右镜像可以节省面积，但不适合不对称花纹。绘制后在 3D 视图检查，而不是只看二维 PNG。

默认皮肤按阶段存放：

```text
assets/dswof/textures/dragon/sky_dragon/
├── newborn.png
├── newborn_glow.png
├── young.png
├── young_glow.png
├── adult.png
└── adult_glow.png
```

如果启用远古阶段，再补 `ancient` 资源。阶段资源映射是按龙种与阶段选择贴图，不是自动按 body 选择贴图；同一种族的多个体型使用不同 UV 时，要专门设计它们的皮肤兼容策略。

### 第五步：制作动画

在 Animate 面板创建动画。DS 当前使用的基础名称是 `idle`、`walk`、`run`、`fly` 等，**不是常见示例中的 `animation.dragon.idle`**。使用正确的名字才能被现有控制器和表情设置调用。

先练习这些动作，再补齐完整集合：

| 动作 | 制作建议 | 重点检查 |
| --- | --- | --- |
| `idle` | 轻微呼吸、颈部和尾巴摆动，循环 | 首尾无跳变 |
| `walk` / `run` | 对角足交替，速度不同 | 脚滑、关节穿插 |
| `fly` / `fly_idle` / `fly_soaring` | 拍翼、悬停与滑翔分别设计 | 翼根、翼膜和收翼过渡 |
| `sneak` / `sneak_walk` | 压低身体并重新调整头位 | 眼高与地面穿插 |
| `swim` / `swim_fast` | 四肢与尾巴配合 | 陆地与水中切换 |
| `use_item_right` / `use_item_left` | 左右物品操作 | 持物挂点位置 |
| `eat_item_right` / `eat_item_left` | 吃东西 | 物品是否进入正确位置 |
| `bite` / `breath` | 张口与攻击 / 龙息动作 | 龙息锚点和嘴部朝向 |

练习用的两秒轻微摆颈动画可以保存为以下结构：

```json
{
  "format_version": "1.8.0",
  "animations": {
    "idle": {
      "loop": true,
      "animation_length": 2.0,
      "bones": {
        "Neck": {
          "rotation": {
            "0.0": [0, 0, 0],
            "1.0": [2, 0, 0],
            "2.0": [0, 0, 0]
          }
        }
      }
    }
  }
}
```

这只是动画格式练习，不是可发行的完整 DS 动画集。控制器还会叠加转头、转尾、表情与施法相关动画；不要把它们全部误做成全身循环。

官方要求独立模型补齐标准龙动画。当前 `dragon_center.json` 有 **104** 个 animation key，逐项列在 [动画名称清单](reference/dragon-animation-names.md)。包括 `head_turn`、`tail_turn`、`blend_*`、`statue_pose_*`、持物、施法与特殊运动等。仅做 idle / walk / fly 适合原型练习，进入完整功能测试前必须补齐，并检查循环 / 单次 / 持续姿势行为。

表情定义当前位于 `dragon_emote_set` 注册表，并由 body 的 `emotes` 引用。旧教程中的单个 `assets/.../emotes.json` 不是当前版本的接入路径。

### 第六步：保存与导出

始终保存 Blockbench `.bbmodel` 源工程，方便后续改几何、UV 和关键帧。再分别导出：

| 输出 | 保存位置 | 配置对应 |
| --- | --- | --- |
| GeckoLib Model | `assets/dswof/geo/sky_dragon.geo.json` | body `model: dswof:sky_dragon` |
| 动画 JSON | `assets/dswof/animations/sky_dragon.json` | body `animation: dswof:sky_dragon` |
| 纹理 PNG | `assets/dswof/textures/dragon/sky_dragon/...png` | 阶段资源中的完整 texture ID |

模型使用 `File → Export → GeckoLib Model`。动画从动画导出入口导出，插件版本不同菜单位置可能不同；导出后检查 JSON 含顶层 `animations`。纹理在纹理面板 Save As。

DS 的龙体型动画在当前版本按 `animations/<逻辑ID>.json` 查找。插件即便导出文件名带 `.animation.json`，也要按上表改成 loader 实际需要的名字。

### 第七步：连接 body 与阶段资源

先按 [原型示例](examples/README.md) 验证默认模型的龙种。自己的 geo、完整动画与贴图准备好后，修改专用 body 的这几项：

```json
"model": "dswof:sky_dragon",
"animation": "dswof:sky_dragon",
"texture_size": {"width": 512, "height": 512}
```

保留 body 必需的 `modifiers`、`emotes`、`scaling_proportions` 和 `crouch_height_ratio`。默认表情集合仍可能请求那些表情动画，因此模型换了不代表可以省掉它们。

再将 `stage_resources` 中各阶段的 `skin` / `glow_skin` 改成自己的 PNG ID。保留每个阶段的 `growth_icon`；图标可以暂时引用 DS 资源。确保玩家选择默认皮肤进行首次测试，避免已有在线皮肤或编辑器配置遮住你的默认 PNG。

多个体型的背包姿势可通过 body 的 `backpack_offset` 微调。先把挂点放对，再调偏移；用很大的偏移掩盖错误骨架会使动画中物品位置不稳定。

### 第八步：游戏内联调

重新进入测试世界，选择自己的龙种与体型，依次检查：

1. 默认贴图和发光图层，幼年到成年变化。
2. idle、行走、跑步、潜行、跳落、游泳与飞行。
3. 第一人称是否被头颈遮挡，第三人称是否缺部位。
4. 左右持物、口持物、吃东西和武器动作。
5. 背包与骑乘，动作切换时位置是否漂移。
6. 龙息与施法，发射点是否在嘴部，方向是否正确。
7. 表情、睡眠、姿势和叠加转头动画。
8. 两个客户端互相观察，以及独立服务端上的状态同步。

客户端资源重载可以辅助修改 PNG、模型和动画，但不代替服务端注册表重载。出现缓存或状态影响时，重新进入世界并检查日志。

## 5. 常见故障

| 表现 | 优先检查 |
| --- | --- |
| 紫黑贴图 | 完整 texture ID、大小写、文件是否被打包、阶段是否有资源 |
| 模型不见或回到人形 | body 的有模型分支必需字段，是否误走 `NO_MODEL_CODEC` |
| 模型资源报错 | model 是否误写了 `geo/` 或 `.geo.json` 后缀 |
| 有模型但不动 | animation ID 与文件名、animation key、骨骼名和层次 |
| 翅膀开关无效 | `can_hide_wings` 与 `bones_to_hide_for_toggle` |
| 第一人称被头挡住 | `Neck` 名称及其可隐藏后代层次 |
| 物品 / 背包位置错 | 持物骨骼、`BackpackBone` 的 pivot 与父骨 |
| 骑乘悬空或报错 | `rideable`、`MountingBone` 与骑乘挂点位置 |
| 龙息从脚下出现 | `BreathSource` 及其跟随关系 |
| 独立模型没有在线皮肤 | 当前版本的既定限制，使用阶段默认贴图 |
| 编辑器皮肤遮住新默认贴图 | 玩家是否仍选自定义皮肤，当前阶段配置是否正确 |

独立模型的交付至少包含 `.bbmodel` 源工程、geo、完整动画、各阶段 PNG、body 和阶段资源配置。仅有漂亮的静态模型还没有完成 DS 玩家模型的接入。
