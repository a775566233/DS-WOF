# 开发验证与排错

核查日期：2026-11-07。基线为当前工程的 Minecraft 1.21.1、NeoForge 21.1.256、Dragon Survival 2.0.71、Java 21。本文记录本次检查的事实及后续验证方法；游戏内行为尚未在本次检查中实测。

## 1. 构建成功具体说明什么

在工程根目录执行：

```powershell
.\gradlew.bat --offline build
```

本次结果为 `BUILD SUCCESSFUL`，产物为 `build/libs/dswof-1.0-SNAPSHOT.jar`。归档包含 `META-INF/neoforge.mods.toml` 和 `dswof.mixins.json`。Java 与资源处理任务显示 `UP-TO-DATE`，表示 Gradle 复用了现有结果；`test NO-SOURCE` 表示没有测试源码，不能写成“自动测试全部通过”。

| 验证步骤 | 能确认什么 | 还需要确认什么 |
| --- | --- | --- |
| `compileJava` | Java 符号、类型与方法调用能编译 | Mixin 注入点在运行时是否匹配 |
| `processResources` / `build` | 资源复制、元数据生成和打包 | JSON 语法、CODEC 字段与注册 ID 是否有效 |
| JSON 语法检查 | 文件是合法 JSON | DS 是否接受结构、引用是否存在 |
| 启动并进入测试世界 | 相关资源被实际读取和解码 | 技能效果、数值和生命周期是否符合设计 |
| 专用服务器加客户端 | 服务端加载、联网与同步 | 多玩家交互及移动手感 |

`processResources` 通常把 JSON 当作普通文件复制。即使资源中有空 JSON，构建也可能成功。

## 2. 适合当前工程的调试顺序

1. 先写一句可观察的规则，例如“冰翼龙在炎热群系且没有站冰时，冰息倍率为 0.5”。列出变回人类、离开群系和站上冰后的预期结果。
2. 查找当前 DS JAR 的同类 JSON，再核对实现的 `CODEC`。字段是否必填、默认值和单位都以当前依赖为准。IDEA 可以直接打开依赖类查看反编译结果；反编译源码的局部变量名和控制流可能与作者源码不同。
3. 每次集中验证一项行为。修改 Java 后重新构建；修改数据后先检查 JSON，再重进测试世界。自定义动态注册表与已有技能实例的刷新不能一概视为 `/reload` 就能完成。
4. 使用 `/attribute @s dswof:ice_breath_multiplier get` 等命令观察属性，再验证伤害。先确认修饰是否存在，可以区分“条件没有触发”和“伤害公式没有使用倍率”。
5. 比较人类、冰翼龙、其他龙种；再测死亡、重新登录和跨维度，确认属性与状态能正确清理。
6. 使用第二名玩家与专用服务器复测。细雪碰撞和冰面移动涉及客户端预测，单人表现正常仍不足以证明联机正常。

临时给属性设置基础值时，记录原值并在测试后恢复。基础值不是 DS 技能修饰的值，手动修改后会影响后续判断。

## 3. 日志从哪里看

开发运行日志通常在 `run/logs/latest.log`，详细日志在 `run/logs/debug.log`。从首次错误向后读异常链，优先关注自己的资源 ID、文件路径和 `Caused by`，不要只看最后一行“加载失败”。

```powershell
rg -n -C 3 'dswof|ERROR|Caused by|Failed to parse|Unknown|Mixin|InvalidInjection' run/logs/latest.log
```

这条命令用于缩小范围，之后仍要阅读相关异常的上下文。`WARN` 或关键字匹配本身不能证明是附属造成的问题。

## 4. 现有实现值得延续的做法

- 龙种、技能、弱点和条件主要由数据定义，自定义冰息效果只接管内置效果无法表达的伤害计算。
- 冰雪适应用同步属性表达能力，冻结与移动逻辑集中从 `IceAdaptation` 查询，其他龙种以后也可复用。
- 冰息接触状态按施法者、目标、技能和维度区分，并提供超时清理、服务器停止清理；新增临时状态时也应明确归属和清理时机。

继续增加内容前，先确认资源能解析、引用存在、条件一致，再围绕一个特色技能完成游戏内验证，能减少多个未验证功能互相干扰。条件组合见 [站方块被动](13-standing-block-passives.md)，名称显示见 [属性翻译说明](12-attributes-and-ice-breath-multiplier.md#23-补充翻译)，这里不重复展开。

## 5. 技能定义与玩家技能实例

`dragon_ability` 注册表保存技能定义，龙种的 `abilities` 列出初始技能，而 `MagicData` 保存玩家各龙种的技能实例（等级、启用状态等）。修改龙种列表后，已有角色可能仍使用保存的实例集合；重启并重新进入世界能加载新定义，但不能保证把新增条目补进已有角色。

DS 2.0.71 提供以下管理员命令。示例中的 `dswof:ice_wings` 可替换成要检查的技能 ID；需以龙形态执行，并先确保新定义已被当前世界加载。

```mcfunction
/dragon-ability query @s dswof:ice_wings level
/dragon-ability add @s dswof:ice_wings
```

`query` 报角色没有该技能，表示注册表中有定义但角色缺少实例；若技能 ID 本身无法识别，应先检查资源加载。`add` 仅补加缺失技能，并同步给客户端；已存在时不覆盖实例。升级规则存在时，新增实例从 0 级开始尝试自动升级；无升级规则时以最大等级创建。补加后可再次查询 `level`，并查询 `is_enabled`、`is_applying_effects` 区分等级、启用和效果执行状态。

若变更初始化规则后要重建一个已有技能，先确认可以丢弃该技能当前等级和状态，再使用带完整技能 ID 的 `remove`，之后 `add`。例如在新定义已加载的世界中执行：

```mcfunction
/dragon-ability remove @s dswof:ice_wings
/dragon-ability add @s dswof:ice_wings
/dragon-ability query @s dswof:ice_wings level
```

这组命令只重建指定技能。`remove` 不带技能 ID 会清空角色技能，不能省略最后的 ID。

需要完整重建当前龙种技能时，可在备份测试存档中执行 `/dragon-ability refresh @s false`。其中 `false` 仅表示不额外清空关联 storage，**不表示保留技能等级**：`refresh` 会停用已有被动、清空当前龙种的实例，再按龙种定义重新创建，等级会按初始化和自动升级规则计算，快捷栏也会重新安排。只补一个技能时使用 `add` 即可。
