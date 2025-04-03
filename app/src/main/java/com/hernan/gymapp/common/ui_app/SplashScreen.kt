package com.hernan.gymapp.common.ui_app

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.hernan.gymapp.R
import com.hernan.gymapp.navigation.Screens
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        //ContainerViewApp()
        Image(painter = painterResource(id = R.drawable.logo_negro),
            contentDescription = "Logo Inicio",
            contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize())
    }

    LaunchedEffect(Unit) {
        delay(1000) // Retraso de 5 segundos
        navController.navigate(route = Screens.MainScreen.routes)



    }
}