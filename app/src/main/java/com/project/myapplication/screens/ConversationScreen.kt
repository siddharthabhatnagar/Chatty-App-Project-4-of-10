package com.project.myapplication.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.project.myapplication.navigation.ChatScreenRoute
import com.project.myapplication.room.ConversationEntity
import com.project.myapplication.viewmodel.ConversationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationListScreen(
    navHostController: NavHostController,
    viewModel: ConversationViewModel = hiltViewModel()
) {

    val conversations by viewModel.conversations.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var titleInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chaty AI") },
                actions = {
                    IconButton(
                        onClick = { showDialog = true }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "New Chat")
                    }
                }
            )
        }
    ) { padding ->

        if (conversations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No conversations yet")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding)
            ) {
                items(conversations) { conversation ->
                    ConversationItem(
                        conversation = conversation,
                        onClick = {
                            navHostController.navigate(
                                ChatScreenRoute(conversation.id)
                            )
                        }
                    )
                }
            }
        }
    }

    // 🔥 Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                titleInput = ""
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (titleInput.isNotBlank()) {
                            viewModel.createNewConversation(
                                titleInput
                            ) { id ->
                                showDialog = false
                                titleInput = ""
                                navHostController.navigate(
                                    ChatScreenRoute(id)
                                )
                            }
                        }
                    }
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        titleInput = ""
                    }
                ) {
                    Text("Cancel")
                }
            },
            title = { Text("New Conversation") },
            text = {
                TextField(
                    value = titleInput,
                    onValueChange = { titleInput = it },
                    placeholder = { Text("Enter conversation title") },
                    singleLine = true
                )
            }
        )
    }
}
@Composable
fun ConversationItem(
    conversation: ConversationEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = conversation.title ?: "New Chat",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tap to continue",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}