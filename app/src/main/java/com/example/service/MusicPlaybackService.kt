package com.example.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.media.session.PlaybackState as SessionPlaybackState
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File

class MusicPlaybackService : Service() {

    inner class MusicBinder : Binder() {
        fun getService(): MusicPlaybackService = this@MusicPlaybackService
    }

    private val binder = MusicBinder()
    private var mediaPlayer: MediaPlayer? = null
    private var mediaSession: MediaSession? = null
    private var audioManager: AudioManager? = null
    private var audioFocusRequest: AudioFocusRequest? = null

    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null

    private val _currentTrack = MutableStateFlow<Track?>(null)
    val currentTrack: StateFlow<Track?> = _currentTrack.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private var onTrackCompletedListener: (() -> Unit)? = null

    companion object {
        const val CHANNEL_ID = "errortune_playback_channel"
        const val NOTIFICATION_ID = 1337

        const val ACTION_PLAY = "com.example.errortune.ACTION_PLAY"
        const val ACTION_PAUSE = "com.example.errortune.ACTION_PAUSE"
        const val ACTION_TOGGLE = "com.example.errortune.ACTION_TOGGLE"
        const val ACTION_NEXT = "com.example.errortune.ACTION_NEXT"
        const val ACTION_PREV = "com.example.errortune.ACTION_PREV"
        const val ACTION_STOP = "com.example.errortune.ACTION_STOP"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        initMediaSession()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_PLAY -> resumePlayback()
            ACTION_PAUSE -> pausePlayback()
            ACTION_TOGGLE -> togglePlayPause()
            ACTION_NEXT -> onTrackCompletedListener?.invoke()
            ACTION_PREV -> seekTo(0L)
            ACTION_STOP -> stopPlayback()
        }
        return START_NOT_STICKY
    }

    private fun initMediaSession() {
        mediaSession = MediaSession(this, "ErrorTuneSession").apply {
            setCallback(object : MediaSession.Callback() {
                override fun onPlay() {
                    resumePlayback()
                }

                override fun onPause() {
                    pausePlayback()
                }

                override fun onSkipToNext() {
                    onTrackCompletedListener?.invoke()
                }

                override fun onSkipToPrevious() {
                    seekTo(0L)
                }

                override fun onSeekTo(pos: Long) {
                    seekTo(pos)
                }
            })
            isActive = true
        }
    }

    fun setOnTrackCompletedListener(listener: () -> Unit) {
        onTrackCompletedListener = listener
    }

    fun playTrack(track: Track) {
        _currentTrack.value = track
        _isBuffering.value = true

        requestAudioFocus()

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )

            setOnPreparedListener { mp ->
                _isBuffering.value = false
                _duration.value = mp.duration.toLong().coerceAtLeast(0L)
                mp.start()
                _isPlaying.value = true
                updateMediaSessionState(SessionPlaybackState.STATE_PLAYING)
                startProgressTracker()
                updateNotification()
            }

            setOnCompletionListener {
                _isPlaying.value = false
                updateMediaSessionState(SessionPlaybackState.STATE_PAUSED)
                onTrackCompletedListener?.invoke()
            }

            setOnErrorListener { _, _, _ ->
                _isBuffering.value = false
                _isPlaying.value = false
                true
            }
        }

        try {
            // Check if local file exists or is remote url
            if (track.isDownloaded && track.localFilePath != null && File(track.localFilePath).exists()) {
                mediaPlayer?.setDataSource(applicationContext, Uri.fromFile(File(track.localFilePath)))
                mediaPlayer?.prepareAsync()
            } else if (track.youtubeVideoId.isBlank()) {
                mediaPlayer?.setDataSource(track.audioUrl)
                mediaPlayer?.prepareAsync()
            } else {
                // YouTube Track: Audio is played by the YouTube Player for this exact video!
                // Release MediaPlayer so fallback demo songs do NOT play!
                mediaPlayer?.release()
                mediaPlayer = null
                _isBuffering.value = false
                _isPlaying.value = true
                val trackDur = if (track.durationMs > 0L) track.durationMs else 210000L
                _duration.value = trackDur
                updateMediaSessionState(SessionPlaybackState.STATE_PLAYING)
                startProgressTracker()
                updateNotification()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _isBuffering.value = false
        }

        startForeground(NOTIFICATION_ID, buildNotification())
    }

    fun togglePlayPause() {
        if (_isPlaying.value) {
            pausePlayback()
        } else {
            resumePlayback()
        }
    }

    fun pausePlayback() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _isPlaying.value = false
                updateMediaSessionState(SessionPlaybackState.STATE_PAUSED)
                updateNotification()
            }
        }
    }

    fun resumePlayback() {
        if (requestAudioFocus()) {
            mediaPlayer?.let {
                it.start()
                _isPlaying.value = true
                updateMediaSessionState(SessionPlaybackState.STATE_PLAYING)
                startProgressTracker()
                updateNotification()
            }
        }
    }

    fun seekTo(positionMs: Long) {
        mediaPlayer?.seekTo(positionMs.toInt())
        _currentPosition.value = positionMs
    }

    fun setPlaybackSpeed(speed: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mediaPlayer?.let {
                    it.playbackParams = it.playbackParams.setSpeed(speed)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = serviceScope.launch {
            while (isActive && _isPlaying.value) {
                if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
                    _currentPosition.value = mediaPlayer!!.currentPosition.toLong()
                } else if (_currentTrack.value?.youtubeVideoId?.isNotBlank() == true) {
                    val maxDur = _duration.value.coerceAtLeast(210000L)
                    val next = (_currentPosition.value + 500L).coerceAtMost(maxDur)
                    _currentPosition.value = next
                    if (next >= maxDur) {
                        _isPlaying.value = false
                        onTrackCompletedListener?.invoke()
                        break
                    }
                }
                delay(500)
            }
        }
    }

    private fun updateMediaSessionState(state: Int) {
        val position = mediaPlayer?.currentPosition?.toLong() ?: 0L
        val playbackState = SessionPlaybackState.Builder()
            .setActions(
                SessionPlaybackState.ACTION_PLAY or
                        SessionPlaybackState.ACTION_PAUSE or
                        SessionPlaybackState.ACTION_PLAY_PAUSE or
                        SessionPlaybackState.ACTION_SKIP_TO_NEXT or
                        SessionPlaybackState.ACTION_SKIP_TO_PREVIOUS or
                        SessionPlaybackState.ACTION_SEEK_TO
            )
            .setState(state, position, 1.0f)
            .build()
        mediaSession?.setPlaybackState(playbackState)
    }

    private fun buildNotification(): Notification {
        val track = _currentTrack.value
        val title = track?.title ?: "errortune Player"
        val artist = track?.artist ?: "Hacker Audio Terminal"

        val activityIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val contentPendingIntent = PendingIntent.getActivity(
            this,
            0,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val toggleIntent = Intent(this, MusicPlaybackService::class.java).apply { action = ACTION_TOGGLE }
        val togglePendingIntent = PendingIntent.getService(
            this,
            1,
            toggleIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val nextIntent = Intent(this, MusicPlaybackService::class.java).apply { action = ACTION_NEXT }
        val nextPendingIntent = PendingIntent.getService(
            this,
            2,
            nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val prevIntent = Intent(this, MusicPlaybackService::class.java).apply { action = ACTION_PREV }
        val prevPendingIntent = PendingIntent.getService(
            this,
            3,
            prevIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playPauseIcon = if (_isPlaying.value) {
            android.R.drawable.ic_media_pause
        } else {
            android.R.drawable.ic_media_play
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(artist)
            .setSubText(if (track?.isDownloaded == true) "OFFLINE VAULT" else "CYBER STREAM")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(contentPendingIntent)
            .addAction(android.R.drawable.ic_media_previous, "Previous", prevPendingIntent)
            .addAction(playPauseIcon, if (_isPlaying.value) "Pause" else "Play", togglePendingIntent)
            .addAction(android.R.drawable.ic_media_next, "Next", nextPendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(_isPlaying.value)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun updateNotification() {
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(NOTIFICATION_ID, buildNotification())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "errortune Audio Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Controls background audio & lockscreen music playback for errortune"
                setShowBadge(false)
            }
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    private fun requestAudioFocus(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val playbackAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(playbackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener { focusChange ->
                    when (focusChange) {
                        AudioManager.AUDIOFOCUS_LOSS,
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> pausePlayback()
                        AudioManager.AUDIOFOCUS_GAIN -> resumePlayback()
                    }
                }
                .build()
            return audioManager?.requestAudioFocus(audioFocusRequest!!) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            @Suppress("DEPRECATION")
            val result = audioManager?.requestAudioFocus(
                { focusChange ->
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) pausePlayback()
                },
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
            return result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }
    }

    private fun stopPlayback() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        progressJob?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
        mediaSession?.release()
        super.onDestroy()
    }
}
