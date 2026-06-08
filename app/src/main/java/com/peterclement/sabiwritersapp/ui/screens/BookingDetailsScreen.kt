package com.peterclement.sabiwritersapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.peterclement.sabiwritersapp.ui.navigation.Routes
import com.peterclement.sabiwritersapp.ui.viewmodel.BookingViewModel

private val BrandGreen = Color(0xFF0B3D2E)
private val BrandGold = Color(0xFFF4C430)
private val SurfaceLight = Color(0xFFF8F9FA)
private val TextPrimary = Color(0xFF1C1C1E)
private val TextSecondary = Color(0xFF6B7280)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailsScreen(
    navController: NavController,
    bookingViewModel: BookingViewModel,
    nextRoute: String
) {
    val booking = bookingViewModel.lastBooking.value

    if (booking == null) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.HOME) {
                popUpTo(0) { inclusive = true }
            }
        }
        return
    }

    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { contentVisible = true }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Booking Details",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BrandGreen
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status Banner
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(600)) + slideInVertically(
                    initialOffsetY = { -30 },
                    animationSpec = tween(600)
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BrandGreen
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(BrandGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = BrandGold,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "Booking Confirmed",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Ref: #SW-${System.currentTimeMillis().toString().takeLast(6)}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Details Card
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(600, delayMillis = 200)) + slideInVertically(
                    initialOffsetY = { 40 },
                    animationSpec = tween(600, delayMillis = 200)
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Client Information",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = BrandGreen
                            )
                        )

                        DetailRow(
                            icon = Icons.Default.Person,
                            label = "Full Name",
                            value = booking.name
                        )

                        DetailRow(
                            icon = Icons.Default.Email,
                            label = "Email Address",
                            value = booking.email
                        )

                        DetailRow(
                            icon = Icons.Default.Phone,
                            label = "Phone Number",
                            value = booking.phone
                        )

                        // FIXED: Divider instead of HorizontalDivider
                        Divider(
                            color = Color.LightGray.copy(alpha = 0.3f),
                            thickness = 1.dp
                        )

                        Text(
                            text = "Service Information",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = BrandGreen
                            )
                        )

                        DetailRow(
                            icon = Icons.Default.Book,
                            label = "Service Type",
                            value = booking.service
                        )

                        if (booking.notes.isNotBlank()) {
                            DetailRow(
                                icon = Icons.Default.Edit,
                                label = "Additional Notes",
                                value = booking.notes
                            )
                        }

                        // FIXED: Divider instead of HorizontalDivider
                        Divider(
                            color = Color.LightGray.copy(alpha = 0.3f),
                            thickness = 1.dp
                        )

                        // Amount Section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Estimated Amount",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextSecondary
                                    )
                                )
                                Text(
                                    text = "To be determined",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextSecondary
                                    )
                                )
                            }
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = BrandGold.copy(alpha = 0.15f)
                                )
                            ) {
                                Text(
                                    text = "$100.00",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = BrandGreen
                                    ),
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(600, delayMillis = 400))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { navController.navigate(nextRoute) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(
                                8.dp,
                                RoundedCornerShape(16.dp),
                                spotColor = BrandGreen.copy(alpha = 0.3f)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandGreen
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Proceed to Payment",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            navController.navigate(Routes.HOME) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = BrandGreen
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.5.dp
                        )
                    ) {
                        Text(
                            "Back to Home",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(SurfaceLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandGreen,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = TextSecondary
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}