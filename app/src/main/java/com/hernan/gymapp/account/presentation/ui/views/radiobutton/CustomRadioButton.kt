package com.hernan.gymapp.account.presentation.ui.views.radiobutton

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun CustomRadioButton(
    modifier: Modifier = Modifier,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    radioOptions: List<String>,
) {
    //(selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    // Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    var selectedOption by remember { mutableStateOf(radioOptions[0]) }
    Column {
        Text("Seleccioná una opción:", style = MaterialTheme.typography.titleMedium)
        radioOptions.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
                    .padding(horizontal = 55.dp)
                    .clickable { selectedOption = option } // hace clic en toda la fila
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { selectedOption = option }
                )
                Text(
                    text = option,
                    color = Color.White)
            }
        }
    }
}