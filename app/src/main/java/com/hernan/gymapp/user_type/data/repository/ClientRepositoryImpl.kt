package com.hernan.gymapp.user_type.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.user_type.domain.model.Client
import com.hernan.gymapp.user_type.domain.repository.ClientRepository
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.main_screen.data.model.FirstStateDto
import com.hernan.gymapp.user_type.data.model.ClientDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClientRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ClientRepository {

    private val _clientsState = MutableStateFlow<ResourceFlow<List<Client>>>(ResourceFlow.Loading)
    val clientsState: StateFlow<ResourceFlow<List<Client>>> = _clientsState.asStateFlow()

    override fun getClient(clientId: String): Flow<ResourceFlow<List<Client>>> = flow {
        if (_clientsState.value is ResourceFlow.Success) {
            emit(_clientsState.value) // Si ya hay datos, los devolvemos
            return@flow
        }

        emit(ResourceFlow.Loading)

        try {
            val snapshot = firestore.collection("Clients").get().await()
            val clients = snapshot.documents.mapNotNull { it.toObject(ClientDto::class.java)?.toDomain() }

            if (clients.isNotEmpty()) {
                _clientsState.value = ResourceFlow.Success(clients)
                emit(ResourceFlow.Success(clients))
            } else {
                _clientsState.value = ResourceFlow.Error("No hay clientes disponibles")
                emit(_clientsState.value)
            }
        } catch (e: Exception) {
            _clientsState.value = ResourceFlow.Error(e.localizedMessage ?: "Error desconocido")
            emit(_clientsState.value)
        }
    }.distinctUntilChanged() // ⚠️ Evita que se emitan los mismos datos repetidamente

}