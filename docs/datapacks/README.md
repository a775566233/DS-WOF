# Dragon Survival 数据包 JSON 文档总览

基线：**Minecraft 1.21.1、NeoForge 21.1.256、Dragon Survival 2.0.71**。本文档集按本工程实际依赖 JAR 和上游提交 `6638c759c5eb224f7de72d9b10b7bec75f9fbafc` 整理。不同版本的字段不可直接混用。

本版本 JAR 的 `data/` 下共 **877 个 JSON**。文档按类型解释字段，每个文件另在索引列出具体用途和当前配置摘要。

从这里查文件的作用和参数：

| 数据类型 | 参数文档 | 主要作用 |
| --- | --- | --- |
| `dragon_species` | [龙种](01-species.md) | 初始成长、体型、技能、弱点、法力及界面资源 |
| `dragon_body`、`dragon_emote_set` | [体型与表情](02-body-and-emotes.md) | 模型、动画、比例、属性、表情动作 |
| `dragon_stage` | [成长阶段](03-stages.md) | 成长区间、时长、成长道具、阶段属性与踩踏 |
| `dragon_ability` | [技能参数手册](../06-dragon-ability-json-reference.md) | 激活、耗蓝、升级、目标、实体与方块效果 |
| `dragon_penalty` | [弱点与惩罚](04-penalties.md) | 环境弱点、抗性储备、伤害和装备限制 |
| `projectile_data` | [投射物](05-projectiles.md) | 球体和箭矢的外观、运动、命中与持续效果 |
| 5 类 `data_maps` | [Data Map](06-data-maps.md) | 饮食、阶段皮肤、体型图标、信标、末地平台 |
| `tags`、`damage_type`、`recipe`、`loot_table`、`loot_modifiers` | [标签、合成与掉落](07-tags-recipes-and-loot.md) | 分组、伤害分类、配方、战利品与全局追加掉落 |
| `advancement`、`enchantment`、DS 条件 | [进度、附魔与条件](08-advancements-enchantments-and-conditions.md) | 成就目标、附魔规则、龙状态检测与加载条件 |
| `worldgen`、内置可选数据包 | [世界生成与内置包](09-worldgen-and-built-in-packs.md) | 结构、拼图池、生成间距、古龙阶段和兼容包 |
| 当前 JAR 的每一个 `data/**/*.json` | [逐文件索引](10-file-index.md) | 精确路径、用途、数据类型及参数文档入口 |

这里的“数据包 JSON”包括 JAR 的 `data/` 下所有命名空间、内置可选包及未启用的遗留目录。`assets/` 属于客户端资源包；其模型和动画教程见[模型文档](../03-modeling-tutorial.md)。Java 注册表类型本身不是独立的 JSON 目录，例如 `activation`、`penalty_effect` 是嵌套对象的类型分派注册表。

## 文件路径和命名空间

自定义龙数据的路径是：

```text
data/<你的命名空间>/dragonsurvival/<数据类型>/<文件名>.json
```

例如 `data/dswof/dragonsurvival/dragon_species/sky_dragon.json` 定义 `dswof:sky_dragon`。路径中第二个 `dragonsurvival` 是注册表命名空间，不能改成 `dswof`。子目录会成为 ID 路径的一部分。

普通 Minecraft 数据则是 `data/dswof/recipe/...json`、`data/dswof/damage_type/...json` 等。1.21.1 使用单数 `recipe`、`loot_table`、`advancement`、`function`、`structure`；自定义注册表标签路径保留 `tags/dragonsurvival/dragon_species/` 这样的两层注册表名。

**Data Map 有固定路径。** 见对应手册；不能把五种 map 当成五种可任意命名的注册表条目。

## 通用类型和阅读约定

| 写法 | 含义 |
| --- | --- |
| 必填 | CODEC 解码要求提供 |
| 可选；默认 X | 省略采用 X；“无”代表未配置 |
| ID | `命名空间:路径`，写完整命名空间 |
| 注册表集合 | 单个 ID、ID 数组或 `#命名空间:标签`；标签必须属于正确注册表 |
| LV | Minecraft `LevelBasedValue`；常数或等级公式，完整字段见[技能手册第 2 节](../06-dragon-ability-json-reference.md#2-通用数值与单位) |
| loot condition | 带 `condition` 的 Minecraft 条件对象，如 `minecraft:entity_properties` |
| EntityPredicate | 实体谓词，不带 loot condition 的外层 `condition` |
| 世界生成 BlockPredicate | 带 `type` 的方块谓词，如 `minecraft:matching_blocks`；不是进度里的方块谓词；[参数](08-advancements-enchantments-and-conditions.md#世界生成-blockpredicate) |
| tick | 正常 20 tick = 1 秒；一天 24000 tick |
| ItemStack | 例如 `{"id":"minecraft:stone","count":1}`，可含物品组件 |
| ParticleOptions | 例如 `{"type":"minecraft:flame"}`，额外参数由粒子类型决定；[DS 粒子参数](08-advancements-enchantments-and-conditions.md#particleoptionsds-粒子选项) |

对象层级属于契约的一部分，同名字段放错位置不会生效。未知字段可能被忽略；JSON 语法检查不能替代 CODEC 解码。表格中的默认值以源码为准，附带的原版文件只是某种配置选择。

## 修改和加载

新增条目用自己的命名空间；覆盖原版条目用相同 ID 和相同路径。注册表定义一般是整条覆盖，不会按字段继承原版。标签和 Data Map 有专门的合并规则，见各篇。

独立数据包根目录需要 `pack.mcmeta`：

```json
{
  "pack": {
    "pack_format": 48,
    "description": "DS WOF 数据包"
  }
}
```

附属模组 `src/main/resources/data/` 中的数据由模组资源自动提供。模型、贴图和声音仍要放到 `assets/` 并分发给客户端。动态注册表修改应退出世界后重新进入，必要时重启客户端和服务端；不要假定 `/reload` 能重建所有已存在的玩家状态和注册表引用。

新龙种的最小串联顺序：先定义 body、species，再补齐三阶段皮肤与图标 Data Map，最后接技能、饮食和弱点。仅创建能力或弱点 JSON 不会自动挂到龙种上。

## 核对来源与验证范围

主要源码入口：[注册表目录](https://github.com/DragonSurvivalTeam/DragonSurvival/tree/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry)、[共用 CODEC](https://github.com/DragonSurvivalTeam/DragonSurvival/tree/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/common/codecs)、[NeoForge Data Map 文档](https://docs.neoforged.net/docs/1.21.1/datamaps/)。各篇列出对应类名，逐文件索引以实际 JAR 为准。

文档示例用于解释结构，已做静态检查；没有把全部示例安装到游戏并逐项验证。Minecraft 通用类型与其他模组注册的扩展类型还可继续增加字段，不能把本文当作所有模组的通用 JSON 规范。
