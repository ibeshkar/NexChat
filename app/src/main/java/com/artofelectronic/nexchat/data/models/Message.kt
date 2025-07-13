package com.artofelectronic.nexchat.data.models

data class Message(
    val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    var isMine: Boolean = false,
    val text: String? = null,
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)