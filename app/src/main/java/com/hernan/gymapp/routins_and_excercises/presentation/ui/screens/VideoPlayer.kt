package com.hernan.gymapp.routins_and_excercises.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun VideoPlayer(
    videoId: String?,
    videoName: String?
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = videoName ?: "",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )

        CustomYouTubePlayer(
            videoId = videoId ?: "No se encuentra video",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            repeatOnEnd = true // 👈 cambia a false si no querés repetirlo
        )
    }
}

@Composable
fun CustomYouTubePlayer(
    videoId: String,
    modifier: Modifier = Modifier,
    repeatOnEnd: Boolean = true
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            YouTubePlayerView(ctx).apply {
                // Opcional: oculta sugerencias y modo de pantalla completa
                enableAutomaticInitialization = false

                val lifecycleOwner = context as? LifecycleOwner
                lifecycleOwner?.lifecycle?.addObserver(this)

                initialize(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.loadVideo(videoId, 0f)
                    }

                    override fun onStateChange(
                        youTubePlayer: YouTubePlayer,
                        state: PlayerConstants.PlayerState
                    ) {
                        if (state == PlayerConstants.PlayerState.ENDED && repeatOnEnd) {
                            youTubePlayer.loadVideo(videoId, 0f)
                        }
                    }
                })
            }
        },
        update = { } // No hace falta actualizar nada por ahora
        /**
         * TODO Pasos a seguir. Crear un QR para fichage del cliente.
         *  deberá haber un QR en el dispositivo del administrador.
         *  El cliente deberá escanear el QR del dispositivo del Administrador.
         *  Al escanear se descontará una clase disponible de la base de datos.
         *  habiendo consumido una clase el cliente.
         * **/
    )
}

