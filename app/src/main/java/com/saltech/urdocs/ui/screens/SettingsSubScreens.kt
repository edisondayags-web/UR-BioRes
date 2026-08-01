package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxWidth

/**
 * Reusable scaffold for the 4 detail screens -- same neon look
 * as the Settings screen (back arrow + title + scrollable body).
 */
@Composable
private fun DetailScreenScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp, 24.dp, 20.dp, 40.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(Modifier.width(4.dp))
                Text(title, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SettingsColors.CardBg)
                    .border(1.dp, Color.White, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun BodyText(text: String) {
    Text(text, color = Color.White, fontSize = 13.sp, lineHeight = 20.sp)
    Spacer(Modifier.height(14.dp))
}

@Composable
private fun BodyHeading(text: String) {
    Text(text, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(6.dp))
}

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    DetailScreenScaffold(title = "Privacy Policy", onBack = onBack) {
        BodyHeading("What we collect")
        BodyText("UR BioRes is built to help you create resumes, bio-data, and letters locally on your device. The details you enter in the forms (name, contact info, work history, etc.) are used only to build the document you're requesting.")

        BodyHeading("Where your data goes")
        BodyText("The information you enter is not automatically uploaded or shared with external servers. When you tap Download, the finished document is saved as an image file directly to your device's Gallery.")

        BodyHeading("Storage/photo access")
        BodyText("We only request the access needed to save your finished resume or document to your Gallery, and to let you upload your own photo (for example, in the 2x2 photo box of the Traditional Resume).")

        BodyHeading("Changes")
        BodyText("This Privacy Policy may be updated as UR BioRes grows as part of the Sal-Tech startup. We'll let you know about any important changes within the app.")
    }
}

@Composable
fun TermsConditionsScreen(onBack: () -> Unit) {
    DetailScreenScaffold(title = "Terms & Conditions", onBack = onBack) {
        BodyHeading("Using the App")
        BodyText("By using UR BioRes, you agree to use the app for personal creation of resumes, bio-data, government forms, and letters. The app may not be used for illegal or fraudulent purposes.")

        BodyHeading("Accuracy of Information")
        BodyText("It's your responsibility as the user to ensure the accuracy and truthfulness of the information in the documents generated using the app. UR BioRes is only a formatting tool and does not verify the truthfulness of content.")

        BodyHeading("Availability")
        BodyText("Because UR BioRes is a product of Sal-Tech, an upcoming startup, there may be downtime, updates, or feature changes as it continues to be developed.")

        BodyHeading("Limitation of Liability")
        BodyText("Sal-Tech and the developer are not liable for any issues that may arise from the use of documents generated using the app (for example, an employer or agency rejecting a submitted document).")
    }
}

@Composable
fun DataPermissionsScreen(onBack: () -> Unit) {
    DetailScreenScaffold(title = "Data & Permissions", onBack = onBack) {
        BodyHeading("📷 Photos / Media")
        BodyText("Needed so you can select the photo to place in the 2x2 photo box (Traditional Resume) and so the finished document can be saved back to your Gallery.")

        BodyHeading("💾 Storage")
        BodyText("Used to save the final resume/bio-data/letter as an image file on your device, inside the Pictures/URDocs folder.")

        BodyHeading("🌐 Internet")
        BodyText("Used only for future app updates; it is not used to upload the personal details you enter into the forms.")

        BodyHeading("Why we ask for these")
        BodyText("UR BioRes is designed to keep your data local to your device. The permissions requested are only for the app's core function: creating and saving your document.")
    }
}

@Composable
fun AboutDeveloperScreen(onBack: () -> Unit) {
    DetailScreenScaffold(title = "About Developer", onBack = onBack) {
        BodyHeading("Edison Suclatan Dayaguit")
        BodyText("Developer and sole creator of UR BioRes, part of Sal-Tech -- an upcoming startup based in the Philippines.")

        BodyHeading("Tech Background")
        BodyText("Besides the Kotlin/Android development used in this app, I also develop using Rust and Ada/SPARK -- languages known for performance, memory safety, and reliability, commonly used in systems programming and high-assurance software.")

        BodyHeading("About UR BioRes")
        BodyText("UR BioRes was built to make it easier for Filipinos to create resumes, bio-data, government forms, and letters -- directly on the phone, with no laptop or printed template needed.")

        BodyHeading("Sal-Tech")
        BodyText("Sal-Tech is a startup currently preparing its papers and registration. UR BioRes is the first product under this brand.")
    }
}

@Composable
fun MyProfileScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("ur_profile", android.content.Context.MODE_PRIVATE) }
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = SettingsColors.TextWhite,
        unfocusedTextColor = SettingsColors.TextWhite,
        focusedBorderColor = SettingsColors.NeonPink,
        unfocusedBorderColor = SettingsColors.NeonPink.copy(alpha = 0.5f),
        focusedLabelColor = SettingsColors.NeonPink,
        unfocusedLabelColor = SettingsColors.TextMuted,
        cursorColor = SettingsColors.NeonPink,
        focusedContainerColor = SettingsColors.Background,
        unfocusedContainerColor = SettingsColors.Background
    )

    var fullName by remember { mutableStateOf(prefs.getString("full_name", "") ?: "") }
    var address by remember { mutableStateOf(prefs.getString("address", "") ?: "") }
    var age by remember { mutableStateOf(prefs.getString("age", "") ?: "") }
    var contactNumber by remember { mutableStateOf(prefs.getString("contact_number", "") ?: "") }
    var email by remember { mutableStateOf(prefs.getString("email", "") ?: "") }

    DetailScreenScaffold(title = "My Profile", onBack = onBack) {
        BodyHeading("Your Information")
        BodyText("Fill this out once and it will be used to auto-fill your resumes, bio-data, and letters.")

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = fullName, onValueChange = { fullName = it },
            label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), colors = fieldColors
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = address, onValueChange = { address = it },
            label = { Text("Address") }, modifier = Modifier.fillMaxWidth(), colors = fieldColors
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = age, onValueChange = { age = it },
            label = { Text("Age") }, modifier = Modifier.fillMaxWidth(), colors = fieldColors
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = contactNumber, onValueChange = { contactNumber = it },
            label = { Text("Contact Number") }, modifier = Modifier.fillMaxWidth(), colors = fieldColors
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = email, onValueChange = { email = it },
            label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), colors = fieldColors
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                prefs.edit()
                    .putString("full_name", fullName)
                    .putString("address", address)
                    .putString("age", age)
                    .putString("contact_number", contactNumber)
                    .putString("email", email)
                    .apply()
                android.widget.Toast.makeText(context, "Profile saved!", android.widget.Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = SettingsColors.NeonPink,
                contentColor = SettingsColors.TextWhite
            )
        ) {
            Text("Save Profile")
        }
    }
}
