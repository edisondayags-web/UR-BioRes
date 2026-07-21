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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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

data class GovtLink(val id: Int, val name: String, val url: String, val initials: String)
data class GovtCategory(val index: Int, val title: String, val links: List<GovtLink>)
private val categoryFilters = listOf(
    CategoryFilter("All", Icons.Filled.GridView, null, GGreen),
    CategoryFilter("Big 5", Icons.Filled.Star, 1, GPink),
    CategoryFilter("IDs & Clearances", Icons.Filled.Badge, 2, GGreen),
    CategoryFilter("Trabaho & Business", Icons.Filled.Work, 3, GPink),
    CategoryFilter("Health & Safety", Icons.Filled.Favorite, 4, GPink),
    CategoryFilter("Job Portals", Icons.Filled.People, 5, GGreen),
    CategoryFilter("Education", Icons.Filled.School, 6, GGreen),
    CategoryFilter("Bayad & Pera", Icons.Filled.Payments, 7, GPink)
)

private val govtCategories = listOf(
    GovtCategory(1, "GOV'T REQUIREMENTS -- THE BIG 5", listOf(
        GovtLink(1, "PSA", "https://appointment.psa.gov.ph/", "PSA"),
        GovtLink(2, "SSS", "https://www.sss.gov.ph/", "SSS"),
        GovtLink(3, "PAG-IBIG", "https://www.pagibigfund.gov.ph/", "PI"),
        GovtLink(4, "PHILHEALTH", "https://www.philhealth.gov.ph/", "PH"),
        GovtLink(5, "BIR", "https://www.bir.gov.ph/", "BIR")
    )),
    GovtCategory(2, "CLEARANCES, IDS & PASSPORT", listOf(
        GovtLink(6, "NBI", "https://nbi.gov.ph/", "NBI"),
        GovtLink(7, "DFA -- Passport", "https://dfa.gov.ph/", "DFA"),
        GovtLink(8, "LTO", "https://lto.gov.ph/", "LTO"),
        GovtLink(9, "Bureau of Immigration", "https://immigration.gov.ph/", "BI"),
        GovtLink(10, "National ID", "https://national-id.gov.ph/", "ID"),
        GovtLink(11, "PRC", "https://www.prc.gov.ph/", "PRC")
    )),
    GovtCategory(3, "TRABAHO, OFW & BUSINESS", listOf(
        GovtLink(12, "DMW", "https://dmw.gov.ph/", "DMW"),
        GovtLink(13, "POEA", "https://www.poea.gov.ph/", "POEA"),
        GovtLink(14, "OWWA", "https://www.owwa.gov.ph/", "OWWA"),
        GovtLink(15, "DOLE", "https://dole.gov.ph/", "DOLE"),
        GovtLink(16, "TESDA", "https://www.tesda.gov.ph/", "TES"),
        GovtLink(17, "CSC", "https://csc.gov.ph/", "CSC"),
        GovtLink(18, "DTI Business Name", "https://bnrs.dti.gov.ph/", "DTI"),
        GovtLink(19, "SEC", "https://www.sec.gov.ph/", "SEC"),
        GovtLink(20, "PEZA", "https://www.peza.gov.ph/", "PEZA"),
        GovtLink(21, "BOI", "https://boi.gov.ph/", "BOI"),
        GovtLink(22, "PhilGEPS", "https://ps-philgeps.gov.ph/", "PG")
    )),
    GovtCategory(4, "HEALTH & SAFETY", listOf(
        GovtLink(23, "FDA", "https://www.fda.gov.ph/", "FDA"),
        GovtLink(24, "DOH", "https://doh.gov.ph/", "DOH"),
        GovtLink(25, "BOQ (Yellow Card)", "https://www.boq.gov.ph/", "BOQ"),
        GovtLink(26, "Red Cross", "https://redcross.org.ph/", "RC")
    )),
    GovtCategory(5, "JOB PORTALS", listOf(
        GovtLink(27, "Jobstreet", "https://www.jobstreet.com.ph/", "JS"),
        GovtLink(28, "LinkedIn Jobs", "https://www.linkedin.com/jobs/", "LI"),
        GovtLink(29, "Kalibrr", "https://www.kalibrr.com/", "KLB"),
        GovtLink(30, "Indeed PH", "https://ph.indeed.com/", "IND")
    )),
    GovtCategory(6, "EDUCATION & SCHOLARSHIP", listOf(
        GovtLink(31, "CHED", "https://ched.gov.ph/", "CHED"),
        GovtLink(32, "DepEd", "https://www.deped.gov.ph/", "DEP"),
        GovtLink(33, "TESDA Courses", "https://www.tesda.gov.ph/", "TES")
    )),
    GovtCategory(7, "BAYAD, BANKO & PERA", listOf(
        GovtLink(34, "BSP", "https://www.bsp.gov.ph/", "BSP"),
        GovtLink(35, "BIR ePayment", "https://www.bir.gov.ph/", "BIR"),
        GovtLink(36, "PAG-IBIG MP2", "https://www.pagibigfund.gov.ph/", "MP2")
    )),
    GovtCategory(8, "LOCAL GOV'T & IBA PA", listOf(
        GovtLink(37, "GOV.PH Portal", "https://www.gov.ph/", "GOV"),
        GovtLink(38, "DILG", "https://dilg.gov.ph/", "DILG"),
        GovtLink(39, "COMELEC", "https://comelec.gov.ph/", "COM"),
        GovtLink(40, "MMDA", "https://www.mmda.gov.ph/", "MMDA")
    ))
)

