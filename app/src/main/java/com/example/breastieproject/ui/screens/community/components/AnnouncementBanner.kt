package com.example.breastieproject.ui.screens.community.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breastieproject.ui.theme.BackupTheme
import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.breastieproject.R

@Composable
fun AnnouncementBanner(
    onBannerClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFE4F2)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // ✅ ADD POSTER IMAGE:
            Image(
                painter = painterResource(id = R.drawable.webinar_poster),
                contentDescription = "Webinar Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = "Managing Side Effects \nof Breast Cancer Treatment",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Practical guidance to help you cope physically and emotionally during treatment",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Row: Date + Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date Info
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Webinar",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFFEC7FA9)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = Color(0xFF999999),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Jan 5, 2025",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }

                // Register Button
                Button(
                    onClick = onBannerClick,  // ✅ Changed!
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEC7FA9)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Register now",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AnnouncementBannerPreview() {
    BackupTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AnnouncementBanner()
        }
    }
}

/**
 * ============================================================================
 * FILE: AnnouncementBanner.kt
 * LOCATION: ui/screens/community/components/AnnouncementBanner.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Banner component untuk highlight upcoming webinar/events.
 * Ditampilkan di top semua tabs (My Community, Explore, Feed).
 *
 * ============================================================================
 * PARAMETERS
 * ============================================================================
 *
 * onBannerClick: () -> Unit (default = {})
 *   - Callback saat banner diklik
 *   - Navigate to WebinarDetailScreen
 *
 * modifier: Modifier (default = Modifier)
 *   - Custom styling dari parent
 *
 * ============================================================================
 * UI STRUCTURE
 * ============================================================================
 *
 * Banner Layout:
 * ┌─────────────────────────────────────────────┐
 * │ Innovative Ideas from Women Entrepreneurs   │  <- Title
 * │                                             │
 * │ Get inspired by success stories of women    │  <- Description
 * │ entrepreneurs in technology                 │
 * │                                             │
 * │ Webinar          📅 June 5, 2025           │  <- Type & Date
 * │                                             │
 * │              [Register now]                 │  <- CTA Button
 * └─────────────────────────────────────────────┘
 *
 * ============================================================================
 * STYLING
 * ============================================================================
 *
 * Container:
 *   - Background: Gradient (#FFE4F2 → #FFB8E0)
 *   - Corner radius: 16.dp
 *   - Padding: 16.dp
 *   - Margin: 16.dp horizontal, 12.dp vertical
 *   - Elevation: 4.dp
 *
 * Title:
 *   - Font: Raleway Bold
 *   - Size: 18.sp
 *   - Color: #333333 (dark)
 *   - Max lines: 2
 *
 * Description:
 *   - Font: Raleway Regular
 *   - Size: 14.sp
 *   - Color: #666666 (gray)
 *   - Max lines: 2
 *
 * Meta Info (Webinar + Date):
 *   - Font: Raleway Medium
 *   - Size: 12.sp
 *   - Color: #EC7FA9 (pink)
 *   - Icons: Calendar
 *
 * Register Button:
 *   - Background: #EC7FA9 (pink)
 *   - Text: White, Raleway Bold, 14.sp
 *   - Corner radius: 20.dp
 *   - Padding: 12.dp horizontal, 8.dp vertical
 *   - Full width
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * // Basic usage
 * AnnouncementBanner(
 *     onBannerClick = {
 *         navController.navigate("webinar_detail")
 *     }
 * )
 *
 * // In tab screens
 * Column {
 *     AnnouncementBanner(
 *         onBannerClick = { showWebinar = true }
 *     )
 *
 *     // Rest of tab content
 *     LazyColumn { ... }
 * }
 *
 * ============================================================================
 * DATA SOURCE
 * ============================================================================
 *
 * Currently: Hard-coded data
 *
 * Future (Firebase):
 *   - Collection: "announcements"
 *   - Fields:
 *     - title: String
 *     - description: String
 *     - type: String (Webinar, Workshop, dll)
 *     - date: Timestamp
 *     - isActive: Boolean
 *     - priority: Int (for ordering)
 *
 * ============================================================================
 * DIPAKAI DI
 * ============================================================================
 *
 * - MyCommunityTab.kt (top of list)
 * - ExploreTab.kt (top of list)
 * - FeedTab.kt (top of feed)
 *
 * ============================================================================
 * FUTURE IMPROVEMENTS
 * ============================================================================
 *
 * - Multiple announcements carousel
 * - Auto-hide after user clicks
 * - Countdown timer to event
 * - Image/video background
 * - Dismiss button (X icon)
 * - Animation entrance
 *
 * ============================================================================
 * NOTES
 * ============================================================================
 *
 * - Single instance across tabs
 * - Clickable entire banner
 * - Gradient for visual appeal
 * - High visibility (top position)
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * LAST UPDATE: 13 Dec 2024
 * ============================================================================
 */
