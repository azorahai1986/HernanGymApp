package com.hernan.gymapp.routins_and_excercises.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.navigation.Screens
import com.hernan.gymapp.routins_and_excercises.domain.model.RoutinService
import com.hernan.gymapp.routins_and_excercises.presentation.viewmodel.RoutineViewModel


@Composable
fun RoutineScreen(
    navController: NavHostController,
    text: String?,
    routineViewModel: RoutineViewModel
) {

    val viewModelState = routineViewModel.routines.collectAsState()

    LaunchedEffect(routineViewModel) {
        if (viewModelState.value !is ResourceFlow.Success) {
            routineViewModel.fetchRoutineData()
        }
    }

    when (val state = viewModelState.value) {
        is ResourceFlow.Loading -> Log.d("STATE", "CARGANDO")
        is ResourceFlow.Success -> RoutineList(state.data, navController, text)
        is ResourceFlow.Error -> Log.d("STATE", "ESTADO ERROR ${state.message}")
    }


}

 @Composable
 fun RoutineList(data: List<RoutinService>, navController: NavHostController, text: String?) {
     LazyColumn (modifier = Modifier
         .fillMaxWidth()
         .padding(16.dp)
     ){
         items(data) { video ->
             InfoText(video.videoName, navController, text)
         }
     }
 }

@Composable
fun InfoText(videoName: String, navController: NavHostController, text: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { navController.navigate(Screens.VideoPlayer.routes + "/$text") }
            .clip(RoundedCornerShape(16.dp)) // 🔁 PRIMERO: recorta la forma
            .background(MaterialTheme.colorScheme.surfaceBright) // 🎨 SEGUNDO: fondo ya recortado
            .border( // 🔲 TERCERO: borde externo
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = videoName,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(14.dp),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.surfaceTint
        )

        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Ir",
            tint = Color.Gray,
            modifier = Modifier.padding(end = 12.dp)
        )
    }
}