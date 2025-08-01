package com.artofelectronic.nexchat.utils

import com.artofelectronic.nexchat.data.models.ChatEntity
import com.artofelectronic.nexchat.data.models.MessageEntity
import com.artofelectronic.nexchat.data.models.UserEntity
import com.artofelectronic.nexchat.domain.model.Chat
import com.artofelectronic.nexchat.domain.model.Message
import com.artofelectronic.nexchat.domain.model.User

const val VALID_EMAIL = "e.beshkar@gmail.com"
const val VALID_PASSWORD = "Matin2018!"

const val INVALID_EMAIL = "thisfjdjndfn"
const val INVALID_PASSWORD = "12345tz"


val userEntities = listOf(
    UserEntity.fromDomain(User(userId = "1", displayName = "User 1")),
    UserEntity.fromDomain(User(userId = "2", displayName = "User 2")),
    UserEntity.fromDomain(User(userId = "3", displayName = "User 3"))
)

val chatEntities = listOf(
    ChatEntity.fromDomain(Chat(chatId = "1", lastMessage = "Hello")),
    ChatEntity.fromDomain(Chat(chatId = "2", lastMessage = "hey, you")),
    ChatEntity.fromDomain(Chat(chatId = "3", lastMessage = "how are you doing?")),
    ChatEntity.fromDomain(Chat(chatId = "4", lastMessage = "I'm doing well")),
)

val messageEntities = listOf(
    MessageEntity.fromDomain(Message(messageId = "1", chatId = "1", text = "heyyyy!!")),
    MessageEntity.fromDomain(Message(messageId = "2", chatId = "1", text = "how are you?")),
    MessageEntity.fromDomain(Message(messageId = "3", chatId = "2", text = "I'm fine, thanks")),
    MessageEntity.fromDomain(Message(messageId = "4", chatId = "2", text = "how aboiut you?"))
)