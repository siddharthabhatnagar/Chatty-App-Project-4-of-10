package com.project.myapplication.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    // Conversations
    @Insert
    suspend fun insertConversation(conversation: ConversationEntity)

    @Query("SELECT * FROM conversations ORDER BY createdAt DESC")
    fun getAllConversations(): Flow<List<ConversationEntity>>

    @Query("SELECT COUNT(*) FROM conversations")
    suspend fun getConversationCount(): Int

    // Messages
    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    fun getMessages(conversationId: String): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY timestamp ASC")
    suspend fun getMessagesOnce(conversationId: String): List<MessageEntity>

}
