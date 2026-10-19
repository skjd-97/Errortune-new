package com.example.ui

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.download.DownloadManager
import com.example.data.model.PlaybackState
import com.example.data.model.Track
import com.example.data.repository.MusicRepository
import com.example.service.MusicPlaybackService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MusicRepository(application)
    val downloadManager = DownloadManager(application)

    private var playbackService: MusicPlaybackService? = null
    private var isServiceBound = false

    // Tabs: 0: TERMINAL, 1: SEARCH, 2: OFFLINE VAULT, 3: PROFILE
    private val _currentTab = MutableStateFlow(0)
    val currentTab: StateFlow<Int> = _currentTab.asStateFlow()

    private val _isPlayerExpanded = MutableStateFlow(false)
    val isPlayerExpanded: StateFlow<Boolean> = _isPlayerExpanded.asStateFlow()

    private val _isVideoMode = MutableStateFlow(false)
    val isVideoMode: StateFlow<Boolean> = _isVideoMode.asStateFlow()

    private val _featuredTracks = MutableStateFlow<List<Track>>(emptyList())
    val featuredTracks: StateFlow<List<Track>> = _featuredTracks.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Track>>(emptyList())
    val searchResults: StateFlow<List<Track>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _selectedFilter = MutableStateFlow("ALL")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()

    // Sound engine presets (Hacker EQ)
    private val _equalizerPreset = MutableStateFlow("CYBER_BASS")
    val equalizerPreset: StateFlow<String> = _equalizerPreset.asStateFlow()

    // Offline downloaded tracks from Room
    val downloadedTracks: StateFlow<List<Track>> = repository.getDownloadedTracks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Service state holders
    private val _serviceTrack = MutableStateFlow<Track?>(null)
    private val _serviceIsPlaying = MutableStateFlow(false)
    private val _servicePosition = MutableStateFlow(0L)
    private val _serviceDuration = MutableStateFlow(0L)
    private val _serviceIsBuffering = MutableStateFlow(false)
    private val _isShuffle = MutableStateFlow(false)
    private val _isRepeat = MutableStateFlow(false)
    private val _playbackSpeed = MutableStateFlow(1.0f)
    private val _queue = MutableStateFlow<List<Track>>(emptyList())
    private val _queueIndex = MutableStateFlow(0)

    val playbackState: StateFlow<PlaybackState> = combine(
        _serviceTrack,
        _serviceIsPlaying,
        _servicePosition,
        _serviceDuration,
        _serviceIsBuffering,
        _isShuffle,
        _isRepeat,
        _playbackSpeed,
        _isVideoMode
    ) { params ->
        val track = params[0] as Track?
        val isPlaying = params[1] as Boolean
        val pos = params[2] as Long
        val dur = params[3] as Long
        val buffering = params[4] as Boolean
        val shuffle = params[5] as Boolean
        val repeat = params[6] as Boolean
        val speed = params[7] as Float
        val videoMode = params[8] as Boolean

        PlaybackState(
            currentTrack = track,
            isPlaying = isPlaying,
            currentPositionMs = pos,
            totalDurationMs = if (dur > 0L) dur else (track?.durationMs ?: 0L),
            isBuffering = buffering,
            isShuffle = shuffle,
            isRepeat = repeat,
            playbackSpeed = speed,
            isVideoMode = videoMode,
            queue = _queue.value,
            queueIndex = _queueIndex.value
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PlaybackState()
    )

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicPlaybackService.MusicBinder
            val s = binder.getService()
            playbackService = s
            isServiceBound = true

            s.setOnTrackCompletedListener {
                playNext()
            }

            viewModelScope.launch {
                s.currentTrack.collect { _serviceTrack.value = it }
            }
            viewModelScope.launch {
                s.isPlaying.collect { _serviceIsPlaying.value = it }
            }
            viewModelScope.launch {
                s.currentPosition.collect { _servicePosition.value = it }
            }
            viewModelScope.launch {
                s.duration.collect { _serviceDuration.value = it }
            }
            viewModelScope.launch {
                s.isBuffering.collect { _serviceIsBuffering.value = it }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            playbackService = null
            isServiceBound = false
        }
    }

    init {
        bindPlaybackService()
        loadInitialTracks()
    }

    private fun bindPlaybackService() {
        val intent = Intent(getApplication(), MusicPlaybackService::class.java)
        getApplication<Application>().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    fun isOnline(): Boolean = repository.isOnline()

    fun setTab(index: Int) {
        _currentTab.value = index
    }

    fun setPlayerExpanded(expanded: Boolean) {
        _isPlayerExpanded.value = expanded
    }

    fun toggleVideoMode() {
        _isVideoMode.value = !_isVideoMode.value
    }

    fun setVideoMode(enabled: Boolean) {
        _isVideoMode.value = enabled
    }

    fun setEqualizerPreset(preset: String) {
        _equalizerPreset.value = preset
    }

    fun loadInitialTracks() {
        viewModelScope.launch {
            val tracks = repository.getInitialFeaturedTracks()
            _featuredTracks.value = tracks
            if (_queue.value.isEmpty()) {
                _queue.value = tracks
            }
        }
    }

    fun setFilter(filter: String) {
        _selectedFilter.value = filter
        if (_searchQuery.value.isNotBlank()) {
            performSearch(_searchQuery.value)
        }
    }

    private var searchJob: kotlinx.coroutines.Job? = null

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            _searchResults.value = emptyList()
            _isSearching.value = false
        } else {
            searchJob = viewModelScope.launch {
                kotlinx.coroutines.delay(350)
                performSearch(trimmed)
            }
        }
    }

    fun performSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _isSearching.value = true
            val results = repository.searchTracks(query)
            val filtered = when (_selectedFilter.value) {
                "VIDEOS" -> results.filter { it.isVideoAvailable }
                "BOLLYWOOD" -> results.filter { it.category.contains("Bollywood", true) || it.title.contains("Bollywood", true) || it.title.contains("Hindi", true) || it.artist.contains("Arijit", true) || it.artist.contains("Pritam", true) }
                "POP" -> results.filter { it.category.contains("Pop", true) || it.title.contains("Pop", true) }
                "SARGAM" -> results.filter { it.title.contains("Sargam", true) || it.category.contains("Sargam", true) }
                else -> results
            }
            _searchResults.value = if (filtered.isNotEmpty()) filtered else results
            _isSearching.value = false
        }
    }

    fun playTrack(track: Track, newQueue: List<Track>? = null) {
        if (newQueue != null && newQueue.isNotEmpty()) {
            _queue.value = newQueue
            _queueIndex.value = newQueue.indexOfFirst { it.id == track.id }.coerceAtLeast(0)
        } else if (!_queue.value.any { it.id == track.id }) {
            _queue.value = _queue.value + track
            _queueIndex.value = _queue.value.lastIndex
        } else {
            _queueIndex.value = _queue.value.indexOfFirst { it.id == track.id }.coerceAtLeast(0)
        }

        _serviceTrack.value = track
        if (track.isVideoAvailable) {
            _isVideoMode.value = true
            _isPlayerExpanded.value = true
        }
        playbackService?.playTrack(track)
    }

    fun togglePlayPause() {
        playbackService?.togglePlayPause()
    }

    fun seekTo(positionMs: Long) {
        playbackService?.seekTo(positionMs)
    }

    fun playNext() {
        val q = _queue.value
        if (q.isEmpty()) return
        val nextIdx = if (_isShuffle.value) {
            (0 until q.size).random()
        } else {
            (_queueIndex.value + 1) % q.size
        }
        _queueIndex.value = nextIdx
        val nextTrack = q[nextIdx]
        playTrack(nextTrack)
    }

    fun playPrevious() {
        val q = _queue.value
        if (q.isEmpty()) return
        val prevIdx = if (_queueIndex.value - 1 < 0) q.size - 1 else _queueIndex.value - 1
        _queueIndex.value = prevIdx
        val prevTrack = q[prevIdx]
        playTrack(prevTrack)
    }

    fun toggleShuffle() {
        _isShuffle.value = !_isShuffle.value
    }

    fun toggleRepeat() {
        _isRepeat.value = !_isRepeat.value
    }

    fun cyclePlaybackSpeed() {
        val speeds = listOf(1.0f, 1.25f, 1.5f, 2.0f, 0.75f)
        val currentIdx = speeds.indexOf(_playbackSpeed.value).takeIf { it >= 0 } ?: 0
        val nextSpeed = speeds[(currentIdx + 1) % speeds.size]
        _playbackSpeed.value = nextSpeed
        playbackService?.setPlaybackSpeed(nextSpeed)
    }

    fun downloadTrack(track: Track) {
        viewModelScope.launch {
            downloadManager.downloadTrack(track)
            loadInitialTracks()
        }
    }

    fun deleteDownloadedTrack(trackId: String) {
        viewModelScope.launch {
            downloadManager.deleteDownloadedTrack(trackId)
            loadInitialTracks()
        }
    }

    override fun onCleared() {
        if (isServiceBound) {
            try {
                getApplication<Application>().unbindService(serviceConnection)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            isServiceBound = false
        }
        super.onCleared()
    }
}
