# Dragon Survival 附属开发指南

## 1. 开发路线

建议采用“数据定义龙种，资源定义外观，Java 实现特殊行为”的结构。Dragon Survival 2.0.71 已经把龙种、体型、成长阶段和技能设计成数据包注册表，许多内容不需要写新实体，也不需要修改原模组。

| 想实现的内容 | 首选方式 | 什么时候加入 Java |
| --- | --- | --- |
| 新龙种、可选体型、成长阶段 | `data/` 下的注册表 JSON | 需要特殊变身条件或流程 |
| 默认皮肤、成长图标、界面颜色 | species JSON、data map 与 `assets/` | 特殊 GUI 或动态渲染 |
| 食谱、技能组合、属性与弱点 | JSON、标签、data map | 内置效果无法表达行为 |
| 新物品、方块、装备、交互 | NeoForge 注册与事件 | 本身属于 Java 开发 |
| 新模型、骨骼动画 | Blockbench + GeckoLib 资源 | 普通体型通常不必写新 renderer |
| 修改原模组内部处理 | 先检查事件和数据扩展点 | 最后才评估 Mixin |

纯数据包与资源包也可以先做试验。你已有 NeoForge 工程，把同样的 `data/` 和 `assets/` 装进附属 JAR 即可；Java 模组与数据驱动并不冲突。

以当前工程名称 WOF 为例，如果目标是多个不同种族，先挑一个特点明确的龙族做原型。本文用原创联调名 `dswof:sky_dragon`，它不代表已经完成某个作品中的正式设定。

## 2. 先完成一个能验证的最小版本

按以下顺序推进，每一步都保留可启动的版本：

1. **新龙种能选中。** 使用默认模型、默认动画和已有资源 ID，确认祭坛能显示自己的名称，进入龙形态后无缺资源报错。
2. **成长与资源完整。** 覆盖幼年、青年、成年，并处理启用远古阶段的情况；默认皮肤、发光图层、阶段图标都能找到。
3. **一项特色能力。** 先通过 JSON 组合内置触发器、目标选择和效果，验证法力、冷却、伤害和联机行为。
4. **自己的美术。** 先画贴图并保留骨架、UV；外形差异确实需要新几何时，再换独立模型并补齐动画。
5. **扩展为多个种族。** 把共享玩法抽成配置和服务，逐个添加食谱、技能、解锁条件与美术资源。

不要把“JSON 能解析”当作完成。每个阶段的验收都要包含真实玩家状态、成长切换和其他玩家观察到的表现。

## 3. 当前工程配置

工程已经使用 Java 21、NeoForge ModDevGradle，并在 `build.gradle` 引入：

```groovy
repositories {
    maven {
        url = "https://www.cursemaven.com"
        content { includeGroup "curse.maven" }
    }
}

dependencies {
    implementation "curse.maven:DS-420799:8973485"  // Dragon Survival 2.0.71
    implementation "curse.maven:GEO-388172:8893490" // GeckoLib 4.9.3
}
```

这是你当前 ModDevGradle 工程的依赖方式。更换 Forge、NeoForge 或 Minecraft 大版本时，不要直接照搬依赖配置和本文 API。

`src/main/templates/META-INF/neoforge.mods.toml` 已声明 Dragon Survival、GeckoLib 为必需依赖。发布时，应把版本范围限定到实际测试过的组合；`[2.0.71,)` 不表示后续所有 DS 版本都保证兼容，当前 Minecraft `[1.21.1,1.22)` 和 NeoForge `[21,)` 也比本文验证范围更宽。

开始实现前，需要处理两项工程问题：

- `gradle.properties` 的目标是 Minecraft `1.21.1`，但 Parchment 写成了 `1.21.11` 与 `2025.12.21-nightly-SNAPSHOT`。应改成适配 1.21.1 的映射；官方 DS 该分支使用 `1.21.1` / `2024.11.17`，可以作为选择依据。
- 当前检出的工程缺少 `gradlew`、`gradlew.bat` 和 `gradle-wrapper.jar`。只有 wrapper properties 不能运行 wrapper。可从匹配的官方 NeoForge MDK 补齐，或先通过 IDEA 的 Gradle 面板运行任务。

本文没有自动修改这些配置。补齐 wrapper 并确认映射后，常用任务为：

```powershell
.\gradlew.bat runClient
.\gradlew.bat runServer
.\gradlew.bat build
```

