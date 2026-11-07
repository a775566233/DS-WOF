# Dragon Survival WOF 附属开发资料

这是为当前 `DS-WOF` 工程整理的中文开发文档。推荐先通过 JSON 添加一个龙种和体型，复用 Dragon Survival 的默认模型完成联调，再制作自己的贴图、动画与独立模型；Java 用于新增物品、交互和数据配置无法表达的玩法。

资料核对基线：Minecraft **1.21.1**、NeoForge **21.1.256**、Java **21**、Dragon Survival **2.0.71**、GeckoLib **4.9.3**。接口名称同时核对了本工程实际依赖 JAR 与官方 1.21.1 分支源码。这不是覆盖所有版本的官方 SDK 文档。

## 阅读顺序

1. [附属开发指南](01-addon-development.md)：开发路线、工程配置、数据目录、技能与联机设计。
2. [Dragon Survival 接口参考](02-dragonsurvival-api.md)：JSON 字段、Java 状态入口、动态注册表和同步。
3. [模型设计与 Blockbench 教程](03-modeling-tutorial.md)：官方皮肤路线、独立模型、骨骼、动画、导出和接入。
4. [官方资料与版本差异](04-sources-and-version-notes.md)：官方教程链接、源码定位以及 Wiki 的过时字段。
5. [原版龙技能实现参考](05-original-abilities.md)：技能运行链路、森林/火焰/闪电龙息、JSON 结构及 Java 扩展点。
6. [dragon_ability JSON 参数手册](06-dragon-ability-json-reference.md)：技能字段的必填项、默认值、单位、全部内置效果参数，以及完整吐息示例。
7. [全部数据包 JSON 文档](datapacks/README.md)：龙种、体型、阶段、技能、弱点、投射物、Data Map，以及配方、掉落、进度、附魔和世界生成的字段说明。
8. [877 个 JSON 的逐文件索引](datapacks/10-file-index.md)：当前依赖 JAR 中每个数据 JSON 的完整路径、配置摘要和参数文档入口，包含内置包和遗留目录。
9. [可复制的原型资源与 Java 示例](examples/README.md)：一个 `dswof:sky_dragon` 龙种的联调样例。
10. [冰翼龙冻结免疫与冰息减伤](08-freezing-immunity.md)：免疫冻结积累和冻结伤害，同时保留冰息直接伤害的 50%。
11. [DS 状态效果：注册、施加与实现](09-status-effects.md)：`DSEffects`、`ModifiableMobEffect`、`PotionData`、Drain 执行流程，以及自定义冰冻状态与叠加的实现分工。
12. [技能等级、成长值与强度数值参考](10-ability-scaling-and-balance.md)：原版吐息分级伤害、经验门槛、成长范围公式、被动加成，以及当前冰息的数值对照。
13. [冰翼龙环境弱点接入](11-environment-penalties.md)：地狱掉血与禁用冰息、被动升级和炎热减伤、`in_fire` 与抗火，以及多种耐受的独立储备、专属属性和统一升级设计。
14. [属性注册与冰息专属倍率](12-attributes-and-ice-breath-multiplier.md)：`ice_breath_multiplier` 注册、玩家属性添加、弱点与表达式接入，以及 `RangedAttribute`、`PercentageAttribute`、`TimeAttribute` 参数和 DS 全部属性注册值。
15. [站方块被动与站冰回蓝](13-standing-block-passives.md)：炽焰魔法、洞穴健将的实际实现，`stepping_on` 条件、可升级冰面回蓝，以及站冰抵消地狱热量和炎热减伤的配置。
16. [自带天赋、冰雪适应与细雪行动](14-ice-adaptation-and-powder-snow.md)：右侧被动栏的分类，冻结免疫改为技能属性控制，细雪站立、潜行下潜与跳跃上浮，以及冰面防滑。
17. [开发验证与排错](15-development-validation.md)：构建与运行验证的区别、日志定位、现有炎热条件问题，以及适合当前工程的调试顺序。

附录：[当前默认模型骨骼](reference/dragon-bones.md)、[当前默认动画名称](reference/dragon-animation-names.md)。

示例放在 `examples`，不会自动进入游戏，也没有覆盖工程现有代码或美术。JSON 已做静态检查，Java 示例的编译结果见示例说明；游戏内表现仍需按教程实际验证。
