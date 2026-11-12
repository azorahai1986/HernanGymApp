package com.hernan.gymapp.calendary.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.calendary.data.model.ShiftDto
import com.hernan.gymapp.calendary.domain.model.Shift
import com.hernan.gymapp.calendary.domain.repository.GetShiftRepository
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class GetShiftRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : GetShiftRepository {
    override fun getAllShift(): Flow<ResourceFlow<List<Shift>>> = callbackFlow {
        trySend(ResourceFlow.Loading)
        val listener = firestore.collectionGroup("Turnos")
            .addSnapshotListener { snapshot, error ->
                when {
                    error != null -> trySend(ResourceFlow.Error("Error obteniendo turnos: ${error.message}"))
                    snapshot != null -> {
                        val shifts = snapshot.documents.mapNotNull { document ->
                            document.toObject(ShiftDto::class.java)?.toShiftModel()
                        }
                        trySend(ResourceFlow.Success(shifts))
                    }
                }
            }
        awaitClose { listener.remove() }
    }
}
