package com.hernan.gymapp.common.ui_app.text_views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.hernan.gymapp.common.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTextCustom(text:String,
                   onValueChange: (String) -> Unit,
                   textType: String,
                   elevationText: String,
                   hint: String,
                   keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
                   keyboardActions: KeyboardActions = KeyboardActions.Default,
                   modifier: Modifier = Modifier,
                   validatorHasErrors: Boolean
) {

    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { onValueChange(it) },
        label = { Text(elevationText) },
        placeholder = { Text(hint) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 55.dp)
            .background(color = MaterialTheme.colorScheme.background),
        enabled = true,
        readOnly = false,
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        leadingIcon = {
            when(textType) { // Seleccionar un icono para el lado izquierdo de la vista
                Constants.SIMPLE_TEXT -> Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "")
                Constants.EMAIL -> Icon(imageVector = Icons.Default.Email, contentDescription = "")
                Constants.PHONE_NUMBER -> Icon(imageVector = Icons.Default.PhoneAndroid, contentDescription = "")
                Constants.PASSWORD -> Icon(imageVector = Icons.Default.Password, contentDescription = "")

            }
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            when(textType) {
                Constants.SIMPLE_TEXT ->{isPasswordVisible = true}
                Constants.PHONE_NUMBER ->{isPasswordVisible = true}
                Constants.EMAIL -> {isPasswordVisible = true}
                Constants.PASSWORD -> {
                    val icon = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                }

            }

        },
        isError = false,
        supportingText = {
            if (validatorHasErrors) {
                when(textType) {
                    Constants.PHONE_NUMBER ->  errorText = "Número demasiado largo..."
                    Constants.EMAIL ->  errorText = "Formato incorrecto de email"
                    Constants.PASSWORD ->  errorText = "Formato de contraseña incorrecto"
                }
                Text(text = errorText,
                    color = MaterialTheme.colorScheme.error)
            }
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1,
        minLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(18.dp)

    )

}
