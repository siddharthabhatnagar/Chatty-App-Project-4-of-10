package com.project.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.project.myapplication.screens.ChatScreen
import com.project.myapplication.screens.ConversationListScreen

@Composable
fun NavGraph(navHostController: NavHostController){
    NavHost(navController = navHostController, startDestination = ConversationScreenRoute) {
        composable<ConversationScreenRoute> {
            ConversationListScreen(navHostController)
        }
        composable<ChatScreenRoute> {navBackStackEntry ->
            ChatScreen(navBackStackEntry.toRoute<ChatScreenRoute>().conversationID)
        }
    }
}