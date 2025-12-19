package com.example.breastieproject.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.breastieproject.viewmodels.AuthViewModel
import com.example.breastieproject.ui.state.AuthState

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()  // ✅ ADD ViewModel!
) {
    // Form states
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }

    // UI states
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // ✅ Observe AuthState dari ViewModel
    val authState by viewModel.authState.collectAsState()

    // ✅ Handle AuthState changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                // Sign up successful! Navigate to main
                onSignUpSuccess()
                viewModel.resetAuthState()
            }
            else -> { /* Do nothing */ }
        }
    }

    // Validation
    val isFormValid = fullName.isNotBlank() &&
            email.isNotBlank() &&
            password.length >= 6 &&
            password == confirmPassword

    // ✅ Loading state
    val isLoading = authState is AuthState.Loading

    // ✅ Error message
    val errorMessage = if (authState is AuthState.Error) {
        (authState as AuthState.Error).message
    } else null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEDFA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFFFB8E0),
                                Color(0xFFEC7FA9)
                            )
                        )
                    )
                    .padding(vertical = 48.dp, horizontal = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Create Account",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        style = MaterialTheme.typography.displayMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Join our supportive community",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // Form Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // ✅ Error message from Firebase
                    errorMessage?.let { error ->
                        Text(
                            text = error,
                            color = Color(0xFFD32F2F),
                            fontSize = 14.sp,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xFFFFEBEE),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                        )
                    }

                    // Full Name
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = {
                            Text(
                                "Full Name *",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFEC7FA9),
                            focusedLabelColor = Color(0xFFEC7FA9),
                            cursorColor = Color(0xFFEC7FA9)
                        ),
                        enabled = !isLoading  // ✅ Disable saat loading
                    )

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = {
                            Text(
                                "Email *",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFEC7FA9),
                            focusedLabelColor = Color(0xFFEC7FA9),
                            cursorColor = Color(0xFFEC7FA9)
                        ),
                        enabled = !isLoading
                    )

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = {
                            Text(
                                "Password *",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible)
                                        "Hide password"
                                    else
                                        "Show password",
                                    tint = Color(0xFFEC7FA9)
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFEC7FA9),
                            focusedLabelColor = Color(0xFFEC7FA9),
                            cursorColor = Color(0xFFEC7FA9)
                        ),
                        enabled = !isLoading
                    )

                    // Password hint
                    Text(
                        text = "Minimum 6 characters",
                        fontSize = 12.sp,
                        color = Color(0xFF666666),
                        style = MaterialTheme.typography.bodySmall
                    )

                    // Confirm Password
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = {
                            Text(
                                "Confirm Password *",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = if (confirmPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        trailingIcon = {
                            IconButton(onClick = {
                                confirmPasswordVisible = !confirmPasswordVisible
                            }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible)
                                        Icons.Default.Visibility
                                    else
                                        Icons.Default.VisibilityOff,
                                    contentDescription = if (confirmPasswordVisible)
                                        "Hide password"
                                    else
                                        "Show password",
                                    tint = Color(0xFFEC7FA9)
                                )
                            }
                        },
                        isError = confirmPassword.isNotEmpty() &&
                                password != confirmPassword,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFEC7FA9),
                            focusedLabelColor = Color(0xFFEC7FA9),
                            cursorColor = Color(0xFFEC7FA9),
                            errorBorderColor = Color(0xFFD32F2F),
                            errorLabelColor = Color(0xFFD32F2F)
                        ),
                        enabled = !isLoading
                    )

                    // Password match error
                    if (confirmPassword.isNotEmpty() && password != confirmPassword) {
                        Text(
                            text = "Passwords don't match",
                            fontSize = 12.sp,
                            color = Color(0xFFD32F2F),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    // Date of Birth (Optional)
                    OutlinedTextField(
                        value = dateOfBirth,
                        onValueChange = { dateOfBirth = it },
                        label = {
                            Text(
                                "Date of Birth (Optional)",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        placeholder = { Text("DD/MM/YYYY") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFEC7FA9),
                            focusedLabelColor = Color(0xFFEC7FA9),
                            cursorColor = Color(0xFFEC7FA9)
                        ),
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Register Button
                    Button(
                        onClick = {
                            // ✅ Call ViewModel signUp()
                            if (isFormValid) {
                                viewModel.signUp(
                                    fullName = fullName.trim(),
                                    email = email.trim(),
                                    password = password,
                                    dateOfBirth = dateOfBirth.trim()
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = isFormValid && !isLoading,  // ✅ Disable saat loading
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEC7FA9),
                            disabledContainerColor = Color(0xFFFFB8E0)
                        ),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = "Create Account",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }

                    // Sign In link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account? ",
                            fontSize = 14.sp,
                            color = Color(0xFF666666),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        TextButton(
                            onClick = onNavigateToSignIn,
                            contentPadding = PaddingValues(0.dp),
                            enabled = !isLoading
                        ) {
                            Text(
                                text = "Sign In",
                                fontSize = 14.sp,
                                color = Color(0xFFEC7FA9),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}