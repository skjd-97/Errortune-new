package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.data.model.PlaybackState
import com.example.data.model.Track
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.GlitchRed
import com.example.ui.theme.HackerMuted
import com.example.ui.theme.HackerText
import com.example.ui.theme.HackerTextSecondary
import com.example.ui.theme.MatrixDark
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.TechBorder
import com.example.ui.theme.TechSurface
import com.example.ui.theme.TerminalGreen

@Composable
fun FullPlayerDialog(
    playbackState: PlaybackState,
    isDownloading: Boolean,
    downloadProgress: Int?,
    onDismiss: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onToggleShuffle: () -> Unit,
    onToggleRepeat: () -> Unit,
    onCycleSpeed: () -> Unit,
    onToggleVideoMode: () -> Unit,
    onDownloadTrack: (Track) -> Unit
) {
    val track = playbackState.currentTrack ?: return

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MatrixDark
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                // Top Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(TechSurface)
                            .border(1.dp, TechBorder, CircleShape)
                            .testTag("full_player_dismiss")
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Collapse Player",
                            tint = NeonCyan,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Mode Switcher Button: Audio Visualizer vs Video Stream
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(TechSurface)
                            .border(1.dp, NeonCyan.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                            .clickable { onToggleVideoMode() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (playbackState.isVideoMode) Icons.Default.MusicNote else Icons.Default.Videocam,
                            contentDescription = "Switch Mode",
                            tint = if (playbackState.isVideoMode) TerminalGreen else NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (playbackState.isVideoMode) "AUDIO MODE" else "VIDEO STREAM",
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    // Status Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(TechSurface)
                            .border(1.dp, if (track.isDownloaded) TerminalGreen else CyberPurple, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (track.isDownloaded) "OFFLINE" else "ONLINE",
                            color = if (track.isDownloaded) TerminalGreen else CyberPurple,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Content View: Either Video Player or Audio Deck
                if (track.isVideoAvailable) {
                    VideoPlayerView(
                        videoId = track.youtubeVideoId,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    CyberVisualizer(
                        isPlaying = playbackState.isPlaying,
                        barCount = 32,
                        modifier = Modifier.fillMaxWidth().height(36.dp)
                    )
                } else {
                    // Audio Cyber Deck with Rotating Neon Ring and Visualizer
                    val infiniteTransition = rememberInfiniteTransition(label = "deck_rotation")
                    val rotation by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 360f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 14000, easing = LinearEasing),
                            repeatMode = RepeatMode.Restart
                        ),
                        label = "rotation"
                    )

                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Outer neon glowing spinning ring
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .rotate(if (playbackState.isPlaying) rotation else 0f)
                                .clip(CircleShape)
                                .border(
                                    2.5.dp,
                                    Brush.sweepGradient(
                                        listOf(
                                            NeonCyan,
                                            CyberPurple,
                                            TerminalGreen,
                                            NeonCyan
                                        )
                                    ),
                                    CircleShape
                                )
                        )

                        // Center Album Art
                        Box(
                            modifier = Modifier
                                .size(190.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.Black, CircleShape)
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = track.thumbnailUrl,
                                contentDescription = track.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            // Vinyl center spindle hole
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(MatrixDark)
                                    .border(1.5.dp, NeonCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(TerminalGreen)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Animated Cyber Visualizer Waves
                    CyberVisualizer(
                        isPlaying = playbackState.isPlaying,
                        barCount = 32,
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Track Info Details
                Text(
                    text = track.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 2,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = track.artist,
                    color = NeonCyan,
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Tech tags row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "[${track.category.uppercase()}]",
                        color = HackerTextSecondary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "•",
                        color = TechBorder
                    )
                    Text(
                        text = "${track.bitRate}",
                        color = TerminalGreen,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "•",
                        color = TechBorder
                    )
                    Text(
                        text = "${track.views} PLAYS",
                        color = NeonYellow,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Seek Slider & Timestamps
                var isUserDragging by remember { mutableStateOf(false) }
                var draggedValue by remember { mutableFloatStateOf(0f) }

                val currentPos = if (isUserDragging) draggedValue.toLong() else playbackState.currentPositionMs
                val totalDur = playbackState.totalDurationMs.coerceAtLeast(1000L)
                val sliderPos = currentPos.toFloat().coerceIn(0f, totalDur.toFloat())

                Slider(
                    value = sliderPos,
                    onValueChange = {
                        isUserDragging = true
                        draggedValue = it
                    },
                    onValueChangeFinished = {
                        isUserDragging = false
                        onSeekTo(draggedValue.toLong())
                    },
                    valueRange = 0f..totalDur.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = NeonCyan,
                        activeTrackColor = TerminalGreen,
                        inactiveTrackColor = TechBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("player_seek_slider")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(currentPos),
                        color = HackerTextSecondary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                    Text(
                        text = formatTime(totalDur),
                        color = HackerTextSecondary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Primary Playback Controls Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Shuffle
                    IconButton(
                        onClick = onToggleShuffle,
                        modifier = Modifier.testTag("full_player_shuffle")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = "Shuffle",
                            tint = if (playbackState.isShuffle) TerminalGreen else HackerMuted,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    // Previous
                    IconButton(
                        onClick = onSkipPrevious,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(TechSurface)
                            .border(1.dp, TechBorder, CircleShape)
                            .testTag("full_player_previous")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous Track",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Master Cyber Play / Pause
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(NeonCyan.copy(alpha = 0.25f), Color.Transparent)
                                )
                            )
                            .border(2.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (playbackState.isBuffering) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(34.dp),
                                color = NeonCyan,
                                strokeWidth = 3.dp
                            )
                        } else {
                            IconButton(
                                onClick = onTogglePlayPause,
                                modifier = Modifier.size(68.dp).testTag("full_player_play_pause")
                            ) {
                                Icon(
                                    imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (playbackState.isPlaying) "Pause" else "Play",
                                    tint = NeonCyan,
                                    modifier = Modifier.size(38.dp)
                                )
                            }
                        }
                    }

                    // Next
                    IconButton(
                        onClick = onSkipNext,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(TechSurface)
                            .border(1.dp, TechBorder, CircleShape)
                            .testTag("full_player_next")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next Track",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Repeat
                    IconButton(
                        onClick = onToggleRepeat,
                        modifier = Modifier.testTag("full_player_repeat")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = "Repeat",
                            tint = if (playbackState.isRepeat) NeonCyan else HackerMuted,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Secondary Actions: Download Offline & Playback Speed
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(TechSurface)
                        .border(1.dp, TechBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Download Action
                    Row(
                        modifier = Modifier
                            .clickable(enabled = !track.isDownloaded && !isDownloading) {
                                onDownloadTrack(track)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (track.isDownloaded) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Downloaded Offline",
                                tint = TerminalGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "SAVED TO OFFLINE VAULT",
                                color = TerminalGreen,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        } else if (isDownloading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = NeonYellow,
                                strokeWidth = 2.dp
                            )
                            Text(
                                text = "DOWNLOADING ${downloadProgress ?: 0}%",
                                color = NeonYellow,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Download Song",
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "DOWNLOAD FOR OFFLINE",
                                color = NeonCyan,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Speed Toggle
                    Row(
                        modifier = Modifier
                            .clickable { onCycleSpeed() }
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Playback Speed",
                            tint = CyberPurple,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "${playbackState.playbackSpeed}x",
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Real-time Hacker Terminal Logs Console
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF060910))
                        .border(1.dp, TechBorder, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "TERMINAL LOG // errortune_daemon",
                        color = HackerMuted,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = ">> PROTOCOL: AUDIO_STREAM_DECRYPTED [OK]",
                        color = TerminalGreen,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp
                    )
                    Text(
                        text = ">> LOCKSCREEN_PLAYBACK: FOREGROUND_SERVICE_ONLINE",
                        color = NeonCyan,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp
                    )
                    Text(
                        text = ">> OFFLINE_CACHE: AES_GCM_STORED | BUFFER: 100%",
                        color = HackerTextSecondary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp
                    )
                    Text(
                        text = ">> SYS_AUTHOR: Shaikh Javeed (jd._.___) | GITHUB: skjd-97",
                        color = CyberPurple,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // CRT Terminal Scanline Overlay on Player Dialog
            TerminalScanlineOverlay(
                modifier = Modifier.fillMaxSize(),
                scanlineAlpha = 0.10f,
                showSweepBeam = true
            )
        }
    }
}
}

private fun formatTime(millis: Long): String {
    val totalSeconds = (millis / 1000).toInt()
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}
