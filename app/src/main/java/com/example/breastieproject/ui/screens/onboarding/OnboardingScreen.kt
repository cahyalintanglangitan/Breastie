package com.example.breastieproject.ui.screens.onboarding

import OnboardingPage
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breastieproject.R
import com.example.breastieproject.ui.screens.onboarding.components.PageIndicator
import com.example.breastieproject.ui.theme.BackupTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

data class OnboardingData(
    val imageRes: Int,
    val title: String
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    onFinish: (String) -> Unit = {}  // ✅ BERUBAH! Pass destination
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val pages = listOf(
        OnboardingData(R.drawable.onboarding_1, "Welcome to Your\nSupportive Community"),
        OnboardingData(R.drawable.onboarding_2, "Connect, Share, and\nSupport One Another"),
        OnboardingData(R.drawable.onboarding_3, "Features That Empower\nYour Health Journey")
    )

    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(Modifier.fillMaxSize()) {
            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnboardingPage(pages[page].imageRes, pages[page].title)
            }

            Column(
                Modifier.fillMaxWidth().padding(horizontal = 32.dp).padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                PageIndicator(pages.size, pagerState.currentPage)

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // SKIP Button
                    Button(
                        onClick = { onFinish("signup") },  // ✅ Navigate to Sign Up
                        modifier = Modifier.weight(1f).height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFB8E0),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text("SKIP", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    // NEXT / REGISTER NOW Button
                    Button(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage < pages.size - 1) {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                } else {
                                    onFinish("signup")  // ✅ Navigate to Sign Up
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEC7FA9),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        Text(
                            if (pagerState.currentPage < pages.size - 1) "NEXT" else "REGISTER NOW",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Sign in link
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Already have an account? ", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF666666))
                    TextButton(
                        onClick = { onFinish("signin") },  // ✅ Navigate to Sign In
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Sign in", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFEC7FA9), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}


/**
 * ============================================================================
 * FILE: OnboardingScreen.kt
 * LOCATION: ui/screens/onboarding/OnboardingScreen.kt
 * ============================================================================
 *
 * 🎯 DESKRIPSI SIMPLE:
 * Layar intro yang muncul PERTAMA KALI user buka app.
 * Kayak slide presentasi - swipe kiri/kanan untuk next/prev page.
 * Ada 3 halaman yang jelasin fitur app.
 *
 * ============================================================================
 * 🤔 UNTUK APA?
 * ============================================================================
 *
 * Welcome screen untuk:
 *   ✅ User baru (first time install)
 *   ✅ Jelasin fitur app (3 slides)
 *   ✅ Ajak user register atau sign in
 *
 * User bisa:
 *   ✅ Swipe kiri/kanan ganti slide
 *   ✅ Klik "SKIP" → Langsung ke register
 *   ✅ Klik "NEXT" → Slide berikutnya
 *   ✅ Klik "REGISTER NOW" di slide terakhir
 *   ✅ Klik "Sign in" kalau udah punya akun
 *
 * ============================================================================
 * 📱 TAMPILAN (3 SLIDES)
 * ============================================================================
 *
 * SLIDE 1:
 * ┌─────────────────────────────────┐
 * │      [Logo] BREASTIE            │  ← Header
 * │                                 │
 * │    [Gambar Ilustrasi 1]         │  ← onboarding_1.png
 * │                                 │
 * │  Welcome to Your                │  ← Title
 * │  Supportive Community           │
 * │                                 │
 * │         ● ○ ○                   │  ← Dots (page 1 of 3)
 * │                                 │
 * │   [SKIP]        [NEXT]          │  ← Buttons
 * │                                 │
 * │  Already have account? Sign in  │  ← Link
 * └─────────────────────────────────┘
 *
 * SLIDE 2:
 * - Same layout
 * - Gambar & title berbeda
 * - Dots: ○ ● ○
 *
 * SLIDE 3:
 * - Same layout
 * - Gambar & title berbeda
 * - Dots: ○ ○ ●
 * - Button berubah: [SKIP] [REGISTER NOW]
 *
 * ============================================================================
 * ⚙️ CARA KERJA
 * ============================================================================
 *
 * Flow:
 *
 * 1. App dibuka → OnboardingScreen muncul
 * 2. User di slide 1
 * 3. User swipe kiri ATAU klik "NEXT" → Slide 2
 * 4. User swipe kiri ATAU klik "NEXT" → Slide 3
 * 5. User klik "REGISTER NOW" → onFinish() dipanggil
 * 6. Parent navigate ke Register/Login screen
 *
 * Shortcut:
 * - User klik "SKIP" kapanpun → Langsung onFinish()
 * - User klik "Sign in" → onFinish() (nanti navigate ke login)
 *
 * ============================================================================
 * 📥 PARAMETER (Input)
 * ============================================================================
 *
 * onFinish: () -> Unit
 *   - Fungsi yang dipanggil saat onboarding selesai
 *   - Parent handle navigation ke login/register
 *   - Default = {} (kosong, untuk preview)
 *
 * Contoh:
 * OnboardingScreen(
 *     onFinish = {
 *         // Navigate ke login screen
 *         navController.navigate("login")
 *     }
 * )
 *
 * ============================================================================
 * 💾 DATA SLIDES
 * ============================================================================
 *
 * Ada 3 slides (hard-coded):
 *
 * Data class:
 * data class OnboardingData(
 *     val imageRes: Int,      // Resource ID gambar
 *     val title: String       // Judul slide
 * )
 *
 * List slides:
 * val pages = listOf(
 *     OnboardingData(
 *         imageRes = R.drawable.onboarding_1,
 *         title = "Welcome to Your\nSupportive Community"
 *     ),
 *     OnboardingData(
 *         imageRes = R.drawable.onboarding_2,
 *         title = "Connect, Share, and\nSupport One Another"
 *     ),
 *     OnboardingData(
 *         imageRes = R.drawable.onboarding_3,
 *         title = "Features That Empower\nYour Health Journey"
 *     )
 * )
 *
 * ============================================================================
 * 🎨 KOMPONEN YANG DIPAKAI
 * ============================================================================
 *
 * OnboardingPage.kt:
 *   - Component untuk tiap slide
 *   - Tampilkan logo, gambar, title
 *
 * PageIndicator.kt:
 *   - Dots indicator (● ○ ○)
 *   - Show slide mana yang aktif
 *
 * HorizontalPager (Accompanist):
 *   - Library untuk swipe slides
 *   - Handle gesture swipe kiri/kanan
 *
 * ============================================================================
 * 🎯 KAPAN PAKAI?
 * ============================================================================
 *
 * Screen ini muncul:
 *   ✅ First time user install app
 *   ✅ User logout & login lagi (optional)
 *   ✅ User clear app data
 *
 * Screen ini TIDAK muncul:
 *   ❌ User sudah pernah login (skip onboarding)
 *   ❌ User sudah set "Don't show again"
 *
 * ============================================================================
 * 🔗 HUBUNGAN DENGAN FILE LAIN
 * ============================================================================
 *
 * Pakai komponen:
 *   - OnboardingPage.kt (slide content)
 *   - PageIndicator.kt (dots)
 *
 * Pakai gambar:
 *   - res/drawable/onboarding_1.png
 *   - res/drawable/onboarding_2.png
 *   - res/drawable/onboarding_3.png
 *   - res/drawable/logo_breastie.png
 *
 * Dipanggil dari:
 *   - MainActivity.kt (first screen)
 *   - ATAU LoginActivity.kt (dedicated login flow)
 *
 * Navigate ke:
 *   - LoginScreen / RegisterScreen (via onFinish)
 *
 * ============================================================================
 * 🚀 FUTURE (Nanti)
 * ============================================================================
 *
 * Bisa ditambah:
 *   - Animation slide transition
 *   - Video background
 *   - "Don't show again" checkbox
 *   - SharedPreferences untuk track udah lihat onboarding
 *   - Skip otomatis kalau user udah login
 *
 * SharedPreferences logic:
 * ```
 * // Save saat user finish onboarding
 * sharedPreferences.edit()
 *     .putBoolean("onboarding_completed", true)
 *     .apply()
 *
 * // Check di MainActivity
 * val completed = sharedPreferences
 *     .getBoolean("onboarding_completed", false)
 *
 * if (!completed) {
 *     OnboardingScreen(...)
 * } else {
 *     MainScreen(...)
 * }
 * ```
 *
 * ============================================================================
 * 💡 TIPS UNTUK TIM
 * ============================================================================
 *
 * Cara test:
 *   1. Uninstall app
 *   2. Install lagi
 *   3. OnboardingScreen muncul!
 *
 * Ganti gambar:
 *   - Replace file di res/drawable/
 *   - Nama file harus sama
 *   - Format: PNG, max 2MB
 *
 * Ganti text:
 *   - Edit `pages` list
 *   - Ubah title sesuai kebutuhan
 *
 * ============================================================================
 * ✅ STATUS
 * ============================================================================
 *
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * STATUS: ✅ COMPLETE (UI only)
 * NEXT: Add SharedPreferences skip logic
 * ============================================================================
 */