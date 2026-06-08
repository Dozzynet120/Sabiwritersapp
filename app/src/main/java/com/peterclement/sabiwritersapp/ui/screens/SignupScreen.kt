package com.peterclement.sabiwritersapp.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.peterclement.sabiwritersapp.R
import com.peterclement.sabiwritersapp.ui.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Same color palette as LoginScreen for consistency
private val PrimaryDark = Color(0xFF1A237E)
private val PrimaryLight = Color(0xFF3949AB)
private val AccentGold = Color(0xFFFFB300)
private val SurfaceLight = Color(0xFFF8F9FA)
private val TextPrimary = Color(0xFF1C1C1E)
private val TextSecondary = Color(0xFF6B7280)
private val ErrorRed = Color(0xFFDC2626)
private val SuccessGreen = Color(0xFF059669)

@Composable
fun SignupScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Gradient background (no video to keep it lighter)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            PrimaryDark,
                            PrimaryLight,
                            Color(0xFF5C6BC0)
                        )
                    )
                )
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            SignupContent(navController)
        }
    }
}

@Composable
private fun SignupContent(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var success by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    // Validation states
    val passwordStrength = calculatePasswordStrength(password)
    val passwordsMatch = password == confirmPassword && confirmPassword.isNotEmpty()

    // Animation states
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        contentVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // Header Section with animation
        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(animationSpec = tween(600)) +
                    slideInVertically(
                        initialOffsetY = { -40 },
                        animationSpec = tween(600, delayMillis = 100)
                    )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo container
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = CircleShape,
                            spotColor = AccentGold.copy(alpha = 0.3f)
                        )
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.95f))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sabiwriterslogo),
                        contentDescription = "SabiWriters Logo",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Join our community of writers",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Signup Card
        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(animationSpec = tween(600, delayMillis = 300)) +
                    slideInVertically(
                        initialOffsetY = { 60 },
                        animationSpec = tween(600, delayMillis = 300)
                    )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(24.dp),
                        spotColor = Color.Black.copy(alpha = 0.2f)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.97f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Full Name Field
                    ModernTextFieldSignup(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            if (error.isNotEmpty()) error = ""
                        },
                        label = "Full Name",
                        leadingIcon = Icons.Default.Person,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next,
                        onImeAction = {
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                        isError = error.isNotEmpty() && fullName.isBlank()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Email Field
                    ModernTextFieldSignup(
                        value = email,
                        onValueChange = {
                            email = it
                            if (error.isNotEmpty()) error = ""
                        },
                        label = "Email Address",
                        leadingIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                        onImeAction = {
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                        isError = error.isNotEmpty() && email.isBlank()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Password Field
                    ModernTextFieldSignup(
                        value = password,
                        onValueChange = {
                            password = it
                            if (error.isNotEmpty()) error = ""
                        },
                        label = "Password",
                        leadingIcon = Icons.Default.Lock,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next,
                        onImeAction = {
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                        isError = error.isNotEmpty() && password.isBlank(),
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onPasswordVisibilityChange = { passwordVisible = !passwordVisible }
                    )

                    // Password Strength Indicator
                    if (password.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        PasswordStrengthIndicator(strength = passwordStrength)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Confirm Password Field
                    ModernTextFieldSignup(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            if (error.isNotEmpty()) error = ""
                        },
                        label = "Confirm Password",
                        leadingIcon = Icons.Default.Lock,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        onImeAction = {
                            focusManager.clearFocus()
                            if (validateInputs(fullName, email, password, confirmPassword, onErrorChange = { error = it })) {
                                performSignup(
                                    auth, fullName, email, password,
                                    navController, context,
                                    coroutineScope = coroutineScope,
                                    onLoadingChange = { loading = it },
                                    onErrorChange = { error = it },
                                    onSuccessChange = { success = it }
                                )
                            }
                        },
                        isError = error.isNotEmpty() && confirmPassword.isBlank(),
                        isPassword = true,
                        passwordVisible = confirmPasswordVisible,
                        onPasswordVisibilityChange = { confirmPasswordVisible = !confirmPasswordVisible }
                    )

                    // Password Match Indicator
                    if (confirmPassword.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (passwordsMatch) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = null,
                                tint = if (passwordsMatch) SuccessGreen else ErrorRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (passwordsMatch) "Passwords match" else "Passwords do not match",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (passwordsMatch) SuccessGreen else ErrorRed
                                )
                            )
                        }
                    }

                    // Error Message
                    AnimatedVisibility(
                        visible = error.isNotEmpty(),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = ErrorRed.copy(alpha = 0.1f)
                            )
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

                    // Success Message
                    AnimatedVisibility(
                        visible = success.isNotEmpty(),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = SuccessGreen.copy(alpha = 0.1f)
                            )
                        ) {
                            Text(
                                text = success,
                                color = SuccessGreen,
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sign Up Button
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp),
                                spotColor = PrimaryLight.copy(alpha = 0.4f)
                            ),
                        enabled = !loading && fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank(),
                        onClick = {
                            if (validateInputs(fullName, email, password, confirmPassword, onErrorChange = { error = it })) {
                                performSignup(
                                    auth, fullName, email, password,
                                    navController, context,
                                    coroutineScope = coroutineScope,
                                    onLoadingChange = { loading = it },
                                    onErrorChange = { error = it },
                                    onSuccessChange = { success = it }
                                )
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryDark,
                            disabledContainerColor = PrimaryDark.copy(alpha = 0.4f)
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
                                text = "Create Account",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Terms Text
                    Text(
                        text = "By signing up, you agree to our Terms of Service and Privacy Policy",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Login Link
        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(animationSpec = tween(600, delayMillis = 500))
        ) {
            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
                Text(
                    text = "Sign In",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = AccentGold,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
private fun ModernTextFieldSignup(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    onImeAction: () -> Unit,
    isError: Boolean = false,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityChange: () -> Unit = {}
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isError) ErrorRed else TextSecondary
                )
            )
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryLight,
            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
            focusedContainerColor = SurfaceLight,
            unfocusedContainerColor = SurfaceLight,
            errorBorderColor = ErrorRed,
            focusedLeadingIconColor = PrimaryLight,
            unfocusedLeadingIconColor = TextSecondary
        ),
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                modifier = Modifier.size(22.dp)
            )
        },
        trailingIcon = {
            if (isPassword) {
                IconButton(
                    onClick = onPasswordVisibilityChange,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        },
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { onImeAction() },
            onDone = { onImeAction() }
        ),
        isError = isError,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = TextPrimary
        )
    )
}

