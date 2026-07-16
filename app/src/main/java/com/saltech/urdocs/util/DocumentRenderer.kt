package com.saltech.urdocs.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface

data class ResumeData(
    val fullName: String = "",
    val jobTitle: String = "",
    val address: String = "",
    val contactNumber: String = "",
    val email: String = "",
    val birthDate: String = "",
    val age: String = "",
    val civilStatus: String = "",
    val nationality: String = "",
    val objective: String = "",
    val education: String = "",
    val experience: String = "",
    val skills: String = "",
    val trainings: String = "",
    val references: String = ""
)

data class BioDataFull(
    val fullName: String = "",
    val gender: String = "",
    val birthDate: String = "",
    val currentAddress: String = "",
    val permanentAddress: String = "",
    val age: String = "",
    val date: String = "",
    val occupation: String = "",
    val telephone: String = "",
    val civilStatus: String = "",
    val cellphone: String = "",
    val placeOfBirth: String = "",
    val email: String = "",
    val height: String = "",
    val citizenship: String = "",
    val weight: String = "",
    val religion: String = "",
    val fatherName: String = "",
    val fatherOccupation: String = "",
    val motherName: String = "",
    val motherOccupation: String = "",
    val language: String = "",
    val emergencyContact: String = "",
    val emergencyAddress: String = "",
    val emergencyContactNo: String = "",
    val elementary: String = "",
    val elementaryYear: String = "",
    val highSchool: String = "",
    val highSchoolYear: String = "",
    val college: String = "",
    val collegeYear: String = ""
)

enum class ResumeStyle { MODERN, CORPORATE }

/**
 * Gumagawa ng "printable" na bitmap (A4-proportioned, naka-bond paper na
 * puti) ng Resume o Bio-Data, kasama ang 2x2 photo. Direktang Canvas
 * drawing -- reliable, walang experimental Compose API.
 */
object DocumentRenderer {

    private const val DOC_WIDTH = 1240
    private const val DOC_HEIGHT = 1754
    private const val MARGIN = 60f

    private fun blankCanvas(): Pair<Bitmap, Canvas> {
        val bitmap = Bitmap.createBitmap(DOC_WIDTH, DOC_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)
        return bitmap to canvas
    }

    private fun wrapText(paint: Paint, text: String, maxWidth: Float): List<String> {
        if (text.isBlank()) return listOf("")
        val lines = mutableListOf<String>()
        for (rawLine in text.split("\n")) {
            val words = rawLine.split(" ")
            var current = StringBuilder()
            for (word in words) {
                val test = if (current.isEmpty()) word else "$current $word"
                if (paint.measureText(test) > maxWidth && current.isNotEmpty()) {
                    lines.add(current.toString())
                    current = StringBuilder(word)
                } else {
                    current = StringBuilder(test)
                }
            }
            lines.add(current.toString())
        }
        return lines
    }

