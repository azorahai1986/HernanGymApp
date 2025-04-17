package com.hernan.gymapp.main_screen.common.presentation.ui.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hernan.gymapp.R
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.main_screen.admin_qr.domain.model.QrAdminInfo
import com.hernan.gymapp.main_screen.list_elements.domain.model.FirstState
import com.hernan.gymapp.main_screen.common.presentation.viewmodel.StateViewModel
import com.hernan.gymapp.navigation.Screens
import com.hernan.gymapp.common.ui_app.toolbar.ToolbarUsersType
import com.hernan.gymapp.main_screen.common.presentation.ui.views.QrImage

@Composable
fun MainScreen(
    isdarkTheme: Boolean,
    navController: NavHostController,
    firstViewModel: StateViewModel,
) {

    GetData(firstViewModel, navController)
}

@Composable
private fun GetData(firstViewModel: StateViewModel, navController: NavHostController) {
    val firstState by firstViewModel.firstState.collectAsState()
    val qrState by firstViewModel.qrCheckIn.collectAsState()

    if (firstState is ResourceFlow.Success && qrState is ResourceFlow.Success) {
        val stateData = (firstState as ResourceFlow.Success).data
        val qrData = (qrState as ResourceFlow.Success).data

        InflateList(stateData, navController, qrData)
    }

    if (firstState is ResourceFlow.Loading || qrState is ResourceFlow.Loading) {
        Log.d("STATE", "CARGANDO")
    }

    if (firstState is ResourceFlow.Error) {
        Log.d("STATE", "ERROR: ${(firstState as ResourceFlow.Error).message}")
    }

    if (qrState is ResourceFlow.Error) {
        Log.d("STATE", "QR ERROR: ${(qrState as ResourceFlow.Error).message}")
    }
}
@Composable
private fun InflateList(
    stateData: List<FirstState>,
    navController: NavHostController,
    qrData: List<QrAdminInfo>
) {
    LazyColumn(horizontalAlignment =Alignment.CenterHorizontally
        ,modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(color = MaterialTheme.colorScheme.scrim)) {

        item { ToolbarUsersType("") }
        item { BackImage(R.drawable.logo_eze_solo_nombre) }
        items(stateData) {label ->
            OptionsApp(MaterialTheme.colorScheme.surfaceTint, label.name, navController)
        }
        item { QrImage(qrData) }

    }
}
@Composable
fun BackImage(image: Int) {
    Image(
        painter = painterResource(image),  //R.drawable.ab1_inversions
        contentDescription = "Imagen Principal",
        contentScale = ContentScale.Crop, // puedo dar 3 estilos de imagen. Fit, FillBounds y Crop
        modifier = Modifier
            .fillMaxHeight()
            .width(260.dp)
            .clip(RectangleShape)
            .padding(vertical = 12.dp)

    )
}

@Composable
fun OptionsApp(backColor: Color, text: String, navController: NavHostController) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .height(86.dp)
            .padding(vertical = 10.dp, horizontal = 55.dp)
            .border(
                width = 2.dp,
                color = Color.White,
        shape = RoundedCornerShape(18.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ), // Cambia a tu color deseado
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ),
        onClick = {
            // funcion para pasar a la siguiente pantalla
            goToUserInfo(text, context, navController)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = text, color = MaterialTheme.colorScheme.surfaceTint)
        }
    }

}

private fun goToUserInfo(text: String, context: Context, navControl: NavHostController) {
    when(text){
        "Turnos" -> navControl.navigate(route = Screens.TurnsScreen.routes + "/Turnos")
        "Rutinas" -> navControl.navigate(route = Screens.RoutineScreen.routes + "/Rutinas")
        "Tipos de clientes" -> navControl.navigate(route = Screens.UsersTypeScreen.routes + "/Tipos de clientes")
        "QR Scanner" -> navControl.navigate(route = Screens.QRScannerScreen.routes + "/\"QR Scanner\"")
    }

}


