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

        val authManager = AuthManager()
        lifecycleScope.launch {
            runCatching { authManager.ensureSignedIn() }
        }

        setContent {
            UrDocsTheme {
                val navController = rememberNavController()
                var pendingSelfieTarget by remember { mutableStateOf<String?>(null) }
                var resumeSelfie by remember { mutableStateOf<Bitmap?>(null) }
                var biodataSelfie by remember { mutableStateOf<Bitmap?>(null) }
                var hybridSelfie by remember { mutableStateOf<Bitmap?>(null) }
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
                                    "hybrid" -> navController.navigate(Screen.ResumeHybrid.route)
                                    else -> navController.navigate(Screen.ResumeChronological.route)
                                }
                            }
                        )
                    }
                    composable(Screen.ResumeTraditional.route) {
                        TraditionalResumeScreen(
                            processedSelfie = resumeSelfie,
                            onTakeSelfie = {
                                pendingSelfieTarget = "resume"
                                navController.navigate(Screen.SelfieCapture.createRoute("resume"))
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.ResumeChronological.route) {
                        ChronologicalResumeScreen()
                    }
                    composable(Screen.ResumeHybrid.route) {
                        HybridResumeScreen(
                            processedSelfie = hybridSelfie,
                            onTakeSelfie = {
                                pendingSelfieTarget = "resume_hybrid"
                                navController.navigate(Screen.SelfieCapture.createRoute("resume_hybrid"))
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.BioData.route) {
                        BioDataScreen(
                            processedSelfie = biodataSelfie,
                            onTakeSelfie = {
                                pendingSelfieTarget = "biodata"
                                navController.navigate(Screen.SelfieCapture.createRoute("biodata"))
                            }
                        )
                    }
                    composable(Screen.GovtForms.route) {
                        GovtFormsScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                    composable(Screen.Letters.route) {
                        LettersScreen(onNavigate = { route -> navController.navigate(route) })
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
                    composable(
                        route = Screen.SelfieCapture.route,
                        arguments = listOf(navArgument("returnTo") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val returnTo = backStackEntry.arguments?.getString("returnTo") ?: "resume"
                        SelfieCaptureScreen(
                            onProcessed = { bitmap ->
                                if (returnTo == "resume") resumeSelfie = bitmap
                                else if (returnTo == "resume_hybrid") hybridSelfie = bitmap
                                else biodataSelfie = bitmap
                                navController.popBackStack()
                            },
                            onCancel = { navController.popBackStack() }
                        )
                    }
                }
            } else {
                NoInternetScreen(onRetry = {})
            }
            }
        }
    }
}
