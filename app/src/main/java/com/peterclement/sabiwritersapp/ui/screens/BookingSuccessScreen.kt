package com.peterclement.sabiwritersapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.peterclement.sabiwritersapp.R
import com.peterclement.sabiwritersapp.ui.viewmodel.BookingViewModel

private val BrandGreen = Color(0xFF0B3D2E)
private val BrandGold = Color(0xFFF4C430)
private val SuccessGreen = Color(0xFF059669)

@Composable
fun BookingSuccessScreen(
    navController: NavController,
    bookingViewModel: BookingViewModel,
    nextRoute: String
) {
    var contentVisible by remember { mutableStateOf(false) }
    var checkmarkScale by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        contentVisible = true
        kotlinx.coroutines.delay(200)
        checkmarkScale = 1f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BrandGreen,
                        Color(0xFF1B5E20),
                        Color(0xFF2E7D32)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(tween(800)) + slideInVertically(
                initialOffsetY = { 60 },
                animationSpec = tween(800, delayMillis = 100)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Success Checkmark with animation
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .shadow(16.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    val scale by animateFloatAsState(
                        targetValue = checkmarkScale,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "checkmark_scale"
                    )

                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = SuccessGreen,
                        modifier = Modifier
                            .size(64.dp)
                            .scale(scale)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Logo
                Image(
                    painter = painterResource(id = R.drawable.sabiwriterslogo),
                    contentDescription = "Sabi Writers Logo",
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Booking Submitted!",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Thank you for choosing Sabi Writers. Our team has received your request and will get back to you within 24 hours.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BrandGold.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "🎉 Expect a confirmation email shortly",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = BrandGold,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { navController.navigate(nextRoute) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = BrandGreen
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Text(
                        "View Booking Details",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = {
                        navController.popBackStack(
                            navController.graph.startDestinationId,
                            inclusive = false
                        )
                    }
                ) {
                    Text(
                        "Back to Home",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}