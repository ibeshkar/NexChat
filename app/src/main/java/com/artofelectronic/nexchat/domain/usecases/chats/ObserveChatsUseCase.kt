package com.artofelectronic.nexchat.domain.usecases.chats

import com.artofelectronic.nexchat.domain.model.Chat
import com.artofelectronic.nexchat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveChatsUseCase @Inject constructor(private val chatRepository: ChatRepository) {
    operator fun invoke(): Flow<List<Chat>> = chatRepository.observeChats()
}