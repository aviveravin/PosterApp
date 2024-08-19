package com.example.dummyapp.componenets

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.dummyapp.viewmodel.UserViewModel

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileDetailsScreen(navController: NavHostController, viewModel: UserViewModel) {
    val userProfile by viewModel.userProfile.collectAsState()

    var name by remember { mutableStateOf(userProfile.name) }
    var designation by remember { mutableStateOf(userProfile.designation) }
    var state by remember { mutableStateOf(userProfile.state) }
    var imageUri by remember { mutableStateOf(userProfile.imageUri) }

    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Profile", style = MaterialTheme.typography.labelLarge)

        // Image upload section
        Box(modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray)
            .clickable { launcher.launch("image/*") }
        ) {
            imageUri?.let {
                Image(
                    painter = rememberImagePainter(data = it),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } ?: run {
                Icon(Icons.Default.Person, contentDescription = "Profile Image")
            }
        }

        Text(text = "Upload your photo", style = MaterialTheme.typography.bodySmall)

        // Input fields
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        TextField(
            value = designation,
            onValueChange = { designation = it },
            label = { Text("Designation") }
        )
        TextField(
            value = state,
            onValueChange = { state = it },
            label = { Text("State") }
        )

        // Save & Next button
        Button(
            onClick = {
                viewModel.updateUserProfile(name, designation, state, imageUri)
                navController.navigate("home")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(

                containerColor = Color.Black
            )
        ) {
            Text("Save & Next", color = Color.White)
        }
    }
}

