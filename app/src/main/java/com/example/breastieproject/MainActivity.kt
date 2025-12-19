package com.example.breastieproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel  // ✅ ADD THIS IMPORT!
import com.example.breastieproject.screens.reminder.ReminderScreen
import com.example.breastieproject.ui.components.BottomNavBar
import com.example.breastieproject.ui.components.BreastieHeader
import com.example.breastieproject.ui.screens.auth.SignInScreen
import com.example.breastieproject.ui.screens.auth.SignUpScreen
import com.example.breastieproject.ui.screens.community.CommunityScreen
import com.example.breastieproject.ui.screens.home.HomeScreen
import com.example.breastieproject.ui.screens.onboarding.OnboardingScreen
import com.example.breastieproject.ui.screens.profile.AboutBreastieScreen
import com.example.breastieproject.ui.screens.profile.ChangePasswordScreen
import com.example.breastieproject.ui.screens.profile.ContactSupportScreen
import com.example.breastieproject.ui.screens.profile.EditProfileScreen
import com.example.breastieproject.ui.screens.profile.ProfileScreen
import com.example.breastieproject.ui.screens.profile.QuestionScreen
import com.example.breastieproject.ui.theme.BackupTheme
import com.example.breastieproject.viewmodels.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BackupTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    var currentScreen by remember { mutableStateOf("onboarding") }
    val authViewModel: AuthViewModel = viewModel()  // ✅ NOW WORKS!

    when (currentScreen) {
        "onboarding" -> {
            OnboardingScreen(
                onFinish = { destination ->
                    currentScreen = destination
                }
            )
        }
        "signup" -> {
            SignUpScreen(
                onSignUpSuccess = {
                    currentScreen = "main"
                },
                onNavigateToSignIn = {
                    currentScreen = "signin"
                },
                viewModel = authViewModel
            )
        }
        "signin" -> {
            SignInScreen(
                onSignInSuccess = {
                    currentScreen = "main"
                },
                onNavigateToSignUp = {
                    currentScreen = "signup"
                },
                viewModel = authViewModel
            )
        }
        "main" -> {
            MainScreen(
                onNavigateToProfile = {
                    currentScreen = "profile"
                },
                authViewModel = authViewModel  // ✅ PASS ViewModel!
            )
        }
        "profile" -> {
            ProfileScreen(
                onBack = {  
                    currentScreen = "main"
                },
                onEditProfile = {
                    currentScreen = "edit_profile"
                },
                onChangePassword = {
                    currentScreen = "change_password"
                },
                onQuestion = {
                    currentScreen = "question"
                },
                onContactSupport = {
                    currentScreen = "contact_support"
                },
                onAbout = {
                    currentScreen = "about"
                },
                onSignOut = {
                    authViewModel.signOut()
                    currentScreen = "onboarding"
                },
                viewModel = authViewModel
            )
        }
        "edit_profile" -> {
            EditProfileScreen(
                onBack = {
                    currentScreen = "profile"
                },
                onSuccess = {
                    currentScreen = "profile"
                },
                viewModel = authViewModel
            )
        }
        "change_password" -> {
            ChangePasswordScreen(
                onBack = {
                    currentScreen = "profile"
                },
                onSuccess = {
                    currentScreen = "profile"
                },
                viewModel = authViewModel
            )
        }
        "question" -> {
            QuestionScreen(
                onBack = {
                    currentScreen = "profile"
                }
            )
        }
        "contact_support" -> {
            ContactSupportScreen(
                onBack = {
                    currentScreen = "profile"
                }
            )
        }
        "about" -> {
            AboutBreastieScreen(
                onBack = {
                    currentScreen = "profile"
                }
            )
        }
    }
}

