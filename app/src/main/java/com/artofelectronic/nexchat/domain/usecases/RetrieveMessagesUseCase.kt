package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.domain.repository.MessageRepository
import javax.inject.Inject

class RetrieveMessagesUseCase @Inject constructor(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(chatId: String) = messageRepository.listenToMessages(chatId)
}