/**
 * ============================================================================
 * FILE: ChangePasswordScreen.kt
 * LOCATION: ui/screens/profile/ChangePasswordScreen.kt
 * ============================================================================
 *
 * 🎯 DESKRIPSI:
 * Screen untuk change password user menggunakan Firebase Authentication.
 * Requires current password for security (re-authentication).
 *
 * SECURITY FEATURES:
 * - Current password verification (re-authentication)
 * - New password validation (min 6 characters)
 * - Password confirmation match
 * - Password visibility toggle
 * - Secure Firebase Auth API
 *
 * DATA FLOW:
 * 1. User enters current password
 * 2. User enters new password
 * 3. User confirms new password
 * 4. Validate all fields
 * 5. Re-authenticate with current password
 * 6. Update password via Firebase Auth
 * 7. Success feedback & navigate back
 *
 * VALIDATION RULES:
 * - Current password: Required, must be correct
 * - New password: Min 6 characters, different from current
 * - Confirm password: Must match new password
 *
 * ERROR HANDLING:
 * - Wrong current password
 * - Weak new password
 * - Network errors
 * - Session expired
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 18 Dec 2024
 * STATUS: ✅ Complete with Firebase Auth integration
 * DEPENDENCIES: Firebase Authentication
 * ============================================================================
 */

package com.example.breastieproject.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.breastieproject.viewmodels.AuthViewModel
import com.example.breastieproject.viewmodels.UpdateState

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    val updateState by viewModel.updateState.collectAsState()

    // Form states
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // UI states
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Handle success
    LaunchedEffect(updateState) {
        if (updateState is UpdateState.Success) {
            onSuccess()
            viewModel.resetUpdateState()
        }
    }

    // Validation
    val isFormValid = currentPassword.isNotBlank() &&
            newPassword.length >= 6 &&
            newPassword == confirmPassword &&
            currentPassword != newPassword

    val isLoading = updateState is UpdateState.Loading
    val errorMessage = if (updateState is UpdateState.Error) {
        (updateState as UpdateState.Error).message
    } else null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEDFA))
    ) {
        // Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFEC7FA9),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, enabled = !isLoading) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Change Password",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Error message
            errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFEBEE)
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = Color(0xFFD32F2F)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = error,
                            color = Color(0xFFD32F2F),
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Info card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                color = Color(0xFFFFF3E0)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFFF9800)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Password must be at least 6 characters.",
                            fontSize = 12.sp,
                            color = Color(0xFF666666)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Form Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Current Password
                    Column {
                        Text(
                            text = "Current Password *",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF666666)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = !isLoading,
                            visualTransformation = if (currentPasswordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            trailingIcon = {
                                IconButton(
                                    onClick = { currentPasswordVisible = !currentPasswordVisible }
                                ) {
                                    Icon(
                                        imageVector = if (currentPasswordVisible)
                                            Icons.Default.Visibility
                                        else
                                            Icons.Default.VisibilityOff,
                                        contentDescription = if (currentPasswordVisible)
                                            "Hide password"
                                        else
                                            "Show password",
                                        tint = Color(0xFFEC7FA9)
                                    )
                                }
                            },
                            shape = MaterialTheme.shapes.medium,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFEC7FA9),
                                focusedLabelColor = Color(0xFFEC7FA9),
                                cursorColor = Color(0xFFEC7FA9)
                            )
                        )
                    }

                    HorizontalDivider(color = Color(0xFFEEEEEE))

                    // New Password
                    Column {
                        Text(
                            text = "New Password *",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF666666)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = !isLoading,
                            visualTransformation = if (newPasswordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            trailingIcon = {
                                IconButton(
                                    onClick = { newPasswordVisible = !newPasswordVisible }
                                ) {
                                    Icon(
                                        imageVector = if (newPasswordVisible)
                                            Icons.Default.Visibility
                                        else
                                            Icons.Default.VisibilityOff,
                                        contentDescription = if (newPasswordVisible)
                                            "Hide password"
                                        else
                                            "Show password",
                                        tint = Color(0xFFEC7FA9)
                                    )
                                }
                            },
                            isError = newPassword.isNotEmpty() && newPassword.length < 6,
                            shape = MaterialTheme.shapes.medium,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFEC7FA9),
                                focusedLabelColor = Color(0xFFEC7FA9),
                                cursorColor = Color(0xFFEC7FA9),
                                errorBorderColor = Color(0xFFD32F2F)
                            )
                        )
                        if (newPassword.isNotEmpty() && newPassword.length < 6) {
                            Text(
                                text = "Password must be at least 6 characters",
                                fontSize = 12.sp,
                                color = Color(0xFFD32F2F),
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }

                    // Confirm Password
                    Column {
                        Text(
                            text = "Confirm New Password *",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF666666)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = !isLoading,
                            visualTransformation = if (confirmPasswordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password
                            ),
                            trailingIcon = {
                                IconButton(
                                    onClick = { confirmPasswordVisible = !confirmPasswordVisible }
                                ) {
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
                            isError = confirmPassword.isNotEmpty() && confirmPassword != newPassword,
                            shape = MaterialTheme.shapes.medium,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFEC7FA9),
                                focusedLabelColor = Color(0xFFEC7FA9),
                                cursorColor = Color(0xFFEC7FA9),
                                errorBorderColor = Color(0xFFD32F2F)
                            )
                        )
                        if (confirmPassword.isNotEmpty() && confirmPassword != newPassword) {
                            Text(
                                text = "Passwords don't match",
                                fontSize = 12.sp,
                                color = Color(0xFFD32F2F),
                                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Save Button
            Button(
                onClick = {
                    viewModel.changePassword(
                        currentPassword = currentPassword,
                        newPassword = newPassword
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = isFormValid && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEC7FA9),
                    disabledContainerColor = Color(0xFFFFB8E0)
                ),
                shape = MaterialTheme.shapes.large
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Change Password",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}