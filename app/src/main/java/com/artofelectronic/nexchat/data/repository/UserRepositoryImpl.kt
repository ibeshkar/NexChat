package com.artofelectronic.nexchat.data.repository

import android.net.Uri
import com.artofelectronic.nexchat.data.datasource.local.db.AppDatabase
import com.artofelectronic.nexchat.data.models.UserEntity
import com.artofelectronic.nexchat.domain.model.User
import com.artofelectronic.nexchat.domain.repository.UserRepository
import com.artofelectronic.nexchat.utils.userCollectionRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val appDB: AppDatabase,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) : UserRepository {

    override fun observeUsers(): Flow<List<User>> =
        appDB.userDao().observeUsers().map { it.map(UserEntity::toDomain) }

    override suspend fun fetchUsersOnceIfNeeded() {
        if (!appDB.userDao().hasUsers()) refreshUsersFromFirebase()
    }

    override suspend fun refreshUsersFromFirebase() {
        val snapshot = userCollectionRef().get().await()
        val userEntities = snapshot.mapNotNull { doc ->
            doc.toObject(User::class.java)
                .copy(userId = doc.id)
                .let(UserEntity::fromDomain)
        }

        appDB.userDao().insertAll(userEntities)
    }

    override suspend fun listenForUserChanges(): ListenerRegistration {
        return userCollectionRef().addSnapshotListener { snapshots, error ->
            if (error != null || snapshots == null) return@addSnapshotListener

            CoroutineScope(Dispatchers.IO).launch {
                for (change in snapshots.documentChanges) {
                    val user = change.document.toObject(User::class.java)
                        .copy(userId = change.document.id)

                    when (change.type) {
                        DocumentChange.Type.ADDED,
                        DocumentChange.Type.MODIFIED -> {
                            appDB.userDao().insert(UserEntity.fromDomain(user))
                        }

                        DocumentChange.Type.REMOVED -> {
                            appDB.userDao().deleteById(user.userId)
                        }
                    }
                }
            }
        }
    }

    override suspend fun uploadAvatar(userId: String, uri: Uri): String {
        val ref = storage.reference.child("avatars/$userId.jpg")
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    override suspend fun createOrUpdateUser(user: User) {
        appDB.userDao().insert(UserEntity.fromDomain(user))

        userCollectionRef()
            .document(user.userId)
            .set(user, SetOptions.merge())
            .await()
    }

    override suspend fun getUserProfile(userId: String): User? {
        var user = appDB.userDao().getUserById(userId)?.toDomain()
        if (user == null) {
            val snapshot = userCollectionRef().document(userId).get().await()
            user = snapshot.toObject(User::class.java)
            if (user != null)
                appDB.userDao().insert(UserEntity.fromDomain(user))
        }

        return user
    }

    override suspend fun signOut() {
        appDB.userDao().clear()
        appDB.chatDao().clear()
        appDB.messageDao().clear()
        appDB.pendingUpdateDao().clear()

        auth.signOut()
    }
}