package com.artofelectronic.nexchat.domain.usecases.chats

import com.artofelectronic.nexchat.domain.model.Message
import com.artofelectronic.nexchat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    operator fun invoke(chatId: String): Flow<List<Message>> =
        chatRepository.observeMessages(chatId)
}