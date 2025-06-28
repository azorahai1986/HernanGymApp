package com.hernan.gymapp.calendary.presentation.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendaryScreen() {
    DatePickerFieldToModal()
    convertMillisToDate()
    SimpleGrid()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePickerFieldToModal(modifier: Modifier = Modifier) {
    //var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }
    val dialogState = rememberMaterialDialogState()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    Log.d("Select DATE", selectedDate.toString())
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp, horizontal = 55.dp)) {
        OutlinedTextField(
            value = selectedDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "",
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

    /*if (showModal) {
        DatePickerModal(
            onDateSelected = { selectedDate = it },
            onDismiss = { showModal = false }
        )
    }*/

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
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    dialogState: MaterialDialogState
) {

    Column {

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                positiveButton("Aceptar")
                negativeButton("Cancelar")
            }
        ) {
            datepicker(
                title = "Elegí una fecha",
                allowedDateValidator = {
                    it.dayOfWeek.value != 7 // deshabilitar domingos
                },
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = Color(0xFF2196F3),
                    dateActiveBackgroundColor = Color(0xFF4CAF50),
                    dateInactiveBackgroundColor = Color(0xFFE0E0E0)
                )
            ) {
                onDateSelected(it)
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertMillisToDate() {

    val calendar = Calendar.getInstance()
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    Log.d("Calendar Week Year", calendar.weekYear.toString()) //año actual
    Log.d("Calendar Time", calendar.time.toString()) // fecha de hoy con dia
    Log.d("Calendar Time", dayOfWeek.toString())  // posición del dia de hoy
    Log.d("Calendar DATE", calendar.get(Calendar.DATE).toString()) // dia de hoy en numero
    val yearMonth = YearMonth.of(2025, 7)
    val daysList = (1..yearMonth.lengthOfMonth()).toList()
    Log.d("Days in month", daysList.toString())


}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleGrid() {
    val items = (1..30).toList() // Ejemplo de items

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // 3 columnas
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items.size) { index ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Item ${items[index]}")
            }
        }
    }
}