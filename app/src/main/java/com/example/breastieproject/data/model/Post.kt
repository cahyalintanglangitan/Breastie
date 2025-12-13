package com.example.breastieproject.data.model

data class Post(
    val id: String = "",
    val communityId: String = "",
    val communityName: String = "",
    val authorId: String = "",
    val authorUsername: String = "",
    val content: String = "",
    val likes: Int = 0,
    val commentCount: Int = 0,
    val timestamp: Long = 0,
    val createdAt: String = ""
)

/**
 * ============================================================================
 * FILE: Post.kt
 * LOCATION: data/model/Post.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Data class untuk merepresentasikan postingan di community feed.
 * User bisa membuat post untuk berbagi cerita, bertanya, atau support.
 *
 * ============================================================================
 * PROPERTIES
 * ============================================================================
 *
 * id: String
 *   - Unique identifier post (e.g., "post_001")
 *   - Digunakan untuk like, comment, share tracking
 *
 * communityId: String
 *   - ID komunitas tempat post dibuat
 *   - Linking ke Community model
 *   - Format: "comm_XXX"
 *
 * communityName: String
 *   - Nama komunitas (denormalized untuk performance)
 *   - Ditampilkan di post card header
 *
 * authorName: String
 *   - Nama pembuat post (anonymized: "Member A", "Member B")
 *   - Privacy: Gak pakai real name
 *
 * authorAvatar: String
 *   - Avatar penulis (URL atau empty untuk placeholder)
 *   - Empty = pink circle placeholder
 *
 * timeAgo: String
 *   - Waktu relative posting ("2 jam yang lalu", "1 hari yang lalu")
 *   - Format human-readable
 *   - Future: Calculate from timestamp
 *
 * content: String
 *   - Isi konten post (text)
 *   - Support multi-line, emoji
 *   - Max length: 5000 karakter (recommended)
 *
 * imageUrl: String
 *   - Optional image attachment
 *   - Empty = no image
 *   - Future: Firebase Storage URL
 *
 * likeCount: Int
 *   - Jumlah likes
 *   - Real-time update saat ada yang like/unlike
 *
 * commentCount: Int
 *   - Jumlah comments
 *   - Auto-increment saat comment ditambahkan
 *
 * shareCount: Int
 *   - Jumlah shares
 *   - Future feature (belum implemented)
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * // Create new post
 * val newPost = Post(
 *     id = "post_${System.currentTimeMillis()}",
 *     communityId = "comm_001",
 *     communityName = "Pejuang Kemo",
 *     authorName = "Member A",
 *     authorAvatar = "",
 *     timeAgo = "Baru saja",
 *     content = "Hari ini saya selesai sesi kemo ke-3...",
 *     imageUrl = "",
 *     likeCount = 0,
 *     commentCount = 0,
 *     shareCount = 0
 * )
 *
 * // Update like count
 * val likedPost = newPost.copy(likeCount = newPost.likeCount + 1)
 *
 * ============================================================================
 * DIPAKAI OLEH
 * ============================================================================
 *
 * - DummyPostData.kt (data source)
 * - FeedTab.kt (display posts list)
 * - PostCard.kt (UI component)
 * - CreatePostScreen.kt (create new post)
 * - CommentScreen.kt (show post detail)
 *
 * ============================================================================
 * FIREBASE MAPPING (FUTURE)
 * ============================================================================
 *
 * Collection: "posts"
 * Document ID: post.id
 *
 * {
 *   "id": "post_001",
 *   "communityId": "comm_001",
 *   "communityName": "Pejuang Kemo",
 *   "authorId": "user_123",  // NEW!
 *   "authorName": "Member A",
 *   "authorAvatar": "",
 *   "timestamp": Timestamp,
 *   "content": "...",
 *   "imageUrl": "",
 *   "likeCount": 0,
 *   "commentCount": 0,
 *   "shareCount": 0,
 *   "likedBy": ["user_123", "user_456"],  // NEW!
 *   "isEdited": false,
 *   "isDeleted": false
 * }
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * LAST UPDATE: 13 Dec 2024
 * ============================================================================
 */