@Composable
private fun PasswordStrengthIndicator(strength: PasswordStrength) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            when {
                                index < strength.level -> strength.color
                                else -> Color.LightGray.copy(alpha = 0.3f)
                            }
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = strength.label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = strength.color,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

private fun calculatePasswordStrength(password: String): PasswordStrength {
    if (password.isEmpty()) return PasswordStrength(0, "Enter password", Color.Gray)

    var score = 0
    if (password.length >= 8) score++
    if (password.any { it.isUpperCase() }) score++
    if (password.any { it.isDigit() }) score++
    if (password.any { !it.isLetterOrDigit() }) score++

    return when (score) {
        0, 1 -> PasswordStrength(1, "Weak", ErrorRed)
        2 -> PasswordStrength(2, "Fair", Color(0xFFF59E0B))
        3 -> PasswordStrength(3, "Good", Color(0xFF10B981))
        4 -> PasswordStrength(4, "Strong", SuccessGreen)
        else -> PasswordStrength(0, "", Color.Gray)
    }
}

data class PasswordStrength(val level: Int, val label: String, val color: Color)

private fun validateInputs(
    fullName: String,
    email: String,
    password: String,
    confirmPassword: String,
    onErrorChange: (String) -> Unit
): Boolean {
    return when {
        fullName.isBlank() -> {
            onErrorChange("Please enter your full name")
            false
        }
        email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
            onErrorChange("Please enter a valid email address")
            false
        }
        password.length < 6 -> {
            onErrorChange("Password must be at least 6 characters")
            false
        }
        password != confirmPassword -> {
            onErrorChange("Passwords do not match")
            false
        }
        else -> true
    }
}

private fun performSignup(
    auth: FirebaseAuth,
    fullName: String,
    email: String,
    password: String,
    navController: NavController,
    context: android.content.Context,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onLoadingChange: (Boolean) -> Unit,
    onErrorChange: (String) -> Unit,
    onSuccessChange: (String) -> Unit
) {
    onLoadingChange(true)
    onErrorChange("")
    onSuccessChange("")

    auth.createUserWithEmailAndPassword(email.trim(), password.trim())
        .addOnSuccessListener { result ->
            onLoadingChange(false)
            onSuccessChange("Account created successfully!")

            // Update display name
            result.user?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(fullName.trim())
                    .build()
            )

            // Navigate to home after delay using coroutineScope
            coroutineScope.launch {
                delay(1500)
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }
        }
        .addOnFailureListener {
            onLoadingChange(false)
            onErrorChange(it.message ?: "Signup failed. Please try again.")
        }
}