package com.peterclement.sabiwritersapp.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.peterclement.sabiwritersapp.R
import com.peterclement.sabiwritersapp.ui.viewmodel.Booking
import com.peterclement.sabiwritersapp.ui.viewmodel.BookingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Brand Color Palette
private val BrandGreen = Color(0xFF0B3D2E)
private val BrandGold = Color(0xFFF4C430)
private val BrandGoldLight = Color(0xFFFFF8E1)
private val BrandGreenLight = Color(0xFFE8F5E9)
private val SurfaceLight = Color(0xFFF8F9FA)
private val TextPrimary = Color(0xFF1C1C1E)
private val TextSecondary = Color(0xFF6B7280)
private val ErrorRed = Color(0xFFDC2626)
private val SuccessGreen = Color(0xFF059669)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFormScreen(
    navController: NavController,
    nextRoute: String,
    bookingViewModel: BookingViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var service by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { contentVisible = true }

    val snackbarHostState = remember { SnackbarHostState() }

    // Service options
    val services = listOf(
        "Academic Writing",
        "Business Writing",
        "Creative Writing",
        "Editing & Proofreading",
        "Resume/CV Writing",
        "Content Writing",
        "Research Paper",
        "Thesis/Dissertation"
    )
    var showServiceDropdown by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Book a Service",
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
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Hero Section
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(600)) + slideInVertically(
                    initialOffsetY = { -30 },
                    animationSpec = tween(600, delayMillis = 100)
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = BrandGreenLight
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .shadow(8.dp, CircleShape)
                                .clip(CircleShape)
                                .background(Color.White)
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.sabiwriterslogo),
                                contentDescription = "Sabi Writers Logo",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Sabi Writers",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BrandGreen
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Professional writing services tailored to your needs. Fill out the form below and our team will reach out within 24 hours.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextSecondary,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // About & Vision Cards
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(600, delayMillis = 200)) + slideInVertically(
                    initialOffsetY = { 40 },
                    animationSpec = tween(600, delayMillis = 200)
                )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    InfoCard(
                        title = "About Sabi Writers",
                        description = "A professional writing and consulting platform delivering high-quality academic, business, and creative writing services tailored to meet client needs.",
                        icon = Icons.Default.Book,
                        containerColor = Color.White
                    )

                    InfoCard(
                        title = "Our Vision & Mission",
                        description = "Vision: To become a trusted global writing brand known for excellence, integrity, and innovation.\n\nMission: To empower individuals and businesses with professionally crafted content that delivers clarity, credibility, and impact.",
                        icon = Icons.Default.Check,
                        containerColor = BrandGoldLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Booking Form
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(tween(600, delayMillis = 400)) + slideInVertically(
                    initialOffsetY = { 50 },
                    animationSpec = tween(600, delayMillis = 400)
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Booking Details",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = BrandGreen
                            )
                        )

                        // Name
                        ModernFormField(
                            value = name,
                            onValueChange = { name = it; if (error.isNotEmpty()) error = "" },
                            label = "Full Name",
                            leadingIcon = Icons.Default.Person,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
                        )

                        // Email
                        ModernFormField(
                            value = email,
                            onValueChange = { email = it; if (error.isNotEmpty()) error = "" },
                            label = "Email Address",
                            leadingIcon = Icons.Default.Email,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                            onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
                        )

                        // Phone
                        ModernFormField(
                            value = phone,
                            onValueChange = { phone = it; if (error.isNotEmpty()) error = "" },
                            label = "Phone Number",
                            leadingIcon = Icons.Default.Phone,
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next,
                            onImeAction = { focusManager.moveFocus(FocusDirection.Down) }
                        )

                        // Service Type Dropdown
                        ExposedDropdownMenuBox(
                            expanded = showServiceDropdown,
                            onExpandedChange = { showServiceDropdown = !showServiceDropdown }
                        ) {
                            OutlinedTextField(
                                value = service,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Service Type") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Book,
                                        contentDescription = null,
                                        tint = BrandGreen
                                    )
                                },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = showServiceDropdown)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BrandGreen,
                                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                                    focusedContainerColor = SurfaceLight,
                                    unfocusedContainerColor = SurfaceLight
                                ),
                                singleLine = true
                            )

                            ExposedDropdownMenu(
                                expanded = showServiceDropdown,
                                onDismissRequest = { showServiceDropdown = false },
                                modifier = Modifier.exposedDropdownSize()
                            ) {
                                services.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            service = item
                                            showServiceDropdown = false
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = if (service == item) BrandGreen else Color.Transparent
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        // Notes
                        OutlinedTextField(
                            value = notes,
                            onValueChange = { notes = it },
                            label = { Text("Additional Notes (Optional)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandGreen,
                                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                                focusedContainerColor = SurfaceLight,
                                unfocusedContainerColor = SurfaceLight
                            ),
                            minLines = 3,
                            maxLines = 5,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = BrandGreen
                                )
                            }
                        )

                        // Error
                        AnimatedVisibility(
                            visible = error.isNotEmpty(),
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = ErrorRed.copy(alpha = 0.1f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = error,
                                    color = ErrorRed,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Submit Button
                        Button(
                            onClick = {
                                if (validateBookingForm(name, email, phone, service, onError = { error = it })) {
                                    loading = true
                                    val booking = Booking(name, email, phone, service, notes)
                                    bookingViewModel.addBooking(
                                        booking,
                                        onSuccess = {
                                            loading = false
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Booking submitted successfully!")
                                                delay(1200)
                                                navController.navigate(nextRoute) {
                                                    popUpTo("booking") { inclusive = true }
                                                }
                                            }
                                        },
                                        onError = { err ->
                                            loading = false
                                            error = err
                                        }
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .shadow(8.dp, RoundedCornerShape(16.dp), spotColor = BrandGreen.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(16.dp),
                            enabled = !loading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrandGreen,
                                disabledContainerColor = BrandGreen.copy(alpha = 0.4f)
                            ),
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp
                            )
                        ) {
                            if (loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.5.dp,
                                    color = Color.White
                                )
                            } else {
                                Text(
                                    "Submit Booking",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    description: String,
    icon: ImageVector,
    containerColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(BrandGold.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = BrandGreen,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = BrandGreen
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun ModernFormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onImeAction: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = BrandGreen,
            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
            focusedContainerColor = SurfaceLight,
            unfocusedContainerColor = SurfaceLight,
            focusedLeadingIconColor = BrandGreen,
            unfocusedLeadingIconColor = TextSecondary
        ),
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { onImeAction() },
            onDone = { onImeAction() }
        ),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = TextPrimary)
    )
}

private fun validateBookingForm(
    name: String,
    email: String,
    phone: String,
    service: String,
    onError: (String) -> Unit
): Boolean {
    return when {
        name.isBlank() -> { onError("Please enter your full name"); false }
        email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
            onError("Please enter a valid email address"); false
        }
        phone.isBlank() -> { onError("Please enter your phone number"); false }
        service.isBlank() -> { onError("Please select a service type"); false }
        else -> true
    }
}