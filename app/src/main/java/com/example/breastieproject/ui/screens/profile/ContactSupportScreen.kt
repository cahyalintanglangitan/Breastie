/**
 * ============================================================================
 * FILE: ContactSupportScreen.kt
 * LOCATION: ui/screens/profile/ContactSupportScreen.kt
 * ============================================================================
 *
 * 🎯 DESKRIPSI:
 * Screen untuk contact support form. Frontend only (no backend integration).
 * Displays success message when submitted, actual email sending not implemented.
 *
 * FEATURES:
 * - Contact form (email, subject, message)
 * - Input validation
 * - Success feedback (simulated)
 * - Support info display
 * - Pink theme consistent
 *
 * UI COMPONENTS:
 * - Top bar with back button
 * - Support contact info
 * - Email input field
 * - Subject input field
 * - Message textarea
 * - Submit button
 * - Success dialog
 *
 * DATA FLOW:
 * - User fills form
 * - Validate inputs
 * - Show success message (no actual sending)
 * - Clear form
 *
 * FUTURE ENHANCEMENTS:
 * - Actual email sending via API
 * - File attachment support
 * - Support ticket system
 * - Response tracking
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 18 Dec 2024
 * STATUS: ✅ Complete - Static Frontend
 * DEPENDENCIES: None
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactSupportScreen(
    onBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Form states
    var email by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Validation
    val isFormValid = email.contains("@") &&
            subject.isNotBlank() &&
            message.length >= 10

    // Success dialog
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                email = ""
                subject = ""
                message = ""
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "Message Sent!",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Thank you for contacting us! Our support team will respond to your message within 24-48 hours.",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        email = ""
                        subject = ""
                        message = ""
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEC7FA9)
                    )
                ) {
                    Text("OK")
                }
            }
        )
    }

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
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Contact Support",
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
                .verticalScroll(scrollState)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Support info card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = null,
                        tint = Color(0xFFEC7FA9),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "We're Here to Help!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Have questions or need assistance? Send us a message and we'll get back to you within 24-48 hours.",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Contact methods
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = Color(0xFFEC7FA9)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Email",
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                            Text(
                                text = "support@breastie.com",
                                fontSize = 11.sp,
                                color = Color(0xFF999999)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = Color(0xFFEC7FA9)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Response Time",
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                            Text(
                                text = "24-48 hours",
                                fontSize = 11.sp,
                                color = Color(0xFF999999)
                            )
                        }
                    }
                }
            }

            // Contact form
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Send us a message",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Your Email *") },
                        placeholder = { Text("example@email.com") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = Color(0xFFEC7FA9)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFEC7FA9),
                            focusedLabelColor = Color(0xFFEC7FA9),
                            cursorColor = Color(0xFFEC7FA9)
                        )
                    )

                    // Subject
                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Subject *") },
                        placeholder = { Text("What is your message about?") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Subject,
                                contentDescription = null,
                                tint = Color(0xFFEC7FA9)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFEC7FA9),
                            focusedLabelColor = Color(0xFFEC7FA9),
                            cursorColor = Color(0xFFEC7FA9)
                        )
                    )

                    // Message
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Message *") },
                        placeholder = { Text("Please describe your question or issue...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        minLines = 6,
                        maxLines = 10,
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFEC7FA9),
                            focusedLabelColor = Color(0xFFEC7FA9),
                            cursorColor = Color(0xFFEC7FA9)
                        )
                    )

                    // Character count
                    Text(
                        text = "${message.length} characters (minimum 10)",
                        fontSize = 12.sp,
                        color = if (message.length >= 10)
                            Color(0xFF4CAF50)
                        else
                            Color(0xFF666666)
                    )

                    // Submit button
                    Button(
                        onClick = { showSuccessDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = isFormValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEC7FA9),
                            disabledContainerColor = Color(0xFFFFB8E0)
                        ),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Send Message",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}