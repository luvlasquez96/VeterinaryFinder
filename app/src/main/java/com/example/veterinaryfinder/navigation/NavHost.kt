package com.example.veterinaryfinder.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.veterinaryfinder.presentation.AddVeterinaryScreen
import com.example.veterinaryfinder.presentation.EditVeterinaryScreen
import com.example.veterinaryfinder.presentation.HomeScreen
import com.example.veterinaryfinder.presentation.LoginScreen
import com.example.veterinaryfinder.presentation.RegisterScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }

        composable("register") {
            RegisterScreen(navController = navController)
        }

        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("add_veterinaria") {
            AddVeterinaryScreen(navController = navController)
        }

        composable("edit_veterinaria/{veterinaryId}") { backStackEntry ->
            val veterinaryId = backStackEntry.arguments?.getString("veterinaryId")?.toIntOrNull()
            if (veterinaryId != null) {
                EditVeterinaryScreen(navController = navController, veterinaryId = veterinaryId)
            }
        }
    }
}
