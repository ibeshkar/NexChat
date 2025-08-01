package com.artofelectronic.nexchat.data.datasource.local.db

import com.artofelectronic.nexchat.data.models.UserEntity
import com.artofelectronic.nexchat.domain.model.User
import com.artofelectronic.nexchat.utils.userEntities
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test


class UserDaoTest : BaseDBTest() {

    @Test
    fun testInsertSingleUser() = runTest {
        val entity = UserEntity.fromDomain(User(userId = "1", displayName = "User 1"))

        userDao.insert(entity)

        val user = userDao.getUserById("1")

        assertEquals(true, userDao.hasUsers())
        assertEquals("User 1", user?.displayName)
    }

    @Test
    fun testInsertMultiUser() = runTest {
        val entities = userEntities

        userDao.insertAll(entities)

        assertEquals(true, userDao.hasUsers())

        val users = userDao.observeUsers().first()

        assertEquals(entities.size, users.size)
    }

    @Test
    fun testDeleteRecord() = runTest {
        val entities = userEntities

        userDao.insertAll(entities)

        assertEquals(true, userDao.hasUsers())

        userDao.deleteById("1")

        val users = userDao.observeUsers().first()

        assertEquals(entities.size - 1, users.size)
    }

    @Test
    fun testClearAllRecords() = runTest {
        val entities = userEntities

        userDao.insertAll(entities)

        assertEquals(true, userDao.hasUsers())

        userDao.clear()

        assertEquals(false, userDao.hasUsers())
    }
}