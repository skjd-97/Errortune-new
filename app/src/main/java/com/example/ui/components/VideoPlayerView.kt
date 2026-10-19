package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TechBorder
import com.example.ui.theme.TechSurface
import com.example.ui.theme.TerminalGreen

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun VideoPlayerView(
    videoId: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cleanVideoId = if (videoId.isNotBlank()) videoId else "jfKfPfyJRdk"

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp))
                .border(1.5.dp, NeonCyan.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                .background(Color.Black)
        ) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            databaseEnabled = true
                            mediaPlaybackRequiresUserGesture = false
                            loadWithOverviewMode = true
                            useWideViewPort = true
                            allowFileAccess = true
                            allowContentAccess = true
                            cacheMode = WebSettings.LOAD_DEFAULT
                            userAgentString = "Mozilla/5.0 (Linux; Android 13; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
                        }
                        webChromeClient = WebChromeClient()
                        webViewClient = WebViewClient()

                        tag = cleanVideoId
                        loadDataWithBaseURL(
                            "https://www.youtube.com",
                            buildYouTubeHtml(cleanVideoId),
                            "text/html",
                            "UTF-8",
                            null
                        )
                    }
                },
                update = { webView ->
                    val currentTag = webView.tag as? String
                    if (currentTag != cleanVideoId) {
                        webView.tag = cleanVideoId
                        webView.loadDataWithBaseURL(
                            "https://www.youtube.com",
                            buildYouTubeHtml(cleanVideoId),
                            "text/html",
                            "UTF-8",
                            null
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Direct YouTube App / Web launcher fallback
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(TechSurface)
                .border(1.dp, TechBorder, RoundedCornerShape(8.dp))
                .clickable {
                    openInYouTubeApp(context, cleanVideoId)
                }
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .testTag("open_youtube_app_btn"),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "YouTube",
                    tint = Color(0xFFFF0000),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "PLAY IN YOUTUBE APP",
                    color = Color.White,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "FULL QUALITY",
                    color = TerminalGreen,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp
                )
                Icon(
                    imageVector = Icons.Default.OpenInNew,
                    contentDescription = "Open",
                    tint = NeonCyan,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

private fun buildYouTubeHtml(videoId: String): String {
    return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
            <style>
                * { margin: 0; padding: 0; box-sizing: border-box; background: #000; }
                html, body { width: 100%; height: 100%; overflow: hidden; background: #000; }
                iframe { width: 100%; height: 100%; border: none; }
            </style>
        </head>
        <body>
            <iframe 
                id="player"
                src="https://www.youtube.com/embed/$videoId?autoplay=1&playsinline=1&enablejsapi=1&origin=https://www.youtube.com&rel=0&modestbranding=1" 
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" 
                allowfullscreen>
            </iframe>
        </body>
        </html>
    """.trimIndent()
}

private fun openInYouTubeApp(context: Context, videoId: String) {
    try {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
        context.startActivity(appIntent)
    } catch (_: Exception) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
        context.startActivity(webIntent)
    }
}
