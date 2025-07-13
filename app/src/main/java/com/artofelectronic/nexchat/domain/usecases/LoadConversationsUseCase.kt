package com.artofelectronic.nexchat.domain.usecases

import com.artofelectronic.nexchat.domain.repository.ChatRepository
import javax.inject.Inject

class LoadConversationsUseCase @Inject constructor(private val chatRepository: ChatRepository) {
    operator fun invoke(userId: String)= chatRepository.getChatsForUser(userId)
}