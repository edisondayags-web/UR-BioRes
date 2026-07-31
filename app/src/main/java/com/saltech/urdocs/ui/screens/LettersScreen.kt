package com.saltech.urdocs.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saltech.urdocs.model.LetterRequest
import com.saltech.urdocs.model.LetterType
import com.saltech.urdocs.viewmodel.LettersViewModel

private val UrPink = Color(0xFF3B6FE0)
private val UrGreen = Color(0xFF0B1530)
private val UrCardBg = Color(0xFF161616)
private val UrGray = Color(0xFF9A9A9A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LettersScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: LettersViewModel = viewModel()
) {
    var screenState by remember { mutableStateOf("hub") }
    var selectedType by remember { mutableStateOf(LetterType.entries.first()) }
    val context = LocalContext.current

    fun findType(keyword: String): LetterType =
        LetterType.entries.firstOrNull { it.label.contains(keyword, ignoreCase = true) }
            ?: LetterType.entries.first()

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = screenState,
            transitionSpec = {
                (slideInHorizontally(initialOffsetX = { it / 3 }) + fadeIn(animationSpec = tween(300)))
                    .togetherWith(slideOutHorizontally(targetOffsetX = { -it / 3 }) + fadeOut(animationSpec = tween(300)))
            },
            label = "lettersScreenTransition"
        ) { state ->
when (state) {
    "resignation" -> ResignationLetterScreen()
    "leave" -> LeaveLetterScreen()
    "generic" -> GenericLetterScreen(letterType = selectedType)
    "form" -> LetterFormContent(
        viewModel = viewModel,
        initialType = selectedType,
        onBack = { screenState = "hub" }
    )
    "all" -> AllTemplatesContent(
        onBack = { screenState = "hub" },
        onPick = { type -> selectedType = type; screenState = "generic" }
    )
    else -> LettersHubContent(
        onPremiumTap = { Toast.makeText(context, "Premium -- Coming Soon!", Toast.LENGTH_SHORT).show() },
        onPopularTap = { _ -> Toast.makeText(context, "coming soon pa to luv🩵", Toast.LENGTH_SHORT).show() },
        onResignationTap = { screenState = "resignation" },
        onLeaveTap = { screenState = "leave" },
        onExcuseTap = { selectedType = findType("Excuse"); screenState = "generic" },
        onPick = { type -> selectedType = type; screenState = "generic" },
        onMoreTemplates = { screenState = "all" },
        onNavigate = onNavigate
    )
}
}
    }
}
// ================== Custom vector icons (hindi Material icon, sariling guhit) ==================

@Composable
private fun CrownIcon(modifier: Modifier = Modifier, color: Color = UrPink) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val path = Path().apply {
            moveTo(w * 0.05f, h * 0.85f)
            lineTo(w * 0.05f, h * 0.45f)
            lineTo(w * 0.28f, h * 0.62f)
            lineTo(w * 0.5f, h * 0.15f)
            lineTo(w * 0.72f, h * 0.62f)
            lineTo(w * 0.95f, h * 0.45f)
            lineTo(w * 0.95f, h * 0.85f)
            close()
        }
        drawPath(path, color = color, style = Stroke(width = w * 0.07f))
        drawCircle(color = color, radius = w * 0.05f, center = Offset(w * 0.5f, h * 0.12f))
        drawCircle(color = color, radius = w * 0.04f, center = Offset(w * 0.05f, h * 0.42f))
        drawCircle(color = color, radius = w * 0.04f, center = Offset(w * 0.95f, h * 0.42f))
    }
}

@Composable
private fun BriefcaseIcon(modifier: Modifier = Modifier, color: Color = UrPink) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.07f
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.1f, h * 0.32f),
            size = androidx.compose.ui.geometry.Size(w * 0.8f, h * 0.55f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.08f),
            style = Stroke(width = strokeW)
        )
        val handle = Path().apply {
            moveTo(w * 0.35f, h * 0.32f)
            lineTo(w * 0.35f, h * 0.18f)
            quadraticBezierTo(w * 0.35f, h * 0.1f, w * 0.5f, h * 0.1f)
            quadraticBezierTo(w * 0.65f, h * 0.1f, w * 0.65f, h * 0.18f)
            lineTo(w * 0.65f, h * 0.32f)
        }
        drawPath(handle, color = color, style = Stroke(width = strokeW))
        drawLine(color, Offset(w * 0.1f, h * 0.58f), Offset(w * 0.9f, h * 0.58f), strokeWidth = strokeW * 0.7f)
    }
}

