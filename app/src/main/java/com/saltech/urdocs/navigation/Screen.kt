package com.saltech.urdocs.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Resume : Screen("resume")
    object ResumeTraditional : Screen("resume_traditional")
    object ResumeChronological : Screen("resume_chronological")
    object ResumeHybrid : Screen("resume_hybrid")
    object ResumeMoreTemplates : Screen("resume_more_templates")
    object ResumeTemplateForm : Screen("resume_template_form/{templateName}") {
      fun createRoute(templateName: String) = "resume_template_form/$templateName"
    }
    object WebTemplate : Screen("web_template/{htmlFile}") {
      fun createRoute(htmlFile: String) = "web_template/$htmlFile"
    }
    object BioData : Screen("biodata")
    object BioDataStandard : Screen("biodata_standard")
    object BioDataPhForm : Screen("biodata_ph_form")
    object BioDataBlack : Screen("biodata_black")
    object BioDataBlue : Screen("biodata_blue")
    object BioDataMoreTemplates : Screen("biodata_more_templates")
    object BioDataTemplateForm : Screen("biodata_template_form/{templateName}") {
      fun createRoute(templateName: String) = "biodata_template_form/$templateName"
    }
    object GovtForms : Screen("govt_forms")
    object Letters : Screen("letters")
    object Interview : Screen("interview")
    object InterviewSession : Screen("interview_session/{mode}") {
      fun createRoute(mode: String) = "interview_session/$mode"
    }
    object JobResearcher : Screen("job_researcher")
    object DragDrop : Screen("drag_drop")
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
