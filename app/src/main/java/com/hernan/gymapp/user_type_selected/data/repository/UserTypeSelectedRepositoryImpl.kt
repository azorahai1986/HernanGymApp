package com.hernan.gymapp.user_type_selected.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.user_type_selected.data.model.UserTypeSelectedDto
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import com.hernan.gymapp.user_type_selected.domain.repository.UserTypeSelectedRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserTypeSelectedRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): UserTypeSelectedRepository {
    override suspend fun getUserById(documentId: String): ResourceFlow<UserTypeSelected> {
        if (documentId.isBlank()) return ResourceFlow.Error("No hay una cuenta de socio activa")
        return try {
            val document = firestore.collection("Members").document(documentId).get().await()
            val user = document.toObject(UserTypeSelectedDto::class.java)
                ?: return ResourceFlow.Error("No encontramos el perfil del socio")
            ResourceFlow.Success(user.toDomain(document.id))
        } catch (exception: Exception) {
            ResourceFlow.Error(exception.message ?: "No se pudo cargar el perfil del socio")
        }
    }

    override fun getUserByType(userTypeId: String): Flow<ResourceFlow<List<UserTypeSelected>>> {
        if (userTypeId.isBlank()) return flowOf(ResourceFlow.Error("ID de cliente inválido"))
        return observeMembers(
            firestore.collection("Members").whereEqualTo("typeId", userTypeId)
        )
    }

    override fun getAllUsers(): Flow<ResourceFlow<List<UserTypeSelected>>> =
        observeMembers(firestore.collection("Members"))

    private fun observeMembers(query: Query): Flow<ResourceFlow<List<UserTypeSelected>>> = callbackFlow {
        trySend(ResourceFlow.Loading)
        val listenerRegistration = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(ResourceFlow.Error(error.message ?: "Error desconocido"))
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val members = snapshot.documents.mapNotNull { document ->
                    document.toObject(UserTypeSelectedDto::class.java)?.toDomain(document.id)
                }
                if (members.isNotEmpty()) trySend(ResourceFlow.Success(members))
                else trySend(ResourceFlow.Error("No hay miembros disponibles"))
            }
        }
        awaitClose { listenerRegistration.remove() }
    }.distinctUntilChanged()

    override suspend fun updateSchedule(
        documentId: String,
        preferredDays: List<Int>,
        preferredHours: Map<String, String>,
        weeklyClassLimit: Int,
        scheduleMode: String
    ): ResourceFlow<Unit> {
        if (documentId.isBlank()) return ResourceFlow.Error("No se encontró el identificador del socio")
        return try {
            firestore.collection("Members").document(documentId).update(
                mapOf(
                    "preferredDays" to preferredDays,
                    "preferredHours" to preferredHours,
                    "weeklyClassLimit" to weeklyClassLimit,
                    "scheduleMode" to scheduleMode
                )
            ).await()
            ResourceFlow.Success(Unit)
        } catch (exception: Exception) {
            ResourceFlow.Error(exception.message ?: "No se pudieron guardar los días preferidos")
        }
    }
}
