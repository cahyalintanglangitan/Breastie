package com.example.breastieproject.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.breastieproject.R



// ✅ Raleway Font Family (3 FILES ONLY!)
val RalewayFont = FontFamily(
    Font(R.font.raleway_regular, FontWeight.Normal),      // 400 - Regular
    Font(R.font.raleway_semibold, FontWeight.SemiBold),  // 600 - SemiBold
    Font(R.font.raleway_bold, FontWeight.Bold)           // 700 - Bold
)

// ✅ Typography (Apply Raleway to all text styles)
val Typography = Typography(
    // Display - untuk judul besar
    displayLarge = TextStyle(
        fontFamily = RalewayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp
    ),
    displayMedium = TextStyle(
        fontFamily = RalewayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),

    // Title - untuk judul section
    titleLarge = TextStyle(
        fontFamily = RalewayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),

    // Body - untuk text biasa
    bodyLarge = TextStyle(
        fontFamily = RalewayFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = RalewayFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    // Label - untuk button text
    labelLarge = TextStyle(
        fontFamily = RalewayFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
)


/**
 * ============================================================================
 * FILE: Type.kt
 * LOCATION: ui/theme/Type.kt
 * ============================================================================
 *
 * 🎯 DESKRIPSI SUPER SIMPLE:
 * File yang setup FONT Raleway untuk seluruh app.
 * Raleway = Font yang kita pakai (elegant & modern).
 *
 * ============================================================================
 * 📁 FILE FONT YANG KITA PUNYA (res/font/)
 * ============================================================================
 *
 * res/font/
 * ├── raleway_regular.ttf      ← Normal text (400)
 * ├── raleway_semibold.ttf     ← Semi bold (600)
 * └── raleway_bold.ttf          ← Very bold (700)
 *
 * CUMA 3 FILES! Cukup untuk semua kebutuhan! ✅
 *
 * ============================================================================
 * 🎨 CARA KERJA
 * ============================================================================
 *
 * 1. Font files di res/font/ (3 files)
 * 2. Type.kt load 3 font files
 * 3. Create RalewayFont family
 * 4. Setup Typography (semua text styles)
 * 5. Theme.kt apply Typography
 * 6. SELURUH app pakai Raleway!
 *
 * ============================================================================
 * 💡 CARA PAKAI (SUPER MUDAH!)
 * ============================================================================
 *
 * OTOMATIS (Tinggal pakai!):
 *
 * Text(
 *     text = "Community",
 *     style = MaterialTheme.typography.titleLarge  // Auto Raleway Bold!
 * )
 *
 * Text(
 *     text = "Description here",
 *     style = MaterialTheme.typography.bodyMedium  // Auto Raleway Regular!
 * )
 *
 * ============================================================================
 * 📋 TYPOGRAPHY STYLES (Kapan pakai yang mana?)
 * ============================================================================
 *
 * JUDUL BESAR:
 *   displayLarge  → 32.sp, Bold     (Judul utama screen)
 *   displayMedium → 28.sp, Bold     (Sub-judul besar)
 *
 * JUDUL SECTION:
 *   titleLarge    → 24.sp, Bold     (Card title, header)
 *
 * TEXT BIASA:
 *   bodyLarge     → 16.sp, Regular  (Post content, paragraf)
 *   bodyMedium    → 14.sp, Regular  (Description, caption)
 *
 * BUTTON TEXT:
 *   labelLarge    → 16.sp, Bold     (Button text)
 *
 * ============================================================================
 * 💡 CONTOH REAL PAKAI
 * ============================================================================
 *
 * Screen Title:
 * Text(
 *     text = "Community",
 *     style = MaterialTheme.typography.displayMedium  // 28.sp Bold
 * )
 *
 * Card Title:
 * Text(
 *     text = "Pejuang Kemo",
 *     style = MaterialTheme.typography.titleLarge  // 24.sp Bold
 * )
 *
 * Description:
 * Text(
 *     text = "Komunitas untuk saling mendukung...",
 *     style = MaterialTheme.typography.bodyMedium  // 14.sp Regular
 * )
 *
 * Button:
 * Text(
 *     text = "Join",
 *     style = MaterialTheme.typography.labelLarge  // 16.sp Bold
 * )
 *
 * ============================================================================
 * 🔍 TROUBLESHOOTING
 * ============================================================================
 *
 * Problem: Font gak muncul
 * Solution:
 *   1. Check file names: raleway_regular.ttf, raleway_semibold.ttf, raleway_bold.ttf
 *   2. File → Sync Project with Gradle Files
 *   3. Build → Clean Project
 *   4. Build → Rebuild Project
 *
 * ============================================================================
 * 💡 TIPS UNTUK TIM
 * ============================================================================
 *
 * Kinan (Dashboard):
 *   - displayMedium untuk "Dashboard" title
 *   - bodyMedium untuk stats description
 *
 * Yanda (Reminder):
 *   - titleLarge untuk reminder title
 *   - bodyMedium untuk time
 *
 * Alfa (AI):
 *   - bodyLarge untuk chat messages
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * STATUS: ✅ COMPLETE & WORKING
 * ============================================================================
 */