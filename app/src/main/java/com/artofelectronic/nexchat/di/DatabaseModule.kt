package com.artofelectronic.nexchat.di

import android.content.Context
import com.artofelectronic.nexchat.data.datasource.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return AppDatabase.getInstance(appContext)
    }

    @Provides
    fun provideChatDao(appDatabase: AppDatabase) = appDatabase.chatDao()

    @Provides
    fun provideMessageDao(appDatabase: AppDatabase) = appDatabase.messageDao()

}