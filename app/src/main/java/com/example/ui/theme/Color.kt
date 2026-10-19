package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Pure Green-on-Black CRT Terminal Color Palette
val TerminalBlack = Color(0xFF020602)            // Pure CRT Obsidian Black
val TerminalDark = Color(0xFF040A04)             // Terminal Screen Void
val TerminalSurface = Color(0xFF091409)          // Terminal Panel & Card Surface
val TerminalSurfaceElevated = Color(0xFF0F220F)  // Elevated Console Deck
val TerminalBorder = Color(0xFF14532D)           // Phosphor Frame Border
val TerminalBorderBright = Color(0xFF22C55E)     // Active Focus Border

// Phosphor Green Spectrum
val TerminalPhosphor = Color(0xFF00FF66)         // Primary Vibrant Phosphor Glow
val TerminalGreenBright = Color(0xFF33FF77)      // High Luminescence Accent
val TerminalGreenDim = Color(0xFF16A34A)         // Secondary Active Element
val TerminalGreenText = Color(0xFF4ADE80)        // Readable Terminal Text
val TerminalGreenMuted = Color(0xFF166534)       // Dim Green Telemetry / Muted

// Terminal Status & Accents
val TerminalCursor = Color(0xFF00FF66)           // Blinking Command Prompt
val TerminalAmber = Color(0xFFFACC15)            // Warning / Alert Signal
val TerminalAlert = Color(0xFFEF4444)            // Panic / Error Output
val TerminalCyanAccent = Color(0xFF22D3EE)       // Auxiliary Sub-routine

// Compatibility bindings for existing UI tokens
val MatrixDark = TerminalBlack
val TechSurface = TerminalSurface
val TechSurfaceElevated = TerminalSurfaceElevated
val TechBorder = TerminalBorder
val TerminalGreen = TerminalPhosphor
val NeonCyan = TerminalPhosphor                 // Cyan components now emit radiant terminal phosphor green!
val CyberPurple = TerminalGreenBright
val HackerText = TerminalGreenText
val HackerTextSecondary = Color(0xFF22C55E)
val HackerMuted = TerminalGreenMuted
val NeonYellow = TerminalAmber
val GlitchRed = TerminalAlert
