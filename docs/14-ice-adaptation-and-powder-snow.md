# 冰雪适应：自带天赋、细雪行动与冰面防滑

核对基线：Minecraft 1.21.1、NeoForge 21.1.256、Dragon Survival 2.0.71。本文对应当前工程已经接入的代码和资源；移动手感仍需客户端及多人游戏实测。

## 1. 把细雪加入 ice_blocks 后会怎样

`#dswof:ice_blocks` 是数据标签，只定义哪些方块属于这组。它既不会让细雪变成实心方块，也不会自动给予皮靴的行走能力。

原版 `entity_properties → predicate.stepping_on` 通过实体的 `getOnPos()` 检查支撑位置。站在细雪顶面、脚下确实匹配细雪时，可以触发站冰被动。但普通玩家会陷入细雪；进入后，支撑方块和脚下位置会变化，不能保证只靠这个条件持续触发。

当前 [on_ice.json](../src/main/resources/data/dswof/predicate/on_ice.json) 使用以下两种条件取其一：

```json
{
  "condition": "minecraft:any_of",
  "terms": [
    {
      "condition": "minecraft:entity_properties",
      "entity": "this",
      "predicate": {
        "stepping_on": {
          "block": { "blocks": "#dswof:ice_blocks" }
        }
      }
    },
    {
      "condition": "minecraft:entity_properties",
      "entity": "this",
      "predicate": {
        "location": {
            "block": { "blocks": "#dswof:ice_blocks" }
        }
      }
    }
  ]
}
```

第二项检查实体脚部位置所在方块，不是检查整个碰撞箱中任意一个接触方块。两个分支都引用同一标签，标签增删成员时，支撑位置与脚部位置采用同一套分类。普通雪层 `minecraft:snow`、完整雪块 `minecraft:snow_block` 与细雪 `minecraft:powder_snow` 是三种不同方块。

可升级的 [ice_magic.json](../src/main/resources/data/dswof/dragonsurvival/dragon_ability/ice_magic.json) 继续引用这个条件，提高自然回蓝。它还常驻提高法力上限，具体数值见 [站方块被动](13-standing-block-passives.md)。施法期间自然回蓝暂停的 DS 规则仍然适用。

### 1.1 雪层碰撞与位置判定

`minecraft:entity_properties` 的位置字段由原版 `EntityPredicate.LocationWrapper.CODEC` 解析：

| JSON 字段 | 对应 Java 调用 | 采样位置 |
| --- | --- | --- |
| `stepping_on` | `Entity.getOnPos()` | 优先使用实体的支撑方块；没有支撑记录时，取脚部 Y 减去极小偏移后的方块坐标 |
| `location` | `Entity.getX()`、`getY()`、`getZ()` | 实体脚部坐标所在格，Y 不是眼睛高度 |
| `movement_affected_by` | `Entity.getBlockPosBelowThatAffectsMyMovement()` | 与移动有关的脚下位置，内部采用约 0.5 格的 Y 偏移；不是检测薄雪层的通用替代方案 |

`SnowLayerBlock.getCollisionShape(...)` 使用 `SHAPE_BY_LAYER[layers - 1]`，碰撞高度为 `(layers - 1) / 8` 格；外观形状使用 `SHAPE_BY_LAYER[layers]`。因此单层雪的碰撞形状为空，玩家实际站在下面的支撑方块上，脚部坐标却可落在雪层所在格。2～8 层的碰撞高度仍比外观低 1/8 格，玩家脚部通常也位于雪层格内。

完整冰块、雪块上方的脚部格通常是空气，适合用 `stepping_on`；雪层或细雪中的脚部格则适合用 `location`。使用 `minecraft:any_of` 将两项连接为“支撑方块属于标签，或者脚部所在方块属于标签”。若把两项直接放在同一个 `predicate` 内，则是同时满足，不能表达这里的二选一。

这是一种位置规则，不是任意碰撞箱接触检测，也不要求 `on_ground`。进入细雪时仍会满足；离地后若支撑位置与脚部格都不再匹配，则停止满足条件。若设计要求必须落地，应另外组合 `flags.is_on_ground`，但这会改变细雪内的判定语义。

## 2. 原版最右侧是什么能力

也是 `dragon_ability`，龙种通过 `abilities` 列表引用。界面实际按升级类型分组：

| 配置方式 | 行为 | 被动栏分组 |
| --- | --- | --- |
| `upgrade_type: dragonsurvival:experience_points` | 手动花经验点学习和升级 | 可手动升级的被动栏 |
| 不写 `upgrade` | 默认最大等级 1，初始化即获得 | 最右侧的被动栏 |
| `experience_levels`、`dragon_growth`、`condition_based` 等 | 按经验等级、成长或条件自动推进，可能有解锁门槛 | 最右侧的被动栏 |

