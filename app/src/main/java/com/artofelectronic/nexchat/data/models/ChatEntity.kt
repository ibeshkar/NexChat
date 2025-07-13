package com.artofelectronic.nexchat.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val chatId: String,
    val lastMessage: String?,
    val lastSenderId: String?,
    val lastUpdated: Long,
    val participants: List<String>
)