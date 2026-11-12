package com.hernan.gymapp.account.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.hernan.gymapp.account.domain.model.AccountData
import com.hernan.gymapp.account.presentation.viewmodel.PhoneNumberViewModel
import com.hernan.gymapp.account.presentation.viewmodel.RegisterViewModel
import com.hernan.gymapp.account.presentation.viewmodel.VerifyEmailViewModel
import com.hernan.gymapp.account.presentation.viewmodel.VerifyPasswordViewModel
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.common.ui_app.toolbar.ToolbarUsersType
import com.hernan.gymapp.navigation.Screens

@Composable
fun AccountScreen(navController: NavHostController, accountViewModel: RegisterViewModel) {
    ViewAccountScreen(accountViewModel, navController)
}

fun navigateToAdittionalsDate(data: AccountData, navControl: NavHostController) {
    navControl.navigate(Screens.AdditionalDateScreen.routes + "/${data.name}" + "/${data.phoneNumber}")
}

@Composable
fun ViewAccountScreen(accountViewModel: RegisterViewModel, navController: NavHostController) {
    var name by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    val emailViewModel: VerifyEmailViewModel = viewModel()
    val phoneViewModel: PhoneNumberViewModel = viewModel()
    val passwordViewModel: VerifyPasswordViewModel = viewModel()
    val accountState by accountViewModel.state.collectAsState()

    val accountData = AccountData(
        name = name,
        email = emailViewModel.email,
        phoneNumber = phoneViewModel.phoneNumber,
        password = passwordViewModel.password
    )
    val canContinue = name.isNotBlank() && emailViewModel.email.isNotBlank() &&
        phoneViewModel.phoneNumber.isNotBlank() && passwordViewModel.password.isNotBlank() &&
        !emailViewModel.emailHasErrors && !phoneViewModel.phoneNumberHasErrors &&
        !passwordViewModel.passwordHasErrors

    if (accountState is ResourceFlow.Success) {
        LaunchedEffect((accountState as ResourceFlow.Success<*>).data) {
            navigateToAdittionalsDate(accountData, navController)
        }
    }

    Scaffold(
        topBar = { ToolbarUsersType("Registro") },
        containerColor = MaterialTheme.colorScheme.background
    ) { insets ->
        Column(
            modifier = Modifier.fillMaxSize().padding(insets).imePadding()
                .verticalScroll(rememberScrollState()).padding(horizontal = 22.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Sumá un nuevo socio", style = MaterialTheme.typography.headlineSmall)
            Text("PASO 1 DE 2", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            androidx.compose.material3.LinearProgressIndicator(progress = 0.5f, modifier = Modifier.fillMaxWidth())
            Text("Datos de acceso", style = MaterialTheme.typography.titleMedium)
            Text("Usaremos estos datos para crear su cuenta.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre completo") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                shape = MaterialTheme.shapes.large
            )
            OutlinedTextField(
                value = emailViewModel.email,
                onValueChange = emailViewModel::updateEmail,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo electrónico") },
                singleLine = true,
                isError = emailViewModel.emailHasErrors,
                supportingText = { if (emailViewModel.emailHasErrors) Text("Revisá el formato del correo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                shape = MaterialTheme.shapes.large
            )
            OutlinedTextField(
                value = phoneViewModel.phoneNumber,
                onValueChange = phoneViewModel::updatePhoneNumber,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Teléfono") },
                singleLine = true,
                isError = phoneViewModel.phoneNumberHasErrors,
                supportingText = { if (phoneViewModel.phoneNumberHasErrors) Text("Ingresá entre 8 y 15 dígitos; podés incluir el prefijo +54") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                shape = MaterialTheme.shapes.large
            )
            OutlinedTextField(
                value = passwordViewModel.password,
                onValueChange = passwordViewModel::updatePassword,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                singleLine = true,
                isError = passwordViewModel.passwordHasErrors,
                supportingText = { if (passwordViewModel.passwordHasErrors) Text("Usá al menos 8 caracteres, una mayúscula y un número") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                shape = MaterialTheme.shapes.large
            )

            if (accountState is ResourceFlow.Error) {
                Text((accountState as ResourceFlow.Error).message, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            Button(
                onClick = {
                    submitted = true
                    accountViewModel.register(accountData)
                },
                enabled = canContinue && !submitted,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                if (submitted && accountState is ResourceFlow.Loading) {
                    CircularProgressIndicator(modifier = Modifier.padding(end = 10.dp), strokeWidth = 2.dp)
                    Text("Creando cuenta…")
                } else Text("Continuar")
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Text("¿Ya tiene cuenta?", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }

    if (accountState is ResourceFlow.Error && submitted) {
        LaunchedEffect(accountState) { submitted = false }
    }
}
