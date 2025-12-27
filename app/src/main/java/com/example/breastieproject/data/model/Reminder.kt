package com.example.breastieproject.data.model

import java.text.SimpleDateFormat
import java.util.*

data class Reminder(
    val id: String = "",
    val name: String = "",              // "Routine Checkup"
    val date: String = "",              // "25/12/2024" (display format)
    val dateTimestamp: Long = 0,        // For sorting & calculation
    val doctor: String = "",            // "Dr. Sarah"
    val createdAt: Long = 0,
    val isCompleted: Boolean = false
) {
    /**
     * Calculate days until this reminder
     * Returns: "H-7" or "H+2" (if passed) or "Today" (if today)
     *
     * ✅ FIXED: Now compares DATES, not exact timestamps
     */
    val daysUntil: String
        get() {
            // ✅ Get today at midnight (00:00:00)
            val todayCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // ✅ Get reminder date at midnight (00:00:00)
            val reminderCalendar = Calendar.getInstance().apply {
                timeInMillis = dateTimestamp
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // ✅ Calculate difference in days
            val todayMidnight = todayCalendar.timeInMillis
            val reminderMidnight = reminderCalendar.timeInMillis
            val diff = reminderMidnight - todayMidnight
            val days = (diff / (1000 * 60 * 60 * 24)).toInt()

            return when {
                days > 0 -> "H-$days"
                days < 0 -> "H+${-days} (Passed)"
                else -> "Today"
            }
        }

    /**
     * Check if reminder is upcoming (not passed yet)
     *
     * ✅ FIXED: Now compares DATES, not exact timestamps
     */
    val isUpcoming: Boolean
        get() {
            val todayCalendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            val reminderCalendar = Calendar.getInstance().apply {
                timeInMillis = dateTimestamp
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            return reminderCalendar.timeInMillis >= todayCalendar.timeInMillis
        }

    /**
     * Get formatted date with day name
     * Returns: "Monday, 25 Dec 2024"
     */
    val formattedDate: String
        get() {
            val sdf = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.ENGLISH)
            return sdf.format(Date(dateTimestamp))
        }
}

/**
 * Helper function to convert date string to timestamp at MIDNIGHT
 * Input: "25/12/2024" (dd/MM/yyyy)
 * Output: 1735084800000 (Dec 25, 2024 00:00:00)
 *
 * ✅ FIXED: Now sets time to midnight for accurate day comparison
 */
fun dateStringToTimestamp(dateString: String): Long {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = sdf.parse(dateString) ?: return 0

        // ✅ IMPORTANT: Set to midnight for accurate day comparison
        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        calendar.timeInMillis
    } catch (e: Exception) {
        0
    }
}