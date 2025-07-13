package com.artofelectronic.nexchat.di

import com.artofelectronic.nexchat.data.repository.ChatRepositoryImpl
import com.artofelectronic.nexchat.data.repository.ChatRoomRepository
import com.artofelectronic.nexchat.data.repository.MessagesRepositoryImpl
import com.artofelectronic.nexchat.domain.repository.ChatRepository
import com.artofelectronic.nexchat.domain.repository.IChatRoomRepository
import com.artofelectronic.nexchat.domain.repository.MessageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    @Binds
    abstract fun bindChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository

     @Binds
     abstract fun bindMessageRepository(messageRepositoryImpl: MessagesRepositoryImpl): MessageRepository

     @Binds
     abstract fun bindChatRoomRepository(chatRoomRepositoryImpl: ChatRoomRepository): IChatRoomRepository


}