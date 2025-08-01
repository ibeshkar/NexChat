package com.artofelectronic.nexchat.domain.model

import com.google.firebase.Timestamp

data class Message(
    val messageId: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val text: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val delivered: Boolean = false,
    val isRead: Boolean = false
)