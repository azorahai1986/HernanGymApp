package com.hernan.gymapp.calendary.domain.repository

import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CalendarRepository {
    fun localDate(): Flow<ResourceFlow<LocalDate>>
}