package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.domain.repository.IChatRoomRepository
import javax.inject.Inject

class GetInitialChatRoomInformation @Inject constructor(
    private val chatRoomRepository: IChatRoomRepository
) {
    suspend operator fun invoke(chatRoomId: String) = chatRoomRepository.getInitialChatRooms(chatRoomId)
}