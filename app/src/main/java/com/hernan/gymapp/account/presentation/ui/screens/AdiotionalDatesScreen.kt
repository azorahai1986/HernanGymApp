package com.hernan.gymapp.account.presentation.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.hernan.gymapp.R
import com.hernan.gymapp.account.data.model.AdditionalDateDto
import com.hernan.gymapp.account.presentation.ui.camera.OpenCamera
import com.hernan.gymapp.account.presentation.ui.camera.PermissionCustomCamera
import com.hernan.gymapp.account.presentation.ui.camera.RequestCameraPermission
import com.hernan.gymapp.account.presentation.ui.views.LoadinAnimation
import com.hernan.gymapp.account.presentation.viewmodel.AdditionalDateViewModel
import com.hernan.gymapp.common.Constants
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.common.ui_app.text_views.EditTextCustom
import com.hernan.gymapp.common.ui_app.toolbar.ToolbarUsersType
import com.hernan.gymapp.navigation.Screens
import com.skydoves.landscapist.glide.GlideImage
import java.io.ByteArrayInputStream

@Composable
fun AdditionalDatesScreen(
    additionalDateViewModel: AdditionalDateViewModel,
    navController: NavHostController,
    name: String?,
    phoneNumber: String?
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    //TimeToPhotoView()
    GetDataImage(additionalDateViewModel, userId, name, phoneNumber, navController)
}


@Composable
fun GetDataImage(
    additionalDateViewModel: AdditionalDateViewModel,
    userId: String?,
    name: String?,
    phoneNumber: String?,
    navController: NavHostController
) {
    var profilePhoto by remember { mutableStateOf("") }
    var isSuccessImage by remember { mutableStateOf(false) }
    var isImageLoading by remember { mutableStateOf(false) }
    val viewModelImage = additionalDateViewModel.uploadImage.collectAsState()

    when (val state = viewModelImage.value) {
        is ResourceFlow.Idle -> {}
        is ResourceFlow.Loading -> {
            LoadinAnimation(true) // La animación entra en pantalla
            isImageLoading = true
            Log.d("ADDITIONAL STATE", "Cargando")
        }
        is ResourceFlow.Success -> {  // La imagen se cargó. se obtiene el Url string del Storage
            profilePhoto = state.data
            LoadinAnimation(false)  // La animación sale de pantalla
            isSuccessImage = true
            isImageLoading = false
            Log.d("ADDITIONAL STATE", state.data)
        }
        is ResourceFlow.Error -> Log.d("STATE", "ESTADO ERROR ${state.message}")
    }

    ShowView(userId, additionalDateViewModel, name, phoneNumber, profilePhoto, isSuccessImage, isImageLoading, navController)

}

