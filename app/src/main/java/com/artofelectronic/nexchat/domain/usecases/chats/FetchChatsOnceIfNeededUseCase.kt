package com.artofelectronic.nexchat.domain.usecases.chats

import com.artofelectronic.nexchat.domain.repository.ChatRepository
import javax.inject.Inject

class FetchChatsOnceIfNeededUseCase @Inject constructor(private val chatRepository: ChatRepository)  {
    suspend operator fun invoke(userId: String) = chatRepository.fetchChatsOnceIfNeeded(userId)
}