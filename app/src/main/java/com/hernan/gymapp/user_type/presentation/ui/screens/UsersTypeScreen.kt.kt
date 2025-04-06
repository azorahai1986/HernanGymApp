package com.hernan.gymapp.user_type.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.user_type.presentation.viewmodel.ClientViewModel
import com.hernan.gymapp.user_type.domain.model.Client
import com.hernan.gymapp.user_type.presentation.ui.tipe_users_view.UserListTypesUsers
import com.hernan.gymapp.common.ui_app.toolbar.ToolbarUsersType


@Composable
fun UsersTypeScreen(
    isdarkTheme: Boolean,
    navController: NavHostController,
    text: String?,
    clientViewModel: ClientViewModel
) {
    Scaffold(
        topBar = { ToolbarUsersType(text) },
        containerColor = MaterialTheme.colorScheme.background, // Asegura el fondo del Scaffold
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(MaterialTheme.colorScheme.background) // Fondo uniforme
            ) {
                GetData(clientViewModel, navController)
            }
        }
    )
}

@Composable
fun GetData(clientViewModel: ClientViewModel, navController: NavHostController) {
    val viewModelState = clientViewModel.clientState.collectAsState()

    LaunchedEffect(clientViewModel) {
        if (viewModelState.value !is ResourceFlow.Success) {
            clientViewModel.fetchClient("")
        }
    }

    when (val state = viewModelState.value) {
        is ResourceFlow.Loading -> Log.d("STATE", "CARGANDO")
        is ResourceFlow.Success -> ColumnToListTypes(state.data, navController)
        is ResourceFlow.Error -> Log.d("STATE", "ESTADO ERROR ${state.message}")
    }
}

@Composable
fun ColumnToListTypes(data: List<Client>, navController: NavHostController) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(vertical = 70.dp, horizontal = 16.dp),
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.scrim)

        ) {
        items(data) { client ->
            UserListTypesUsers(
                client.image,
                client.typeClient,
                navController
            )
        }
    }

}

