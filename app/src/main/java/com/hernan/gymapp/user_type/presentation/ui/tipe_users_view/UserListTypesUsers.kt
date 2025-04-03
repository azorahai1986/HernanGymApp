package com.hernan.gymapp.user_type.presentation.ui.tipe_users_view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hernan.gymapp.navigation.Screens
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun UserListTypesUsers(image: String, text: String, navController: NavHostController) {

    Card(
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .clickable { navController.navigate(Screens.UsersScreen.routes + "/$text") },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ), // Cambia a tu color deseado
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            GlideImage(
                imageModel = {image},
                modifier = Modifier
                    .fillMaxSize()
                    .clip(AbsoluteRoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.padding(horizontal = 30.dp))

            Text(text,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.surfaceTint,
                modifier = Modifier.padding(start = 20.dp, bottom = 16.dp).align(Alignment.BottomStart))
        }
    }
}