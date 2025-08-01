package com.artofelectronic.nexchat.data.datasource.local.db

import com.artofelectronic.nexchat.data.models.ChatEntity
import com.artofelectronic.nexchat.domain.model.Chat
import com.artofelectronic.nexchat.utils.chatEntities
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test


class ChatDaoTest : BaseDBTest() {

    @Test
    fun testInsertSingleChat() = runTest {
        val entity = ChatEntity.fromDomain(Chat(chatId = "1", lastMessage = "Hello"))

        chatDao.insert(entity)

        val chat = chatDao.getChatById("1")

        assertEquals(true, chatDao.hasChats())
        assertEquals("Hello", chat.first()?.lastMessage)
    }

    @Test
    fun testInsertMultiUser() = runTest {
        val entities = chatEntities

        chatDao.insertAll(entities)

        assertEquals(true, chatDao.hasChats())

        val chats = chatDao.observeChats().first()

        assertEquals(entities.size, chats.size)
    }

    @Test
    fun testDeleteRecord() = runTest {
        val entities = chatEntities

        chatDao.insertAll(entities)

        assertEquals(true, chatDao.hasChats())

        chatDao.deleteById("1")

        val chats = chatDao.observeChats().first()

        assertEquals(entities.size - 1, chats.size)
    }

    @Test
    fun testClearAllRecords() = runTest {
        val entities = chatEntities

        chatDao.insertAll(entities)

        assertEquals(true, chatDao.hasChats())

        chatDao.clear()

        assertEquals(false, chatDao.hasChats())
    }
}