package com.hernan.gymapp.account.presentation.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.hernan.gymapp.R
import com.hernan.gymapp.account.data.model.AdditionalDateDto
import com.hernan.gymapp.account.presentation.ui.camera.OpenCamera
import com.hernan.gymapp.account.presentation.ui.camera.PermissionCustomCamera
import com.hernan.gymapp.account.presentation.ui.camera.RequestCameraPermission
import com.hernan.gymapp.account.presentation.ui.views.radiobutton.CustomRadioButton
import com.hernan.gymapp.common.ui_app.animations.LoadinAnimation
import com.hernan.gymapp.account.presentation.viewmodel.AdditionalDateViewModel
import com.hernan.gymapp.common.Constants
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.common.ui_app.buttons.OutlinedRoundedButton
import com.hernan.gymapp.common.ui_app.text_views.CentredTextField
import com.hernan.gymapp.common.ui_app.text_views.EditTextCustom
import com.hernan.gymapp.common.ui_app.toolbar.ToolbarUsersType
import com.hernan.gymapp.navigation.Screens
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun AdditionalDatesScreen(
    additionalDateViewModel: AdditionalDateViewModel,
    navController: NavHostController,
    name: String?,
    phoneNumber: String?
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
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
    val selectedOption = remember { mutableStateOf("") }

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
            item { ToolbarUsersType(stringResource(R.string.info_for_toolbar, name ?: "Usuario")) }
            item { CentredTextField(stringResource(R.string.info_for_selfies), 18.sp)
                Spacer(modifier = Modifier.height(16.dp)) }
        }

        if (!isImageLoading) {
            item { UserImage(
                imageFromsStorage = profilePhoto,
                onImageCaptured = { imageBytes ->
                    capturedImage = imageBytes
                    // Apenas captes la imagen, subila a storage
                    additionalDateViewModel.setimageToStorage(userId, imageBytes)
                })
            }
        }

        item {
            if (isImageLoading) {
                CentredTextField("Cargando Imagen. Por favor espera...", 12.sp)
            } else {
                CentredTextField("Elige una imagen de la galería", 12.sp)
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
                modifier = Modifier
                    .focusRequester(ageFocusRequester),
                validatorHasErrors = false

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
                modifier = Modifier.focusRequester(healthStatusFocusRequester),
                validatorHasErrors = false
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
                modifier = Modifier.focusRequester(bodyWeightFocusRequester),
                validatorHasErrors = false
            ) }
            item { CustomRadioButton(
                selectedOption = selectedOption.value,
                onOptionSelected = {selectedOption.value = it},
                modifier = Modifier,


                )}

            item { OutlinedRoundedButton(
                enabled = isButtonEnabled,
                onClick = {
                    additionalDateViewModel.saveUser(
                        collectionPath = Constants.COLLECTION_MEMBERS,
                        documentId = userId ?: "No User",
                        user = dataUserAccounted)

                },
                text = "Registrarse"
            ) }
        }
    }
}
/**
 * LLamo a la imagen circular con la imagen de user. si se concede el permiso se podra tomar una selfie
 * **/
@Composable
fun UserImage(
    imageFromsStorage: String,
    onImageCaptured: (ByteArray) -> Unit
)
{

    val context = LocalContext.current
    var showCamera by remember { mutableStateOf(false) }
    var shootToast by remember { mutableStateOf(false) }
    /**
     * Solicito los permisos necesarios y
     * abro la camara
     * **/

    val permission = android.Manifest.permission.CAMERA
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        )
    }
    /**Obtengo el permiso y cambio el valor de "hasCameraPermission"
     * Con "rememberLauncherForActivityResult" actualizo en tiemporeal el permiso desde el click en la imagen **/
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

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
                            if (!hasCameraPermission) {
                                permissionLauncher.launch(permission)
                                shootToast = true
                            } else {
                                showCamera = true
                            }

                        }
                )
            }
        }

    /** si no tengo permiso. Lanzo un Toast informando donde solicitarlo en la configuracion**/
    if (shootToast) {
        ShowToastMessage(
            message = stringResource(R.string.go_to_config_for_camera_permission),
            context = context
        )
        shootToast = false
    }
}

@Composable
fun ShowToastMessage(message: String, context: Context) {
    val duration = Toast.LENGTH_SHORT

    val toast = Toast.makeText(context, message, duration)
    toast.show()
}

/**
 * TODO crear los spinner para el genero, para el estado de salud, y objetivos.
 * **/