f = "app/src/main/java/com/saltech/urdocs/ui/screens/ResumeTemplateGalleryScreen.kt"
kt = open(f, encoding="utf-8").read()

# 1. Add all 3 new LayoutStyle values
old1 = "CREAM_BROWN_SPLIT, TOPBAR_SPLIT, DARK_YELLOW_PILL }"
new1 = "CREAM_BROWN_SPLIT, TOPBAR_SPLIT, DARK_YELLOW_PILL, BLACK_PILL_CUT, HEADER_PHOTO_SPLIT, NAVY_EXEC }"
assert kt.count(old1) == 1, "enum anchor not found"
kt = kt.replace(old1, new1)

# 2. Add all 3 TemplateInfo entries after 19
old2 = '    TemplateInfo("ai_template_more_19.html", "19", Color(0xFF1E1E1E), Color(0xFFF2C230), LayoutStyle.DARK_YELLOW_PILL),'
new2 = old2 + '''
    TemplateInfo("ai_template_more_20.html", "20", Color(0xFF161616), Color(0xFFFFFFFF), LayoutStyle.BLACK_PILL_CUT),
    TemplateInfo("ai_template_more_21.html", "21", Color(0xFF1C1C1C), Color(0xFFFFFFFF), LayoutStyle.HEADER_PHOTO_SPLIT),
    TemplateInfo("ai_template_more_22.html", "22", Color(0xFF1B3A5C), Color(0xFFFFFFFF), LayoutStyle.NAVY_EXEC),'''
assert kt.count(old2) == 1, "TemplateInfo anchor not found"
kt = kt.replace(old2, new2)

# 3. Add all 3 preview branches
old3 = "        LayoutStyle.DARK_YELLOW_PILL -> DarkYellowPillPreview(modifier)"
new3 = old3 + '''
        LayoutStyle.BLACK_PILL_CUT -> BlackPillCutPreview(modifier)
        LayoutStyle.HEADER_PHOTO_SPLIT -> HeaderPhotoSplitPreview(modifier)
        LayoutStyle.NAVY_EXEC -> NavyExecPreview(modifier)'''
assert kt.count(old3) == 1, "branch anchor not found"
kt = kt.replace(old3, new3)

# 4. Add all 3 preview composables after DarkYellowPillPreview's closing brace
anchor_end = kt.rindex("@Composable\nprivate fun DarkYellowPillPreview")
close_idx = kt.index("\n}", anchor_end) + 2

new_funs = '''

@Composable
private fun BlackPillCutPreview(modifier: Modifier = Modifier) {
    val black = Color(0xFF161616)
    val gray = Color(0xFF999999)
    Row(modifier = modifier.background(Color.White).padding(6.dp)) {
        Column(modifier = Modifier.width(30.dp).background(black).padding(4.dp)) {
            Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFFDDDDDD)).align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(4.dp))
            Box(modifier = Modifier.width(22.dp).height(4.dp).background(Color.White).align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(8.dp))
            repeat(3) {
                Box(modifier = Modifier.fillMaxWidth().height(6.dp).background(Color.White, RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)))
                Spacer(Modifier.height(3.dp))
                repeat(2) {
                    Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.dp).background(Color(0xFFCCCCCC)))
                    Spacer(Modifier.height(2.dp))
                }
                Spacer(Modifier.height(6.dp))
            }
        }
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            repeat(2) {
                Box(modifier = Modifier.width(36.dp).height(6.dp).background(gray))
                Spacer(Modifier.height(4.dp))
                repeat(2) {
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFFDDDDDD)))
                    Spacer(Modifier.height(2.dp))
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun HeaderPhotoSplitPreview(modifier: Modifier = Modifier) {
    val black = Color(0xFF1C1C1C)
    val gray = Color(0xFF999999)
    Column(modifier = modifier.background(Color.White).padding(6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(22.dp).clip(CircleShape).background(Color(0xFFDDDDDD)))
            Spacer(Modifier.width(8.dp))
            Column {
                Box(modifier = Modifier.width(46.dp).height(7.dp).background(black))
                Spacer(Modifier.height(3.dp))
                Box(modifier = Modifier.width(30.dp).height(3.dp).background(gray))
            }
        }
        Spacer(Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(black))
        Spacer(Modifier.height(6.dp))
        Row(modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.width(30.dp).fillMaxHeight().background(black).padding(4.dp)) {
                repeat(3) {
                    Box(modifier = Modifier.width(22.dp).height(4.dp).background(Color.White))
                    Spacer(Modifier.height(3.dp))
                    Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.dp).background(Color(0xFFAAAAAA)))
                    Spacer(Modifier.height(8.dp))
                }
            }
            Spacer(Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                repeat(3) {
                    Box(modifier = Modifier.width(30.dp).height(5.dp).background(black))
                    Spacer(Modifier.height(3.dp))
                    repeat(2) {
                        Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFFDDDDDD)))
                        Spacer(Modifier.height(2.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

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

kt = kt[:close_idx] + new_funs + kt[close_idx:]

open(f, "w", encoding="utf-8").write(kt)
print("Kotlin patched OK for Templates 20, 21, 22")
