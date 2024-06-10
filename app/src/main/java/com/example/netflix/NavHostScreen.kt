package com.example.netflix

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavHostScreen() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val loginManager = LogInManager(context = context)

    NavHost(navController = navController, startDestination = "login") {
        composable(route = "login") {
            LogInScreen(
                onNavigateToSignUp = { navController.navigate("signup") },
                onNavigateToHome = { navController.navigate("home") },
                message = null,
                loginManager = loginManager
            )
        }
        composable(route = "signup") {
            SignUpScreen(
                onNavigateToLogin = { navController.navigate("login") },
                loginManager = loginManager
            )
        }
        composable(route = "home") {
            HomeScreen(
                onNavigateToHome = { navController.navigate("home") },
                onNavigateToMovieDetails = { navController.navigate("movie-details") }
            )
        }
        composable(route = "profile") {
            Profile (
                onNavigateToHome = { navController.navigate("home") },
            )
        }
        composable(route = "downloads") {
            DownloadsScreen ()
        }
        composable(route = "search") {
            SearchScreen (
                onNavigateToMovieDetails = { navController.navigate("movie-details") }
            )
        }
        composable(route = "movie-details") {
            MovieDetailScreen(
                onNavigateToHome = { navController.navigate("home") },
                onNavigateToMovieDetails = { navController.navigate("movie-details") }
            )
        }
    }
}