private val categoryFilters = listOf(
    CategoryFilter("All", Icons.Filled.GridView, null),
    CategoryFilter("Big 5", Icons.Filled.Star, 1),
    CategoryFilter("IDs & Clearances", Icons.Filled.Badge, 2),
    CategoryFilter("Trabaho & Business", Icons.Filled.Work, 3),
    CategoryFilter("Health & Safety", Icons.Filled.Favorite, 4),
    CategoryFilter("Job Portals", Icons.Filled.People, 5),
    CategoryFilter("Education", Icons.Filled.School, 6),
    CategoryFilter("Bayad & Pera", Icons.Filled.Payments, 7)
)

@Composable
fun GovtFormsScreen(onNavigate: (String) -> Unit = {}) {
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    var expandedCategory by remember { mutableStateOf<Int?>(1) }
    var selectedFilter by remember { mutableStateOf(0) }

    fun openLink(url: String) {
        try {
            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(context, Uri.parse(url))
        } catch (e: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    val filterCatIndex = categoryFilters[selectedFilter].categoryIndex
    val baseCategories = if (filterCatIndex == null) govtCategories else govtCategories.filter { it.index == filterCatIndex }

    val filteredCategories = if (query.isBlank()) {
        baseCategories
    } else {
        baseCategories.mapNotNull { cat ->
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
                Icon(Icons.Filled.AccountBalance, contentDescription = null, tint = GGreen, modifier = Modifier.size(22.dp))
                Row {
                    Text("BODEGA", color = GPink, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(" NG ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("LINKS", color = GGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Text("G O V ' T   W E B S I T E S", color = GGray, fontSize = 10.sp)
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

        Spacer(Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(categoryFilters) { idx, filter ->
                val isSelected = idx == selectedFilter
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) GCardBg else Color.Transparent)
                        .border(1.dp, if (isSelected) GPink else GGray.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                        .clickable {
                            selectedFilter = idx
                            expandedCategory = filter.categoryIndex
                        }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .widthIn(min = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(filter.icon, contentDescription = null, tint = filter.iconColor, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.height(4.dp))
                    Text(filter.label, color = if (isSelected) Color.White else GGray, fontSize = 10.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        val totalLinks = filteredCategories.sumOf { it.links.size }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("$totalLinks", color = GGreen, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(Modifier.width(6.dp))
        Text("OFFICIAL LINKS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
    Spacer(Modifier.height(3.dp))
    Box(modifier = Modifier.width(28.dp).height(2.dp).background(GGreen))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Tap any link to open", color = GGray, fontSize = 11.sp)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Filled.AutoAwesome, contentDescription = null, tint = GGreen, modifier = Modifier.size(12.dp))
            }
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
                                        modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(link.initials, color = Color.Black, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(Modifier.width(10.dp))
                                    Box(
                                        modifier = Modifier.size(20.dp).clip(CircleShape).background(GPink.copy(alpha = 0.15f)).border(1.dp, GPink, CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("${link.id}", color = GPink, fontSize = 8.sp, fontWeight = FontWeight.Bold)
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
                Column(modifier = Modifier.weight(1f)) {
                    Text("Disclaimer:", color = GPink, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("UR BioRes is not affiliated. We only provide access to official sites.", color = GGray, fontSize = 11.sp)
                }
                Icon(Icons.Filled.VerifiedUser, contentDescription = null, tint = GGreen, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}
