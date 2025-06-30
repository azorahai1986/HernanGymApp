package com.hernan.gymapp.calendary.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.hernan.gymapp.calendary.domain.repository.CalendarRepository
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

class LocalDateUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    //@RequiresApi(Build.VERSION_CODES.O)
    /*operator fun invoke(year: Int, month: Int): Flow<ResourceFlow<List<String>>>{

    }*/
}