package com.artofelectronic.nexchat.data.datasource.remote

import com.artofelectronic.nexchat.data.models.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreMessagesDataSource @Inject constructor(private val firestore: FirebaseFirestore) {

    companion object {
        private const val COLLECTION_CHATS = "chats"
        private const val COLLECTION_MESSAGES = "messages"
        private const val FIELD_TIMESTAMP = "timestamp"
    }

    /**
     * Get messages of the specified chat.
     */
    fun getMessages(chatId: String, userId: String): Flow<Message> = callbackFlow {
        val chatRef = firestore
            .collection(COLLECTION_CHATS)
            .document(chatId)
            .collection(COLLECTION_MESSAGES)

        val query = chatRef.orderBy(FIELD_TIMESTAMP, Query.Direction.ASCENDING)

        val listener = query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }

            val messages = snapshot?.documents?.mapNotNull { doc ->
                val message = doc.toObject(Message::class.java)
                message?.copy(id = doc.id)
            } ?: emptyList()

            messages.forEach { message ->
                message.isMine = message.senderId == userId
                try {
                    trySend(message).isSuccess
                } catch (e: Exception) {
                    close(e)
                }
            }
        }

        awaitClose { listener.remove() }
    }

    /**
     * Send a message to the specified chat.
     */
    fun sendMessage(chatId: String, message: Message) {
        val chatRef = firestore
            .collection(COLLECTION_CHATS)
            .document(chatId)
            .collection(COLLECTION_MESSAGES)

        chatRef.add(message)
    }
}