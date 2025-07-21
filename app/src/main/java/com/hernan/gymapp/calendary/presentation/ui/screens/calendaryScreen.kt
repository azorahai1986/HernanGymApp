package com.hernan.gymapp.calendary.presentation.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hernan.gymapp.calendary.presentation.ui.view.CustomCalendar
import com.hernan.gymapp.calendary.presentation.viewmodel.ShiftsViewModel
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.common.ui_app.buttons.OutlinedRoundedButton
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendaryScreen() {
    DatePickerFieldToModal()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerFieldToModal(modifier: Modifier = Modifier) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    Log.e("SEND USER ID.", userId.toString())
    var showModal by remember { mutableStateOf(false) }
    val dialogState = rememberMaterialDialogState()
    var selectedDate by remember { mutableStateOf("") }
    val viewmodelShift: ShiftsViewModel = hiltViewModel()
    viewmodelShift.setShift(selectedDate, userId)
    val viewModelState = viewmodelShift.shift.collectAsState()
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp, horizontal = 55.dp)
        .imePadding()) {
        OutlinedTextField(
            value = selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            onValueChange = {},
            label = { Text("Selecciona una fecha") },
            placeholder = { Text("DD/MM/YYYY") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date",
                    modifier = Modifier.clickable {
                        showModal = true
                        dialogState.show()
                    }
                )
            },
            readOnly = true, // 👈 importante para evitar teclado
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    showModal = true
                    Log.d("SHOW MODEL", showModal.toString())
                }
        )
        Log.e("SELECT DATE VALUE", selectedDate.toString())

       // Boton para enviar fecha a firebase
        if (selectedDate.isNotEmpty()) {
            Spacer(modifier =
                Modifier.weight(1f))
            OutlinedRoundedButton(
                enabled = true,
                onClick = {
                    SendShift(
                        date = selectedDate,
                        viewModelState = viewModelState
                    )
                },
                text = "Confirmar"
            )
        }
    }

    if (showModal) {
        ShowDatePicker(
            dialogState = dialogState,
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowDatePicker(
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    dialogState: MaterialDialogState
) {
    Column {

        MaterialDialog(
            backgroundColor = MaterialTheme.colorScheme.background,
            dialogState = dialogState,
            properties = DialogProperties(
                dismissOnClickOutside = false
            ),
            buttons = {
                positiveButton("Aceptar")
            },

        ) {
            CustomCalendar(
                onDateSelected = { onDateSelected(it) }
            )
        }
    }
}


fun SendShift(date: String, viewModelState: State<ResourceFlow<String>>) {
    Log.e("SEND DATE D", viewModelState.value.toString())
    when(val state = viewModelState.value) {
        is ResourceFlow.Loading -> {}
        is ResourceFlow.Success -> {Log.e("SEND DATE", state.data)}
        is ResourceFlow.Error -> {}
        is ResourceFlow.Idle -> {}
    }
}