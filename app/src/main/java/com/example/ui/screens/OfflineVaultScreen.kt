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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.TechSurfaceElevated
import com.example.ui.theme.TerminalGreen

@Composable
fun OfflineVaultScreen(
    downloadedTracks: List<Track>,
    playbackState: PlaybackState,
    onTrackSelected: (Track, List<Track>) -> Unit,
    onDeleteTrack: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MatrixDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // Vault Header Card
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(TechSurface)
                    .border(1.5.dp, TerminalGreen.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Vault",
                                tint = TerminalGreen,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "ENCRYPTED OFFLINE VAULT",
                                color = TerminalGreen,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF032210))
                                .border(1.dp, TerminalGreen, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "100% AIRGAPPED",
                                color = TerminalGreen,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tracks saved here play instantly anywhere, with zero cellular data or WiFi required. Mobile lockscreen background playback is fully supported.",
                        color = HackerTextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Storage and Count summary
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storage,
                                contentDescription = "Storage",
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${downloadedTracks.size} OFFLINE TRACKS",
                                color = Color.White,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        if (downloadedTracks.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(TerminalGreen)
                                    .clickable {
                                        onTrackSelected(downloadedTracks.first(), downloadedTracks)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("play_all_offline_btn"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play All",
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "PLAY ALL",
                                    color = Color.Black,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (downloadedTracks.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .clip(CircleShape)
                                .background(TechSurface)
                                .border(1.dp, TechBorder, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = "Empty",
                                tint = HackerMuted,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "OFFLINE VAULT IS EMPTY",
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Download any song or YouTube video using the download icon. Once downloaded, you can play them here without internet!",
                            color = HackerMuted,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(downloadedTracks) { track ->
                val isCurrent = playbackState.currentTrack?.id == track.id

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isCurrent) TechSurfaceElevated else TechSurface)
                        .border(1.dp, if (isCurrent) TerminalGreen else TechBorder, RoundedCornerShape(12.dp))
                        .clickable { onTrackSelected(track, downloadedTracks) }
                        .padding(10.dp)
                        .testTag("offline_track_${track.id}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, TerminalGreen.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .background(Color.Black)
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
                                    color = TerminalGreen,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = track.title,
                            color = if (isCurrent) TerminalGreen else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = track.artist,
                            color = HackerTextSecondary,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "LOCAL FILE [READY]",
                                color = TerminalGreen,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "• ${track.duration}",
                                color = HackerMuted,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Delete from vault button
                    IconButton(
                        onClick = { onDeleteTrack(track.id) },
                        modifier = Modifier.size(36.dp).testTag("delete_track_${track.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = GlitchRed.copy(alpha = 0.8f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
