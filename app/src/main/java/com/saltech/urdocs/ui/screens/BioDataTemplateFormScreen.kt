package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

data class BioDataTemplateFields(
    val fullName: String = "",
    val dob: String = "",
    val placeOfBirth: String = "",
    val civilStatus: String = "",
    val nationality: String = "",
    val religion: String = "",
    val contactNo: String = "",
    val email: String = "",
    val currentAddress: String = "",
    val height: String = "",
    val weight: String = "",
    val avatarUri: String = "",
    val edu1Level: String = "", val edu1School: String = "", val edu1Year: String = "",
    val edu2Level: String = "", val edu2School: String = "", val edu2Year: String = "",
    val edu3Level: String = "", val edu3School: String = "", val edu3Year: String = "",
    val work1Company: String = "", val work1Position: String = "", val work1Dates: String = "",
    val work2Company: String = "", val work2Position: String = "", val work2Dates: String = "",
    val work3Company: String = "", val work3Position: String = "", val work3Dates: String = "",
    val skill1: String = "", val skill2: String = "", val skill3: String = "",
    val skill4: String = "", val skill5: String = "", val skill6: String = "",
    val ref1: String = "", val ref2: String = "", val ref3: String = ""
)

@Composable
fun BioDataTemplateFormScreen(
    templateName: String,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var data by remember { mutableStateOf(BioDataTemplateFields()) }
    val templateNum = templateName.filter { it.isDigit() }.trimStart('0').ifEmpty { "1" }.toIntOrNull() ?: 1

    fun update(field: String, value: String) {
        data = when (field) {
            "fullName" -> data.copy(fullName = value)
            "dateOfBirth" -> data.copy(dob = value)
            "placeOfBirth" -> data.copy(placeOfBirth = value)
            "civilStatus" -> data.copy(civilStatus = value)
            "citizenship" -> data.copy(nationality = value)
            "religion" -> data.copy(religion = value)
            "contactNo" -> data.copy(contactNo = value)
            "email" -> data.copy(email = value)
            "currentAddress" -> data.copy(currentAddress = value)
            "height" -> data.copy(height = value)
            "weight" -> data.copy(weight = value)
            "avatarUri" -> data.copy(avatarUri = value)
            "edu1Level" -> data.copy(edu1Level = value)
            "edu1School" -> data.copy(edu1School = value)
            "edu1Year" -> data.copy(edu1Year = value)
            "edu2Level" -> data.copy(edu2Level = value)
            "edu2School" -> data.copy(edu2School = value)
            "edu2Year" -> data.copy(edu2Year = value)
            "edu3Level" -> data.copy(edu3Level = value)
            "edu3School" -> data.copy(edu3School = value)
            "edu3Year" -> data.copy(edu3Year = value)
            "work1Company" -> data.copy(work1Company = value)
            "work1Position" -> data.copy(work1Position = value)
            "work1Dates" -> data.copy(work1Dates = value)
            "work2Company" -> data.copy(work2Company = value)
            "work2Position" -> data.copy(work2Position = value)
            "work2Dates" -> data.copy(work2Dates = value)
            "work3Company" -> data.copy(work3Company = value)
            "work3Position" -> data.copy(work3Position = value)
            "work3Dates" -> data.copy(work3Dates = value)
            "skill1" -> data.copy(skill1 = value)
            "skill2" -> data.copy(skill2 = value)
            "skill3" -> data.copy(skill3 = value)
            "skill4" -> data.copy(skill4 = value)
            "skill5" -> data.copy(skill5 = value)
            "skill6" -> data.copy(skill6 = value)
            "ref1" -> data.copy(ref1 = value)
            "ref2" -> data.copy(ref2 = value)
            "ref3" -> data.copy(ref3 = value)
            else -> data
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B1530))
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        val onFieldChange: (String, String) -> Unit = { field, value -> update(field, value) }
        when (templateNum) {
            1 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate01_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            2 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate02_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            3 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate03_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            4 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate04_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            5 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate05_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            6 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate06_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            7 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate07_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            8 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate08_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            9 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate09_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            10 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate10_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            11 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate11_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            12 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate12_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            13 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate13_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            14 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate14_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            15 -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate15_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
            else -> com.saltech.urdocs.ui.templates.biodata.BiodataTemplate16_PixelPerfect(
                fullName = data.fullName, dateOfBirth = data.dob, placeOfBirth = data.placeOfBirth,
                civilStatus = data.civilStatus, religion = data.religion, citizenship = data.nationality,
                height = data.height, weight = data.weight, email = data.email, contactNo = data.contactNo,
                currentAddress = data.currentAddress, avatarUri = data.avatarUri,
                edu1Level = data.edu1Level, edu1School = data.edu1School, edu1Year = data.edu1Year,
                edu2Level = data.edu2Level, edu2School = data.edu2School, edu2Year = data.edu2Year,
                edu3Level = data.edu3Level, edu3School = data.edu3School, edu3Year = data.edu3Year,
                work1Company = data.work1Company, work1Position = data.work1Position, work1Dates = data.work1Dates,
                work2Company = data.work2Company, work2Position = data.work2Position, work2Dates = data.work2Dates,
                work3Company = data.work3Company, work3Position = data.work3Position, work3Dates = data.work3Dates,
                skill1 = data.skill1, skill2 = data.skill2, skill3 = data.skill3,
                skill4 = data.skill4, skill5 = data.skill5, skill6 = data.skill6,
                ref1 = data.ref1, ref2 = data.ref2, ref3 = data.ref3,
                onFieldChange = onFieldChange
            )
        }
    }
}
