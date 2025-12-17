package com.example.breastieproject.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breastieproject.ui.theme.*
import com.example.breastieproject.R


// Data Class untuk Carousel
data class CarouselData(
    val image: Int,
    val topText: String,
    val bottomText: String
)

// Data Class untuk Insight
data class InsightData(
    val icon: Int,
    val text: String
)

// Data Class untuk FAQ
data class FaqData(
    val icon: Int,
    val question: String,
    val sub: String
)

@Composable
fun HomeScreen(
    onReminderClick: () -> Unit,
    onProfileClick: () -> Unit,
    onCheckUpClick: (String) -> Unit // <-- UPDATE: Callback untuk navigasi ke Chatbot
) {
    val scrollState = rememberScrollState()

    // --- WARNA LOKAL (Sesuai Kode Anda) ---
    val PinkDarkBox = Color(0xFFEC7FA9)
    val PinkLightBox = Color(0xFFFCE4EC)
    val TextDarkPink = Color(0xFFAD1457)

    // Carousel Section 2
    val CarouselTextBg = Color(0xFFF2F2F7)
    val CarouselTextColor = Color(0xFFBE5985)

    // Feature Highlights Colors
    val FeatureCanvasPink = Color(0xFFFFB8E0)
    val FeatureCardBg = Color.White
    val FeatureIconBg = Color(0xFFEEEEEE)
    val FeatureIconTint = Color(0xFFC05D85)

    // Insight For You Colors
    val InsightTopGrey = Color(0xFFEEEEEE)
    val InsightBottomPink = Color(0xFFEC7FA9)
    val InsightContainerWhite = Color(0xFFFFFFFF)

    // FAQ Colors
    val FaqCanvasPink = Color(0xFFFFB8E0) // Warna Canvas Paling Luar
    val FaqInnerBox = Color(0xFFFFEDFA)   // Box pembungkus konten FAQ
    val FaqIconBg = Color(0xFFF5F5F5)

    // DATA SETUP
    val carouselItems = listOf(
        CarouselData(R.drawable.ic_1, "Good Morning Kinan", "Do you feel good?"),
        CarouselData(R.drawable.ic_4, "Deteksi dini bisa meningkatkan peluang sembuh hingga lebih dari 90%.", "Kamu lebih kuat dari yang kamu kira — langkah kecil hari ini berarti masa depan yang lebih sehat."),
        CarouselData(R.drawable.ic_5, "Sebagian besar benjolan payudara ternyata non-kanker — jadi jangan takut untuk memeriksakan diri.", "Keberanian bukan tanpa rasa takut, tapi bergerak meski takut."),
        CarouselData(R.drawable.ic_6, "Survivor breast cancer bisa hidup aktif dan sehat puluhan tahun setelah diagnosis", "Harapan selalu ada — dan kamu punya kekuatan untuk terus maju.")
    )

    val insightItems = listOf(
        InsightData(R.drawable.ic_healthy, "Don't forget your meds, take rest, take your power back."),
        InsightData(R.drawable.ic_drink, "Hydrate your body, elevate your mood."), // Pastikan ic_drink ada atau ganti ic_obat
        InsightData(R.drawable.ic_sleep, "Rest isn’t quitting — healing.")
    )

    val faqItems = listOf(
        FaqData(R.drawable.ic_11, "How to perform a self-breast exam properly?", "Explore tips and techniques"),
        FaqData(R.drawable.ic_12, "5 early signs you should never ignore", "Be informed, stay safe"),
        FaqData(R.drawable.ic_13, "Why hormone cycles affect breast texture?", "Understanding your body"),
        FaqData(R.drawable.ic_14, "What your breast pain may actually mean", "Learn the causes"),
        FaqData(R.drawable.ic_15, "Best foods for breast tissue health", "Diet tips for wellness"),
        FaqData(R.drawable.ic_16, "Caffeine lifestyle and breast tenderness explained?", "Find balance in consumption"),
        FaqData(R.drawable.ic_17, "How often should women do self-checks?", "Frequency and maintenance"),
        FaqData(R.drawable.ic_18, "Understanding breast lumps: benign vs risky", "Know the differences")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PinkBackground)
            .verticalScroll(scrollState)
    ) {

        // --- 2. GREETING SECTION ---
        // Parent Column sebagai pembungkus utama
        Column(
            modifier = Modifier
                .fillMaxWidth()
                // Memberi padding luar agar tidak mepet ke tepi layar dan elemen atas/bawah
                .padding(horizontal = 20.dp, vertical = 24.dp),
            // INI KUNCINYA: Memberi jarak vertikal antar elemen di dalamnya sebesar 24dp
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- BOX 1: Bagian Profil ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    // Opsional: Tambahkan rounded corner agar terlihat lebih seperti "box/card"
                    // .clip(RoundedCornerShape(16.dp))
                    .background(PinkLight)
                    // Padding internal di dalam box pink
                    .padding(all = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_avatar),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .clickable { onProfileClick() }
                    )
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            "Hi, ⋆. ˚Wonder Woman \uD835\uDF17\uD835\uDF1A˚⋆",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Black
                        )
                        Text(
                            "Here's your daily breast-care update",
                            fontSize = 12.sp,
                            color = Color(0xFF444444)
                        )
                    }
                }
            }

            // --- BOX 2: Bagian Reminder ---
            // (Spacer sudah tidak diperlukan karena sudah diatur oleh verticalArrangement induk)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    // Opsional: Tambahkan rounded corner
                    // .clip(RoundedCornerShape(16.dp))
                    .background(PinkLight)
                    // Padding internal di dalam box pink
                    .padding(all = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_3),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                            .clickable { onReminderClick() }
                    )
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            "H-100: Your next clinical breast check is approaching.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Black
                        )
                        Text(
                            "Tap to view schedule & recommendation",
                            fontSize = 12.sp,
                            color = Color(0xFF444444)
                        )
                    }
                }
            }
        }

        // --- CAROUSEL SLIDER ---
        val carouselCardHeight = 225.dp
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFFFFFFFF)).padding(vertical = 24.dp)) {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(carouselItems) { index, item ->
                    if (index == 0) {
                        // BOX 1 (WELCOME)
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillParentMaxWidth().height(carouselCardHeight),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Box(modifier = Modifier.weight(1f).fillMaxWidth().background(Color(0xFFF5F5F5)), contentAlignment = Alignment.Center) {
                                    Image(painter = painterResource(id = item.image), contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.fillMaxWidth(0.6f).fillMaxHeight(0.6f))
                                }
                                Column(modifier = Modifier.fillMaxWidth().background(PinkLightBox).padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("${item.topText}, Welcome to PinkBreastie!", color = TextDarkPink, fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                    Text(item.bottomText, color = TextDarkPink, fontSize = 12.sp, textAlign = TextAlign.Center)
                                }
                            }
                        }
                    } else {
                        // BOX 2 DST
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.width(320.dp).height(carouselCardHeight),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Row(modifier = Modifier.fillMaxWidth().weight(1f)) {
                                    Box(modifier = Modifier.weight(0.4f).fillMaxHeight().background(Color.White), contentAlignment = Alignment.Center) {
                                        Image(painter = painterResource(id = item.image), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                                    }
                                    Box(modifier = Modifier.weight(0.6f).fillMaxHeight().background(PinkDarkBox), contentAlignment = Alignment.CenterStart) {
                                        Text(item.topText, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(16.dp))
                                    }
                                }
                                Box(modifier = Modifier.fillMaxWidth().background(CarouselTextBg).padding(16.dp)) {
                                    Text(item.bottomText, color = CarouselTextColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 3. FEATURE HIGHLIGHTS ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(FeatureCanvasPink)
                .padding(vertical = 24.dp, horizontal = 20.dp)
        ) {
            Text("Feature Highlights", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextBlack, modifier = Modifier.padding(bottom = 16.dp))

            val featureHeight = 190.dp

            @Composable
            fun FeatureItemCard(iconRes: Int, title: String, subtitle: String, modifier: Modifier = Modifier, isHeartIcon: Boolean = false) {
                Card(
                    modifier = modifier.height(featureHeight),
                    colors = CardDefaults.cardColors(containerColor = FeatureCardBg),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(modifier = Modifier.fillMaxWidth().weight(1f).background(FeatureIconBg), contentAlignment = Alignment.Center) {
                            if (isHeartIcon) {
                                Box(modifier = Modifier.size(70.dp).clip(CircleShape).background(FeatureIconTint), contentAlignment = Alignment.Center) {
                                    Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(36.dp), colorFilter = ColorFilter.tint(Color.White))
                                }
                            } else {
                                Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(50.dp))
                            }
                        }
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(subtitle, fontSize = 11.sp, color = Color.Gray, lineHeight = 14.sp)
                            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black, maxLines = 1)
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FeatureItemCard(R.drawable.ic_community, "Community", "Mini Talks with Other", Modifier.weight(1f))
                    FeatureItemCard(R.drawable.ic_assistant, "Insight & Get personalized insights", "PinkBreastie Insight's", Modifier.weight(1f))
                }
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    FeatureItemCard(R.drawable.ic_heart, "Breast Journal", "Self-Check Guide", isHeartIcon = true, modifier = Modifier.width(170.dp))
                }
            }
        }

        // --- 4. INSIGHT FOR YOU ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(InsightContainerWhite)
                .padding(top = 24.dp, bottom = 24.dp)
        ) {
            Text(
                text = "Insight For You",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextBlack,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(insightItems) { _, item ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.width(175.dp).height(280.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Box(modifier = Modifier.fillMaxWidth().weight(0.7f).background(InsightTopGrey), contentAlignment = Alignment.Center) {
                                Image(painter = painterResource(id = item.icon), contentDescription = null, modifier = Modifier.size(85.dp), contentScale = ContentScale.Fit)
                            }
                            Box(modifier = Modifier.fillMaxWidth().weight(0.3f).background(InsightBottomPink).padding(16.dp), contentAlignment = Alignment.CenterStart) {
                                Text(item.text, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium, lineHeight = 16.sp, maxLines = 3)
                            }
                        }
                    }
                }
            }
        }

        // --- 5. FREQUENTLY ASKED QUESTIONS (NAVIGASI AKTIF) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(FaqCanvasPink)
                .padding(20.dp)
        ) {

            // --- BOX PEMBUNGKUS BARU (FFEDFA) ---
            Card(
                colors = CardDefaults.cardColors(containerColor = FaqInnerBox),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Frequently Asked Questions",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // LIST QUESTION
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        faqItems.forEach { item ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    // --- UPDATE: CLICKABLE untuk Navigasi Chatbot ---
                                    .clickable { onCheckUpClick(item.question) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Icon Bubble
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(FaqIconBg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(painter = painterResource(item.icon), contentDescription = null, modifier = Modifier.size(24.dp))
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    // Texts
                                    Column {
                                        Text(item.question, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Black)
                                        Text(item.sub, fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // See More Button
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("See more", color = Color(0xFFAD1457), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}