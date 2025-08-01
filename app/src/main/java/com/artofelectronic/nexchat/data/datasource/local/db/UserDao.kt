package com.artofelectronic.nexchat.data.datasource.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.artofelectronic.nexchat.data.models.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun observeUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE userId = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("DELETE FROM users WHERE userId = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) > 0 FROM users")
    suspend fun hasUsers(): Boolean

    @Query("DELETE FROM users")
    suspend fun clear()
}