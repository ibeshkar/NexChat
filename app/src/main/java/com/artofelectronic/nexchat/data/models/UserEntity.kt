package com.artofelectronic.nexchat.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.artofelectronic.nexchat.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val displayName: String,
    val email: String,
    val avatarUrl: String,
    val bio: String?
) {
    fun toDomain() = User(userId, displayName, email, avatarUrl, bio)

    companion object {
        fun fromDomain(user: User) = UserEntity(
            user.userId,
            user.displayName,
            user.email,
            user.avatarUrl,
            user.bio
        )
    }
}