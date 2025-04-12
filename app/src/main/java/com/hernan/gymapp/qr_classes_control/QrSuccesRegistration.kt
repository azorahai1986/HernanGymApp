package com.hernan.gymapp.qr_classes_control

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.hernan.gymapp.R
import com.hernan.gymapp.navigation.Screens
import kotlinx.coroutines.delay

@Composable
fun QrSuccessRegistration(navController: NavHostController) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ok_tilde_animation))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = 1, // solo una vez
            speed = 2f // Velocidad x2
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(300.dp)
            )
        }

    // Volver automáticamente luego de 4 segundos
    LaunchedEffect(Unit) {
        delay(4000)
        navController.navigate(route = Screens.MainScreen.routes){
            popUpTo(0) { inclusive = true }
        }
    }

    BackHandler {
        navController.navigate(route = Screens.MainScreen.routes){
            popUpTo(0) { inclusive = true }
        }
    }

    /**
     * TODO una vez separadas las capas entre cliente y Administrador. Deberá desidirse la acción del boton hacia atras.
     *  si volverá a la pantalla inicial o solo hacia atras.
     *
     *  TODO Agregar Pantalla de icono de error por si el QR no es el correcto
     * **/
}