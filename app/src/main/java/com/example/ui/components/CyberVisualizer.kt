package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TerminalGreen
import kotlin.random.Random

@Composable
fun CyberVisualizer(
    isPlaying: Boolean,
    barCount: Int = 28,
    modifier: Modifier = Modifier.fillMaxWidth().height(60.dp)
) {
    val transition = rememberInfiniteTransition(label = "cyber_bars")
    val pulse1 by transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 420, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse1"
    )
    val pulse2 by transition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 310, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse2"
    )
    val pulse3 by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 530, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse3"
    )

    Canvas(modifier = modifier) {
        val totalWidth = size.width
        val canvasHeight = size.height
        val barWidth = (totalWidth / barCount) * 0.65f
        val spacing = (totalWidth / barCount) * 0.35f

        for (i in 0 until barCount) {
            val factor = when (i % 3) {
                0 -> pulse1
                1 -> pulse2
                else -> pulse3
            }

            // Pseudo-random height variation per bar
            val pseudoRandom = (kotlin.math.sin(i * 0.8) + 1.0) / 2.0
            val barHeight = if (isPlaying) {
                (canvasHeight * (0.15f + 0.85f * factor * pseudoRandom.toFloat())).coerceIn(6f, canvasHeight)
            } else {
                8f
            }

            val x = i * (barWidth + spacing)
            val y = canvasHeight - barHeight

            val brush = Brush.verticalGradient(
                colors = listOf(
                    NeonCyan,
                    TerminalGreen,
                    CyberPurple
                ),
                startY = y,
                endY = canvasHeight
            )

            drawRoundRect(
                brush = brush,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}
