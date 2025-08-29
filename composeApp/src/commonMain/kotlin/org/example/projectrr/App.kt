package org.example.projectrr


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.projectrr.routing.Routes
import org.example.projectrr.screens.MainScreen
import org.example.projectrr.screens.SplashScreen

@Composable
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(Color.White)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val navController = rememberNavController()
            NavHost(
                startDestination = Routes.Splash,
                navController = navController
            ){
                composable<Routes.Splash> {
                    SplashScreen(navController = navController)
                }
                composable<Routes.Main> {
                    MainScreen()
                }
            }
        }
    }
}