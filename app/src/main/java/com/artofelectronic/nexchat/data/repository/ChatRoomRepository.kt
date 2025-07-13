package com.artofelectronic.nexchat.data.repository

import com.artofelectronic.nexchat.data.models.ChatRoom
import com.artofelectronic.nexchat.domain.repository.IChatRoomRepository
import javax.inject.Inject

class ChatRoomRepository @Inject constructor() : IChatRoomRepository {

    override suspend fun getInitialChatRooms(chatRoomId: String): ChatRoom {
        TODO("Not yet implemented")
    }
}