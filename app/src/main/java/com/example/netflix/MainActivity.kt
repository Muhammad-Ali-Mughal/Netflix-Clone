package com.example.netflix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.netflix.ui.theme.NetflixTheme

data class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NetflixTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "home"
        ),
        BottomNavigationItem(
            title = "Search",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.icons8_search_25),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.icons8_search_25),
            route = "search"
        ),
        BottomNavigationItem(
            title = "Downloads",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.frame_5),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.frame_5),
            route = "downloads"
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.user__1__1),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.user__1__1),
            route = "profile"
        ),
    )

    val selectedItemIndex = rememberSaveable { mutableStateOf(0) }
    val navController = rememberNavController()
    val context = LocalContext.current
    val loginManager = remember { LogInManager(context) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    items = items,
                    selectedItemIndex = selectedItemIndex.value,
                    onItemSelected = { index, route ->
                        selectedItemIndex.value = index
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    backgroundColor = Color.Black
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                NavigationHost(navController, loginManager)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavigationItem>,
    selectedItemIndex: Int,
    onItemSelected: (index: Int, route: String) -> Unit,
    backgroundColor: Color
) {
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = { onItemSelected(index, item.route) },
                label = { Text(text = item.title) },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = if (index == selectedItemIndex) {
                            item.selectedIcon
                        } else item.unselectedIcon,
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}

@Composable
fun NavigationHost(navController: NavHostController, loginManager: LogInManager) {
    NavHost(navController = navController, startDestination = "login") {
        composable("home") {
            HomeScreen(
                onNavigateToHome = { navController.navigate("home") },
                onNavigateToMovieDetails = { navController.navigate("movie-details") }
            )
        }
        composable(route = "login") {
            LogInScreen(
                onNavigateToSignUp = {
                    navController.navigate("signup")
                },
                onNavigateToHome = {
                    navController.navigate("home")
                },
                message = null, // Pass the error message here
                loginManager = loginManager
            )
        }
        composable("signup") {
            SignUpScreen(
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                loginManager = loginManager
            )
        }
        composable("profile") {
            Profile(
                onNavigateToHome = {
                    navController.navigate("home")
                },
            )
        }
        composable("downloads") {
            DownloadsScreen()
        }
        composable(route = "search") {
            SearchScreen (
                onNavigateToMovieDetails = { navController.navigate("movie-details") }
            )
        }
        composable("movie-details") {
            MovieDetailScreen(
                onNavigateToHome = { navController.navigate("home") },
                onNavigateToMovieDetails = { navController.navigate("movie-details") }
            )
        }
    }
}
