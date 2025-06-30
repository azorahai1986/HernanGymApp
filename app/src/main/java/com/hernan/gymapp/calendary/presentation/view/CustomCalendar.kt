package com.hernan.gymapp.calendary.presentation.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomCalendar(onDateSelected: (String) -> Unit) {
    val fechaActual = LocalDate.now()
    val mesActual = fechaActual.monthValue      // Por ejemplo: 6 (junio)
    val anioActual = fechaActual.year
    val daysofWeek = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
    val days = remember { calendarDayList(2025, 6) }
    //TODO pasar codigo a repo, usecase y viemodel para obtener los datos
    Text(text = mesActual.toString())
    Text(text = anioActual.toString())
    LazyVerticalGrid(
        columns = GridCells.Fixed(7), // 7 columnas
        modifier = Modifier
            .defaultMinSize()
            .padding(8.dp),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(daysofWeek.size) { index ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .width(12.dp)
                    .height(12.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(text = daysofWeek[index].toString())
            }
        }

        items(days.size) { index ->
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .width(12.dp)
                    .height(12.dp)
                    .background(Color.White)
                    .clickable(
                        onClick = {
                            Log.d("Dia Clickeado", days.toString())
                             onDateSelected("${days[index]} / $mesActual / $anioActual")
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = days[index].toString())
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun calendarDayList(year: Int, month: Int): List<String> {
    val primerDia = LocalDate.of(year, month, 1)
    val yearMonth = YearMonth.of(year, month)
    val lastDay = yearMonth.lengthOfMonth()

    val dayOfWeekNames = listOf("lun", "mar", "mié", "jue", "vie", "sáb", "dom")

    val nombrePrimerDia = primerDia.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("es", "ES")).lowercase()
    val primerIndice = dayOfWeekNames.indexOf(nombrePrimerDia)

    val diasTotales = 42 // 6 semanas x 7 días
    val calendario = mutableListOf<String>()

    for (i in 0 until diasTotales) {
        val dia = i - primerIndice + 1
        if (dia in 1..lastDay) {
            calendario.add(dia.toString())
        } else {
            calendario.add("")
        }
    }

    return calendario


}