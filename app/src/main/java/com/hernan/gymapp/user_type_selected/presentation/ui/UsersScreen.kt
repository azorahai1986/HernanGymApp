package com.hernan.gymapp.user_type_selected.presentation.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.common.ui_app.PersonDateScreen
import com.hernan.gymapp.common.ui_app.toolbar.ToolbarUsersType
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.UserTypeSelectedVM
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
private var bottomState: BottomSheetScaffoldState? = null
var coroutineScoper: CoroutineScope? = null
private var textScreen: String? = null

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UsersScreen(
    isdarkTheme: Boolean,
    navController: NavHostController,
    text: String?,
    userTypeSelectedVM: UserTypeSelectedVM
) {

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    bottomState = bottomSheetScaffoldState
    coroutineScoper = coroutineScope

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {

                       goToUserInfo(text = "")
            /*Column {
                // Aquí puedes poner el contenido de tu bottom sheet
                Button(onClick = { /* Handle bottom sheet item click */ }) {
                    // Botón dentro del bottom sheet
                }
            }*/
        },
        content = {
            // Contenido principal de la pantalla
            /*Button(onClick = {
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.expand()
                }
            }) {
                // Botón para mostrar el bottom sheet
            }*/
            Scaffold(
                topBar = { ToolbarUsersType(text) }, // el toolbar es reutilizado del package common. se ahorra codigo
                content = { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        GetData(userTypeSelectedVM, text)
                    }
                }
            )
        }
    )


    textScreen = text
}

@Composable
fun GetData(userTypeSelectedVM: UserTypeSelectedVM, text: String?) {
    val viewModelState = userTypeSelectedVM.userType.collectAsState()

    LaunchedEffect(viewModelState) {
        if (viewModelState.value !is ResourceFlow.Success) {
            userTypeSelectedVM.fetchClient(text ?: "")
        }
    }

    when (val state = viewModelState.value) {
        is ResourceFlow.Loading -> Log.d("STATE", "CARGANDO")
        is ResourceFlow.Success -> ColumnToList(state.data)
        is ResourceFlow.Error -> Log.d("STATE", "ESTADO ERROR ${state.message}")
    }
}

@Composable
fun ColumnToList(data: List<UserTypeSelected>) {
    Log.d("USER SELECTED", data.toString())
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(vertical = 60.dp, horizontal = 16.dp),
        modifier = Modifier.background(color = MaterialTheme.colorScheme.scrim)

        ) {

        items(data) { users ->
            UserList(
                users.userImage,
                users.typeId
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserList(image: String, text: String) {
    val bottomSheetHeight = with(LocalDensity.current) { 400.dp.toPx() }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(vertical = 10.dp, horizontal = 5.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
        ), // Cambia a tu color deseado
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ),
        onClick = {
            //goToNextScreen("text")
            coroutineScoper!!.launch {
                bottomState!!.bottomSheetState.expand()
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
                imageModel = {image},
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.padding(horizontal = 30.dp))
            Text(text,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.surfaceTint)

            // Otros elementos de la tarjeta
        }
    }
}

@Composable
private fun goToUserInfo(text: String) {

   PersonDateScreen()
}
    //navControl.navigate(route = Screens.BottomDialogUserData.routes + "/$text")

