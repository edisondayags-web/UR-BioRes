path = "app/src/main/java/com/saltech/urdocs/ui/screens/LetterAssistantScreen.kt"

with open(path) as f:
    lines = f.readlines()

start_line = 474  # 1-indexed, "suspend fun saveToGallery() {"
end_line = 513    # 1-indexed, closing "    }"

# Sanity check before touching anything
first = lines[start_line - 1]
last = lines[end_line - 1]
if "suspend fun saveToGallery" not in first:
    print("ABORT: line", start_line, "is not the start of saveToGallery(). Found:")
    print(repr(first))
    raise SystemExit(1)

new_block = '''    suspend fun saveToGallery() {
        var success = false

        withContext(Dispatchers.IO) {
            val widthPx = with(density) { paperWidthDp.roundToPx() }
            val heightPx = with(density) { paperHeightDp.roundToPx() }
            val paddingPx = with(density) { 48.dp.roundToPx() }

            val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
            val canvas = android.graphics.Canvas(bitmap)
            canvas.drawColor(android.graphics.Color.WHITE)

            val paint = android.text.TextPaint().apply {
                color = android.graphics.Color.BLACK
                textSize = with(density) { 16.sp.toPx() }
                isAntiAlias = true
            }

            val layout = android.text.StaticLayout.Builder
                .obtain(letterText, 0, letterText.length, paint, (widthPx - 2 * paddingPx).coerceAtLeast(1))
                .setLineSpacing(0f, 1.5f)
                .build()

            canvas.save()
            canvas.translate(paddingPx.toFloat(), paddingPx.toFloat())
            layout.draw(canvas)
            canvas.restore()

            val filename = "UR_Letter_${System.currentTimeMillis()}.png"
            val resolver = context.contentResolver
            val values = android.content.ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/URDocs")
            }
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            uri?.let {
                resolver.openOutputStream(it)?.use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
                success = true
            }
        }

        withContext(Dispatchers.Main) {
            if (success) {
                android.widget.Toast.makeText(context, "see your gallery luv\\uD83D\\uDDE1", android.widget.Toast.LENGTH_LONG).show()
            } else {
                android.widget.Toast.makeText(context, "Hindi na-download, subukan ulit.", android.widget.Toast.LENGTH_LONG).show()
            }
        }
    }
'''

new_lines = lines[:start_line - 1] + [new_block] + lines[end_line:]
new_src = "".join(new_lines)

if "import kotlinx.coroutines.withContext" not in new_src:
    new_src = new_src.replace("\n", "\nimport kotlinx.coroutines.withContext\n", 1)
if "import kotlinx.coroutines.Dispatchers" not in new_src:
    new_src = new_src.replace(
        "import kotlinx.coroutines.withContext",
        "import kotlinx.coroutines.withContext\nimport kotlinx.coroutines.Dispatchers",
        1,
    )

with open(path, "w") as f:
    f.write(new_src)

print("PATCHED lines", start_line, "-", end_line, "in", path)
