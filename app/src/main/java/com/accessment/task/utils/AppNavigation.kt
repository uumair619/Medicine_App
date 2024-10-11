package com.accessment.task.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.accessment.task.ui.views.HomeScreen
import com.accessment.task.ui.views.LoginScreen



@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("home/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username")
            HomeScreen(username = username,navController = navController)
        }
    }
}
