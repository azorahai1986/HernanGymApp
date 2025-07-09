package com.hernan.gymapp.calendary.utils_calendary

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

object DateProvider {
    @RequiresApi(Build.VERSION_CODES.O)
    fun fechaActual() = LocalDate.now()
    @RequiresApi(Build.VERSION_CODES.O)
    fun anioActual() = fechaActual().year
    @RequiresApi(Build.VERSION_CODES.O)
    fun mesActual() = fechaActual().monthValue
    @RequiresApi(Build.VERSION_CODES.O)
    fun diaActual() = fechaActual().dayOfMonth
    fun daysOfWeek() = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

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
}