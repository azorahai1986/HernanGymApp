package com.hernan.gymapp.user_type_selected.presentation.ui.screens.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.common.ui_app.toolbar.ToolbarUsersType
import com.hernan.gymapp.user_type_selected.presentation.ui.screens.botomsheet.PersonDateScreen
import com.hernan.gymapp.user_type_selected.presentation.ui.views.user_screen_views.ColumnToUserList
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.UserTypeSelectedVM
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.ALL_USERS_FILTER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UsersScreen(
    isdarkTheme: Boolean,
    navController: NavHostController,
    text: String?,
    userTypeSelectedVM: UserTypeSelectedVM,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope
) {

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContainerColor = androidx.compose.material3.MaterialTheme.colorScheme.background,
        sheetShape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        sheetTonalElevation = 0.dp,
        sheetContent = {
            PersonDateScreen(
                onClose = { coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.hide() } }
            )
        },
        content = {
            Scaffold(
                topBar = {
                    ToolbarUsersType(if (text == ALL_USERS_FILTER) "Todos los socios" else text)
                },
                content = { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        GetData(
                            userTypeSelectedVM,
                            text,
                            bottomSheetScaffoldState,
                            coroutineScope
                        )
                    }
                }
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetData(
    userTypeSelectedVM: UserTypeSelectedVM,
    text: String?,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
) {
    val viewModelState = userTypeSelectedVM.userType.collectAsState()

    LaunchedEffect(text) { userTypeSelectedVM.fetchClient(text.orEmpty()) }

    when (val state = viewModelState.value) {
        is ResourceFlow.Idle -> Unit
        is ResourceFlow.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        is ResourceFlow.Success -> ColumnToUserList(
            state.data,
            bottomSheetScaffoldState,
            coroutineScope
        )
        is ResourceFlow.Error -> Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (state.message.contains("No hay miembros", ignoreCase = true)) {
                    if (text == ALL_USERS_FILTER) "Todavía no hay socios registrados." else "Todavía no hay socios en esta categoría."
                } else "No pudimos cargar los socios.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (!state.message.contains("No hay miembros", ignoreCase = true)) {
                Button(onClick = { userTypeSelectedVM.fetchClient(text.orEmpty()) }) {
                    Text("Reintentar")
                }
            }
        }
    }
}

