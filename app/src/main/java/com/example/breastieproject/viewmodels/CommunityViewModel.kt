package com.example.breastieproject.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.breastieproject.data.model.Community
import com.example.breastieproject.data.model.Post
import com.example.breastieproject.data.model.Comment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.ListenerRegistration
import com.example.breastieproject.data.model.ChatMessage

/**
 * ============================================================================
 * COMMUNITY VIEW MODEL
 * ============================================================================
 * Handles all community, post, and comment operations with Firebase Firestore
 */
class CommunityViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // ==================== STATE FLOWS ====================

    // Communities
    private val _allCommunities = MutableStateFlow<List<Community>>(emptyList())
    val allCommunities: StateFlow<List<Community>> = _allCommunities.asStateFlow()

    private val _joinedCommunityIds = MutableStateFlow<List<String>>(emptyList())
    val joinedCommunityIds: StateFlow<List<String>> = _joinedCommunityIds.asStateFlow()

    //ChatMessage
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private var messagesListener: ListenerRegistration? = null

    // Posts
    private val _feedPosts = MutableStateFlow<List<Post>>(emptyList())
    val feedPosts: StateFlow<List<Post>> = _feedPosts.asStateFlow()

    //like
    private val _likedPostIds = MutableStateFlow<Set<String>>(emptySet())
    val likedPostIds: StateFlow<Set<String>> = _likedPostIds

    // Comments
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    // Loading states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // ✅ FIXED INIT BLOCK
    init {
        Log.d("COMMUNITY_VM", "ViewModel initialized")

        loadCommunities()
        loadUserJoinedCommunities()
        loadUserLikedPosts()

        // ✅ FIX: Auto-reload feed when joined communities change
        viewModelScope.launch {
            _joinedCommunityIds.collect { joinedIds ->
                Log.d("COMMUNITY_VM", "Joined communities changed: ${joinedIds.size} communities")
                if (joinedIds.isNotEmpty()) {
                    Log.d("COMMUNITY_VM", "Auto-reloading feed...")
                    loadFeedPosts()
                } else {
                    Log.d("COMMUNITY_VM", "No joined communities, clearing feed")
                    _feedPosts.value = emptyList()
                }
            }
        }
    }

    // ==================== COMMUNITY OPERATIONS ====================

    /**
     * Load all active communities from Firestore
     */
    fun loadCommunities() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("COMMUNITY_VM", "Loading communities...")

                val snapshot = firestore.collection("communities")
                    .whereEqualTo("isActive", true)
                    .orderBy("memberCount", Query.Direction.DESCENDING)
                    .get()
                    .await()

                val communities = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Community::class.java)?.copy(id = doc.id)
                }

                _allCommunities.value = communities
                Log.d("COMMUNITY_VM", "✅ Loaded ${communities.size} communities")

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "❌ Error loading communities", e)
                _errorMessage.value = "Failed to load communities: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Load communities that current user has joined
     */
    fun loadUserJoinedCommunities() {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid
                if (userId == null) {
                    Log.d("COMMUNITY_VM", "User not logged in, clearing joined communities")
                    _joinedCommunityIds.value = emptyList()
                    return@launch
                }

                Log.d("COMMUNITY_VM", "Loading joined communities for user: $userId")

                val snapshot = firestore.collection("community_members")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val joinedIds = snapshot.documents.mapNotNull { doc ->
                    doc.getString("communityId")
                }

                _joinedCommunityIds.value = joinedIds
                Log.d("COMMUNITY_VM", "✅ User joined ${joinedIds.size} communities: $joinedIds")

                // ✅ Feed will auto-reload via Flow collect in init

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "❌ Error loading joined communities", e)
                _joinedCommunityIds.value = emptyList()
            }
        }
    }

    /**
     * Join a community
     */
    fun joinCommunity(communityId: String) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: throw IllegalStateException("Not logged in")
                Log.d("COMMUNITY_VM", "Joining community: $communityId")

                // Add to community_members
                val memberData = hashMapOf(
                    "userId" to userId,
                    "communityId" to communityId,
                    "joinedAt" to System.currentTimeMillis()
                )

                firestore.collection("community_members")
                    .add(memberData)
                    .await()

                // ✅ Update local state (will trigger feed reload via Flow)
                _joinedCommunityIds.value = _joinedCommunityIds.value + communityId

                // Increment member count
                firestore.collection("communities")
                    .document(communityId)
                    .update("memberCount", com.google.firebase.firestore.FieldValue.increment(1))
                    .await()

                Log.d("COMMUNITY_VM", "✅ Joined community successfully!")

                // Reload communities to get updated count
                loadCommunities()

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "❌ Error joining community", e)
                _errorMessage.value = "Failed to join community: ${e.message}"
            }
        }
    }

    /**
     * Leave a community
     */
    fun leaveCommunity(communityId: String) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: throw IllegalStateException("Not logged in")
                Log.d("COMMUNITY_VM", "Leaving community: $communityId")

                // Remove from community_members
                val snapshot = firestore.collection("community_members")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("communityId", communityId)
                    .get()
                    .await()

                snapshot.documents.forEach { doc ->
                    doc.reference.delete().await()
                }

                // ✅ Update local state (will trigger feed reload via Flow)
                _joinedCommunityIds.value = _joinedCommunityIds.value - communityId

                // Decrement member count
                firestore.collection("communities")
                    .document(communityId)
                    .update("memberCount", com.google.firebase.firestore.FieldValue.increment(-1))
                    .await()

                Log.d("COMMUNITY_VM", "✅ Left community successfully!")

                // Reload communities
                loadCommunities()

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "❌ Error leaving community", e)
                _errorMessage.value = "Failed to leave community: ${e.message}"
            }
        }
    }

    /**
     * Load messages for a community (real-time)
     */
    fun loadMessages(communityId: String) {
        viewModelScope.launch {
            try {
                Log.d("COMMUNITY_VM", "Loading messages for community: $communityId")

                // Remove old listener if exists
                messagesListener?.remove()

                // Real-time listener
                messagesListener = firestore.collection("communities")
                    .document(communityId)
                    .collection("messages")
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            Log.e("COMMUNITY_VM", "Error listening to messages", error)
                            return@addSnapshotListener
                        }

                        val messagesList = snapshot?.documents?.mapNotNull { doc ->
                            doc.toObject(ChatMessage::class.java)?.copy(id = doc.id)
                        } ?: emptyList()

                        _messages.value = messagesList
                        Log.d("COMMUNITY_VM", "Loaded ${messagesList.size} messages")
                    }

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "Error loading messages", e)
            }
        }
    }

    /**
     * Send message to community
     */
    fun sendMessage(
        communityId: String,
        messageText: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser ?: throw IllegalStateException("Not logged in")
                val userId = currentUser.uid

                // Get user's anonymous name
                val userDoc = firestore.collection("users").document(userId).get().await()
                val anonymousName = userDoc.getString("anonymousName") ?: "Anonymous User"

                Log.d("COMMUNITY_VM", "Sending message to community: $communityId")

                val message = hashMapOf(
                    "userId" to userId,
                    "userName" to anonymousName,
                    "message" to messageText,
                    "timestamp" to System.currentTimeMillis()
                )

                firestore.collection("communities")
                    .document(communityId)
                    .collection("messages")
                    .add(message)
                    .await()

                Log.d("COMMUNITY_VM", "✅ Message sent!")
                onSuccess()

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "Error sending message", e)
                _errorMessage.value = "Failed to send message: ${e.message}"
            }
        }
    }

    /**
     * Stop listening to messages (cleanup)
     */
    fun stopListeningToMessages() {
        messagesListener?.remove()
        messagesListener = null
        _messages.value = emptyList()
    }

    // ==================== POST OPERATIONS ====================

    /**
     * Load posts for feed (ONLY from joined communities)
     */
    fun loadFeedPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userId = auth.currentUser?.uid
                if (userId == null) {
                    Log.d("COMMUNITY_VM", "User not logged in")
                    _feedPosts.value = emptyList()
                    _isLoading.value = false
                    return@launch
                }

                // Get user's joined communities
                val joinedIds = _joinedCommunityIds.value

                Log.d("COMMUNITY_VM", "Loading feed posts from ${joinedIds.size} communities: $joinedIds")

                if (joinedIds.isEmpty()) {
                    Log.d("COMMUNITY_VM", "No joined communities - empty feed")
                    _feedPosts.value = emptyList()
                    _isLoading.value = false
                    return@launch
                }

                // ✅ Load posts from joined communities
                val snapshot = firestore.collection("posts")
                    .whereIn("communityId", joinedIds)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(50)
                    .get()
                    .await()

                val posts = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Post::class.java)?.copy(id = doc.id)
                }

                _feedPosts.value = posts
                Log.d("COMMUNITY_VM", "✅ Loaded ${posts.size} feed posts")

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "❌ Error loading posts", e)
                _errorMessage.value = "Failed to load posts: ${e.message}"
                _feedPosts.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Create a new post
     */
    fun createPost(
        communityId: String,
        communityName: String,
        content: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val currentUser = auth.currentUser ?: return@launch
                val userId = currentUser.uid

                // Get user's anonymous name
                val userDoc = firestore.collection("users").document(userId).get().await()
                val anonymousName = userDoc.getString("anonymousName") ?: "Anonymous User"

                Log.d("COMMUNITY_VM", "Creating post in community: $communityName")

                val postData = hashMapOf(
                    "communityId" to communityId,
                    "communityName" to communityName,
                    "authorId" to userId,
                    "authorUsername" to anonymousName,
                    "content" to content,
                    "likes" to 0,
                    "commentCount" to 0,
                    "timestamp" to System.currentTimeMillis()
                )

                firestore.collection("posts")
                    .add(postData)
                    .await()

                Log.d("COMMUNITY_VM", "✅ Post created successfully!")

                // ✅ Reload feed (will show new post)
                loadFeedPosts()
                onSuccess()

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "❌ Error creating post", e)
                _errorMessage.value = "Failed to create post: ${e.message}"
            }
        }
    }

    /**
     * Load posts that current user has liked
     */
    fun loadUserLikedPosts() {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: return@launch
                Log.d("COMMUNITY_VM", "Loading liked posts for user: $userId")

                val snapshot = firestore.collection("post_likes")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val likedIds = snapshot.documents.mapNotNull {
                    it.getString("postId")
                }.toSet()

                _likedPostIds.value = likedIds
                Log.d("COMMUNITY_VM", "✅ Loaded ${likedIds.size} liked posts")

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "❌ Error loading liked posts", e)
            }
        }
    }

    /**
     * Like/unlike a post
     */
    fun toggleLikePost(postId: String) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: throw IllegalStateException("Not logged in")

                // Check if already liked
                val likeSnapshot = firestore.collection("post_likes")
                    .whereEqualTo("postId", postId)
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                if (likeSnapshot.isEmpty) {
                    // Like post
                    val likeData = hashMapOf(
                        "postId" to postId,
                        "userId" to userId,
                        "timestamp" to System.currentTimeMillis()
                    )

                    firestore.collection("post_likes")
                        .add(likeData)
                        .await()

                    // Increment likes
                    firestore.collection("posts")
                        .document(postId)
                        .update("likes", com.google.firebase.firestore.FieldValue.increment(1))
                        .await()

                    // Update local state
                    _likedPostIds.value = _likedPostIds.value + postId

                    Log.d("COMMUNITY_VM", "✅ Post liked!")
                } else {
                    // Unlike post
                    likeSnapshot.documents.forEach { doc ->
                        doc.reference.delete().await()
                    }

                    // Decrement likes
                    firestore.collection("posts")
                        .document(postId)
                        .update("likes", com.google.firebase.firestore.FieldValue.increment(-1))
                        .await()

                    // Update local state
                    _likedPostIds.value = _likedPostIds.value - postId

                    Log.d("COMMUNITY_VM", "✅ Post unliked!")
                }

                // Reload feed
                loadFeedPosts()

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "❌ Error toggling like", e)
            }
        }
    }

    /**
     * Delete own post
     */
    fun deletePost(postId: String) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: throw IllegalStateException("Not logged in")

                // Verify ownership
                val postSnapshot = firestore.collection("posts")
                    .document(postId)
                    .get()
                    .await()

                val authorId = postSnapshot.getString("authorId")
                if (authorId != userId) {
                    _errorMessage.value = "You can only delete your own posts"
                    return@launch
                }

                // Delete post
                firestore.collection("posts")
                    .document(postId)
                    .delete()
                    .await()

                Log.d("COMMUNITY_VM", "✅ Post deleted!")

                // Reload feed
                loadFeedPosts()

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "❌ Error deleting post", e)
                _errorMessage.value = "Failed to delete post: ${e.message}"
            }
        }
    }

    // ==================== COMMENT OPERATIONS ====================

    /**
     * Load comments for a post
     */
    fun loadComments(postId: String) {
        viewModelScope.launch {
            try {
                Log.d("COMMUNITY_VM", "Loading comments for post: $postId")

                val snapshot = firestore.collection("comments")
                    .whereEqualTo("postId", postId)
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .get()
                    .await()

                val comments = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Comment::class.java)?.copy(id = doc.id)
                }

                _comments.value = comments
                Log.d("COMMUNITY_VM", "✅ Loaded ${comments.size} comments")

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "❌ Error loading comments", e)
            }
        }
    }

    /**
     * Add a comment to a post
     */
    fun addComment(
        postId: String,
        commentText: String,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: throw IllegalStateException("Not logged in")

                // Get user's anonymous name
                val userSnapshot = firestore.collection("users")
                    .document(userId)
                    .get()
                    .await()

                val anonymousName = userSnapshot.getString("anonymousName") ?: "Anonymous User"

                Log.d("COMMUNITY_VM", "Adding comment...")

                val comment = hashMapOf(
                    "postId" to postId,
                    "authorId" to userId,
                    "authorUsername" to anonymousName,
                    "comment" to commentText,
                    "timestamp" to System.currentTimeMillis(),
                )

                firestore.collection("comments")
                    .add(comment)
                    .await()

                // Increment comment count on post
                firestore.collection("posts")
                    .document(postId)
                    .update("commentCount", com.google.firebase.firestore.FieldValue.increment(1))
                    .await()

                Log.d("COMMUNITY_VM", "✅ Comment added!")

                // Reload comments
                loadComments(postId)
                onSuccess()

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "❌ Error adding comment", e)
                _errorMessage.value = "Failed to add comment: ${e.message}"
            }
        }
    }

    /**
     * Delete own comment
     */
    fun deleteComment(commentId: String, postId: String) {
        viewModelScope.launch {
            try {
                val userId = auth.currentUser?.uid ?: throw IllegalStateException("Not logged in")

                // Verify ownership
                val commentSnapshot = firestore.collection("comments")
                    .document(commentId)
                    .get()
                    .await()

                val authorId = commentSnapshot.getString("authorId")
                if (authorId != userId) {
                    _errorMessage.value = "You can only delete your own comments"
                    return@launch
                }

                // Delete comment
                firestore.collection("comments")
                    .document(commentId)
                    .delete()
                    .await()

                // Decrement comment count
                firestore.collection("posts")
                    .document(postId)
                    .update("commentCount", com.google.firebase.firestore.FieldValue.increment(-1))
                    .await()

                Log.d("COMMUNITY_VM", "✅ Comment deleted!")

                // Reload comments
                loadComments(postId)

            } catch (e: Exception) {
                Log.e("COMMUNITY_VM", "❌ Error deleting comment", e)
                _errorMessage.value = "Failed to delete comment: ${e.message}"
            }
        }
    }

    // ==================== HELPER FUNCTIONS ====================

    /**
     * Get joined communities (filtered from all communities)
     */
    fun getJoinedCommunities(): List<Community> {
        return _allCommunities.value.filter { it.id in _joinedCommunityIds.value }
    }

    /**
     * Get available communities (not joined yet)
     */
    fun getAvailableCommunities(): List<Community> {
        return _allCommunities.value.filter { it.id !in _joinedCommunityIds.value }
    }

    /**
     * Clear error message
     */
    fun clearError() {
        _errorMessage.value = null
    }
}