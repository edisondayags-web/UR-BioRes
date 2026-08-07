package com.saltech.urdocs.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
 * ================== NEON THEME ==================
 */
object SettingsColors {
    val Background = Color(0xFF0A0A0A)
    val CardBg = Color(0xFF141414)
    val NeonPink = Color(0xFF3B6FE0)
    val NeonGreen = Color(0xFF0B1530)
    val TextWhite = Color(0xFFF5F5F5)
    val TextMuted = Color(0xFFB0B0B0)
    
    // Cached Brushes para hindi paulit-ulit na gawan ng gradient memory ang GPU
    val CardBorderBrush = Brush.linearGradient(listOf(Color(0xFF4C8DFF), Color.Black))
    val HeaderBorderBrush = Brush.linearGradient(listOf(NeonPink, NeonGreen))
}

data class SettingsItemData(
    val icon: String,
    val title: String,
    val subtitle: String,
    val route: String? = null,
    val enabled: Boolean = false
)

object SettingsRoutes {
    const val MY_PROFILE = "my_profile"
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

    // Naka-remember ang buong data list para isang beses lang ilagay sa memory
    val sections = remember {
        listOf(
            "ACCOUNT" to listOf(
                SettingsItemData("👤", "My Profile", "Set up your info for auto-fill", SettingsRoutes.MY_PROFILE, enabled = true),
            ),
            "PRIVACY & SECURITY" to listOf(
                SettingsItemData("🔒", "Privacy Policy", "Learn how we protect your data", SettingsRoutes.PRIVACY_POLICY, enabled = true),
                SettingsItemData("📄", "Terms & Conditions", "Read our terms and conditions", SettingsRoutes.TERMS_CONDITIONS, enabled = true),
                SettingsItemData("🛡️", "Data & Permissions", "Understand why we need access", SettingsRoutes.DATA_PERMISSIONS, enabled = true),
            ),
            "APP" to listOf(
                SettingsItemData("⭐", "Rate UR BioRes", "Show your support", enabled = true),
                SettingsItemData("🔗", "Share UR BioRes", "Share the app with your friends", enabled = true),
                SettingsItemData("⬇️", "Check for Updates", "Get the latest version", enabled = true),
            ),
            "SUPPORT" to listOf(
                SettingsItemData("✉️", "Contact Support", "We're here to help you"),
                SettingsItemData("🐞", "Report a Bug", "Help us improve the app"),
                SettingsItemData("💡", "Suggest a Feature", "Share your ideas with us"),
            ),
            "ABOUT" to listOf(
                SettingsItemData("👤", "About Developer", "Meet the developer", SettingsRoutes.ABOUT_DEVELOPER, enabled = true),
                SettingsItemData("</>", "Open Source Licenses", "Third-party libraries used", enabled = true),
                SettingsItemData("ℹ️", "Version", "UR BioRes v1.0.0"),
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        PremiumWaveBackground()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, top = 24.dp, end = 20.dp, bottom = 100.dp)
        ) {
            // Header Section
            item(key = "header_section") {
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

            // High-Performance Section Rendering
            sections.forEach { (sectionTitle, sectionItems) ->
                item(key = "header_$sectionTitle") {
                    SettingsSectionHeader(sectionTitle)
                }

                items(
                    items = sectionItems,
                    key = { it.title } // Mabilis na memory recycling sa Compose Engine
                ) { itemData ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black.copy(alpha = 0.55f))
                            .border(1.dp, SettingsColors.CardBorderBrush, RoundedCornerShape(16.dp))
                    ) {
                        SettingsItemRow(
                            item = itemData,
                            accentColor = Color.White,
                            onClick = {
                                val emailItems = setOf("Contact Support", "Report a Bug", "Suggest a Feature")
                                if (itemData.title in emailItems) {
                                    val subject = when (itemData.title) {
                                        "Contact Support" -> "UR BioRes Support"
                                        "Report a Bug" -> "UR BioRes Bug Report"
                                        else -> "UR BioRes Feature Suggestion"
                                    }
                                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                                        data = Uri.parse("mailto:")
                                        putExtra(Intent.EXTRA_EMAIL, arrayOf("edisondayags@gmail.com"))
                                        putExtra(Intent.EXTRA_SUBJECT, subject)
                                    }
                                    context.startActivity(Intent.createChooser(intent, "Send Email"))
                                } else if (itemData.title == "Rate UR BioRes") {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.saltech.urdocs"))
                                    try { 
                                        context.startActivity(intent) 
                                    } catch (e: Exception) {
                                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.saltech.urdocs")))
                                    }
                                } else if (itemData.title == "Share UR BioRes") {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, "Check out UR BioRes! https://play.google.com/store/apps/details?id=com.saltech.urdocs")
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share UR BioRes"))
                                } else if (itemData.title == "Check for Updates") {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.saltech.urdocs")))
                                } else if (itemData.title == "Open Source Licenses") {
                                    Toast.makeText(context, "Built with Jetpack Compose, AndroidX, Material3", Toast.LENGTH_LONG).show()
                                } else if (itemData.enabled && itemData.route != null) {
                                    onNavigate(itemData.route)
                                } else {
                                    Toast.makeText(context, "Coming soon \uD83E\uDEB5", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                }

                item(key = "spacer_$sectionTitle") { 
                    Spacer(Modifier.height(14.dp)) 
                }
            }

            item(key = "footer_quote") { 
                SettingsQuoteFooter() 
            }
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
                brush = SettingsColors.HeaderBorderBrush,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(18.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, SettingsColors.HeaderBorderBrush, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("UR\nBioRes", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("UR BioRes", color = SettingsColors.TextWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text("Version 1.0.0", color = Color.White, fontSize = 13.sp)
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
            Text("🚀 Startup Project", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
        Text("● ", color = Color.White, fontSize = 12.sp)
        Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SettingsItemRow(item: SettingsItemData, accentColor: Color = SettingsColors.NeonPink, onClick: () -> Unit) {
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
                .background(Color(0xFF1A1A1A))
                .border(1.dp, SettingsColors.CardBorderBrush, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(item.icon, fontSize = 16.sp)
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(item.title, color = accentColor, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(item.subtitle, color = SettingsColors.TextWhite, fontSize = 12.sp)
        }
        Text("›", color = accentColor, fontSize = 22.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SettingsQuoteFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SettingsColors.CardBg)
            .border(1.dp, SettingsColors.CardBorderBrush, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text("❝ ", color = Color.White, fontSize = 18.sp)
        Text(
            buildString {
                append("WALANG KWENTA PAGIGING MATALINO NYO KONG TATAWANAN LANG YAN NG SALTIK... este ")
            },
            color = SettingsColors.TextMuted,
            fontSize = 12.sp
        )
        Text("SAL-TECH. 😁", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text("Ayg pataka diha ka inspirasyon basig walopon ko na imong sampot! ✌️", color = SettingsColors.TextMuted, fontSize = 11.sp)
    }
}
