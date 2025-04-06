package com.hernan.gymapp.user_type_selected.presentation.ui.screens.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.common.ui_app.toolbar.ToolbarUsersType
import com.hernan.gymapp.user_type_selected.presentation.ui.screens.botomsheet.PersonDateScreen
import com.hernan.gymapp.user_type_selected.presentation.ui.views.user_screen_views.ColumnToUserList
import com.hernan.gymapp.user_type_selected.presentation.viewmodel.UserTypeSelectedVM
import kotlinx.coroutines.CoroutineScope

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
        sheetContent = { PersonDateScreen() }, // 🔥 Llamado directo, eliminando función extra
        content = {
            Scaffold(
                topBar = { ToolbarUsersType(text) },
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

    LaunchedEffect(viewModelState) {
        if (viewModelState.value !is ResourceFlow.Success) {
            userTypeSelectedVM.fetchClient(text ?: "")
        }
    }

    when (val state = viewModelState.value) {
        is ResourceFlow.Loading -> Log.d("STATE", "CARGANDO")
        is ResourceFlow.Success -> ColumnToUserList(
            state.data,
            bottomSheetScaffoldState,
            coroutineScope
        )
        is ResourceFlow.Error -> Log.d("STATE", "ESTADO ERROR ${state.message}")
    }
}

