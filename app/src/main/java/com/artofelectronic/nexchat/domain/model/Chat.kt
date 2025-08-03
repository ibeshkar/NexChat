package com.artofelectronic.nexchat.domain.model

import com.google.firebase.Timestamp

data class Chat(
    val chatId: String = "",
    val lastMessage: String? = null,
    val lastSenderId: String? = null,
    val receiver: String? = null,
    val lastUpdated: Timestamp = Timestamp.now(),
    val participants: List<String> = emptyList()
) {

    companion object {
        fun fromMessage(message: Message, receiver: String?) = Chat(
            chatId = message.chatId,
            lastMessage = message.text,
            lastSenderId = message.senderId,
            receiver = receiver,
            lastUpdated = message.timestamp,
            participants = listOf(message.senderId, message.receiverId)
        )
    }
}