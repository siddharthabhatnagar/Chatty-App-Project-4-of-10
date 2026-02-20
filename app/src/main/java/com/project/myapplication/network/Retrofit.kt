package com.project.myapplication.network

import com.project.myapplication.data.ChatResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {

    @POST("chat")
    suspend fun sendChat(
        @Body request: com.project.myapplication.network.ChatRequest
    ): ChatResponse
}

data class MessageDto(
    val role: String,
    val content: String
)

data class ChatRequest(
    val messages: List<MessageDto>
)

data class ChatResponse(
    val response: String
)