@Composable
fun MainScreen(
    onNavigateToProfile: () -> Unit = {},  // ✅ ADD PARAMETER!
    authViewModel: AuthViewModel = viewModel()  // ✅ ADD PARAMETER!
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            BreastieHeader(
                onNotificationClick = {
                    // TODO: Navigate to notifications
                },
                onProfileClick = onNavigateToProfile  // ✅ NOW WORKS!
            )
        },
        bottomBar = {
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        containerColor = Color(0xFFFFEDFA)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    onProfileClick = onNavigateToProfile,  // ✅ NOW WORKS!
                    onReminderClick = {
                        // TODO: Navigate to reminder details
                    },
                    onCheckUpClick = { question ->
                        // TODO: Navigate to AI checkup with question
                    },
                    onFaqClick = {
                        // TODO: Navigate to FAQ
                    },
                    viewModel = authViewModel  // ✅ PASS ViewModel!
                )
                1 -> CommunityScreen()
                2 -> ReminderScreen()
                3 -> AIPlaceholder()
            }
        }
    }
}

@Composable
fun AIPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "✨ AI Checkup",
                fontSize = 24.sp,
                color = Color(0xFFEC7FA9)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "(Alfa's Feature)",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}


/**
 * ============================================================================
 * FILE: MainActivity.kt
 * LOCATION: MainActivity.kt (root package)
 * ============================================================================
 *
 * 🎯 DESKRIPSI SUPER SIMPLE:
 * File UTAMA app - entry point saat app dibuka.
 * Handle navigation antar 4 main screens (Home, Community, Reminder, AI).
 *
 * ============================================================================
 * 🤔 UNTUK APA?
 * ============================================================================
 *
 * Ini adalah "PUSAT KENDALI" app:
 *   ✅ Setup app theme (pink colors, Raleway font)
 *   ✅ Show BreastieHeader (pink header di atas)
 *   ✅ Show BottomNavBar (4 tabs di bawah)
 *   ✅ Handle navigation antar screens
 *   ✅ Manage selected tab state
 *
 * ============================================================================
 * 🎨 STRUKTUR APP (Layout)
 * ============================================================================
 *
 * ┌─────────────────────────────────┐
 * │  BREASTIE          🔔 👤       │  ← BreastieHeader (pink)
 * ├─────────────────────────────────┤
 * │                                 │
 * │                                 │
 * │     MAIN CONTENT AREA           │  ← Screen berubah sesuai tab
 * │     (Dashboard/Community/       │     yang dipilih
 * │      Reminder/AI Screen)        │
 * │                                 │
 * │                                 │
 * ├─────────────────────────────────┤
 * │  🏠   👥   ⏰   ✨             │  ← BottomNavBar
 * │ Home Com  Rem   AI              │
 * └─────────────────────────────────┘
 *
 * ============================================================================
 * ⚙️ CARA KERJA (Flow)
 * ============================================================================
 *
 * 1. User buka app
 * 2. onCreate() dipanggil
 * 3. setContent { MainScreen() }
 * 4. MainScreen setup:
 *    - selectedTab = 0 (default Home)
 *    - Show BreastieHeader
 *    - Show BottomNavBar
 *    - Show DashboardScreen (karena tab 0)
 *
 * 5. User tap tab lain (misal Community):
 *    - onTabSelected(1) dipanggil
 *    - selectedTab berubah jadi 1
 *    - Content berubah jadi CommunityScreen()
 *
 * ============================================================================
 * 📱 4 MAIN SCREENS (Tab Navigation)
 * ============================================================================
 *
 * TAB 0 - HOME (Dashboard):
 *   Icon: 🏠 Home
 *   Screen: DashboardScreen()
 *   Owner: KINAN
 *   File: ui/screens/dashboard/DashboardScreen.kt
 *   Isi: Stats, articles, health tracker
 *
 * TAB 1 - COMMUNITY:
 *   Icon: 👥 Person
 *   Screen: CommunityScreen()
 *   Owner: LINTANG (Kamu!)
 *   File: ui/screens/community/CommunityScreen.kt
 *   Isi: ✅ COMPLETE! (My Community, Explore, Feed)
 *
 * TAB 2 - REMINDER:
 *   Icon: ⏰ Notifications
 *   Screen: ReminderScreen()
 *   Owner: YANDA
 *   File: ui/screens/reminder/ReminderScreen.kt
 *   Isi: Medication reminders, chemo schedule
 *
 * TAB 3 - AI CHECKUP:
 *   Icon: ✨ AutoAwesome (sparkles)
 *   Screen: AIScreen()
 *   Owner: ALFA
 *   File: ui/screens/ai/AIScreen.kt
 *   Isi: AI chatbot, symptom checker
 *
 * ============================================================================
 * 🔄 NAVIGATION LOGIC
 * ============================================================================
 *
 * State management:
 *
 * var selectedTab by remember { mutableIntStateOf(0) }
 *   - 0 = Home (default saat app dibuka)
 *   - 1 = Community
 *   - 2 = Reminder
 *   - 3 = AI
 *
 * When user tap tab:
 *
 * BottomNavBar(
 *     selectedTab = selectedTab,      // Current active tab
 *     onTabSelected = { index ->
 *         selectedTab = index          // Update state
 *     }
 * )
 *
 * Show screen based on selectedTab:
 *
 * when (selectedTab) {
 *     0 -> DashboardScreen()      // Kinan's screen
 *     1 -> CommunityScreen()      // Lintang's screen (COMPLETE!)
 *     2 -> ReminderScreen()       // Yanda's screen
 *     3 -> AIScreen()             // Alfa's screen
 * }
 *
 * ============================================================================
 * 🚧 STATUS PLACEHOLDER SCREENS
 * ============================================================================
 *
 * SEKARANG (Pakai Placeholder):
 *
 * when (selectedTab) {
 *     0 -> DashboardPlaceholder()     // ⚠️ TEMPORARY!
 *     1 -> CommunityScreen()          // ✅ DONE!
 *     2 -> ReminderPlaceholder()      // ⚠️ TEMPORARY!
 *     3 -> AIPlaceholder()            // ⚠️ TEMPORARY!
 * }
 *
 * NANTI (Setelah tim selesai):
 *
 * when (selectedTab) {
 *     0 -> DashboardScreen()          // ✅ Kinan done!
 *     1 -> CommunityScreen()          // ✅ Lintang done!
 *     2 -> ReminderScreen()           // ✅ Yanda done!
 *     3 -> AIScreen()                 // ✅ Alfa done!
 * }
 *
 * ============================================================================
 * 👥 INSTRUKSI UNTUK TIM
 * ============================================================================
 *
 * KINAN (Dashboard):
 * 1. Bikin file: ui/screens/dashboard/DashboardScreen.kt
 * 2. Implement DashboardScreen() composable
 * 3. Di MainActivity.kt, GANTI:
 *    0 -> DashboardPlaceholder()
 *    JADI:
 *    0 -> DashboardScreen()
 * 4. Jangan lupa import:
 *    import com.example.breastieproject.ui.screens.dashboard.DashboardScreen
 *
 * YANDA (Reminder):
 * 1. Bikin file: ui/screens/reminder/ReminderScreen.kt
 * 2. Implement ReminderScreen() composable
 * 3. Di MainActivity.kt, GANTI:
 *    2 -> ReminderPlaceholder()
 *    JADI:
 *    2 -> ReminderScreen()
 * 4. Import:
 *    import com.example.breastieproject.ui.screens.reminder.ReminderScreen
 *
 * ALFA (AI Checkup):
 * 1. Bikin file: ui/screens/ai/AIScreen.kt
 * 2. Implement AIScreen() composable
 * 3. Di MainActivity.kt, GANTI:
 *    3 -> AIPlaceholder()
 *    JADI:
 *    3 -> AIScreen()
 * 4. Import:
 *    import com.example.breastieproject.ui.screens.ai.AIScreen
 *
 * ⚠️ PENTING:
 * - Jangan edit BottomNavBar atau BreastieHeader!
 * - Jangan ubah selectedTab logic!
 * - Cuma ganti placeholder jadi screen asli!
 *
 * ============================================================================
 * 📝 CONTOH TEMPLATE SCREEN (Untuk Tim)
 * ============================================================================
 *
 * // DashboardScreen.kt (Kinan)
 * package com.example.breastieproject.ui.screens.dashboard
 *
 * import androidx.compose.foundation.layout.*
 * import androidx.compose.material3.Text
 * import androidx.compose.runtime.Composable
 * import androidx.compose.ui.Modifier
 * import androidx.compose.ui.unit.dp
 *
 * @Composable
 * fun DashboardScreen() {
 *     Column(
 *         modifier = Modifier
 *             .fillMaxSize()
 *             .padding(16.dp)
 *     ) {
 *         Text("Dashboard")
 *         // Kinan, tambahin konten di sini!
 *     }
 * }
 *
 * ============================================================================
 * 🔗 HUBUNGAN DENGAN FILE LAIN
 * ============================================================================
 *
 * Pakai komponen:
 *   - BreastieHeader.kt (pink header)
 *   - BottomNavBar.kt (navigation)
 *
 * Pakai screens:
 *   - CommunityScreen.kt (Lintang - DONE!)
 *   - DashboardScreen.kt (Kinan - TODO!)
 *   - ReminderScreen.kt (Yanda - TODO!)
 *   - AIScreen.kt (Alfa - TODO!)
 *
 * Pakai theme:
 *   - BackupTheme (pink colors, Raleway font)
 *
 * ============================================================================
 * 🎨 SCAFFOLD STRUCTURE
 * ============================================================================
 *
 * Scaffold = Layout template Material 3:
 *
 * Scaffold(
 *     topBar = { ... },           // Header di atas
 *     bottomBar = { ... },        // Navigation di bawah
 *     containerColor = ...        // Background color
 * ) { paddingValues ->
 *     // Main content area
 *     Box(modifier = Modifier.padding(paddingValues)) {
 *         // Screen content di sini
 *     }
 * }
 *
 * paddingValues = Auto-spacing untuk header & bottom bar
 *
 * ============================================================================
 * 🚀 FUTURE IMPROVEMENTS
 * ============================================================================
 *
 * Nanti bisa ditambah:
 *   - Splash screen sebelum MainActivity
 *   - Onboarding screen (first time user)
 *   - Login check (kalau belum login, redirect ke login)
 *   - Deep linking (buka screen tertentu dari notif)
 *   - Navigation animations
 *
 * ============================================================================
 * 💡 TIPS DEBUG
 * ============================================================================
 *
 * Cek tab mana yang aktif:
 *   - Tambah log: println("Selected tab: $selectedTab")
 *   - Lihat di Logcat
 *
 * Test navigation:
 *   1. Run app
 *   2. Tap tiap tab
 *   3. Screen harus berubah
 *   4. Tab icon highlight (pink)
 *

 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * LAST UPDATE: 13 Dec 2024
 * STATUS: ✅ COMPLETE (Navigation setup)
 * NEXT: Tim complete their screens!
 * ============================================================================
 */

