package com.hernan.gymapp.qr_classes_control

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.hernan.gymapp.common.Constants
import com.hernan.gymapp.navigation.Screens

@Composable
fun QRScannerScreen(navController: NavHostController, viewModel: QRScannerViewModel) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    val qrCodeData by viewModel.qrCodeData.collectAsState()
    LaunchedEffect(qrCodeData) {
        if (qrCodeData != Constants.SCAN_QR &&
            qrCodeData != Constants.QR_DOESNT_READ &&
            !qrCodeData.startsWith(Constants.ERROR)
        ){
            val qrStatus = if (qrCodeData == Constants.CORRECT_OWNER_NAME) true else false
            navController.navigate(route = Screens.QrSuccessRegistration.routes + "/$qrStatus")
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Resultado del QR: $qrCodeData")
        CameraPreview(viewModel = viewModel)
    }
}