package com.hernan.gymapp.calendary.presentation.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hernan.gymapp.calendary.data.model.Shifts
import com.hernan.gymapp.calendary.presentation.ui.view.CustomCalendar
import com.hernan.gymapp.calendary.presentation.viewmodel.ShiftsViewModel
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.common.ui.theme.availableGrey
import com.hernan.gymapp.common.ui.theme.orange_primary
import com.hernan.gymapp.common.ui_app.buttons.OutlinedRoundedButton
import com.hernan.gymapp.common.ui_app.text_views.CentredTextField
import com.hernan.gymapp.common.ui_app.text_views.CustomWhiteText
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
    var selectedHour by remember { mutableStateOf("") }
    val shift = Shifts(selectedHour, selectedDate)
    val viewmodelShift: ShiftsViewModel = hiltViewModel()
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
            readOnly = true, // Importante para evitar teclado
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    showModal = true
                    Log.d("SHOW MODEL", showModal.toString())
                }
        )
        Log.e("SELECT Hour VALUE", selectedHour.toString())

        if (selectedDate.isNotEmpty()) ShowHoursList(hourSelected = {selectedHour = it})

       // Boton para enviar fecha a firebase
        if (selectedHour.isNotEmpty()) {
            Spacer(modifier =
                Modifier.weight(1f))
            OutlinedRoundedButton(
                enabled = true,
                onClick = {
                    viewmodelShift.setShift(shift, userId)
                    SendShift(
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

fun SendShift(viewModelState: State<ResourceFlow<String>>) {
    Log.e("SEND DATE D", viewModelState.value.toString())
    when(val state = viewModelState.value) {
        is ResourceFlow.Loading -> {}
        is ResourceFlow.Success -> {Log.e("SEND DATE", state.data)}
        is ResourceFlow.Error -> {}
        is ResourceFlow.Idle -> {}
    }
}

@Composable
fun ShowHoursList(hourSelected:(String) -> Unit) {
    val hours = listOf("9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30")

    LazyColumn(
        modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        itemsIndexed(hours) { index, hour ->
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(availableGrey)
                .clickable {
                    Log.e("BOX CLICKEADO", hour)
                    hourSelected(hour)
                }) {
                CentredTextField(
                    text = "$hour hs",
                    size = 16.sp
                )
            }
        }
    }

}