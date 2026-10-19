package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TerminalColorScheme = darkColorScheme(
    primary = TerminalPhosphor,
    onPrimary = TerminalBlack,
    primaryContainer = Color(0xFF003814),
    onPrimaryContainer = TerminalGreenBright,
    secondary = TerminalGreenBright,
    onSecondary = TerminalBlack,
    secondaryContainer = Color(0xFF052E16),
    onSecondaryContainer = Color(0xFF86EFAC),
    tertiary = TerminalGreenDim,
    onTertiary = TerminalBlack,
    tertiaryContainer = Color(0xFF063318),
    onTertiaryContainer = Color(0xFFA7F3D0),
    background = TerminalBlack,
    onBackground = TerminalGreenText,
    surface = TerminalSurface,
    onSurface = TerminalGreenText,
    surfaceVariant = TerminalSurfaceElevated,
    onSurfaceVariant = TerminalGreenBright,
    outline = TerminalBorder,
    outlineVariant = TerminalBorderBright,
    error = TerminalAlert,
    onError = TerminalBlack
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false, // Keep pure green-on-black terminal theme consistent
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TerminalColorScheme,
        typography = Typography,
        content = content
    )
}
