package com.artofelectronic.nexchat.data.repository

import com.artofelectronic.nexchat.data.datasource.local.db.ChatDao
import com.artofelectronic.nexchat.data.models.Chat
import com.artofelectronic.nexchat.data.models.ChatEntity
import com.artofelectronic.nexchat.domain.repository.ChatRepository
import com.artofelectronic.nexchat.utils.COLLECTION_CHATS
import com.artofelectronic.nexchat.utils.NetworkResourceBoundary
import com.artofelectronic.nexchat.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val firestore: FirebaseFirestore
) : ChatRepository {

    override fun getChatsForUser(userId: String): Flow<Resource<List<Chat>>> {
        return object : NetworkResourceBoundary<List<Chat>, List<ChatEntity>>() {

            override suspend fun fetchFromRemote(): List<Chat> {
                val snapshot = firestore.collection(COLLECTION_CHATS)
                    .whereArrayContains("participants", userId)
                    .orderBy("lastUpdated", Query.Direction.DESCENDING)
                    .get()
                    .await()

                return snapshot.documents.mapNotNull {
                    it.toObject(Chat::class.java)
                }
            }

            override suspend fun saveRemoteData(data: List<Chat>) {
                val entities = data.map {
                    ChatEntity(
                        chatId = it.chatId,
                        lastMessage = it.lastMessage,
                        lastSenderId = it.lastSenderId,
                        lastUpdated = it.lastUpdated,
                        participants = it.participants
                    )
                }
                chatDao.insertAll(entities)
            }

            override suspend fun loadFromDb(): List<ChatEntity> {
                return chatDao.getAllChats()
            }

            override fun mapToResult(local: List<ChatEntity>): List<Chat> {
                return local.map {
                    Chat(
                        chatId = it.chatId,
                        lastMessage = it.lastMessage,
                        lastSenderId = it.lastSenderId,
                        lastUpdated = it.lastUpdated,
                        participants = it.participants
                    )
                }
            }

        }.asFlow()
    }

    override suspend fun createChatWith(userIds: List<String>): Chat {
        val newChatRef = firestore.collection(COLLECTION_CHATS).document()
        val chatData = mapOf(
            "participantIds" to userIds,
            "lastMessage" to "",
            "lastUpdated" to System.currentTimeMillis()
        )
        newChatRef.set(chatData).await()

        return Chat(
            chatId = newChatRef.id,
            participants = userIds,
            lastMessage = "",
            lastUpdated = chatData["lastUpdated"] as Long
        )
    }

//    private suspend fun enrichChatsWithUsers(chats: List<Chat>): List<Chat> {
//        val userCache = mutableMapOf<String, User>()
//
//        return chats.map { chat ->
//            val enrichedParticipants = chat.participants.map { user ->
//                val cached = userCache[user.id]
//                if (cached != null) {
//                    cached
//                } else {
//                    val fetched = fetchUserInfo(user.id)
//                    userCache[user.id] = fetched
//                    fetched
//                }
//            }
//            chat.copy(participants = enrichedParticipants)
//        }
//    }
//
//    private suspend fun fetchUserInfo(userId: String): User {
//        return try {
//            val doc = firestore.collection(COLLECTION_USERS).document(userId).get().await()
//            val name = doc["displayName"] as? String ?: ""
//            val avatar = doc["avatar"] as? String ?: ""
//            User(
//                id = userId,
//                name = name,
//                avatar = avatar
//            )
//        } catch (e: Exception) {
//            User(id = userId)
//        }
//    }
}