package com.example.breastieproject.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
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

@Composable
fun BreastieHeader(
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFFFB8E0), // Pink header
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo + App Name
            Text(
                text = "Breastie",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // Icons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNotificationClick) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White
                    )
                }

                IconButton(onClick = onProfileClick) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun BreastieHeaderPreview() {
    BackupTheme {
        BreastieHeader()
    }
}


/**
 * ============================================================================
 * FILE: BreastieHeader.kt
 * LOCATION: ui/components/BreastieHeader.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Reusable top header component dengan branding "Breastie".
 * Konsisten di semua screens dengan notification & profile icons.
 *
 * ============================================================================
 * PARAMETERS
 * ============================================================================
 *
 * onNotificationClick: () -> Unit (default = {})
 *   - Callback saat notification icon diklik
 *   - Navigate ke notification screen (future)
 *   - Show notification badge (future)
 *
 * onProfileClick: () -> Unit (default = {})
 *   - Callback saat profile icon diklik
 *   - Navigate ke profile screen
 *   - Show user settings
 *
 * ============================================================================
 * UI ELEMENTS
 * ============================================================================
 *
 * Logo/Title: "Breastie"
 *   - Font: Raleway Bold
 *   - Size: 24sp
 *   - Color: White
 *   - Position: Left
 *
 * Notification Icon:
 *   - Icon: Bell (Icons.Default.Notifications)
 *   - Color: White
 *   - Position: Right (before profile)
 *   - Future: Show badge count
 *
 * Profile Icon:
 *   - Icon: Person (Icons.Default.Person)
 *   - Color: White
 *   - Position: Right (end)
 *
 * Background:
 *   - Gradient: #FFB8E0 to #EC7FA9
 *   - Height: 64dp (with padding)
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * // Basic usage
 * BreastieHeader()
 *
 * // With callbacks
 * BreastieHeader(
 *     onNotificationClick = {
 *         navController.navigate("notifications")
 *     },
 *     onProfileClick = {
 *         navController.navigate("profile")
 *     }
 * )
 *
 * // In Scaffold
 * Scaffold(
 *     topBar = {
 *         BreastieHeader(
 *             onNotificationClick = { /* ... */ },
 *             onProfileClick = { /* ... */ }
 *         )
 *     }
 * ) { ... }
 *
 * ============================================================================
 * DIPAKAI DI
 * ============================================================================
 *
 * - MainActivity.kt (global header)
 *
 * ❌ JANGAN PAKAI DI:
 * - CommunityScreen (duplikat!)
 * - Child screens yang fullscreen (ChatScreen, dll)
 *
 * ============================================================================
 * FUTURE IMPROVEMENTS
 * ============================================================================
 *
 * - Notification badge with count
 * - User avatar instead of Person icon
 * - Search icon/functionality
 * - Custom title per screen (optional)
 *
 * ============================================================================
 * NOTES
 * ============================================================================
 *
 * - Fixed height untuk consistency
 * - Gradient background untuk brand identity
 * - Icon buttons with ripple effect
 * - Safe area padding (status bar)
 *
 * ============================================================================
 * AUTHOR: Lintang*/