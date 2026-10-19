package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.data.model.Track
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.HackerMuted
import com.example.ui.theme.HackerTextSecondary
import com.example.ui.theme.MatrixDark
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.TechBorder
import com.example.ui.theme.TechSurface
import com.example.ui.theme.TechSurfaceElevated
import com.example.ui.theme.TerminalGreen

@Composable
fun TerminalHomeScreen(
    tracks: List<Track>,
    playbackState: PlaybackState,
    isOnline: Boolean,
    downloadingSet: Set<String>,
    downloadProgress: Map<String, Int>,
    onTrackSelected: (Track) -> Unit,
    onDownloadTrack: (Track) -> Unit,
    onOpenVideoMode: (Track) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    var selectedGenre by remember { mutableStateOf("ALL") }
    val genres = listOf("ALL", "BOLLYWOOD HITS", "ROMANTIC MELODIES", "SARGAM MELODIES", "GLOBAL POP", "CHILL MELODIES")

    val filteredTracks = if (selectedGenre == "ALL") {
        tracks
    } else {
        tracks.filter { it.category.equals(selectedGenre, ignoreCase = true) }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MatrixDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // Hacker HUD System Status Header
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(TechSurface)
                    .border(1.dp, TechBorder, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (isOnline) Icons.Default.Wifi else Icons.Default.WifiOff,
                        contentDescription = "Status",
                        tint = if (isOnline) TerminalGreen else NeonYellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isOnline) "SYS_STAT: ONLINE" else "SYS_STAT: OFFLINE_VAULT",
                        color = if (isOnline) TerminalGreen else NeonYellow,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = "320 KBPS | NO_AUTH",
                    color = NeonCyan,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }


        // Genre / Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(genres) { genre ->
                    val isSelected = selectedGenre == genre
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else TechSurface)
                            .border(
                                1.dp,
                                if (isSelected) NeonCyan else TechBorder,
                                RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedGenre = genre }
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = genre,
                            color = if (isSelected) NeonCyan else HackerMuted,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Section Title: Featured Trending Tracks
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "// TRENDING MUSIC RADAR",
                    color = TerminalGreen,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = "${filteredTracks.size} SIGNALS",
                    color = HackerMuted,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Track Items List
        items(filteredTracks) { track ->
            val isCurrent = playbackState.currentTrack?.id == track.id
            val isDownloading = downloadingSet.contains(track.id)
            val progress = downloadProgress[track.id]

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isCurrent) TechSurfaceElevated else TechSurface)
                    .border(
                        1.dp,
                        if (isCurrent) NeonCyan else TechBorder,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onTrackSelected(track) }
                    .padding(10.dp)
                    .testTag("track_item_${track.id}"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Artwork thumbnail
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, if (isCurrent) NeonCyan else TechBorder, RoundedCornerShape(8.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = track.thumbnailUrl,
                        contentDescription = track.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    if (isCurrent && playbackState.isPlaying) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "▶",
                                color = NeonCyan,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Track details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = track.title,
                        color = if (isCurrent) NeonCyan else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
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
                            text = "• ${track.duration}",
                            color = HackerMuted,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "[${track.category}]",
                            color = CyberPurple,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 9.sp
                        )
                        if (track.isDownloaded) {
                            Text(
                                text = "SAVED OFFLINE",
                                color = TerminalGreen,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Video stream button
                if (track.isVideoAvailable) {
                    IconButton(
                        onClick = { onOpenVideoMode(track) },
                        modifier = Modifier.size(36.dp).testTag("video_mode_btn_${track.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Watch Video",
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Download Offline Action Button
                IconButton(
                    onClick = { onDownloadTrack(track) },
                    enabled = !track.isDownloaded && !isDownloading,
                    modifier = Modifier.size(36.dp).testTag("download_btn_${track.id}")
                ) {
                    if (track.isDownloaded) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Downloaded",
                            tint = TerminalGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    } else if (isDownloading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = NeonYellow,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download Offline",
                            tint = HackerMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
