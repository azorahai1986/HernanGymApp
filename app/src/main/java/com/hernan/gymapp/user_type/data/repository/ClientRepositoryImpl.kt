package com.hernan.gymapp.user_type.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.user_type.domain.model.Client
import com.hernan.gymapp.user_type.domain.repository.ClientRepository
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.user_type.data.model.ClientDto
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ClientRepository {
        override fun getClient(clientId: String): Flow<ResourceFlow<List<Client>>> = callbackFlow {
            trySend(ResourceFlow.Loading)

            val listenerRegistration = firestore.collection("Clients")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        trySend(ResourceFlow.Error(error.message ?: "Error desconocido"))
                        return@addSnapshotListener
                    }

                    if (snapshot != null) {
                        val clients = snapshot.documents.mapNotNull { it.toObject(ClientDto::class.java)?.toDomain() }

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