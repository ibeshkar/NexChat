package com.artofelectronic.nexchat.domain.repository

import com.artofelectronic.nexchat.data.models.ChatRoom

interface IChatRoomRepository {
    suspend fun getInitialChatRooms(chatRoomId: String): ChatRoom
}