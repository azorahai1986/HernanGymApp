package com.hernan.gymapp.account.presentation.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.hernan.gymapp.account.domain.model.AccountData
import com.hernan.gymapp.account.presentation.viewmodel.RegisterViewModel
import com.hernan.gymapp.common.Constants
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.common.ui_app.text_views.EditTextCustom
import com.hernan.gymapp.common.ui_app.toolbar.ToolbarUsersType
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AccountScreen(
    navController: NavHostController,
    accountViewModel: RegisterViewModel
) {
    val viewmodel = accountViewModel.state.collectAsState()

    when (val state = viewmodel.value) {
        is ResourceFlow.Loading -> Log.d("STATE", "CARGANDO")
        is ResourceFlow.Success -> Log.d("STATE ACCOUNT", state.data.toString())
        is ResourceFlow.Error -> Log.d("STATE", "ESTADO ERROR ${state.message}")
    }

    ViewAccountScreen(accountViewModel)

}

@Composable
fun ViewAccountScreen(accountViewModel: RegisterViewModel) {

    val focusManager = LocalFocusManager.current

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val nameFocusRequester = remember { FocusRequester() }
    val emailFocusRequester = remember { FocusRequester() }
    val phoneFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    val isButtonEnabled = name.isNotBlank() && email.isNotBlank() && phoneNumber.isNotBlank() && password.isNotBlank()
    val accountData = AccountData(name, email, "20/03/1986", phoneNumber, "X", password)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
            .imePadding()
    ) {


            ToolbarUsersType("Registrate como usuario")

            EditTextCustom(
                text = name,
                onValueChange = {name = it},
                textType = Constants.SIMPLE_TEXT,
                elevationText = "Ingresa tu nombre",
                hint = "",
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { emailFocusRequester.requestFocus() }
                ),
                modifier = Modifier.focusRequester(nameFocusRequester)

            )

            EditTextCustom(
                text = email,
                onValueChange = {email = it},
                textType = Constants.EMAIL,
                elevationText = "Ingresa un email válido",
                hint = "",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { phoneFocusRequester.requestFocus() }
                ),
                modifier = Modifier.focusRequester(emailFocusRequester)

             )

            EditTextCustom(
                text = phoneNumber,
                onValueChange = {phoneNumber = it},
                textType = Constants.SIMPLE_TEXT,
                elevationText = "número telefónico",
                hint = "",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocusRequester.requestFocus() }
                ),
                modifier = Modifier.focusRequester(phoneFocusRequester)

            )

            EditTextCustom(
                text = password,
                onValueChange = {password = it},
                textType = Constants.PASSWORD,
                elevationText = "Contraseña",
                hint = "",
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier.focusRequester(passwordFocusRequester)
            )

            Spacer(modifier =
                Modifier.weight(1f))

            OutlinedButtonExample (
                enabled = isButtonEnabled,
                onClick = { accountViewModel.register(accountData) }
            )

    }


}

@Composable
fun OutlinedButtonExample(enabled:Boolean ,onClick: () -> Unit) {
    OutlinedButton(
        onClick = {
            onClick()},
        enabled = enabled,
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth()
            .padding(horizontal = 55.dp),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = Color.White,
            disabledContentColor = MaterialTheme.colorScheme.surfaceContainerLow,
            disabledContainerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Text("Registrarse")
    }
}

/**
 * Proximos pasos aqui: Darle el correcto funcionamiento a los textos. Por ejemplo: Al password; Que contenga
 * mensajes de error por falta de mayusculas o numeros.
 * en el mail. para asegurarme de que sea un mail correcto.
 * Eventualmente agregar verificacion de numero de telefono y mail. para estar seguro de que cada texto es correcto.
 * **/

