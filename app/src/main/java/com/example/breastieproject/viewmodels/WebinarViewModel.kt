package com.example.breastieproject.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breastieproject.data.model.WebinarEvent
import com.example.breastieproject.data.model.WebinarRegistration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WebinarViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _currentWebinar = MutableStateFlow<WebinarEvent?>(null)
    val currentWebinar: StateFlow<WebinarEvent?> = _currentWebinar

    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadCurrentWebinar()
        checkIfRegistered()
    }

    /**
     * Load current active webinar from Firestore
     */
    fun loadCurrentWebinar() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("WEBINAR_VM", "===== LOADING WEBINAR =====")
                Log.d("WEBINAR_VM", "Firestore instance: ${firestore != null}")

                val snapshot = firestore.collection("webinars")
                    .whereEqualTo("isActive", true)
                    .orderBy("date")
                    .limit(1)
                    .get()
                    .await()

                Log.d("WEBINAR_VM", "Query executed!")
                Log.d("WEBINAR_VM", "Documents count: ${snapshot.documents.size}")

                if (snapshot.documents.isEmpty()) {
                    Log.e("WEBINAR_VM", "❌ NO WEBINARS FOUND!")
                    Log.e("WEBINAR_VM", "Check Firestore: does 'webinars' collection exist?")
                    Log.e("WEBINAR_VM", "Check: does document have isActive = true?")
                }

                snapshot.documents.forEach { doc ->
                    Log.d("WEBINAR_VM", "Document ID: ${doc.id}")
                    Log.d("WEBINAR_VM", "Document data: ${doc.data}")
                }

                val webinar = snapshot.documents.firstOrNull()?.let { doc ->
                    doc.toObject(WebinarEvent::class.java)?.copy(id = doc.id)
                }

                _currentWebinar.value = webinar
                Log.d("WEBINAR_VM", "Loaded webinar: ${webinar?.title}")

                if (webinar == null) {
                    Log.e("WEBINAR_VM", "❌ Webinar object is NULL!")
                    Log.e("WEBINAR_VM", "Check: do field names match WebinarEvent.kt?")
                }

            } catch (e: Exception) {
                Log.e("WEBINAR_VM", "❌ ERROR loading webinar!", e)
                Log.e("WEBINAR_VM", "Error message: ${e.message}")
                Log.e("WEBINAR_VM", "Error type: ${e.javaClass.simpleName}")
                e.printStackTrace()
                _errorMessage.value = "Failed to load webinar: ${e.message}"
            } finally {
                _isLoading.value = false
                Log.d("WEBINAR_VM", "Loading finished")
            }
        }
    }

    /**
     * Check if current user already registered
     */
    fun checkIfRegistered() {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: return@launch
                val webinarId = _currentWebinar.value?.id ?: return@launch

                val snapshot = firestore.collection("webinar_registrations")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("webinarId", webinarId)
                    .whereEqualTo("status", "registered")
                    .get()
                    .await()

                _isRegistered.value = snapshot.documents.isNotEmpty()

            } catch (e: Exception) {
                Log.e("WEBINAR_VM", "Error checking registration", e)
            }
        }
    }

    /**
     * Register user for webinar (auto-use logged-in email)
     */
    fun registerForWebinar(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser ?: throw IllegalStateException("Not logged in")
                val userId = currentUser.uid
                val userEmail = currentUser.email ?: throw IllegalStateException("No email")

                val webinarId = _currentWebinar.value?.id
                    ?: throw IllegalStateException("No active webinar")

                Log.d("WEBINAR_VM", "Registering user: $userEmail for webinar: $webinarId")

                // Create registration
                val registration = hashMapOf(
                    "userId" to userId,
                    "webinarId" to webinarId,
                    "userEmail" to userEmail,
                    "registeredAt" to System.currentTimeMillis(),
                    "status" to "registered"
                )

                firestore.collection("webinar_registrations")
                    .add(registration)
                    .await()

                _isRegistered.value = true
                Log.d("WEBINAR_VM", "✅ Registration successful!")

                onSuccess()

            } catch (e: Exception) {
                Log.e("WEBINAR_VM", "Error registering", e)
                _errorMessage.value = "Failed to register: ${e.message}"
            }
        }
    }

    /**
     * Cancel registration
     */
    fun cancelRegistration() {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: return@launch
                val webinarId = _currentWebinar.value?.id ?: return@launch

                val snapshot = firestore.collection("webinar_registrations")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("webinarId", webinarId)
                    .get()
                    .await()

                snapshot.documents.forEach { doc ->
                    doc.reference.update("status", "cancelled").await()
                }

                _isRegistered.value = false
                Log.d("WEBINAR_VM", "Registration cancelled")

            } catch (e: Exception) {
                Log.e("WEBINAR_VM", "Error cancelling registration", e)
            }
        }
    }
}