@file:OptIn(androidx.media3.common.util.UnstableApi::class)

package com.peterclement.sabiwritersapp.ui.screens

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.peterclement.sabiwritersapp.ui.navigation.Routes
import kotlinx.coroutines.delay

// Brand Colors
private val BrandGreen = Color(0xFF0B3D2E)
private val BrandGold = Color(0xFFF4C430)

@Composable
fun SplashVideoScreen(navController: NavController) {
    val context = LocalContext.current

    // Animation states
    var logoVisible by remember { mutableStateOf(false) }
    var textVisible by remember { mutableStateOf(false) }
    var buttonVisible by remember { mutableStateOf(false) }

    // Logo bounce animation
    var bounceCount by remember { mutableIntStateOf(0) }

    val logoScale by animateFloatAsState(
        targetValue = when {
            bounceCount % 2 == 0 -> 1.0f
            else -> 1.25f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_bounce"
    )

    // ExoPlayer for background video
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = Uri.parse("android.resource://${context.packageName}/raw/splash_bg")
            setMediaItem(MediaItem.fromUri(uri))
            repeatMode = Player.REPEAT_MODE_ALL
            volume = 0f
            playWhenReady = true
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    // Launch animations sequence
    LaunchedEffect(Unit) {
        delay(500)
        logoVisible = true

        repeat(3) {
            bounceCount++
            delay(600)
            bounceCount++
            delay(300)
        }

        delay(200)
        textVisible = true
        delay(400)
        buttonVisible = true
    }

    // Auto-navigate after 5 seconds
    LaunchedEffect(Unit) {
        delay(5000)
        if (navController.currentDestination?.route == Routes.SPLASH) {
            navigateToLogin(navController)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Video
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            }
        )

        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.2f),
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.8f),
                            BrandGreen.copy(alpha = 0.9f)
                        )
                    )
                )
        )

        // Content Overlay
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Animated Logo
            AnimatedVisibility(
                visible = logoVisible,
                enter = fadeIn(tween(800)) + scaleIn(
                    initialScale = 0.5f,
                    animationSpec = tween(800)
                )
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .shadow(
                            elevation = 20.dp,
                            shape = CircleShape,
                            spotColor = BrandGold.copy(alpha = 0.3f)
                        )
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.95f))
                        .padding(20.dp)
                        .scale(logoScale),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = "Sabi Writers Logo",
                        tint = BrandGreen,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Brand Name
            AnimatedVisibility(
                visible = textVisible,
                enter = fadeIn(tween(600)) + slideInVertically(
                    initialOffsetY = { 30 },
                    animationSpec = tween(600)
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Sabi Writers",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = BrandGold,
                            letterSpacing = 1.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Professional Writing Services",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            letterSpacing = 0.5.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Excellence | Integrity | Innovation",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = BrandGold.copy(alpha = 0.8f)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Get Started Button
            AnimatedVisibility(
                visible = buttonVisible,
                enter = fadeIn(tween(600)) + slideInVertically(
                    initialOffsetY = { 50 },
                    animationSpec = tween(600)
                )
            ) {
                Button(
                    onClick = { navigateToLogin(navController) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = BrandGold.copy(alpha = 0.3f)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandGold
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Text(
                        text = "Get Started",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = BrandGreen,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Skip text
            AnimatedVisibility(
                visible = buttonVisible,
                enter = fadeIn(tween(800, delayMillis = 200))
            ) {
                TextButton(
                    onClick = { navigateToLogin(navController) }
                ) {
                    Text(
                        text = "Skip",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.6f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

private fun navigateToLogin(navController: NavController) {
    navController.navigate(Routes.LOGIN) {
        popUpTo(Routes.SPLASH) { inclusive = true }
    }
}