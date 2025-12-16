package com.example.optifit

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.optifit.ui.theme.OptiFitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sharedPref = getSharedPreferences("OptiFit", Context.MODE_PRIVATE)
        val isFirstLaunch = sharedPref.getBoolean("first_launch", true)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)

        setContent {
            OptiFitTheme {
                var showWelcome by remember { mutableStateOf(isFirstLaunch && isLoggedIn) }
                val navController = rememberNavController()

                if (showWelcome) {
                    WelcomeScreen(
                        onGetStartedClicked = {
                            sharedPref.edit().putBoolean("first_launch", false).apply()
                            showWelcome = false
                        }
                    )
                } else {
                    NavHost(
                        navController = navController,
                        startDestination = if (isLoggedIn) "home" else "login"
                    ) {
                        composable("login") {
                            LoginScreen(navController)
                        }
                        composable("signup") {
                            SignupScreen(navController)
                        }
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("chat") {
                            ChatScreen(navController)
                        }
                        composable("planner") {
                            PlannerScreen(navController)
                        }
                        composable("preferences") {
                            PreferencesScreen(navController)
                        }
                        composable("workout/{workoutId}") { backStackEntry ->
                            val workoutId = backStackEntry.arguments?.getString("workoutId") ?: ""
                            WorkoutDetailScreen(navController, workoutId)
                        }
                    }
                }
            }
        }
    }
}