path = "app/src/main/java/com/saltech/urdocs/ui/templates/ResumeTemplate02_PixelPerfect.kt"
s = open(path).read()

old = '''    val accent = Color(0xFF9EFF00)
    val nameFontSize = autoShrinkNameFontSize(userName)

    Box(Modifier.fillMaxSize().background(Color(0xFF050505)).padding(0.dp).verticalScroll(rememberScrollState())) {'''

new = '''    val accent = Color(0xFF9EFF00)
    val nameFontSize = autoShrinkNameFontSize(userName)
    val graphicsLayer = androidx.compose.ui.graphics.rememberGraphicsLayer()

    Box(Modifier.fillMaxSize()) {
    Box(
        Modifier
            .fillMaxSize()
            .drawWithContent {
                graphicsLayer.record { this@drawWithContent.drawContent() }
                drawLayer(graphicsLayer)
            }
            .background(Color(0xFF050505)).padding(0.dp).verticalScroll(rememberScrollState())
    ) {'''

assert old in s
s = s.replace(old, new, 1)

fn_idx = s.find("fun ResumeTemplate02_PixelPerfect(")
next_composable = s.find("\n@Composable\n", fn_idx + 10)
assert next_composable != -1, "next function marker not found"

last_close = s.rfind("\n}\n", fn_idx, next_composable)
assert last_close != -1, "closing brace not found"

insertion = '''
    }
    TemplateExportMenu(graphicsLayer, "resume_$userName", onHome = {})
'''
s = s[:last_close] + insertion + s[last_close+1:]

open(path, "w").write(s)
print("done")
