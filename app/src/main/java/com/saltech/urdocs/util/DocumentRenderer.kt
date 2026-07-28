package com.saltech.urdocs.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface

data class EducRowData(val level: String, val school: String, val address: String, val year: String, val honors: String)
data class WorkRowData(val company: String, val position: String, val duties: String, val started: String, val ended: String)
data class TrainingRowData(val title: String, val sponsor: String, val date: String)
data class ReferenceRowData(val name: String, val position: String, val company: String, val contact: String)

data class ResumeFull(
    val fullName: String = "",
    val address: String = "",
    val contactNumber: String = "",
    val email: String = "",
    val birthDate: String = "",
    val age: String = "",
    val civilStatus: String = "",
    val nationality: String = "",
    val objective: String = "",
    val educRows: List<EducRowData> = emptyList(),
    val workRows: List<WorkRowData> = emptyList(),
    val skills: List<String> = emptyList(),
    val trainingRows: List<TrainingRowData> = emptyList(),
    val referenceRows: List<ReferenceRowData> = emptyList()
)

data class BioDataFull(
    val fullName: String = "", val gender: String = "", val birthDate: String = "",
    val currentAddress: String = "", val permanentAddress: String = "", val age: String = "",
    val date: String = "", val occupation: String = "", val telephone: String = "",
    val civilStatus: String = "", val cellphone: String = "", val placeOfBirth: String = "",
    val email: String = "", val height: String = "", val citizenship: String = "",
    val weight: String = "", val religion: String = "", val fatherName: String = "",
    val fatherOccupation: String = "", val motherName: String = "", val motherOccupation: String = "",
    val language: String = "", val emergencyContact: String = "", val emergencyAddress: String = "",
    val emergencyContactNo: String = "", val elementary: String = "", val elementaryYear: String = "",
    val highSchool: String = "", val highSchoolYear: String = "", val college: String = "",
    val collegeYear: String = ""
)

/**
 * Gumagawa ng "printable" na bitmap (bond-paper na puti) ng Resume o
 * Bio-Data na SUNOD SA TOTOONG PH TEMPLATE FORMAT -- kasama ang tables
 * para sa Educational Background, Work Experience, Trainings, at
 * Character References, hindi lang plain paragraphs.
 */
object DocumentRenderer {

    private const val DOC_WIDTH = 1240
    private const val MARGIN = 50f

