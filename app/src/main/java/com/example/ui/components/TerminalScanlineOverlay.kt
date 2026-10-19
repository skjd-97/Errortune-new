package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

/**
 * Authentic CRT Terminal Scanline Overlay Composable.
 * Renders horizontal phosphor scanlines, a moving CRT refresh beam, and a radial vignette
 * giving the entire Compose UI a true retro green-on-black terminal look.
 */
@Composable
fun TerminalScanlineOverlay(
    modifier: Modifier = Modifier,
    scanlineAlpha: Float = 0.14f,
    showSweepBeam: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "crt_refresh_beam")
    val beamOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "beam_offset"
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .testTag("terminal_scanline_overlay")
    ) {
        val width = size.width
        val height = size.height
        if (width <= 0f || height <= 0f) return@Canvas

        // 1. Horizontal CRT Scanlines (Every 3.5 dp)
        val lineSpacing = 3.5.dp.toPx()
        val numLines = (height / lineSpacing).toInt()
        val phosphorScanline = Color(0xFF00FF66).copy(alpha = scanlineAlpha * 0.45f)
        val darkScanline = Color(0xFF000500).copy(alpha = scanlineAlpha * 1.1f)

        for (i in 0..numLines) {
            val y = i * lineSpacing
            drawLine(
                color = if (i % 2 == 0) darkScanline else phosphorScanline,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.2.dp.toPx()
            )
        }

        // 2. Moving CRT Refresh Beam Sweep
        if (showSweepBeam) {
            val beamY = beamOffset * (height + 160f) - 80f
            val beamHeight = 70.dp.toPx()
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xFF00FF66).copy(alpha = 0.05f),
                        Color(0xFF33FF77).copy(alpha = 0.10f),
                        Color(0xFF00FF66).copy(alpha = 0.05f),
                        Color.Transparent
                    ),
                    startY = beamY - beamHeight,
                    endY = beamY + beamHeight
                ),
                topLeft = Offset(0f, (beamY - beamHeight).coerceAtLeast(0f)),
                size = Size(width, (beamHeight * 2).coerceAtMost(height))
            )
        }

        // 3. CRT Screen Vignette (Darkened edges mimicking rounded CRT glass curvature)
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Transparent,
                    Color(0xFF001204).copy(alpha = 0.25f),
                    Color(0xFF000500).copy(alpha = 0.65f)
                ),
                center = Offset(width / 2f, height / 2f),
                radius = maxOf(width, height) * 0.72f
            )
        )
    }
}

/**
 * Modifier extension to apply the terminal scanline effect directly onto any surface or component.
 */
fun Modifier.terminalScanlineModifier(scanlineAlpha: Float = 0.12f): Modifier = this.drawWithContent {
    drawContent()
    val width = size.width
    val height = size.height
    if (width > 0f && height > 0f) {
        val lineSpacing = 3.5.dp.toPx()
        val numLines = (height / lineSpacing).toInt()
        val scanlineColor = Color(0xFF000500).copy(alpha = scanlineAlpha)
        for (i in 0..numLines step 2) {
            val y = i * lineSpacing
            drawLine(
                color = scanlineColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )
        }
    }
}
