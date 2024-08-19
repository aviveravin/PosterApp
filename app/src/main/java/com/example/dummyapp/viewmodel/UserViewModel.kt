package com.example.dummyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.dummyapp.data.User
import android.net.Uri
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {

    // Private mutable state flow
    private val _userProfile = MutableStateFlow(User())

    // Public state flow
    val userProfile: StateFlow<User> = _userProfile.asStateFlow()

    // Function to update phone number
    fun updatePhoneNumber(phoneNumber: String) {
        _userProfile.value = _userProfile.value.copy(phoneNumber = phoneNumber)
    }

    // Function to update user profile details
    fun updateUserProfile(name: String, designation: String, state: String, imageUri: Uri?) {
        _userProfile.value = _userProfile.value.copy(
            name = name,
            designation = designation,
            state = state,
            imageUri = imageUri
        )
    }
}
