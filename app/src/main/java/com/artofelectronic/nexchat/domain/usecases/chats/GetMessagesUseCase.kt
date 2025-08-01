package com.artofelectronic.nexchat.domain.usecases.chats

import com.artofelectronic.nexchat.domain.repository.ChatRepository
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: String) = chatRepository.startMessageListener(chatId)
}