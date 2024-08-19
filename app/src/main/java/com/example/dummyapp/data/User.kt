package com.example.dummyapp.data

import android.net.Uri
import androidx.compose.ui.graphics.painter.Painter
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val phoneNumber: String = "",
    val name: String = "",
    val designation: String = "",
    val state: String = "",
    val imageUri: Uri? = null
)

