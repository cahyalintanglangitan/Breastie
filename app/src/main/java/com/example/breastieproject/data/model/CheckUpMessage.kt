package com.example.breastieproject.data.model

data class CheckUpMessage(
    val id: String = "",
    val role: String = "", // "user" or "assistant"
    val content: String = "",
    val timestamp: Long = 0,
    val recommendation: String = "", // Only for assistant messages
    val urgency: String = "" // "low", "medium", "high"
) {
    val timeAgo: String
        get() = getTimeAgo(timestamp)

    private fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60_000 -> "Baru saja"
            diff < 3600_000 -> "${diff / 60_000}m yang lalu"
            diff < 86400_000 -> "${diff / 3600_000}j yang lalu"
            else -> {
                val formatter = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                formatter.format(java.util.Date(timestamp))
            }
        }
    }
}