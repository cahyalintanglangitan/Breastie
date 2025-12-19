package com.example.breastieproject.ui.screens.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.breastieproject.viewmodels.AuthViewModel
import com.example.breastieproject.viewmodels.UpdateState  // ✅ ADD THIS IMPORT!

@Composable
fun EditProfileScreen(
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val updateState by viewModel.updateState.collectAsState()

    var fullName by remember { mutableStateOf(currentUser?.fullName ?: "") }
    var dateOfBirth by remember { mutableStateOf(currentUser?.dateOfBirth ?: "") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            fullName = user.fullName
            dateOfBirth = user.dateOfBirth
        }
    }

    // ✅ OBSERVE UPDATE STATE
    LaunchedEffect(updateState) {
        Log.d("BREASTIE_UI", "====== UI STATE CHANGED ======")
        Log.d("BREASTIE_UI", "updateState = $updateState")
        Log.d("BREASTIE_UI", "Is Success? ${updateState is UpdateState.Success}")

        if (updateState is UpdateState.Success) {
            Log.d("BREASTIE_UI", "✅ SUCCESS DETECTED!")
            showSuccessDialog = true
            Log.d("BREASTIE_UI", "Dialog set to: $showSuccessDialog")

            kotlinx.coroutines.delay(1500)
            Log.d("BREASTIE_UI", "Navigating...")

            onSuccess()
            viewModel.resetUpdateState()
        }
    }

    LaunchedEffect(showSuccessDialog) {
        Log.d("BREASTIE_UI", "showSuccessDialog = $showSuccessDialog")
    }

    // ✅ SUCCESS DIALOG
    if (showSuccessDialog) {
        Log.d("BREASTIE_UI", "Rendering dialog...")

        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onSuccess()
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
                    text = "Profile Updated!",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Your profile has been successfully updated.",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEC7FA9)
                    )
                ) {
                    Text("OK")
                }
            },
            containerColor = Color.White
        )
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val isFormValid = fullName.isNotBlank()
    val isLoading = updateState is UpdateState.Loading
    val errorMessage = if (updateState is UpdateState.Error) {
        (updateState as UpdateState.Error).message
    } else null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEDFA))
    ) {
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
                    text = "Edit Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )

                TextButton(
                    onClick = {
                        Log.d("BREASTIE_UI", "Save clicked!")
                        viewModel.updateProfile(
                            fullName = fullName.trim(),
                            dateOfBirth = dateOfBirth.trim(),
                            imageUri = selectedImageUri
                        )
                    },
                    enabled = isFormValid && !isLoading
                ) {
                    if (isLoading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Text(
                                text = "Saving...",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        Text(
                            text = "Save",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

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

            if (isLoading) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD)
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFEC7FA9),
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Updating profile...",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1976D2)
                            )
                            Text(
                                text = "Please wait, uploading photo and saving data",
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Box(
                modifier = Modifier.size(140.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    selectedImageUri != null -> {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected photo",
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .border(4.dp, Color(0xFFEC7FA9), CircleShape)
                        )
                    }
                    currentUser?.profilePhotoUrl?.isNotBlank() == true -> {
                        AsyncImage(
                            model = currentUser?.profilePhotoUrl,
                            contentDescription = "Profile photo",
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .border(4.dp, Color(0xFFEC7FA9), CircleShape)
                        )
                    }
                    else -> {
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFE4F2))
                                .border(4.dp, Color(0xFFEC7FA9), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(70.dp),
                                tint = Color(0xFFEC7FA9)
                            )
                        }
                    }
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(44.dp)
                        .clickable(enabled = !isLoading) {
                            imagePickerLauncher.launch("image/*")
                        },
                    shape = CircleShape,
                    color = Color(0xFFEC7FA9),
                    shadowElevation = 6.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Upload photo",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Tap camera icon to change photo",
                fontSize = 12.sp,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(32.dp))

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
                    Column {
                        Text(
                            text = "Full Name *",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF666666)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = !isLoading,
                            shape = MaterialTheme.shapes.medium,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFEC7FA9),
                                focusedLabelColor = Color(0xFFEC7FA9),
                                cursorColor = Color(0xFFEC7FA9)
                            )
                        )
                    }

                    Column {
                        Text(
                            text = "Date of Birth (Optional)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF666666)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = dateOfBirth,
                            onValueChange = { dateOfBirth = it },
                            placeholder = { Text("DD/MM/YYYY") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = !isLoading,
                            shape = MaterialTheme.shapes.medium,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFEC7FA9),
                                focusedLabelColor = Color(0xFFEC7FA9),
                                cursorColor = Color(0xFFEC7FA9)
                            )
                        )
                    }

                    Surface(
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
                                tint = Color(0xFFFF9800),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Your real name is private and only visible to you.",
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ❌❌❌ DELETE THIS ENTIRE SECTION! ❌❌❌
// UpdateState should ONLY be in AuthViewModel.kt!