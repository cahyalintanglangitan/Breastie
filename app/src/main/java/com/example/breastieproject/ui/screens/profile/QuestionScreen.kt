/**
 * ============================================================================
 * FILE: QuestionScreen.kt (RENAMED from FAQScreen.kt)
 * LOCATION: ui/screens/profile/QuestionScreen.kt
 * ============================================================================
 *
 * 🎯 DESKRIPSI:
 * Screen untuk menampilkan Questions seputar aplikasi Breastie dan fitur-fiturnya.
 * BERBEDA dengan FAQ di HomeScreen yang untuk tanya AI Assistant!
 *
 * PERBEDAAN:
 * - HomeScreen FAQ: Pertanyaan kesehatan → Tanya AI Chatbot
 * - Profile Question: Pertanyaan app → Baca jawaban di sini
 *
 * ============================================================================
 */

package com.example.breastieproject.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Question(
    val question: String,
    val answer: String,
    val category: String
)

@Composable
fun QuestionScreen(
    onBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Question Data (about the app)
    val questionList = listOf(
        // Getting Started
        Question(
            question = "What is Breastie?",
            answer = "Breastie is a comprehensive breast health support app designed for breast cancer survivors and those at risk. It provides community support, health tracking, reminders, and AI-powered assistance to help you manage your breast health journey.",
            category = "Getting Started"
        ),
        Question(
            question = "How do I create an account?",
            answer = "Tap 'Register Now' on the welcome screen, fill in your full name, email, and password. Your account will be created immediately and you can start using all features.",
            category = "Getting Started"
        ),
        Question(
            question = "Is Breastie free to use?",
            answer = "Yes! Breastie is completely free. All features including community access, reminders, and AI assistant are available at no cost.",
            category = "Getting Started"
        ),

        // Community
        Question(
            question = "How do I join a community?",
            answer = "Go to the Community tab, browse available communities, and tap 'Join' on any community that interests you. You'll instantly become a member and can start participating.",
            category = "Community Features"
        ),
        Question(
            question = "Can I create my own community?",
            answer = "Yes! In the Community tab, tap the '+' button to create a new community. Choose a name, description, and set the community type (public or private).",
            category = "Community Features"
        ),
        Question(
            question = "How do I post in a community?",
            answer = "Open any community you've joined, tap the '+' button at the bottom, write your post, and tap 'Share'. Your post will appear with your anonymous name.",
            category = "Community Features"
        ),

        // Privacy & Anonymous
        Question(
            question = "Is my identity really anonymous in communities?",
            answer = "Yes! Your real name and profile photo are NEVER shown in community areas. You appear with an anonymous name (like 'Brave Warrior') and a pink circle avatar. Only you can see your real identity in your profile.",
            category = "Privacy & Anonymous Identity"
        ),
        Question(
            question = "Can I change my anonymous name?",
            answer = "Currently, anonymous names are auto-generated and cannot be changed. This feature will be available in a future update to maintain consistency in communities.",
            category = "Privacy & Anonymous Identity"
        ),
        Question(
            question = "Who can see my profile photo?",
            answer = "Only you can see your profile photo. It's displayed in your private Profile screen but NEVER shown in communities, posts, comments, or chats. Your privacy is our priority.",
            category = "Privacy & Anonymous Identity"
        ),
        Question(
            question = "Can other users find out my real name?",
            answer = "No. Your real name is stored securely and only visible to you. In all community interactions (posts, comments, chats), only your anonymous name is shown.",
            category = "Privacy & Anonymous Identity"
        ),

        // Reminders
        Question(
            question = "How do I set medication reminders?",
            answer = "Go to the Reminder tab, tap '+', choose 'Medication', set the time and frequency, and save. You'll receive notifications at the scheduled times.",
            category = "Reminders & Health Tracking"
        ),
        Question(
            question = "Can I set multiple reminders?",
            answer = "Yes! You can set unlimited reminders for medications, appointments, self-checks, and other health activities. Each reminder can have its own schedule.",
            category = "Reminders & Health Tracking"
        ),
        Question(
            question = "What if I miss a reminder?",
            answer = "You'll receive a notification, and the reminder will be marked as missed in your Reminder tab. You can mark it as completed later or reschedule it.",
            category = "Reminders & Health Tracking"
        ),

        // AI Assistant
        Question(
            question = "What can the AI Assistant help me with?",
            answer = "The AI Assistant can answer questions about breast health, explain medical terms, provide emotional support, and guide you through self-examination procedures. It's available 24/7 in the AI tab.",
            category = "AI Assistant"
        ),
        Question(
            question = "Is the AI Assistant a replacement for doctors?",
            answer = "No. The AI Assistant provides information and support but is NOT a substitute for professional medical advice. Always consult your doctor for diagnosis and treatment.",
            category = "AI Assistant"
        ),
        Question(
            question = "Is my conversation with AI private?",
            answer = "Yes. Your AI conversations are private and secure. They are not shared with other users or displayed in communities.",
            category = "AI Assistant"
        ),

        // Profile & Account
        Question(
            question = "How do I change my profile photo?",
            answer = "Go to Profile → Edit Profile → Tap the camera icon on your photo → Select a new photo from your gallery → Save. Remember, this photo is private and only visible to you.",
            category = "Profile & Account"
        ),
        Question(
            question = "How do I change my password?",
            answer = "Go to Profile → Change Password → Enter your current password → Enter new password → Confirm new password → Save. You'll be prompted to sign in again with the new password.",
            category = "Profile & Account"
        ),
        Question(
            question = "Can I delete my account?",
            answer = "Account deletion will be available in a future update. For now, you can sign out and stop using the app. Contact support if you need urgent account deletion.",
            category = "Profile & Account"
        ),
        Question(
            question = "How do I sign out?",
            answer = "Go to Profile → Sign Out → Confirm. You'll be returned to the welcome screen and can sign in again anytime.",
            category = "Profile & Account"
        )
    )

    // Group by category
    val questionsByCategory = questionList.groupBy { it.category }

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
                    text = "Question",  // ✅ CHANGED FROM "FAQ"
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Intro text
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF3E0)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Find answers to common questions about using Breastie app. Tap any question to expand.",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }
            }

            // Question Categories
            questionsByCategory.forEach { (category, questions) ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Category header
                    Text(
                        text = category,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEC7FA9)
                    )

                    // Question items
                    questions.forEach { question ->
                        QuestionCard(question = question)
                    }
                }
            }

            // Still have questions?
            Card(
                modifier = Modifier.fillMaxWidth(),
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
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color(0xFFEC7FA9),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Still have questions?",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Contact our support team for help",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionCard(question: Question) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Question
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question.question,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded)
                        Icons.Default.ExpandLess
                    else
                        Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = Color(0xFFEC7FA9)
                )
            }

            // Answer (expandable)
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = question.answer,
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}