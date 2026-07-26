path = "app/src/main/java/com/saltech/urdocs/ui/screens/BioDataScreen.kt"
with open(path, "r", encoding="utf-8") as f:
    content = f.read()

old = '''    uri?.let {
        resolver.openOutputStream(it)?.use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    }
}'''

new = '''    uri?.let {
        resolver.openOutputStream(it)?.use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        android.widget.Toast.makeText(context, "Na-download! Nasa Gallery (Pictures/URDocs)", android.widget.Toast.LENGTH_LONG).show()
    } ?: run {
        android.widget.Toast.makeText(context, "Hindi na-download, subukan ulit.", android.widget.Toast.LENGTH_LONG).show()
    }
}'''

assert old in content, "OLD NOT FOUND"
content = content.replace(old, new, 1)

with open(path, "w", encoding="utf-8") as f:
    f.write(content)
print("DONE")
