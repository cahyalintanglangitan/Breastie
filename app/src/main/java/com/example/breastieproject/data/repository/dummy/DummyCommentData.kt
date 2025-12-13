package com.example.breastieproject.data.repository.dummy

import com.example.breastieproject.data.model.Comment

object DummyCommentData {

    // Dummy comments for post_001
    private val commentsPost001 = listOf(
        Comment(
            id = "comment_001",
            postId = "post_001",
            authorId = "user_201",
            authorUsername = "@anonim_user_5678",
            comment = "Semangat kak! Kamu pasti bisa melewati ini 💪",
            timestamp = System.currentTimeMillis() - (10 * 60 * 1000),
            createdAt = "10 mins ago"
        ),
        Comment(
            id = "comment_002",
            postId = "post_001",
            authorId = "user_202",
            authorUsername = "@anonim_user_1234",
            comment = "Aku juga sama kok kak, tapi alhamdulillah sekarang udah lebih baik. Keep fighting! ❤️",
            timestamp = System.currentTimeMillis() - (15 * 60 * 1000),
            createdAt = "15 mins ago"
        ),
        Comment(
            id = "comment_003",
            postId = "post_001",
            authorId = "user_203",
            authorUsername = "@anonim_user_9999",
            comment = "Makasih udah berbagi kak. Ini jadi penyemangat buat aku yang baru mulai treatment 🙏",
            timestamp = System.currentTimeMillis() - (25 * 60 * 1000),
            createdAt = "25 mins ago"
        ),
        Comment(
            id = "comment_004",
            postId = "post_001",
            authorId = "user_204",
            authorUsername = "@anonim_user_7777",
            comment = "Peluk dari jarak jauh kak 🤗",
            timestamp = System.currentTimeMillis() - (30 * 60 * 1000),
            createdAt = "30 mins ago"
        ),
        Comment(
            id = "comment_005",
            postId = "post_001",
            authorId = "user_205",
            authorUsername = "@anonim_user_3333",
            comment = "You're not alone! Kita semua ada buat support kamu 💖",
            timestamp = System.currentTimeMillis() - (45 * 60 * 1000),
            createdAt = "45 mins ago"
        )
    )

    // Dummy comments for post_003
    private val commentsPost003 = listOf(
        Comment(
            id = "comment_101",
            postId = "post_003",
            authorId = "user_301",
            authorUsername = "@anonim_user_4444",
            comment = "Coba makan makanan dingin kak, kayak es krim atau buah dingin. Aku kemarin gitu dan lumayan membantu",
            timestamp = System.currentTimeMillis() - (20 * 60 * 1000),
            createdAt = "20 mins ago"
        ),
        Comment(
            id = "comment_102",
            postId = "post_003",
            authorId = "user_302",
            authorUsername = "@anonim_user_8888",
            comment = "Air jahe + madu juga bagus loh kak. Atau coba permen jahe",
            timestamp = System.currentTimeMillis() - (35 * 60 * 1000),
            createdAt = "35 mins ago"
        ),
        Comment(
            id = "comment_103",
            postId = "post_003",
            authorId = "user_303",
            authorUsername = "@anonim_user_2222",
            comment = "Dokter ku dulu saranin makan sedikit tapi sering. Dan hindari makanan yang terlalu berminyak",
            timestamp = System.currentTimeMillis() - (50 * 60 * 1000),
            createdAt = "50 mins ago"
        )
    )

    // Map of post ID to comments
    private val commentsMap = mapOf(
        "post_001" to commentsPost001.toMutableList(),
        "post_003" to commentsPost003.toMutableList()
    )

    // Function: Get comments by post ID
    fun getCommentsByPost(postId: String): List<Comment> {
        return commentsMap[postId]?.sortedBy { it.timestamp } ?: emptyList()
    }

    // Function: Add comment (for demo)
    fun addComment(comment: Comment): Boolean {
        val postComments = commentsMap[comment.postId]?.toMutableList()
        return if (postComments != null) {
            postComments.add(comment)
            true
        } else {
            // Create new list for this post
            commentsMap.toMutableMap()[comment.postId] = mutableListOf(comment)
            true
        }
    }
}