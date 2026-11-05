"""Index the actual DS dependency without extracting or redistributing its assets.

Run from the repository root:
    python docs/datapacks/generate_file_index.py build/research/ds.jar
"""

from __future__ import annotations

import argparse
from collections import Counter, defaultdict
import hashlib
import json
from pathlib import Path
import re
from zipfile import ZipFile


DOCS = {
    "dragon_species": ("龙种", "01-species.md"),
    "dragon_body": ("体型", "02-body-and-emotes.md"),
    "dragon_emote_set": ("表情集", "02-body-and-emotes.md"),
    "dragon_stage": ("成长阶段", "03-stages.md"),
    "dragon_ability": ("技能", "../06-dragon-ability-json-reference.md"),
    "dragon_penalty": ("弱点", "04-penalties.md"),
    "projectile_data": ("投射物", "05-projectiles.md"),
    "data_maps": ("Data Map", "06-data-maps.md"),
    "tags": ("标签", "07-tags-recipes-and-loot.md"),
    "damage_type": ("伤害类型", "07-tags-recipes-and-loot.md"),
    "recipe": ("配方", "07-tags-recipes-and-loot.md"),
    "loot_table": ("战利品表", "07-tags-recipes-and-loot.md"),
    "loot_modifiers": ("全局掉落", "07-tags-recipes-and-loot.md"),
    "advancement": ("进度", "08-advancements-enchantments-and-conditions.md"),
    "enchantment": ("附魔", "08-advancements-enchantments-and-conditions.md"),
    "worldgen": ("世界生成", "09-worldgen-and-built-in-packs.md"),
}

MAPS = {
    "body_icons": "体型图标",
    "diet_entries": "龙种饮食",
    "dragon_beacon_data": "龙种信标效果与支付",
    "end_platforms": "龙种末地平台",
    "stage_resources": "龙种各成长阶段贴图",
}


def clean(value: object) -> str:
    return re.sub(r"§.", "", str(value)).replace("|", "\\|").replace("\n", " ")


def short(values: list[str], limit: int = 5) -> str:
    return "、".join(clean(v) for v in values[:limit]) + (
        f" 等 {len(values)} 项" if len(values) > limit else ""
    )


def typed_values(value: object, key: str) -> list[str]:
    found: list[str] = []

    def visit(node: object) -> None:
        if isinstance(node, dict):
            if isinstance(node.get(key), str) and node[key] not in found:
                found.append(node[key])
            for child in node.values():
                visit(child)
        elif isinstance(node, list):
            for child in node:
                visit(child)

    visit(value)
    return found


def classify(path: str) -> tuple[str, str, str, str]:
    parts = path.split("/")
    pack = ""
    if parts[2] == "datapacks":
        pack = parts[3]
        parts = parts[4:]
        assert parts[0] == "data", path
    namespace, category = parts[1:3]
    tail = parts[3:]
    if category == "dragonsurvival":
        category, tail = tail[0], tail[1:]
    legacy = ""
    if category == "advancement_unused":
        legacy, category = "未加载遗留进度", "advancement"
    elif category == "unused":
        legacy, category, tail = "未加载遗留附魔", tail[0], tail[1:]
    assert category in DOCS, (path, category)
    entry = namespace + ":" + "/".join(tail).removesuffix(".json")
    group = f"内置包 / {pack}" if pack else (
        legacy if legacy else DOCS[category][0] + " / " + category
    )
    return category, entry, group, legacy


