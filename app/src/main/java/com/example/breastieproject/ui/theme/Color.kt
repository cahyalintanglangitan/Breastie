package com.example.breastieproject.ui.theme

import androidx.compose.ui.graphics.Color

// Primary Pink Colors
val PinkPrimary = Color(0xFFEC7FA9)
val PinkLight = Color(0xFFFFB8E0)
val PinkLighter = Color(0xFFFFEDFA)
val PinkDark = Color(0xFFBE5985)

// Background Colors
val PinkBackground = Color(0xFFFFEDFA)
val WhiteBackground = Color.White

// Text Colors
val TextBlack = Color(0xFF333333)
val TextGray = Color(0xFF666666)
val TextLightGray = Color(0xFF999999)

// Additional Colors
val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val ErrorRed = Color(0xFFD32F2F)
val SuccessGreen = Color(0xFF4CAF50)


/**
 * ============================================================================
 * FILE: Color.kt
 * LOCATION: ui/theme/Color.kt
 * ============================================================================
 *
 * 🎯 DESKRIPSI SUPER SIMPLE:
 * File yang define semua WARNA di app.
 * Warna-warna pink yang konsisten di seluruh app.
 *
 * ============================================================================
 * 🎨 WARNA BREASTIE (Pink Theme)
 * ============================================================================
 *
 * PinkLight = #FFEDFA
 *   - Background app (paling terang)
 *   - Dipakai di: Screen background
 *
 * PinkMedium = #FFB8E0
 *   - Pink sedang
 *   - Dipakai di: Buttons, cards
 *
 * PinkPrimary = #EC7FA9
 *   - Pink utama (PALING SERING DIPAKAI!)
 *   - Dipakai di: Headers, selected items, icons
 *
 * PinkDark = #BE5985
 *   - Pink gelap
 *   - Dipakai di: Text emphasis, titles
 *
 * ============================================================================
 * 💡 CARA PAKAI
 * ============================================================================
 *
 * Di Composable:
 *
 * // Pakai warna langsung
 * Text(
 *     text = "Hello",
 *     color = Color(0xFFEC7FA9)  // Pink primary
 * )
 *
 * // ATAU pakai MaterialTheme
 * Text(
 *     text = "Hello",
 *     color = MaterialTheme.colorScheme.primary  // Pink primary
 * )
 *
 * Background(
 *     modifier = Modifier.background(Color(0xFFFFEDFA))  // Pink light
 * )
 *
 * ============================================================================
 * 🔗 DIPAKAI DI
 * ============================================================================
 *
 * - Theme.kt (setup Material theme)
 * - Semua composable screens & components
 *
 * ============================================================================
 * 💡 TIPS
 * ============================================================================
 *
 * Ganti warna tema:
 *   1. Ubah value di sini
 *   2. Warna berubah di SELURUH app!
 *
 * Tambah warna baru:
 *   val GreenAccent = Color(0xFF4CAF50)
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * ============================================================================
 */