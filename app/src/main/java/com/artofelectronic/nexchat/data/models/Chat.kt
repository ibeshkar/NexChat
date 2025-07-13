package com.artofelectronic.nexchat.data.models

data class Chat(
    val chatId: String = "",
    val lastMessage: String? = null,
    val lastSenderId: String? = null,
    val lastUpdated: Long = System.currentTimeMillis(),
    val participants: List<String> = emptyList()
)