package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadedTrackDao {
    @Query("SELECT * FROM downloaded_tracks ORDER BY downloadTimestamp DESC")
    fun getAllTracks(): Flow<List<DownloadedTrackEntity>>

    @Query("SELECT * FROM downloaded_tracks WHERE id = :id LIMIT 1")
    suspend fun getTrackById(id: String): DownloadedTrackEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: DownloadedTrackEntity)

    @Query("DELETE FROM downloaded_tracks WHERE id = :id")
    suspend fun deleteTrackById(id: String)

    @Query("SELECT COUNT(*) FROM downloaded_tracks")
    fun getTrackCount(): Flow<Int>
}
