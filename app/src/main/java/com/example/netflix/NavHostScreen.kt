package com.example.netflix

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavHostScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable(route = "login") {
            LogInScreen (onNavigateToSignUp = {
                navController.navigate("signup")
            }, onNavigateToHome = {
                navController.navigate("home")
            })
        }
        composable(route = "home") {
            HomeScreen {
                navController.navigate("home")
            }
        }
        composable(route = "signup") {
            SignUpScreen {
                navController.navigate("login")
            }
        }
    }
}