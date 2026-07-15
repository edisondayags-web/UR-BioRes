package com.saltech.urdocs.model

enum class LetterType(val label: String) {
    LEAVE("Leave Letter"),
    EXCUSE("Excuse Letter"),
    RESIGNATION("Resignation Letter"),
    GOVT_SSS("SSS Letter/Request"),
    GOVT_PAGIBIG("Pag-IBIG Letter/Request"),
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
