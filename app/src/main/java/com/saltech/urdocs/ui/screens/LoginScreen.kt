package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.saltech.urdocs.data.AuthManager
import kotlinx.coroutines.launch

private val LBlue = Color(0xFF4C8DFF)
private val LGray = Color(0xFF9A9A9A)

private fun friendly(e: Exception): String = when (e) {
    is FirebaseAuthUserCollisionException -> "May account na ang email na ito. Mag-login ka na lang."
    is FirebaseAuthWeakPasswordException -> "Masyadong mahina ang password (min. 6 characters)."
    is FirebaseAuthInvalidUserException, is FirebaseAuthInvalidCredentialsException -> "Mali ang email o password."
    else -> "Hindi natuloy: ${e.localizedMessage ?: "subukan ulit"}"
}

@Composable
fun LoginScreen(onBack: () -> Unit, onDone: () -> Unit) {
    val scope = rememberCoroutineScope()
    val auth = remember { AuthManager() }
    var isSignUp by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        focusedBorderColor = LBlue,
        unfocusedBorderColor = LBlue.copy(alpha = 0.5f),
        focusedLabelColor = LBlue,
        unfocusedLabelColor = LGray,
        cursorColor = LBlue
    )

    fun submit() {
        val e = email.trim()
        if (e.isBlank() || password.length < 6) {
            message = "Ilagay ang email at password (min. 6 characters)."
            return
        }
        scope.launch {
            loading = true
            message = null
            try {
                if (isSignUp) auth.signUp(e, password) else auth.signIn(e, password)
                onDone()
            } catch (ex: Exception) {
                message = friendly(ex)
            }
            loading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        PremiumWaveBackground()

        Box(
            modifier = Modifier
                .padding(start = 16.dp, top = 40.dp)
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
            Text(
                if (isSignUp) "Gumawa ng Account" else "Mag-login",
                color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Email at password lang luv 🩵",
                color = LGray, fontSize = 13.sp, textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email") }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(), colors = fieldColors
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Password") }, singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(), colors = fieldColors
            )

            message?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = Color(0xFFFF7A8A), fontSize = 13.sp, textAlign = TextAlign.Center)
            }

            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { submit() },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LBlue, contentColor = Color.White)
            ) {
                Text(
                    when {
                        loading -> "Sandali lang..."
                        isSignUp -> "Sign up"
                        else -> "Login"
                    },
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(10.dp))
            TextButton(onClick = { isSignUp = !isSignUp; message = null }) {
                Text(
                    if (isSignUp) "May account na? Mag-login" else "Wala pang account? Mag-sign up",
                    color = LBlue
                )
            }
            if (!isSignUp) {
                TextButton(onClick = {
                    val e = email.trim()
                    if (e.isBlank()) {
                        message = "Ilagay muna ang email mo sa taas."
                    } else {
                        scope.launch {
                            runCatching { auth.resetPassword(e) }
                            message = "Kung may account ang email na ito, may ipinadala kaming reset link."
                        }
                    }
                }) { Text("Nakalimutan ang password?", color = LGray) }
            }
        }
    }
}
