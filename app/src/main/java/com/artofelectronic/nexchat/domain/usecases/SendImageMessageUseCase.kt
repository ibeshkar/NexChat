package com.artofelectronic.nexchat.domain.usecases

import android.net.Uri
import com.artofelectronic.nexchat.domain.repository.MessageRepository
import javax.inject.Inject

class SendImageMessageUseCase @Inject constructor(private val messageRepository: MessageRepository) {
    suspend operator fun invoke(chatId: String, senderId: String, uri: Uri) {
        messageRepository.uploadImageAndSendMessage(chatId, senderId, uri)
    }
}