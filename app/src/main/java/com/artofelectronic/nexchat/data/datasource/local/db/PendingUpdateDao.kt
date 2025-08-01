package com.artofelectronic.nexchat.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.artofelectronic.nexchat.data.models.PendingChatUpdate

@Dao
interface PendingUpdateDao {
    @Query("SELECT * FROM pending_chat_updates")
    suspend fun getAll(): List<PendingChatUpdate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun enqueue(update: PendingChatUpdate)

    @Delete
    suspend fun dequeue(update: PendingChatUpdate)

    @Query("DELETE FROM pending_chat_updates")
    suspend fun clear()
}