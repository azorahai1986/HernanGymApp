package com.hernan.gymapp.main_screen.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.main_screen.data.model.FirstStateDto
import com.hernan.gymapp.main_screen.domain.model.FirstState
import com.hernan.gymapp.main_screen.domain.repository.FirstStateRepository
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class FirstStateRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirstStateRepository {
    override fun getFirstState(): Flow<ResourceFlow<List<FirstState>>> = callbackFlow {
        trySend(ResourceFlow.Loading)

        val listenerRegistration = firestore.collection("MainScreenColection")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ResourceFlow.Error(error.message ?: "Error desconocido"))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val firstStateList = snapshot.documents.mapNotNull {
                        it.toObject(FirstStateDto::class.java)?.toDomain()
                    }

                    if (firstStateList.isNotEmpty()) {
                        trySend(ResourceFlow.Success(firstStateList))
                    } else {
                        trySend(ResourceFlow.Error("No hay estados disponibles"))
                    }
                }
            }
        awaitClose {
            listenerRegistration.remove()
        }
    }.distinctUntilChanged()
}