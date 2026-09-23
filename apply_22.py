f = "app/src/main/java/com/saltech/urdocs/ui/screens/ResumeTemplateGalleryScreen.kt"
kt = open(f, encoding="utf-8").read()

old1 = "CREAM_BROWN_SPLIT, TOPBAR_SPLIT, DARK_YELLOW_PILL, BLACK_PILL_CUT, HEADER_PHOTO_SPLIT }"
new1 = "CREAM_BROWN_SPLIT, TOPBAR_SPLIT, DARK_YELLOW_PILL, BLACK_PILL_CUT, HEADER_PHOTO_SPLIT, NAVY_EXEC }"
assert kt.count(old1) == 1
kt = kt.replace(old1, new1)

old2 = '    TemplateInfo("ai_template_more_21.html", "21", Color(0xFF1C1C1C), Color(0xFFFFFFFF), LayoutStyle.HEADER_PHOTO_SPLIT),'
new2 = old2 + '\n    TemplateInfo("ai_template_more_22.html", "22", Color(0xFF1B3A5C), Color(0xFFFFFFFF), LayoutStyle.NAVY_EXEC),'
assert kt.count(old2) == 1
kt = kt.replace(old2, new2)

old3 = "        LayoutStyle.HEADER_PHOTO_SPLIT -> HeaderPhotoSplitPreview(modifier)"
new3 = old3 + "\n        LayoutStyle.NAVY_EXEC -> NavyExecPreview(modifier)"
assert kt.count(old3) == 1
kt = kt.replace(old3, new3)

anchor_end = kt.rindex("@Composable\nprivate fun HeaderPhotoSplitPreview")
close_idx = kt.index("\n}", anchor_end) + 2

new_fun = '''

@Composable
private fun NavyExecPreview(modifier: Modifier = Modifier) {
    val navy = Color(0xFF1B3A5C)
    val gray = Color(0xFF999999)
    Row(modifier = modifier.background(Color.White).padding(6.dp)) {
        Column(modifier = Modifier.width(32.dp).fillMaxHeight().background(navy).padding(4.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(30.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF456A8A)))
            Spacer(Modifier.height(8.dp))
            repeat(3) {
                Box(modifier = Modifier.width(22.dp).height(4.dp).background(Color.White))
                Spacer(Modifier.height(3.dp))
                repeat(2) {
                    Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.dp).background(Color(0xFF89A0B8)))
                    Spacer(Modifier.height(2.dp))
                }
                Spacer(Modifier.height(8.dp))
            }
        }
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Box(modifier = Modifier.width(60.dp).height(9.dp).background(Color(0xFF333333)))
            Spacer(Modifier.height(3.dp))
            Box(modifier = Modifier.width(44.dp).height(4.dp).background(navy))
            Spacer(Modifier.height(3.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.5.dp).background(navy))
            Spacer(Modifier.height(8.dp))
            repeat(2) {
                Box(modifier = Modifier.width(34.dp).height(5.dp).background(navy))
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
print("Kotlin patched OK for Template 22")
