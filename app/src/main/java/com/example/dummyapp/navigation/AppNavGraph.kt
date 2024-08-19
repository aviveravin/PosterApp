package com.example.dummyapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dummyapp.componenets.HomeScreen
import com.example.dummyapp.componenets.PhoneNumberScreen
import com.example.dummyapp.componenets.ProfileDetailsScreen
import com.example.dummyapp.components.CreatePosterScreen
import com.example.dummyapp.viewmodel.UserViewModel

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: UserViewModel) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { PhoneNumberScreen(navController, viewModel) }
        composable("user_profile") { ProfileDetailsScreen(navController = navController, viewModel = viewModel)}
        composable("home") { HomeScreen(navController, viewModel) }
        composable("poster") { CreatePosterScreen(navController, viewModel) }
    }

}
