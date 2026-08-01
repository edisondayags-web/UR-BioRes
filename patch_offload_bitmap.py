import re

files = [
    "app/src/main/java/com/saltech/urdocs/ui/screens/ResignationLetterScreen.kt",
    "app/src/main/java/com/saltech/urdocs/ui/screens/BioDataScreen.kt",
    "app/src/main/java/com/saltech/urdocs/ui/screens/ChronologicalResumeScreen.kt",
    "app/src/main/java/com/saltech/urdocs/ui/screens/LetterAssistantScreen.kt",
    "app/src/main/java/com/saltech/urdocs/ui/screens/TraditionalResumeScreen.kt",
    "app/src/main/java/com/saltech/urdocs/ui/screens/LeaveLetterScreen.kt",
    "app/src/main/java/com/saltech/urdocs/ui/screens/GenericLetterScreen.kt",
]

# Matches from "val bitmap = Bitmap.createBitmap(" down through the
# save...(context, bitmap) call, non-greedy, across multiple lines.
block_pattern = re.compile(
    r"([ \t]*)(val bitmap = Bitmap\.createBitmap\([\s\S]*?save\w*\(context, bitmap\)\s*\n)"
)

changed = []
skipped = []

for path in files:
    try:
        with open(path) as f:
            src = f.read()
    except FileNotFoundError:
        skipped.append(f"{path} (not found)")
        continue

    match = block_pattern.search(src)
    if not match:
        skipped.append(f"{path} (pattern not found - check manually)")
        continue

    indent, block = match.group(1), match.group(2)
    # Re-indent the block one level deeper, wrap in withContext(Dispatchers.Default)
    inner = "".join(
        (indent + "    " + line[len(indent):] if line.strip() else line)
        for line in block.splitlines(keepends=True)
    )
    wrapped = f"{indent}withContext(Dispatchers.Default) {{\n{inner}{indent}}}\n"
    new_src = src[:match.start()] + wrapped + src[match.end():]

    # Ensure imports exist
    if "import kotlinx.coroutines.withContext" not in new_src:
        new_src = new_src.replace(
            "import androidx.compose.animation.AnimatedVisibility",
            "import androidx.compose.animation.AnimatedVisibility\nimport kotlinx.coroutines.withContext",
            1,
        )
        if "import kotlinx.coroutines.withContext" not in new_src:
            # fallback: insert after package line
            new_src = new_src.replace(
                "\n", "\nimport kotlinx.coroutines.withContext\n", 1
            )
    if "import kotlinx.coroutines.Dispatchers" not in new_src:
        new_src = new_src.replace(
            "import kotlinx.coroutines.withContext",
            "import kotlinx.coroutines.withContext\nimport kotlinx.coroutines.Dispatchers",
            1,
        )

    with open(path, "w") as f:
        f.write(new_src)
    changed.append(path)

print("CHANGED:")
for c in changed:
    print(" -", c)
print("\nSKIPPED (review manually):")
for s in skipped:
    print(" -", s)
