package xyz.potasyyum.mynotes.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orhanobut.logger.Logger
import xyz.potasyyum.mynotes.presentation.screen.TaskView
import xyz.potasyyum.mynotes.presentation.viewmodel.TaskViewModel


@Composable
fun App() {
    val navController = rememberNavController()
    val taskViewModel = hiltViewModel<TaskViewModel>()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            TaskView()
        }
    }
}