@Composable
private fun PersonIcon(modifier: Modifier = Modifier, color: Color = UrPink) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.08f
        drawCircle(color = color, radius = w * 0.18f, center = Offset(w * 0.5f, h * 0.3f), style = Stroke(width = strokeW))
        val body = Path().apply {
            moveTo(w * 0.2f, h * 0.9f)
            quadraticBezierTo(w * 0.2f, h * 0.55f, w * 0.5f, h * 0.55f)
            quadraticBezierTo(w * 0.8f, h * 0.55f, w * 0.8f, h * 0.9f)
        }
        drawPath(body, color = color, style = Stroke(width = strokeW))
    }
}

@Composable
private fun PencilIcon(modifier: Modifier = Modifier, color: Color = UrPink) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.07f
        val path = Path().apply {
            moveTo(w * 0.2f, h * 0.8f)
            lineTo(w * 0.15f, h * 0.9f)
            lineTo(w * 0.25f, h * 0.85f)
            lineTo(w * 0.75f, h * 0.2f)
            lineTo(w * 0.65f, h * 0.1f)
            lineTo(w * 0.2f, h * 0.8f)
            close()
        }
        drawPath(path, color = color, style = Stroke(width = strokeW))
        drawLine(color, Offset(w * 0.6f, h * 0.28f), Offset(w * 0.72f, h * 0.4f), strokeWidth = strokeW * 0.7f)
    }
}

@Composable
private fun GridIcon(modifier: Modifier = Modifier, color: Color = UrPink) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val boxSize = w * 0.35f
        val gap = w * 0.12f
        val strokeW = w * 0.06f
        val positions = listOf(
            Offset(w * 0.08f, h * 0.08f),
            Offset(w * 0.08f + boxSize + gap, h * 0.08f),
            Offset(w * 0.08f, h * 0.08f + boxSize + gap),
            Offset(w * 0.08f + boxSize + gap, h * 0.08f + boxSize + gap)
        )
        positions.forEach { pos ->
            drawRoundRect(
                color = color,
                topLeft = pos,
                size = androidx.compose.ui.geometry.Size(boxSize, boxSize),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(boxSize * 0.2f),
                style = Stroke(width = strokeW)
            )
        }
    }
}

@Composable
private fun EnvelopeIcon(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.07f
        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.08f, h * 0.2f),
            size = androidx.compose.ui.geometry.Size(w * 0.84f, h * 0.6f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.06f),
            style = Stroke(width = strokeW)
        )
        val flap = Path().apply {
            moveTo(w * 0.1f, h * 0.24f)
            lineTo(w * 0.5f, h * 0.55f)
            lineTo(w * 0.9f, h * 0.24f)
        }
        drawPath(flap, color = color, style = Stroke(width = strokeW))
    }
}

/** Guhit na paper + pen + envelope, aproximate sa hero illustration ng design. */
@Composable
private fun LetterIllustration(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val envW = w * 0.55f
        val envH = h * 0.42f
        val envLeft = 0f
        val envTop = h * 0.5f
        drawRoundRect(
            color = UrPink,
            topLeft = Offset(envLeft, envTop),
            size = androidx.compose.ui.geometry.Size(envW, envH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f),
            style = Stroke(width = 3f)
        )
        val flap = Path().apply {
            moveTo(envLeft, envTop)
            lineTo(envLeft + envW / 2f, envTop + envH * 0.55f)
            lineTo(envLeft + envW, envTop)
        }
        drawPath(flap, color = UrPink, style = Stroke(width = 3f))

        val paperW = w * 0.55f
        val paperH = h * 0.85f
        val paperLeft = w * 0.42f
        val paperTop = 0f
        drawRoundRect(
            color = Color(0xFF1A1A1A),
            topLeft = Offset(paperLeft, paperTop),
            size = androidx.compose.ui.geometry.Size(paperW, paperH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
        )
        drawRoundRect(
            color = UrGreen,
            topLeft = Offset(paperLeft, paperTop),
            size = androidx.compose.ui.geometry.Size(paperW, paperH),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f),
            style = Stroke(width = 2f)
        )
        val fold = Path().apply {
            moveTo(paperLeft + paperW - 22f, paperTop)
            lineTo(paperLeft + paperW, paperTop)
            lineTo(paperLeft + paperW, paperTop + 22f)
            close()
        }
        drawPath(fold, color = UrPink)

        val lineX = paperLeft + paperW * 0.15f
        val lineWidth = paperW * 0.65f
        listOf(0.28f, 0.42f, 0.56f).forEach { frac ->
            drawLine(
                color = UrPink,
                start = Offset(lineX, paperTop + paperH * frac),
                end = Offset(lineX + lineWidth, paperTop + paperH * frac),
                strokeWidth = 4f
            )
        }
        val sig = Path().apply {
            moveTo(lineX, paperTop + paperH * 0.72f)
            quadraticBezierTo(lineX + lineWidth * 0.2f, paperTop + paperH * 0.62f, lineX + lineWidth * 0.4f, paperTop + paperH * 0.72f)
            quadraticBezierTo(lineX + lineWidth * 0.6f, paperTop + paperH * 0.82f, lineX + lineWidth * 0.8f, paperTop + paperH * 0.7f)
        }
        drawPath(sig, color = UrPink, style = Stroke(width = 3f))

        val penPath = Path().apply {
            moveTo(paperLeft + paperW * 0.55f, paperTop + paperH * 0.95f)
            lineTo(paperLeft + paperW * 0.85f, paperTop + paperH * 0.45f)
            lineTo(paperLeft + paperW * 0.95f, paperTop + paperH * 0.55f)
            lineTo(paperLeft + paperW * 0.65f, paperTop + paperH)
            close()
        }
        drawPath(penPath, color = Color(0xFF0D0D0D))
        drawPath(penPath, color = UrPink, style = Stroke(width = 3f))
    }
}

