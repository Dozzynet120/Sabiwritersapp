package com.peterclement.sabiwritersapp.ui.screens

import android.content.res.Resources
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import com.peterclement.sabiwritersapp.R
import com.peterclement.sabiwritersapp.ui.navigation.Routes
import kotlinx.coroutines.delay

// Brand Colors
private val BrandGreen = Color(0xFF0B3D2E)
private val BrandGold = Color(0xFFF4C430)

@Composable
fun LogoScreen(navController: NavController) {
    val context = LocalContext.current

    // Track bounce count (0, 1, 2 = three bounces)
    var bounceCount by remember { mutableIntStateOf(0) }

    // Animation state
    val scale by animateFloatAsState(
        targetValue = when {
            bounceCount % 2 == 0 -> 1.0f
            else -> 1.3f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "bounce_scale"
    )

    // Load logo painter safely
    val logoPainter = remember(context) {
        loadLogoPainter(context.resources, R.drawable.sabiwriterslogo)
    }

    // Trigger bounces with delays
    LaunchedEffect(Unit) {
        delay(300)

        repeat(3) {
            bounceCount++
            delay(600)
            bounceCount++
            delay(400)
        }

        delay(500)
        navController.navigate(Routes.SPLASH) {
            popUpTo(Routes.LOGO) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BrandGreen,
                        BrandGreen.copy(alpha = 0.9f),
                        BrandGreen.copy(alpha = 0.95f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Container with white background
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = CircleShape,
                        spotColor = BrandGold.copy(alpha = 0.3f)
                    )
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.95f))
                    .padding(24.dp)
                    .scale(scale),
                contentAlignment = Alignment.Center
            ) {
                // Use loaded painter or fallback
                if (logoPainter != null) {
                    Image(
                        painter = logoPainter,
                        contentDescription = "Sabi Writers Logo",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Fallback icon
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = "Sabi Writers Logo",
                        tint = BrandGreen,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Brand name with fade-in
            AnimatedVisibility(
                visible = bounceCount >= 4,
                enter = fadeIn(animationSpec = tween(800))
            ) {
                Text(
                    text = "Sabi Writers",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = BrandGold,
                        letterSpacing = 1.sp
                    )
                )
            }
        }
    }
}

/**
 * Safely loads a logo painter from resources.
 * Handles WebP and other formats properly.
 */
private fun loadLogoPainter(resources: Resources, resId: Int): Painter? {
    return try {
        val drawable = resources.getDrawable(resId, null)
        val bitmap = drawable.toBitmap()
        BitmapPainter(bitmap.asImageBitmap())
    } catch (e: Resources.NotFoundException) {
        null
    } catch (e: Exception) {
        null
    }
}