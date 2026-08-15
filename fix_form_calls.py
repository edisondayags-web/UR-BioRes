import re

FORM_FILE = "app/src/main/java/com/saltech/urdocs/ui/screens/ResumeTemplateFormScreen.kt"
s = open(FORM_FILE).read()

def find_matching_brace(s, open_idx):
    depth = 1
    i = open_idx + 1
    while depth > 0:
        if s[i] == "{": depth += 1
        elif s[i] == "}": depth -= 1
        i += 1
    return i

# Extract master block (Template02Screen function)
m = re.search(r'@Composable\nfun ResumeTemplate02Screen\(', s)
func_start = m.start()
brace_open = s.find("{", m.end())
brace_end = find_matching_brace(s, brace_open)
master_block = s[func_start:brace_end]

# For each other template number, replace its Screen function with master block (renamed)
nums = [f"{i:02d}" for i in range(1, 24) if i != 2]
count = 0
for num in nums:
    pattern = re.compile(r'@Composable\nfun ResumeTemplate' + num + r'Screen\(')
    m2 = pattern.search(s)
    if not m2:
        print(f"Template{num}Screen not found, skip")
        continue
    fstart = m2.start()
    bopen = s.find("{", m2.end())
    bend = find_matching_brace(s, bopen)

    new_block = master_block.replace("ResumeTemplate02Screen", f"ResumeTemplate{num}Screen")
    new_block = new_block.replace("ResumeTemplate02_PixelPerfect", f"ResumeTemplate{num}_PixelPerfect")

    s = s[:fstart] + new_block + s[bend:]
    count += 1
    print(f"Replaced Template{num}Screen call")

open(FORM_FILE, "w").write(s)
print(f"\nTotal replaced: {count}")
