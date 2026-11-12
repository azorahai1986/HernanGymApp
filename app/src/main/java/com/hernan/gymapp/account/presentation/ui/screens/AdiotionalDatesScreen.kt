package com.hernan.gymapp.account.presentation.ui.screens

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.hernan.gymapp.R
import com.hernan.gymapp.account.data.model.AdditionalDateDto
import com.hernan.gymapp.account.presentation.ui.camera.OpenCamera
import com.hernan.gymapp.account.presentation.ui.views.radiobutton.CustomRadioButton
import com.hernan.gymapp.account.presentation.viewmodel.AdditionalDateViewModel
import com.hernan.gymapp.common.Constants
import com.hernan.gymapp.common.state.ResourceFlow
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
    AdditionalProfileScreen(additionalDateViewModel, userId, name, phoneNumber, navController)
}

@Composable
private fun AdditionalProfileScreen(
    viewModel: AdditionalDateViewModel,
    userId: String?,
    name: String?,
    phoneNumber: String?,
    navController: NavHostController
) {
    var profilePhoto by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var bodyWeight by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var health by remember { mutableStateOf("") }
    var objective by remember { mutableStateOf("") }
    val uploadState by viewModel.uploadImage.collectAsState()
    val saveState by viewModel.userAcconted.collectAsState()
    val isUploading = uploadState is ResourceFlow.Loading
    val parsedAge = age.toIntOrNull()
    val parsedWeight = bodyWeight.toFloatOrNull()
    val canSubmit = parsedAge != null && parsedAge in 1..120 &&
        parsedWeight != null && parsedWeight in 1f..500f &&
        gender.isNotBlank() && health.isNotBlank() && objective.isNotBlank()

    LaunchedEffect(uploadState) {
        if (uploadState is ResourceFlow.Success) profilePhoto = (uploadState as ResourceFlow.Success<String>).data
    }
    LaunchedEffect(saveState) {
        if (saveState is ResourceFlow.Success) {
            navController.navigate(Screens.CalendaryScreen.routes) {
                popUpTo(Screens.AccountScreen.routes) { inclusive = true }
            }
        }
    }

    val member = AdditionalDateDto(
        typeId = "Basic",
        name = name.orEmpty(),
        userImage = profilePhoto,
        classes = "0",
        edad = "$age años",
        estadoSalud = health,
        objetivo = objective,
        phoneNumber = phoneNumber.orEmpty(),
        weight = "$bodyWeight kg",
        gender = gender,
        pay = false,
        preferredDays = emptyList(),
        preferredHours = emptyMap(),
        weeklyClassLimit = 2,
        scheduleMode = "FIXED"
    )

    androidx.compose.material3.Scaffold(
        topBar = { ToolbarUsersType("Completar perfil") },
        containerColor = MaterialTheme.colorScheme.background
    ) { insets ->
        Column(
            modifier = Modifier.fillMaxSize().padding(insets).imePadding()
                .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Perfil del socio", style = MaterialTheme.typography.headlineSmall)
            Text("PASO 2 DE 2", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            androidx.compose.material3.LinearProgressIndicator(progress = 1f, modifier = Modifier.fillMaxWidth())
            Text("Algunos datos ayudan a adaptar su entrenamiento.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    ProfilePhotoPicker(
                        photoUrl = profilePhoto,
                        enabled = !isUploading,
                        onImageCaptured = { image -> viewModel.setimageToStorage(userId, image) }
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Foto de perfil (opcional)", style = MaterialTheme.typography.titleSmall)
            Text("Tocá la imagen para tomar una foto; también podés omitirla.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (isUploading) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        if (uploadState is ResourceFlow.Error) Text("No se pudo subir la foto. Podés continuar sin ella.", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            OutlinedTextField(
                value = age,
                onValueChange = { age = it.filter(Char::isDigit).take(3) },
                modifier = Modifier.fillMaxWidth(), label = { Text("Edad") }, suffix = { Text("años") },
                singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = MaterialTheme.shapes.large
            )
            OutlinedTextField(
                value = bodyWeight,
                onValueChange = { bodyWeight = it.filter { char -> char.isDigit() || char == '.' || char == ',' }.replace(',', '.').take(6) },
                modifier = Modifier.fillMaxWidth(), label = { Text("Peso corporal") }, suffix = { Text("kg") },
                singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = MaterialTheme.shapes.large
            )

            ChoiceSection("Género", listOf("Masculino", "Femenino", "No binario"), gender) { gender = it }
            ChoiceSection("Estado de salud", listOf("Bueno", "Regular", "Malo"), health) { health = it }
            ChoiceSection("Objetivo principal", listOf("Quemar grasas", "Tonificar músculos", "Incrementar masa muscular", "Otro"), objective) { objective = it }

            Text("Tus turnos", style = MaterialTheme.typography.titleMedium)
            Text(
                "Después de crear tu perfil, vas a elegir la frecuencia, los días y horarios en el calendario.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (saveState is ResourceFlow.Error) {
                Text((saveState as ResourceFlow.Error).message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Button(
                onClick = {
                    if (!userId.isNullOrBlank()) {
                        viewModel.saveUser(Constants.COLLECTION_MEMBERS, userId, member)
                    }
                },
                enabled = canSubmit && !isUploading && saveState !is ResourceFlow.Loading && !userId.isNullOrBlank(),
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
            ) {
                if (saveState is ResourceFlow.Loading) CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                else Text("Finalizar registro")
            }
            if (userId.isNullOrBlank()) Text("No pudimos identificar la cuenta creada. Volvé a iniciar el registro.", color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ChoiceSection(title: String, options: List<String>, selected: String, onSelected: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall)
        CustomRadioButton(
            selectedOption = selected,
            onOptionSelected = onSelected,
            radioOptions = options
        )
    }
}

@Composable
private fun ProfilePhotoPicker(photoUrl: String, enabled: Boolean, onImageCaptured: (ByteArray) -> Unit) {
    val context = LocalContext.current
    var showCamera by remember { mutableStateOf(false) }
    val permission = android.Manifest.permission.CAMERA
    var hasCameraPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        hasCameraPermission = granted
        if (granted) showCamera = true
    }

    if (showCamera) {
        OpenCamera(
            onImageCaptured = { bytes -> onImageCaptured(bytes); showCamera = false },
            onError = { showCamera = false }
        )
    } else {
        GlideImage(
            imageModel = { if (photoUrl.isBlank()) R.drawable.avatar else photoUrl },
            modifier = Modifier.size(76.dp).clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable(enabled = enabled) {
                    if (hasCameraPermission) showCamera = true else permissionLauncher.launch(permission)
                }
        )
    }
}
