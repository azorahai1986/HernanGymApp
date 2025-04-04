package com.hernan.gymapp.user_type_selected.presentation.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hernan.gymapp.R
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.UserDetailViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PersonDateScreen(
    userDetailViewModel: UserDetailViewModel = hiltViewModel()
) {
    val bottomSheetHeight = with(LocalDensity.current) { 20.dp.toPx() }
    val userState by userDetailViewModel.selectedUser.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        var bottomSheetVisible by remember { mutableStateOf(true) }

        val targetHeight = if (bottomSheetVisible) bottomSheetHeight else 0f
        val animatableHeight = remember { Animatable(targetHeight) }
        LaunchedEffect(bottomSheetVisible) {
            animatableHeight.animateTo(
                targetValue = targetHeight,
                animationSpec = tween(
                    durationMillis = 400,
                    easing = LinearOutSlowInEasing
                )
            )
        }

        userState?.let { user ->
        Column(
            Modifier
                .graphicsLayer {
                    translationY = animatableHeight.value
                }
                .fillMaxSize()
                .padding(16.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
        ) {
            GlideImage(
                imageModel = { user.userImage ?: R.drawable.logo_eze_solo_nombre }, // 🔥 Carga la imagen desde los recursos
                modifier = Modifier
                    .height(250.dp)
                    .width(250.dp)
                    .padding(15.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally)
            )

           // DateUser(R.drawable.telephone_24, )
            // TODO hacer que los datos que lleguen a DateUser sean dinamicos. Tal vez
            //  reutilizar la vista de UserList. o la de la pantalla de inicio. Agregar
            //  un item por cada valor string y agregar un icono particular para cada texto desde Firestore
            //  Si ha pagado la cuota tal vez  podria ir un icono de tilde de color llamativo. si no una cruz o algo en tono apagado

            DateUser(user.name)
            DateUser(user.typeId)
        }

        } ?: Text(text = "Selecciona un usuario")
    }
}

@Composable
private fun DateUser(text: String) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 1.dp, horizontal = 15.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .align(CenterHorizontally),
        ) {
            Image(
                painter = painterResource(R.drawable.telephone_24),
                contentDescription = null,
                modifier = Modifier
                    .height(50.dp)
                    .width(100.dp)
                    .padding(10.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
            Text(text = text,
                Modifier
                    .padding(5.dp)
                    .align(alignment = Alignment.CenterVertically)
            )

        }

    }

}