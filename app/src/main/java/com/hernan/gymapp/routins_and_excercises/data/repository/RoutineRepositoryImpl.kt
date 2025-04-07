package com.hernan.gymapp.routins_and_excercises.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.routins_and_excercises.data.model.RoutinServiceDto
import com.hernan.gymapp.routins_and_excercises.domain.model.RoutinService
import com.hernan.gymapp.routins_and_excercises.domain.repository.RoutineRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class RoutineRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): RoutineRepository {
    override fun getRoutineVideos(): Flow<ResourceFlow<List<RoutinService>>> = callbackFlow {
        trySend(ResourceFlow.Loading)

        val listenerRegistration = firestore.collection("Routines")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ResourceFlow.Error(error.message ?: "Error desconocido"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val clients = snapshot.documents.mapNotNull { it.toObject(RoutinServiceDto::class.java)?.toDomain() }

                    if (clients.isNotEmpty()) {
                        trySend(ResourceFlow.Success(clients))
                    } else {
                        trySend(ResourceFlow.Error("No hay clientes disponibles"))
                    }
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }.distinctUntilChanged()
}