// ================== Main hub content (now LazyColumn for smooth scroll) ==================

@Composable
private fun LettersHubContent(
    onPremiumTap: () -> Unit,
    onPopularTap: (String) -> Unit,
    onResignationTap: () -> Unit,
    onLeaveTap: () -> Unit,
    onExcuseTap: () -> Unit,
    onPick: (LetterType) -> Unit,
    onMoreTemplates: () -> Unit,
    onNavigate: (String) -> Unit
) {
    // Precompute the "other" letter types once instead of filtering every recomposition.
    val otherTypes = remember {
        LetterType.entries.filter { it != LetterType.LEAVE && it != LetterType.RESIGNATION && it != LetterType.EXCUSE }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            item { Spacer(Modifier.height(20.dp)) }

            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .shadow(8.dp, RoundedCornerShape(14.dp), ambientColor = UrPink, spotColor = UrPink)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.verticalGradient(listOf(Color(0xFF3B6FE0), Color(0xFF081024))))
                            .border(1.dp, UrPink, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        EnvelopeIcon(modifier = Modifier.size(26.dp), color = Color.White)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("LETTERS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                        Text("Professional Letters Made Easy", color = UrGray, fontSize = 12.sp)
                    }
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF2A0A16))
                            .border(1.dp, Brush.linearGradient(listOf(Color(0xFF4C8DFF), Color.Black)), RoundedCornerShape(20.dp))
                            .clickable { onPremiumTap() }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CrownIcon(modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("PREMIUM", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(10.dp, RoundedCornerShape(18.dp), ambientColor = UrPink, spotColor = UrGreen)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF0A0A0A))
                        .border(
                            BorderStroke(1.5.dp, Brush.linearGradient(listOf(UrPink, UrGreen))),
                            RoundedCornerShape(18.dp)
                        )
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Create Professional", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Row {
                                Text("Letters", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                Text(" in Minutes", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            }
                        }
                        LetterIllustration(modifier = Modifier.size(width = 90.dp, height = 80.dp))
                    }
                    Spacer(Modifier.height(14.dp))
                    listOf("Ready-to-print", "HR Friendly", "Philippine Format").forEach { line ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 3.dp)) {
                            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = UrGreen, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(line, color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
                Spacer(Modifier.height(22.dp))
                SectionLabel("POPULAR LETTERS", UrGreen)
                Spacer(Modifier.height(10.dp))
            }

            item {
                LetterCard(
                    icon = { BriefcaseIcon(modifier = it) },
                    badge = { Icon(Icons.Filled.BeachAccess, contentDescription = null, tint = Color.Black, modifier = it) },
                    title = "Leave Letter",
                    subtitle = "Request for leave from work or school",
                    onClick = { onLeaveTap() }
                )
                Spacer(Modifier.height(12.dp))
            }
            item {
                LetterCard(
                    icon = { PersonIcon(modifier = it) },
                    badge = { Icon(Icons.Filled.ExitToApp, contentDescription = null, tint = Color.Black, modifier = it) },
                    title = "Resignation Letter",
                    subtitle = "Formal resignation from your position",
                    onClick = { onResignationTap() }
                )
                Spacer(Modifier.height(12.dp))
            }
            item {
                LetterCard(
                    icon = { PencilIcon(modifier = it) },
                    badge = { Icon(Icons.Filled.Favorite, contentDescription = null, tint = Color.Black, modifier = it) },
                    title = "Excuse Letter",
                    subtitle = "Apology or reason for absence",
                    onClick = { onExcuseTap() }
                )
            }

            items(otherTypes, key = { it.name }) { type ->
                val (mainIcon, badgeIcon) = when (type) {
                    LetterType.GOVT_SSS -> Icons.Filled.AccountBalance to Icons.Filled.Description
                    LetterType.GOVT_PAGIBIG -> Icons.Filled.Home to Icons.Filled.Description
                    LetterType.APPLICATION -> Icons.Filled.Description to Icons.Filled.Send
                    LetterType.AUTHORIZATION -> Icons.Filled.VerifiedUser to Icons.Filled.CheckCircle
                    LetterType.REFERRAL -> Icons.Filled.ThumbUp to Icons.Filled.Star
                    LetterType.FOLLOW_UP -> Icons.Filled.Refresh to Icons.Filled.Schedule
                    LetterType.THANK_YOU -> Icons.Filled.Favorite to Icons.Filled.Star
                    LetterType.JOB_OFFER -> Icons.Filled.Work to Icons.Filled.CheckCircle
                    LetterType.SALARY_INCREASE -> Icons.Filled.AttachMoney to Icons.Filled.TrendingUp
                    LetterType.COMPLAINT -> Icons.Filled.ReportProblem to Icons.Filled.Warning
                    LetterType.BRGY_CITY_REQUEST -> Icons.Filled.LocationCity to Icons.Filled.Description
                    LetterType.SCHOLARSHIP -> Icons.Filled.School to Icons.Filled.Star
                    LetterType.OJT_INTERNSHIP -> Icons.Filled.Build to Icons.Filled.Description
                    LetterType.OTHERS_REQUEST -> Icons.Filled.HelpOutline to Icons.Filled.Description
                    LetterType.CUSTOM -> Icons.Filled.Edit to Icons.Filled.Description
                    else -> Icons.Filled.Description to Icons.Filled.Description
                }
                Spacer(Modifier.height(12.dp))
                LetterCard(
                    icon = { Icon(mainIcon, contentDescription = null, tint = UrPink, modifier = it) },
                    badge = { Icon(badgeIcon, contentDescription = null, tint = Color.Black, modifier = it) },
                    title = type.label,
                    subtitle = when (type) {
    LetterType.GOVT_SSS -> "para sa gusto mong padalhan para maayos/ayusin"
    LetterType.GOVT_PAGIBIG -> "Request or inquiry letter para sa Pag-IBIG"
    LetterType.APPLICATION -> "Job or school application letter"
    LetterType.AUTHORIZATION -> "para sa hindi makadalo at ibang tao lang ipadalo mo for you"
    LetterType.REFERRAL -> "kung may gusto kang e-recommend na tao"
    LetterType.FOLLOW_UP -> "kung may gusto kang i-follow-up kaso mahiyain ka"
    LetterType.THANK_YOU -> "Pasasalamat para sa gusto mong pasalamatan"
    LetterType.JOB_OFFER -> "if may gusto kang offeran ng trabaho via letter"
    LetterType.SALARY_INCREASE -> "letter request para sa dagdag sweldo"
    LetterType.COMPLAINT -> "Reklamo tungkol sa isyu or need mo i-settle"
    LetterType.BRGY_CITY_REQUEST -> "Request letter sa barangay o city hall para sa lugar nyo"
    LetterType.SCHOLARSHIP -> "form para mag apply ng scholarship"
    LetterType.OJT_INTERNSHIP -> "Application letter para sa fresh grad na mag OJT"
    LetterType.OTHERS_REQUEST -> "other requests pa na gusto mo? tap mo lang luv"
    LetterType.CUSTOM -> "Gumawa ng sarili mong klase ng letter"
    else -> "Tap to create this letter"
},
                    onClick = { onPick(type) }
                )
            }

            item {
                Spacer(Modifier.height(18.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(16.dp), ambientColor = UrGreen, spotColor = UrPink)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF0A0A0A))
                        .border(
                            BorderStroke(1.5.dp, Brush.linearGradient(listOf(UrPink, UrGreen))),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CrownIcon(modifier = Modifier.size(30.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Go Premium", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("Unlock all templates and premium features", color = UrGray, fontSize = 11.sp)
                    }
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(UrPink)
                            .clickable { onPremiumTap() }
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("UPGRADE NOW", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFF0A0A0A))
                .border(
                    BorderStroke(1.5.dp, Brush.linearGradient(listOf(UrPink, UrGreen))),
                    RoundedCornerShape(24.dp)
                )
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(Icons.Filled.Home, "Home", false) { onNavigate("home") }
            BottomNavItem(Icons.Filled.Description, "Resume", false) { onNavigate("resume") }
            BottomNavItem(Icons.Filled.Person, "Bio-Data", false) { onNavigate("biodata") }
            BottomNavItem(Icons.Filled.Email, "Letters", true) { }
            BottomNavItem(Icons.Filled.List, "Forms", false) { onNavigate("govt_forms") }
            BottomNavItem(Icons.Filled.Settings, "Settings", false) { onNavigate("settings") }
        }
    }
}

