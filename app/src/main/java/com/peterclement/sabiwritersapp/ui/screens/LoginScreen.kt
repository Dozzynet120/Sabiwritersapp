package com.peterclement.sabiwritersapp.ui.screens

import android.net.Uri
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
import androidx.compose.ui.graphics.SolidColor
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.google.firebase.auth.FirebaseAuth
import com.peterclement.sabiwritersapp.R
import com.peterclement.sabiwritersapp.ui.navigation.Routes

// Modern color palette - adjust these to match your brand
private val PrimaryDark = Color(0xFF1A237E)
private val PrimaryLight = Color(0xFF3949AB)
private val AccentGold = Color(0xFFFFB300)
private val SurfaceLight = Color(0xFFF8F9FA)
private val TextPrimary = Color(0xFF1C1C1E)
private val TextSecondary = Color(0xFF6B7280)
private val ErrorRed = Color(0xFFDC2626)

@Composable
fun LoginScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundVideo()

        // Gradient overlay for better text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.6f),
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            LoginContent(navController)
        }
    }
}

@Composable
private fun LoginContent(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

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
        Spacer(modifier = Modifier.height(60.dp))

        // Logo Section with animation
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
                // Logo container with subtle background
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = CircleShape,
                            spotColor = AccentGold.copy(alpha = 0.3f)
                        )
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.95f))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sabiwriterslogo),
                        contentDescription = "SabiWriters Logo",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sign in to your account",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Login Card
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
                    // Email Field
                    ModernTextField(
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

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Field
                    ModernTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if (error.isNotEmpty()) error = ""
                        },
                        label = "Password",
                        leadingIcon = Icons.Default.Lock,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        onImeAction = {
                            focusManager.clearFocus()
                            if (email.isNotBlank() && password.isNotBlank()) {
                                performLogin(
                                    auth, email, password,
                                    navController, context,
                                    onLoadingChange = { loading = it },
                                    onErrorChange = { error = it }
                                )
                            }
                        },
                        isError = error.isNotEmpty() && password.isBlank(),
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onPasswordVisibilityChange = { passwordVisible = !passwordVisible }
                    )

                    // Forgot Password
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        TextButton(
                            onClick = {
                                if (email.isBlank()) {
                                    error = "Enter email to reset password"
                                } else {
                                    auth.sendPasswordResetEmail(email.trim())
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Password reset email sent",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                        .addOnFailureListener {
                                            error = it.message ?: "Reset failed"
                                        }
                                }
                            },
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "Forgot Password?",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = PrimaryLight,
                                    fontWeight = FontWeight.Medium
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

                    Spacer(modifier = Modifier.height(8.dp))

                    // Login Button
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp),
                                spotColor = PrimaryLight.copy(alpha = 0.4f)
                            ),
                        enabled = !loading && email.isNotBlank() && password.isNotBlank(),
                        onClick = {
                            performLogin(
                                auth, email, password,
                                navController, context,
                                onLoadingChange = { loading = it },
                                onErrorChange = { error = it }
                            )
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
                                text = "Sign In",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Divider with Social Auth
        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(animationSpec = tween(600, delayMillis = 500))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                ModernDividerWithText("Or continue with")

                Spacer(modifier = Modifier.height(20.dp))

                // Social Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SocialAuthButtonModern(
                        text = "Google",
                        iconVector = Icons.Default.Email, // Replace with actual Google icon later
                        onClick = {
                            Toast.makeText(context, "Google login coming soon", Toast.LENGTH_SHORT).show()
                        }
                    )

                    SocialAuthButtonModern(
                        text = "Apple",
                        iconVector = Icons.Default.Lock, // Replace with actual Apple icon later
                        onClick = {
                            Toast.makeText(context, "Apple login coming soon", Toast.LENGTH_SHORT).show()
                        }
                    )

                    SocialAuthButtonModern(
                        text = "Guest",
                        iconVector = Icons.Default.Person,
                        onClick = {
                            navController.navigate(Routes.HOME)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sign Up Link
        AnimatedVisibility(
            visible = contentVisible,
            enter = fadeIn(animationSpec = tween(600, delayMillis = 600))
        ) {
            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = 0.8f)
                    )
                )
                Text(
                    text = "Create Account",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = AccentGold,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable {
                        navController.navigate(Routes.SIGNUP)
                    }
                )
            }
        }
    }
}

@Composable
private fun ModernTextField(
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
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Filled.Visibility,
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
private fun ModernDividerWithText(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.White.copy(alpha = 0.3f),
            thickness = 1.dp
        )
        Text(
            text = "  $text  ",
            style = MaterialTheme.typography.labelMedium.copy(
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 0.5.sp
            )
        )
        Divider(
            modifier = Modifier.weight(1f),
            color = Color.White.copy(alpha = 0.3f),
            thickness = 1.dp
        )
    }
}

@Composable
private fun SocialAuthButtonModern(
    text: String,
    iconVector: ImageVector,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .width(100.dp)
            .height(56.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White.copy(alpha = 0.1f),
            contentColor = Color.White
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            width = 1.dp,
            brush = SolidColor(Color.White.copy(alpha = 0.3f))
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = iconVector,
                contentDescription = text,
                modifier = Modifier.size(20.dp),
                tint = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = Color.White
                )
            )
        }
    }
}

private fun performLogin(
    auth: FirebaseAuth,
    email: String,
    password: String,
    navController: NavController,
    context: android.content.Context,
    onLoadingChange: (Boolean) -> Unit,
    onErrorChange: (String) -> Unit
) {
    onLoadingChange(true)
    onErrorChange("")

    auth.signInWithEmailAndPassword(email.trim(), password.trim())
        .addOnSuccessListener {
            onLoadingChange(false)
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }
        .addOnFailureListener {
            onLoadingChange(false)
            onErrorChange("Invalid login credentials")
        }
}

@Composable
private fun BackgroundVideo() {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = Uri.parse(
                "android.resource://${context.packageName}/raw/splash_bg"
            )
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

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = false
            }
        }
    )
}