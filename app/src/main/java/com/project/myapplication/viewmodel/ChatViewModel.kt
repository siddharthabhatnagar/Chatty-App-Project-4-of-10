package com.project.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.myapplication.data.ChatUiState
import com.project.myapplication.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    fun observeMessages(conversationId: String) {
        viewModelScope.launch {
            repository.getMessages(conversationId)
                .collect { messages ->
                    _uiState.update {
                        it.copy(messages = messages)
                    }
                }
        }
    }

    fun sendMessage(
        conversationId: String,
        message: String
    ) {
        if (message.isBlank()) return

        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                repository.sendMessage(conversationId, message)

                _uiState.update {
                    it.copy(isLoading = false)
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }
}
