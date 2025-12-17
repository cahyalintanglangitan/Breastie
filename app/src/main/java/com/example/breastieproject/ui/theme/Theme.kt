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

// Dark Mode Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = PinkDark,           // EC7FA9
    onPrimary = White,
    secondary = PinkMedium,        // FFB8E0
    onSecondary = White,
    tertiary = PinkDeep,           // BE5985
    background = Color(0xFF1A1A1A), // Dark gray background
    onBackground = White,
    surface = Color(0xFF2A2A2A),   // Slightly lighter gray
    onSurface = White
)

// Light Mode Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = PinkDark,           // EC7FA9
    onPrimary = White,
    secondary = PinkMedium,        // FFB8E0
    onSecondary = White,
    tertiary = PinkDeep,           // BE5985
    background = PinkLight,        // FFEDFA
    onBackground = Black,
    surface = White,
    onSurface = Black
)

val PinkBackground = Color(0xFFFFF5FA)
val PinkTopBar = Color(0xFFFFB8E0)
val TextBlack = Color(0xFF1C1C1E)

@Composable
fun BackupTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}