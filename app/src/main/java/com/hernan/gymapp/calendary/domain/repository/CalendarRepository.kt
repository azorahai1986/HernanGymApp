package com.hernan.gymapp.calendary.domain.repository

import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {
    fun localDate(date: String, userId: String?): Flow<ResourceFlow<String>>
}