    fun renderBioData(data: BioDataFull, photo: Bitmap?): Bitmap {
        val (bitmap, canvas) = blankCanvas()
        val black = Paint().apply { color = Color.BLACK; isAntiAlias = true }
        val title = Paint(black).apply { textSize = 60f; typeface = Typeface.DEFAULT_BOLD }
        canvas.drawText("BIO-DATA", MARGIN, 100f, title)

        val photoSize = 260f
        val photoLeft = DOC_WIDTH - MARGIN - photoSize
        val photoTop = 40f
        val photoRect = RectF(photoLeft, photoTop, photoLeft + photoSize, photoTop + photoSize)
        val boxPaint = Paint(black).apply { style = Paint.Style.STROKE; strokeWidth = 3f }
        photo?.let {
            val scaled = Bitmap.createScaledBitmap(it, photoSize.toInt(), photoSize.toInt(), true)
            canvas.drawBitmap(scaled, photoLeft, photoTop, null)
        }
        canvas.drawRect(photoRect, boxPaint)

        val headerPaint = Paint(black).apply { textSize = 30f; typeface = Typeface.DEFAULT_BOLD }
        val labelPaint = Paint(black).apply { textSize = 22f }
        var y = 150f

        fun section(title: String) {
            canvas.drawText(title, MARGIN, y, headerPaint)
            y += 34f
        }
        fun row(vararg pairs: Pair<String, String>) {
            val colWidth = (DOC_WIDTH - 2 * MARGIN) / pairs.size
            pairs.forEachIndexed { i, (label, value) ->
                canvas.drawText("$label: $value", MARGIN + i * colWidth, y, labelPaint)
            }
            y += 32f
        }

        section("PERSONAL DATA")
        row("Name" to data.fullName)
        row("Gender" to data.gender, "Date" to data.date)
        row("Date of Birth" to data.birthDate, "Age" to data.age)
        row("Current Address" to data.currentAddress)
        row("Permanent Address" to data.permanentAddress)
        row("Occupation" to data.occupation, "Telephone" to data.telephone)
        row("Civil Status" to data.civilStatus, "Cellphone" to data.cellphone)
        row("Place of Birth" to data.placeOfBirth, "Email" to data.email)
        row("Height" to data.height, "Citizenship" to data.citizenship)
        row("Weight" to data.weight, "Religion" to data.religion)
        row("Father's Name" to data.fatherName, "Occupation" to data.fatherOccupation)
        row("Mother's Name" to data.motherName, "Occupation" to data.motherOccupation)
        row("Language/Dialect" to data.language)
        row("Emergency Contact" to data.emergencyContact)
        row("Emergency Address" to data.emergencyAddress, "Contact No." to data.emergencyContactNo)

        y += 12f
        section("EDUCATIONAL BACKGROUND")
        row("Elementary" to data.elementary, "Year Graduated" to data.elementaryYear)
        row("High School" to data.highSchool, "Year Graduated" to data.highSchoolYear)
        row("College" to data.college, "Year Graduated" to data.collegeYear)

        y += 40f
        val certPaint = Paint(black).apply { textSize = 22f }
        val certText = "I here certify that the above information is true and correct to the best of my knowledge and belief."
        for (line in wrapText(certPaint, certText, DOC_WIDTH - 2 * MARGIN)) {
            canvas.drawText(line, MARGIN, y, certPaint)
            y += 30f
        }

        y += 80f
        canvas.drawLine(MARGIN, y, MARGIN + 300f, y, black)
        canvas.drawLine(DOC_WIDTH - MARGIN - 300f, y, DOC_WIDTH - MARGIN, y, black)
        y += 28f
        val smallLabel = Paint(black).apply { textSize = 22f; textAlign = Paint.Align.CENTER }
        canvas.drawText("Date", MARGIN + 150f, y, smallLabel)
        canvas.drawText("Signature", DOC_WIDTH - MARGIN - 150f, y, smallLabel)

        return bitmap
    }

    fun renderResume(data: ResumeData, photo: Bitmap?, style: ResumeStyle): Bitmap =
        when (style) {
            ResumeStyle.CORPORATE -> renderCorporate(data, photo)
            ResumeStyle.MODERN -> renderModern(data, photo)
        }