@Composable
fun ShowView(
    userId: String?,
    additionalDateViewModel: AdditionalDateViewModel,
    name: String?,
    phoneNumber: String?,
    profilePhoto: String,
    isSuccessImage: Boolean,
    isImageLoading: Boolean,
    navController: NavHostController,
) {

    val focusManager = LocalFocusManager.current
    var capturedImage by remember { mutableStateOf<ByteArray?>(null) }
    var age by remember { mutableStateOf("") }
    var healthStatus by remember { mutableStateOf("") }
    var bodyWeight by remember { mutableStateOf("") }

    val ageFocusRequester = remember { FocusRequester() }
    val healthStatusFocusRequester = remember { FocusRequester() }
    val bodyWeightFocusRequester = remember { FocusRequester() }

    val isButtonEnabled = age.isNotBlank() && healthStatus.isNotBlank() && bodyWeight.isNotBlank()

    val userAccounted = additionalDateViewModel.userAcconted.collectAsState()


    when (val state = userAccounted.value) {
        is ResourceFlow.Idle -> {}
        is ResourceFlow.Loading -> Log.d("STATE", "CARGANDO")
        is ResourceFlow.Success -> {
            Log.d("USER ACCOUNTED STATE", state.data.toString())
            LoadinAnimation(true)
            navController.navigate(route = Screens.MainScreen.routes)
        }
        is ResourceFlow.Error -> Log.d("STATE", "ESTADO ERROR ${state.message}")
    }

    val dataUserAccounted = AdditionalDateDto(
        typeId = "Basic",
        name = name ?: "No Name",
        userImage = profilePhoto,
        classes = "0",  // se completará cuando el cliente realice un pago. Segun la forma. si paga por clase o por mes, etc
        edad = age,
        estadoSalud = healthStatus,  /**TODO colocar un spinner**/
        objetivo = "Bajar grasas. Quemar calorias", /**TODO colocar un spinner ó un radioButton, que pueda elegir mas de una opción. evaluar eso**/
        phoneNumber = phoneNumber ?: "0000-0000",
        weight = bodyWeight,
        gender = "Masculino", /**TODO colocar un spinner**/
        pay = false
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .imePadding()
    ) {

        if (!isSuccessImage) {
            item { ToolbarUsersType("Información adicional para $name") }
            item { CentredText("Es momento de tomarte una selfie. O si lo prefieres elegí una foto desde tu galería", 18.sp)
                Spacer(modifier = Modifier.height(16.dp)) }
        }

        if (!isImageLoading) {
            item { UserImage(
                imageFromsStorage = profilePhoto,
                userId = userId,
                onImageCaptured = { imageBytes ->

                    capturedImage = imageBytes
                    // Apenas captes la imagen, subila a storage
                    additionalDateViewModel.setimageToStorage(userId, imageBytes)
                }
            )
            }
        }

        item {
            if (isImageLoading) {
                CentredText("Cargando Imagen. Por favor espera...", 12.sp)
            } else {
                CentredText("Elige una imagen de la galería", 12.sp)
            }
        }

        if (isSuccessImage) {
            item { EditTextCustom(
                text = age,
                onValueChange = {age = it},
                textType = Constants.SIMPLE_TEXT,
                elevationText = "Ingresa tu edad",
                hint = "",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { healthStatusFocusRequester.requestFocus() }
                ),
                modifier = Modifier.focusRequester(ageFocusRequester)

            ) }

            item { EditTextCustom(
                text = healthStatus,
                onValueChange = {healthStatus = it},
                textType = Constants.SIMPLE_TEXT,
                elevationText = "Dinos tu estado de salud",
                hint = "",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { bodyWeightFocusRequester.requestFocus() }
                ),
                modifier = Modifier.focusRequester(healthStatusFocusRequester)

            ) }

            item { EditTextCustom(
                text = bodyWeight,
                onValueChange = {bodyWeight = it},
                textType = Constants.SIMPLE_TEXT,
                elevationText = "Peso corporal",
                hint = "",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier.focusRequester(bodyWeightFocusRequester)
            ) }


            item { OutlinedButton(
                enabled = isButtonEnabled,
                onClick = {
                    additionalDateViewModel.saveUser(
                        collectionPath = Constants.COLLECTION_MEMBERS,
                        documentId = userId ?: "No User",
                        user = dataUserAccounted)

                }
            ) }
        }
    }
}

@Composable
fun UserImage(
    imageFromsStorage: String,
    userId: String?,
    onImageCaptured: (ByteArray) -> Unit
)
{

    var showCamera by remember { mutableStateOf(false) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    /**
     * Solicito los permisos necesarios y
     * abro la camara
     * **/

    PermissionCustomCamera()
    RequestCameraPermission(
        onPermissionGranted = { hasCameraPermission = true },
        onPermissionDenied = { /* manejar si querés */ }
    )

    if (hasCameraPermission) {
        if (showCamera) {
            OpenCamera(
                onImageCaptured = { imageBytes ->
                    onImageCaptured(imageBytes)
                    showCamera = false // Después de sacar la foto, cerramos la cámara
                },
                onError = { exception ->
                    showCamera = false // También cerramos si hubo error
                }
            )
        } else {
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GlideImage(

                    imageModel = {
                        if (imageFromsStorage.isNotBlank()) imageFromsStorage else R.drawable.avatar
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

@Composable
fun CentredText(text: String, size: TextUnit) {
    Spacer(modifier = Modifier.height(48.dp))

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        text = text,
        fontSize = size,
        textAlign = TextAlign.Center,
        color = Color.White
    )
}