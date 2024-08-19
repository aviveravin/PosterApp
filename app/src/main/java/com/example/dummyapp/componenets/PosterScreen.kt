package com.example.dummyapp.components

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.example.dummyapp.data.User
import com.example.dummyapp.viewmodel.UserViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("StateFlowValueCalledInComposition", "RememberReturnType")
@OptIn(ExperimentalCoilApi::class)
@Composable
fun CreatePosterScreen(navController: NavHostController, viewModel: UserViewModel) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val user = remember { viewModel.userProfile.value }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text(text = "Upload Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Container for the uploaded image or placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .border(2.dp, Color.Gray) // Add border or other styling as needed
        ) {
            if (imageUri != null) {
                val bitmap = uriToBitmap(context, imageUri!!)
                bitmap?.let {
                    val userInfo =
                        "Name: ${user.name}\nDesignation: ${user.designation}\nState: ${user.state}"
                    val bitmapWithText = addTextToBitmap(context, bitmap, user)
                    Image(
                        bitmap = bitmapWithText.asImageBitmap(),
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                // Show a placeholder image if no image is uploaded
                // Replace with your placeholder image if available
                // Image(
                //     painter = painterResource(id = R.drawable.placeholder_image),
                //     contentDescription = "Placeholder Image",
                //     modifier = Modifier.fillMaxSize()
                // )
            }
            // Display user information with red background at the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                // The column containing user information
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = "Name: ${user.name}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Designation: ${user.designation}",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    user.imageUri?.let { uri ->
                        Image(
                            painter = rememberImagePainter(uri),
                            contentDescription = "User Image",
                            modifier = Modifier
                                .align(Alignment.BottomEnd) // Aligns the image to the right bottom corner
                                .offset(y = (-8).dp) // Adjusts the offset
                        )
                    }
                }

            }


        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            imageUri?.let { uri ->
                val bitmap = uriToBitmap(context, uri)
                bitmap?.let {
                    val userInfo =
                        "Name: ${user.name}\nDesignation: ${user.designation}\nState: ${user.state}"
                    val bitmapWithText = addTextToBitmap(context, bitmap, user)
                    val file = saveBitmapToFile(context, bitmapWithText, "poster_with_label.jpg")
                    Toast.makeText(context, "Image downloaded with label!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }) {
            Text(text = "Download Image")
        }

        Button(onClick = {
            imageUri?.let { uri ->
                val bitmap = uriToBitmap(context, uri)
                bitmap?.let {
                    val userInfo =
                        "Name: ${user.name}\nDesignation: ${user.designation}\nState: ${user.state}"
                    val bitmapWithText = addTextToBitmap(context, bitmap, user)
                    val file = saveBitmapToFile(context, bitmapWithText, "poster_with_label.jpg")
                    if (file != null) {
                        shareImage(context, file)
                    }
                }
            }
        }) {
            Text(text = "Share Image")
        }
    }
}

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val parcelFileDescriptor =
            context.contentResolver.openFileDescriptor(uri, "r") ?: return null
        val fileDescriptor = parcelFileDescriptor.fileDescriptor
        val bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun addTextToBitmap(context: Context, bitmap: Bitmap, user: User): Bitmap {
    val newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
    val canvas = Canvas(newBitmap)

    // Draw the uploaded image
    canvas.drawBitmap(bitmap, 0f, 0f, null)

    // User information
    val userInfo = "Name: ${user.name}\nDesignation: ${user.designation}\nState: ${user.state}"

    // Paint for user info text
    val textPaint = Paint().apply {
        textSize = 48f
        isAntiAlias = true
        color = android.graphics.Color.WHITE
        setShadowLayer(1f, 0f, 1f, android.graphics.Color.BLACK)
    }

    // Paint for user info box
    val boxHeight = 150f
    val boxPaint = Paint().apply {
        color = android.graphics.Color.RED
    }

    // Draw background for user info box
    canvas.drawRect(
        0f, newBitmap.height - boxHeight,
        newBitmap.width.toFloat(), newBitmap.height.toFloat(),
        boxPaint
    )

    // Draw user info text
    val x = 50f
    var y = newBitmap.height - boxHeight + 50f
    userInfo.split("\n").forEach { line ->
        canvas.drawText(line, x, y, textPaint)
        y += textPaint.descent() - textPaint.ascent() // Move to the next line
    }

    // Draw user image if available
    user.imageUri?.let { uri ->
        val userImageBitmap = uriToBitmap(context, uri)
        userImageBitmap?.let { userBitmap ->
            val userImageScale = (newBitmap.width / 4).toFloat() / userBitmap.width
            val scaledUserImageHeight = userBitmap.height * userImageScale
            val scaledUserImageWidth = userBitmap.width * userImageScale
            val userImageX = newBitmap.width - scaledUserImageWidth - 16f
            val userImageY = newBitmap.height - scaledUserImageHeight - 16f

            canvas.drawBitmap(
                userBitmap,
                null,
                RectF(
                    userImageX,
                    userImageY,
                    userImageX + scaledUserImageWidth,
                    userImageY + scaledUserImageHeight
                ),
                null
            )
        }
    }

    return newBitmap
}


fun saveBitmapToFile(context: Context, bitmap: Bitmap, fileName: String): Uri? {
    val contentResolver = context.contentResolver
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/YourAppName") // Change as needed
    }

    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

    uri?.let {
        contentResolver.openOutputStream(it)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
        }
    }

    return uri
}

fun shareImage(context: Context, uri: Uri) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/jpeg"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
}

