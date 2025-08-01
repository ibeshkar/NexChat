package com.artofelectronic.nexchat.utils

import com.artofelectronic.nexchat.data.models.UserEntity
import com.artofelectronic.nexchat.domain.model.Chat
import com.artofelectronic.nexchat.domain.model.Message
import com.artofelectronic.nexchat.domain.model.User
import com.facebook.AccessToken


const val VALID_EMAIL = "e.beshkar@gmail.com"
const val VALID_PASSWORD = "Matin2018!"

const val INVALID_EMAIL = "thisfjdjndfn"
const val INVALID_PASSWORD = "12345tz"

val Facebook_Access_Token = AccessToken(
    "token123",
    "appId",
    "userId",
    null,
    null,
    null,
    null,
    null,
    lastRefreshTime = null,
    dataAccessExpirationTime = null,
    graphDomain = null
)

const val Fake_UserId = "currentUser123"
const val Fake_ReceiverId = "otherUser456"
const val Fake_ChatId = "djnjkscnsdncasdbjfmnasbf"

val chats = listOf(
    Chat(chatId = "1", lastMessage = "Hello"),
    Chat(chatId = "2", lastMessage = "hey, you"),
    Chat(chatId = "3", lastMessage = "how are you doing?"),
    Chat(chatId = "4", lastMessage = "I'm doing well"),
)

val fakeUser = User(
    userId = Fake_UserId,
    displayName = "Esmaeil",
    email = VALID_EMAIL,
    avatarUrl = "http://",
    bio = ""
)

val fakeChat = Chat(chatId = Fake_ChatId, lastMessage = "Hello")

val messages = listOf(
    Message(messageId = "1", chatId = "1", text = "heyyyy!!"),
    Message(messageId = "2", chatId = "1", text = "how are you?"),
    Message(messageId = "3", chatId = "2", text = "I'm fine, thanks"),
    Message(messageId = "4", chatId = "2", text = "how aboiut you?")
)

val users = listOf(
    User(userId = "1", displayName = "User 1"),
    User(userId = "2", displayName = "User 2"),
    User(userId = "3", displayName = "User 3")
)