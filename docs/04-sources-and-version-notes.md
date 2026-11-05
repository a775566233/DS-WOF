# 官方资料、源码入口与版本差异

整理日期：2026-10-07。本文针对当前工程，核对的是实际依赖 Dragon Survival 2.0.71，而非推测所有版本的统一 API。

## 1. 官方资料

| 资料 | 用途 |
| --- | --- |
| [Dragon Survival 仓库](https://github.com/DragonSurvivalTeam/DragonSurvival) | 模组官方代码与说明 |
| [1.21.1 分支](https://github.com/DragonSurvivalTeam/DragonSurvival/tree/1.21.1) | 对应平台的实现 |
| [官方 Wiki](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki) | 数据配置与游戏说明 |
| [1.21.1 附属教程（俄文）](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/RU-%E2%80%90-1.21.1-Create-your-own-Addons-for-Dragon-Survival) | 龙种、资源、模型和动画要求 |
| [DragonSpecies](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/DragonSpecies) | 龙种概念与 JSON |
| [DragonBody](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/DragonBody) | 体型与模型配置 |
| [DragonStage](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/DragonStage) | 成长阶段 |
| [Dragon Ability](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/Dragon-Ability) | 技能结构 |
| [DietEntry](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/DietEntry) | 食谱配置 |
| [3. Customization](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/3.-Customization) | 官方皮肤制作与 SkinMakerKit |
| [9. How Help or Make Content](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/9.-How-Help-or-Make-Content) | 内容制作与贡献说明，部分示例为旧版 |
| [官方附属工具页](https://dragonsurvivalteam.github.io/partners/) | 配置工具入口 |
| [Dragon Body 生成器](https://dragonsurvivalteam.github.io/dragonsurvival/dragon_body/) | 辅助生成体型配置，生成结果仍需核查 |
| [GeckoLib 的 Blockbench 教程](https://github.com/bernie-g/geckolib/wiki/Making-Your-Models-%28Blockbench%29) | 插件、骨骼组、工程与导出 |
| [Blockbench 建模指南](https://www.blockbench.net/wiki/guides/bedrock-modeling) | 方块模型、层次、UV 等基础；DS 最终使用 GeckoLib 导出 |
| [NeoForge 1.21.1 开发文档](https://docs.neoforged.net/docs/1.21.1/gettingstarted/) | 工程搭建、注册、事件、网络等 |

本文的中文模型步骤是依据这些资料整理的适配教程，不是官方发布的中文译文。Wiki 和工具会继续更新，若与已安装版本冲突，以该版本 CODEC、加载路径和实际测试为准。

## 2. 固定源码定位

本次读取的 1.21.1 分支提交为 **`6638c759c5eb224f7de72d9b10b7bec75f9fbafc`**，源码声明 `mod_version=2.0.71`；重要公开签名也通过本工程 JAR 核查。固定链接便于以后复查：

- [DragonStateProvider](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/capability/DragonStateProvider.java)：状态入口与 attachment。
- [DragonStateHandler](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/capability/DragonStateHandler.java)：状态读写、成长、序列化。
- [DragonSpecies](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/DragonSpecies.java)：龙种 CODEC。
- [DragonBody](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/body/DragonBody.java)：模型 / 无模型分支、体型合法性。
- [DragonStage](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/stage/DragonStage.java)：成长字段与验证。
- [DragonAbility](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/DragonAbility.java)：技能顶层 CODEC。
- [Activation](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/activation/Activation.java)：按 MapCodec 注册的激活类型。
- [MagicData](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/attachments/MagicData.java)：法力、施法和技能实例。
- [DSDataMaps](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/DSDataMaps.java)：data map 注册、同步与合并。
- [StageResources](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/codecs/StageResources.java)：默认皮肤、发光图层和图标。
- [MiscResources](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/codecs/MiscResources.java)：龙种 UI 资源必需字段。
- [DragonCommand](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/commands/DragonCommand.java)：官方变身与变回人流程。
- [PlayerLoginHandler](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/server/handlers/PlayerLoginHandler.java)：状态刷新与同步入口。
- [SyncComplete](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/network/syncing/SyncComplete.java)：全量龙状态刷新逻辑。
- [DragonModel](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/client/models/DragonModel.java)：模型 / 动画路径、默认贴图、在线皮肤限制。
- [DragonRenderer](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/client/render/entity/dragon/DragonRenderer.java)：第一人称隐藏、持物与挂点。

## 3. Wiki 与当前版本的重要差异

| Wiki / 旧示例写法 | 2.0.71 实际行为 |
| --- | --- |
| `dragon_bodies`、`dragon_stages`、`dragon_abilities` 目录 | 单数 `dragon_body`、`dragon_stage`、`dragon_ability` |
| species `starting_size` | `starting_growth` |
| species `misc_resources` 可选 | CODEC 必需 |
| species 顶层 `diet` / `modifiers` | 当前 species CODEC 无这些字段；分别通过 data map 与相关属性配置表达 |
| stage `size_range` | `growth_range` |
| stage `grow_into_requirements` | 当前 stage CODEC 未定义 |
| body 缩放 `offset` / `mounting_offset` | 当前 scaling CODEC 无这些字段，挂点与相关配置另行处理 |
| `MountingBone` 尚未使用 | 已用于骑乘，rideable 模型会校验该骨骼 |
| 旧版 `assets/.../emotes.json` | 当前为 `dragon_emote_set` 注册表 + body 引用 |
| 按通用 GeckoLib 教程使用 `.animation.json` | DS 龙体型 loader 当前补 `animations/` 和 `.json` |

“没有定义”不保证一定报错；某些 CODEC 会忽略未知字段，body 还可能退到无模型分支。发现修改无效时，对照字段，而不是只看 JSON 是否能被文本解析器读取。

## 4. 验证边界

- 已读取官方 Wiki 和对应分支源码，检查工程依赖 JAR 的版本、目录、模型骨骼、动画键和常用 Java 签名。
- 示例资源只使用原创配置和上游资源 ID，没有把上游几何、动画或图片复制到交付目录。
- 示例的静态检查与 Java 编译结果见 [示例说明](examples/README.md)。本文没有实际启动客户端或专用服务器，无法承诺游戏中的视觉与玩法已经验收。
- 官方源码构建使用的 NeoForge / GeckoLib 版本与本工程不完全相同，且 Wiki 会变化；教程基线是本工程 DS 2.0.71，而不是“任何最新版”。

上游许可证见 [LICENSE.txt](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/LICENSE.txt)。模组元数据声明自己的 MIT 不覆盖上游资源许可。
