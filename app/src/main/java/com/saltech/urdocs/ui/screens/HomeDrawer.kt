package com.saltech.urdocs.ui.screens

import androidx.compose.foundation.background
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
private val ROW = 38.dp

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
                    .clickable { onNavigate("settings") }
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .shadow(14.dp, CircleShape, ambientColor = DBlue, spotColor = DBlue)
                        .clip(CircleShape)
                        .background(DBg)
                        .border(2.dp, DBlue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Person, null, tint = DDim, modifier = Modifier.size(32.dp))
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text("Profile", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(
                        if (email.isNullOrBlank()) "Guest" else email,
                        color = DBlue, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis
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
                                Icon(Icons.Outlined.ChatBubbleOutline, null, tint = DBlue, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(14.dp))
                                Text(chat.title, color = DText, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
                                Icon(Icons.Outlined.Description, null, tint = DDim, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(14.dp))
                                Text(name, color = DDim, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            }

            // ---- Upgrade (fixed) ----
            val cardShape = RoundedCornerShape(14.dp)
            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .shadow(10.dp, cardShape, ambientColor = DBlue, spotColor = DBlue)
                    .clip(cardShape)
                    .background(Brush.horizontalGradient(listOf(Color(0xFF0B1B44), Color(0xFF07122B))))
                    .border(1.dp, DBlue, cardShape)
                    .clickable { onUpgrade() }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.size(42.dp).border(1.5.dp, DBlue, RoundedCornerShape(10.dp)),
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
            if (loggedIn) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { onLogout() }
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.size(24.dp).border(1.dp, DBlue, RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.AutoMirrored.Outlined.Logout, null, tint = DBlue, modifier = Modifier.size(14.dp)) }
                    Spacer(Modifier.width(10.dp))
                    Text("Log out", color = DText, fontSize = 14.sp)
                }
            }
            Text(
                "DEVELOPER: EDISON SUCLATAN DAYAGUIT",
                color = DDim.copy(alpha = 0.45f), fontSize = 8.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 2.dp, bottom = 10.dp)
            )
        }
    }
}

@Composable
private fun DrawerItem(icon: ImageVector, title: String, subtitle: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = DBlue, modifier = Modifier.size(26.dp))
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            if (subtitle != null) Text(subtitle, color = DBlue, fontSize = 11.sp)
        }
    }
}
