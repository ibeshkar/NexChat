package com.artofelectronic.nexchat.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.artofelectronic.nexchat.domain.model.Message
import com.google.firebase.Timestamp

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val messageId: String,
    val chatId: String,
    val senderId: String,
    val receiverId: String,
    val text: String,
    val timestamp: Timestamp,
    val delivered: Boolean,
    val isRead: Boolean
) {
    fun toDomain() = Message(messageId, chatId, senderId, receiverId, text, timestamp, delivered, isRead)

    companion object {
        fun fromDomain(domain: Message) = MessageEntity(
            domain.messageId,
            domain.chatId,
            domain.senderId,
            domain.receiverId,
            domain.text,
            domain.timestamp,
            domain.delivered,
            domain.isRead
        )
    }
}