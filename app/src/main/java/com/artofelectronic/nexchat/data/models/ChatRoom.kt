package com.artofelectronic.nexchat.data.models

data class ChatRoom(
    val id: String = "",
    val name: String = "",
    val messages: List<Message> = emptyList()
)
