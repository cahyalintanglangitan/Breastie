package com.example.breastieproject.ui.screens.webinar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.breastieproject.data.model.WebinarEvent
import com.example.breastieproject.data.repository.dummy.DummyWebinarData
import com.example.breastieproject.ui.theme.BackupTheme
import com.example.breastieproject.viewmodels.WebinarViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.breastieproject.R

@Composable
fun WebinarDetailScreen(
    viewModel: WebinarViewModel = viewModel(),  // ✅ ADD ViewModel
    onBackClick: () -> Unit = {}
) {
    var showSuccessDialog by remember { mutableStateOf(false) }

    // ✅ Get states from ViewModel
    val currentWebinar by viewModel.currentWebinar.collectAsState()
    val isRegistered by viewModel.isRegistered.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // ✅ Get user email
    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: ""

    // Use webinar from ViewModel or fallback to dummy
    val webinar = currentWebinar ?: DummyWebinarData.currentWebinar

    Scaffold(
        topBar = {
            WebinarHeader(
                title = webinar.title,
                onBackClick = onBackClick
            )
        },
        containerColor = Color(0xFFFFF0F8)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            Image(
                painter = painterResource(id = R.drawable.webinar_poster),
                contentDescription = "Webinar Poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )


            // Event Overview Card
            EventOverviewCard(webinar = webinar)

            // What to Expect Section
            SectionCard(
                title = "What to Expect on the Day",
                content = webinar.whatToExpect
            )

            // Registration Details Section
            SectionCard(
                title = "Registration Details",
                content = webinar.registrationDetails
            )

            // ✅ UPDATED REGISTER BUTTON:
            if (isRegistered) {
                // Already registered
                Button(
                    onClick = { /* Already registered */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFCCCCCC)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = false
                ) {
                    Text(
                        text = "✓ Already Registered",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Not registered yet
                Button(
                    onClick = {
                        viewModel.registerForWebinar(
                            onSuccess = {
                                showSuccessDialog = true
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEC7FA9)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Reserve a Spot",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // ✅ SUCCESS DIALOG (Updated message)
    if (showSuccessDialog) {
        SuccessDialog(
            userEmail = userEmail,
            onDismiss = {
                showSuccessDialog = false
                onBackClick()
            }
        )
    }
}

@Composable
private fun WebinarHeader(
    title: String,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFFFB8E0),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Webinar",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun EventOverviewCard(
    webinar: WebinarEvent
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Badge
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFFE4F2)
            ) {
                Text(
                    text = "Webinar",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEC7FA9)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = "Event Overview",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = webinar.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = webinar.description,
                fontSize = 14.sp,
                color = Color(0xFF555555),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date",
                    tint = Color(0xFFEC7FA9),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Date:",
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                    Text(
                        text = webinar.date,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = "Location",
                    tint = Color(0xFFEC7FA9),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Location:",
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                    Text(
                        text = webinar.location,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = content,
                fontSize = 14.sp,
                color = Color(0xFF555555),
                lineHeight = 20.sp
            )
        }
    }
}



@Composable
private fun SuccessDialog(
    userEmail: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Icon
                Text(
                    text = "🎉",
                    fontSize = 64.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "YOU ARE RESERVED!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEC7FA9)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ✅ Show user email
                Text(
                    text = "Zoom link and event details will be sent to:",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = userEmail,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEC7FA9),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEC7FA9)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WebinarDetailScreenPreview() {
    BackupTheme {
        WebinarDetailScreen()
    }
}