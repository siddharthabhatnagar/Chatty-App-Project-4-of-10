package com.project.myapplication.repository

import com.project.myapplication.network.ChatApi
import com.project.myapplication.network.MessageDto
import com.project.myapplication.room.ChatDao
import com.project.myapplication.room.ConversationEntity
import com.project.myapplication.room.MessageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import java.util.UUID
import javax.inject.Inject

class ChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val api: ChatApi
) {

    fun getMessages(conversationId: String): Flow<List<MessageEntity>> {
        return chatDao.getMessages(conversationId)
    }

    suspend fun sendMessage(
        conversationId: String,
        userMessage: String
    ) {

        // 1️⃣ Save user message locally
        chatDao.insertMessage(
            MessageEntity(
                conversationId = conversationId,
                role = "user",
                content = userMessage
            )
        )

        // 2️⃣ Fetch full history
        val history = chatDao
            .getMessagesOnce(conversationId)
            .map {
                MessageDto(
                    role = it.role,
                    content = it.content
                )
            }

        // 3️⃣ Trim messages (last 20 only)
        val trimmedHistory = history.takeLast(20)

        // 4️⃣ Call backend
        val response = api.sendChat(
            com.project.myapplication.network.ChatRequest(messages = trimmedHistory)
        )

        // 5️⃣ Save AI response
        chatDao.insertMessage(
            MessageEntity(
                conversationId = conversationId,
                role = "assistant",
                content = response.response
            )
        )
    }
    suspend fun addConversation(title:String): String {
        val conversationId=UUID.randomUUID().toString()
        chatDao.insertConversation(
            ConversationEntity(
                id = conversationId,
                title = title
            )
        )
        return conversationId
    }
    fun getAllConversations(): Flow<List<ConversationEntity>> {
        return chatDao.getAllConversations()

    }
}
