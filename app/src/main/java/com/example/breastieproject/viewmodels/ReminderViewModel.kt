package com.example.breastieproject.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breastieproject.data.model.Reminder
import com.example.breastieproject.data.model.dateStringToTimestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ReminderViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // All reminders
    private val _reminders = MutableStateFlow<List<Reminder>>(emptyList())
    val reminders: StateFlow<List<Reminder>> = _reminders

    // Nearest upcoming reminder (for HomeScreen card)
    private val _nearestReminder = MutableStateFlow<Reminder?>(null)
    val nearestReminder: StateFlow<Reminder?> = _nearestReminder

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadReminders()
    }

    /**
     * Load all reminders from Firestore
     */
    fun loadReminders() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = auth.currentUser?.uid
                if (userId == null) {
                    Log.d("REMINDER_VM", "User not logged in")
                    _reminders.value = emptyList()
                    _nearestReminder.value = null
                    return@launch
                }

                Log.d("REMINDER_VM", "Loading reminders for user: $userId")

                val snapshot = firestore.collection("reminders")
                    .document(userId)
                    .collection("schedules")
                    .orderBy("dateTimestamp", Query.Direction.ASCENDING)
                    .get()
                    .await()

                val reminderList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Reminder::class.java)?.copy(id = doc.id)
                }

                _reminders.value = reminderList

                // Find nearest upcoming reminder
                _nearestReminder.value = reminderList
                    .filter { it.isUpcoming && !it.isCompleted }
                    .minByOrNull { it.dateTimestamp }

                Log.d("REMINDER_VM", "Loaded ${reminderList.size} reminders")
                Log.d("REMINDER_VM", "Nearest reminder: ${_nearestReminder.value?.name}")

            } catch (e: Exception) {
                Log.e("REMINDER_VM", "Error loading reminders", e)
                _errorMessage.value = "Failed to load your schedule: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Add new reminder
     */
    fun addReminder(
        name: String,
        date: String,
        doctor: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid
                    ?: throw IllegalStateException("User not logged in")

                Log.d("REMINDER_VM", "Adding reminder: $name on $date")

                // Convert date string to timestamp
                val timestamp = dateStringToTimestamp(date)
                if (timestamp == 0L) {
                    _errorMessage.value = "Invalid date format"
                    return@launch
                }

                val reminder = Reminder(
                    id = "",
                    name = name,
                    date = date,
                    dateTimestamp = timestamp,
                    doctor = doctor,
                    createdAt = System.currentTimeMillis(),
                    isCompleted = false
                )

                // Save to Firestore
                val docRef = firestore.collection("reminders")
                    .document(userId)
                    .collection("schedules")
                    .add(reminder)
                    .await()

                Log.d("REMINDER_VM", "Reminder added with ID: ${docRef.id}")

                // Update local state
                val newReminder = reminder.copy(id = docRef.id)
                _reminders.value = (_reminders.value + newReminder)
                    .sortedBy { it.dateTimestamp }

                // Recalculate nearest reminder
                updateNearestReminder()

                onSuccess()

            } catch (e: Exception) {
                Log.e("REMINDER_VM", "Error adding reminder", e)
                _errorMessage.value = "Failed to add reminder: ${e.message}"
            }
        }
    }

    /**
     * Delete reminder
     */
    fun deleteReminder(reminderId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid
                    ?: throw IllegalStateException("User not logged in")

                Log.d("REMINDER_VM", "Deleting reminder: $reminderId")

                // Delete from Firestore
                firestore.collection("reminders")
                    .document(userId)
                    .collection("schedules")
                    .document(reminderId)
                    .delete()
                    .await()

                // Update local state
                _reminders.value = _reminders.value.filter { it.id != reminderId }

                // Recalculate nearest reminder
                updateNearestReminder()

                Log.d("REMINDER_VM", "Reminder deleted successfully")
                onSuccess()

            } catch (e: Exception) {
                Log.e("REMINDER_VM", "Error deleting reminder", e)
                _errorMessage.value = "Failed to delete reminder: ${e.message}"
            }
        }
    }

    /**
     * Mark reminder as completed
     */
    fun markAsCompleted(reminderId: String) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: return@launch

                firestore.collection("reminders")
                    .document(userId)
                    .collection("schedules")
                    .document(reminderId)
                    .update("isCompleted", true)
                    .await()

                // Update local state
                _reminders.value = _reminders.value.map { reminder ->
                    if (reminder.id == reminderId) {
                        reminder.copy(isCompleted = true)
                    } else {
                        reminder
                    }
                }

                // Recalculate nearest reminder
                updateNearestReminder()

                Log.d("REMINDER_VM", "Reminder marked as completed")

            } catch (e: Exception) {
                Log.e("REMINDER_VM", "Error marking as completed", e)
            }
        }
    }

    /**
     * Update nearest upcoming reminder
     */
    private fun updateNearestReminder() {
        _nearestReminder.value = _reminders.value
            .filter { it.isUpcoming && !it.isCompleted }
            .minByOrNull { it.dateTimestamp }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}