package com.hernan.gymapp.account.presentation.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import com.hernan.gymapp.account.presentation.viewmodel.RegisterViewModel
import com.hernan.gymapp.common.state.ResourceFlow

@Composable
fun AccountScreen(
    navController: NavHostController,
    accountViewModel: RegisterViewModel
) {
    val viewmodel = accountViewModel.state.collectAsState()

    LaunchedEffect(accountViewModel) {
        if (viewmodel.value !is ResourceFlow.Success) {
            accountViewModel.register("hernan@torres.com", "123456789")
        }
    }

    when (val state = viewmodel.value) {
        is ResourceFlow.Loading -> Log.d("STATE", "CARGANDO")
        is ResourceFlow.Success -> Log.d("STATE ACCOUNT", state.data.toString())
        is ResourceFlow.Error -> Log.d("STATE", "ESTADO ERROR ${state.message}")
    }

}