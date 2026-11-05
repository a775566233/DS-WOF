# 原型资源与 Java 示例

## 1. 默认模型原型

`prototype-resources/` 是一个 `dswof:sky_dragon` 龙种的资源根目录，可按目录合并到项目的 `src/main/resources/`。它不会自动生效；已有同路径 JSON 或语言文件应合并条目。

原型包括：

- 一个龙种和只供这个龙种使用的 `dswof:sky_body`。
- 默认模型、默认动画、默认表情以及已有的皮肤和界面资源 ID。
- 幼年、青年、成年和可选远古阶段资源；它没有强制启用远古阶段。
- 一个简单肉食食谱、中英文显示名以及排序标签。

没有提供独立几何或自有 PNG，也没有专属技能、惩罚和解锁条件。外观借助已安装的 DS 资源用于联调，正式版本应按自己的设定完善。

关键文件：

- [龙种](prototype-resources/data/dswof/dragonsurvival/dragon_species/sky_dragon.json)
- [体型](prototype-resources/data/dswof/dragonsurvival/dragon_body/sky_body.json)
- [阶段资源](prototype-resources/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/stage_resources.json)
- [食谱](prototype-resources/data/dragonsurvival/data_maps/dragonsurvival/dragon_species/diet_entries.json)
- [中文翻译](prototype-resources/assets/dswof/lang/zh_cn.json)

接入后重新进入世界，用祭坛选择天空龙。默认 skin 可能被玩家已有自定义皮肤设置覆盖，首次联调应选择默认皮肤。

## 2. Java 适配示例

[DragonSurvivalAccess.java](java/org/wgx/dswof/compat/dragonsurvival/DragonSurvivalAccess.java) 可以复制到相同的 `src/main/java/` 包路径，演示：

- 读取龙形态快照。
- 对已经获准的请求查询体型、验证种族兼容性、修改并同步。
- 设置目标成长值，或立即调整实际成长值。
- 调用当前版本的变回人流程。

这是业务调用适配层，不是完整的指令或网络 handler。调用方必须在服务端线程执行修改，并先完成权限、解锁、消耗、冷却和入口条件校验。示例没有接受任意客户端 NBT。

## 3. 检查结果

已完成以下检查：

- 8 个 JSON 文件全部通过语法检查，原型必需字段、阶段结构和翼骨引用通过静态核查。
- 所引用的 DS 贴图、geo、动画和默认表情资源都存在于当前 2.0.71 JAR。
- Java 示例使用 Java 21、NeoForge 21.1.256 的本机生成制品及当前 DS 依赖，已通过 `javac --release 21 -proc:none` 编译。
- 文档内部文件链接与固定提交的源码路径检查通过。

这些检查未在游戏内执行 CODEC 加载或状态操作；尚未启动客户端与专用服务器做运行验收。接入后请按 [开发指南验收表](../01-addon-development.md#8-验收与发布) 和 [模型联调步骤](../03-modeling-tutorial.md#第八步游戏内联调) 测试。
