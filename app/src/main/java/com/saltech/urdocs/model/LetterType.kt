package com.saltech.urdocs.model

enum class LetterType(val label: String) {
    LEAVE("Leave Letter"),
    EXCUSE("Excuse Letter"),
    RESIGNATION("Resignation Letter"),
    GOVT_SSS("SSS Letter/Request"),
    GOVT_PAGIBIG("Pag-IBIG Letter/Request"),
    APPLICATION("Application Letter"),
    AUTHORIZATION("Authorization Letter"),
    REFERRAL("Referral Letter"),
    FOLLOW_UP("Follow Up Letter"),
    THANK_YOU("Thank You Letter"),
    JOB_OFFER("Job Offer Letter"),
    SALARY_INCREASE("Salary Increase Request"),
    COMPLAINT("Complaint Letter"),
    BRGY_CITY_REQUEST("Brgy/City Request Letter"),
    SCHOLARSHIP("Scholarship Application Letter"),
    OJT_INTERNSHIP("OJT/Internship Letter"),
    OTHERS_REQUEST("Other Requests"),
    MEDICAL_AUTHORIZATION("Medical Authorization Letter"),
    AFFIDAVIT_LOSS("Affidavit of Loss"),
    MEDICAL_ASSISTANCE("Medical/Financial Assistance Request"),
    PARENTAL_CONSENT("Parental/Guardian Consent Letter"),
    JOBSEEKER_OATH("Oath of Undertaking (First-Time Jobseeker)"),
    AFFIDAVIT_DISCREPANCY("Affidavit of Discrepancy"),
    SPA("Special Power of Attorney"),
    DEMAND_LETTER("Demand Letter (Simple/Personal)"),
    AFFIDAVIT_DESISTANCE("Affidavit of Desistance"),
    AFFIDAVIT_TWO_PERSONS("Affidavit of Two Disinterested Persons"),
    CUSTOM("Custom / Iba pa")
}

data class LetterRequest(
    val type: LetterType,
    val fullName: String,
    val position: String = "",
    val company: String = "",
    val reason: String = "",
    val dateNeeded: String = "",
    val extraDetails: String = ""
)
