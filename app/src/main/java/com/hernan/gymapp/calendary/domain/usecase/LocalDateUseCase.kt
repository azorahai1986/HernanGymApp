package com.hernan.gymapp.calendary.domain.usecase

import com.hernan.gymapp.calendary.data.model.Shifts
import com.hernan.gymapp.calendary.domain.repository.CalendarRepository
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDateUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    operator fun invoke(shift: Shifts, userId: String?): Flow<ResourceFlow<String>> =
        calendarRepository.localDate(shift, userId)

}