    private fun renderCorporate(data: ResumeData, photo: Bitmap?): Bitmap {
        val (bitmap, canvas) = blankCanvas()
        val black = Paint().apply { color = Color.BLACK; isAntiAlias = true }

        val photoSize = 260f
        val photoLeft = MARGIN
        val photoTop = 40f
        val photoRect = RectF(photoLeft, photoTop, photoLeft + photoSize, photoTop + photoSize)
        val boxPaint = Paint(black).apply { style = Paint.Style.STROKE; strokeWidth = 3f }
        photo?.let {
            val scaled = Bitmap.createScaledBitmap(it, photoSize.toInt(), photoSize.toInt(), true)
            canvas.drawBitmap(scaled, photoLeft, photoTop, null)
        }
        canvas.drawRect(photoRect, boxPaint)

        val title = Paint(black).apply { textSize = 56f; typeface = Typeface.DEFAULT_BOLD; textAlign = Paint.Align.RIGHT }
        canvas.drawText("RESUME", DOC_WIDTH - MARGIN, 90f, title)

        val labelPaint = Paint(black).apply { textSize = 24f; typeface = Typeface.DEFAULT_BOLD }
        val valuePaint = Paint(black).apply { textSize = 24f }
        var y = 90f
        val textLeft = photoLeft + photoSize + 30f
        fun infoRow(label: String, value: String) {
            canvas.drawText(label, textLeft, y, labelPaint)
            canvas.drawText(value, textLeft + 220f, y, valuePaint)
            y += 34f
        }
        infoRow("FULL NAME:", data.fullName)
        infoRow("ADDRESS:", data.address)
        infoRow("CONTACT NUMBER:", data.contactNumber)
        infoRow("EMAIL ADDRESS:", data.email)
        infoRow("DATE OF BIRTH:", data.birthDate)
        infoRow("AGE:", data.age)
        infoRow("CIVIL STATUS:", data.civilStatus)
        infoRow("NATIONALITY:", data.nationality)

        y = maxOf(y, photoTop + photoSize) + 30f
        canvas.drawLine(MARGIN, y, DOC_WIDTH - MARGIN, y, black)
        y += 40f

        val headerPaint = Paint(black).apply { textSize = 28f; typeface = Typeface.DEFAULT_BOLD }
        val bodyPaint = Paint(black).apply { textSize = 24f }
        fun section(title: String, content: String) {
            canvas.drawText(title, MARGIN, y, headerPaint)
            y += 36f
            for (line in wrapText(bodyPaint, content, DOC_WIDTH - 2 * MARGIN)) {
                canvas.drawText(line, MARGIN, y, bodyPaint)
                y += 32f
            }
            y += 24f
        }
        section("CAREER OBJECTIVE", data.objective)
        section("EDUCATIONAL BACKGROUND", data.education)
        section("WORK EXPERIENCE", data.experience)
        section("SKILLS", data.skills)
        if (data.trainings.isNotBlank()) section("TRAININGS / SEMINARS", data.trainings)
        if (data.references.isNotBlank()) section("CHARACTER REFERENCES", data.references)

        return bitmap
    }

    private fun renderModern(data: ResumeData, photo: Bitmap?): Bitmap {
        val (bitmap, canvas) = blankCanvas()
        val black = Paint().apply { color = Color.BLACK; isAntiAlias = true }

        val namePaint = Paint(black).apply { textSize = 52f; typeface = Typeface.DEFAULT_BOLD }
        canvas.drawText(data.fullName.ifBlank { "IYONG PANGALAN" }.uppercase(), MARGIN, 100f, namePaint)

        val titlePaint = Paint(black).apply { textSize = 26f; typeface = Typeface.DEFAULT_BOLD; color = Color.DKGRAY }
        canvas.drawText(data.jobTitle.uppercase(), MARGIN, 138f, titlePaint)

        val contactPaint = Paint(black).apply { textSize = 22f }
        val contactLine = listOf(data.contactNumber, data.email, data.address)
            .filter { it.isNotBlank() }.joinToString("   |   ")
        canvas.drawText(contactLine, MARGIN, 176f, contactPaint)

        var y = 210f
        canvas.drawLine(MARGIN, y, DOC_WIDTH - MARGIN, y, black)
        y += 50f

        val photoSize = 240f
        val photoLeft = DOC_WIDTH - MARGIN - photoSize
        val photoTop = 40f
        photo?.let {
            val scaled = Bitmap.createScaledBitmap(it, photoSize.toInt(), photoSize.toInt(), true)
            canvas.drawBitmap(scaled, photoLeft, photoTop, null)
            val boxPaint = Paint(black).apply { style = Paint.Style.STROKE; strokeWidth = 3f }
            canvas.drawRect(RectF(photoLeft, photoTop, photoLeft + photoSize, photoTop + photoSize), boxPaint)
        }

        val headerPaint = Paint(black).apply { textSize = 30f; typeface = Typeface.DEFAULT_BOLD }
        val bodyPaint = Paint(black).apply { textSize = 24f }
        fun section(title: String, content: String) {
            if (content.isBlank()) return
            canvas.drawText(title, MARGIN, y, headerPaint)
            y += 38f
            for (line in wrapText(bodyPaint, content, DOC_WIDTH - 2 * MARGIN)) {
                canvas.drawText(line, MARGIN, y, bodyPaint)
                y += 34f
            }
            y += 26f
        }
        section("PROFILE", data.objective)
        section("WORK EXPERIENCE", data.experience)
        section("EDUCATION", data.education)
        section("SKILLS", data.skills)
        section("CERTIFICATIONS / TRAININGS", data.trainings)
        section("REFERENCES", data.references)

        return bitmap
    }
}
