package com.hernan.gymapp.routins_and_excercises.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.routins_and_excercises.domain.model.RoutinService
import com.hernan.gymapp.routins_and_excercises.presentation.viewmodel.RoutineViewModel
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.SimpleYouTubePlayerOptionsBuilder
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayer
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayerHostState
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubePlayerState
import io.github.ilyapavlovskii.multiplatform.youtubeplayer.YouTubeVideoId


@Composable
fun RoutineScreen(
    navController: NavHostController,
    string: String?,
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
        is ResourceFlow.Success -> VideoPlayer(
            state.data
        )
        is ResourceFlow.Error -> Log.d("STATE", "ESTADO ERROR ${state.message}")
    }


}

@Composable
fun VideoPlayer(data: List<RoutinService>) {

    val hostStates = remember { data.map { YouTubePlayerHostState() } }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        itemsIndexed(data) { index, item ->
            val hostState = hostStates[index]

            // Lógica para cargar el video cuando el player esté listo
            if (hostState.currentState == YouTubePlayerState.Ready) {
                LaunchedEffect(item.videoLink) {
                    hostState.loadVideo(YouTubeVideoId(item.videoLink))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = item.videoName,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )

                YouTubePlayer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    hostState = hostState,
                    options = SimpleYouTubePlayerOptionsBuilder.builder {
                        autoplay(false)
                        controls(true)
                        fullscreen = true
                    }
                )
            }
        }
    }
}