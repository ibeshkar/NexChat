package com.artofelectronic.nexchat.data.datasource.local.db

import com.artofelectronic.nexchat.data.models.MessageEntity
import com.artofelectronic.nexchat.domain.model.Message
import com.artofelectronic.nexchat.utils.messageEntities
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test


class MessageDaoTest : BaseDBTest() {

    @Test
    fun testInsertSingleMessage() = runTest {
        val entity =
            MessageEntity.fromDomain(Message(messageId = "1", chatId = "1", text = "heyyyy!!"))

        messageDao.insert(entity)

        val message = messageDao.getMessages("1").first()[0]

        assertEquals("1", message.chatId)
        assertEquals("heyyyy!!", message.text)
    }

    @Test
    fun testInsertMultiUser() = runTest {
        val entities = messageEntities

        messageDao.insertAll(entities)

        val messages = messageDao.getMessages("1").first()
        assertEquals(2, messages.size)

        val otherMessages = messageDao.getMessages("2").first()
        assertEquals(2, otherMessages.size)
    }

    @Test
    fun testClearAllRecords() = runTest {
        val entities = messageEntities

        messageDao.insertAll(entities)

        assertEquals(true, messageDao.hasMessages())

        messageDao.clear()

        assertEquals(false, messageDao.hasMessages())
    }

}