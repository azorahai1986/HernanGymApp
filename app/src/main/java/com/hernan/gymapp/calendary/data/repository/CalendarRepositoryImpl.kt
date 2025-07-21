package com.hernan.gymapp.calendary.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.calendary.domain.repository.CalendarRepository
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): CalendarRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun localDate(date: String, userId: String?): Flow<ResourceFlow<String>> = flow {
        emit(ResourceFlow.Loading)
        try {
            val shiftData = mapOf("date" to date)
            firestore.collection("ClassShifts")
                .document(userId.toString())
                .set(shiftData)
                .await()

            emit(ResourceFlow.Success("Se ha cargado el turno"))
        } catch (e: Exception) {
            emit(ResourceFlow.Error(e.message ?: "Error saving date"))
        }
    }

}