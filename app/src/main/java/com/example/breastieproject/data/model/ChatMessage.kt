package com.example.breastieproject.data.model

import java.util.concurrent.TimeUnit

data class ChatMessage(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val message: String = "",
    val timestamp: Long = 0
) {
    /**
     * Format timestamp jadi "time ago"
     * Examples: "Just now", "5m ago", "2h ago", "1d ago", "2w ago"
     */
    val timeAgo: String
        get() {
            val now = System.currentTimeMillis()
            val diff = now - timestamp

            return when {
                // < 1 menit
                diff < TimeUnit.MINUTES.toMillis(1) -> "Just now"

                // 1-59 menit
                diff < TimeUnit.HOURS.toMillis(1) -> {
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
                    "${minutes}m ago"
                }

                // 1-23 jam
                diff < TimeUnit.DAYS.toMillis(1) -> {
                    val hours = TimeUnit.MILLISECONDS.toHours(diff)
                    "${hours}h ago"
                }

                // 1-6 hari
                diff < TimeUnit.DAYS.toMillis(7) -> {
                    val days = TimeUnit.MILLISECONDS.toDays(diff)
                    "${days}d ago"
                }

                // 1-4 minggu
                diff < TimeUnit.DAYS.toMillis(30) -> {
                    val weeks = TimeUnit.MILLISECONDS.toDays(diff) / 7
                    "${weeks}w ago"
                }

                // 1-11 bulan
                diff < TimeUnit.DAYS.toMillis(365) -> {
                    val months = TimeUnit.MILLISECONDS.toDays(diff) / 30
                    "${months}mo ago"
                }

                // > 1 tahun
                else -> {
                    val years = TimeUnit.MILLISECONDS.toDays(diff) / 365
                    "${years}y ago"
                }
            }
        }

    /**
     * Format timestamp jadi jam lengkap (HH:mm)
     * Untuk fallback atau detail view
     */
    val formattedTime: String
        get() {
            val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
            return sdf.format(java.util.Date(timestamp))
        }
}

