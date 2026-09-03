#!/usr/bin/env python3
import sys

path = "app/src/main/java/com/saltech/urdocs/ui/screens/ChronologicalResumeScreen.kt"

with open(path, "r", encoding="utf-8") as f:
    content = f.read()

# ---------- EDIT 1: add imports ----------
old_imports = '''import androidx.compose.ui.text.AnnotatedString

private val NavyColor = Color(0xFF1B2A4A)'''

new_imports = '''import androidx.compose.ui.text.AnnotatedString
import android.graphics.pdf.PdfDocument
import android.text.StaticLayout
import android.text.TextPaint

private val NavyColor = Color(0xFF1B2A4A)'''

# ---------- EDIT 2: add PDF export function after saveBitmapToGalleryChrono ----------
old_savefunc = '''private fun saveBitmapToGalleryChrono(context: android.content.Context, bitmap: Bitmap) {
    val filename = "Resume_Chronological_${System.currentTimeMillis()}.png"
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/URDocs")
        }
    }
    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        resolver.openOutputStream(it)?.use { out -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, out) }
        android.widget.Toast.makeText(context, "see your gallery luv🩵", android.widget.Toast.LENGTH_LONG).show()
    } ?: run {
        android.widget.Toast.makeText(context, "Hindi na-download, subukan ulit.", android.widget.Toast.LENGTH_LONG).show()
    }
}'''

new_savefunc = old_savefunc + '''

// ===== REAL ATS-friendly export: text-based PDF (hindi bitmap/image) =====
private fun exportChronoResumeToPdf(context: android.content.Context, data: ChronologicalResumeFields) {
    val pageWidth = 595
    val pageHeight = 842
    val marginX = 40f
    val contentWidth = (pageWidth - marginX * 2).toInt()
    var y = 40f

    val pdfDocument = PdfDocument()
    var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    var page = pdfDocument.startPage(pageInfo)
    var canvas = page.canvas
    var pageNum = 1

    val navy = android.graphics.Color.rgb(27, 42, 74)
    val black = android.graphics.Color.BLACK

    val titlePaint = TextPaint().apply { color = navy; textSize = 20f; isFakeBoldText = true; isAntiAlias = true; textAlign = android.graphics.Paint.Align.CENTER }
    val namePaint = TextPaint().apply { color = black; textSize = 13f; isAntiAlias = true; textAlign = android.graphics.Paint.Align.CENTER }
    val contactPaint = TextPaint().apply { color = black; textSize = 9f; isAntiAlias = true; textAlign = android.graphics.Paint.Align.CENTER }
    val headerPaint = TextPaint().apply { color = navy; textSize = 12f; isFakeBoldText = true; isAntiAlias = true }
    val bodyPaint = TextPaint().apply { color = black; textSize = 10f; isAntiAlias = true }
    val boldPaint = TextPaint().apply { color = black; textSize = 10f; isFakeBoldText = true; isAntiAlias = true }
    val italicPaint = TextPaint().apply { color = black; textSize = 9.5f; isAntiAlias = true; textSkewX = -0.25f }
    val linePaint = android.graphics.Paint().apply { color = navy; strokeWidth = 1f }

    fun ensureSpace(needed: Float) {
        if (y + needed > pageHeight - 40f) {
            pdfDocument.finishPage(page)
            pageNum += 1
            pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create()
            page = pdfDocument.startPage(pageInfo)
            canvas = page.canvas
            y = 40f
        }
    }

    fun drawWrapped(text: String, paint: TextPaint, width: Int, leading: Float = 1.15f) {
        if (text.isBlank()) return
        val layout = StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
            .setLineSpacing(0f, leading)
            .build()
        ensureSpace(layout.height.toFloat() + 4f)
        canvas.save()
        canvas.translate(marginX, y)
        layout.draw(canvas)
        canvas.restore()
        y += layout.height + 6f
    }

    fun sectionHeader(title: String) {
        ensureSpace(24f)
        canvas.drawText(title, marginX, y + 10f, headerPaint)
        y += 14f
        canvas.drawLine(marginX, y, pageWidth - marginX, y, linePaint)
        y += 10f
    }

    fun bulletList(items: List<String>) {
        items.filter { it.isNotBlank() }.forEach { drawWrapped("•  $it", bodyPaint, contentWidth - 10) }
    }

    ensureSpace(30f)
    canvas.drawText(data.jobTitle, pageWidth / 2f, y + 16f, titlePaint)
    y += 24f
    canvas.drawText(data.name, pageWidth / 2f, y + 10f, namePaint)
    y += 16f
    canvas.drawText("${data.phone}   •   ${data.email}   •   ${data.location}   •   ${data.linkedin}", pageWidth / 2f, y + 8f, contactPaint)
    y += 16f
    canvas.drawLine(marginX, y, pageWidth - marginX, y, linePaint)
    y += 14f

    sectionHeader("PROFESSIONAL SUMMARY")
    drawWrapped(data.summary, bodyPaint, contentWidth)
    y += 6f

    sectionHeader("SKILLS")
    bulletList(data.technicalSkills)
    bulletList(data.softSkills)
    y += 6f

    sectionHeader("PROFESSIONAL EXPERIENCE")
    data.work.forEach { entry ->
        ensureSpace(16f)
        canvas.drawText(entry.company, marginX, y + 10f, boldPaint)
        val dateText = "${entry.from} - ${entry.to}"
        canvas.drawText(dateText, pageWidth - marginX - boldPaint.measureText(dateText), y + 10f, boldPaint)
        y += 14f
        drawWrapped(entry.role, italicPaint, contentWidth, 1.1f)
        bulletList(entry.bullets)
        y += 6f
    }

    sectionHeader("EDUCATION")
    drawWrapped(data.eduSchool, bodyPaint, contentWidth, 1.1f)
    drawWrapped(data.eduDegree, boldPaint, contentWidth, 1.1f)
    drawWrapped(data.eduYear, bodyPaint, contentWidth, 1.1f)
    y += 6f

    sectionHeader("LANGUAGES")
    bulletList(data.languages)
    y += 6f

    sectionHeader("INTERESTS")
    bulletList(data.interests)

    pdfDocument.finishPage(page)

    val filename = "Resume_Chronological_ATS_${System.currentTimeMillis()}.pdf"
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Documents/URDocs")
        }
    }
    val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
    if (uri != null) {
        resolver.openOutputStream(uri)?.use { out -> pdfDocument.writeTo(out) }
        android.widget.Toast.makeText(context, "PDF saved luv🩵 (ATS-friendly text)", android.widget.Toast.LENGTH_LONG).show()
    } else {
        android.widget.Toast.makeText(context, "Hindi na-save yung PDF, subukan ulit.", android.widget.Toast.LENGTH_LONG).show()
    }
    pdfDocument.close()
}'''

