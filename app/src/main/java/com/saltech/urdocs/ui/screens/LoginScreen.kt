package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.saltech.urdocs.data.AuthManager
import kotlinx.coroutines.launch

private val LBlue = Color(0xFF4C8DFF)
private val LGray = Color(0xFF9A9A9A)

@Composable
fun LoginScreen(onBack: () -> Unit, onDone: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val auth = remember { AuthManager() }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    fun googleLogin() {
        val resId = context.resources.getIdentifier("default_web_client_id", "string", context.packageName)
        if (resId == 0) {
            message = "Wala pang Google setup sa app (google-services.json)."
            return
        }
        val webClientId = context.getString(resId)
        scope.launch {
            loading = true
            message = null
            try {
                val option = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(webClientId)
                    .build()
                val request = GetCredentialRequest.Builder().addCredentialOption(option).build()
                val result = CredentialManager.create(context).getCredential(context, request)
                val cred = result.credential
                if (cred is CustomCredential && cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val token = GoogleIdTokenCredential.createFrom(cred.data).idToken
                    auth.signInWithGoogleIdToken(token)
                    onDone()
                } else {
                    message = "Hindi natuloy ang login."
                }
            } catch (e: GetCredentialCancellationException) {
                // kinansel ng user
            } catch (e: NoCredentialException) {
                message = "Walang Google account sa selpon. Mag-add muna sa Settings."
            } catch (e: Exception) {
                message = "Hindi natuloy: ${e.localizedMessage ?: "subukan ulit"}"
            }
            loading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        PremiumWaveBackground()

        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 8.dp)
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.06f))
                .border(1.dp, Brush.verticalGradient(listOf(LBlue, Color.Black)), RoundedCornerShape(12.dp))
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = LBlue)
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("UR Docs", color = Color.White, fontSize = 34.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Mag-login para ma-save ang mga gawa mo 🩵",
                color = LGray, fontSize = 14.sp, textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            message?.let {
                Text(it, color = Color(0xFFFF7A8A), fontSize = 13.sp, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .clickable(enabled = !loading) { googleLogin() },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(28.dp).clip(CircleShape).background(Color(0xFFF1F3F4)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("G", color = Color(0xFF4285F4), fontWeight = FontWeight.Bold, fontSize = 17.sp)
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    if (loading) "Sandali lang..." else "Continue with Google",
                    color = Color.Black, fontSize = 17.sp, fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onBack) { Text("Mamaya na (Guest)", color = LGray) }
        }
    }
}
