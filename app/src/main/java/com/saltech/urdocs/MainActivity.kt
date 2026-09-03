package com.saltech.urdocs
import com.saltech.urdocs.util.rememberConnectivityState

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.lifecycleScope
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.fillMaxSize
import com.saltech.urdocs.data.AuthManager
import com.saltech.urdocs.navigation.Screen
import com.saltech.urdocs.ui.screens.*
import com.saltech.urdocs.ui.theme.UrDocsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            val trace = android.util.Log.getStackTraceString(throwable)
            val intent = android.content.Intent(this, CrashActivity::class.java).apply {
                putExtra("error", trace)
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            android.os.Process.killProcess(android.os.Process.myPid())
        }
        val authManager = AuthManager()
        lifecycleScope.launch {
            runCatching { authManager.ensureSignedIn() }
        }
        setContent {
            UrDocsTheme {
                val navController = rememberNavController()
                var resumeSelfie by remember { mutableStateOf<Bitmap?>(null) }
                var biodataSelfie by remember { mutableStateOf<Bitmap?>(null) }
                val isConnected by rememberConnectivityState()

                if (isConnected) {
                NavHost(navController = navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) {
                        HomeScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                    composable(Screen.Resume.route) {
                        ResumeChoiceScreen(
                            onChoose = { choice ->
                                when (choice) {
                                    "traditional" -> navController.navigate(Screen.ResumeTraditional.route)
                                    "ai_html" -> navController.navigate(Screen.HtmlTemplateGallery.route)
                                    "gallery" -> navController.navigate(Screen.ResumeMoreTemplates.route)
                                    else -> navController.navigate(Screen.ResumeChronologicalPicker.route)
                                }
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.ResumeTraditional.route) {
                        TraditionalResumeScreen(
                            processedSelfie = resumeSelfie,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.ResumeChronological.route) {
                        ChronologicalResumeScreen()
                    }
                    composable(Screen.ResumeChronologicalPicker.route) {
                        ChronologicalTemplatePickerScreen(
                            onTemplateSelected = { choice ->
                                when (choice) {
                                    1 -> navController.navigate(Screen.ResumeChronological.route)
                                    2 -> navController.navigate(Screen.ResumeChronological2.route)
                                    else -> navController.navigate(Screen.ResumeChronological3.route)
                                }
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.ResumeChronological2.route) {
                        ChronologicalResumeScreen2()
                    }
                    composable(Screen.ResumeChronological3.route) {
                        ChronologicalResumeScreen3()
                    }
                    composable(Screen.ResumeMoreTemplates.route) {
                        ResumeTemplateGalleryScreen(
                            onTemplateSelected = { templateName ->
                                if (templateName.endsWith(".html")) {
                                    navController.navigate(Screen.AiTemplate.createRoute(templateName))
                                } else {
                                    navController.navigate(Screen.ResumeTemplateForm.createRoute(templateName))
                                }
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.ResumeTemplateGallery.route) {
                        ResumeTemplateGalleryScreen(
                            onTemplateSelected = { templateName ->
                                if (templateName.endsWith(".html")) {
                                    navController.navigate(Screen.AiTemplate.createRoute(templateName))
                                } else {
                                    navController.navigate(Screen.ResumeTemplateForm.createRoute(templateName))
                                }
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.HtmlTemplateGallery.route) {
                        com.saltech.urdocs.ui.screens.HtmlTemplateGalleryScreen(
                            onTemplateSelected = { fileName ->
                                navController.navigate(Screen.AiTemplate.createRoute(fileName))
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(
                        Screen.AiTemplate.route,
                        arguments = listOf(navArgument("htmlFile") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val htmlFile = backStackEntry.arguments?.getString("htmlFile") ?: ""
                        com.saltech.urdocs.ui.screens.AiTemplateScreen(
                            htmlFileName = htmlFile,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(
                        Screen.ResumeTemplateForm.route,
                        arguments = listOf(navArgument("templateName") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val templateName = backStackEntry.arguments?.getString("templateName") ?: ""
                        ResumeTemplateFormScreen(
                            templateName = templateName,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.BioData.route) {
                        BiodataChoiceScreen(
                            onChoose = { choice ->
                                when (choice) {
                                    "ph_form" -> navController.navigate(Screen.BioDataPhForm.route)
                                    "more_templates" -> navController.navigate(Screen.BioDataMoreTemplates.route)
                                    else -> navController.navigate(Screen.BioDataStandard.route)
                                }
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.BioDataStandard.route) {
                        BioDataPhFormScreen(
                            processedSelfie = biodataSelfie
                        )
                    }
                    composable(Screen.BioDataPhForm.route) {
                        BioDataPhFormScreen(
                            processedSelfie = biodataSelfie
                        )
                    }
                    composable(Screen.BioDataBlack.route) {
                        BioDataV2Screen(
                            isBlack = true,
                            processedSelfie = biodataSelfie
                        )
                    }
                    composable(Screen.BioDataBlue.route) {
                        BioDataV2Screen(
                            isBlack = false,
                            processedSelfie = biodataSelfie
                        )
                    }
                    composable(Screen.BioDataMoreTemplates.route) {
                        BioDataMoreTemplatesScreen(
                            onTemplateSelected = { templateName ->
                                navController.navigate(Screen.BioDataTemplateForm.createRoute(templateName))
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(
                        Screen.BioDataTemplateForm.route,
                        arguments = listOf(navArgument("templateName") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val templateName = backStackEntry.arguments?.getString("templateName") ?: ""
                        BioDataTemplateFormScreen(
                            templateName = templateName,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.GovtForms.route) {
                        GovtFormsScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                    composable(Screen.Letters.route) {
                        LettersScreen(
                            onNavigate = { route -> navController.navigate(route) },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(
                        route = Screen.LetterAssistant.route,
                        arguments = listOf(navArgument("letterType") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val typeArg = backStackEntry.arguments?.getString("letterType") ?: "CUSTOM"
                        val letterType = com.saltech.urdocs.model.LetterType.entries.firstOrNull { it.name == typeArg }
                            ?: com.saltech.urdocs.model.LetterType.CUSTOM
                        LetterAssistantScreen(
                            letterType = letterType,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.Interview.route) {
                        InterviewScreen(
                            onBack = { navController.popBackStack() },
                            onSelect = { mode -> navController.navigate(Screen.InterviewSession.createRoute(mode)) }
                        )
                    }
                    composable(
                        route = Screen.InterviewSession.route,
                        arguments = listOf(navArgument("mode") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val mode = backStackEntry.arguments?.getString("mode") ?: "local_traditional"
                        InterviewSessionScreen(
                            mode = mode,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.JobResearcher.route) {
                        JobResearcherScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.Settings.route) {
                        SettingsScreen(
                            onBack = { navController.popBackStack() },
                            onNavigate = { route -> navController.navigate(route) }
                        )
                    }
                    composable(Screen.MyProfile.route) {
                        MyProfileScreen(onBack = { navController.popBackStack() })
                    }
                    composable(Screen.PrivacyPolicy.route) {
                        PrivacyPolicyScreen(onBack = { navController.popBackStack() })
                    }
                    composable(Screen.TermsConditions.route) {
                        TermsConditionsScreen(onBack = { navController.popBackStack() })
                    }
                    composable(Screen.DataPermissions.route) {
                        DataPermissionsScreen(onBack = { navController.popBackStack() })
                    }
                    composable(Screen.AboutDeveloper.route) {
                        AboutDeveloperScreen(onBack = { navController.popBackStack() })
                    }
                }
            } else {
                NoInternetScreen(onRetry = {})
            }
            }
        }
    }
}
