package com.artofelectronic.nexchat.data.repository

import com.artofelectronic.nexchat.data.models.User
import com.artofelectronic.nexchat.domain.repository.UserRepository
import com.artofelectronic.nexchat.utils.COLLECTION_USERS
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {
    override suspend fun createUserIfNotExists(user: User) {
        val userDocRef = firestore.collection(COLLECTION_USERS).document(user.id)
        val snapshot = userDocRef.get().await()
        if (!snapshot.exists()) {
            userDocRef.set(user).await()
        }
    }

    override suspend fun getAllUsers(): List<User> {
        return firestore.collection(COLLECTION_USERS)
            .get()
            .await()
            .documents.mapNotNull {
                it.toObject(User::class.java)
            }
    }
}