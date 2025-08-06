package com.hernan.gymapp.calendary.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.calendary.data.model.ShiftDto
import com.hernan.gymapp.calendary.domain.model.Shift
import com.hernan.gymapp.calendary.domain.repository.GetShiftRepository
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GetShiftRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): GetShiftRepository {
    override fun getAllShift(): Flow<ResourceFlow<List<Shift>>> = flow {
        emit(ResourceFlow.Loading)
        try {
            val querySnapshot = firestore
                .collectionGroup("Turnos")
                .get()
                .await()

            val shiftList = querySnapshot.documents.mapNotNull {
                it.toObject(ShiftDto::class.java)?.toShiftModel() }
           /** val shifts = querySnapshot.documents.mapNotNull { doc ->
                val date = doc.getString("date")
                val hour = doc.getString("hour")
                val id = doc.id

                if (date != null && hour != null) {
                    ShiftDto(id = id, dayOfMonth = date, hour = hour)
                } else {
                    null
                }
            }
            TODO este pedazo de codigo se puede reemplazar por lo implementado en la val "shiftList"
           **/
            emit(ResourceFlow.Success(shiftList))
        } catch (e: Exception) {
            emit(ResourceFlow.Error("Error obteniendo turnos: ${e.message}"))
        }
    }
}