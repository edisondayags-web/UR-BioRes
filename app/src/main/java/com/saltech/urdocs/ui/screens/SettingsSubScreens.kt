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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Reusable scaffold para sa 4 na detail screens -- parehong neon look
 * ng Settings screen (back arrow + title + scrollable body).
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
            .background(SettingsColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp, 24.dp, 20.dp, 40.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = SettingsColors.NeonPink)
                }
                Spacer(Modifier.width(4.dp))
                Text(title, color = SettingsColors.TextWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(SettingsColors.CardBg)
                    .border(1.dp, SettingsColors.NeonPink, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun BodyText(text: String) {
    Text(text, color = SettingsColors.TextMuted, fontSize = 13.sp, lineHeight = 20.sp)
    Spacer(Modifier.height(14.dp))
}

@Composable
private fun BodyHeading(text: String) {
    Text(text, color = SettingsColors.NeonGreen, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(6.dp))
}

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    DetailScreenScaffold(title = "Privacy Policy", onBack = onBack) {
        BodyHeading("Ano ang kinokolekta namin")
        BodyText("Ang UR BioRes ay ginawa para tumulong sa paggawa ng resume, bio-data, at liham nang lokal sa device mo. Ang mga detalyeng inilalagay mo sa mga form (pangalan, contact info, work history, atbp.) ay ginagamit lamang para buuin ang dokumentong nire-request mo.")

        BodyHeading("Saan napupunta ang data")
        BodyText("Ang mga nilagay mong impormasyon ay hindi awtomatikong ini-upload o ibinabahagi sa external servers. Kapag pinindot mo ang Download, ang nagawang dokumento ay dine-download bilang image file diretso sa Gallery ng iyong device.")

        BodyHeading("Access sa storage/photos")
        BodyText("Hinihiling lang namin ang access na kailangan para mag-save ng nabuong resume o dokumento sa iyong Gallery, at para mag-upload ka ng sariling larawan (halimbawa sa 2x2 photo box ng Traditional Resume).")

        BodyHeading("Mga pagbabago")
        BodyText("Maaaring i-update ang Privacy Policy na ito habang umuunlad ang UR BioRes bilang bahagi ng Sal-Tech startup. Ipapaalam namin ang mahahalagang pagbabago sa loob ng app.")
    }
}

@Composable
fun TermsConditionsScreen(onBack: () -> Unit) {
    DetailScreenScaffold(title = "Terms & Conditions", onBack = onBack) {
        BodyHeading("Paggamit ng App")
        BodyText("Sa paggamit ng UR BioRes, sumasang-ayon kang gagamitin ang app para sa personal na paggawa ng resume, bio-data, gov't forms, at liham. Bawal gamitin ang app para sa mga ilegal o mapanlinlang na layunin.")

        BodyHeading("Accuracy ng Impormasyon")
        BodyText("Responsibilidad mo bilang user na tiyakin ang tama at totoong impormasyon sa mga dokumentong nabubuo gamit ang app. Ang UR BioRes ay isang tool lamang para sa formatting at hindi verifier ng katotohanan ng nilalaman.")

        BodyHeading("Availability")
        BodyText("Dahil ang UR BioRes ay produkto ng Sal-Tech, isang upcoming startup, maaaring magkaroon ng downtime, updates, o pagbabago sa mga feature habang patuloy itong dinedevelop.")

        BodyHeading("Limitation of Liability")
        BodyText("Hindi mananagot ang Sal-Tech at ang developer sa anumang isyu na maaaring idulot ng paggamit ng mga dokumentong nabuo gamit ang app (halimbawa: pagtanggi ng employer o ahensya sa naisumiteng dokumento).")
    }
}

@Composable
fun DataPermissionsScreen(onBack: () -> Unit) {
    DetailScreenScaffold(title = "Data & Permissions", onBack = onBack) {
        BodyHeading("📷 Photos / Media")
        BodyText("Kailangan para mapili mo ang larawan na ilalagay sa 2x2 photo box (Traditional Resume) at para ma-save ang nabuong dokumento pabalik sa Gallery mo.")

        BodyHeading("💾 Storage")
        BodyText("Ginagamit para i-save ang final na resume/bio-data/liham bilang image file sa iyong device, sa loob ng Pictures/URDocs folder.")

        BodyHeading("🌐 Internet")
        BodyText("Ginagamit lamang para sa mga update sa app sa hinaharap; hindi ito ginagamit para i-upload ang mga personal detalyeng inilalagay mo sa mga form.")

        BodyHeading("Bakit lang namin hinihingi ang mga 'to")
        BodyText("Layunin ng UR BioRes na panatilihing lokal sa device mo ang iyong data. Ang mga permission na hinihingi ay para lamang sa core function ng app: paggawa at pag-save ng dokumento.")
    }
}

@Composable
fun AboutDeveloperScreen(onBack: () -> Unit) {
    DetailScreenScaffold(title = "About Developer", onBack = onBack) {
        BodyHeading("Edison Suclatan Dayaguit")
        BodyText("Developer at nag-iisang gumagawa ng UR BioRes, bahagi ng Sal-Tech -- isang upcoming startup na base sa Pilipinas.")

        BodyHeading("Tech Background")
        BodyText("Bukod sa Kotlin/Android development na ginamit sa app na 'to, nagde-develop din ako gamit ang Rust at Ada/SPARK -- mga language na kilala sa performance, memory safety, at reliability, kadalasang ginagamit sa systems programming at high-assurance software.")

        BodyHeading("Tungkol sa UR BioRes")
        BodyText("Ginawa ang UR BioRes para gawing mas madali ang paggawa ng resume, bio-data, gov't forms, at liham para sa mga Pilipino -- direkta sa phone, walang kailangang laptop o printed template.")

        BodyHeading("Sal-Tech")
        BodyText("Ang Sal-Tech ay startup na kasalukuyang inihahanda ang mga papeles at registration nito. Ang UR BioRes ang unang produkto sa ilalim ng brand na ito.")
    }
}
