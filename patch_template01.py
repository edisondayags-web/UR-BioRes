import re

path = "app/src/main/java/com/saltech/urdocs/ui/templates/ResumeTemplate01_PixelPerfect.kt"
s = open(path).read()
original = s

def find_matching_brace(s, open_idx):
    depth = 1
    i = open_idx + 1
    while depth > 0:
        if s[i] == "{": depth += 1
        elif s[i] == "}": depth -= 1
        i += 1
    return i

anchor = s.find("userName.take(1)")
if anchor == -1:
    print("Anchor not found")
else:
    box_start = s.rfind("Box(", 0, anchor)
    brace_open = s.find("{", box_start)
    block = s[box_start:brace_open]
    size_m = re.search(r'size\((\d+)\.dp\)', block)
    size = size_m.group(1) if size_m else "96"
    block_end = find_matching_brace(s, brace_open)

    replacement = f'SharedAvatarPicker(avatarUri, {size}.dp, accent, userName) {{ onFieldChange("avatarUri", it) }}'
    s = s[:box_start] + replacement + s[block_end:]

    if "avatarUri: String" not in s:
        sig_m = re.search(r'userName:\s*String\s*=\s*""', s)
        if sig_m:
            s = s[:sig_m.end()] + ',\n    avatarUri: String = "",\n    onFieldChange: (String, String) -> Unit = { _, _ -> }' + s[sig_m.end():]

    open(path, "w").write(s)
    print("PATCHED Template01" if s != original else "NO CHANGE")
