package com.hernan.gymapp.user_type.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.user_type.domain.model.Client
import com.hernan.gymapp.user_type.domain.repository.ClientRepository
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.user_type.data.model.ClientDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ClientRepository {
        override fun getClient(clientId: String): Flow<ResourceFlow<List<Client>>> = flow {
            emit(ResourceFlow.Loading)
            try {
                val snapshot = firestore.collection("Clients").get().await()
                val clients = snapshot.documents.mapNotNull { it.toObject(ClientDto::class.java)?.toDomain() }

                if (clients.isNotEmpty()) {
                    emit(ResourceFlow.Success(clients))
                } else {
                    emit(ResourceFlow.Error("No hay clientes disponibles"))
                }
            } catch (e: Exception) {
                emit(ResourceFlow.Error(e.localizedMessage ?: "Error desconocido"))
        }
    }.distinctUntilChanged()

}