package com.hernan.gymapp.calendary.presentation.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hernan.gymapp.calendary.presentation.view.CustomCalendar
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.toString

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendaryScreen() {
    DatePickerFieldToModal()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerFieldToModal(modifier: Modifier = Modifier) {
    var showModal by remember { mutableStateOf(false) }
    val dialogState = rememberMaterialDialogState()
    var selectedDate by remember { mutableStateOf("") }

    Log.d("Select DATE", selectedDate.toString())
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp, horizontal = 55.dp)) {
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
                        Log.d("SHOW MODEL", "Icon clicked")
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
    Log.d("Select DATE", selectedDate.toString())

    Column {

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                positiveButton("Aceptar")
                negativeButton("Cancelar")
            },

        ) {
            CustomCalendar(
                onDateSelected = { onDateSelected(it) }
            )
        }
    }
}

