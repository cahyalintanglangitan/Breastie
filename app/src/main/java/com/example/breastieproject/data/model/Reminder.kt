package com.example.breastieproject.data.model

data class Reminder(
    val id: String = "",
    val name: String = "",              // "Pemeriksaan Rutin"
    val date: String = "",              // "25/12/2024" (display format)
    val dateTimestamp: Long = 0,        // For sorting & calculation
    val doctor: String = "",            // "Dr. Sarah"
    val createdAt: Long = 0,
    val isCompleted: Boolean = false
) {
    /**
     * Calculate days until this reminder
     * Returns: "H-7" or "H+2" (if passed) or "Hari Ini" (if today)
     */
    val daysUntil: String
        get() {
            val today = System.currentTimeMillis()
            val diff = dateTimestamp - today
            val days = (diff / (1000 * 60 * 60 * 24)).toInt()

            return when {
                days > 0 -> "H-$days"
                days < 0 -> "H+${-days} (Terlewat)"
                else -> "Hari Ini"
            }
        }

    /**
     * Check if reminder is upcoming (not passed yet)
     */
    val isUpcoming: Boolean
        get() = dateTimestamp >= System.currentTimeMillis()

    /**
     * Get formatted date with day name
     * Returns: "Senin, 25 Des 2024"
     */
    val formattedDate: String
        get() {
            val sdf = java.text.SimpleDateFormat("EEEE, dd MMM yyyy", java.util.Locale("id", "ID"))
            return sdf.format(java.util.Date(dateTimestamp))
        }
}

/**
 * Helper function to convert date string to timestamp
 * Input: "25/12/2024" (dd/MM/yyyy)
 * Output: 1735084800000
 */
fun dateStringToTimestamp(dateString: String): Long {
    return try {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        sdf.parse(dateString)?.time ?: 0
    } catch (e: Exception) {
        0
    }
}