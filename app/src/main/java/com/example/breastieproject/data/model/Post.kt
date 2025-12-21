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
    val timestamp: Long = 0
) {
    val createdAt: String
        get() = getTimeAgo(timestamp)

    private fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60_000 -> "Just now"
            diff < 3600_000 -> "${diff / 60_000}m ago"
            diff < 86400_000 -> "${diff / 3600_000}h ago"
            diff < 604800_000 -> "${diff / 86400_000}d ago"
            diff < 2592000_000 -> "${diff / 604800_000}w ago"
            else -> "${diff / 2592000_000}mo ago"
        }
    }
}