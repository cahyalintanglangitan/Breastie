package com.example.breastieproject.ui.screens.profile

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.breastieproject.viewmodels.AuthViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(
    onBack: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onQuestion: () -> Unit = {},
    onContactSupport: () -> Unit = {},
    onAbout: () -> Unit = {},
    onSignOut: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val scrollState = rememberScrollState()

    // Format date
    val memberSince = currentUser?.createdAt?.let { timestamp ->
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(timestamp))
    } ?: "Recently"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEDFA))
            .verticalScroll(scrollState)
    ) {
        // Header
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
                // ✅ BACK BUTTON
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back to Home",
                        tint = Color.White
                    )
                }

                // Title
                Text(
                    text = "Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Profile Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Photo
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentUser?.profilePhotoUrl?.isNotBlank() == true) {
                        AsyncImage(
                            model = currentUser?.profilePhotoUrl,
                            contentDescription = "Profile photo",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color(0xFFEC7FA9), CircleShape)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFE4F2))
                                .border(3.dp, Color(0xFFEC7FA9), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp),
                                tint = Color(0xFFEC7FA9)
                            )
                        }
                    }

                    // Camera button
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                            .clickable { onEditProfile() },
                        shape = CircleShape,
                        color = Color(0xFFEC7FA9),
                        shadowElevation = 4.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Upload photo",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name
                Text(
                    text = currentUser?.fullName ?: "Loading...",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Email
                Text(
                    text = currentUser?.email ?: "",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Anonymous name badge
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    color = Color(0xFFFFE4F2),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = Color(0xFFEC7FA9),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentUser?.anonymousName ?: "Anonymous",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFEC7FA9)
                            )
                            Text(
                                text = "Your community identity",
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Member since
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFF666666),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Member since $memberSince",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Account Section
        MenuSection(
            title = "Account",
            items = listOf(
                MenuItem(Icons.Default.Edit, "Edit Profile", onEditProfile),
                MenuItem(Icons.Default.Lock, "Change Password", onChangePassword),
                MenuItem(Icons.Default.Logout, "Sign Out", onSignOut, isDestructive = true)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Help & Support Section
        MenuSection(
            title = "Help & Support",
            items = listOf(
                MenuItem(Icons.Default.QuestionAnswer, "Question", onQuestion),
                MenuItem(Icons.Default.Email, "Contact Support", onContactSupport)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // About Section
        MenuSection(
            title = "About",
            items = listOf(
                MenuItem(Icons.Default.Info, "About Breastie", onAbout)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // App Version
        Text(
            text = "Version 1.0.0",
            fontSize = 12.sp,
            color = Color(0xFF999999),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        )
    }
}

@Composable
fun MenuSection(
    title: String,
    items: List<MenuItem>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF666666),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            items.forEachIndexed { index, item ->
                MenuItemRow(item)
                if (index < items.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFEEEEEE)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItemRow(item: MenuItem) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() },
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = if (item.isDestructive) Color(0xFFD32F2F) else Color(0xFFEC7FA9),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.title,
                fontSize = 16.sp,
                color = if (item.isDestructive) Color(0xFFD32F2F) else Color(0xFF333333),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFFCCCCCC),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

data class MenuItem(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit,
    val isDestructive: Boolean = false
)