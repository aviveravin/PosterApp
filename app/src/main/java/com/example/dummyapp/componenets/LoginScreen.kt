package com.example.dummyapp.componenets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.dummyapp.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberScreen(navController: NavHostController, viewModel: UserViewModel) {
    var phoneNumber by remember { mutableStateOf(viewModel.userProfile.value.phoneNumber) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") }
        )
        Button(onClick = {
            viewModel.updatePhoneNumber(phoneNumber)
            navController.navigate("user_profile")
        }) {
            Text("Next")
        }
    }
}


@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    val viewModel = UserViewModel() // Create a mock or real ViewModel instance

    PhoneNumberScreen(navController = navController, viewModel = viewModel)
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreenPreview()
}
