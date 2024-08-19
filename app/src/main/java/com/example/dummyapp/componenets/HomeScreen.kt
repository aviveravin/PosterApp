package com.example.dummyapp.componenets

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dummyapp.viewmodel.UserViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HomeScreen(navController: NavHostController, viewModel: UserViewModel) {
    val user = viewModel.userProfile.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome, ${user.name ?: "User"}!")
        Text("Phone: ${user.phoneNumber}")
        Text("Job Position: ${user.designation}")
        Text("State: ${user.state}")
        Button(onClick = { navController.navigate("poster") }) {
            Text(text = "Create Poster")
        }
    }
}
