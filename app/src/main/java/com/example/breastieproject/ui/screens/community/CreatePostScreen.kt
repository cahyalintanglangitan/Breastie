package com.example.breastieproject.ui.screens.community

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breastieproject.data.model.Community
import com.example.breastieproject.data.model.Post
import com.example.breastieproject.data.repository.dummy.DummyCommunityData
import com.example.breastieproject.ui.theme.BackupTheme

@Composable
fun CreatePostScreen(
    joinedCommunities: List<Community> = DummyCommunityData.getJoinedCommunities(),
    onBackClick: () -> Unit = {},
    onPostCreated: (Post) -> Unit = {}
) {
    var postContent by remember { mutableStateOf("") }
    var selectedCommunity by remember { mutableStateOf<Community?>(joinedCommunities.firstOrNull()) }
    var showCommunityDropdown by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val maxCharacters = 500
    val canPost = postContent.isNotBlank() &&
            postContent.length <= maxCharacters &&
            selectedCommunity != null

    Scaffold(
        topBar = {
            CreatePostHeader(
                onBackClick = onBackClick,
                onPostClick = {
                    if (canPost && selectedCommunity != null) {
                        // Create new post
                        val newPost = Post(
                            id = "post_${System.currentTimeMillis()}",
                            communityId = selectedCommunity!!.id,
                            communityName = selectedCommunity!!.name,
                            authorId = "user_123", // Current user
                            authorUsername = "@anonim_user_1234", // Current user
                            content = postContent.trim(),
                            likes = 0,
                            commentCount = 0,
                            timestamp = System.currentTimeMillis(),
                            createdAt = "Baru saja"
                        )
                        onPostCreated(newPost)
                        onBackClick()
                    } else if (postContent.isBlank()) {
                        errorMessage = "Post tidak boleh kosong!"
                        showErrorDialog = true
                    } else if (selectedCommunity == null) {
                        errorMessage = "Pilih komunitas terlebih dahulu!"
                        showErrorDialog = true
                    }
                },
                canPost = canPost
            )
        },
        containerColor = Color(0xFFFFF0F8)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Community Selector
            Text(
                text = "Pilih Komunitas",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dropdown Button
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                onClick = { showCommunityDropdown = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCommunity?.name ?: "Pilih komunitas",
                        fontSize = 16.sp,
                        color = if (selectedCommunity != null)
                            Color(0xFF333333) else Color(0xFF999999)
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color(0xFF999999)
                    )
                }
            }

            // Community Dropdown Menu
            DropdownMenu(
                expanded = showCommunityDropdown,
                onDismissRequest = { showCommunityDropdown = false },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White)
            ) {
                joinedCommunities.forEach { community ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = community.name,
                                fontSize = 16.sp,
                                color = Color(0xFF333333)
                            )
                        },
                        onClick = {
                            selectedCommunity = community
                            showCommunityDropdown = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Post Content Input
            Text(
                text = "Konten Post",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = postContent,
                onValueChange = {
                    if (it.length <= maxCharacters) {
                        postContent = it
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 400.dp),
                placeholder = {
                    Text(
                        text = "Tulis sesuatu...\n\nBagikan pengalaman, ajukan pertanyaan, atau berikan dukungan kepada sesama member komunitas.",
                        color = Color(0xFF999999),
                        fontSize = 14.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFFEC7FA9),
                    unfocusedBorderColor = Color(0xFFDDDDDD),
                    // ✅ ADD THESE!
                    focusedTextColor = Color(0xFF333333),      // Hitam!
                    unfocusedTextColor = Color(0xFF333333),    // Hitam!
                    cursorColor = Color(0xFFEC7FA9)            // Pink cursor
                ),
                shape = RoundedCornerShape(12.dp),
                maxLines = Int.MAX_VALUE
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Character Counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "${postContent.length}/$maxCharacters karakter",
                    fontSize = 12.sp,
                    color = if (postContent.length >= maxCharacters)
                        Color.Red else Color(0xFF999999)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Guidelines Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFE4F2)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "💡 Tips Posting",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = """
                            • Bersikaplah sopan dan supportif
                            • Hormati privasi orang lain
                            • Bagikan pengalaman positif
                            • Hindari informasi medis yang menyesatkan
                        """.trimIndent(),
                        fontSize = 12.sp,
                        color = Color(0xFF666666),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }

    // Error Dialog
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = {
                Text(
                    text = "Perhatian",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(text = errorMessage)
            },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text(
                        text = "OK",
                        color = Color(0xFFEC7FA9)
                    )
                }
            }
        )
    }
}

