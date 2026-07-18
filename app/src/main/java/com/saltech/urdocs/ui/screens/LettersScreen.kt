package com.saltech.urdocs.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saltech.urdocs.model.LetterRequest
import com.saltech.urdocs.model.LetterType
import com.saltech.urdocs.viewmodel.LettersViewModel

// Kulay na hango sa Canva design
private val UrPink = Color(0xFFFF2E7E)
private val UrGreen = Color(0xFF39FF6A)
private val UrCardBg = Color(0xFF161616)
private val UrGray = Color(0xFF9A9A9A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LettersScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: LettersViewModel = viewModel()
) {
    // "hub" = yung Canva design (buod ng mga letter), "all" = buong listahan,
    // "form" = yung totoong form na gumagawa ng letter (existing logic).
    var screenState by remember { mutableStateOf("hub") }
    var selectedType by remember { mutableStateOf(LetterType.entries.first()) }
    val context = LocalContext.current

    fun findType(keyword: String): LetterType =
        LetterType.entries.firstOrNull { it.label.contains(keyword, ignoreCase = true) }
            ?: LetterType.entries.first()

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        when (screenState) {
            "form" -> LetterFormContent(
                viewModel = viewModel,
                initialType = selectedType,
                onBack = { screenState = "hub" }
            )
            "all" -> AllTemplatesContent(
                onBack = { screenState = "hub" },
                onPick = { type -> selectedType = type; screenState = "form" }
            )
            else -> LettersHubContent(
                onPremiumTap = { Toast.makeText(context, "Premium -- Coming Soon!", Toast.LENGTH_SHORT).show() },
                onPopularTap = { keyword -> selectedType = findType(keyword); screenState = "form" },
                onMoreTemplates = { screenState = "all" },
                onNavigate = onNavigate
            )
        }
    }
}

@Composable
private fun LettersHubContent(
    onPremiumTap: () -> Unit,
    onPopularTap: (String) -> Unit,
    onMoreTemplates: () -> Unit,
    onNavigate: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(20.dp))

            // ===== HEADER =====
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF2A0A16))
                        .border(1.dp, UrPink.copy(alpha = 0.6f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Email, contentDescription = null, tint = UrPink, modifier = Modifier.size(26.dp))
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
                        .border(1.dp, UrPink.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                        .clickable { onPremiumTap() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = UrPink, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("PREMIUM", color = UrPink, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                }
            }

            Spacer(Modifier.height(20.dp))

            // ===== HERO CARD =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF0D0D0D))
                    .border(
                        BorderStroke(1.dp, Brush.linearGradient(listOf(UrPink, UrGreen))),
                        RoundedCornerShape(18.dp)
                    )
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            buildString { },
                            fontSize = 0.sp
                        )
                        Text(
                            "Create Professional",
                            color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp
                        )
                        Row {
                            Text("Letters", color = UrPink, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text(" in Minutes", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }
                    Text("📝✉️🖊️", fontSize = 30.sp)
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

            LetterCard(
                icon = Icons.Filled.Work, badgeEmoji = "🏖️",
                title = "Leave Letter",
                subtitle = "Request for leave from work or school",
                onClick = { onPopularTap("Leave") }
            )
            Spacer(Modifier.height(12.dp))
            LetterCard(
                icon = Icons.Filled.SwapHoriz, badgeEmoji = "➡️",
                title = "Resignation Letter",
                subtitle = "Formal resignation from your position",
                onClick = { onPopularTap("Resign") }
            )
            Spacer(Modifier.height(12.dp))
            LetterCard(
                icon = Icons.Filled.Edit, badgeEmoji = "❤️",
                title = "Excuse Letter",
                subtitle = "Apology or reason for absence",
                onClick = { onPopularTap("Excuse") }
            )

            Spacer(Modifier.height(22.dp))
            SectionLabel("OTHER", UrPink)
            Spacer(Modifier.height(10.dp))

            LetterCard(
                icon = Icons.Filled.Apps, badgeEmoji = "➕",
                title = "More Letter Templates",
                subtitle = "View all templates",
                onClick = onMoreTemplates
            )

            Spacer(Modifier.height(18.dp))

            // ===== GO PREMIUM BANNER =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0D0D0D))
                    .border(
                        BorderStroke(1.dp, Brush.linearGradient(listOf(UrGreen, UrPink))),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = UrPink, modifier = Modifier.size(28.dp))
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Go Premium", color = UrPink, fontWeight = FontWeight.Bold, fontSize = 15.sp)
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

        // ===== BOTTOM NAV =====
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0A0A0A))
                .border(BorderStroke(1.dp, Color(0xFF222222)))
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(Icons.Filled.Home, "Home", false) { onNavigate("home") }
            BottomNavItem(Icons.Filled.Description, "Resume", false) { onNavigate("resume") }
            BottomNavItem(Icons.Filled.Person, "Bio-Data", false) { onNavigate("biodata") }
            BottomNavItem(Icons.Filled.Email, "Letters", true) { }
            BottomNavItem(Icons.Filled.List, "Forms", false) { onNavigate("govt_forms") }
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
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    badgeEmoji: String,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(UrCardBg)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1F1F1F)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = UrPink, modifier = Modifier.size(24.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 4.dp, y = 4.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(badgeEmoji, fontSize = 10.sp)
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
        Text(label, color = if (active) UrPink else UrGray, fontSize = 10.sp)
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
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            LetterType.entries.forEach { type ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(UrCardBg)
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

