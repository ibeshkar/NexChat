package com.artofelectronic.nexchat.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_chat_updates")
data class PendingChatUpdate(
    @PrimaryKey val chatId: String,
    val updateFields: Map<String, Any>
)
