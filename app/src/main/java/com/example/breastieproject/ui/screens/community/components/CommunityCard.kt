package com.example.breastieproject.ui.screens.community.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breastieproject.data.model.Community
import com.example.breastieproject.ui.theme.BackupTheme

@Composable
fun CommunityCard(
    community: Community,
    buttonText: String = "Lihat", // "Lihat" untuk My Community, "Gabung" untuk Explore
    onButtonClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF0F8) // Pink muda
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Icon + Name + Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon + Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Community Icon (placeholder)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(0xFFFFB8E0),
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Community Icon",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Community Name
                    Text(
                        text = community.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Active Status
                if (community.isActive) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFE4F2)
                    ) {
                        Text(
                            text = "Active",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFEC7FA9)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Member Count
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Members",
                    tint = Color(0xFF999999),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${community.memberCount} anggota",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = community.description,
                fontSize = 14.sp,
                color = Color(0xFF555555),
                lineHeight = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Upcoming Event (if exists)
            if (community.upcomingEvent.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFFFF0F8),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Event",
                        tint = Color(0xFFEC7FA9),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = community.upcomingEvent,
                        fontSize = 12.sp,
                        color = Color(0xFF666666),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button (Lihat / Gabung)
            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEC7FA9)
                ),
                shape = RoundedCornerShape(22.dp)
            ) {
                Text(
                    text = buttonText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

// Preview untuk development
@Preview(showBackground = true)
@Composable
fun CommunityCardPreview() {
    BackupTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFEDFA))
                .padding(vertical = 16.dp)
        ) {
            CommunityCard(
                community = Community(
                    id = "comm_001",
                    name = "Pejuang Kemo",
                    description = "Komunitas untuk saling mendukung selama proses kemoterapi",
                    memberCount = 1247,
                    isActive = true,
                    upcomingEvent = "Akan ada event edukasi minggu ini!"
                ),
                buttonText = "Lihat",
                onButtonClick = { }
            )
        }
    }
}


/**
 * ============================================================================
 * FILE: CommunityCard.kt
 * LOCATION: ui/screens/community/components/CommunityCard.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Reusable card component untuk display komunitas di list.
 * Dipakai di My Community tab & Explore tab dengan styling berbeda.
 *
 * ============================================================================
 * PARAMETERS
 * ============================================================================
 *
 * community: Community (REQUIRED)
 *   - Data komunitas yang akan ditampilkan
 *   - Contains: name, description, memberCount, dll
 *
 * onCardClick: () -> Unit (default = {})
 *   - Callback saat card diklik
 *   - My Community: Navigate to ChatScreen
 *   - Explore: Show join confirmation
 *
 * modifier: Modifier (default = Modifier)
 *   - Custom styling dari parent
 *
 * ============================================================================
 * UI STRUCTURE
 * ============================================================================
 *
 * Card Layout:
 * ┌─────────────────────────────────┐
 * │ ●  Community Name         ⚡    │  <- Avatar + Name + Event
 * │    1,247 members                │  <- Member count
 * │                                 │
 * │    Description text here...     │  <- Description (2 lines max)
 * │                                 │
 * │    📅 Upcoming event info       │  <- Event highlight
 * └─────────────────────────────────┘
 *
 * ============================================================================
 * STYLING
 * ============================================================================
 *
 * Card:
 *   - Background: White
 *   - Corner radius: 12.dp
 *   - Elevation: 2.dp
 *   - Padding: 16.dp
 *
 * Avatar:
 *   - Size: 48.dp
 *   - Shape: Circle
 *   - Color: #FFE4F2 (light pink)
 *   - Icon: Person (placeholder)
 *
 * Name:
 *   - Font: Raleway SemiBold
 *   - Size: 16.sp
 *   - Color: Black
 *
 * Member count:
 *   - Font: Raleway Regular
 *   - Size: 12.sp
 *   - Color: #666666 (gray)
 *   - Format: "1,247 members"
 *
 * Description:
 *   - Font: Raleway Regular
 *   - Size: 14.sp
 *   - Color: #333333
 *   - Max lines: 2
 *   - Ellipsis: "..."
 *
 * Event Badge:
 *   - Background: #FFF3E0 (light orange)
 *   - Icon: ⚡ (lightning)
 *   - Text: Orange
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * // Basic usage
 * CommunityCard(
 *     community = myCommunity,
 *     onCardClick = {
 *         navController.navigate("chat/${myCommunity.id}")
 *     }
 * )
 *
 * // In LazyColumn
 * LazyColumn {
 *     items(communities) { community ->
 *         CommunityCard(
 *             community = community,
 *             onCardClick = { /* ... */ },
 *             modifier = Modifier.padding(vertical = 8.dp)
 *         )
 *     }
 * }
 *
 * ============================================================================
 * VARIANTS
 * ============================================================================
 *
 * My Community Tab:
 *   - Click → Navigate to ChatScreen
 *   - Show active indicator
 *   - Highlight unread messages (future)
 *
 * Explore Tab:
 *   - Click → Show join dialog
 *   - Show "Join" button
 *   - Disable if already joined
 *
 * ============================================================================
 * DIPAKAI DI
 * ============================================================================
 *
 * - MyCommunityTab.kt (joined communities)
 * - ExploreTab.kt (available communities)
 *
 * ============================================================================
 * FUTURE IMPROVEMENTS
 * ============================================================================
 *
 * - Real avatar from Firebase Storage
 * - Unread message count badge
 * - Active users indicator (online)
 * - Long press menu (mute, leave, etc)
 * - Swipe actions
 *
 * ============================================================================
 * NOTES
 * ============================================================================
 *
 * - Immutable community data (copy for changes)
 * - Clickable with ripple effect
 * - Responsive to different screen sizes
 * - Accessible (content descriptions)
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * LAST UPDATE: 13 Dec 2024
 * ============================================================================
 */
