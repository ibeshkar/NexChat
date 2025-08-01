package com.artofelectronic.nexchat.domain.model

data class User(
    val userId: String = "",
    val displayName: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val bio: String? = null
)
