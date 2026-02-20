package com.project.myapplication.navigation

import kotlinx.serialization.Serializable

@Serializable
object ConversationScreenRoute

@Serializable
data class ChatScreenRoute(val conversationID:String)