package com.project.myapplication.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.project.myapplication.viewmodel.ChatViewModel
// Compose Core
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions

// Material 3
import androidx.compose.material3.*
import androidx.compose.material3.TextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

// Icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send

// Runtime
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

// UI
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

// Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.myapplication.screens.components.*

// Coroutine
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@Composable
fun ChatScreen (
    conversationId:String
) {

    val viewModel= hiltViewModel<ChatViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    var message by remember { mutableStateOf("") }

    // Observe messages
    LaunchedEffect(conversationId) {
        viewModel.observeMessages(conversationId)
    }

    // Auto scroll to bottom
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // 🔹 Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(uiState.messages) { messageEntity ->
                ChatBubble(
                    message = messageEntity.content,
                    isUser = messageEntity.role == "user"
                )
            }

            // 🔹 Loading Indicator
            if (uiState.isLoading) {
                item {
                    TypingIndicator()
                }
            }
        }

        // 🔹 Input Bar
        ChatInputBar(
            message = message,
            onMessageChange = { message = it },
            isLoading = uiState.isLoading,
            onSendClick = {
                viewModel.sendMessage(conversationId, message)
                message = ""
            }
        )
    }
}