// ============================================================================
// PLACEHOLDER SCREENS (Temporary - Will be replaced by team!)
// ============================================================================

/**
 * PLACEHOLDER: Dashboard (Home)
 *
 * TODO - KINAN:
 * 1. Bikin file: ui/screens/home/HomeScreen.kt
 * 2. Implement DashboardScreen() composable
 * 3. Ganti DashboardPlaceholder() jadi HomeScreen()
 * 4. Import: import com.example.breastieproject.ui.screens.home.HomeScreen
 */
/**
 * PLACEHOLDER: Reminder
 *
 * TODO - YANDA:
 * 1. Bikin file: ui/screens/reminder/ReminderScreen.kt
 * 2. Implement ReminderScreen() composable
 * 3. Ganti ReminderPlaceholder() jadi ReminderScreen()
 * 4. Import: import com.example.breastieproject.ui.screens.reminder.ReminderScreen
 */

/**
 * PLACEHOLDER: AI Checkup
 *
 * TODO - ALFA:
 * 1. Bikin file: ui/screens/ai/AIScreen.kt
 * 2. Implement AIScreen() composable
 * 3. Ganti AIPlaceholder() jadi CheckUpScreen()
 * 4. Import: import com.example.breastieproject.ui.screens.checkup.CheckUpScreen
 */