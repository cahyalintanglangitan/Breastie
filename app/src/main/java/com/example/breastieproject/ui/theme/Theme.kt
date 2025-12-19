package com.example.breastieproject.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// ✅ LIGHT COLOR SCHEME ONLY!
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFEC7FA9),           // Pink primary
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFB8E0),
    onPrimaryContainer = Color(0xFF333333),

    secondary = Color(0xFFBE5985),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFEDFA),
    onSecondaryContainer = Color(0xFF333333),

    tertiary = Color(0xFFFFB8E0),
    onTertiary = Color.White,

    background = Color(0xFFFFEDFA),        // Pink background
    onBackground = Color(0xFF333333),

    surface = Color.White,
    onSurface = Color(0xFF333333),
    surfaceVariant = Color(0xFFFFF3F8),
    onSurfaceVariant = Color(0xFF666666),

    error = Color(0xFFD32F2F),
    onError = Color.White,

    outline = Color(0xFFEEEEEE),
    outlineVariant = Color(0xFFF5F5F5)
)

@Composable
fun BackupTheme(
    darkTheme: Boolean = false,  // ✅ ALWAYS FALSE! FORCE LIGHT MODE!
    dynamicColor: Boolean = false,  // ✅ DISABLE DYNAMIC COLOR
    content: @Composable () -> Unit
) {
    // ✅ ALWAYS USE LIGHT COLOR SCHEME!
    val colorScheme = LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}