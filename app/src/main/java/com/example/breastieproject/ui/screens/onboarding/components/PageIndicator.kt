package com.example.breastieproject.ui.screens.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.breastieproject.ui.theme.BackupTheme

@Composable
fun PageIndicator(
    pageCount: Int,          // Total halaman (3 untuk onboarding)
    currentPage: Int,        // Halaman aktif sekarang (0, 1, atau 2)
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pageCount) { page ->
            Box(
                modifier = Modifier
                    .size(if (page == currentPage) 12.dp else 8.dp) // Aktif lebih besar
                    .clip(CircleShape)
                    .background(
                        if (page == currentPage)
                            MaterialTheme.colorScheme.primary    // PinkDark (aktif)
                        else
                            MaterialTheme.colorScheme.secondary   // PinkMedium (inactive)
                    )
            )
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun PageIndicatorPreview() {
    BackupTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Page 1 active
            PageIndicator(pageCount = 3, currentPage = 0)

            // Page 2 active
            PageIndicator(pageCount = 3, currentPage = 1)

            // Page 3 active
            PageIndicator(pageCount = 3, currentPage = 2)
        }
    }
}


/**
 * ============================================================================
 * FILE: PageIndicator.kt
 * LOCATION: ui/screens/onboarding/components/PageIndicator.kt
 * ============================================================================
 *
 * 🎯 DESKRIPSI SUPER SIMPLE:
 * Dots indicator - titik-titik yang show "ini halaman ke berapa".
 * Kayak di Instagram stories atau app onboarding lain.
 *
 * ============================================================================
 * 🤔 UNTUK APA?
 * ============================================================================
 *
 * Visual indicator:
 *   ✅ Show jumlah total pages (3 dots)
 *   ✅ Highlight page yang aktif (● ○ ○)
 *   ✅ User tau "saya di page berapa"
 *
 * ============================================================================
 * 📥 PARAMETER
 * ============================================================================
 *
 * pageCount: Int
 *   - Jumlah total pages
 *   - Contoh: 3 (ada 3 slides)
 *
 * currentPage: Int
 *   - Index page yang aktif (0, 1, atau 2)
 *   - 0 = page 1, 1 = page 2, 2 = page 3
 *
 * ============================================================================
 * 🎨 TAMPILAN
 * ============================================================================
 *
 * Page 1 (currentPage = 0):
 *   ● ○ ○  ← Dot 1 aktif (besar, pink)
 *
 * Page 2 (currentPage = 1):
 *   ○ ● ○  ← Dot 2 aktif
 *
 * Page 3 (currentPage = 2):
 *   ○ ○ ●  ← Dot 3 aktif
 *
 * Styling:
 *   - Aktif: 12.dp, Pink (#EC7FA9)
 *   - Non-aktif: 8.dp, Light Pink (#FFDFF0)
 *
 * ============================================================================
 * 💡 CARA PAKAI
 * ============================================================================
 *
 * PageIndicator(
 *     pageCount = 3,        // 3 pages total
 *     currentPage = 0       // Page 1 aktif
 * )
 *
 * // Auto-update dari pager state
 * val pagerState = rememberPagerState()
 *
 * PageIndicator(
 *     pageCount = pages.size,
 *     currentPage = pagerState.currentPage  // Auto-sync!
 * )
 *
 * ============================================================================
 * 🔗 DIPAKAI DI
 * ============================================================================
 *
 * - OnboardingScreen.kt
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * ============================================================================
 */