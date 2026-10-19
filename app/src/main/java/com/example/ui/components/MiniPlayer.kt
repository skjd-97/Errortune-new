package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.PlaybackState
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.HackerTextSecondary
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TechSurfaceElevated
import com.example.ui.theme.TerminalGreen

@Composable
fun MiniPlayer(
    playbackState: PlaybackState,
    onTogglePlayPause: () -> Unit,
    onSkipNext: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val track = playbackState.currentTrack ?: return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(TechSurfaceElevated)
            .border(1.2.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("mini_player_dock")
    ) {
        // Thin glowing cyber progress indicator at top edge of mini player
        val progress = if (playbackState.totalDurationMs > 0L) {
            (playbackState.currentPositionMs.toFloat() / playbackState.totalDurationMs.toFloat()).coerceIn(0f, 1f)
        } else 0f

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(2.5.dp),
            color = TerminalGreen,
            trackColor = Color(0xFF1E293B)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Track thumbnail with cyber border
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, CyberPurple, RoundedCornerShape(8.dp))
                    .background(Color.Black)
            ) {
                AsyncImage(
                    model = track.thumbnailUrl,
                    contentDescription = track.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Track title & artist
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = track.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = track.artist,
                        color = HackerTextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (track.isDownloaded) "● OFFLINE" else "● CYBER STREAM",
                        color = if (track.isDownloaded) TerminalGreen else NeonCyan,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Mini visualizer bars
            Box(modifier = Modifier.width(42.dp)) {
                CyberVisualizer(
                    isPlaying = playbackState.isPlaying,
                    barCount = 6,
                    modifier = Modifier.height(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Play / Pause button
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(NeonCyan.copy(alpha = 0.15f))
                    .border(1.dp, NeonCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (playbackState.isBuffering) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = NeonCyan,
                        strokeWidth = 2.dp
                    )
                } else {
                    IconButton(
                        onClick = onTogglePlayPause,
                        modifier = Modifier.size(38.dp).testTag("mini_player_play_pause")
                    ) {
                        Icon(
                            imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (playbackState.isPlaying) "Pause" else "Play",
                            tint = NeonCyan,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(2.dp))

            // Skip Next button
            IconButton(
                onClick = onSkipNext,
                modifier = Modifier.size(38.dp).testTag("mini_player_next")
            ) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next Track",
                    tint = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