NeoForge 1.21.1 官方入口：[开发文档](https://docs.neoforged.net/docs/1.21.1/gettingstarted/)、[MDK 模板](https://github.com/NeoForgeMDKs/MDK-1.21.1-ModDevGradle)。

## 4. 工程组织

推荐把与 DS 实现类的接触集中在一个包中：

```text
src/main/java/org/wgx/dswof/
├── DsWof.java
├── registry/                  新物品、方块等注册
├── compat/dragonsurvival/      DS 状态读取、校验、同步适配
├── gameplay/                  自己的玩法服务与 NeoForge 事件
├── network/                   自己的 payload
└── client/                    客户端输入与渲染

src/main/resources/
├── data/dswof/dragonsurvival/
│   ├── dragon_species/sky_dragon.json
│   ├── dragon_body/sky_body.json
│   └── dragon_ability/...
├── data/dragonsurvival/data_maps/dragonsurvival/dragon_species/
│   ├── stage_resources.json
│   └── diet_entries.json
├── data/dragonsurvival/tags/dragonsurvival/dragon_species/order.json
└── assets/dswof/
    ├── geo/sky_dragon.geo.json
    ├── animations/sky_dragon.json
    ├── textures/dragon/sky_dragon/...
    └── lang/zh_cn.json
```

`dragon_species`、`dragon_body`、`dragon_stage`、`dragon_ability` 是**单数目录**。部分官方 Wiki 示意图仍写复数，不能用于当前版本。

新增注册表对象使用自己的命名空间。data map 的文件位置则由 DS 注册的 map ID 和 registry ID 决定，所以上面的 `stage_resources.json` 必须放在 `data/dragonsurvival/...`，在 `values` 中增加 `dswof:sky_dragon`。不要把文件整体搬进 `data/dswof/`，也不要替换全部原版 entries。

标签用 `"replace": false` 增量添加。新增体型通常设置 `is_default: false` 并在自己的 species `bodies` 中显式引用，避免让所有原版龙种获得它。

模组 JAR 的普通资源目录无需另加 `pack.mcmeta`。如果单独发布外部包，Minecraft 1.21.1 **数据包格式为 48，资源包格式为 34**，建议分别制作两个包。

## 5. 示例怎么接入

[示例目录](examples/README.md) 已提供默认模型原型。将其 `prototype-resources/` 内的 `data/`、`assets/` 按目录结构合并到 `src/main/resources/`；已有文件要合并自己的 entry，而不是覆盖它们。

该原型包括独立龙种、专用体型、肉食食谱、阶段资源与中英文名字。几何、动画和图片通过资源 ID 使用 DS 现有内容，便于先验证接入。它没有设计专属技能、弱点、解锁或平衡机制。

用祭坛选择新龙种。开发管理员也可参考游戏内 `/dragon` 命令自动补全；以当前命令树为准，不套用旧版教程中的命令参数。修改数据包注册表后，重新进入世界或重启服务器；只做客户端资源重载不能更新服务端龙种定义。

## 6. 技能开发思路

在 `data/dswof/dragonsurvival/dragon_ability/` 中定义技能，然后在 species 的 `abilities` 中列出其 ID，或引用自己的技能标签。

每项技能分成四个问题：

1. **怎么触发？** 被动、按键施放还是持续引导，对应 `activation`。
2. **作用于谁？** 自身、视线目标、范围内敌人等，对应目标选择。
3. **做什么？** 治疗、伤害、状态效果、粒子、函数等，组合已有动作。
4. **什么代价？** 法力、冷却、持续费用、升级与禁用条件。

设计一个龙族能力时，先列出行为规格，例如“短距离治疗自身，冷却固定，不能在非龙形态使用”，再选择 DS 内置触发器和效果。不要先写 Java tick 循环来模拟整个技能系统。

官方说明：[Dragon Ability](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/Dragon-Ability)、[Activation](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/Activation)、[ActionContainer](https://github.com/DragonSurvivalTeam/DragonSurvival/wiki/ActionContainer)。具体子对象字段仍需对照当前源码的 CODEC。JAR 内的 `test_*.json` 适合观察结构，其中某些图标和用途是测试内容，不能原样当作发行技能。原版森林息、火焰息和闪电息的动作拆解与教学配置见[原版龙技能实现参考](05-original-abilities.md)。

如果缺少需要的效果，当前实现有按 `MapCodec` 注册的激活、目标选择、效果类型。可以沿用它们的接口，注册自己的类型 ID，再由 JSON 使用；先阅读相应接口的 `REGISTRY_KEY`、`CODEC` 和内置实现，不要假定存在一个统一的 `DragonSurvivalAPI.registerAbility()`。

## 7. Java 与联机边界

通过 `DragonStateProvider.getData(player)` 取得状态，先判断 `isDragon()`，再读龙种、体型、阶段 ID。详见 [Java 接口参考](02-dragonsurvival-api.md#3-java-状态接口)。

客户端负责按键、界面和视觉反馈。改变龙种、体型、成长或能力的请求必须到服务端校验，再修改和同步。需要校验玩家身份、当前形态、可用体型、解锁条件、冷却与物品消耗，不能让客户端任意提交 DS 状态 NBT。

数据包注册表对象从当前玩家或世界 `registryAccess()` 解析。长久保存 `ResourceLocation` / `ResourceKey`；不要在模组启动时永久保存某个世界的 `Holder` 或注册表实例。

`setBody` 等 public 方法不是完整业务流程；有些修改不自动同步，需要调用上游当前版本的同步入口。把这些调用封装在适配层，升级 DS 时只需集中核查。

自己的种族进度、任务或特殊能量可以用自己的 NeoForge attachment。优先使用自己的数据模型，避免往 DS 的序列化 NBT 塞未约定字段。

## 8. 验收与发布

| 场景 | 验收内容 |
| --- | --- |
| 单人客户端 | 祭坛、变身、成长、食谱、技能、默认皮肤与界面 |
| 双客户端 | 另一玩家观察体型、翅膀、动画、技能和变回人 |
| 独立专用服务端 | 启动无客户端类加载错误，两人加入、施放、退出重进 |
| 状态迁移 | 死亡重生、跨维度、存档重载、阶段切换 |
| 模型交互 | 第一人称、左右持物、口持物、背包、骑乘、龙息 |
| 发布 JAR | 在干净实例中安装实际依赖，检查资源是否打包 |

官方特别强调：LAN 测试不能代替专用服务器测试。普通 JSON/资源变更无需刻意编写镜像实现的单元测试；真正的业务逻辑可测试权限、费用计算、目标选择与状态迁移。

发布说明中记录实际测试版本。附属自身的 MIT 许可证不等于获得 DS 模型、贴图和动画的重新分发许可；原型通过资源 ID 引用已安装的 DS，自己的发行资源应由自己制作或取得相应授权。
