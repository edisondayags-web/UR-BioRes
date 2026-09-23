f = "app/src/main/java/com/saltech/urdocs/ui/screens/ResumeTemplateGalleryScreen.kt"
kt = open(f, encoding="utf-8").read()

old1 = "CREAM_BROWN_SPLIT, TOPBAR_SPLIT, DARK_YELLOW_PILL, BLACK_PILL_CUT, HEADER_PHOTO_SPLIT, NAVY_EXEC }"
new1 = "CREAM_BROWN_SPLIT, TOPBAR_SPLIT, DARK_YELLOW_PILL, BLACK_PILL_CUT, HEADER_PHOTO_SPLIT, NAVY_EXEC, NAVY_SIDE_SIMPLE }"
assert kt.count(old1) == 1, "enum anchor not found"
kt = kt.replace(old1, new1)

old2 = '    TemplateInfo("ai_template_more_22.html", "22", Color(0xFF1B3A5C), Color(0xFFFFFFFF), LayoutStyle.NAVY_EXEC),'
new2 = old2 + '\n    TemplateInfo("ai_template_more_23.html", "23", Color(0xFF1B2A4A), Color(0xFFFFFFFF), LayoutStyle.NAVY_SIDE_SIMPLE),'
assert kt.count(old2) == 1, "TemplateInfo anchor not found"
kt = kt.replace(old2, new2)

old3 = "        LayoutStyle.NAVY_EXEC -> NavyExecPreview(modifier)"
new3 = old3 + "\n        LayoutStyle.NAVY_SIDE_SIMPLE -> NavySideSimplePreview(modifier)"
assert kt.count(old3) == 1, "branch anchor not found"
kt = kt.replace(old3, new3)

anchor_end = kt.rindex("@Composable\nprivate fun NavyExecPreview")
close_idx = kt.index("\n}", anchor_end) + 2

new_fun = '''

@Composable
private fun NavySideSimplePreview(modifier: Modifier = Modifier) {
    val navy = Color(0xFF1B2A4A)
    val gray = Color(0xFF999999)
    Row(modifier = modifier.background(Color.White).padding(6.dp)) {
        Column(modifier = Modifier.width(28.dp).fillMaxHeight().background(navy).padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFF5A6E94)))
            Spacer(Modifier.height(8.dp))
            repeat(2) {
                Box(modifier = Modifier.width(20.dp).height(3.dp).background(Color.White))
                Spacer(Modifier.height(6.dp))
                repeat(2) {
                    Box(modifier = Modifier.width(20.dp).height(2.dp).background(Color(0xFF8A9AB8)))
                    Spacer(Modifier.height(2.dp))
                }
                Spacer(Modifier.height(8.dp))
            }
        }
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.width(50.dp).height(7.dp).background(Color(0xFF333333)))
            Spacer(Modifier.height(3.dp))
            Box(modifier = Modifier.width(40.dp).height(3.dp).background(gray))
            Spacer(Modifier.height(8.dp))
            repeat(3) {
                Box(modifier = Modifier.width(30.dp).height(4.dp).background(navy))
                Spacer(Modifier.height(4.dp))
                repeat(2) {
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFFDDDDDD)))
                    Spacer(Modifier.height(2.dp))
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}'''

kt = kt[:close_idx] + new_fun + kt[close_idx:]

open(f, "w", encoding="utf-8").write(kt)
print("Kotlin patched OK for Template 23")
