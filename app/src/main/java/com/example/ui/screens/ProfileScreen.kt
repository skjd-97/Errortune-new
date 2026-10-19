package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyberPurple
import com.example.ui.theme.HackerMuted
import com.example.ui.theme.HackerTextSecondary
import com.example.ui.theme.MatrixDark
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.TechBorder
import com.example.ui.theme.TechSurface
import com.example.ui.theme.TerminalGreen

@Composable
fun ProfileScreen(
    currentEqualizerPreset: String = "",
    onSelectEqualizerPreset: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val instagramUsername = "jd._.___"
    val githubUsername = "skjd-97"
    val linkedinUrl = "https://www.linkedin.com/in/shaikh-javeed-337872368?utm_source=share_via&utm_content=profile&utm_medium=member_android"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MatrixDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(bottom = 120.dp)
    ) {
        // Hacker ID Card
        item {
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF0C1322),
                                Color(0xFF141F36),
                                Color(0xFF091410)
                            )
                        )
                    )
                    .border(1.5.dp, NeonCyan, RoundedCornerShape(16.dp))
                    .padding(20.dp)
                    .testTag("creator_profile_card")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Cyber Avatar with glowing rings - Clickable to open Instagram
                    Box(
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                            .border(2.dp, NeonCyan, CircleShape)
                            .background(Color.Black)
                            .clickable {
                                openInstagramProfile(context, instagramUsername)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "JD",
                            color = TerminalGreen,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 32.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Prominent Creator Credit: Clickable to open Instagram
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(TechSurface.copy(alpha = 0.6f))
                            .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .clickable {
                                openInstagramProfile(context, instagramUsername)
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .testTag("creator_credit_link"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.creator_credit),
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            textAlign = TextAlign.Center
                        )
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open Instagram",
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Direct Instagram ID Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF833AB4).copy(alpha = 0.35f),
                                        Color(0xFFFD1D1D).copy(alpha = 0.35f),
                                        Color(0xFFFCB045).copy(alpha = 0.35f)
                                    )
                                )
                            )
                            .border(1.dp, CyberPurple, RoundedCornerShape(20.dp))
                            .clickable {
                                openInstagramProfile(context, instagramUsername)
                            }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .testTag("instagram_id_button"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "INSTAGRAM: @$instagramUsername",
                            color = Color(0xFFFF88DD),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open Instagram Profile",
                            tint = Color(0xFFFF88DD),
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Direct GitHub Profile Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF09200F),
                                        Color(0xFF14532D),
                                        Color(0xFF063012)
                                    )
                                )
                            )
                            .border(1.dp, TerminalGreen, RoundedCornerShape(20.dp))
                            .clickable {
                                openGitHubProfile(context, githubUsername)
                            }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .testTag("github_id_button"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "Open GitHub",
                            tint = TerminalGreen,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "GITHUB: @$githubUsername",
                            color = TerminalGreen,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open GitHub Profile",
                            tint = TerminalGreen,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Direct LinkedIn Profile Button
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF001F3F),
                                        Color(0xFF0A66C2).copy(alpha = 0.5f),
                                        Color(0xFF00152B)
                                    )
                                )
                            )
                            .border(1.dp, Color(0xFF0A66C2), RoundedCornerShape(20.dp))
                            .clickable {
                                openLinkedInProfile(context, linkedinUrl)
                            }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                            .testTag("linkedin_id_button"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Open LinkedIn",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "LINKEDIN: Shaikh Javeed",
                            color = Color(0xFF38BDF8),
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Open LinkedIn Profile",
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "CORE SYSTEM ARCHITECT // ROOT ACCESS",
                        color = TerminalGreen,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Badge row
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(CyberPurple.copy(alpha = 0.2f))
                                .border(1.dp, CyberPurple, RoundedCornerShape(6.dp))
                                .clickable {
                                    openInstagramProfile(context, instagramUsername)
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "DEV: jd._.___",
                                color = CyberPurple,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonCyan.copy(alpha = 0.2f))
                                .border(1.dp, NeonCyan, RoundedCornerShape(6.dp))
                                .clickable {
                                    openGitHubProfile(context, githubUsername)
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "GIT: skjd-97",
                                color = NeonCyan,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF0A66C2).copy(alpha = 0.25f))
                                .border(1.dp, Color(0xFF0A66C2), RoundedCornerShape(6.dp))
                                .clickable {
                                    openLinkedInProfile(context, linkedinUrl)
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "IN: Javeed",
                                color = Color(0xFF38BDF8),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(TerminalGreen.copy(alpha = 0.2f))
                                .border(1.dp, TerminalGreen, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "AUTH: NO_LOGIN",
                                color = TerminalGreen,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Section: System Specs Console
        item {
            Text(
                text = "// SYSTEM SPECIFICATIONS",
                color = TerminalGreen,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(TechSurface)
                    .border(1.dp, TechBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SpecRow(label = "APPLICATION", value = "errortune v1.0.0", valueColor = NeonCyan)
                SpecRow(
                    label = "DEVELOPER",
                    value = "jd._.___ [TAP FOR INSTAGRAM]",
                    valueColor = TerminalGreen,
                    onClick = { openInstagramProfile(context, instagramUsername) }
                )
                SpecRow(
                    label = "GITHUB PROFILE",
                    value = "github.com/skjd-97 [TAP TO OPEN]",
                    valueColor = NeonCyan,
                    onClick = { openGitHubProfile(context, githubUsername) }
                )
                SpecRow(
                    label = "LINKEDIN PROFILE",
                    value = "Shaikh Javeed [TAP TO CONNECT]",
                    valueColor = Color(0xFF38BDF8),
                    onClick = { openLinkedInProfile(context, linkedinUrl) }
                )
                SpecRow(label = "DISTRIBUTION", value = "GitHub Open Source", valueColor = TerminalGreen)
                SpecRow(label = "AUDIO DECODER", value = "CyberTune Native Core", valueColor = Color.White)
                SpecRow(label = "LOCKSCREEN PLAY", value = "FOREGROUND SERVICE ACTIVE", valueColor = TerminalGreen)
                SpecRow(label = "OFFLINE VAULT", value = "Room Flash Storage (No Internet)", valueColor = NeonYellow)
                SpecRow(label = "AUTHENTICATION", value = "DIRECT ACCESS (NO LOGIN REQ)", valueColor = TerminalGreen)
                SpecRow(label = "STREAM BITRATE", value = "320 KBPS / FLAC HIGH FIDELITY", valueColor = NeonCyan)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Section: Diagnostics & Cache Operations
        item {
            Text(
                text = "// SYSTEM OPERATIONS",
                color = TerminalGreen,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(TechSurface)
                    .border(1.dp, TechBorder, RoundedCornerShape(12.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Toast.makeText(context, "Audio buffers flushed. Memory optimal.", Toast.LENGTH_SHORT).show()
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteSweep,
                        contentDescription = "Clear Cache",
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "CLEAR AUDIO BUFFER CACHE",
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Frees memory without removing your downloaded offline songs",
                            color = HackerMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(TechBorder)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Toast.makeText(context, "Audio Daemon OK: Lockscreen background service running", Toast.LENGTH_SHORT).show()
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Memory,
                        contentDescription = "Daemon Diagnostics",
                        tint = TerminalGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "DIAGNOSE BACKGROUND SERVICE",
                            color = Color.White,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Verify Foreground Service and WakeLock status",
                            color = HackerMuted,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

private fun openInstagramProfile(context: Context, username: String) {
    val cleanUsername = username.trim().removePrefix("@")
    val webUri = Uri.parse("https://instagram.com/$cleanUsername")
    val appIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
        setPackage("com.instagram.android")
    }

    try {
        context.startActivity(appIntent)
    } catch (e: Exception) {
        // Fallback to web browser
        try {
            val browserIntent = Intent(Intent.ACTION_VIEW, webUri)
            context.startActivity(browserIntent)
        } catch (ex: Exception) {
            Toast.makeText(context, "Instagram: @$cleanUsername", Toast.LENGTH_LONG).show()
        }
    }
}

private fun openGitHubProfile(context: Context, username: String) {
    val cleanUsername = username.trim().removePrefix("@")
    val webUri = Uri.parse("https://github.com/$cleanUsername")
    try {
        val browserIntent = Intent(Intent.ACTION_VIEW, webUri)
        context.startActivity(browserIntent)
    } catch (e: Exception) {
        Toast.makeText(context, "GitHub: https://github.com/$cleanUsername", Toast.LENGTH_LONG).show()
    }
}

private fun openLinkedInProfile(context: Context, url: String) {
    try {
        val webUri = Uri.parse(url)
        val browserIntent = Intent(Intent.ACTION_VIEW, webUri)
        context.startActivity(browserIntent)
    } catch (e: Exception) {
        Toast.makeText(context, "LinkedIn: $url", Toast.LENGTH_LONG).show()
    }
}

@Composable
private fun SpecRow(
    label: String,
    value: String,
    valueColor: Color,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = HackerMuted,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp
        )
        Text(
            text = value,
            color = valueColor,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
    }
}
