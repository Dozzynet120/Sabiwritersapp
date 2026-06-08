package com.peterclement.sabiwritersapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.peterclement.sabiwritersapp.ui.navigation.Routes

@Composable
fun RegisterScreen(navController: NavController) {

    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading,
            onClick = {

                when {
                    email.isBlank() || password.isBlank() -> {
                        error = "All fields are required"
                        return@Button
                    }
                    password != confirmPassword -> {
                        error = "Passwords do not match"
                        return@Button
                    }
                    password.length < 6 -> {
                        error = "Password must be at least 6 characters"
                        return@Button
                    }
                }

                loading = true
                error = ""

                auth.createUserWithEmailAndPassword(
                    email.trim(),
                    password.trim()
                )
                    .addOnSuccessListener {

                        // 🔥 IMPORTANT FIX
                        auth.signOut()

                        loading = false

                        Toast.makeText(
                            context,
                            "Account created successfully. Please login.",
                            Toast.LENGTH_LONG
                        ).show()

                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.SIGNUP) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                    .addOnFailureListener {
                        loading = false
                        error = it.message ?: "Registration failed"
                    }
            }
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text("Create Account")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
            onClick = {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.SIGNUP) { inclusive = true }
                }
            }
        ) {
            Text("Already have an account? Login")
        }
    }
}
