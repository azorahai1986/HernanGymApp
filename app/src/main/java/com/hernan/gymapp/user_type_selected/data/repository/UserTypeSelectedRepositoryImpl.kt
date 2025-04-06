package com.hernan.gymapp.user_type_selected.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.user_type_selected.data.model.UserTypeSelectedDto
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import com.hernan.gymapp.user_type_selected.domain.repository.UserTypeSelectedRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class UserTypeSelectedRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): UserTypeSelectedRepository {
    override fun getUserByType(userTypeId: String): Flow<ResourceFlow<List<UserTypeSelected>>> = callbackFlow {
        if (userTypeId.isBlank()) {
            trySend(ResourceFlow.Error("ID de cliente inválido"))
            close()
            return@callbackFlow
        }

        trySend(ResourceFlow.Loading)

        val listenerRegistration = firestore.collection("Members")
            .whereEqualTo("typeId", userTypeId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(ResourceFlow.Error(error.message ?: "Error desconocido"))
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val members = snapshot.documents.mapNotNull { it.toObject(UserTypeSelectedDto::class.java)?.toDomain() }

                    if (members.isNotEmpty()) {
                        trySend(ResourceFlow.Success(members))
                    } else {
                        trySend(ResourceFlow.Error("No hay miembros disponibles"))
                    }
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }.distinctUntilChanged()
}