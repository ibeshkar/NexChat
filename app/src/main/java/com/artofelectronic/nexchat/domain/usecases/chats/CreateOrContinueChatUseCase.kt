package com.artofelectronic.nexchat.domain.usecases.chats

import com.artofelectronic.nexchat.domain.model.Chat
import com.artofelectronic.nexchat.domain.repository.ChatRepository
import com.artofelectronic.nexchat.utils.generateChatId
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CreateOrContinueChatUseCase @Inject constructor(private val chatRepository: ChatRepository) {
    suspend operator fun invoke(currentUserId: String, otherUserId: String): Chat {
        val existingChats = chatRepository.observeChats().first()
        val matchedChat = existingChats.find {
            it.participants.containsAll(
                listOf(
                    currentUserId,
                    otherUserId
                )
            ) && it.participants.size == 2
        }

        return matchedChat ?: Chat(
            chatId = generateChatId(currentUserId, otherUserId).orEmpty(),
            participants = listOf(currentUserId, otherUserId),
        )
    }
}