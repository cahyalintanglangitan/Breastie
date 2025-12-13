package com.example.breastieproject.data.model

data class Comment(
    val id: String = "",
    val postId: String = "",
    val authorId: String = "",
    val authorUsername: String = "",  // @anonim_user_8472
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val createdAt: String = ""  // "10 mins ago"
)

/**
 * ============================================================================
 * FILE: Comment.kt
 * LOCATION: data/model/Comment.kt
 * ============================================================================
 *
 * DESKRIPSI:
 * Data class untuk merepresentasikan komentar pada sebuah post.
 * User bisa memberikan komentar untuk support atau bertanya.
 *
 * ============================================================================
 * PROPERTIES
 * ============================================================================
 *
 * id: String - Unique identifier comment
 * postId: String - ID post yang dikomentari
 * authorName: String - Nama pemberi komentar (anonymized)
 * authorAvatar: String - Avatar pemberi komentar
 * timeAgo: String - Waktu relative ("5 menit yang lalu")
 * content: String - Isi komentar (text)
 * likeCount: Int - Jumlah likes pada comment (future feature)
 *
 * ============================================================================
 * CARA PAKAI
 * ============================================================================
 *
 * val comment = Comment(
 *     id = "comment_001",
 *     postId = "post_001",
 *     authorName = "Member B",
 *     authorAvatar = "",
 *     timeAgo = "5 menit yang lalu",
 *     content = "Semangat! Kamu pasti bisa!",
 *     likeCount = 3
 * )
 *
 * ============================================================================
 * DIPAKAI OLEH
 * ============================================================================
 *
 * - DummyCommentData.kt
 * - CommentScreen.kt
 * - CommentCard.kt (future component)
 *
 * ============================================================================
 * AUTHOR: Lintang
 * CREATED: 13 Dec 2024
 * ============================================================================
 */