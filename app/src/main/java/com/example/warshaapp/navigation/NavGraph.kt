package com.example.warshaapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.warshaapp.screens.sharedScreens.authentication.login.AuthenticationViewModel
import com.example.warshaapp.screens.sharedScreens.authentication.login.LoginScreen
import com.example.warshaapp.screens.sharedScreens.splash.SplashScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AllScreens.SplashScreen.name
    ) {

        composable(route = AllScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(route = AllScreens.LoginScreen.name) {
            val authenticationViewModel = hiltViewModel<AuthenticationViewModel>()
            LoginScreen(
                navController = navController,
                authenticationViewModel = authenticationViewModel
            )
        }
    }
}