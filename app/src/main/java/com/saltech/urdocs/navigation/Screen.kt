package com.saltech.urdocs.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Resume : Screen("resume")
    object ResumeTraditional : Screen("resume_traditional")
    object ResumeChronological : Screen("resume_chronological")
    object BioData : Screen("biodata")
    object GovtForms : Screen("govt_forms")
    object Letters : Screen("letters")
    object SelfieCapture : Screen("selfie_capture/{returnTo}") {
        fun createRoute(returnTo: String) = "selfie_capture/$returnTo"
    }
}