因此最右侧不代表“全部没有等级、全部出生就解锁”。例如原版 `fire_immunity` 没有 `upgrade`，而 `lava_swimming` 使用经验等级自动升级，翅膀使用条件解锁。

另一个字段 `can_be_manually_disabled` 决定玩家是否能手动关闭技能，与升级方式独立。本次冰雪适应设为 `false`，作为持续生效的生理特征。

## 3. 当前新增的自带被动

文件：[ice_adaptation.json](../src/main/resources/data/dswof/dragonsurvival/dragon_ability/ice_adaptation.json)。

它使用 `dragonsurvival:passive`，不写 `upgrade`，以 `dragonsurvival:self` 对自己施加原版 `dragonsurvival:modifier`。核心配置为：

```json
{
  "base": {
    "id": "dswof:ice_adaptation",
    "custom_icon": "dragonsurvival:textures/ability_effect/fire_immunity.png",
    "is_hidden": false,
    "should_remove_automatically": true
  },
  "modifiers": [
    {
      "attribute": "dswof:freezing_immunity",
      "amount": 1.0,
      "operation": "add_value"
    },
    {
      "attribute": "dswof:powder_snow_mobility",
      "amount": 1.0,
      "operation": "add_value"
    },
    {
      "attribute": "dswof:ice_grip",
      "amount": 1.0,
      "operation": "add_value"
    }
  ]
}
```