    private fun canvasOf(height: Int): Pair<Bitmap, Canvas> {
        val bitmap = Bitmap.createBitmap(DOC_WIDTH, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        return bitmap to canvas
    }

    private fun drawTable(
        canvas: Canvas, x: Float, y: Float, width: Float,
        headers: List<String>, weights: List<Float>, rows: List<List<String>>, black: Paint
    ): Float {
        val headerPaint = Paint(black).apply { textSize = 18f; typeface = Typeface.DEFAULT_BOLD }
        val cellPaint = Paint(black).apply { textSize = 18f }
        val border = Paint(black).apply { style = Paint.Style.STROKE; strokeWidth = 2f }
        val rowH = 50f
        val totalW = weights.sum()
        val colW = weights.map { it / totalW * width }

        var cy = y
        var cx = x
        for (i in headers.indices) {
            canvas.drawRect(cx, cy, cx + colW[i], cy + rowH, border)
            canvas.drawText(headers[i], cx + 6f, cy + rowH / 2 + 6f, headerPaint)
            cx += colW[i]
        }
        cy += rowH
        for (row in rows) {
            cx = x
            for (i in row.indices) {
                canvas.drawRect(cx, cy, cx + colW[i], cy + rowH, border)
                val text = if (row[i].length > 22) row[i].take(20) + "…" else row[i]
                canvas.drawText(text, cx + 6f, cy + rowH / 2 + 6f, cellPaint)
                cx += colW[i]
            }
            cy += rowH
        }
        return cy
    }

    fun renderResume(data: ResumeFull, photo: Bitmap?): Bitmap {
        val estHeight = 1450 + data.workRows.size * 50 + data.trainingRows.size * 50 +
            data.referenceRows.size * 50 + data.skills.size * 34
        val (bitmap, canvas) = canvasOf(estHeight)
        val black = Paint().apply { color = Color.BLACK; isAntiAlias = true }

        val photoSize = 260f
        val photoRect = RectF(MARGIN, 40f, MARGIN + photoSize, 40f + photoSize)
        val box = Paint(black).apply { style = Paint.Style.STROKE; strokeWidth = 3f }
        photo?.let {
            val scaled = Bitmap.createScaledBitmap(it, photoSize.toInt(), photoSize.toInt(), true)
            canvas.drawBitmap(scaled, MARGIN, 40f, null)
        }
        canvas.drawRect(photoRect, box)

        val titlePaint = Paint(black).apply { textSize = 56f; typeface = Typeface.DEFAULT_BOLD; textAlign = Paint.Align.RIGHT }
        canvas.drawText("RESUME", DOC_WIDTH - MARGIN, 90f, titlePaint)

        val labelPaint = Paint(black).apply { textSize = 22f; typeface = Typeface.DEFAULT_BOLD }
        val valuePaint = Paint(black).apply { textSize = 22f }
        val textLeft = MARGIN + photoSize + 30f
        var y = 90f
        fun info(label: String, value: String) {
            canvas.drawText(label, textLeft, y, labelPaint)
            canvas.drawText(value, textLeft + 210f, y, valuePaint)
            y += 32f
        }
        info("FULL NAME:", data.fullName)
        info("ADDRESS:", data.address)
        info("CONTACT NUMBER:", data.contactNumber)
        info("EMAIL ADDRESS:", data.email)
        info("DATE OF BIRTH:", data.birthDate)
        info("AGE:", data.age)
        info("CIVIL STATUS:", data.civilStatus)
        info("NATIONALITY:", data.nationality)

        y = maxOf(y, 40f + photoSize) + 20f
        canvas.drawLine(MARGIN, y, DOC_WIDTH - MARGIN, y, black)
        y += 36f

        val headerPaint = Paint(black).apply { textSize = 26f; typeface = Typeface.DEFAULT_BOLD }
        canvas.drawText("CAREER OBJECTIVE", MARGIN, y, headerPaint)
        y += 30f
        val bodyPaint = Paint(black).apply { textSize = 22f }
        val words = data.objective.split(" ")
        var line = StringBuilder()
        for (w in words) {
            val test = if (line.isEmpty()) w else "$line $w"
            if (bodyPaint.measureText(test) > DOC_WIDTH - 2 * MARGIN && line.isNotEmpty()) {
                canvas.drawText(line.toString(), MARGIN, y, bodyPaint); y += 28f; line = StringBuilder(w)
            } else line = StringBuilder(test)
        }
        if (line.isNotEmpty()) { canvas.drawText(line.toString(), MARGIN, y, bodyPaint); y += 28f }
        y += 20f

        canvas.drawText("EDUCATIONAL BACKGROUND", MARGIN, y, headerPaint)
        y += 20f
        y = drawTable(
            canvas, MARGIN, y, DOC_WIDTH - 2 * MARGIN,
            listOf("LEVEL", "SCHOOL", "ADDRESS", "YEAR", "HONORS"),
            listOf(1.2f, 2f, 2f, 1f, 1.2f),
            data.educRows.map { listOf(it.level, it.school, it.address, it.year, it.honors) },
            black
        )
        y += 30f

        canvas.drawText("WORK EXPERIENCE", MARGIN, y, headerPaint)
        y += 20f
        y = drawTable(
            canvas, MARGIN, y, DOC_WIDTH - 2 * MARGIN,
            listOf("COMPANY", "POSITION", "DUTIES", "STARTED", "ENDED"),
            listOf(1.5f, 1.3f, 2f, 1f, 1f),
            data.workRows.filter { it.company.isNotBlank() || it.position.isNotBlank() }
                .map { listOf(it.company, it.position, it.duties, it.started, it.ended) },
            black
        )
        y += 30f

        canvas.drawText("SKILLS", MARGIN, y, headerPaint)
        y += 30f
        for (skill in data.skills.filter { it.isNotBlank() }) {
            canvas.drawText("•  $skill", MARGIN, y, bodyPaint)
            y += 30f
        }
        y += 10f

        canvas.drawText("TRAININGS / SEMINARS ATTENDED", MARGIN, y, headerPaint)
        y += 20f
        y = drawTable(
            canvas, MARGIN, y, DOC_WIDTH - 2 * MARGIN,
            listOf("TITLE", "SPONSOR/ORG", "DATE"),
            listOf(2f, 2f, 1.2f),
            data.trainingRows.filter { it.title.isNotBlank() }.map { listOf(it.title, it.sponsor, it.date) },
            black
        )
        y += 30f

        canvas.drawText("CHARACTER REFERENCES", MARGIN, y, headerPaint)
        y += 20f
        y = drawTable(
            canvas, MARGIN, y, DOC_WIDTH - 2 * MARGIN,
            listOf("NAME", "POSITION", "COMPANY", "CONTACT"),
            listOf(1.5f, 1.3f, 1.5f, 1.2f),
            data.referenceRows.filter { it.name.isNotBlank() }.map { listOf(it.name, it.position, it.company, it.contact) },
            black
        )
        y += 60f

        canvas.drawLine(MARGIN, y, MARGIN + 300f, y, black)
        canvas.drawLine(DOC_WIDTH - MARGIN - 300f, y, DOC_WIDTH - MARGIN, y, black)
        y += 26f
        val small = Paint(black).apply { textSize = 20f; textAlign = Paint.Align.CENTER }
        canvas.drawText("Signature", MARGIN + 150f, y, small)
        canvas.drawText("Date", DOC_WIDTH - MARGIN - 150f, y, small)

        return Bitmap.createBitmap(bitmap, 0, 0, DOC_WIDTH, minOf((y + 40f).toInt(), bitmap.height))
    }

    fun renderBioData(data: BioDataFull, photo: Bitmap?): Bitmap {
        val (bitmap, canvas) = canvasOf(1600)
        val black = Paint().apply { color = Color.BLACK; isAntiAlias = true }
        val title = Paint(black).apply { textSize = 60f; typeface = Typeface.DEFAULT_BOLD }
        canvas.drawText("BIO-DATA", MARGIN, 100f, title)

        val photoSize = 260f
        val photoLeft = DOC_WIDTH - MARGIN - photoSize
        val photoRect = RectF(photoLeft, 40f, photoLeft + photoSize, 40f + photoSize)
        val box = Paint(black).apply { style = Paint.Style.STROKE; strokeWidth = 3f }
        photo?.let {
            val scaled = Bitmap.createScaledBitmap(it, photoSize.toInt(), photoSize.toInt(), true)
            canvas.drawBitmap(scaled, photoLeft, 40f, null)
        }
        canvas.drawRect(photoRect, box)
val headerPaint = Paint(black).apply { textSize = 28f; typeface = Typeface.DEFAULT_BOLD }
        val labelBoldPaint = Paint(black).apply { textSize = 22f; typeface = Typeface.DEFAULT_BOLD }
        val valuePaint = Paint(black).apply { textSize = 22f }
        var y = 150f
        canvas.drawText("PERSONAL DATA", MARGIN, y, headerPaint)
        y += 34f

        fun row(vararg pairs: Pair<String, String>) {
            val colWidth = (DOC_WIDTH - 2 * MARGIN) / pairs.size
            pairs.forEachIndexed { i, (label, value) ->
                val x = MARGIN + i * colWidth
                val labelText = "$label: "
                canvas.drawText(labelText, x, y, labelBoldPaint)
                val labelWidth = labelBoldPaint.measureText(labelText)
                canvas.drawText(value, x + labelWidth, y, valuePaint)
            }
            y += 32f
        }


        row("Name" to data.fullName)
        row("Gender" to data.gender)
        row("Date of Birth" to data.birthDate)
        row("Current Address" to data.currentAddress)
        row("Permanent Address" to data.permanentAddress)
        row("Age" to data.age, "Date" to data.date)
        row("Occupation" to data.occupation, "Telephone" to data.telephone)
        row("Civil Status" to data.civilStatus, "Cellphone" to data.cellphone)
        row("Place of Birth" to data.placeOfBirth, "Email" to data.email)
        row("Height" to data.height, "Citizenship" to data.citizenship)
        row("Weight" to data.weight, "Religion" to data.religion)
        row("Father's Name" to data.fatherName, "Occupation" to data.fatherOccupation)
        row("Mother's Name" to data.motherName, "Occupation" to data.motherOccupation)
        row("Language/Dialect" to data.language)
        row("Person to Contact (Emergency)" to data.emergencyContact)
        row("Address" to data.emergencyAddress, "Contact No." to data.emergencyContactNo)

        y += 10f
        canvas.drawText("EDUCATIONAL BACKGROUND", MARGIN, y, headerPaint)
        y += 34f
        row("Elementary" to data.elementary, "Year Graduated" to data.elementaryYear)
        row("High School" to data.highSchool, "Year Graduated" to data.highSchoolYear)
        row("College" to data.college, "Year Graduated" to data.collegeYear)

        y += 40f
        val certPaint = Paint(black).apply { textSize = 20f }
        val certLines = listOf(
            "I here certify that the above information is true and correct to the best of my",
            "knowledge and belief. I also understand that any misinterpretation will be",
            "considered reason for withdrawal of an offer or subsequent dismissal if employed."
        )
        for (line in certLines) { canvas.drawText(line, MARGIN, y, certPaint); y += 28f }

        y += 70f
        canvas.drawLine(MARGIN, y, MARGIN + 300f, y, black)
        canvas.drawLine(DOC_WIDTH - MARGIN - 300f, y, DOC_WIDTH - MARGIN, y, black)
        y += 26f
        val small = Paint(black).apply { textSize = 20f; textAlign = Paint.Align.CENTER }
        canvas.drawText("Date", MARGIN + 150f, y, small)
        canvas.drawText("Signature", DOC_WIDTH - MARGIN - 150f, y, small)

        return bitmap
    }
}
