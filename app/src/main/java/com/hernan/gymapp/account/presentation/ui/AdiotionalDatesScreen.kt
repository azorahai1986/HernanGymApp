package com.hernan.gymapp.account.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.hernan.gymapp.R
import com.hernan.gymapp.account.data.model.AdditionalDateDto
import com.hernan.gymapp.account.presentation.ui.camera.OpenCamera
import com.hernan.gymapp.account.presentation.ui.camera.PermissionCustomCamera
import com.hernan.gymapp.account.presentation.ui.camera.RequestCameraPermission
import com.hernan.gymapp.account.presentation.viewmodel.AdditionalDateViewModel
import com.hernan.gymapp.common.Constants
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.common.ui_app.text_views.EditTextCustom
import com.hernan.gymapp.common.ui_app.toolbar.ToolbarUsersType
import com.skydoves.landscapist.glide.GlideImage
import java.io.ByteArrayInputStream

@Composable
fun AdditionalDatesScreen(
    additionalDateViewModel: AdditionalDateViewModel,
    navController: NavHostController,
    name: String?,
    phoneNumber: String?,
    email: String?
) {

    if (email != null) {
        Log.d("INGRESA A ADDIOTIONAL SCREEN", email)
    }
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    ShowView(userId, additionalDateViewModel, name, phoneNumber)


}

@Composable
fun ShowView(
    userId: String?,
    additionalDateViewModel: AdditionalDateViewModel,
    name: String?,
    phoneNumber: String?
) {

    val focusManager = LocalFocusManager.current
    var capturedImage by remember { mutableStateOf<ByteArray?>(null) }

    var age by remember { mutableStateOf("") }
    var profilePhoto by remember { mutableStateOf("") }

    val ageFocusRequester = remember { FocusRequester() }


    val viewModelState = additionalDateViewModel.uploadImage.collectAsState()



    when (val state = viewModelState.value) {
        is ResourceFlow.Loading -> Log.d("STATE", "CARGANDO")
        is ResourceFlow.Success -> {
            profilePhoto = state.data
            Log.d("ADDITIONAL STATE", state.data)
        }
        is ResourceFlow.Error -> Log.d("STATE", "ESTADO ERROR ${state.message}")
    }

    val userAccounted = additionalDateViewModel.userAcconted.collectAsState()

    Log.d("ADDITIONAL STATE 2", profilePhoto)

    when (val state = userAccounted.value) {
        is ResourceFlow.Loading -> Log.d("STATE", "CARGANDO")
        is ResourceFlow.Success -> {
            Log.d("USER ACCOUNTED STATE", state.data.toString())
        }
        is ResourceFlow.Error -> Log.d("STATE", "ESTADO ERROR ${state.message}")
    }

    val dataUserAccounted = AdditionalDateDto(
        typeId = "Basic",
        name = name ?: "No Name",
        userImage = profilePhoto,
        classes = "0",
        edad = age,
        estadoSalud = "Bueno",
        objetivo = "Bajar grasas. Quemar calorias",
        phoneNumber = phoneNumber ?: "0000-0000",
        weight = "180 cm",
        gender = "Masculino",
        pay = false
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
            .imePadding()
    ) {
        item { ToolbarUsersType("Información adicional para $name") }
        item { UserImage(
            userId = userId,
            onImageCaptured = { imageBytes ->
                capturedImage = imageBytes
                // Apenas captes la imagen, subila a storage
                additionalDateViewModel.setimageToStorage(userId, imageBytes)

            }
        ) }
        item { EditTextCustom(
            text = age,
            onValueChange = {age = it},
            textType = Constants.SIMPLE_TEXT,
            elevationText = "Ingresa tu nombre",
            hint = "",
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { ageFocusRequester.requestFocus() }
            ),
            modifier = Modifier.focusRequester(ageFocusRequester)

        ) }

        item { OutlinedButton(
            enabled = true,
            onClick = {
                    additionalDateViewModel.saveUser(
                        collectionPath = Constants.COLLECTION_MEMBERS,
                        documentId = userId ?: "No User",
                        user = dataUserAccounted)

            }
        ) }
    }
}

@Composable
fun UserImage(
    userId: String?,
    onImageCaptured: (ByteArray) -> Unit
)
{

    val context = LocalContext.current
    var profileImageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var showCamera by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    /**
     * Abro la camara
     * **/

    PermissionCustomCamera()
    RequestCameraPermission(
        onPermissionGranted = { hasCameraPermission = true },
        onPermissionDenied = { /* manejar si querés */ }
    )

    if (hasCameraPermission) {
        if (showCamera) {
            OpenCamera(
                userId = userId ?: "",
                onImageCaptured = { imageBytes ->
                    profileImageBytes = imageBytes
                    onImageCaptured(imageBytes)
                    showCamera = false // Después de sacar la foto, cerramos la cámara
                },
                onError = { exception ->
                    Log.e("ProfileScreen", "Error al capturar imagen", exception)
                    showCamera = false // También cerramos si hubo error
                }
            )
        } else {
            GlideImage(

                imageModel = {
                    profileImageBytes?.let {
                        rotateBitmapIfNeeded(context, it) // ya corregido
                    } ?: R.drawable.avatar
                },
                modifier = Modifier
                    .size(250.dp)
                    .padding(15.dp)
                    .clip(CircleShape)
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    )
                    .clickable {
                        showCamera = true
                    }
            )
        }
    }


}

@Composable
fun OutlinedButton(enabled:Boolean ,onClick: () -> Unit) {
    OutlinedButton(
        onClick = {
            onClick()},
        enabled = enabled,
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .padding(horizontal = 55.dp),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = Color.White,
            disabledContentColor = MaterialTheme.colorScheme.surfaceContainerLow,
            disabledContainerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Text("Registrarse")
    }
}

fun rotateBitmapIfNeeded(context: Context, bytes: ByteArray): Bitmap {
    val originalBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

    val exif = ExifInterface(ByteArrayInputStream(bytes))
    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )

    val matrix = Matrix()
    when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
    }

    return Bitmap.createBitmap(
        originalBitmap, 0, 0,
        originalBitmap.width, originalBitmap.height,
        matrix, true
    )
}