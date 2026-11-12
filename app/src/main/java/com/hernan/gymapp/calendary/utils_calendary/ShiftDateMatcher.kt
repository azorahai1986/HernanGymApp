package com.hernan.gymapp.calendary.utils_calendary

import android.os.Build
import androidx.annotation.RequiresApi
import com.hernan.gymapp.calendary.domain.model.Shift
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun Shift.matchesDate(dateToMatch: LocalDate): Boolean {
    return matchesCalendarDate(dateToMatch.year, dateToMatch.monthValue, dateToMatch.dayOfMonth)
}

fun Shift.matchesCalendarDate(year: Int, month: Int, dayOfMonth: Int): Boolean {
    val parsed = listOf("yyyy-MM-dd", "d/M/yyyy", "dd/MM/yyyy", "d-MM-yyyy").firstNotNullOfOrNull { pattern ->
        val parser = SimpleDateFormat(pattern, Locale.ROOT).apply { isLenient = false }
        val position = ParsePosition(0)
        parser.parse(date.trim(), position)?.takeIf { position.index == date.trim().length }
    }
    if (parsed != null) {
        val calendar = Calendar.getInstance().apply { time = parsed }
        return calendar.get(Calendar.YEAR) == year &&
            calendar.get(Calendar.MONTH) + 1 == month &&
            calendar.get(Calendar.DAY_OF_MONTH) == dayOfMonth
    }
    return day.toIntOrNull() == dayOfMonth
}
