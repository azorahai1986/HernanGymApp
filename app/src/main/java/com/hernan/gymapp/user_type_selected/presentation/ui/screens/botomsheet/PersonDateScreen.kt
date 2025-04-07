package com.hernan.gymapp.user_type_selected.presentation.ui.screens.botomsheet

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hernan.gymapp.R
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import com.hernan.gymapp.user_type_selected.presentation.ui.views.bottomsheet_view.InfoBox
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.UserDetailViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PersonDateScreen(
    userDetailViewModel: UserDetailViewModel = hiltViewModel()
) {
    val bottomSheetHeight = with(LocalDensity.current) { 10.dp.toPx() }
    val userState by userDetailViewModel.selectedUser.collectAsState()

    Box(
        modifier = Modifier
            .wrapContentSize()
            .background(color = MaterialTheme.colorScheme.secondary)
            .fillMaxHeight(0.85f),
        contentAlignment = Alignment.Center
    ) {
        var bottomSheetVisible by remember { mutableStateOf(true) }

        val targetHeight = if (bottomSheetVisible) bottomSheetHeight else 0f
        val animatableHeight = remember { Animatable(targetHeight) }
        LaunchedEffect(bottomSheetVisible) {
            animatableHeight.animateTo(
                targetValue = targetHeight,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = LinearOutSlowInEasing
                )
            )
        }

        userState?.let { user ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        translationY = animatableHeight.value
                    }
                    .background(MaterialTheme.colorScheme.background)
                    .padding(26.dp)
            ){
                GlideImage(
                    imageModel = { user.userImage.ifBlank { R.drawable.logo_eze_solo_nombre } },
                    modifier = Modifier
                        .height(250.dp)
                        .width(250.dp)
                        .padding(15.dp)
                        .clip(CircleShape)
                        .border(
                            width = 4.dp, // Grosor del borde
                            color = MaterialTheme.colorScheme.secondaryContainer, // Color del borde
                            shape = CircleShape
                        )
                        .align(CenterHorizontally)
                )

                // 🟢 HACEMOS QUE LA LISTA SEA SCROLLEABLE
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    item { InfoBox(user, "PersonalData") }
                    item { InfoBox(user, "Health") }
                    item { InfoBox(user, "Payment") }
                }
            }

            // DateUser(R.drawable.telephone_24, )
            // TODO hacer que los datos que lleguen a DateUser sean dinamicos. Tal vez
            //  reutilizar la vista de UserList. o la de la pantalla de inicio. Agregar
            //  un item por cada valor string y agregar un icono particular para cada texto desde Firestore
            //  Si ha pagado la cuota tal vez  podria ir un icono de tilde de color llamativo. si no una cruz o algo en tono apagado
            //  agregar telefono e icono, si tiene alguna enfermedad de base, si quiere adelagasar o
            //  volumen. toma medicacion, cual y para que. Si tiene lesiones, cual. edad, altura, peso, objetivo. alguna vez entreno.
            //  Seria interezante agregar un sppiner con tiempos: ,as de un mes. menos de un mes. mas de un año. menos de un año,
            //  intermitentemente etc
            //  Cuantas clases le quedan y cuantas pago.


            //TODO Agregar videos con ejercicios de muestra. segun su plan de usuario.

        }
    }
}