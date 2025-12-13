package com.example.breastieproject.data.repository.dummy

import com.example.breastieproject.data.model.Post

object DummyPostData {

    val posts = listOf(
        Post(
            id = "post_001",
            communityId = "comm_001",
            communityName = "Pejuang Kemo",
            authorId = "user_456",
            authorUsername = "@anonim_user_8472",
            content = "Hari ini aku merasa lebih baik setelah sesi kemo kemarin. Terima kasih untuk semua dukungannya! 💪",
            likes = 45,
            commentCount = 12,
            timestamp = System.currentTimeMillis() - (2 * 60 * 60 * 1000), // 2 hours ago
            createdAt = "2 hours ago"
        ),
        Post(
            id = "post_002",
            communityId = "comm_002",
            communityName = "Survivor Stories",
            authorId = "user_789",
            authorUsername = "@anonim_user_9123",
            content = "Selamat pagi semua! Hari ini adalah hari ke-365 sejak remisi ku. Terima kasih untuk semua yang sudah support ❤️🎉",
            likes = 134,
            commentCount = 45,
            timestamp = System.currentTimeMillis() - (5 * 60 * 60 * 1000),
            createdAt = "5 hours ago"
        ),
        Post(
            id = "post_003",
            communityId = "comm_001",
            communityName = "Pejuang Kemo",
            authorId = "user_234",
            authorUsername = "@anonim_user_3456",
            content = "Ada yang punya tips untuk mengatasi mual setelah kemo? Sharing dong 🙏",
            likes = 67,
            commentCount = 23,
            timestamp = System.currentTimeMillis() - (8 * 60 * 60 * 1000),
            createdAt = "8 hours ago"
        ),
        Post(
            id = "post_004",
            communityId = "comm_003",
            communityName = "Healing Together",
            authorId = "user_567",
            authorUsername = "@anonim_user_5678",
            content = "Meditasi pagi hari benar-benar membantu aku merasa lebih tenang. Highly recommended! 🧘‍♀️✨",
            likes = 52,
            commentCount = 15,
            timestamp = System.currentTimeMillis() - (1 * 24 * 60 * 60 * 1000),
            createdAt = "1 day ago"
        ),
        Post(
            id = "post_005",
            communityId = "comm_002",
            communityName = "Survivor Stories",
            authorId = "user_890",
            authorUsername = "@anonim_user_7890",
            content = "Buat yang baru mulai perjalanan, jangan menyerah! Aku dulu juga sempat down, tapi sekarang alhamdulillah sudah jauh lebih baik. You got this! 💪💖",
            likes = 89,
            commentCount = 28,
            timestamp = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000),
            createdAt = "2 days ago"
        ),
        Post(
            id = "post_006",
            communityId = "comm_003",
            communityName = "Healing Together",
            authorId = "user_321",
            authorUsername = "@anonim_user_4321",
            content = "Sharing playlist lagu-lagu yang bikin aku tenang. Ada yang mau? Drop komen ya!",
            likes = 32,
            commentCount = 19,
            timestamp = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000),
            createdAt = "3 days ago"
        ),
        Post(
            id = "post_007",
            communityId = "comm_001",
            communityName = "Pejuang Kemo",
            authorId = "user_654",
            authorUsername = "@anonim_user_6543",
            content = "Dokter bilang hasil lab ku membaik! Seneng banget rasanya. Terima kasih sudah jadi support system yang luar biasa ❤️",
            likes = 156,
            commentCount = 52,
            timestamp = System.currentTimeMillis() - (4 * 24 * 60 * 60 * 1000),
            createdAt = "4 days ago"
        ),
        Post(
            id = "post_008",
            communityId = "comm_003",
            communityName = "Healing Together",
            authorId = "user_987",
            authorUsername = "@anonim_user_9876",
            content = "Ada yang pernah coba aromaterapi? Aku pengen nyoba tapi masih ragu. Share pengalaman kalian dong!",
            likes = 41,
            commentCount = 16,
            timestamp = System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000),
            createdAt = "5 days ago"
        )
    )

    // Function: Get posts sorted by timestamp (newest first)
    fun getFeedPosts(): List<Post> {
        return posts.sortedByDescending { it.timestamp }
    }
}