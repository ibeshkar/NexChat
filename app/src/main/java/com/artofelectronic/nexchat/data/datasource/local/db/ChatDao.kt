package com.artofelectronic.nexchat.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.artofelectronic.nexchat.data.models.ChatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Query("SELECT * FROM chats ORDER BY lastUpdated DESC")
    fun observeChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats WHERE chatId = :id LIMIT 1")
    fun getChatById(id: String): Flow<ChatEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chat: ChatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chatEntities: List<ChatEntity>)

    @Query("DELETE FROM chats WHERE chatId = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) > 0 FROM chats")
    suspend fun hasChats(): Boolean

    @Query("DELETE FROM chats")
    suspend fun clear()
}