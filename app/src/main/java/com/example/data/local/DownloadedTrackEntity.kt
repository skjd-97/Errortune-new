package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "downloaded_tracks")
data class DownloadedTrackEntity(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val duration: String,
    val durationMs: Long,
    val thumbnailUrl: String,
    val localFilePath: String,
    val fileSizeFormatted: String,
    val downloadTimestamp: Long = System.currentTimeMillis(),
    val category: String = "Offline Audio",
    val bitRate: String = "320 kbps"
)
