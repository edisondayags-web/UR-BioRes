path = "app/src/main/java/com/saltech/urdocs/ui/screens/LettersScreen.kt"

with open(path) as f:
    lines = f.readlines()

def find_line_containing(lines, needle):
    for i, line in enumerate(lines):
        if needle in line:
            return i
    return -1

# ---------- Icon mapping: insert before the CUSTOM icon line ----------
icon_idx = find_line_containing(lines, "LetterType.CUSTOM -> Icons.Filled.Edit to Icons.Filled.Description")
if icon_idx == -1:
    print("ABORT: icon anchor line not found")
    raise SystemExit(1)

icon_line = lines[icon_idx]
indent = icon_line[:len(icon_line) - len(icon_line.lstrip())]
new_icon_line = f'{indent}LetterType.JOBSEEKER_OATH -> Icons.Filled.Badge to Icons.Filled.Description\n'
lines.insert(icon_idx, new_icon_line)

# ---------- Description mapping: insert before the CUSTOM description line ----------
# (search again since indices shifted by the insert above)
desc_idx = find_line_containing(lines, 'LetterType.CUSTOM -> "Gumawa ng sarili mong klase ng letter"')
if desc_idx == -1:
    print("ABORT: description anchor line not found")
    raise SystemExit(1)

desc_line = lines[desc_idx]
indent2 = desc_line[:len(desc_line) - len(desc_line.lstrip())]
new_desc_line = f'{indent2}LetterType.JOBSEEKER_OATH -> "Libreng oath para sa unang beses mag-a-apply ng trabaho (RA 11261)"\n'
lines.insert(desc_idx, new_desc_line)

with open(path, "w") as f:
    f.writelines(lines)

print("PATCHED:", path)
print("Icon entry inserted before line (now):", icon_idx + 1)
print("Description entry inserted before line (now):", desc_idx + 1)