这是 `entity_effect[].modifiers[]` 中的一项；完整可加载结构请看上述生产文件。固定修饰 ID 防止每 tick 累加，自动移除配置交给 DS 管理技能效果的生命周期。当前通过 `is_hidden: false` 显示持续属性实例，`custom_icon` 暂用原版炽焰庇护素材；名称来自冰雪适应技能翻译。持续效果图标、技能菜单图标的路径与显示规则见[状态效果文档 8.2、8.3 节](09-status-effects.md#82-ds-属性实例modifiereffect的图标)。

[冰翼龙龙种](../src/main/resources/data/dswof/dragonsurvival/dragon_species/ice_wings_dragon.json) 的 `abilities` 已追加 `dswof:ice_adaptation`。图标暂用原版 `dragonsurvival:abilities/cave/cave_dragon_1`，可日后替换为自己的 GUI sprite。

三个属性在 [DsWofAttributes.java](../src/main/java/org/wgx/dswof/registry/dragonsurvival/DsWofAttributes.java) 注册并添加到玩家上：默认 0，范围 0～1，开启同步。技能将其加到 1，底层以大于 0 作为启用条件。

| 属性 | 作用 |
| --- | --- |
| `dswof:freezing_immunity` | 阻止原版冻结积累、冻结标签伤害，并进入已有的冰息免冻结目标减伤分支 |
| `dswof:powder_snow_mobility` | 细雪表面支撑、主动下潜、跳跃上浮、免除细雪黏滞 |
| `dswof:ice_grip` | 行走时将冰的摩擦系数视为普通地面 |

三个属性也可分到三个独立被动中。若希望其他龙种拥有其中某项，只需给它的技能授予相应属性；底层已经不再写死冰翼龙 ID。

## 4. 为什么改用 dragon_ability 后仍保留 Mixin

数据流为：龙种引用技能 → DS 被动授予属性 → 附属读取属性 → 执行冻结、碰撞或移动规则。

`dragon_ability` 负责是否拥有这些特性；Minecraft 的冻结值、细雪碰撞、冰面摩擦仍需要 Java 执行。现有 DS JSON 没有能一次完成这些动作的内置效果，因此本次复用 `modifier`，并让原有冻结 Mixin 改为读取属性。

[IceAdaptation.java](../src/main/java/org/wgx/dswof/dragonsurvival/IceAdaptation.java) 统一检查：实体为玩家、当前是龙形态、对应属性大于 0。切回人类，即使属性尚在同步过程中，也不会继续享有这些特性。

### 4.1 冻结免疫

原版冻结使用 `ticksFrozen`，不是名为“冻结”的原版 `MobEffect`。单独使用药水免疫类效果不能阻止该值变化。

- `LivingEntityMixin` 让持有特性的玩家 `canFreeze()` 返回 `false`。
- `EntityMixin` 将 `setTicksFrozen(...)` 写入值改为 0，拦住直接累积冻结值的技能。
- `IceWingFreezingImmunity` 取消 `#minecraft:is_freezing` 伤害，但放行 `dswof:ice_breath` 的直接伤害。冰息自己的免冻结目标倍率负责减半，避免重复处理。

详细规则见 [冻结免疫与冰息减伤](08-freezing-immunity.md)。其他模组自行定义的冰冻药水、禁锢等效果需要另行兼容。

### 4.2 细雪行动

[PowderSnowBlockMixin.java](../src/main/java/org/wgx/dswof/mixin/PowderSnowBlockMixin.java) 修改以下规则：

1. `canEntityWalkOnPowderSnow` 对持有特性的玩家返回 `true`，接入原版皮靴式的雪内上浮逻辑。
2. `getCollisionShape` 在实体位于方块上方且没有主动下潜时返回完整顶面支撑。高处落下也采用顶面支撑；这不授予摔落伤害免疫。
3. 只跳过 `entityInside` 中的 `makeStuckInBlock` 黏滞处理，保留细雪内标记、灭火等其他原版逻辑。

`LivingEntityMixin` 在雪内上浮判断处，使潜行优先于原版横向碰撞上推，便于贴墙下潜。

| 玩家操作 | 预期行为 |
| --- | --- |
| 站在细雪顶面，不潜行 | 保持站立，不自动陷入 |
| 按潜行 | 穿过细雪顶面、向下移动 |
| 在细雪中按跳跃，松开潜行 | 使用原版雪内上浮逻辑向上移动 |
| 在细雪内松开跳跃和潜行 | 仍受原版重力，不保证原地悬停 |

这里没有增加新按键或游泳动画。细雪是方块，不是 Fluid；不能通过填写 `fluid_type: minecraft:powder_snow` 来套用 DS 的岩浆游泳能力。

### 4.3 冰面防滑

[LivingEntityMixin.java](../src/main/java/org/wgx/dswof/mixin/LivingEntityMixin.java) 在 `travel` 读取脚下方块摩擦系数时，若方块属于 `#minecraft:ice` 且持有 `ice_grip`，返回普通地面的 `0.6F`。这同时影响行走加速和后续水平惯性；单独提高移动速度属性解决不了冰上打滑。

这里使用原版冰标签，不使用包含雪层、细雪的 `#dswof:ice_blocks`。自定义标签继续负责回蓝环境判定，原版冰标签负责哪些表面需要防滑。

## 5. 与热量弱点的关系

本次接入了回蓝的细雪内条件，以及冻结免疫、细雪行动、冰面防滑天赋。

冰雪适应本身不直接控制热量弱点。`hot_ice_breath` 已使用“炎热且不满足 `on_ice`”的条件，并在其取反时提前移除减伤；因此共享冰雪判定也控制这项减伤的豁免。`nether_heat` 是否采用相同豁免，由它自己的条件配置决定。条件组合方式见 [站方块被动第 3、4 节](13-standing-block-passives.md)。

## 6. 检查与游戏内验收

代码已通过 `compileJava`，资源已通过 `processResources`。本次服务端启动检查成功应用三个附属 Mixin，未出现注入失败；随后 GameTest 因工程没有测试函数而结束。该检查不等于移动行为或联机实测通过。

重启开发客户端测试。已有存档若未出现新被动，应检查技能列表刷新；不要假定只执行 `/reload` 就会更新已有玩家的全部技能实例，可在备份存档中重新选择龙种核验。

1. 在技能界面右侧确认“冰雪适应”已获得，不需要经验点且不能手动关闭。`ice_magic` 仍位于可手动升级的被动栏。
2. 用 `/attribute @s dswof:powder_snow_mobility get`、`/attribute @s dswof:ice_grip get`、`/attribute @s dswof:freezing_immunity get` 检查龙形态为 1。
3. 对比人类与冰翼龙在细雪顶面的站立、潜行下潜、雪内按跳跃上浮；测试多层细雪、侧面进入、贴墙、落入和顶部出口。
4. 比较冰、浮冰、蓝冰上的行走、停止和转向，确认普通方块上的移动不受影响。
5. 在细雪内、细雪表面和普通方块上比较 `dragonsurvival:mana_regeneration`；先学习 `ice_magic`，并停止施法后观察自然回蓝。
6. 按冻结文档验证冻结值归零、`minecraft:freeze` 免伤及冰息直接伤害仍保留。
7. 切回人类、其他龙种并重新登录，检查修饰移除；连接专用服务器复测移动和属性同步。

## 7. 源码依据

- DS [DragonAbilityScreen.java](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/client/gui/screens/DragonAbilityScreen.java)：`filterPassiveByUpgrade` 对手动经验点被动与其他被动分栏。
- DS [DragonAbility.java](https://github.com/DragonSurvivalTeam/DragonSurvival/blob/6638c759c5eb224f7de72d9b10b7bec75f9fbafc/src/main/java/by/dragonsurvivalteam/dragonsurvival/registry/dragon/ability/DragonAbility.java)：无升级配置时最大等级为 1；`MagicData` 初始化赋予该等级。
- 原版技能配置已核对实际依赖 JAR 的 `fire_immunity.json` 与 `lava_swimming.json`。
- Minecraft 逻辑核对当前 NeoForge 1.21.1 源码归档中的 `EntityPredicate`、`Entity`、`EntityCollisionContext`、`PowderSnowBlock`、`LivingEntity`，并核对相关调用字节码。
