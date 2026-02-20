package com.project.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.myapplication.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    val conversations = repository.getAllConversations()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun createNewConversation(
        title: String,
        onCreated: (String) -> Unit
    ) {
        viewModelScope.launch {
            val id = repository.addConversation(title)
            onCreated(id)
        }
    }
}