package com.hernan.gymapp.calendary.presentation.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hernan.gymapp.calendary.utils_calendary.DateProvider.anioActual
import com.hernan.gymapp.calendary.utils_calendary.DateProvider.calendarDayList
import com.hernan.gymapp.calendary.utils_calendary.DateProvider.daysOfWeek
import com.hernan.gymapp.calendary.utils_calendary.DateProvider.diaActual
import com.hernan.gymapp.calendary.utils_calendary.DateProvider.mesActual
import com.hernan.gymapp.common.ui_app.text_views.CustomWhiteText

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomCalendar(onDateSelected: (String) -> Unit) {
    var month by remember { mutableIntStateOf(mesActual()) }
    var toDay by remember { mutableIntStateOf(diaActual()) }
    val days by remember { // Con derivedStateOf Puedo cambiar el calendario segun el mes en tiempo real..
        derivedStateOf { calendarDayList(anioActual(), month) }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.padding(top = 24.dp))
        CustomWhiteText(
            text = anioActual().toString(),
            background = MaterialTheme.colorScheme.background
        )

        CustomSpinner(
            monthSelected = { month = it }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(7), // 7 columnas
            modifier = Modifier
                .defaultMinSize()
                .padding(8.dp)
                .background(color = MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(daysOfWeek().size) { index ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .width(12.dp)
                        .height(12.dp)
                        .background(color = MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    CustomWhiteText(
                        text = daysOfWeek()[index],
                        background = MaterialTheme.colorScheme.background
                    )
                }
            }

            items(days.size) { index ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .width(12.dp)
                        .height(12.dp)
                        .background(color = MaterialTheme.colorScheme.background)
                        .clickable(
                            onClick = {
                                Log.d("Dia Clickeado", days[index].toString())
                                onDateSelected("${days[index]}/$month/${anioActual()}")
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    CustomWhiteText(
                        text = days[index],
                        background =
                            if (days[index] == diaActual().toString())
                            MaterialTheme.colorScheme.onTertiary
                            else MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }
}