package com.hernan.gymapp.calendary.data.model

import com.hernan.gymapp.calendary.domain.model.Shift

data class ShiftDto(
    val id: String = "",
    val date: String = "",
    val day: String = "",
    val month: String = "",
    val hour: String = "",
) {
    fun toShiftModel() : Shift =
        Shift(
            id = id,
            date = date,
            day = day,
            month = month,
            hour = hour
            )
}

