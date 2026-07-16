package com.saltech.urdocs.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ResumeChoice : Screen("resume_choice")
    object Resume : Screen("resume/{style}") {
        fun createRoute(style: String) = "resume/$style"
    }
    object BioData : Screen("biodata")
    object GovtForms : Screen("govt_forms")
    object Letters : Screen("letters")
    object SelfieCapture : Screen("selfie_capture/{returnTo}") {
        fun createRoute(returnTo: String) = "selfie_capture/$returnTo"
    }
}