@Composable
private fun CreatePostHeader(
    onBackClick: () -> Unit,
    onPostClick: () -> Unit,
    canPost: Boolean
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "Buat Post",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Post Button
            TextButton(
                onClick = onPostClick,
                enabled = canPost,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White,
                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = "Post",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreatePostScreenPreview() {
    BackupTheme {
        CreatePostScreen()
    }
}


/**
 * ============================================================================
 * FILE: CreatePostScreen.kt
 * LOCATION: ui/screens/community/CreatePostScreen.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Modal screen untuk membuat post baru di community feed.
 * User pilih komunitas, tulis konten, optional attach image.
 *
 * ============================================================================
 * PARAMETERS
 * ============================================================================
 *
 * joinedCommunities: List<Community> (REQUIRED)
 *   - List komunitas yang user sudah join
 *   - User hanya bisa post di komunitas yang sudah di-join
 *   - Ditampilkan di dropdown selector
 *
 * onBackClick: () -> Unit (default = {})
 *   - Callback saat cancel/close
 *   - Discard draft & close screen
 *
 * onPostCreated: (Post) -> Unit (default = {})
 *   - Callback saat post berhasil dibuat
 *   - Parameter: Post object yang baru dibuat
 *   - Parent add post to feed list
 *
 * ============================================================================
 * UI STRUCTURE
 * ============================================================================
 *
 * Screen Layout:
 * ┌─────────────────────────────────┐
 * │ ← Buat Post Baru         [Post] │  <- Header with post button
 * ├─────────────────────────────────┤
 * │                                 │
 * │ Pilih Komunitas:                │
 * │ [Pejuang Kemo ▼]                │  <- Community dropdown
 * │                                 │
 * │ Tulis sesuatu...                │  <- Content text field
 * │ [Multi-line text input]         │     (expandable)
 * │                                 │
 * │ [+ Tambah Gambar]               │  <- Optional image picker
 * │                                 │
 * │ [Preview image if selected]     │
 * │                                 │
 * └─────────────────────────────────┘
 *
 * ============================================================================
 * STATE MANAGEMENT
 * ============================================================================
 *
 * Local States:
 *
 * selectedCommunity: Community?
 *   - Komunitas yang dipilih untuk post
 *   - null = belum pilih (disable post button)
 *   - Required field
 *
 * postContent: String
 *   - Isi konten post yang diketik user
 *   - Multi-line text
 *   - Minimum length: 1 karakter
 *
 * selectedImageUri: Uri?
 *   - URI gambar yang dipilih (optional)
 *   - null = no image
 *   - Future: Upload to Firebase Storage
 *
 * showCommunityDropdown: Boolean
 *   - true = Dropdown menu terbuka
 *   - false = Tertutup
 *
 * isPosting: Boolean
 *   - true = Sedang proses post (loading)
 *   - false = Idle
 *   - Disable button saat posting
 *
 * ============================================================================
 * VALIDATION
 * ============================================================================
 *
 * Post Button Enabled When:
 *   - selectedCommunity != null ✅
 *   - postContent.isNotEmpty() ✅
 *   - postContent.trim().isNotEmpty() ✅
 *   - !isPosting ✅
 *
 * Post Button Disabled When:
 *   - selectedCommunity == null ❌
 *   - postContent.isEmpty() ❌
 *   - isPosting ❌
 *
 * ============================================================================
 * CREATE POST FLOW
 * ============================================================================
 *
 * 1. User fills form:
 *    - Select community
 *    - Type content
 *    - (Optional) Select image
 *
 * 2. User clicks "Post" button
 *
 * 3. Validation:
 *    if (selectedCommunity == null || postContent.isEmpty()) {
 *        Show error toast
 *        return
 *    }
 *
 * 4. Create Post object:
 *    val newPost = Post(
 *        id = "post_${System.currentTimeMillis()}",
 *        communityId = selectedCommunity.id,
 *        communityName = selectedCommunity.name,
 *        authorName = "Member A",  // Current user (anonymized)
 *        authorAvatar = "",
 *        timeAgo = "Baru saja",
 *        content = postContent.trim(),
 *        imageUrl = "",  // Future: Upload image first
 *        likeCount = 0,
 *        commentCount = 0,
 *        shareCount = 0
 *    )
 *
 * 5. Call callback:
 *    onPostCreated(newPost)
 *
 * 6. Close screen:
 *    onBackClick()
 *
 * ============================================================================
 * COMPONENTS
 * ============================================================================
 *
 * Header Row:
 *   - Back button (←)
 *   - Title: "Buat Post Baru"
 *   - Post button: Primary action (pink)
 *
 * Community Dropdown:
 *   - Label: "Pilih Komunitas"
 *   - Shows: community.name
 *   - Dropdown list: All joined communities
 *   - Required field (red * indicator)
 *
 * Content TextField:
 *   - Placeholder: "Tulis sesuatu..."
 *   - Multi-line: true
 *   - Min lines: 5
 *   - Max lines: 15
 *   - Character counter (future)
 *
 * Image Picker (Future):
 *   - Button: "+ Tambah Gambar"
 *   - Show preview after select
 *   - Remove button (X) on preview
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * // From CommunityScreen
 * var showCreatePost by remember { mutableStateOf(false) }
 *
 * if (showCreatePost) {
 *     val joinedCommunities = DummyCommunityData.getJoinedCommunities()
 *
 *     CreatePostScreen(
 *         joinedCommunities = joinedCommunities,
 *         onBackClick = {
 *             showCreatePost = false
 *         },
 *         onPostCreated = { newPost ->
 *             feedPosts.add(0, newPost)  // Add to top of feed
 *             showCreatePost = false
 *         }
 *     )
 * }
 *
 * // Trigger from header button
 * Button(
 *     onClick = { showCreatePost = true },
 *     text = "+ Buat Post"
 * )
 *
 * ============================================================================
 * STYLING
 * ============================================================================
 *
 * Background: White
 *
 * Header:
 *   - Height: 64.dp
 *   - Background: White
 *   - Elevation: 2.dp
 *   - Back icon: #333333, 24.dp
 *   - Title: Raleway SemiBold, 18.sp, #333333
 *   - Post button: Pink background, white text
 *
 * Community Dropdown:
 *   - Border: 1.dp, #DDDDDD
 *   - Corner radius: 8.dp
 *   - Padding: 12.dp
 *   - Selected: Raleway Medium, 14.sp
 *
 * Content TextField:
 *   - Border: 1.dp, #DDDDDD
 *   - Corner radius: 8.dp
 *   - Padding: 12.dp
 *   - Font: Raleway Regular, 14.sp
 *   - Placeholder: #999999
 *
 * ============================================================================
 * DIPAKAI DI
 * ============================================================================
 *
 * - CommunityScreen.kt (modal overlay)
 *
 * Triggered by:
 * - "+ Buat Post" button in CommunityPageHeader
 *
 * ============================================================================
 * FUTURE IMPROVEMENTS
 * ============================================================================
 *
 * - Image upload to Firebase Storage
 * - Multiple images support (carousel)
 * - Video upload
 * - Emoji picker
 * - Mention users (@Member A)
 * - Hashtags (#support)
 * - Post privacy (public/members only)
 * - Schedule post
 * - Save as draft
 * - Character limit (5000)
 * - Link preview
 * - Poll creation
 *
 * ============================================================================
 * FIREBASE INTEGRATION (FUTURE)
 * ============================================================================
 *
 * Upload Image:
 * 1. selectedImageUri → Firebase Storage
 * 2. Get download URL
 * 3. Use URL in Post.imageUrl
 *
 * Create Post:
 * ```
 * val postData = hashMapOf(
 *     "communityId" to selectedCommunity.id,
 *     "authorId" to currentUserId,
 *     "content" to postContent,
 *     "imageUrl" to imageUrl,
 *     "timestamp" to FieldValue.serverTimestamp(),
 *     "likeCount" to 0,
 *     "commentCount" to 0
 * )
 *
 * firestore.collection("posts")
 *     .add(postData)
 *     .addOnSuccessListener { ... }
 * ```
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * LAST UPDATE: 13 Dec 2024
 * STATUS: ✅ COMPLETE (Text only)
 * NEXT: Image upload feature
 * ============================================================================
 */