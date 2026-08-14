files = [
    "app/src/main/java/com/saltech/urdocs/ui/templates/ResumeTemplate01_PixelPerfect.kt",
    "app/src/main/java/com/saltech/urdocs/ui/templates/ResumeTemplate02_PixelPerfect.kt",
]

for path in files:
    with open(path, "r") as f:
        c = f.read()

    c = c.replace(
        "import androidx.compose.foundation.background",
        "import androidx.compose.foundation.background\nimport androidx.compose.foundation.rememberScrollState\nimport androidx.compose.foundation.verticalScroll",
        1
    )
    c = c.replace(
        "import androidx.compose.foundation.layout.*",
        "import androidx.compose.foundation.layout.*\nimport androidx.compose.foundation.layout.IntrinsicSize",
        1
    )
    c = c.replace(
        "Box(Modifier.fillMaxSize().background(Color(0xFF050505)).padding(0.dp)) {",
        "Box(Modifier.fillMaxSize().background(Color(0xFF050505)).padding(0.dp).verticalScroll(rememberScrollState())) {",
        1
    )
    c = c.replace(
        "Box(Modifier.fillMaxSize().padding(10.dp).border(1.dp, accent.copy(alpha=0.35f)",
        "Box(Modifier.fillMaxWidth().defaultMinSize(minHeight = 700.dp).padding(10.dp).border(1.dp, accent.copy(alpha=0.35f)",
        1
    )
    c = c.replace(
        "Column(Modifier.fillMaxSize().padding(horizontal=16.dp, vertical=18.dp)) {",
        "Column(Modifier.fillMaxWidth().padding(horizontal=16.dp, vertical=18.dp)) {",
        1
    )
    c = c.replace(
        "Row(Modifier.fillMaxSize()) {",
        "Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {",
        1
    )

    with open(path, "w") as f:
        f.write(c)
    print(path, "-> patched")

print("ALL DONE")
