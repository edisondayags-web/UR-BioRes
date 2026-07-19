package com.saltech.urdocs.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * ================== NEON THEME (galing sa Canva mockup) ==================
 * Black background + neon pink/magenta + neon green accents.
 * Ginamit sa buong Settings screen at sa 4 na functional sub-screens.
 */
object SettingsColors {
    val Background = Color(0xFF0A0A0A)
    val CardBg = Color(0xFF141414)
    val NeonPink = Color(0xFFFF2E7E)
    val NeonGreen = Color(0xFF39FF6E)
    val TextWhite = Color(0xFFF5F5F5)
    val TextMuted = Color(0xFFB0B0B0)
}

data class SettingsItemData(
    val icon: String,
    val title: String,
    val subtitle: String,
    val route: String? = null,   // non-null = functional, may destination
    val enabled: Boolean = false // false = "Coming soon" lang, walang navigation
)

/**
 * Route constants -- ikonekta mo 'to sa NavHost mo.
 * Halimbawa:
 *   composable(SettingsRoutes.PRIVACY_POLICY) { PrivacyPolicyScreen(onBack = { navController.popBackStack() }) }
 */
object SettingsRoutes {
    const val PRIVACY_POLICY = "privacy_policy"
    const val TERMS_CONDITIONS = "terms_conditions"
    const val DATA_PERMISSIONS = "data_permissions"
    const val ABOUT_DEVELOPER = "about_developer"
}

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current

    val sections = listOf(
        "PRIVACY & SECURITY" to listOf(
            SettingsItemData("🔒", "Privacy Policy", "Learn how we protect your data", SettingsRoutes.PRIVACY_POLICY, enabled = true),
            SettingsItemData("📄", "Terms & Conditions", "Read our terms and conditions", SettingsRoutes.TERMS_CONDITIONS, enabled = true),
            SettingsItemData("🛡️", "Data & Permissions", "Understand why we need access", SettingsRoutes.DATA_PERMISSIONS, enabled = true),
        ),
        "APP" to listOf(
            SettingsItemData("⭐", "Rate UR BioRes", "Show your support"),
            SettingsItemData("🔗", "Share UR BioRes", "Share the app with your friends"),
            SettingsItemData("⬇️", "Check for Updates", "Get the latest version"),
        ),
        "SUPPORT" to listOf(
            SettingsItemData("✉️", "Contact Support", "We're here to help you"),
            SettingsItemData("🐞", "Report a Bug", "Help us improve the app"),
            SettingsItemData("💡", "Suggest a Feature", "Share your ideas with us"),
        ),
        "ABOUT" to listOf(
            SettingsItemData("👤", "About Developer", "Meet the developer", SettingsRoutes.ABOUT_DEVELOPER, enabled = true),
            SettingsItemData("</>", "Open Source Licenses", "Third-party libraries used"),
            SettingsItemData("ℹ️", "Version", "UR BioRes v1.0.0"),
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SettingsColors.Background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp, 24.dp, 20.dp, 100.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SettingsColors.NeonPink)
                    }
                    Spacer(Modifier.width(4.dp))
                    Column {
                        Text("SETTINGS", color = SettingsColors.TextWhite, fontSize = 30.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Manage your app preferences and information",
                            color = SettingsColors.TextMuted,
                            fontSize = 13.sp
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                SettingsHeaderCard()
                Spacer(Modifier.height(24.dp))
            }

            sections.forEach { (sectionTitle, sectionItems) ->
                item {
                    SettingsSectionHeader(sectionTitle)
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(SettingsColors.CardBg)
                    ) {
                        sectionItems.forEachIndexed { index, itemData ->
                            SettingsItemRow(
                                item = itemData,
                                onClick = {
                                    if (itemData.enabled && itemData.route != null) {
                                        onNavigate(itemData.route)
                                    } else {
                                        Toast.makeText(context, "Coming soon 🩵", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                            if (index != sectionItems.lastIndex) {
                                Spacer(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(Color(0xFF2A2A2A))
                                )
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(24.dp)) }
            }

            item { SettingsQuoteFooter() }
        }
    }
}

@Composable
private fun SettingsHeaderCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SettingsColors.CardBg)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(listOf(SettingsColors.NeonPink, SettingsColors.NeonGreen)),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, Brush.linearGradient(listOf(SettingsColors.NeonPink, SettingsColors.NeonGreen)), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("UR\nBioRes", color = SettingsColors.NeonPink, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("UR BioRes", color = SettingsColors.TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Version 1.0.0", color = SettingsColors.NeonGreen, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF1E1E1E))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text("Made with ❤️ in PH", color = SettingsColors.TextWhite, fontSize = 11.sp)
            }
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("🚀 Startup Project", color = SettingsColors.NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(
                "Sal-Tech is an upcoming startup. Papers and registration coming soon!",
                color = SettingsColors.TextMuted,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp, top = 4.dp)) {
        Text("● ", color = SettingsColors.NeonGreen, fontSize = 12.sp)
        Text(title, color = SettingsColors.NeonGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SettingsItemRow(item: SettingsItemData, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, SettingsColors.NeonPink, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(item.icon, fontSize = 16.sp)
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.title, color = SettingsColors.NeonPink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(item.subtitle, color = SettingsColors.TextWhite, fontSize = 12.sp)
        }
        Text("›", color = SettingsColors.NeonPink, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SettingsQuoteFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SettingsColors.CardBg)
            .border(1.dp, SettingsColors.NeonPink, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text("❝ ", color = SettingsColors.NeonGreen, fontSize = 18.sp)
        Text(
            buildString {
                append("WALANG KWENTA PAGIGING MATALINO NYO KONG TATAWANAN LANG YAN NG SALTIK... este ")
            },
            color = SettingsColors.TextMuted,
            fontSize = 12.sp
        )
        Text("SAL-TECH. 😁", color = SettingsColors.NeonPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text("Just a meme, walang halong seriosong sinasabi. Peace! ✌️", color = SettingsColors.TextMuted, fontSize = 11.sp)
    }
}
