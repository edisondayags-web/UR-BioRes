package com.saltech.urdocs.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val GPink = Color(0xFFFF2E7E)
private val GGreen = Color(0xFF39FF6A)
private val GGray = Color(0xFF9A9A9A)
private val GCardBg = Color(0xFF0F0F0F)

data class GovtLink(val id: Int, val name: String, val url: String)
data class GovtCategory(val index: Int, val title: String, val links: List<GovtLink>)

private val govtCategories = listOf(
    GovtCategory(1, "GOV'T REQUIREMENTS -- THE BIG 5", listOf(
        GovtLink(1, "PSA", "https://appointment.psa.gov.ph/"),
        GovtLink(2, "SSS", "https://www.sss.gov.ph/"),
        GovtLink(3, "PAG-IBIG", "https://www.pagibigfund.gov.ph/"),
        GovtLink(4, "PHILHEALTH", "https://www.philhealth.gov.ph/"),
        GovtLink(5, "BIR", "https://www.bir.gov.ph/")
    )),
    GovtCategory(2, "CLEARANCES, IDS & PASSPORT", listOf(
        GovtLink(6, "NBI", "https://nbi.gov.ph/"),
        GovtLink(7, "DFA -- Passport", "https://dfa.gov.ph/"),
        GovtLink(8, "LTO", "https://lto.gov.ph/"),
        GovtLink(9, "Bureau of Immigration", "https://immigration.gov.ph/"),
        GovtLink(10, "National ID", "https://national-id.gov.ph/"),
        GovtLink(11, "PRC", "https://www.prc.gov.ph/")
    )),
    GovtCategory(3, "TRABAHO, OFW & BUSINESS", listOf(
        GovtLink(12, "DMW", "https://dmw.gov.ph/"),
        GovtLink(13, "POEA", "https://www.poea.gov.ph/"),
        GovtLink(14, "OWWA", "https://www.owwa.gov.ph/"),
        GovtLink(15, "DOLE", "https://dole.gov.ph/"),
        GovtLink(16, "TESDA", "https://www.tesda.gov.ph/"),
        GovtLink(17, "CSC", "https://csc.gov.ph/"),
        GovtLink(18, "DTI Business Name", "https://bnrs.dti.gov.ph/"),
        GovtLink(19, "SEC", "https://www.sec.gov.ph/"),
        GovtLink(20, "PEZA", "https://www.peza.gov.ph/"),
        GovtLink(21, "BOI", "https://boi.gov.ph/"),
        GovtLink(22, "PhilGEPS", "https://ps-philgeps.gov.ph/")
    )),
    GovtCategory(4, "HEALTH & SAFETY", listOf(
        GovtLink(23, "FDA", "https://www.fda.gov.ph/"),
        GovtLink(24, "DOH", "https://doh.gov.ph/"),
        GovtLink(25, "BOQ (Yellow Card)", "https://www.boq.gov.ph/"),
        GovtLink(26, "Red Cross", "https://redcross.org.ph/")
    )),
    GovtCategory(5, "JOB PORTALS", listOf(
        GovtLink(27, "Jobstreet", "https://www.jobstreet.com.ph/"),
        GovtLink(28, "LinkedIn Jobs", "https://www.linkedin.com/jobs/"),
        GovtLink(29, "Kalibrr", "https://www.kalibrr.com/"),
        GovtLink(30, "Indeed PH", "https://ph.indeed.com/")
    )),
    GovtCategory(6, "EDUCATION & SCHOLARSHIP", listOf(
        GovtLink(31, "CHED", "https://ched.gov.ph/"),
        GovtLink(32, "DepEd", "https://www.deped.gov.ph/"),
        GovtLink(33, "TESDA Courses", "https://www.tesda.gov.ph/")
    )),
    GovtCategory(7, "BAYAD, BANKO & PERA", listOf(
        GovtLink(34, "BSP", "https://www.bsp.gov.ph/"),
        GovtLink(35, "BIR ePayment", "https://www.bir.gov.ph/"),
        GovtLink(36, "PAG-IBIG MP2", "https://www.pagibigfund.gov.ph/")
    )),
    GovtCategory(8, "LOCAL GOV'T & IBA PA", listOf(
        GovtLink(37, "GOV.PH Portal", "https://www.gov.ph/"),
        GovtLink(38, "DILG", "https://dilg.gov.ph/"),
        GovtLink(39, "COMELEC", "https://comelec.gov.ph/"),
        GovtLink(40, "MMDA", "https://www.mmda.gov.ph/")
    ))
)

@Composable
fun GovtFormsScreen(onNavigate: (String) -> Unit = {}) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    var expandedCategory by remember { mutableStateOf<Int?>(1) }

    fun openLink(url: String) {
        try {
            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(context, Uri.parse(url))
        } catch (e: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    val filteredCategories = if (query.isBlank()) {
        govtCategories
    } else {
        govtCategories.mapNotNull { cat ->
            val matches = cat.links.filter { it.name.contains(query, ignoreCase = true) }
            if (matches.isNotEmpty()) cat.copy(links = matches) else null
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp))
                    .border(1.dp, GPink, RoundedCornerShape(10.dp))
                    .clickable { onNavigate("home") },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = GPink)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Row {
                    Text("BODEGA", color = GPink, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(" NG ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("LINKS", color = GGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Text("GOV'T WEBSITES", color = GGray, fontSize = 11.sp)
            }
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).border(1.dp, GPink, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Favorite, contentDescription = null, tint = GPink, modifier = Modifier.size(20.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(GCardBg)
                .border(BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(GGreen, GPink))), RoundedCornerShape(24.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Search, contentDescription = null, tint = GGray, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            TextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Ano hahanapin mo luv?", color = GGray, fontSize = 13.sp) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
            )
            Icon(Icons.Filled.Tune, contentDescription = null, tint = GPink, modifier = Modifier.size(20.dp))
        }

        Spacer(Modifier.height(14.dp))

        val totalLinks = filteredCategories.sumOf { it.links.size }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$totalLinks", color = GGreen, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(Modifier.width(6.dp))
                Text("OFFICIAL LINKS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Text("Tap any link to open", color = GGray, fontSize = 11.sp)
        }

        Spacer(Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            filteredCategories.forEach { category ->
                val isExpanded = expandedCategory == category.index || query.isNotBlank()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(GCardBg)
                        .border(1.dp, GGreen.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedCategory = if (expandedCategory == category.index) null else category.index }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(26.dp).clip(CircleShape).border(1.dp, GPink, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("${category.index}", color = GPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(category.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).border(1.dp, GGreen, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("${category.links.size}", color = GGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = GGray,
                            modifier = Modifier.size(20.dp).rotate(if (isExpanded) 180f else 0f)
                        )
                    }

                    AnimatedVisibility(visible = isExpanded, enter = expandVertically(), exit = shrinkVertically()) {
                        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                            category.links.forEach { link ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { openLink(link.url) }
                                        .padding(vertical = 10.dp, horizontal = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(22.dp).clip(CircleShape).background(GPink.copy(alpha = 0.15f)).border(1.dp, GPink, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("${link.id}", color = GPink, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(link.name, color = Color.White, fontSize = 14.sp)
                                        Text(link.url, color = GGreen, fontSize = 10.sp)
                                    }
                                    Icon(Icons.Filled.OpenInNew, contentDescription = null, tint = GPink, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(GCardBg)
                    .border(1.dp, GGreen, RoundedCornerShape(14.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Shield, contentDescription = null, tint = GGreen, modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Disclaimer:", color = GPink, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("UR BioRes is not affiliated. We only provide access to official sites.", color = GGray, fontSize = 11.sp)
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}
