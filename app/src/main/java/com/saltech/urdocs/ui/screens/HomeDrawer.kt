package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.background
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.rotate
import com.saltech.urdocs.BuildConfig
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val DBlue = Color(0xFF4C8DFF)
private val DBg = Color(0xFF050A16)
private val DText = Color(0xFFE8ECF5)
private val DDim = Color(0xFFA9B4CC)
private val ROW = 32.dp

data class DrawerChat(val id: Long, val title: String)

@Composable
fun HomeDrawerContent(
    email: String?,
    loggedIn: Boolean,
    chats: List<DrawerChat>,
    recents: List<String>,
    onNavigate: (String) -> Unit,
    onNewChat: () -> Unit,
    onOpenChat: (Long) -> Unit,
    onUpgrade: () -> Unit,
    onLogout: () -> Unit
) {
    val shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp).border(1.dp, DBlue.copy(alpha = 0.45f), shape),
        drawerShape = shape,
        drawerContainerColor = DBg,
        drawerTonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(DBg, Color(0xFF07122B))))
        ) {
            // ---- Profile (fixed) ----
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate("my_profile") }
                    .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .shadow(14.dp, CircleShape, ambientColor = DBlue, spotColor = DBlue)
                        .clip(CircleShape)
                        .background(DBg)
                        .border(2.dp, DBlue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Person, null, tint = DDim, modifier = Modifier.size(26.dp))
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("Profile", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(
                        if (email.isNullOrBlank()) "Guest" else email,
                        color = DBlue, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Box(
                Modifier.fillMaxWidth().height(1.dp)
                    .background(Brush.horizontalGradient(listOf(Color.Transparent, DBlue, Color.Transparent)))
            )
            Spacer(Modifier.height(6.dp))

            // ---- Menu (fixed, hindi scroll) ----
            DrawerItem(Icons.Outlined.Edit, "Drafts", "(save if no payment)") { onNavigate("drafts") }
            DrawerItem(Icons.Outlined.Settings, "Settings") { onNavigate("settings") }
            DrawerItem(Icons.Outlined.Description, "RESUME") { onNavigate("resume") }
            DrawerItem(Icons.Outlined.Badge, "BIODATA") { onNavigate("biodata") }
            DrawerItem(Icons.Outlined.Mail, "LETTERS") { onNavigate("letters") }
            DrawerItem(Icons.Outlined.Mic, "INTERVIEW") { onNavigate("interview") }
            DrawerItem(Icons.Outlined.Search, "JOB RESEARCHER") { onNavigate("job_researcher") }

            Spacer(Modifier.height(6.dp))

            // ---- YOUR CHATS (auto-shrink, max 4 rows, scroll sa loob lang) ----
            Column(Modifier.weight(1f, fill = false)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 14.dp, top = 6.dp, bottom = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "YOUR CHATS", color = DBlue, fontSize = 12.sp,
                        fontWeight = FontWeight.Bold, letterSpacing = 3.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        Icons.Outlined.Add, "New chat", tint = DBlue,
                        modifier = Modifier.size(22.dp).clickable { onNewChat() }
                    )
                }
                if (chats.isEmpty()) {
                    Text("Wala pang chats", color = DDim.copy(alpha = 0.6f), fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
                } else {
                    LazyColumn(Modifier.weight(1f, fill = false).heightIn(max = ROW * 4)) {
                        items(chats, key = { it.id }) { chat ->
                            Row(
                                modifier = Modifier.fillMaxWidth().height(ROW)
                                    .clickable { onOpenChat(chat.id) }
                                    .padding(horizontal = 20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Outlined.ChatBubbleOutline, null, tint = DBlue, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(14.dp))
                                Text(chat.title, color = DText, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            }

            // ---- RECENTS (auto-shrink, max 4 rows, scroll sa loob lang) ----
            Column(Modifier.weight(1f, fill = false)) {
                Text(
                    "RECENTS", color = DBlue, fontSize = 12.sp,
                    fontWeight = FontWeight.Bold, letterSpacing = 3.sp,
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 2.dp)
                )
                if (recents.isEmpty()) {
                    Text("Wala pang recents", color = DDim.copy(alpha = 0.6f), fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
                } else {
                    LazyColumn(Modifier.weight(1f, fill = false).heightIn(max = ROW * 4)) {
                        items(recents) { name ->
                            Row(
                                modifier = Modifier.fillMaxWidth().height(ROW)
                                    .clickable { onNavigate("drafts") }
                                    .padding(horizontal = 20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Outlined.Description, null, tint = DDim, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(14.dp))
                                Text(name, color = DDim, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            }

            // ---- Upgrade (fixed, may umiikot na scan line) ----
            ScanBorderCard(onClick = onUpgrade) {
                Box(
                    Modifier.size(36.dp).border(1.5.dp, DBlue, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Outlined.Star, null, tint = DBlue, modifier = Modifier.size(24.dp)) }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Upgrade to Pro 99", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Unlock templates like resume • biodata • letter • etc",
                        color = DBlue, fontSize = 10.sp, lineHeight = 13.sp)
                }
            }

            // ---- Log out (lalabas lang kung may email login) ----
            run {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { if (loggedIn) onLogout() else onNavigate("login") }
                        .padding(horizontal = 16.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(24.dp).border(1.dp, DBlue, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.AutoMirrored.Outlined.Logout, null, tint = DBlue, modifier = Modifier.size(14.dp)) }
                    Spacer(Modifier.width(10.dp))
                    Text(if (loggedIn) "Log out" else "Log in", color = DText, fontSize = 13.sp)
                }
            }
            Box(
                Modifier.fillMaxWidth().height(1.dp)
                    .background(Brush.horizontalGradient(listOf(Color.Transparent, DBlue.copy(alpha = 0.5f), Color.Transparent)))
            )
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Code, null, tint = DBlue, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(8.dp))
                    Box(Modifier.width(1.dp).height(24.dp).background(DBlue.copy(alpha = 0.5f)))
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("DEVELOPER:", color = DDim, fontSize = 7.sp, letterSpacing = 2.sp)
                        Text("EDISON SUCLATAN DAYAGUIT", color = DBlue, fontSize = 10.sp,
                            fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                    }
                }
                Spacer(Modifier.height(6.dp))
                Text("© 2026 EDISON SUCLATAN DAYAGUIT", color = DDim.copy(alpha = 0.85f), fontSize = 7.sp, letterSpacing = 0.5.sp)
                Text("ALL RIGHTS RESERVED", color = DDim.copy(alpha = 0.85f), fontSize = 7.sp, letterSpacing = 0.5.sp)
                Spacer(Modifier.height(6.dp))
                Box(
                    Modifier.border(1.dp, DBlue, RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 2.dp)
                ) {
                    Text("Version ${BuildConfig.VERSION_NAME}", color = DBlue, fontSize = 8.sp)
                }
            }
        }
    }
}

@Composable
private fun DrawerItem(icon: ImageVector, title: String, subtitle: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = DBlue, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(14.dp))
        Column {
            Text(title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            if (subtitle != null) Text(subtitle, color = DBlue, fontSize = 10.sp)
        }
    }
}

@Composable
private fun ScanBorderCard(onClick: () -> Unit, content: @Composable RowScope.() -> Unit) {
    val transition = rememberInfiniteTransition(label = "scan")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), RepeatMode.Restart),
        label = "scanAngle"
    )
    val outer = RoundedCornerShape(14.dp)
    val inner = RoundedCornerShape(12.5.dp)
    Box(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .fillMaxWidth()
            .shadow(10.dp, outer, ambientColor = DBlue, spotColor = DBlue)
            .clip(outer)
            .drawBehind {
                val radius = kotlin.math.hypot(size.width, size.height) / 2f
                rotate(angle, pivot = center) {
                    drawCircle(
                        brush = Brush.sweepGradient(
                            0f to Color(0xFF1B3A7A),
                            0.55f to Color(0xFF1B3A7A),
                            0.85f to DBlue,
                            0.95f to Color.White,
                            1f to Color(0xFF1B3A7A),
                            center = center
                        ),
                        radius = radius,
                        center = center
                    )
                }
            }
            .padding(1.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(inner)
                .background(Brush.horizontalGradient(listOf(Color(0xFF0B1B44), Color(0xFF07122B))))
                .clickable { onClick() }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}
