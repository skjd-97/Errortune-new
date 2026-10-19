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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.PlaybackState
import com.example.data.model.Track
import com.example.ui.theme.CyberPurple
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
fun SearchScreen(
    searchQuery: String,
    searchResults: List<Track>,
    isSearching: Boolean,
    selectedFilter: String,
    playbackState: PlaybackState,
    downloadingSet: Set<String>,
    downloadProgress: Map<String, Int>,
    onQueryChange: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    onPerformSearch: (String) -> Unit,
    onTrackSelected: (Track) -> Unit,
    onOpenVideoMode: (Track) -> Unit,
    onDownloadTrack: (Track) -> Unit
) {
    val focusManager = LocalFocusManager.current

    val quickSearches = listOf(
        "Arijit Singh",
        "Kesariya",
        "Sargam Beats",
        "Sidhu Moose Wala",
        "Alan Walker",
        "Hindi Retro",
        "Romantic Hits",
        "Bollywood"
    )

    val filterOptions = listOf("ALL", "VIDEOS", "BOLLYWOOD", "POP", "SARGAM")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MatrixDark)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Cyber Hacker Terminal Search Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_input_field"),
            placeholder = {
                Text(
                    text = "user@errortune:~$ search query...",
                    color = HackerMuted,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = HackerMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = TechSurface,
                unfocusedContainerColor = TechSurface,
                focusedBorderColor = NeonCyan,
                unfocusedBorderColor = TechBorder,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = TerminalGreen
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                onPerformSearch(searchQuery)
            })
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Filter Options (ALL, VIDEOS, SONGS, HACKER, SARGAM)
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(filterOptions) { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) TerminalGreen.copy(alpha = 0.2f) else TechSurface)
                        .border(1.dp, if (isSelected) TerminalGreen else TechBorder, RoundedCornerShape(8.dp))
                        .clickable { onFilterChange(filter) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "[$filter]",
                        color = if (isSelected) TerminalGreen else HackerMuted,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Tag Suggestions
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(quickSearches) { suggestion ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(TechSurfaceElevated)
                        .border(1.dp, TechBorder, RoundedCornerShape(16.dp))
                        .clickable {
                            onQueryChange(suggestion)
                            onPerformSearch(suggestion)
                        }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "#$suggestion",
                        color = CyberPurple,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Results List / Loading State
        if (isSearching) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = NeonCyan,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = ">> QUERYING YOUTUBE & MUSIC MAINFRAME...",
                        color = TerminalGreen,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }
        } else if (searchResults.isEmpty() && searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NO SIGNALS FOUND FOR: \"$searchQuery\"\nTRY ANOTHER KEYWORD",
                    color = HackerMuted,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                item {
                    Text(
                        text = if (searchQuery.isEmpty()) "// POPULAR CODING & MUSIC SIGNALS" else "// MATCHED ${searchResults.size} RESULTS",
                        color = HackerTextSecondary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }

                items(searchResults) { track ->
                    val isCurrent = playbackState.currentTrack?.id == track.id
                    val isDownloading = downloadingSet.contains(track.id)
                    val progress = downloadProgress[track.id]

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isCurrent) TechSurfaceElevated else TechSurface)
                            .border(1.dp, if (isCurrent) NeonCyan else TechBorder, RoundedCornerShape(10.dp))
                            .clickable { onTrackSelected(track) }
                            .padding(10.dp)
                            .testTag("search_result_${track.id}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Thumbnail with duration badge
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, TechBorder, RoundedCornerShape(8.dp))
                                .background(Color.Black)
                        ) {
                            AsyncImage(
                                model = track.thumbnailUrl,
                                contentDescription = track.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .background(Color.Black.copy(alpha = 0.8f))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = track.duration,
                                    color = Color.White,
                                    fontSize = 8.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Title & Artist
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
                                    text = track.views,
                                    color = NeonYellow,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "•",
                                    color = TechBorder
                                )
                                Text(
                                    text = track.category,
                                    color = CyberPurple,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }

                        // Play Audio Action
                        IconButton(
                            onClick = { onTrackSelected(track) },
                            modifier = Modifier.size(34.dp).testTag("play_audio_${track.id}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = "Play Audio",
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Watch Video Action
                        if (track.isVideoAvailable) {
                            IconButton(
                                onClick = { onOpenVideoMode(track) },
                                modifier = Modifier.size(34.dp).testTag("watch_video_${track.id}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = "Watch Video",
                                    tint = TerminalGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        // Download Offline Action
                        IconButton(
                            onClick = { onDownloadTrack(track) },
                            enabled = !track.isDownloaded && !isDownloading,
                            modifier = Modifier.size(34.dp).testTag("download_track_${track.id}")
                        ) {
                            if (track.isDownloaded) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Downloaded",
                                    tint = TerminalGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            } else if (isDownloading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = NeonYellow,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Download Offline",
                                    tint = HackerMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
