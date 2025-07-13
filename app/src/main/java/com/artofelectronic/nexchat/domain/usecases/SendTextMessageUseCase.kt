package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.data.models.Message
import com.artofelectronic.nexchat.domain.repository.MessageRepository
import javax.inject.Inject

class SendTextMessageUseCase @Inject constructor(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(chatId: String, message: Message) {
        messageRepository.sendMessage(chatId, message)
    }
}