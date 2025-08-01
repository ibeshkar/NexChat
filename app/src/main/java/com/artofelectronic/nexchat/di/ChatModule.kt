package com.artofelectronic.nexchat.di

import com.artofelectronic.nexchat.data.repository.ChatRepositoryImpl
import com.artofelectronic.nexchat.domain.repository.ChatRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {
    @Binds
    abstract fun bindChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository

}