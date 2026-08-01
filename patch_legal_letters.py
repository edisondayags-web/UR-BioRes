letter_type_path = "app/src/main/java/com/saltech/urdocs/model/LetterType.kt"
repo_path = "app/src/main/java/com/saltech/urdocs/data/GeminiRepository.kt"
ui_path = "app/src/main/java/com/saltech/urdocs/ui/screens/LettersScreen.kt"

def find_line_containing(lines, needle):
    for i, line in enumerate(lines):
        if needle in line:
            return i
    return -1

def insert_before(path, needle, new_lines_text):
    """Insert new_lines_text (already newline-terminated) before the line
    containing `needle`, matching that line's indentation."""
    with open(path) as f:
        lines = f.readlines()
    idx = find_line_containing(lines, needle)
    if idx == -1:
        print(f"ABORT: anchor not found in {path}: {needle!r}")
        return False
    indent = lines[idx][:len(lines[idx]) - len(lines[idx].lstrip())]
    block = "".join(indent + l + "\n" if l else "\n" for l in new_lines_text.split("\n") if True)
    # simpler: just prefix indent to each non-empty content line already provided fully formed
    lines.insert(idx, new_lines_text)
    with open(path, "w") as f:
        f.writelines(lines)
    print(f"OK: inserted before line {idx+1} in {path}")
    return True

# ---------- 1. LetterType.kt enum ----------
new_enum_entries = (
    '    AFFIDAVIT_DISCREPANCY("Affidavit of Discrepancy"),\n'
    '    SPA("Special Power of Attorney"),\n'
    '    DEMAND_LETTER("Demand Letter (Simple/Personal)"),\n'
    '    AFFIDAVIT_DESISTANCE("Affidavit of Desistance"),\n'
    '    AFFIDAVIT_TWO_PERSONS("Affidavit of Two Disinterested Persons"),\n'
)
insert_before(letter_type_path, '    CUSTOM("Custom / Iba pa")', new_enum_entries)

# ---------- 2. GeminiRepository.kt configs ----------
new_configs = (
    '        LetterType.AFFIDAVIT_DISCREPANCY to LetterConfig(\n'
    '            "Affidavit of Discrepancy",\n'
    '            "buong pangalan ng nagsasalaysay, address, ang dalawang magkaibang detalye (hal. maling spelling ng pangalan o petsa ng kapanganakan) na lumalabas sa iba\'t ibang dokumento/ID, at pagpapatunay na iisang tao lang ang tinutukoy ng dalawang detalye -- gamitin ang tamang legal na tono ng isang sinumpaang salaysay"\n'
    '        ),\n'
    '        LetterType.SPA to LetterConfig(\n'
    '            "Special Power of Attorney",\n'
    '            "buong pangalan ng nagbibigay ng kapangyarihan (principal), buong pangalan ng kinatawan/ahente (attorney-in-fact), tiyak na gawain o transaksyon na ipinapahintulot (hal. mag-claim ng dokumento, mag-withdraw sa bangko, mag-representa sa gov\'t office), petsa o saklaw ng bisa -- gamitin ang pormal na legal na wika ng isang Special Power of Attorney"\n'
    '        ),\n'
    '        LetterType.DEMAND_LETTER to LetterConfig(\n'
    '            "Demand Letter (Simple/Personal)",\n'
    '            "buong pangalan ng humihingi, buong pangalan ng pinadadalhan, dahilan ng demand (hal. hindi nabayarang utang, hindi natupad na kasunduan), halaga o obligasyon, deadline para tumugon -- gamitin ang pormal, maigsi, at diretsahang tono; huwag magbanggit ng partikular na batas maliban kung nakalagay ang partikular na batayan"\n'
    '        ),\n'
    '        LetterType.AFFIDAVIT_DESISTANCE to LetterConfig(\n'
    '            "Affidavit of Desistance",\n'
    '            "buong pangalan ng nagsasalaysay, address, deskripsyon ng complaint o kaso na ninanais bawiin, dahilan ng pagbawi (hal. nakipag-areglo na, hindi na interesado ituloy) -- gamitin ang tamang legal na tono ng isang sinumpaang salaysay"\n'
    '        ),\n'
    '        LetterType.AFFIDAVIT_TWO_PERSONS to LetterConfig(\n'
    '            "Affidavit of Two Disinterested Persons",\n'
    '            "buong pangalan ng DALAWANG nagsasalaysay (hindi kamag-anak ng paksa), address nila, ano ang pinatutunayan nila (hal. tunay na tirahan o pagkakakilanlan ng isang tao), kaugnayan nila sa taong pinatutunayan (hal. kapitbahay) -- gamitin ang tamang legal na tono ng isang joint affidavit"\n'
    '        ),\n'
)
insert_before(repo_path, "        LetterType.CUSTOM to LetterConfig(", new_configs)

# ---------- 3. LettersScreen.kt icon mapping ----------
new_icons = (
    "                    LetterType.AFFIDAVIT_DISCREPANCY -> Icons.Filled.Rule to Icons.Filled.Description\n"
    "                    LetterType.SPA -> Icons.Filled.Gavel to Icons.Filled.Description\n"
    "                    LetterType.DEMAND_LETTER -> Icons.Filled.Warning to Icons.Filled.Description\n"
    "                    LetterType.AFFIDAVIT_DESISTANCE -> Icons.Filled.RemoveCircle to Icons.Filled.Description\n"
    "                    LetterType.AFFIDAVIT_TWO_PERSONS -> Icons.Filled.Groups to Icons.Filled.Description\n"
)
insert_before(ui_path, "LetterType.CUSTOM -> Icons.Filled.Edit to Icons.Filled.Description", new_icons)

# ---------- 4. LettersScreen.kt description mapping ----------
new_descs = (
    '            LetterType.AFFIDAVIT_DISCREPANCY -> "Para sa magkaibang detalye sa pangalan o birthdate sa iyong mga dokumento"\n'
    '            LetterType.SPA -> "Bigyan ng kapangyarihan ang iba na mag-transact para sa\'yo"\n'
    '            LetterType.DEMAND_LETTER -> "Pormal na paghingi ng bayad o pagtupad sa obligasyon"\n'
    '            LetterType.AFFIDAVIT_DESISTANCE -> "Pagbawi sa isang complaint o kaso"\n'
    '            LetterType.AFFIDAVIT_TWO_PERSONS -> "Patunay mula sa dalawang testigo na hindi kamag-anak"\n'
)
insert_before(ui_path, 'LetterType.CUSTOM -> "Gumawa ng sarili mong klase ng letter"', new_descs)

print("\nDONE. Review the OK/ABORT lines above.")
