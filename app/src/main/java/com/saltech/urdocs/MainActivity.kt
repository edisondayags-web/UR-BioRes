package com.saltech.urdocs

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

                NavHost(navController = navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) {
                        HomeScreen(onNavigate = { route -> navController.navigate(route) })
                    }
                    composable(Screen.Resume.route) {
                        ResumeScreen(
                            processedSelfie = resumeSelfie,
                            onTakeSelfie = {
                                pendingSelfieTarget = "resume"
                                navController.navigate(Screen.SelfieCapture.createRoute("resume"))
                            }
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
                        LettersScreen()
                    }
                    composable(
                        route = Screen.SelfieCapture.route,
                        arguments = listOf(navArgument("returnTo") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val returnTo = backStackEntry.arguments?.getString("returnTo") ?: "resume"
                        SelfieCaptureScreen(
                            onProcessed = { bitmap ->
                                if (returnTo == "resume") resumeSelfie = bitmap
                                else biodataSelfie = bitmap
                                navController.popBackStack()
                            },
                            onCancel = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
