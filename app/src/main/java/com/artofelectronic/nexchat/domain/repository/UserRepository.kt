package com.artofelectronic.nexchat.domain.repository

import android.net.Uri
import com.artofelectronic.nexchat.domain.model.User
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUsers(): Flow<List<User>>
    suspend fun fetchUsersOnceIfNeeded()
    suspend fun refreshUsersFromFirebase()
    suspend fun listenForUserChanges(): ListenerRegistration
    suspend fun uploadAvatar(userId: String, uri: Uri): String
    suspend fun createOrUpdateUser(user: User)
    suspend fun getUserProfile(userId: String): User?
    suspend fun signOut()
}