def make_summary(category: str, entry: str, obj: dict, lang: dict) -> str:
    namespace, name = entry.split(":", 1)
    local = lang.get(f"{category}.{namespace}.{name}", name)
    if category == "dragon_species":
        return f"{local}；技能={obj.get('abilities', '空')}；弱点={obj.get('penalties', '空')}"
    if category == "dragon_body":
        return f"{local}；模型={obj.get('model', '默认 dragon_model')}；动画={obj.get('animation', '使用对象分支/缺省')}"
    if category == "dragon_emote_set":
        return f"{local}；{len(obj.get('emotes', []))} 个动作；" + short(typed_values(obj, "animation_key"))
    if category == "dragon_stage":
        return f"{local}；成长区间={obj.get('growth_range')}；自然成长时长={obj.get('ticks_until_grown', '缺省')} tick"
    if category == "dragon_ability":
        effects = typed_values(obj, "effect_type")
        activation = obj.get("activation", {}).get("type", "缺省")
        # Activation has its own discriminator in this DS version.
        activation = obj.get("activation", {}).get("activation_type", activation)
        return f"{local}；激活={activation}；效果：" + (short(effects) if effects else "见 actions 配置")
    if category == "dragon_penalty":
        return f"{local}；效果={obj.get('effect', {}).get('penalty_type', '见原文件')}；触发={obj.get('trigger', {}).get('penalty_trigger', '缺省')}"
    if category == "projectile_data":
        effects = []
        for key in ["entity_effect", "block_effect", "world_effect"]:
            effects.extend(typed_values(obj, key))
        return f"{name}；{obj.get('projectile_type', obj.get('type', '见球体/箭矢分支'))}；命中/持续效果：" + short(effects)
    if category == "data_maps":
        return f"{MAPS.get(name.rsplit('/', 1)[-1], name)}；配置键：" + short(list(obj.get("values", {})))
    if category == "tags":
        values = [v.get("id", "") if isinstance(v, dict) else v for v in obj.get("values", [])]
        mode = "替换" if obj.get("replace", False) else "追加"
        return f"{name} 分组；{mode} {len(values)} 项：" + (short(values) or "空集合")
    if category == "damage_type":
        return f"{name} 伤害元数据；消息={obj.get('message_id')}；缩放={obj.get('scaling')}；exhaustion={obj.get('exhaustion')}"
    if category == "recipe":
        result = obj.get("result", {})
        result = result if isinstance(result, str) else str(result.get("id", result.get("item", ""))) + " ×" + str(result.get("count", 1))
        return f"{obj.get('type')}；产出 {result}"
    if category == "loot_table":
        items = []
        def visit(node: object) -> None:
            if isinstance(node, dict):
                if node.get("type") in ("minecraft:item", "item") and node.get("name") not in items:
                    items.append(node["name"])
                for value in node.values():
                    visit(value)
            elif isinstance(node, list):
                for value in node:
                    visit(value)
        visit(obj)
        return f"{name}；上下文={obj.get('type', 'minecraft:generic')}；产物：" + (short(items) or "无直接物品条目")
    if category == "loot_modifiers":
        if name == "global_loot_modifiers":
            return "登记全局掉落修饰器：" + short(obj.get("entries", []))
        return f"{obj.get('type')}；附加表={obj.get('table', '由类型决定')}；目标=" + short(obj.get("tables_to_apply", []))
    if category == "advancement":
        title = obj.get("display", {}).get("title", {})
        if isinstance(title, dict):
            title = lang.get(title.get("translate", ""), title.get("text", title.get("translate", "")))
        triggers = typed_values(obj.get("criteria", {}), "trigger")
        recipes = obj.get("rewards", {}).get("recipes", [])
        reward = "；解锁配方：" + short(recipes) if recipes else ""
        return f"{title or name}；触发：{short(triggers)}{reward}"
    if category == "enchantment":
        desc = obj.get("description", {})
        if isinstance(desc, dict):
            desc = lang.get(desc.get("translate", ""), desc.get("text", name))
        effects = list(obj.get("effects", {}))
        return f"{desc}；最高等级={obj.get('max_level')}；组件=" + (short(effects) or "空，Java 专用行为见参数文档")
    if category == "worldgen":
        subtype = name.split("/", 1)[0]
        if subtype == "structure":
            return f"拼图结构；群系={obj.get('biomes')}；起始池={obj.get('start_pool')}"
        if subtype == "structure_set":
            placement = obj.get("placement", {})
            note = "；注意：super_exclusion_zone 被原版类型忽略" if placement.get("type") == "minecraft:random_spread" and "super_exclusion_zone" in placement else ""
            structures = [v["structure"] for v in obj.get("structures", [])]
            return f"分布 {short(structures)}；spacing={placement.get('spacing')}、separation={placement.get('separation')}{note}"
        if subtype == "template_pool":
            locations = typed_values(obj, "location")
            return f"拼图候选池；{len(obj.get('elements', []))} 项；NBT：{short(locations)}"
        if subtype == "processor_list":
            outputs = typed_values(obj, "Name")
            return f"放置时替换方块；输出：{short(outputs)}"
    raise ValueError((category, entry))


def generate(jar: Path) -> str:
    with ZipFile(jar) as archive:
        paths = sorted(n for n in archive.namelist() if n.startswith("data/") and n.endswith(".json"))
        assert len(paths) == len(set(paths)), "Duplicate ZIP entry names"
        language = json.loads(archive.read("assets/dragonsurvival/lang/zh_cn.json"))
        groups: dict[str, list[str]] = defaultdict(list)
        totals: Counter = Counter()
        for path in paths:
            category, entry, group, legacy = classify(path)
            obj = json.loads(archive.read(path))
            label, document = DOCS[category]
            summary = make_summary(category, entry, obj, language)
            if legacy:
                summary = "**未加载**；" + summary
            totals[group] += 1
            row = f"| `{path}` | {clean(summary)} | [{label}参数]({document}) |"
            groups[group].append(row)
    digest = hashlib.sha256(jar.read_bytes()).hexdigest()
    lines = [
        "# Dragon Survival 数据包 JSON 逐文件索引", "",
        "[返回总目录](README.md)。基线为本工程实际依赖 Dragon Survival **2.0.71** JAR。", "",
        f"共 **{len(paths)} 个** `data/**/*.json`，包含所有命名空间、六个内置包的 28 个 JSON，以及 5 个未加载的遗留 JSON。每个完整路径只登记一次。", "",
        "每行给出具体文件的用途 / 当前配置摘要及完整参数手册入口。同格式文件共享字段说明；摘要不是完整配置，也不将文件名或翻译名称视为实际行为的保证。", "",
        "内置包中的路径需要启用相应包才参与加载，详见[启用策略与遗留说明](09-worldgen-and-built-in-packs.md)。", "",
        f"索引源 JAR SHA-256：`{digest}`。", "",
        "## 数量统计", "", "| 分组 | JSON 数 |", "| --- | ---: |",
    ]
    for group in sorted(totals):
        lines.append(f"| {group} | {totals[group]} |")
    lines.extend(["", "可在编辑器搜索完整文件名、ID 路径或中文译名。", ""])
    for group in sorted(groups):
        lines.extend([f"## {group}", "", "| JAR 内完整路径 | 作用与当前配置摘要 | 参数说明 |", "| --- | --- | --- |"])
        lines.extend(groups[group])
        lines.append("")
    lines.extend([
        "## 更新方式", "",
        "从工程根目录运行 `python docs/datapacks/generate_file_index.py build/research/ds.jar`。也可传入其他 DS JAR 路径；升级版本后必须同步重新核对参数手册，不能只更新索引。", "",
        "生成器只读取 ZIP 与中文语言表，写入本索引；不提取上游源码或美术，不会改动游戏资源。", "",
    ])
    return "\n".join(lines)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("jar", type=Path)
    args = parser.parse_args()
    output = Path(__file__).with_name("10-file-index.md")
    output.write_text(generate(args.jar), encoding="utf-8")
    print(f"Generated {output}")
