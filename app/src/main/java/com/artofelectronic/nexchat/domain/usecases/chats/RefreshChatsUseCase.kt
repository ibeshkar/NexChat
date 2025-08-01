package com.artofelectronic.nexchat.domain.usecases.chats

import com.artofelectronic.nexchat.domain.repository.ChatRepository
import javax.inject.Inject

class RefreshChatsUseCase @Inject constructor(private val repository: ChatRepository) {
    suspend operator fun invoke(userId: String) = repository.refreshChatsFromFirebase(userId)
}