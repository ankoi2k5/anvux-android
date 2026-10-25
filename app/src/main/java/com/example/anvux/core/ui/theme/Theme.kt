package com.example.anvux.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AnVuxDarkColorScheme = darkColorScheme(
    primary = Violet,
    onPrimary = OnViolet,
    primaryContainer = VioletContainer,
    onPrimaryContainer = OnVioletContainer,
    secondary = Sky,
    onSecondary = OnSky,
    tertiary = Mint,
    onTertiary = OnMint,
    background = Night,
    onBackground = TextPrimary,
    surface = NightSurface,
    onSurface = TextPrimary,
    surfaceVariant = NightSurfaceHigh,
    onSurfaceVariant = TextSecondary,
    outline = Outline,
    error = ErrorRed,
    onError = OnErrorRed,
)

@Composable
fun AnVuxTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AnVuxDarkColorScheme,
        typography = Typography,
        shapes = AnVuxShapes,
        content = content,
    )
}