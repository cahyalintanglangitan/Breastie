package com.example.breastieproject.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search      // ✅ AI/Robot icon!
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.breastieproject.ui.theme.BackupTheme

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavBar(
    selectedTab: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, "dashboard"),
        BottomNavItem("Community", Icons.Default.Person, "community"),
        BottomNavItem("Reminder", Icons.Default.Notifications, "reminder"),
        BottomNavItem("Check Up", Icons.Default.Search, "checkup")  // ✅ CHANGED!
    )

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFFEC7FA9)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFEC7FA9),
                    selectedTextColor = Color(0xFFEC7FA9),
                    unselectedIconColor = Color(0xFF999999),
                    unselectedTextColor = Color(0xFF999999),
                    indicatorColor = Color(0xFFFFE4F2)
                )
            )
        }
    }
}

@Preview
@Composable
fun BottomNavBarPreview() {
    BackupTheme {
        BottomNavBar(selectedTab = 1)
    }
}
/**
 * ============================================================================
 * FILE: BottomNavBar.kt
 * LOCATION: ui/components/BottomNavBar.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Reusable bottom navigation bar component untuk app-wide navigation.
 * Sticky di bottom semua screens, handle navigation antar main features.
 *
 * ============================================================================
 * PARAMETERS
 * ============================================================================
 *
 * selectedTab: Int (default = 0)
 *   - Index tab yang aktif (0-3)
 *   - 0 = Home, 1 = Community, 2 = Reminder, 3 = AI
 *   - Controlled by parent (MainActivity)
 *
 * onTabSelected: (Int) -> Unit (default = {})
 *   - Callback saat tab diklik
 *   - Parameter: index tab yang diklik
 *   - Parent update selectedTab state
 *
 * ============================================================================
 * NAVIGATION ITEMS
 * ============================================================================
 *
 * Index 0: Home
 *   - Icon: Icons.Default.Home
 *   - Route: "dashboard"
 *   - Feature: Dashboard (Kinan)
 *
 * Index 1: Community
 *   - Icon: Icons.Default.Person (temporary)
 *   - Route: "community"
 *   - Feature: Community (Lintang)
 *
 * Index 2: Reminder
 *   - Icon: Icons.Default.Notifications
 *   - Route: "reminder"
 *   - Feature: Reminder (Yanda)
 *
 * Index 3: AI
 *   - Icon: Icons.Default.AutoAwesome (sparkles)
 *   - Route: "ai"
 *   - Feature: AI Chatbot (Alfa)
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * // Di MainActivity atau parent composable
 * var selectedTab by remember { mutableStateOf(0) }
 *
 * Scaffold(
 *     bottomBar = {
 *         BottomNavBar(
 *             selectedTab = selectedTab,
 *             onTabSelected = { index ->
 *                 selectedTab = index
 *             }
 *         )
 *     }
 * ) { paddingValues ->
 *     when (selectedTab) {
 *         0 -> DashboardScreen()
 *         1 -> CommunityScreen()
 *         2 -> ReminderScreen()
 *         3 -> AIScreen()
 *     }
 * }
 *
 * ============================================================================
 * STYLING & COLORS
 * ============================================================================
 *
 * Container Color: White
 * Selected Icon: #EC7FA9 (pink primary)
 * Selected Text: #EC7FA9 (pink primary)
 * Unselected Icon: #999999 (gray)
 * Unselected Text: #999999 (gray)
 * Indicator: #FFE4F2 (light pink)
 *
 * ============================================================================
 * DIPAKAI DI
 * ============================================================================
 *
 * - MainActivity.kt (main app navigation)
 *
 * ❌ JANGAN PAKAI DI:
 * - CommunityScreen (duplikat!)
 * - DashboardScreen (duplikat!)
 * - Child screens (hanya di MainActivity!)
 *
 * ============================================================================
 * FUTURE IMPROVEMENTS
 * ============================================================================
 *
 * - Tambah badge notification count
 * - Custom community icon (bukan Person)
 * - Animation on tab change
 * - Haptic feedback on tap
 *
 * ============================================================================
 * NOTES
 * ============================================================================
 *
 * - Uses Material3 NavigationBar
 * - Auto-handle selected state
 * - Responsive to screen size
 * - Accessibility compliant
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * LAST UPDATE: 13 Dec 2024
 * ============================================================================
 */