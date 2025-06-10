package xyz.potasyyum.mynotes.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orhanobut.logger.Logger


@Composable
fun App() {
    val navController = rememberNavController()
    Logger.d("hello");

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            Text("HOME")
        }
    }
}