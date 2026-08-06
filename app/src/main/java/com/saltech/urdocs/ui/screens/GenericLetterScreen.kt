package com.saltech.urdocs.ui.screens

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Picture
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saltech.urdocs.R
import com.saltech.urdocs.model.LetterType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.viewinterop.AndroidView

private val GLBlue = Color(0xFF1D3FB5)

/** A single closing field, e.g. "Student Name" or "SSS/ID Number" — width scales to fit the label. */
private data class ClosingField(val label: String, val width: Dp = 150.dp)

/**
 * Title, subject, salutation, default body, closing line and closing fields — researched per
 * letter type so we don't force irrelevant fields like Employee ID onto a student's excuse letter.
 */
private data class LetterContent(
    val title: String,
    val subject: String,
    val salutation: String,
    val defaultBody: String,
    val closingLine: String,
    val closingFields: List<ClosingField>,
    val fileNamePrefix: String
)

private fun contentFor(type: LetterType): LetterContent = when (type) {

    // Researched: school excuse letters use student name + grade/section + parent signature,
    // never Employee ID or Department (student, not employee).
    LetterType.EXCUSE -> LetterContent(
        title = "EXCUSE LETTER",
        subject = "Excuse Letter",
        salutation = "Dear Ma'am/Sir,",
        defaultBody =
            "Please be informed that my son/daughter was not able to attend his/her\n" +
            "class(es) on the date(s) stated above, due to the reason indicated below.\n\n" +
            "I apologize for any inconvenience this may have caused and hope you will\n" +
            "understand the situation. Thank you very much for your consideration.",
        closingLine = "Respectfully yours,",
        closingFields = listOf(
            ClosingField("Parent/Guardian Name", 180.dp),
            ClosingField("Student Name", 180.dp),
            ClosingField("Grade & Section", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "Excuse"
    )

    // Researched: SSS/Pag-IBIG letters are personal transactions - full name + a valid ID
    // reference, not an employer's Employee ID/Department.
    LetterType.GOVT_SSS -> LetterContent(
        title = "SSS LETTER/REQUEST",
        subject = "SSS Request",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally request assistance regarding my SSS\n" +
            "records/account, specifically for the concern stated below.\n\n" +
            "I would greatly appreciate your help in processing this request at your\n" +
            "earliest convenience. Please let me know if any additional documents\n" +
            "or information are needed.\n\n" +
            "Thank you for your time and assistance.",
        closingLine = "Respectfully yours,",
        closingFields = listOf(
            ClosingField("Full Name", 180.dp),
            ClosingField("SSS Number", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "SSS"
    )

    LetterType.GOVT_PAGIBIG -> LetterContent(
        title = "PAG-IBIG LETTER/REQUEST",
        subject = "Pag-IBIG Request",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally request assistance regarding my Pag-IBIG\n" +
            "membership/account, specifically for the concern stated below.\n\n" +
            "I would greatly appreciate your help in processing this request at your\n" +
            "earliest convenience. Please let me know if any additional documents\n" +
            "or information are needed.\n\n" +
            "Thank you for your time and assistance.",
        closingLine = "Respectfully yours,",
        closingFields = listOf(
            ClosingField("Full Name", 180.dp),
            ClosingField("Pag-IBIG MID Number", 170.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "PagIBIG"
    )

    LetterType.APPLICATION -> LetterContent(
        title = "APPLICATION LETTER",
        subject = "Application",
        salutation = "Dear Ma'am/Sir,",
        defaultBody =
            "I am writing to formally apply for the position/opportunity stated\n" +
            "above. I believe that my skills and experience make me a strong\n" +
            "candidate, and I am eager to contribute to your organization.\n\n" +
            "I have attached my credentials for your review and would welcome\n" +
            "the opportunity to discuss my application further.\n\n" +
            "Thank you for considering my application.",
        closingLine = "Respectfully yours,",
        closingFields = listOf(
            ClosingField("Full Name", 180.dp),
            ClosingField("Contact Number", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "Application"
    )

    // Researched: authorization letters name the Principal AND the Representative, each with
    // their own valid ID reference - never Employee ID/Department.
    LetterType.AUTHORIZATION -> LetterContent(
        title = "AUTHORIZATION LETTER",
        subject = "Authorization",
        salutation = "To Whom It May Concern,",
        defaultBody =
            "I, the undersigned, hereby authorize the person named below to act on\n" +
            "my behalf regarding the matter stated in this letter, in the event that I\n" +
            "am unable to attend or process it personally.\n\n" +
            "A photocopy of both our valid IDs is attached for verification. I take full\n" +
            "responsibility for the actions taken by my representative on my behalf.\n\n" +
            "Note: This letter is valid for routine transactions only (e.g., claiming\n" +
            "documents, IDs, or packages). Transactions involving property, banking,\n" +
            "or legal matters require a notarized Special Power of Attorney instead.\n\n" +
            "Thank you for your assistance in this matter.",
        closingLine = "Sincerely,",
        closingFields = listOf(
            ClosingField("Principal's Name", 180.dp),
            ClosingField("Principal's Valid ID No.", 170.dp),
            ClosingField("Representative's Name", 180.dp),
            ClosingField("Representative's Valid ID No.", 170.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "Authorization"
    )

    LetterType.REFERRAL -> LetterContent(
        title = "REFERRAL LETTER",
        subject = "Referral",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally recommend the person named below for the\n" +
            "opportunity/position stated in this letter.\n\n" +
            "Based on my experience working with them, I am confident that they\n" +
            "will be a valuable addition and will perform their responsibilities well.\n\n" +
            "Please feel free to reach out should you need further information.",
        closingLine = "Sincerely,",
        closingFields = listOf(
            ClosingField("Referrer's Name", 180.dp),
            ClosingField("Position/Title", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "Referral"
    )

    LetterType.FOLLOW_UP -> LetterContent(
        title = "FOLLOW-UP LETTER",
        subject = "Follow-up",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to kindly follow up on my previous request/application\n" +
            "regarding the matter stated above.\n\n" +
            "I understand that you may be busy, and I appreciate your time. I would\n" +
            "just like to check on the status and see if there is any additional\n" +
            "information needed from my end.\n\n" +
            "Thank you for your attention to this matter.",
        closingLine = "Respectfully yours,",
        closingFields = listOf(
            ClosingField("Full Name", 180.dp),
            ClosingField("Contact Number", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "FollowUp"
    )

    LetterType.THANK_YOU -> LetterContent(
        title = "THANK YOU LETTER",
        subject = "Thank You",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to express my sincere gratitude for the opportunity,\n" +
            "support, or assistance you have given me.\n\n" +
            "Your kindness and generosity have meant a lot to me, and I truly\n" +
            "appreciate everything you have done.\n\n" +
            "Thank you once again from the bottom of my heart.",
        closingLine = "Sincerely,",
        closingFields = listOf(
            ClosingField("Full Name", 180.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "ThankYou"
    )

    // Job offer letters go FROM the employer TO the applicant - the signer is the
    // company representative, not the employee, so the fields reflect that.
    LetterType.JOB_OFFER -> LetterContent(
        title = "JOB OFFER LETTER",
        subject = "Job Offer",
        salutation = "Dear",
        defaultBody =
            "We are pleased to formally offer you the position stated above. We\n" +
            "were impressed by your qualifications and believe you will be a great\n" +
            "addition to our team.\n\n" +
            "Please review the details of this offer and let us know your decision\n" +
            "at your earliest convenience.\n\n" +
            "We look forward to having you on board.",
        closingLine = "Sincerely,",
        closingFields = listOf(
            ClosingField("Company Representative", 190.dp),
            ClosingField("Position/Title", 150.dp),
            ClosingField("Company Name", 170.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "JobOffer"
    )

    // This one IS a genuine employment matter, so Employee ID/Department are correct here.
    LetterType.SALARY_INCREASE -> LetterContent(
        title = "SALARY INCREASE REQUEST",
        subject = "Salary Increase Request",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally request a review of my current salary, given\n" +
            "my contributions and responsibilities since I started this position.\n\n" +
            "I believe this adjustment would fairly reflect my performance and\n" +
            "continued commitment to the company.\n\n" +
            "I would appreciate the opportunity to discuss this further at your\n" +
            "convenience.",
        closingLine = "Respectfully yours,",
        closingFields = listOf(
            ClosingField("Name", 160.dp),
            ClosingField("Employee ID", 130.dp),
            ClosingField("Department", 130.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "SalaryIncrease"
    )

    LetterType.COMPLAINT -> LetterContent(
        title = "COMPLAINT LETTER",
        subject = "Complaint",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally raise a concern regarding the matter stated\n" +
            "above.\n\n" +
            "I would appreciate it if this could be looked into and resolved at the\n" +
            "earliest possible time. Please let me know if you need any further\n" +
            "details from my end.\n\n" +
            "Thank you for your attention to this matter.",
        closingLine = "Respectfully yours,",
        closingFields = listOf(
            ClosingField("Full Name", 180.dp),
            ClosingField("Contact Number", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "Complaint"
    )

    LetterType.BRGY_CITY_REQUEST -> LetterContent(
        title = "BARANGAY/CITY REQUEST LETTER",
        subject = "Request Letter",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally request assistance regarding the matter\n" +
            "stated above, for our barangay/city.\n\n" +
            "I would greatly appreciate your kind consideration and support in\n" +
            "addressing this request at your earliest convenience.\n\n" +
            "Thank you for your time and assistance.",
        closingLine = "Respectfully yours,",
        closingFields = listOf(
            ClosingField("Full Name", 180.dp),
            ClosingField("Address", 220.dp),
            ClosingField("Valid ID No.", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "BrgyCity"
    )

    LetterType.SCHOLARSHIP -> LetterContent(
        title = "SCHOLARSHIP APPLICATION LETTER",
        subject = "Scholarship Application",
        salutation = "Dear Ma'am/Sir,",
        defaultBody =
            "I am writing to formally apply for the scholarship program stated\n" +
            "above. I believe that this opportunity would greatly help me continue\n" +
            "my studies and achieve my academic goals.\n\n" +
            "I have attached my credentials for your review and would be grateful\n" +
            "for the chance to be considered.\n\n" +
            "Thank you for your time and consideration.",
        closingLine = "Respectfully yours,",
        closingFields = listOf(
            ClosingField("Full Name", 180.dp),
            ClosingField("School", 170.dp),
            ClosingField("Student Number", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "Scholarship"
    )

    LetterType.OJT_INTERNSHIP -> LetterContent(
        title = "OJT/INTERNSHIP LETTER",
        subject = "OJT/Internship Application",
        salutation = "Dear Ma'am/Sir,",
        defaultBody =
            "I am writing to formally apply for an On-the-Job Training/Internship\n" +
            "opportunity at your company, as required by my course/program.\n\n" +
            "I am eager to apply my academic knowledge in a practical setting and\n" +
            "would welcome the opportunity to learn from your team.\n\n" +
            "Thank you for considering my application.",
        closingLine = "Respectfully yours,",
        closingFields = listOf(
            ClosingField("Student Name", 180.dp),
            ClosingField("School", 170.dp),
            ClosingField("Student Number", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "OJT"
    )

    LetterType.OTHERS_REQUEST -> LetterContent(
        title = "REQUEST LETTER",
        subject = "Request",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally request your assistance regarding the matter\n" +
            "stated above.\n\n" +
            "I would greatly appreciate your kind consideration and support at\n" +
            "your earliest convenience.\n\n" +
            "Thank you for your time and assistance.",
        closingLine = "Respectfully yours,",
        closingFields = listOf(
            ClosingField("Full Name", 180.dp),
            ClosingField("Contact Number", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "Request"
    )

    LetterType.CUSTOM -> LetterContent(
        title = "LETTER",
        subject = "Letter",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to formally address the matter stated above.\n\n" +
            "Please feel free to edit this letter to fit your specific needs.\n\n" +
            "Thank you for your time and consideration.",
        closingLine = "Sincerely,",
        closingFields = listOf(
            ClosingField("Full Name", 180.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "Custom"
    )

    // Researched: medical authorization needs patient info, grantor, authorized person,
    // relationship, and emergency contact - not Employee ID/Department.
    LetterType.MEDICAL_AUTHORIZATION -> LetterContent(
        title = "MEDICAL AUTHORIZATION LETTER",
        subject = "Medical Authorization",
        salutation = "To Whom It May Concern,",
        defaultBody =
            "I, the undersigned, hereby authorize the person named below to make\n" +
            "medical decisions and consent to treatment on behalf of the patient\n" +
            "named below, in the event that I am unavailable or unable to do so\n" +
            "myself.\n\n" +
            "This authorization covers routine and emergency medical care, including\n" +
            "consultations, procedures, and treatment as deemed necessary by the\n" +
            "attending physician.\n\n" +
            "Please contact me immediately using the information below regarding\n" +
            "any medical decisions concerning the patient.",
        closingLine = "Sincerely,",
        closingFields = listOf(
            ClosingField("Patient's Name", 180.dp),
            ClosingField("Grantor's Name (Parent/Guardian)", 220.dp),
            ClosingField("Authorized Person's Name", 190.dp),
            ClosingField("Relationship to Patient", 170.dp),
            ClosingField("Emergency Contact No.", 170.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "MedicalAuth"
    )

    // Researched: Affidavit of Loss is a sworn statement - needs affiant details + valid ID,
    // and MUST be notarized afterward (letter alone has no legal effect until notarized).
    LetterType.AFFIDAVIT_LOSS -> LetterContent(
        title = "AFFIDAVIT OF LOSS",
        subject = "Affidavit of Loss",
        salutation = "To Whom It May Concern,",
        defaultBody =
            "I, the undersigned, after having been duly sworn in accordance with law,\n" +
            "depose and state that:\n\n" +
            "1. I am the lawful owner/holder of the document/item described above;\n\n" +
            "2. Said document/item was lost under the circumstances stated above,\n" +
            "despite diligent efforts to locate it;\n\n" +
            "3. I am executing this affidavit to attest to the truth of the foregoing\n" +
            "and for whatever legal purpose it may serve, such as requesting a\n" +
            "replacement of the lost document/item.\n\n" +
            "Note: This affidavit must be signed before a notary public to be legally\n" +
            "valid. Please bring a valid government-issued ID when you have this\n" +
            "notarized.",
        closingLine = "Affiant,",
        closingFields = listOf(
            ClosingField("Affiant's Full Name", 190.dp),
            ClosingField("Valid ID No.", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "AffidavitOfLoss"
    )

    LetterType.MEDICAL_ASSISTANCE -> LetterContent(
        title = "MEDICAL/FINANCIAL ASSISTANCE REQUEST",
        subject = "Request for Medical/Financial Assistance",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "I am writing to respectfully request financial or medical assistance\n" +
            "for the hospitalization/treatment stated above.\n\n" +
            "This request is urgent due to the immediate medical needs involved,\n" +
            "and any support you can provide would greatly help ease this burden.\n" +
            "Supporting documents (medical certificate, hospital bill, etc.) are\n" +
            "attached for your reference.\n\n" +
            "Thank you for your kind consideration and prompt attention to this\n" +
            "request.",
        closingLine = "Respectfully yours,",
        closingFields = listOf(
            ClosingField("Full Name", 180.dp),
            ClosingField("Contact Number", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "MedicalAssistance"
    )

    LetterType.PARENTAL_CONSENT -> LetterContent(
        title = "PARENTAL/GUARDIAN CONSENT LETTER",
        subject = "Parental/Guardian Consent",
        salutation = "To Whom It May Concern,",
        defaultBody =
            "I, the undersigned parent/legal guardian, hereby give my full consent\n" +
            "and permission for my child/ward named below to travel/undergo the\n" +
            "activity stated above, accompanied by the person named below.\n\n" +
            "I take full responsibility for this decision and trust that my\n" +
            "child/ward will be well taken care of during this time.\n\n" +
            "Please contact me should you need any further confirmation.",
        closingLine = "Sincerely,",
        closingFields = listOf(
            ClosingField("Parent/Guardian Name", 190.dp),
            ClosingField("Child's/Ward's Name", 180.dp),
            ClosingField("Accompanying Adult", 170.dp),
            ClosingField("Contact Number", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "ParentalConsent"
    )

    LetterType.JOBSEEKER_OATH -> LetterContent(
        title = "OATH OF UNDERTAKING",
        subject = "First-Time Jobseeker Oath (RA 11261)",
        salutation = "To Whom It May Concern,",
        defaultBody =
            "I, the undersigned, being a first-time jobseeker availing of the benefits of\n" +
            "Republic Act No. 11261, otherwise known as the First-Time Jobseekers\n" +
            "Assistance Act, do hereby declare and undertake the following:\n\n" +
            "1. This is the first time that I will actively look for a job, and I am\n" +
            "requesting that a Barangay Certification be issued in my favor;\n\n" +
            "2. I understand that this benefit is valid for one (1) year from the date\n" +
            "of issuance, and may only be availed of once;\n\n" +
            "3. I will inform the Barangay once I am employed, and I will not use this\n" +
            "certification for any fraudulent purpose.\n\n" +
            "I am executing this oath voluntarily and in accordance with the Data\n" +
            "Privacy Act and other applicable laws.",
        closingLine = "Signed by,",
        closingFields = listOf(
            ClosingField("First-Time Jobseeker Name", 190.dp),
            ClosingField("Age", 90.dp),
            ClosingField("Barangay", 150.dp),
            ClosingField("Signature", 130.dp),
            ClosingField("Witnessed by (Brgy. Official)", 200.dp)
        ),
        fileNamePrefix = "JobseekerOath"
    )

    LetterType.AFFIDAVIT_DISCREPANCY -> LetterContent(
        title = "AFFIDAVIT OF DISCREPANCY",
        subject = "Affidavit of Discrepancy",
        salutation = "To Whom It May Concern,",
        defaultBody =
            "I, the undersigned, after having been duly sworn in accordance with law,\n" +
            "depose and state that:\n\n" +
            "1. My name appears differently across my documents/records, specifically\n" +
            "between the name/details stated above;\n\n" +
            "2. Said discrepancy was due to a clerical or typographical error, and\n" +
            "both names/details refer to one and the same person, myself;\n\n" +
            "3. I am executing this affidavit to attest to the truth of the foregoing\n" +
            "and for whatever legal purpose it may serve.\n\n" +
            "Note: This affidavit must be signed before a notary public to be legally\n" +
            "valid. Please bring a valid government-issued ID when you have this\n" +
            "notarized.",
        closingLine = "Affiant,",
        closingFields = listOf(
            ClosingField("Affiant's Full Name", 190.dp),
            ClosingField("Valid ID No.", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "AffidavitDiscrepancy"
    )

    LetterType.SPA -> LetterContent(
        title = "SPECIAL POWER OF ATTORNEY",
        subject = "Special Power of Attorney",
        salutation = "KNOW ALL MEN BY THESE PRESENTS:",
        defaultBody =
            "I, the Principal named above, of legal age, and a resident of the\n" +
            "address stated, do hereby name, constitute, and appoint the\n" +
            "Attorney-in-Fact named below to be my true and lawful representative,\n" +
            "for me and in my name, place, and stead, to do and perform the\n" +
            "specific act described above.\n\n" +
            "I hereby give and grant unto my said Attorney-in-Fact full power and\n" +
            "authority to do and perform every act necessary or proper to carry\n" +
            "out the foregoing, as fully as I might or could do if personally present.\n\n" +
            "Note: A Special Power of Attorney must be signed before a notary\n" +
            "public to be legally valid, especially for transactions involving real\n" +
            "property, banking, or other significant legal matters.",
        closingLine = "Principal,",
        closingFields = listOf(
            ClosingField("Principal's Name", 180.dp),
            ClosingField("Principal's Valid ID No.", 170.dp),
            ClosingField("Attorney-in-Fact's Name", 180.dp),
            ClosingField("Attorney-in-Fact's Valid ID No.", 180.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "SPA"
    )

    LetterType.DEMAND_LETTER -> LetterContent(
        title = "DEMAND LETTER",
        subject = "Formal Demand",
        salutation = "Dear Sir/Madam,",
        defaultBody =
            "This letter serves as formal demand regarding the matter stated above.\n\n" +
            "Despite previous attempts to settle this matter amicably, it remains\n" +
            "unresolved. I am therefore demanding that appropriate action be taken\n" +
            "within a reasonable period from receipt of this letter.\n\n" +
            "Should this matter remain unresolved, I may be constrained to pursue\n" +
            "further legal remedies available to me under the law.\n\n" +
            "I trust that this matter will be given your prompt and serious attention.",
        closingLine = "Sincerely,",
        closingFields = listOf(
            ClosingField("Full Name", 180.dp),
            ClosingField("Contact Number", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "DemandLetter"
    )

    LetterType.AFFIDAVIT_DESISTANCE -> LetterContent(
        title = "AFFIDAVIT OF DESISTANCE",
        subject = "Affidavit of Desistance",
        salutation = "To Whom It May Concern,",
        defaultBody =
            "I, the undersigned complainant, after having been duly sworn in\n" +
            "accordance with law, depose and state that:\n\n" +
            "1. I am the complainant in the case/matter stated above;\n\n" +
            "2. After careful consideration, I have voluntarily decided to withdraw\n" +
            "and desist from further pursuing this case/complaint;\n\n" +
            "3. This desistance is made freely and voluntarily, without any force,\n" +
            "threat, or intimidation from any party.\n\n" +
            "Note: This affidavit must be signed before a notary public to be legally\n" +
            "valid. Please bring a valid government-issued ID when you have this\n" +
            "notarized.",
        closingLine = "Affiant,",
        closingFields = listOf(
            ClosingField("Complainant's Full Name", 190.dp),
            ClosingField("Valid ID No.", 150.dp),
            ClosingField("Signature", 130.dp)
        ),
        fileNamePrefix = "AffidavitDesistance"
    )

    LetterType.AFFIDAVIT_TWO_PERSONS -> LetterContent(
        title = "AFFIDAVIT OF TWO DISINTERESTED PERSONS",
        subject = "Affidavit of Two Disinterested Persons",
        salutation = "To Whom It May Concern,",
        defaultBody =
            "We, the undersigned, both of legal age and residents of the addresses\n" +
            "stated below, after having been duly sworn in accordance with law,\n" +
            "depose and state that:\n\n" +
            "1. We personally know the person named above for a considerable\n" +
            "length of time;\n\n" +
            "2. We are executing this affidavit to attest to the truth of the matter\n" +
            "stated above, based on our personal knowledge;\n\n" +
            "3. We are not related to the said person by consanguinity or affinity,\n" +
            "and we are executing this affidavit voluntarily and without any\n" +
            "consideration.\n\n" +
            "Note: This affidavit must be signed before a notary public to be legally\n" +
            "valid. Both affiants must bring valid government-issued IDs when\n" +
            "having this notarized.",
        closingLine = "Affiants,",
        closingFields = listOf(
            ClosingField("First Affiant's Name", 180.dp),
            ClosingField("First Affiant's Valid ID No.", 170.dp),
            ClosingField("Second Affiant's Name", 180.dp),
            ClosingField("Second Affiant's Valid ID No.", 170.dp),
            ClosingField("Signatures", 130.dp)
        ),
        fileNamePrefix = "AffidavitTwoPersons"
    )

    else -> LetterContent(
        title = "LETTER",
        subject = "Letter",
        salutation = "Dear Sir/Madam,",
        defaultBody = "I am writing to formally address the matter stated above.",
        closingLine = "Sincerely,",
        closingFields = listOf(ClosingField("Full Name", 180.dp), ClosingField("Signature", 130.dp)),
        fileNamePrefix = "Letter"
    )
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun GenericLetterScreen(letterType: LetterType, onBack: () -> Unit = {}) {
    val content = remember(letterType) { contentFor(letterType) }
    val paperWidthDp = 850.dp
    val paperHeightDp = 1600.dp
    val context = LocalContext.current
    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    LaunchedEffect(Unit) {
        InterstitialAd.load(
            context,
            "ca-app-pub-3134240485602899/5274307709",
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                }
            }
        )
    }

    var dateVal by remember { mutableStateOf("") }
    var to1Val by remember { mutableStateOf("") }
    var to2Val by remember { mutableStateOf("") }
    var subjectVal by remember(letterType) { mutableStateOf(content.subject) }
    var salutationVal by remember(letterType) { mutableStateOf(content.salutation) }
    var closingVal by remember(letterType) { mutableStateOf(content.closingLine) }

    // Dynamic closing field values, keyed by label - each letter type only shows the fields
    // it actually needs (see closingFields above).
    val fieldValues = remember(letterType) { mutableStateMapOf<String, String>() }

    var offset by remember { mutableStateOf(Offset.Zero) }
    val picture = remember { Picture() }
    val coroutineScope = rememberCoroutineScope()

    var isEditMode by remember { mutableStateOf(false) }
    var bodyText by remember(letterType) { mutableStateOf(content.defaultBody) }

    var isPlainMode by remember { mutableStateOf(false) }

    val textColor = if (isPlainMode) Color.Black else GLBlue
    val bodyFontFamily = if (isPlainMode) FontFamily.Default else FontFamily.Serif
    val bodyFontStyle = if (isPlainMode) FontStyle.Normal else FontStyle.Italic

    Column(modifier = Modifier.fillMaxSize()) {
        IconButton(onClick = onBack) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }
        AndroidView(
            factory = { ctx ->
                AdView(ctx).apply {
                    val displayMetrics = ctx.resources.displayMetrics
                    val adWidthPixels = displayMetrics.widthPixels.toFloat()
                    val adDensity = displayMetrics.density
                    val adWidth = (adWidthPixels / adDensity).toInt()
                    setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(ctx, adWidth))
                    adUnitId = "ca-app-pub-3134240485602899/5923255956"
                    loadAd(AdRequest.Builder().build())
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        BoxWithConstraints(modifier = Modifier.weight(1f).background(Color.Black)) {
            val fitScale = minOf(maxWidth / paperWidthDp, maxHeight / paperHeightDp)
            var scale by remember { mutableStateOf(fitScale) }

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer(
                        scaleX = scale, scaleY = scale,
                        translationX = offset.x, translationY = offset.y
                    )
                    .requiredWidth(paperWidthDp)
                    .requiredHeight(paperHeightDp)
                    .drawWithCache {
                        val w = size.width.toInt().coerceAtLeast(1)
                        val h = size.height.toInt().coerceAtLeast(1)
                        onDrawWithContent {
                            if (isEditMode) {
                                this@onDrawWithContent.drawContent()
                            } else {
                                val pictureCanvas = androidx.compose.ui.graphics.Canvas(picture.beginRecording(w, h))
                                draw(this, this.layoutDirection, pictureCanvas, this.size) {
                                    this@onDrawWithContent.drawContent()
                                }
                                picture.endRecording()
                                drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
                            }
                        }
                    }
                    .background(Color.White)
            ) {
                if (!isPlainMode) {
                    Image(
                        painter = painterResource(R.drawable.bond_paper_blank),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 70.dp, vertical = 55.dp)
                ) {
                    Spacer(Modifier.height(60.dp))

                    if (isPlainMode) {
                        Text(
                            text = content.title,
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            letterSpacing = 1.sp,
                            color = textColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            buildAnnotatedString {
                                content.title.forEachIndexed { i, c ->
                                    val isFirstOfWord = i == 0 || content.title[i - 1] == ' '
                                    withStyle(SpanStyle(fontSize = if (isFirstOfWord) 44.sp else 30.sp)) {
                                        append(c)
                                    }
                                }
                            },
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(Modifier.height(20.dp))
                    Spacer(Modifier.weight(1f))

                    GLField("Date", dateVal, fieldWidth = 130.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { dateVal = it }

                    Spacer(Modifier.height(35.dp))

                    GLPlainText("To,", color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    Spacer(Modifier.height(10.dp))
                    GLUnderline(to1Val, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { to1Val = it }
                    Spacer(Modifier.height(10.dp))
                    GLUnderline(to2Val, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { to2Val = it }

                    Spacer(Modifier.height(16.dp))

                    FlowRow(modifier = Modifier.fillMaxWidth()) {
                        GLWord("Subject:", bold = true, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                        GLInlineField(subjectVal, 220.dp, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not()) { subjectVal = it }
                    }

                    Spacer(Modifier.height(14.dp))

                    GLEditableText(salutationVal, { salutationVal = it }, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())

                    Spacer(Modifier.height(16.dp))

                    if (isEditMode) {
                        BasicTextField(
                            value = bodyText,
                            onValueChange = { bodyText = it },
                            textStyle = TextStyle(
                                fontFamily = bodyFontFamily,
                                fontStyle = bodyFontStyle,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp,
                                color = textColor,
                                textAlign = TextAlign.Justify,
                                lineHeight = 27.sp
                            ),
                            cursorBrush = SolidColor(textColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFEFF3FF))
                                .padding(8.dp)
                        )
                    } else {
                        GLParagraph(bodyText, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    }

                    Spacer(Modifier.height(45.dp))

                    Box(modifier = Modifier.padding(start = 60.dp)) {
                        GLEditableText(closingVal, { closingVal = it }, color = textColor, fontFamily = bodyFontFamily, italic = isPlainMode.not())
                    }

                    Spacer(Modifier.height(50.dp))

                    // Only the fields this specific letter type actually needs.
                    Column(modifier = Modifier.padding(start = 100.dp)) {
                        content.closingFields.forEachIndexed { index, field ->
                            GLField(
                                field.label,
                                fieldValues[field.label] ?: "",
                                fieldWidth = field.width,
                                color = textColor,
                                fontFamily = bodyFontFamily,
                                italic = isPlainMode.not()
                            ) { fieldValues[field.label] = it }
                            if (index != content.closingFields.lastIndex) {
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }

                    Spacer(Modifier.weight(1f))
                }
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { isEditMode = !isEditMode },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = GLBlue)
                ) {
                    Text(if (isEditMode) "Done" else "Edit", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        isEditMode = false
                        scale = fitScale
                        offset = Offset.Zero
                        val activity = context as? android.app.Activity
                        fun proceedDownload() {
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
                                saveGenericLetterToGallery(context, bitmap, content.fileNamePrefix)
                            }
                        }
                        if (activity != null && interstitialAd != null) {
                            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    interstitialAd = null
                                    proceedDownload()
                                }
                            }
                            interstitialAd?.show(activity)
                        } else {
                            proceedDownload()
                        }
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = GLBlue)
                ) {
                    Text("Download", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { isPlainMode = !isPlainMode },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = if (isPlainMode) Color.DarkGray else GLBlue
                    )
                ) {
                    Text(if (isPlainMode) "Design" else "Plain", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ---------- Reusable pieces ----------

@Composable
private fun GLPlainText(
    text: String,
    bold: Boolean = false,
    color: Color = GLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true
) {
    Text(
        text,
        fontFamily = fontFamily,
        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        fontSize = 16.sp,
        color = color,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

@Composable
private fun GLEditableText(
    value: String,
    onChange: (String) -> Unit,
    bold: Boolean = false,
    color: Color = GLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    BasicTextField(
        value = value, onValueChange = onChange,
        textStyle = TextStyle(
            fontFamily = fontFamily,
            fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp,
            color = color
        ),
        cursorBrush = SolidColor(color),
        interactionSource = interactionSource,
        modifier = Modifier
            .background(if (isFocused) Color(0xFFEFF3FF) else Color.Transparent)
            .padding(vertical = 2.dp)
    )
}

@Composable
private fun GLParagraph(
    text: String,
    color: Color = GLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true
) {
    Text(
        text,
        fontFamily = fontFamily,
        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        color = color,
        textAlign = TextAlign.Justify,
        lineHeight = 27.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun GLWord(
    word: String,
    bold: Boolean = false,
    color: Color = GLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true
) {
    Text(
        "$word ",
        fontFamily = fontFamily,
        fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Medium,
        fontSize = 18.sp,
        color = color
    )
}

@Composable
private fun GLInlineField(
    value: String,
    width: Dp,
    color: Color = GLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true,
    onChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    Box(
        modifier = Modifier
            .padding(end = 4.dp)
            .background(if (isFocused) Color(0xFFEFF3FF) else Color.Transparent)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    val y = size.height - 2.dp.toPx()
                    drawLine(color, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
                }
            }
    ) {
        BasicTextField(
            value = value, onValueChange = onChange,
            textStyle = TextStyle(
                fontFamily = fontFamily,
                fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
                fontSize = 20.sp,
                color = color
            ),
            cursorBrush = SolidColor(color),
            interactionSource = interactionSource,
            modifier = Modifier.widthIn(min = width)
        )
    }
}

@Composable
private fun GLUnderline(
    value: String,
    color: Color = GLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true,
    onChange: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    BasicTextField(
        value = value, onValueChange = onChange,
        textStyle = TextStyle(
            fontFamily = fontFamily,
            fontStyle = if (italic) FontStyle.Italic else FontStyle.Normal,
            fontSize = 15.sp,
            color = color
        ),
        cursorBrush = SolidColor(color),
        interactionSource = interactionSource,
        modifier = Modifier
            .width(400.dp)
            .background(if (isFocused) Color(0xFFEFF3FF) else Color.Transparent)
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    val y = size.height - 2.dp.toPx()
                    drawLine(color, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.dp.toPx())
                }
            }
    )
}

@Composable
private fun GLField(
    label: String,
    value: String,
    fieldWidth: Dp,
    color: Color = GLBlue,
    fontFamily: FontFamily = FontFamily.Serif,
    italic: Boolean = true,
    onChange: (String) -> Unit
) {
    Row(verticalAlignment = Alignment.Bottom) {
        GLPlainText("$label: ", bold = true, color = color, fontFamily = fontFamily, italic = italic)
        GLInlineField(value, fieldWidth, color = color, fontFamily = fontFamily, italic = italic, onChange = onChange)
    }
}

private fun saveGenericLetterToGallery(context: android.content.Context, bitmap: Bitmap, prefix: String) {
    val filename = "${prefix}_${System.currentTimeMillis()}.png"
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
}
