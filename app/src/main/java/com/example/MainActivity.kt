package com.example

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MusicViewModel
import com.example.ui.components.FullPlayerDialog
import com.example.ui.components.MiniPlayer
import com.example.ui.components.TerminalScanlineOverlay
import com.example.ui.screens.OfflineVaultScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.TerminalHomeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                ErrorTuneApp()
            }
        }
    }
}

@Composable
fun ErrorTuneApp(viewModel: MusicViewModel = viewModel()) {
    val context = LocalContext.current

    // Request notification permission on Android 13+ for lockscreen controls
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { /* Handled gracefully */ }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val currentTab by viewModel.currentTab.collectAsState()
    val playbackState by viewModel.playbackState.collectAsState()
    val isPlayerExpanded by viewModel.isPlayerExpanded.collectAsState()
    val featuredTracks by viewModel.featuredTracks.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val downloadedTracks by viewModel.downloadedTracks.collectAsState()
    val isDownloading by viewModel.downloadManager.isDownloading.collectAsState()
    val downloadProgress by viewModel.downloadManager.downloadProgress.collectAsState()
    val eqPreset by viewModel.equalizerPreset.collectAsState()

    // Android system BackHandler: collapse player or navigate to home tab
    BackHandler(enabled = isPlayerExpanded || currentTab != 0) {
        if (isPlayerExpanded) {
            viewModel.setPlayerExpanded(false)
        } else {
            viewModel.setTab(0)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = MatrixDark,
        topBar = {
            CyberTopBar(
                isOnline = viewModel.isOnline(),
                onSearchClick = { viewModel.setTab(1) }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.fillMaxWidth().background(MatrixDark)) {
                // Docked Mini Player
                if (playbackState.currentTrack != null) {
                    MiniPlayer(
                        playbackState = playbackState,
                        onTogglePlayPause = { viewModel.togglePlayPause() },
                        onSkipNext = { viewModel.playNext() },
                        onClick = { viewModel.setPlayerExpanded(true) }
                    )
                }

                // Cyber Hacker Bottom Navigation
                CyberBottomNavigationBar(
                    selectedTab = currentTab,
                    onTabSelected = { viewModel.setTab(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> TerminalHomeScreen(
                    tracks = featuredTracks,
                    playbackState = playbackState,
                    isOnline = viewModel.isOnline(),
                    downloadingSet = isDownloading,
                    downloadProgress = downloadProgress,
                    onTrackSelected = { track ->
                        viewModel.playTrack(track, featuredTracks)
                        if (track.isVideoAvailable) {
                            viewModel.setVideoMode(true)
                            viewModel.setPlayerExpanded(true)
                        }
                    },
                    onDownloadTrack = { track ->
                        Toast.makeText(context, "Downloading \"${track.title}\" to Offline Vault...", Toast.LENGTH_SHORT).show()
                        viewModel.downloadTrack(track)
                    },
                    onOpenVideoMode = { track ->
                        viewModel.playTrack(track, featuredTracks)
                        viewModel.setVideoMode(true)
                        viewModel.setPlayerExpanded(true)
                    },
                    onNavigateToSearch = { viewModel.setTab(1) }
                )
                1 -> SearchScreen(
                    searchQuery = searchQuery,
                    searchResults = if (searchQuery.isEmpty()) featuredTracks else searchResults,
                    isSearching = isSearching,
                    selectedFilter = selectedFilter,
                    playbackState = playbackState,
                    downloadingSet = isDownloading,
                    downloadProgress = downloadProgress,
                    onQueryChange = { viewModel.onSearchQueryChanged(it) },
                    onFilterChange = { viewModel.setFilter(it) },
                    onPerformSearch = { viewModel.performSearch(it) },
                    onTrackSelected = { track ->
                        val currentList = if (searchQuery.isEmpty()) featuredTracks else searchResults
                        viewModel.playTrack(track, currentList)
                        if (track.isVideoAvailable) {
                            viewModel.setVideoMode(true)
                            viewModel.setPlayerExpanded(true)
                        }
                    },
                    onOpenVideoMode = { track ->
                        val currentList = if (searchQuery.isEmpty()) featuredTracks else searchResults
                        viewModel.playTrack(track, currentList)
                        viewModel.setVideoMode(true)
                        viewModel.setPlayerExpanded(true)
                    },
                    onDownloadTrack = { track ->
                        Toast.makeText(context, "Saving \"${track.title}\" for offline playback...", Toast.LENGTH_SHORT).show()
                        viewModel.downloadTrack(track)
                    }
                )
                2 -> OfflineVaultScreen(
                    downloadedTracks = downloadedTracks,
                    playbackState = playbackState,
                    onTrackSelected = { track, list ->
                        viewModel.playTrack(track, list)
                    },
                    onDeleteTrack = { trackId ->
                        viewModel.deleteDownloadedTrack(trackId)
                        Toast.makeText(context, "Removed from Offline Vault", Toast.LENGTH_SHORT).show()
                    }
                )
                3 -> ProfileScreen()
            }

            // Authentic Green CRT Terminal Scanline Overlay Effect
            TerminalScanlineOverlay(
                modifier = Modifier.fillMaxSize(),
                scanlineAlpha = 0.12f,
                showSweepBeam = true
            )
        }
    }

    // Full Screen Cyber Player Dialog
    if (isPlayerExpanded && playbackState.currentTrack != null) {
        val currentTrack = playbackState.currentTrack!!
        val isCurrentDownloading = isDownloading.contains(currentTrack.id)
        val progress = downloadProgress[currentTrack.id]

        FullPlayerDialog(
            playbackState = playbackState,
            isDownloading = isCurrentDownloading,
            downloadProgress = progress,
            onDismiss = { viewModel.setPlayerExpanded(false) },
            onTogglePlayPause = { viewModel.togglePlayPause() },
            onSeekTo = { viewModel.seekTo(it) },
            onSkipNext = { viewModel.playNext() },
            onSkipPrevious = { viewModel.playPrevious() },
            onToggleShuffle = { viewModel.toggleShuffle() },
            onToggleRepeat = { viewModel.toggleRepeat() },
            onCycleSpeed = { viewModel.cyclePlaybackSpeed() },
            onToggleVideoMode = { viewModel.toggleVideoMode() },
            onDownloadTrack = { track ->
                Toast.makeText(context, "Downloading to Offline Vault...", Toast.LENGTH_SHORT).show()
                viewModel.downloadTrack(track)
            }
        )
    }
}

@Composable
fun CyberTopBar(
    isOnline: Boolean,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MatrixDark)
            .statusBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // App Brand & Logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(TerminalSurface)
                    .border(1.5.dp, TerminalGreen, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ">_",
                    color = TerminalGreen,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
            }

            Column {
                Text(
                    text = "errortune",
                    color = TerminalGreen,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "SYS://TERMINAL_CORE_v1.0",
                    color = TerminalGreenDim,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp
                )
            }
        }

        // Action icons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(TechSurface)
                    .border(1.dp, TechBorder, CircleShape)
                    .clickable { onSearchClick() }
                    .padding(8.dp)
                    .testTag("top_bar_search_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = NeonCyan,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun CyberBottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val items = listOf(
        NavigationItem(0, "TERMINAL", Icons.Default.Terminal),
        NavigationItem(1, "SEARCH", Icons.Default.Search),
        NavigationItem(2, "OFFLINE", Icons.Default.Download),
        NavigationItem(3, "DEV_JD", Icons.Default.Code)
    )

    NavigationBar(
        containerColor = TechSurface,
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, TechBorder.copy(alpha = 0.6f))
    ) {
        items.forEach { item ->
            val isSelected = selectedTab == item.index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.index) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        modifier = Modifier.size(20.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NeonCyan,
                    selectedTextColor = NeonCyan,
                    unselectedIconColor = HackerMuted,
                    unselectedTextColor = HackerMuted,
                    indicatorColor = NeonCyan.copy(alpha = 0.15f)
                ),
                modifier = Modifier.testTag("nav_item_${item.label.lowercase()}")
            )
        }
    }
}

private data class NavigationItem(
    val index: Int,
    val label: String,
    val icon: ImageVector
)
