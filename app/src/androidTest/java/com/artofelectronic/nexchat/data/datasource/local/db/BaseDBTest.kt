package com.artofelectronic.nexchat.data.datasource.local.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
open class BaseDBTest {

    private lateinit var appDatabase: AppDatabase
    lateinit var userDao: UserDao
    lateinit var chatDao: ChatDao
    lateinit var messageDao: MessageDao


    @Before
    fun createDb() {
        appDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        userDao = appDatabase.userDao()
        chatDao = appDatabase.chatDao()
        messageDao = appDatabase.messageDao()
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }
}