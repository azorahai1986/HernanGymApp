package com.hernan.gymapp.calendary.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.calendary.data.model.Shifts
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
    override fun localDate(shift: Shifts, userId: String?): Flow<ResourceFlow<String>> = flow {
        emit(ResourceFlow.Loading)
        try {
            firestore.collection("ClassShifts")
                .document("04C5luhXJIh9GK2oeuLrfRaIKHF3")
                .collection("Turnos")
                .add(shift)
                .await()

            emit(ResourceFlow.Success("Se ha cargado el turno"))
        } catch (e: Exception) {
            emit(ResourceFlow.Error(e.message ?: "Error saving date"))
        }
    }

}