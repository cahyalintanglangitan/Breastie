package com.example.breastieproject.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breastieproject.data.model.User
import com.example.breastieproject.ui.state.AuthState
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

sealed class UpdateState {
    object Idle : UpdateState()
    object Loading : UpdateState()
    object Success : UpdateState()
    data class Error(val message: String) : UpdateState()
}

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _updateState = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val updateState: StateFlow<UpdateState> = _updateState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        auth.currentUser?.uid?.let { uid ->
            loadUserData(uid)
        }
    }

    fun signUp(
        fullName: String,
        email: String,
        password: String,
        dateOfBirth: String = ""
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user
                    ?: throw IllegalStateException("User creation failed")

                val user = User(
                    userId = firebaseUser.uid,
                    fullName = fullName,
                    email = email,
                    anonymousName = generateAnonymousName(),
                    dateOfBirth = dateOfBirth.ifBlank { "" },
                    profilePhotoUrl = "",
                    createdAt = System.currentTimeMillis()
                )

                firestore.collection("users")
                    .document(user.userId)
                    .set(user)
                    .await()

                _currentUser.value = user
                _authState.value = AuthState.Success

            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user
                    ?: throw IllegalStateException("Sign in failed")

                loadUserData(firebaseUser.uid)
                _authState.value = AuthState.Success

            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    private fun loadUserData(userId: String) {
        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users")
                    .document(userId)
                    .get()
                    .await()

                _currentUser.value = snapshot.toObject(User::class.java)

            } catch (e: Exception) {
                _authState.value = AuthState.Error("Failed to load user data")
            }
        }
    }

    fun updateProfile(
        fullName: String,
        dateOfBirth: String,
        imageUri: Uri? = null
    ) {
        viewModelScope.launch {
            Log.d("BREASTIE_VM", "===== UPDATE PROFILE START =====")
            _updateState.value = UpdateState.Loading
            Log.d("BREASTIE_VM", "State = Loading")

            try {
                val userId = auth.currentUser?.uid
                    ?: throw IllegalStateException("User not logged in")

                Log.d("BREASTIE_VM", "User ID = $userId")

                val photoUrl = if (imageUri != null) {
                    Log.d("BREASTIE_VM", "Uploading photo...")
                    val url = uploadProfilePhoto(userId, imageUri)
                    Log.d("BREASTIE_VM", "Photo uploaded: $url")
                    url
                } else {
                    Log.d("BREASTIE_VM", "No photo")
                    _currentUser.value?.profilePhotoUrl ?: ""
                }

                val updates = mapOf(
                    "fullName" to fullName,
                    "dateOfBirth" to dateOfBirth,
                    "profilePhotoUrl" to photoUrl
                )

                Log.d("BREASTIE_VM", "Updating Firestore: $updates")

                firestore.collection("users")
                    .document(userId)
                    .update(updates)
                    .await()

                Log.d("BREASTIE_VM", "Firestore updated!")
                loadUserData(userId)
                Log.d("BREASTIE_VM", "User data reloaded!")

                _updateState.value = UpdateState.Success
                Log.d("BREASTIE_VM", "===== SUCCESS! State = ${_updateState.value} =====")

            } catch (e: Exception) {
                Log.e("BREASTIE_VM", "===== ERROR! =====", e)
                _updateState.value = UpdateState.Error(e.message ?: "Failed")
            }
        }
    }

    private suspend fun uploadProfilePhoto(userId: String, imageUri: Uri): String {
        return try {
            Log.d("UPLOAD", "Start upload")

            val filename = "profile_${UUID.randomUUID()}.jpg"
            val storageRef = storage.reference.child("users").child(userId).child(filename)

            Log.d("UPLOAD", "Path: users/$userId/$filename")

            val oldPhotoUrl = _currentUser.value?.profilePhotoUrl
            if (!oldPhotoUrl.isNullOrBlank()) {
                try {
                    storage.getReferenceFromUrl(oldPhotoUrl).delete().await()
                    Log.d("UPLOAD", "Old photo deleted")
                } catch (e: Exception) {
                    Log.d("UPLOAD", "Old photo delete failed")
                }
            }

            val uploadTask = storageRef.putFile(imageUri).await()
            Log.d("UPLOAD", "Upload done! ${uploadTask.totalByteCount} bytes")

            val downloadUrl = storageRef.downloadUrl.await().toString()
            Log.d("UPLOAD", "URL: $downloadUrl")

            downloadUrl

        } catch (e: Exception) {
            Log.e("UPLOAD", "Upload failed!", e)
            throw Exception("Failed to upload photo: ${e.message}")
        }
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading

            try {
                val user = auth.currentUser ?: throw IllegalStateException("Not logged in")
                val email = user.email ?: throw IllegalStateException("No email")

                val credential = EmailAuthProvider.getCredential(email, currentPassword)
                user.reauthenticate(credential).await()
                user.updatePassword(newPassword).await()

                _updateState.value = UpdateState.Success

            } catch (e: Exception) {
                val errorMsg = when {
                    e.message?.contains("password is invalid") == true -> "Wrong password"
                    e.message?.contains("WEAK_PASSWORD") == true -> "Weak password (min 6)"
                    else -> e.message ?: "Failed"
                }
                _updateState.value = UpdateState.Error(errorMsg)
            }
        }
    }

    fun signOut() {
        auth.signOut()
        _currentUser.value = null
        _authState.value = AuthState.Idle
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    fun resetUpdateState() {
        _updateState.value = UpdateState.Idle
        Log.d("BREASTIE_VM", "State reset to Idle")
    }

    private fun generateAnonymousName(): String {
        val adjectives = listOf("Brave", "Strong", "Hopeful", "Caring", "Gentle")
        val nouns = listOf("Warrior", "Fighter", "Survivor", "Guardian", "Phoenix")
        return "${adjectives.random()} ${nouns.random()}"
    }
}
