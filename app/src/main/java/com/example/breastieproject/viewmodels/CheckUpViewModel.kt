package com.example.breastieproject.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breastieproject.data.model.CheckUpFAQ
import com.example.breastieproject.data.model.CheckUpMessage
import com.example.breastieproject.data.repository.GroqApiRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CheckUpViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val groqApi = GroqApiRepository()

    private val _messages = MutableStateFlow<List<CheckUpMessage>>(emptyList())
    val messages: StateFlow<List<CheckUpMessage>> = _messages

    private val _faqs = MutableStateFlow<List<CheckUpFAQ>>(emptyList())
    val faqs: StateFlow<List<CheckUpFAQ>> = _faqs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadFAQs()
        loadChatHistory()
    }

    /**
     * Load FAQs from Firestore
     */
    fun loadFAQs() {
        viewModelScope.launch {
            try {
                Log.d("CHECKUP_VM", "Loading FAQs...")

                val snapshot = firestore.collection("faqs")
                    .whereEqualTo("isActive", true)
                    .orderBy("order", Query.Direction.ASCENDING)
                    .get()
                    .await()

                val faqList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(CheckUpFAQ::class.java)?.copy(id = doc.id)
                }

                _faqs.value = faqList
                Log.d("CHECKUP_VM", "Loaded ${faqList.size} FAQs")

            } catch (e: Exception) {
                Log.e("CHECKUP_VM", "Error loading FAQs", e)
                // Fallback to hardcoded FAQs
                _faqs.value = getDefaultFAQs()
            }
        }
    }

    /**
     * Load chat history from Firestore
     */
    private fun loadChatHistory() {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: return@launch
                Log.d("CHECKUP_VM", "Loading chat history for user: $userId")

                val snapshot = firestore.collection("chat_sessions")
                    .document(userId)
                    .collection("messages")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .limit(50) // Last 50 messages
                    .get()
                    .await()

                val messageList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(CheckUpMessage::class.java)?.copy(id = doc.id)
                }

                _messages.value = messageList
                Log.d("CHECKUP_VM", "Loaded ${messageList.size} messages")

            } catch (e: Exception) {
                Log.e("CHECKUP_VM", "Error loading chat history", e)
            }
        }
    }

    /**
     * Send message and get AI response
     */
    fun sendMessage(messageText: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val userId = auth.currentUser?.uid ?: throw IllegalStateException("Not logged in")
                Log.d("CHECKUP_VM", "Sending message: $messageText")

                // 1. Create user message
                val userMessage = CheckUpMessage(
                    id = "",
                    role = "user",
                    content = messageText,
                    timestamp = System.currentTimeMillis()
                )

                // 2. Save user message to Firestore
                val userMessageRef = firestore.collection("chat_sessions")
                    .document(userId)
                    .collection("messages")
                    .add(userMessage)
                    .await()
                // Update chat session metadata (user message)
                firestore.collection("chat_sessions")
                    .document(userId)
                    .set(
                        mapOf(
                            "lastMessageAt" to System.currentTimeMillis(),
                            "createdAt" to System.currentTimeMillis()
                        ),
                        SetOptions.merge()
                    )


                // 3. Add to local state
                _messages.value = _messages.value + userMessage.copy(id = userMessageRef.id)

                // 4. Get conversation history for context
                val history = _messages.value
                    .takeLast(10) // Last 10 messages for context
                    .map { it.role to it.content }

                // 5. Call Groq API
                val result = groqApi.getChatResponse(messageText, history)

                if (result.isSuccess) {
                    val (aiContent, urgency) = result.getOrThrow()

                    // 6. Create AI message
                    val aiMessage = CheckUpMessage(
                        id = "",
                        role = "assistant",
                        content = aiContent,
                        timestamp = System.currentTimeMillis(),
                        recommendation = extractRecommendation(aiContent),
                        urgency = urgency
                    )

                    // 7. Save AI message to Firestore
                    val aiMessageRef = firestore.collection("chat_sessions")
                        .document(userId)
                        .collection("messages")
                        .add(aiMessage)
                        .await()

                    // 8. Add to local state
                    _messages.value = _messages.value + aiMessage.copy(id = aiMessageRef.id)

                    Log.d("CHECKUP_VM", "AI response received with urgency: $urgency")

                } else {
                    throw result.exceptionOrNull() ?: Exception("Unknown error")
                }

            } catch (e: Exception) {
                Log.e("CHECKUP_VM", "Error sending message", e)
                _errorMessage.value = "Gagal mengirim pesan: ${e.message}"

                // Add error message to chat
                val errorMessage = CheckUpMessage(
                    id = "error_${System.currentTimeMillis()}",
                    role = "assistant",
                    content = "Maaf, terjadi kesalahan. Silakan coba lagi.",
                    timestamp = System.currentTimeMillis(),
                    urgency = "low"
                )
                _messages.value = _messages.value + errorMessage

            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Extract recommendation from AI response
     */
    private fun extractRecommendation(content: String): String {
        // Look for recommendation keywords
        val lines = content.split("\\n")
        val recLine = lines.find {
            it.contains("REKOMENDASI", ignoreCase = true) ||
                    it.contains("Sebaiknya", ignoreCase = true) ||
                    it.contains("Saran", ignoreCase = true)
        }
        return recLine?.trim() ?: ""
    }

    /**
     * Clear chat history
     */
    fun clearChat() {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: return@launch

                val snapshot = firestore.collection("chat_sessions")
                    .document(userId)
                    .collection("messages")
                    .get()
                    .await()

                snapshot.documents.forEach { doc ->
                    doc.reference.delete()
                }

                _messages.value = emptyList()
                Log.d("CHECKUP_VM", "Chat cleared")

            } catch (e: Exception) {
                Log.e("CHECKUP_VM", "Error clearing chat", e)
            }
        }
    }

    /**
     * Default FAQs (fallback if Firestore fails)
     */
    private fun getDefaultFAQs(): List<CheckUpFAQ> {
        return listOf(
            CheckUpFAQ(
                id = "faq1",
                question = "Apa gejala awal kanker payudara?",
                category = "symptoms",
                order = 1,
                isActive = true
            ),
            CheckUpFAQ(
                id = "faq2",
                question = "Kapan saya harus segera ke dokter?",
                category = "urgent",
                order = 2,
                isActive = true
            ),
            CheckUpFAQ(
                id = "faq3",
                question = "Bagaimana cara deteksi dini?",
                category = "prevention",
                order = 3,
                isActive = true
            ),
            CheckUpFAQ(
                id = "faq4",
                question = "Apakah benjolan selalu berarti kanker?",
                category = "symptoms",
                order = 4,
                isActive = true
            ),
            CheckUpFAQ(
                id = "faq5",
                question = "Apa faktor risiko kanker payudara?",
                category = "risk",
                order = 5,
                isActive = true
            )
        )
    }

    fun clearError() {
        _errorMessage.value = null
    }
}