f = "app/src/main/java/com/saltech/urdocs/ui/screens/ResumeTemplateGalleryScreen.kt"
kt = open(f, encoding="utf-8").read()

# 1. Add new LayoutStyle
old1 = "CREAM_BROWN_SPLIT, TOPBAR_SPLIT }"
new1 = "CREAM_BROWN_SPLIT, TOPBAR_SPLIT, DARK_YELLOW_PILL }"
assert kt.count(old1) == 1
kt = kt.replace(old1, new1)

# 2. Add TemplateInfo entry after more_18
old2 = '    TemplateInfo("ai_template_more_18.html", "18", Color(0xFF1A1A1A), Color(0xFFF7F4EE), LayoutStyle.TOPBAR_SPLIT),'
new2 = old2 + '\n    TemplateInfo("ai_template_more_19.html", "19", Color(0xFF1E1E1E), Color(0xFFF2C230), LayoutStyle.DARK_YELLOW_PILL),'
assert kt.count(old2) == 1
kt = kt.replace(old2, new2)

# 3. Add preview branch
old3 = "        LayoutStyle.TOPBAR_SPLIT -> TopbarSplitPreview(modifier)"
new3 = old3 + "\n        LayoutStyle.DARK_YELLOW_PILL -> DarkYellowPillPreview(modifier)"
assert kt.count(old3) == 1
kt = kt.replace(old3, new3)

# 4. Add the preview composable function, right after PillHeaderPreview's closing brace
anchor = '''            Column(modifier = Modifier.weight(1f)) {
                repeat(3) {
                    Box(modifier = Modifier.fillMaxWidth().height(6.dp).background(dark, RoundedCornerShape(3.dp)))
                    Spacer(Modifier.height(3.dp))
                    repeat(3) {
                        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(gray))
                        Spacer(Modifier.height(2.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}'''
assert kt.count(anchor) == 1

new_fun = '''

@Composable
private fun DarkYellowPillPreview(modifier: Modifier = Modifier) {
    val dark = Color(0xFF1E1E1E)
    val yellow = Color(0xFFF2C230)
    val gray = Color(0xFFB8B8B8)
    Row(modifier = modifier.background(dark).padding(6.dp)) {
        Column(modifier = Modifier.width(34.dp)) {
            Box(modifier = Modifier.size(28.dp).background(Color(0xFF3A3A3A)))
            Spacer(Modifier.height(6.dp))
            repeat(3) {
                Box(modifier = Modifier.width(26.dp).height(6.dp).background(yellow, RoundedCornerShape(50)))
                Spacer(Modifier.height(3.dp))
                repeat(2) {
                    Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.dp).background(gray))
                    Spacer(Modifier.height(2.dp))
                }
                Spacer(Modifier.height(6.dp))
            }
        }
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.width(50.dp).height(8.dp).background(Color.White))
            Spacer(Modifier.height(3.dp))
            Box(modifier = Modifier.width(36.dp).height(4.dp).background(yellow))
            Spacer(Modifier.height(8.dp))
            repeat(3) {
                Box(modifier = Modifier.width(26.dp).height(6.dp).background(yellow, RoundedCornerShape(50)))
                Spacer(Modifier.height(3.dp))
                repeat(2) {
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(gray))
                    Spacer(Modifier.height(2.dp))
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}'''

kt = kt.replace(anchor, anchor + new_fun, 1)

open(f, "w", encoding="utf-8").write(kt)
print("Kotlin patched OK")