@Composable
private fun SectionLabel(text: String, color: Color) {
    Column {
        Text(text, color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Spacer(Modifier.height(4.dp))
        Box(modifier = Modifier.width(50.dp).height(2.dp).background(color))
    }
}

@Composable
private fun LetterCard(
    icon: @Composable (Modifier) -> Unit,
    badge: @Composable (Modifier) -> Unit,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(UrCardBg)
            .border(1.dp, Brush.linearGradient(listOf(Color(0xFF4C8DFF), Color.Black)), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1F1F1F))
                .border(1.dp, Brush.linearGradient(listOf(Color(0xFF4C8DFF), Color.Black)), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            icon(Modifier.size(24.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp)
                    .size(19.dp)
                    .clip(CircleShape)
                    .background(UrGreen)
                    .border(1.5.dp, Color.Black, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                badge(Modifier.size(12.dp))
            }
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(subtitle, color = UrGray, fontSize = 12.sp)
        }
        Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = UrPink)
    }
}

@Composable
private fun BottomNavItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, active: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(icon, contentDescription = label, tint = if (active) UrPink else UrGray, modifier = Modifier.size(22.dp))
        Spacer(Modifier.height(2.dp))
        Text(label, color = if (active) Color.White else UrGray, fontSize = 10.sp)
    }
}

