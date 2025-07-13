package com.artofelectronic.nexchat.data.repository

import android.net.Uri
import com.artofelectronic.nexchat.data.datasource.local.db.MessageDao
import com.artofelectronic.nexchat.data.models.Message
import com.artofelectronic.nexchat.data.models.MessageEntity
import com.artofelectronic.nexchat.domain.repository.MessageRepository
import com.artofelectronic.nexchat.utils.COLLECTION_CHATS
import com.artofelectronic.nexchat.utils.COLLECTION_MESSAGES
import com.artofelectronic.nexchat.utils.NetworkResourceBoundary
import com.artofelectronic.nexchat.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : MessageRepository {
    override fun listenToMessages(chatId: String): Flow<Resource<List<Message>>> {
        return object : NetworkResourceBoundary<List<Message>, List<MessageEntity>>() {

            override suspend fun fetchFromRemote(): List<Message> {
                val snapshot = firestore.collection(COLLECTION_CHATS)
                    .document(chatId)
                    .collection(COLLECTION_MESSAGES)
                    .orderBy("timestamp", Query.Direction.ASCENDING)
                    .get()
                    .await()

                return snapshot.documents.mapNotNull { it.toObject(Message::class.java) }
            }

            override suspend fun saveRemoteData(data: List<Message>) {
                val local = data.map {
                    MessageEntity(
                        id = it.id,
                        chatId = it.chatId,
                        senderId = it.senderId,
                        text = it.text,
                        imageUrl = it.imageUrl,
                        timestamp = it.timestamp
                    )
                }
                messageDao.insertAll(local)
            }

            override suspend fun loadFromDb(): List<MessageEntity> {
                return messageDao.getMessagesForChat(chatId)
            }

            override fun mapToResult(local: List<MessageEntity>): List<Message> {
                return local.map {
                    Message(
                        id = it.id,
                        chatId = it.chatId,
                        senderId = it.senderId,
                        text = it.text,
                        imageUrl = it.imageUrl,
                        timestamp = it.timestamp
                    )
                }
            }

        }.asFlow()

    }

    override suspend fun sendMessage(chatId: String, message: Message) {
        firestore.collection(COLLECTION_CHATS)
            .document(chatId)
            .collection(COLLECTION_MESSAGES)
            .document(message.id)
            .set(message)
            .await()
    }

    override suspend fun uploadImageAndSendMessage(
        chatId: String,
        senderId: String,
        imageUri: Uri
    ) {
        val storageRef = storage.reference.child("chat_images/${UUID.randomUUID()}.jpg")

        storageRef.putFile(imageUri).await()

        val downloadUrl = storageRef.downloadUrl.await().toString()

        val message = Message(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            senderId = senderId,
            imageUrl = downloadUrl,
            text = null,
            timestamp = System.currentTimeMillis()
        )
        sendMessage(chatId, message)
    }
}