# ---------- EDIT 3: add second button (PDF) beside existing Download button ----------
old_button = '''        Button(
            onClick = {
                fun doSave() {
                    scale = fitScale
                    offset = Offset.Zero
                    coroutineScope.launch {
                        delay(100)
                        val bitmap = Bitmap.createBitmap(
                            picture.width.coerceAtLeast(1),
                            picture.height.coerceAtLeast(1),
                            Bitmap.Config.ARGB_8888
                        )
                        val canvas = android.graphics.Canvas(bitmap)
                        canvas.drawColor(android.graphics.Color.WHITE)
                        canvas.drawPicture(picture)
                        saveBitmapToGalleryChrono(context, bitmap)
                    }
                }
                val activity = context as? android.app.Activity
               if (activity != null && interstitialAd != null) {
                    interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            interstitialAd = null
                            doSave()
                        }
                    }
                    interstitialAd?.show(activity)
                } else {
                    doSave()
               }
            },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .wrapContentWidth()
                .heightIn(min = 52.dp)
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
                .background(
                    androidx.compose.ui.graphics.Brush.horizontalGradient(
                        listOf(Color(0xFF3B6FE0), Color(0xFF1A1A1A), Color(0xFF0B1530))
                    )
                )
        ) { Text("Download", color = Color.White, fontWeight = FontWeight.Bold) }'''

new_button = '''        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    fun doSave() {
                        scale = fitScale
                        offset = Offset.Zero
                        coroutineScope.launch {
                            delay(100)
                            val bitmap = Bitmap.createBitmap(
                                picture.width.coerceAtLeast(1),
                                picture.height.coerceAtLeast(1),
                                Bitmap.Config.ARGB_8888
                            )
                            val canvas = android.graphics.Canvas(bitmap)
                            canvas.drawColor(android.graphics.Color.WHITE)
                            canvas.drawPicture(picture)
                            saveBitmapToGalleryChrono(context, bitmap)
                        }
                    }
                    val activity = context as? android.app.Activity
                    if (activity != null && interstitialAd != null) {
                        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                interstitialAd = null
                                doSave()
                            }
                        }
                        interstitialAd?.show(activity)
                    } else {
                        doSave()
                    }
                },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .wrapContentWidth()
                    .heightIn(min = 52.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
                    .background(
                        androidx.compose.ui.graphics.Brush.horizontalGradient(
                            listOf(Color(0xFF3B6FE0), Color(0xFF1A1A1A), Color(0xFF0B1530))
                        )
                    )
            ) { Text("Image", color = Color.White, fontWeight = FontWeight.Bold) }

            Button(
                onClick = { exportChronoResumeToPdf(context, data) },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = NavyColor),
                contentPadding = PaddingValues(horizontal = 18.dp),
                modifier = Modifier
                    .wrapContentWidth()
                    .heightIn(min = 52.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(50))
            ) { Text("PDF (ATS)", color = Color.White, fontWeight = FontWeight.Bold) }
        }'''

edits = [
    ("imports", old_imports, new_imports),
    ("save function / new PDF function", old_savefunc, new_savefunc),
    ("download button", old_button, new_button),
]

errors = []
for name, old, new in edits:
    c = content.count(old)
    if c == 0:
        errors.append(f"NO MATCH for [{name}]")
    elif c > 1:
        errors.append(f"{c} MATCHES for [{name}] (dapat 1 lang)")

if errors:
    print("Walang nagalaw sa file mo. Mga problema:")
    for e in errors:
        print(" - " + e)
    sys.exit(1)

for name, old, new in edits:
    content = content.replace(old, new)

with open(path, "w", encoding="utf-8") as f:
    f.write(content)

print("Done! Na-add na yung totoong PDF (ATS-friendly text) export, kasabay ng existing Image download.")
