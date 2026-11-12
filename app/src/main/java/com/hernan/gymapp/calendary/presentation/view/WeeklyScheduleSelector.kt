package com.hernan.gymapp.calendary.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hernan.gymapp.calendary.domain.model.Shift
import com.hernan.gymapp.calendary.utils_calendary.matchesCalendarDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

const val SCHEDULE_FIXED = "FIXED"
const val SCHEDULE_FLEXIBLE = "FLEXIBLE"
private const val MAX_PEOPLE_PER_SLOT = 5

@Composable
fun WeeklyScheduleSelector(
    selectedDays: Set<Int>,
    onSelectedDaysChange: (Set<Int>) -> Unit,
    selectedHours: Map<String, String>,
    onSelectedHoursChange: (Map<String, String>) -> Unit,
    weeklyClassLimit: Int,
    onWeeklyClassLimitChange: (Int) -> Unit,
    scheduleMode: String,
    onScheduleModeChange: (String) -> Unit,
    shifts: List<Shift>,
    modifier: Modifier = Modifier
) {
    var weekStartMillis by rememberSaveable { mutableStateOf(currentWeekStartMillis()) }
    val weekStart = remember(weekStartMillis) {
        Calendar.getInstance().apply { timeInMillis = weekStartMillis }
    }
    val weekDates = remember(weekStartMillis) {
        (0..6).map { offset ->
            (weekStart.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, offset) }
        }
    }
    val dateLabel = SimpleDateFormat("d MMM", Locale("es", "AR"))
    val hours = remember(shifts) { shifts.map { it.hour.trim() }.filter(String::isNotBlank).distinct().sorted() }

    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "${dateLabel.format(weekStart.time)} – ${dateLabel.format(weekDates.last().time)}",
                style = MaterialTheme.typography.titleSmall
            )
            Row {
                TextButton(onClick = {
                    weekStartMillis = (weekStart.clone() as Calendar)
                        .apply { add(Calendar.DAY_OF_MONTH, -7) }.timeInMillis
                }) { Text("‹") }
                TextButton(onClick = {
                    weekStartMillis = (weekStart.clone() as Calendar)
                        .apply { add(Calendar.DAY_OF_MONTH, 7) }.timeInMillis
                }) { Text("›") }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            weekDates.forEach { date ->
                val weekday = date.get(Calendar.DAY_OF_WEEK).toMondayFirstDay()
                val selected = weekday in selectedDays
                val dayLabel = SimpleDateFormat("EEE", Locale("es", "AR")).format(date.time)
                    .replaceFirstChar { it.titlecase(Locale("es", "AR")) }
                val currentDateShifts = shifts.count {
                    it.matchesCalendarDate(
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH) + 1,
                        date.get(Calendar.DAY_OF_MONTH)
                    )
                }
                Column(
                    modifier = Modifier.weight(1f)
                        .background(
                            color = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .toggleable(
                            value = selected,
                            enabled = selected || selectedDays.size < weeklyClassLimit,
                            role = Role.Checkbox,
                            onValueChange = { checked ->
                                onSelectedDaysChange(
                                    if (checked) selectedDays + weekday else selectedDays - weekday
                                )
                                if (!checked) {
                                    onSelectedHoursChange(selectedHours - weekday.toString())
                                }
                            }
                        )
                        .padding(vertical = 9.dp, horizontal = 2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(dayLabel, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
                    Text(
                        date.get(Calendar.DAY_OF_MONTH).toString(),
                        style = MaterialTheme.typography.titleSmall,
                        color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        when {
                            selectedHours.containsKey(weekday.toString()) -> "Elegido"
                            selected -> "Elegí hora"
                            currentDateShifts > 0 -> "$currentDateShifts ocup."
                            else -> "Disponible"
                        },
                        fontSize = 9.sp,
                        textAlign = TextAlign.Center,
                        color = if (selected) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Text("Frecuencia semanal", style = MaterialTheme.typography.titleSmall)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf(1, 2, 3, 7).forEach { limit ->
                FilterChip(
                    selected = weeklyClassLimit == limit,
                    onClick = { onWeeklyClassLimitChange(limit) },
                    label = {
                        Text(
                            when (limit) {
                                1 -> "1 día"
                                7 -> "Todos"
                                else -> "$limit días"
                            },
                            fontSize = 12.sp
                        )
                    }
                )
            }
        }

        if (selectedDays.isEmpty()) {
            Text(
                "Elegí $weeklyClassLimit ${if (weeklyClassLimit == 1) "día" else "días"} para entrenar.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            selectedDays.sorted().forEach { weekday ->
                val date = weekDates.first { it.get(Calendar.DAY_OF_WEEK).toMondayFirstDay() == weekday }
                val dayShifts = shifts.filter {
                    it.matchesCalendarDate(
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH) + 1,
                        date.get(Calendar.DAY_OF_MONTH)
                    )
                }
                val label = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es", "AR"))
                    .format(date.time).replaceFirstChar { it.titlecase(Locale("es", "AR")) }
                Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text("$label · horario", style = MaterialTheme.typography.titleSmall)
                    if (hours.isEmpty()) {
                        Text(
                            "No hay horarios registrados para mostrar.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        hours.forEach { hour ->
                            val occupancy = dayShifts.count { it.hour.trim() == hour }
                            val full = occupancy >= MAX_PEOPLE_PER_SLOT
                            val selected = selectedHours[weekday.toString()] == hour
                            FilterChip(
                                selected = selected,
                                enabled = !full || selected,
                                onClick = {
                                    onSelectedHoursChange(
                                        if (selected) selectedHours - weekday.toString()
                                        else selectedHours + (weekday.toString() to hour)
                                    )
                                },
                                label = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(hour, style = MaterialTheme.typography.bodyMedium)
                                        Text(
                                            if (full) "Completo · $occupancy/$MAX_PEOPLE_PER_SLOT"
                                            else "$occupancy/$MAX_PEOPLE_PER_SLOT lugares",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (full) MaterialTheme.colorScheme.error
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = "${selectedDays.size} de $weeklyClassLimit días · ${selectedHours.size} con horario asignado",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = scheduleMode == SCHEDULE_FIXED,
                onClick = { onScheduleModeChange(SCHEDULE_FIXED) },
                label = { Text("Días fijos") }
            )
            FilterChip(
                selected = scheduleMode == SCHEDULE_FLEXIBLE,
                onClick = { onScheduleModeChange(SCHEDULE_FLEXIBLE) },
                label = { Text("Flexible") }
            )
        }
        Text(
            if (scheduleMode == SCHEDULE_FIXED) "Se repiten estos días y horarios todas las semanas."
            else "Son preferencias; podés cambiarlas según la disponibilidad.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun Int.toMondayFirstDay(): Int = if (this == Calendar.SUNDAY) 7 else this - 1

private fun currentWeekStartMillis(): Long {
    val calendar = Calendar.getInstance().apply {
        firstDayOfWeek = Calendar.MONDAY
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
        calendar.add(Calendar.DAY_OF_MONTH, -1)
    }
    return calendar.timeInMillis
}
