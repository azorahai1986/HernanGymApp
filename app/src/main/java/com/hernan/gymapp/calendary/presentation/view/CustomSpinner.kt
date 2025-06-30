package com.hernan.gymapp.calendary.presentation.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hernan.gymapp.R
import com.hernan.gymapp.common.ui_app.text_views.CustomWhiteText
import java.time.LocalDate
import java.time.Month
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomSpinner(
    monthSelected: (Int) -> Unit
){
    val fechaActual = LocalDate.now()
    val mesActual = fechaActual.monthValue.absoluteValue
    val mesesEnEspañol = Month.entries.map { month ->
        month.getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale("es"))
    }
    val meses = mesesEnEspañol.map { it.replaceFirstChar { c -> c.uppercase() } }

    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }

    val itemPosition = remember {
        mutableIntStateOf(mesActual -1)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    isDropDownExpanded.value = true
                }
            ) {
                CustomWhiteText(
                    text = meses[itemPosition.intValue]
                )
                Spacer(modifier = Modifier.padding(horizontal = 18.dp))
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                    contentDescription = "DropDown Icon"
                )
            }
            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                meses.forEachIndexed { index, mes ->
                    DropdownMenuItem(text = {
                        Text(text = mes)
                    },
                        onClick = {
                            isDropDownExpanded.value = false
                            itemPosition.value = index
                            monthSelected(index + 1)
                        })
                }
            }
        }

    }
}