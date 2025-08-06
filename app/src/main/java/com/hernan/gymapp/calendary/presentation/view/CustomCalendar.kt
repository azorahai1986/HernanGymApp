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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hernan.gymapp.calendary.domain.model.Shift
import com.hernan.gymapp.calendary.presentation.viewmodel.ShiftViewModel
import com.hernan.gymapp.calendary.utils_calendary.DateProvider.anioActual
import com.hernan.gymapp.calendary.utils_calendary.DateProvider.calendarDayList
import com.hernan.gymapp.calendary.utils_calendary.DateProvider.daysOfWeek
import com.hernan.gymapp.calendary.utils_calendary.DateProvider.diaActual
import com.hernan.gymapp.calendary.utils_calendary.DateProvider.mesActual
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.common.ui.theme.gray_unavailable
import com.hernan.gymapp.common.ui.theme.green_available
import com.hernan.gymapp.common.ui_app.text_views.CustomWhiteText

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomCalendar(onDateSelected: (String) -> Unit) {

    val viewmodelShift: ShiftViewModel = hiltViewModel()
    val getAllShift = viewmodelShift.shift.collectAsState()
    viewmodelShift.getAllShifts()
    var month by remember { mutableIntStateOf(mesActual()) }
    var toDay by remember { mutableIntStateOf(diaActual()) }
    var unavailableDate = remember { mutableStateListOf<Shift>() }
    val days by remember { // Con derivedStateOf Puedo cambiar el calendario segun el mes en tiempo real..
        derivedStateOf { calendarDayList(anioActual(), month) }
    }

    when(val state = getAllShift.value) {
        is ResourceFlow.Idle -> {}
        is ResourceFlow.Loading -> Log.d("STATE", "CARGANDO")
        is ResourceFlow.Success -> {
            unavailableDate.addAll(state.data)
            Log.d("STATE", "SUCCES ${state.data}")
        }
        is ResourceFlow.Error -> Log.d("STATE", "ESTADO ERROR ${state.message}")
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

                    Box(modifier = Modifier.clip(CircleShape)) {
                        CustomWhiteText(
                            text = days[index],
                            background = getDateBackground(unavailableDate, days[index], month)

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getDateBackground(unavailableDate: SnapshotStateList<Shift>, days: String, month: Int): Color {
    if (unavailableDate.isNotEmpty() && days.isNotEmpty()) {
        Log.d("MONTH ACTUAL", month.toString())
        unavailableDate.forEach { date ->
            if (days == date.day && month == date.month.toInt()) {
                return gray_unavailable
            }
        }
        return green_available
    }
    return MaterialTheme.colorScheme.background
}