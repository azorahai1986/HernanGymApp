package com.hernan.gymapp.calendary.domain.usecase

import com.hernan.gymapp.calendary.domain.repository.CalendarRepository
import javax.inject.Inject

class LocalDateUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    //@RequiresApi(Build.VERSION_CODES.O)
    /*operator fun invoke(year: Int, month: Int): Flow<ResourceFlow<List<String>>>{

    }*/
}