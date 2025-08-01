package com.artofelectronic.nexchat.domain.usecases.chats

import com.artofelectronic.nexchat.domain.model.Message
import com.artofelectronic.nexchat.domain.repository.ChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(message: Message) = chatRepository.sendMessage(message)
}