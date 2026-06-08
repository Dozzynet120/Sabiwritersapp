package com.peterclement.sabiwritersapp.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.peterclement.sabiwritersapp.R
import com.peterclement.sabiwritersapp.ui.navigation.Routes
import com.peterclement.sabiwritersapp.ui.viewmodel.BookingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Brand Colors
private val BrandGreen = Color(0xFF0B3D2E)
private val BrandGold = Color(0xFFF4C430)
private val SuccessGreen = Color(0xFF059669)
private val ErrorRed = Color(0xFFDC2626)
private val TextPrimary = Color(0xFF1C1C1E)
private val TextSecondary = Color(0xFF6B7280)
private val SurfaceLight = Color(0xFFF8F9FA)

enum class PaymentState {
    IDLE, PROCESSING, SUCCESS, FAILED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    bookingViewModel: BookingViewModel,
    amount: Double = 100.00,
    onPaymentComplete: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var paymentState by remember { mutableStateOf(PaymentState.IDLE) }
    var errorMessage by remember { mutableStateOf("") }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { contentVisible = true }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Secure Payment",
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
            when (paymentState) {
                PaymentState.IDLE -> PaymentIdleContent(
                    contentVisible = contentVisible,
                    amount = amount,
                    bookingViewModel = bookingViewModel,
                    onPayClick = {
                        paymentState = PaymentState.PROCESSING
                        coroutineScope.launch {
                            delay(2000)
                            paymentState = PaymentState.SUCCESS
                        }
                    }
                )

                PaymentState.PROCESSING -> PaymentProcessingContent()

                PaymentState.SUCCESS -> PaymentSuccessContent(
                    amount = amount,
                    onContinue = {
                        onPaymentComplete(true)
                        navController.navigate(Routes.HOME) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )

                PaymentState.FAILED -> PaymentFailedContent(
                    errorMessage = errorMessage,
                    onRetry = { paymentState = PaymentState.IDLE }
                )
            }
        }
    }
}

@Composable
private fun PaymentIdleContent(
    contentVisible: Boolean,
    amount: Double,
    bookingViewModel: BookingViewModel,
    onPayClick: () -> Unit
) {
    val booking = bookingViewModel.lastBooking.value

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = BrandGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "SSL Secure Payment",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                        Text(
                            text = "Your payment is encrypted and secure",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

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
                        text = "Order Summary",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = BrandGreen
                        )
                    )

                    if (booking != null) {
                        SummaryRow(
                            icon = Icons.Default.Person,
                            label = "Client",
                            value = booking.name
                        )
                        SummaryRow(
                            icon = Icons.Default.Book,
                            label = "Service",
                            value = booking.service
                        )
                        SummaryRow(
                            icon = Icons.Default.Email,
                            label = "Email",
                            value = booking.email
                        )
                    }

                    Divider(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        thickness = 1.dp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Amount",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = BrandGold.copy(alpha = 0.15f)
                            )
                        ) {
                            Text(
                                text = "₦${String.format("%,.2f", amount)}",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BrandGreen
                                ),
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(tween(600, delayMillis = 400))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Payment Methods",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )

                PaymentMethodCard(
                    icon = Icons.Default.CreditCard,
                    title = "Card Payment",
                    subtitle = "Visa, Mastercard, Verve",
                    isSelected = true
                )

                PaymentMethodCard(
                    icon = Icons.Default.PhoneAndroid,
                    title = "Bank Transfer",
                    subtitle = "USSD, Mobile Banking",
                    isSelected = false
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(tween(600, delayMillis = 600))
        ) {
            Button(
                onClick = onPayClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
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
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Pay ₦${String.format("%,.2f", amount)}",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(tween(600, delayMillis = 700))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TrustBadge(icon = Icons.Default.Verified, text = "Verified")
                TrustBadge(icon = Icons.Default.Shield, text = "Secure")
                TrustBadge(icon = Icons.Default.Support, text = "24/7 Support")
            }
        }
    }
}

@Composable
private fun PaymentProcessingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                strokeWidth = 4.dp,
                color = BrandGreen
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Processing Payment...",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = BrandGreen
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Please do not close this screen",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary
                )
            )
        }
    }
}

@Composable
private fun PaymentSuccessContent(
    amount: Double,
    onContinue: () -> Unit
) {
    var checkmarkScale by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        delay(200)
        checkmarkScale = 1f
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = SuccessGreen,
                modifier = Modifier
                    .size(72.dp)
                    .scale(scale)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Payment Successful!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = BrandGreen
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Your payment of ₦${String.format("%,.2f", amount)} has been received.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = TextSecondary,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = BrandGold.copy(alpha = 0.15f)
            )
        ) {
            Text(
                text = "Ref: #SW-PAY-${System.currentTimeMillis().toString().takeLast(8)}",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = BrandGreen,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandGreen
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp
            )
        ) {
            Text(
                "Continue to Home",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}

@Composable
private fun PaymentFailedContent(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .shadow(12.dp, CircleShape)
                .clip(CircleShape)
                .background(ErrorRed.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Failed",
                tint = ErrorRed,
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Payment Failed",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = ErrorRed
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = errorMessage.ifEmpty { "Something went wrong. Please try again." },
            style = MaterialTheme.typography.bodyLarge.copy(
                color = TextSecondary,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRetry,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
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
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Try Again",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}

@Composable
private fun SummaryRow(
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
                .size(36.dp)
                .clip(CircleShape)
                .background(SurfaceLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandGreen,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
private fun PaymentMethodCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    isSelected: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) BrandGreen.copy(alpha = 0.05f) else Color.White
        ),
        border = if (isSelected) {
            BorderStroke(
                width = 2.dp,
                color = BrandGreen
            )
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) BrandGreen.copy(alpha = 0.1f)
                        else SurfaceLight
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) BrandGreen else TextSecondary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary
                    )
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = BrandGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun TrustBadge(
    icon: ImageVector,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = BrandGreen.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary
            )
        )
    }
}