package com.artofelectronic.nexchat.data.repository

import com.artofelectronic.nexchat.data.datasource.local.db.ChatDao
import com.artofelectronic.nexchat.data.datasource.local.db.MessageDao
import com.artofelectronic.nexchat.data.datasource.local.db.PendingUpdateDao
import com.artofelectronic.nexchat.data.models.ChatEntity
import com.artofelectronic.nexchat.data.models.MessageEntity
import com.artofelectronic.nexchat.data.models.PendingChatUpdate
import com.artofelectronic.nexchat.domain.model.Chat
import com.artofelectronic.nexchat.domain.model.Message
import com.artofelectronic.nexchat.domain.repository.ChatRepository
import com.artofelectronic.nexchat.utils.COLLECTION_MESSAGES
import com.artofelectronic.nexchat.utils.FIELD_IS_READ
import com.artofelectronic.nexchat.utils.FIELD_LAST_MESSAGE
import com.artofelectronic.nexchat.utils.FIELD_LAST_SENDER_ID
import com.artofelectronic.nexchat.utils.FIELD_LAST_UPDATED
import com.artofelectronic.nexchat.utils.FIELD_PARTICIPANTS
import com.artofelectronic.nexchat.utils.FIELD_TIMESTAMP
import com.artofelectronic.nexchat.utils.chatCollectionRef
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val pendingUpdateDao: PendingUpdateDao,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ChatRepository {

    /**
     * Observe chats from the local database.
     */
    override fun observeChats(): Flow<List<Chat>> =
        chatDao.observeChats().map { it.map { entity -> entity.toDomain() } }

    /**
     * Observe a specific chat from the local database.
     */
    override fun observeChat(chatId: String): Flow<Chat?> =
        chatDao.getChatById(chatId).map { it?.toDomain() }

    /**
     * Fetch chats once if they are not already in the local database.
     */
    override suspend fun fetchChatsOnceIfNeeded(userId: String) {
        if (!chatDao.hasChats()) refreshChatsFromFirebase(userId)
    }

    /**
     * Refresh chats from the Firebase collection.
     */
    override suspend fun refreshChatsFromFirebase(userId: String) {
        val snapshot = chatCollectionRef()
            .whereArrayContains(FIELD_PARTICIPANTS, userId)
            .get()
            .await()

        val chatEntities = snapshot.mapNotNull { doc ->
            doc.toObject(Chat::class.java)
                .copy(chatId = doc.id)
                .let(ChatEntity::fromDomain)
        }

        chatDao.insertAll(chatEntities)
    }

    /**
     * Start a real-time sync between the local database and the Firebase collection.
     */
    override fun startRealtimeSync(userId: String): ListenerRegistration {
        return chatCollectionRef()
            .whereArrayContains(FIELD_PARTICIPANTS, userId)
            .addSnapshotListener { snapshots, error ->
                if (error != null || snapshots == null) return@addSnapshotListener

                CoroutineScope(Dispatchers.IO).launch {
                    for (change in snapshots.documentChanges) {
                        val chat = change.document.toObject(Chat::class.java)
                            .copy(chatId = change.document.id)

                        when (change.type) {
                            DocumentChange.Type.ADDED,
                            DocumentChange.Type.MODIFIED -> {
                                chatDao.insert(ChatEntity.fromDomain(chat))
                            }

                            DocumentChange.Type.REMOVED -> {
                                chatDao.deleteById(chat.chatId)
                            }
                        }
                    }
                }
            }
    }

    /**
     * Insert a chat into the local database and the Firebase collection.
     * If the chat already exists in the Firebase collection, it will be updated.
     * In case of failure, the chat will be enqueued for retry.
     */
    override suspend fun insertChat(chat: Chat) {
        val entity = ChatEntity.fromDomain(chat)
        chatDao.insert(entity)
        try {
            chatCollectionRef()
                .document(chat.chatId)
                .set(chat, SetOptions.merge())
                .await()
        } catch (_: Exception) {
            pendingUpdateDao.enqueue(
                PendingChatUpdate(
                    chat.chatId,
                    mapOf(
                        FIELD_LAST_MESSAGE to chat.lastMessage.orEmpty(),
                        FIELD_LAST_SENDER_ID to chat.lastSenderId.orEmpty(),
                        FIELD_LAST_UPDATED to chat.lastUpdated,
                        FIELD_PARTICIPANTS to chat.participants
                    )
                )
            )
        }
    }

    /**
     * Retry pending updates to the Firebase collection.
     */
    override suspend fun retryPendingUpdates() {
        val updates = pendingUpdateDao.getAll()
        for (pending in updates) {
            try {
                chatCollectionRef()
                    .document(pending.chatId)
                    .update(pending.updateFields)
                    .await()
                pendingUpdateDao.dequeue(pending)
            } catch (_: Exception) {
            }
        }
    }

    /**
     * Observe messages from the local database.
     */
    override fun observeMessages(chatId: String): Flow<List<Message>> =
        messageDao.getMessages(chatId).map { entityList -> entityList.map { it.toDomain() } }

    /**
     * Send a message to the Firebase collection.
     */
    override suspend fun sendMessage(message: Message) {
        val chatRef = chatCollectionRef().document(message.chatId)
        val messageRef = chatRef.collection(COLLECTION_MESSAGES).document(message.messageId)

        val deliveredMessage = message.copy(delivered = true)

        // Save message locally
        messageDao.insert(MessageEntity.fromDomain(deliveredMessage))

        val updatedChat = Chat.fromMessage(message)

        // Save chat locally
        chatDao.insert(ChatEntity.fromDomain(updatedChat))

        // Save both chat metadata and message in Firestore atomically
        firestore.runBatch { batch ->
            batch.set(chatRef, updatedChat, SetOptions.merge())
            batch.set(messageRef, deliveredMessage)
        }.await()
    }

    /**
     * Start a real-time listener for messages in the Firebase collection.
     */
    override fun startMessageListener(chatId: String): ListenerRegistration {
        val currentUserId = firebaseAuth.currentUser?.uid.orEmpty()

        return chatCollectionRef().document(chatId).collection(COLLECTION_MESSAGES)
            .orderBy(FIELD_TIMESTAMP)
            .addSnapshotListener { snapshots, error ->
                if (error != null || snapshots == null) return@addSnapshotListener

                CoroutineScope(Dispatchers.IO).launch {
                    for (change in snapshots.documentChanges) {
                        val msg = change.document.toObject(Message::class.java)
                            .copy(messageId = change.document.id, delivered = true)

                        if (!msg.isRead && msg.receiverId == currentUserId) {
                            chatCollectionRef()
                                .document(chatId)
                                .collection(COLLECTION_MESSAGES)
                                .document(msg.messageId)
                                .update(FIELD_IS_READ, true)
                        }

                        messageDao.insert(MessageEntity.fromDomain(msg))
                    }
                }
            }
    }
}