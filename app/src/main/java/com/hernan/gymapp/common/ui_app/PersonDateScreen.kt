package com.hernan.gymapp.common.ui_app

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hernan.gymapp.R
import com.hernan.gymapp.funtions.userList
import kotlinx.coroutines.launch


var userImage: Int? = null
var userName: Int? = null

@Composable
fun PersonDateScreen(){

    userList().map { item ->
        userName = item.text
        userImage = item.drawable
    }
    val bottomSheetHeight = with(LocalDensity.current) { 20.dp.toPx() }

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
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
        }

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
            Image(
                painter = painterResource(R.drawable.fc4_self_massage),  //R.drawable.ab1_inversions
                contentDescription = null,
                contentScale = ContentScale.Crop, // puedo dar 3 estilos de imagen. Fit, FillBounds y Crop
                modifier = Modifier
                    .height(250.dp)
                    .width(250.dp)
                    .padding(15.dp)
                    .clip(CircleShape)
                    .align(Alignment.CenterHorizontally)
            )

           // DateUser(R.drawable.telephone_24, )
            DateUser()
            DateUser()

        }

        // Toggle bottom sheet visibility
        /*coroutineScoper!!.launch {
            bottomSheetVisible = !bottomSheetVisible
        }*/
    }
}

@Composable
private fun DateUser(){

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
            Text(text = "Main Content",
                Modifier
                    .padding(5.dp)
                    .align(alignment = Alignment.CenterVertically)
            )

        }

    }

}