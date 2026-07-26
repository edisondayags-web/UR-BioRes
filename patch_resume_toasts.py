import re

files_and_names = [
    ("app/src/main/java/com/saltech/urdocs/ui/screens/ChronologicalResumeScreen.kt", "saveBitmapToGalleryChrono"),
    ("app/src/main/java/com/saltech/urdocs/ui/screens/TraditionalResumeScreen.kt", "saveBitmapToGalleryTraditional"),
]

for path, fname in files_and_names:
    with open(path, "r", encoding="utf-8") as f:
        content = f.read()

    old = '''    uri?.let {
        resolver.openOutputStream(it)?.use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
    }
}'''

    new = '''    uri?.let {
        resolver.openOutputStream(it)?.use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
        android.widget.Toast.makeText(context, "Na-download! Nasa Gallery (Pictures/URDocs)", android.widget.Toast.LENGTH_LONG).show()
    } ?: run {
        android.widget.Toast.makeText(context, "Hindi na-download, subukan ulit.", android.widget.Toast.LENGTH_LONG).show()
    }
}'''

    count = content.count(old)
    assert count == 1, f"{path}: expected 1 match, got {count}"
    content = content.replace(old, new, 1)

    with open(path, "w", encoding="utf-8") as f:
        f.write(content)
    print(f"DONE: {path}")
