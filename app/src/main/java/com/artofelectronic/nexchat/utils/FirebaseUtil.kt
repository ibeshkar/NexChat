package com.artofelectronic.nexchat.utils

import com.artofelectronic.nexchat.domain.model.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


fun userCollectionRef() = FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
fun chatCollectionRef() = FirebaseFirestore.getInstance().collection(COLLECTION_CHATS)


fun FirebaseUser.toDomain(): User {
    return User(
        userId = uid,
        email = email ?: "",
        displayName = displayName ?: email?.substringBefore("@").orEmpty(),
        avatarUrl = photoUrl?.toString() ?: ""
    )
}

fun generateChatId(user1: String?, user2: String?): String? {
    if (user1.isNullOrEmpty() || user2.isNullOrEmpty()) return null
    return listOf(user1, user2).sorted().joinToString("_")
}