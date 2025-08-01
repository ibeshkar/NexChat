package com.artofelectronic.nexchat.domain.usecases.chats

import com.artofelectronic.nexchat.domain.repository.ChatRepository
import com.google.firebase.firestore.ListenerRegistration
import javax.inject.Inject

class ChatRealtimeSyncUseCase @Inject constructor(private val chatRepository: ChatRepository) {
    operator fun invoke(userId: String): ListenerRegistration = chatRepository.startRealtimeSync(userId)
}