package com.hernan.gymapp.calendary.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.hernan.gymapp.calendary.domain.repository.CalendarRepository
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

class CalendarRepositoryImpl @RequiresApi(Build.VERSION_CODES.O)
@Inject constructor(
    private val localDate: LocalDate
): CalendarRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun localDate(): Flow<ResourceFlow<LocalDate>> = callbackFlow {
        trySend(ResourceFlow.Loading)
        trySend(ResourceFlow.Success(localDate))
    }

}