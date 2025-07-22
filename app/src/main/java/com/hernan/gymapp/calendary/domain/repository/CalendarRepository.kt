package com.hernan.gymapp.calendary.domain.repository

import com.hernan.gymapp.calendary.data.model.Shifts
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {
    fun localDate(shift: Shifts, userId: String?): Flow<ResourceFlow<String>>
}