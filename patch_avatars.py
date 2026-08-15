import re, glob

files = sorted(glob.glob("app/src/main/java/com/saltech/urdocs/ui/templates/ResumeTemplate*_PixelPerfect.kt"))
results = {"patched": [], "skipped": []}

def find_matching_brace(s, open_idx):
    depth = 1
    i = open_idx + 1
    while depth > 0:
        if s[i] == "{": depth += 1
        elif s[i] == "}": depth -= 1
        i += 1
    return i

for path in files:
    s = open(path).read()
    original = s

    # skip Template01/02 na tapos na
    if "SharedAvatarPicker(" in s:
        results["skipped"].append(path.split("/")[-1] + " (already patched)")
        continue

    marker = s.find("// PHOTO CIRCLE")
    if marker == -1:
        results["skipped"].append(path.split("/")[-1] + " (no marker)")
        continue

    box_start = s.find("Box(", marker)
    if box_start == -1:
        results["skipped"].append(path.split("/")[-1] + " (Box not found)")
        continue

    brace_open = s.find("{", box_start)
    block = s[box_start:brace_open]
    size_m = re.search(r'size\((\d+)\.dp\)', block)
    size = size_m.group(1) if size_m else "96"

    block_end = find_matching_brace(s, brace_open)

    replacement = f'SharedAvatarPicker(avatarUri, {size}.dp, accent, userName) {{ onFieldChange("avatarUri", it) }}'
    s = s[:box_start] + replacement + s[block_end:]

    # tiyakin may avatarUri param sa function signature
    if "avatarUri: String" not in s:
        sig_m = re.search(r'userName:\s*String\s*=\s*""', s)
        if sig_m:
            s = s[:sig_m.end()] + ',\n    avatarUri: String = "",\n    onFieldChange: (String, String) -> Unit = { _, _ -> }' + s[sig_m.end():]

    if s != original:
        open(path, "w").write(s)
        results["patched"].append(path.split("/")[-1])
    else:
        results["skipped"].append(path.split("/")[-1] + " (no change)")

print("=== PATCHED (" + str(len(results["patched"])) + ") ===")
for f in results["patched"]: print(f)
print("\n=== SKIPPED (" + str(len(results["skipped"])) + ") ===")
for f in results["skipped"]: print(f)
