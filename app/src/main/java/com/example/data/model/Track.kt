package com.example.data.model

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val duration: String,
    val durationMs: Long = 180000L,
    val thumbnailUrl: String,
    val audioUrl: String,
    val videoUrl: String = "",
    val youtubeVideoId: String = "",
    val isDownloaded: Boolean = false,
    val localFilePath: String? = null,
    val category: String = "Cyber Music",
    val views: String = "1.2M",
    val bitRate: String = "320 kbps"
) {
    val isVideoAvailable: Boolean
        get() = youtubeVideoId.isNotBlank() || videoUrl.isNotBlank()
}

data class PlaybackState(
    val currentTrack: Track? = null,
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val totalDurationMs: Long = 0L,
    val isBuffering: Boolean = false,
    val isShuffle: Boolean = false,
    val isRepeat: Boolean = false,
    val playbackSpeed: Float = 1.0f,
    val isVideoMode: Boolean = false,
    val queue: List<Track> = emptyList(),
    val queueIndex: Int = 0,
    val errorMessage: String? = null
)
