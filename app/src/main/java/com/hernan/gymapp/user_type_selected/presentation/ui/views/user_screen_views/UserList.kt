package com.hernan.gymapp.user_type_selected.presentation.ui.views.user_screen_views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hernan.gymapp.R
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.UserDetailViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserList(
    users: UserTypeSelected,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    userDetailViewModel: UserDetailViewModel = hiltViewModel()
) {

    val bottomSheetHeight = with(LocalDensity.current) { 400.dp.toPx() }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(vertical = 10.dp, horizontal = 5.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary, // Cambiá este color como quieras
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        onClick = {
            userDetailViewModel.setSelectedUser(users) // 📌 Guardamos el usuario seleccionado en el ViewModel
            coroutineScope.launch {
                bottomSheetScaffoldState.bottomSheetState.expand() // Abrimos el BottomSheet
            }
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            GlideImage(
                imageModel = { users.userImage ?: R.drawable.logo_eze_solo_nombre }, // Evita que image sea null
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(30.dp))

            Text(
                text = users.name ?: "Usuario desconocido", // Maneja caso nulo
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.surfaceTint
            )
        }
        //TODO ver si es bueno agregar una luz por tipo de cliente. como una especie de semaforo señalador. Por ejemplo:
        // Si es Basisc - Luz verde
        // Si es MediaForce - Luz Amarilla
        // Si es Strongest - Luz Roja.
        // O ponerle un iconito distintivo. estudiar eso...
    }
}
