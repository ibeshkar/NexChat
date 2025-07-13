package com.artofelectronic.nexchat.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.artofelectronic.nexchat.data.models.ChatEntity

@Dao
interface ChatDao {

    @Query("SELECT * FROM chats ORDER BY lastUpdated DESC")
    suspend fun getAllChats(): List<ChatEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chats: List<ChatEntity>)
}