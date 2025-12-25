package com.example.breastieproject.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.breastieproject.R
import com.example.breastieproject.ui.theme.*
import com.example.breastieproject.viewmodels.AuthViewModel
import com.example.breastieproject.viewmodels.CheckUpViewModel
import com.example.breastieproject.viewmodels.ReminderViewModel  // ✅ CRITICAL!

// Data Classes (same as before)
data class CarouselData(
    val image: Int,
    val topText: String,
    val bottomText: String
)

data class InsightData(
    val icon: Int,
    val text: String
)

//data class FaqData(
//    val icon: Int,
//    val question: String,
//    val sub: String
//)

@Composable
fun HomeScreen(
    onReminderClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onCheckUpClick: (String) -> Unit = {},
    onFaqClick: () -> Unit = {},
    viewModel: AuthViewModel = viewModel(),
    reminderViewModel: ReminderViewModel = viewModel(),
    checkUpViewModel: CheckUpViewModel = viewModel()
) {
    val scrollState = rememberScrollState()

    // ✅ GET CURRENT USER DATA
    val currentUser by viewModel.currentUser.collectAsState()
    val faqs by checkUpViewModel.faqs.collectAsState()
    val nearestReminder by reminderViewModel.nearestReminder.collectAsState()

    // ✅ EXTRACT USER NAME (or default)
    val userName = currentUser?.fullName ?: "User"
    val userPhotoUrl = currentUser?.profilePhotoUrl



    // --- WARNA LOKAL ---
    val PinkDarkBox = Color(0xFFEC7FA9)
    val PinkLightBox = Color(0xFFFCE4EC)
    val TextDarkPink = Color(0xFFAD1457)
    val CarouselTextBg = Color(0xFFF2F2F7)
    val CarouselTextColor = Color(0xFFBE5985)
    val FeatureCanvasPink = Color(0xFFFFB8E0)
    val FeatureCardBg = Color.White
    val FeatureIconBg = Color(0xFFEEEEEE)
    val FeatureIconTint = Color(0xFFC05D85)
    val InsightTopGrey = Color(0xFFEEEEEE)
    val InsightBottomPink = Color(0xFFEC7FA9)
    val InsightContainerWhite = Color(0xFFFFFFFF)
    val FaqCanvasPink = Color(0xFFFFB8E0)
    val FaqInnerBox = Color(0xFFFFEDFA)
    val FaqIconBg = Color(0xFFF5F5F5)

    // DATA SETUP
    val carouselItems = listOf(
        CarouselData(R.drawable.ic_1, "Good Morning $userName", "Do you feel good?"),  // ✅ DYNAMIC NAME!
        CarouselData(R.drawable.ic_4, "Deteksi dini bisa meningkatkan peluang sembuh hingga lebih dari 90%.", "Kamu lebih kuat dari yang kamu kira — langkah kecil hari ini berarti masa depan yang lebih sehat."),
        CarouselData(R.drawable.ic_5, "Sebagian besar benjolan payudara ternyata non-kanker — jadi jangan takut untuk memeriksakan diri.", "Keberanian bukan tanpa rasa takut, tapi bergerak meski takut."),
        CarouselData(R.drawable.ic_6, "Survivor breast cancer bisa hidup aktif dan sehat puluhan tahun setelah diagnosis", "Harapan selalu ada — dan kamu punya kekuatan untuk terus maju.")
    )

    val insightItems = listOf(
        InsightData(R.drawable.ic_healthy, "Don't forget your meds, take rest, take your power back."),
        InsightData(R.drawable.ic_drink, "Hydrate your body, elevate your mood."),
        InsightData(R.drawable.ic_sleep, "Rest isn't quitting — healing.")
    )

//    val faqItems = listOf(
//        FaqData(R.drawable.ic_11, "How to perform a proper breast self-exam?", "Step-by-step guidance"),
//        FaqData(R.drawable.ic_12, "What are early signs of breast cancer?", "Symptoms to be aware of"),
//        FaqData(R.drawable.ic_13, "Why does breast pain occur?", "Common causes explained"),
//        FaqData(R.drawable.ic_14, "When should I consult a doctor?", "Know the right time"),
//        FaqData(R.drawable.ic_15, "Are all breast lumps dangerous?", "Benign vs concerning lumps"),
//        FaqData(R.drawable.ic_16, "How does menstrual cycle affect breasts?", "Hormonal changes"),
//        FaqData(R.drawable.ic_17, "Is breast pain always related to cancer?", "Understanding the risks"),
//        FaqData(R.drawable.ic_18, "What lifestyle habits support breast health?", "Daily prevention tips"),
//        FaqData(R.drawable.ic_14, "How often should breast checks be done?", "Recommended frequency"),
//        FaqData(R.drawable.ic_18, "Can stress affect breast health?", "Mind–body connection")
//    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PinkBackground)
            .verticalScroll(scrollState)
    ) {

        // --- 2. GREETING SECTION ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- BOX 1: Bagian Profil ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProfileClick() }  // ✅ WHOLE CARD CLICKABLE!
                    .background(PinkLight)
                    .padding(all = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // ✅ PROFILE PHOTO (Real or Placeholder)
                    if (userPhotoUrl?.isNotBlank() == true) {
                        AsyncImage(
                            model = userPhotoUrl,
                            contentDescription = "Profile photo",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_avatar),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                        )
                    }

                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            "Hi, $userName",  // ✅ DYNAMIC NAME!
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
            if (nearestReminder != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PinkLight)
                        .padding(all = 16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_3),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { onReminderClick() }
                        )
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(
                                "${nearestReminder!!.daysUntil}: ${nearestReminder!!.name}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Black
                            )
                            Text(
                                "📅 ${nearestReminder!!.date} - ${nearestReminder!!.doctor}",
                                fontSize = 12.sp,
                                color = Color(0xFF444444)
                            )
                            Text(
                                "Tap to view all schedules",
                                fontSize = 11.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
            } else {
                // ✅ Empty state when no reminders
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PinkLight)
                        .padding(all = 16.dp)
                        .clickable { onReminderClick() }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_3),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(
                                "No schedules yet",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Black
                            )
                            Text(
                                "Tap to add a health schedule",
                                fontSize = 12.sp,
                                color = Color(0xFF444444)
                            )
                        }
                    }
                }
            }
        }

        // --- CAROUSEL SLIDER ---
        val carouselCardHeight = 225.dp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFFFFF))
                .padding(vertical = 24.dp)
        ) {
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
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(carouselCardHeight),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                        .background(Color(0xFFF5F5F5)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = item.image),
                                        contentDescription = null,
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier
                                            .fillMaxWidth(0.6f)
                                            .fillMaxHeight(0.6f)
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(PinkLightBox)
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "${item.topText}, Welcome to PinkBreastie!",  // ✅ WITH DYNAMIC NAME!
                                        color = TextDarkPink,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        item.bottomText,
                                        color = TextDarkPink,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    } else {
                        // BOX 2 DST (unchanged)
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .width(320.dp)
                                .height(carouselCardHeight),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(0.4f)
                                            .fillMaxHeight()
                                            .background(Color.White),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = item.image),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(0.6f)
                                            .fillMaxHeight()
                                            .background(PinkDarkBox),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Text(
                                            item.topText,
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(CarouselTextBg)
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        item.bottomText,
                                        color = CarouselTextColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 3. FEATURE HIGHLIGHTS (unchanged) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(FeatureCanvasPink)
                .padding(vertical = 24.dp, horizontal = 20.dp)
        ) {
            Text(
                "Feature Highlights",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextBlack,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val featureHeight = 190.dp

            @Composable
            fun FeatureItemCard(
                iconRes: Int,
                title: String,
                subtitle: String,
                modifier: Modifier = Modifier,
                isHeartIcon: Boolean = false
            ) {
                Card(
                    modifier = modifier.height(featureHeight),
                    colors = CardDefaults.cardColors(containerColor = FeatureCardBg),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(FeatureIconBg),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isHeartIcon) {
                                Box(
                                    modifier = Modifier
                                        .size(70.dp)
                                        .clip(CircleShape)
                                        .background(FeatureIconTint),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = null,
                                        modifier = Modifier.size(36.dp),
                                        colorFilter = ColorFilter.tint(Color.White)
                                    )
                                }
                            } else {
                                Image(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                subtitle,
                                fontSize = 11.sp,
                                color = Color.Gray,
                                lineHeight = 14.sp
                            )
                            Text(
                                title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.Black,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FeatureItemCard(
                        R.drawable.ic_community,
                        "Community",
                        "Mini Talks with Other",
                        Modifier.weight(1f)
                    )
                    FeatureItemCard(
                        R.drawable.ic_assistant,
                        "Insight & Get personalized insights",
                        "PinkBreastie Insight's",
                        Modifier.weight(1f)
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    FeatureItemCard(
                        R.drawable.ic_heart,
                        "Breast Journal",
                        "Self-Check Guide",
                        isHeartIcon = true,
                        modifier = Modifier.width(170.dp)
                    )
                }
            }
        }

        // --- 4. INSIGHT FOR YOU (unchanged) ---
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
                        modifier = Modifier
                            .width(175.dp)
                            .height(280.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.7f)
                                    .background(InsightTopGrey),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(85.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(0.3f)
                                    .background(InsightBottomPink)
                                    .padding(16.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    item.text,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 16.sp,
                                    maxLines = 3
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 5. FAQ (unchanged) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(FaqCanvasPink)
                .padding(20.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = FaqInnerBox),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Frequently Asked Questions",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        faqs.forEach { faq ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onCheckUpClick(faq.question)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFF5F5F5)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column {
                                        Text(
                                            text = faq.question,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 14.sp
                                        )
                                        Text(
                                            text = faq.category.uppercase(),
                                            fontSize = 11.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    "See more",
                    color = Color(0xFFAD1457),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}