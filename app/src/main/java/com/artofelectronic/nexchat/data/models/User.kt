package com.artofelectronic.nexchat.data.models

import com.google.firebase.Timestamp

data class User(
    val uid: String,
    val email: String,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val createdAt: Timestamp = Timestamp.now()
)
