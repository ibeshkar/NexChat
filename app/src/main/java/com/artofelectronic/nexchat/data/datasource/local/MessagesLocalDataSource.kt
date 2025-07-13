package com.artofelectronic.nexchat.data.datasource.local

import com.artofelectronic.nexchat.data.datasource.local.db.MessageDao
import com.artofelectronic.nexchat.data.models.MessageEntity
import javax.inject.Inject

class MessagesLocalDataSource @Inject constructor(private val messageDao: MessageDao) {

    suspend fun getMessagesInConversation(chatId: String) =
        messageDao.getMessagesForChat(chatId)

    suspend fun insertMessages(messages: List<MessageEntity>) =
        messageDao.insertAll(messages)

}