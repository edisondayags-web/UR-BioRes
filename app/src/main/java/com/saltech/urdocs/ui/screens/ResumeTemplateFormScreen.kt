package com.example.resumetemplates.ui.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Data model para sa bawat template style configuration
data class TemplateConfig(
    val id: Int,
    val backgroundColor: Color,
    val accentColor: Color,
    val lineColor: Color,
    val hasSidebar: Boolean,
    val hasBorderFrame: Boolean = false
)

// 2. Repository/Provider para sa lahat ng 23 templates
object TemplateRepository {
    fun getAllTemplates(): List<TemplateConfig> {
        return listOf(
            TemplateConfig(1, Color(0xFF0C0C0C), Color(0xFFD4AF37), Color(0xFF222222), true),  // Luxury Gold
            TemplateConfig(2, Color(0xFF1B263B), Color(0xFFFFFFFF), Color(0xFF2C3E50), true),  // Navy Sidebar
            TemplateConfig(3, Color(0xFF080808), Color(0xFF00FF66), Color(0xFF1A3322), true, true), // Cyberpunk
            TemplateConfig(4, Color(0xFFFFFFFF), Color(0xFF111111), Color(0xFFE0E0E0), false), // Clean White
            TemplateConfig(5, Color(0xFF1A0B2E), Color(0xFFD946EF), Color(0xFF2E1065), true),  // Neon Purple
            TemplateConfig(6, Color(0xFFF4F6F8), Color(0xFF0F766E), Color(0xFFCBD5E1), false), // Teal Corporate
            TemplateConfig(7, Color(0xFF0F172A), Color(0xFFF59E0B), Color(0xFF1E293B), true),  // Slate Amber
            TemplateConfig(8, Color(0xFFFFFFFF), Color(0xFF2563EB), Color(0xFFE2E8F0), false), // Modern Blue
            TemplateConfig(9, Color(0xFF18181B), Color(0xFFEF4444), Color(0xFF27272A), true),  // Dark Crimson
            TemplateConfig(10, Color(0xFF0A0A0A), Color(0xFFDC2626), Color(0xFF1C1917), true), // Red Neon Dark
            TemplateConfig(11, Color(0xFFF0FDF4), Color(0xFF15803D), Color(0xFFDCFCE7), false),// Eco Green
            TemplateConfig(12, Color(0xFF1E1B4B), Color(0xFF818CF8), Color(0xFF312E81), true),  // Indigo Night
            TemplateConfig(13, Color(0xFFFAFAF9), Color(0xFF44403C), Color(0xFFE7E5E4), false),// Warm Minimal
            TemplateConfig(14, Color(0xFF111827), Color(0xFFEC4899), Color(0xFF1F2937), true),  // Pink Dark
            TemplateConfig(15, Color(0xFFFFFFFF), Color(0xFF0284C7), Color(0xFFF1F5F9), false),// Sky Light
            TemplateConfig(16, Color(0xFF09090B), Color(0xFFEAB308), Color(0xFF27272A), true),  // Gold Dark
            TemplateConfig(17, Color(0xFF030712), Color(0xFF38BDF8), Color(0xFF111827), true, true), // Tech Cyan
            TemplateConfig(18, Color(0xFFFFFBEB), Color(0xFFB45309), Color(0xFFFEF3C7), false),// Cream Classic
            TemplateConfig(19, Color(0xFF22090A), Color(0xFF991B1B), Color(0xFF450A0A), true),  // Wine Red
            TemplateConfig(20, Color(0xFFF8FAFC), Color(0xFF0F766E), Color(0xFFE2E8F0), false),// Slate Light
            TemplateConfig(21, Color(0xFF050505), Color(0xFF10B981), Color(0xFF064E3B), true),  // Emerald Dark
            TemplateConfig(22, Color(0xFF0F172A), Color(0xFF6366F1), Color(0xFF1E293B), true),  // Slate Indigo
            TemplateConfig(23, Color(0xFFFFFFFF), Color(0xFF475569), Color(0xFFE2E8F0), false) // Standard Gray
        )
    }
}

// 3. Dynamic Renderer para sa lahat ng 23 templates
@Composable
fun DynamicResumePreview(config: TemplateConfig) {
    val containerModifier = if (config.hasBorderFrame) {
        Modifier
            .fillMaxSize()
            .background(config.backgroundColor)
            .border(1.5.dp, config.accentColor, RoundedCornerShape(4.dp))
            .padding(16.dp)
    } else {
        Modifier
            .fillMaxSize()
            .background(config.backgroundColor)
            .padding(16.dp)
    }

    Box(modifier = containerModifier) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sidebar or Left Column
            Column(
                modifier = Modifier
                    .weight(0.38f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .border(2.dp, config.accentColor, CircleShape)
                        .background(if (config.backgroundColor == Color.White) Color.LightGray else Color.Black)
                )

                TemplateHeader(title = "CONTACT", color = config.accentColor)
                repeat(4) { PlaceholderBar(color = config.lineColor) }

                TemplateHeader(title = "SKILLS", color = config.accentColor)
                repeat(5) { PlaceholderBar(color = config.lineColor) }
            }

            // Main Content Column
            Column(
                modifier = Modifier
                    .weight(0.62f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    PlaceholderBar(color = config.accentColor, height = 12.dp, widthFraction = 0.8f)
                    PlaceholderBar(color = config.accentColor.copy(alpha = 0.6f), height = 7.dp, widthFraction = 0.5f)
                }

                TemplateHeader(title = "ABOUT ME", color = config.accentColor)
                PlaceholderBar(color = config.lineColor, widthFraction = 1f)
                PlaceholderBar(color = config.lineColor, widthFraction = 0.9f)

                TemplateHeader(title = "EDUCATION", color = config.accentColor)
                repeat(2) {
                    PlaceholderBar(color = config.lineColor, widthFraction = 0.95f)
                    PlaceholderBar(color = config.lineColor, widthFraction = 0.6f)
                }

                TemplateHeader(title = "EXPERIENCE", color = config.accentColor)
                repeat(3) {
                    PlaceholderBar(color = config.lineColor, widthFraction = 0.95f)
                }
            }
        }
    }
}

@Composable
fun TemplateHeader(title: String, color: Color) {
    Text(
        text = title,
        color = color,
        fontSize = 10.sp,
        letterSpacing = 1.sp
    )
}

@Composable
fun PlaceholderBar(
    color: Color,
    height: androidx.compose.ui.unit.Dp = 5.dp,
    widthFraction: Float = 0.85f
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(height)
            .clip(RoundedCornerShape(2.dp))
            .background(color)
    )
}
