package com.saltech.urdocs.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Resume : Screen("resume")
    object ResumeTraditional : Screen("resume_traditional")
    object ResumeChronological : Screen("resume_chronological")
    object BioData : Screen("biodata")
    object GovtForms : Screen("govt_forms")
    object Letters : Screen("letters")
    object JobResearcher : Screen("job_researcher")
    object Settings : Screen("settings")
    object MyProfile : Screen("my_profile")
    object PrivacyPolicy : Screen("privacy_policy")
    object TermsConditions : Screen("terms_conditions")
    object DataPermissions : Screen("data_permissions")
    object AboutDeveloper : Screen("about_developer")
    object SelfieCapture : Screen("selfie_capture/{returnTo}") {
      fun createRoute(returnTo: String) = "selfie_capture/$returnTo"
    }
    object LetterAssistant : Screen("letter_assistant/{letterType}") {
      fun createRoute(letterType: String) = "letter_assistant/$letterType"
    }
}
