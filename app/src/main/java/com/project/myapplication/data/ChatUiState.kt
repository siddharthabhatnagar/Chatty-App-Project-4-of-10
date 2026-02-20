package com.project.myapplication.data

import com.project.myapplication.room.MessageEntity

data class ChatUiState(
    val messages: List<MessageEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
