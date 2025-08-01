package com.artofelectronic.nexchat.domain.model

import com.google.firebase.Timestamp

data class Chat(
    val chatId: String = "",
    val lastMessage: String? = null,
    val lastSenderId: String? = null,
    val lastUpdated: Timestamp = Timestamp.now(),
    val participants: List<String> = emptyList()
) {

    companion object {
        fun fromMessage(message: Message) = Chat(
            message.chatId,
            message.text,
            message.senderId,
            message.timestamp,
            listOf(message.senderId, message.receiverId)
        )
    }
}