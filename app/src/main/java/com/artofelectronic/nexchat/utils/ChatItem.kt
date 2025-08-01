package com.artofelectronic.nexchat.utils

import com.artofelectronic.nexchat.domain.model.Message

sealed class ChatItem {
    data class DateHeader(val date: String) : ChatItem()
    data class MessageItem(val message: Message) : ChatItem()
    data object UnreadSeparator : ChatItem()
}