@Composable
private fun AllTemplatesContent(onBack: () -> Unit, onPick: (LetterType) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.ChevronLeft, contentDescription = "Back", tint = Color.White,
                modifier = Modifier.size(28.dp).clickable { onBack() }
            )
            Spacer(Modifier.width(8.dp))
            Text("Lahat ng Templates", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(LetterType.entries, key = { it.name }) { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(UrCardBg)
                        .border(1.dp, Brush.linearGradient(listOf(Color(0xFF4C8DFF), Color.Black)), RoundedCornerShape(14.dp))
                        .clickable { onPick(type) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(type.label, color = Color.White, fontSize = 15.sp, modifier = Modifier.weight(1f))
                    Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = UrPink)
                }
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LetterFormContent(
    viewModel: LettersViewModel,
    initialType: LetterType,
    onBack: () -> Unit
) {
    var selectedType by remember { mutableStateOf(initialType) }
    var fullName by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var dateNeeded by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.ChevronLeft, contentDescription = "Back", tint = Color.White,
                modifier = Modifier.size(28.dp).clickable { onBack() }
            )
            Spacer(Modifier.width(8.dp))
            Text("✉️ Letter Generator", color = Color.White, style = MaterialTheme.typography.titleLarge)
        }
        Spacer(modifier = Modifier.height(16.dp))

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
            OutlinedTextField(
                value = selectedType.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Klase ng Letter") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                LetterType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.label) },
                        onClick = { selectedType = type; expanded = false }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = fullName, onValueChange = { fullName = it },
            label = { Text("Buong Pangalan") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = position, onValueChange = { position = it },
            label = { Text("Position (optional)") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = company, onValueChange = { company = it },
            label = { Text("Company / Office (optional)") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = reason, onValueChange = { reason = it },
            label = { Text("Rason") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = dateNeeded, onValueChange = { dateNeeded = it },
            label = { Text("Petsa (hal. July 20, 2026)") }, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                viewModel.generate(
                    LetterRequest(
                        type = selectedType,
                        fullName = fullName,
                        position = position,
                        company = company,
                        reason = reason,
                        dateNeeded = dateNeeded
                    )
                )
            },
            enabled = !uiState.isLoading && fullName.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isLoading) "Ginagawa..." else "Generate Letter")
        }

        uiState.error?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text("⚠️ $it", color = MaterialTheme.colorScheme.error)
        }

        uiState.generatedLetter?.let { letter ->
            Spacer(modifier = Modifier.height(20.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = letter,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
