path = "app/src/main/java/com/saltech/urdocs/ui/screens/GovtFormsScreen.kt"

def find_matching(text, open_idx, open_ch, close_ch):
    depth = 0
    i = open_idx
    while i < len(text):
        if text[i] == open_ch:
            depth += 1
        elif text[i] == close_ch:
            depth -= 1
            if depth == 0:
                return i
        i += 1
    raise ValueError(f"No matching {close_ch} found")

def extract_call_block(text, call_start_idx):
    paren_open = text.index('(', call_start_idx)
    paren_close = find_matching(text, paren_open, '(', ')')
    j = paren_close + 1
    while text[j] in ' \t\n':
        j += 1
    if j < len(text) and text[j] == '{':
        brace_close = find_matching(text, j, '{', '}')
        return text[call_start_idx:brace_close+1], brace_close+1
    else:
        return text[call_start_idx:paren_close+1], paren_close+1

with open(path, "r", encoding="utf-8") as f:
    content = f.read()

header_marker = "// ---------- HEADER ----------"
search_marker = "// ---------- SEARCH BAR ----------"
filter_marker = "// ---------- FILTER CHIPS ----------"

assert header_marker in content, "HEADER marker not found - screen may have already been patched"
assert search_marker in content, "SEARCH BAR marker not found"
assert filter_marker in content, "FILTER CHIPS marker not found"

header_marker_idx = content.index(header_marker)
search_marker_idx = content.index(search_marker)
filter_marker_idx = content.index(filter_marker)

before_header = content[:header_marker_idx]
after_filter = content[filter_marker_idx:]

header_section = content[header_marker_idx:search_marker_idx]
search_section = content[search_marker_idx:filter_marker_idx]

row_start = header_section.index("Row(")
row_full, row_end = extract_call_block(header_section, row_start)
body_start = row_full.index('{') + 1
body = row_full[body_start:-1]

box1_start = body.index("Box(")
back_box_full, box1_end = extract_call_block(body, box1_start)

col_start = body.index("Column(", box1_end)
logo_col_full, col_end = extract_call_block(body, col_start)

box2_start = body.index("Box(", col_end)
heart_box_full, box2_end = extract_call_block(body, box2_start)

srow_start = search_section.index("Row(")
srow_full, srow_end = extract_call_block(search_section, srow_start)
sbody_start = srow_full.index('{') + 1
sbody = srow_full[sbody_start:-1]

logo_inner = logo_col_full[logo_col_full.index('{')+1 : -1]

new_section = f"""// ---------- TOP ROW: back + search + heart ----------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {{
            {back_box_full}
            Spacer(Modifier.width(10.dp))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GCardBg)
                    .border(
                        BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(GGreen, GPink))),
                        RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {{
{sbody}
            }}
            Spacer(Modifier.width(10.dp))
            {heart_box_full}
        }}

        // ---------- LOGO / TITLE ----------
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {{
{logo_inner}
        }}

        Spacer(Modifier.height(12.dp))

        """

new_content = before_header + new_section + after_filter

with open(path, "w", encoding="utf-8") as f:
    f.write(new_content)

print("DONE")
