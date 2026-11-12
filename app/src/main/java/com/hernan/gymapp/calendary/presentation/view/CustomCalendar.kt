package com.hernan.gymapp.calendary.presentation.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hernan.gymapp.calendary.domain.model.Shift
import com.hernan.gymapp.calendary.utils_calendary.matchesDate
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomCalendar(
    selectedDate: LocalDate,
    shifts: List<Shift>,
    onDateSelected: (LocalDate) -> Unit
) {
    val month = remember(selectedDate) { YearMonth.from(selectedDate) }
    val days by remember(month) { derivedStateOf { monthGrid(month) } }
    val monthLabel = month.month.getDisplayName(java.time.format.TextStyle.FULL, Locale("es", "AR"))
        .replaceFirstChar { it.titlecase(Locale("es", "AR")) }

    Column(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.large).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                "${monthLabel} ${month.year}",
                style = MaterialTheme.typography.titleMedium
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                MonthButton("‹", "Mes anterior") { onDateSelected(month.minusMonths(1).atDay(1)) }
                MonthButton("›", "Mes siguiente") { onDateSelected(month.plusMonths(1).atDay(1)) }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("L", "M", "M", "J", "V", "S", "D").forEach { day ->
                Text(
                    day,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth().height(252.dp),
            contentPadding = PaddingValues(vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(days.size, key = { index -> days[index]?.toString() ?: "blank-$index" }) { index ->
                val date = days[index]
                if (date == null) {
                    Box(Modifier.height(38.dp))
                } else {
                    val shiftsCount = shifts.count { it.matchesDate(date) }
                    val isSelected = date == selectedDate
                    Box(
                        modifier = Modifier.height(38.dp).clip(CircleShape)
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
                            .clickable { onDateSelected(date) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            date.dayOfMonth.toString(),
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodySmall
                        )
                        if (shiftsCount > 0 && !isSelected) {
                            Box(
                                Modifier.align(Alignment.BottomCenter).padding(bottom = 3.dp)
                                    .clip(CircleShape).background(MaterialTheme.colorScheme.tertiary)
                                    .padding(2.dp)
                            )
                        }
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(Modifier.clip(CircleShape).background(MaterialTheme.colorScheme.tertiary).padding(3.dp))
                Text("Con turnos", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(Modifier.clip(CircleShape).background(MaterialTheme.colorScheme.primary).padding(3.dp))
                Text("Día seleccionado", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun MonthButton(label: String, description: String, onClick: () -> Unit) {
    androidx.compose.material3.TextButton(onClick = onClick, modifier = Modifier.semantics { contentDescription = description }) {
        Text(label, fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun monthGrid(month: YearMonth): List<LocalDate?> {
    val offset = month.atDay(1).dayOfWeek.value - 1
    return List(42) { index ->
        val day = index - offset + 1
        if (day in 1..month.lengthOfMonth()) month.atDay(day) else null
    }
}
