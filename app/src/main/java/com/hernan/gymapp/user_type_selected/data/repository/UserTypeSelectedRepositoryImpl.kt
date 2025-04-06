package com.hernan.gymapp.user_type_selected.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.user_type_selected.data.model.UserTypeSelectedDto
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import com.hernan.gymapp.user_type_selected.domain.repository.UserTypeSelectedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserTypeSelectedRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): UserTypeSelectedRepository {
    override fun getUserByType(userTypeId: String): Flow<ResourceFlow<List<UserTypeSelected>>> = flow {

        if (userTypeId.isBlank()) {
            emit(ResourceFlow.Error("ID de cliente inválido"))
            return@flow
        }

        emit(ResourceFlow.Loading)

        try {
            val snapshot = firestore.collection("Members")
                .whereEqualTo("typeId", userTypeId) // 🔥 Filtrar por typeId
                .get()
                .await()

            val members = snapshot.documents.mapNotNull { it.toObject(UserTypeSelectedDto::class.java)?.toDomain() }

            if (members.isNotEmpty()) {
                emit(ResourceFlow.Success(members))
            } else {
                emit(ResourceFlow.Error("No hay miembros disponibles"))
            }
        } catch (e: Exception) {
            emit(ResourceFlow.Error(e.localizedMessage ?: "Error desconocido"))
        }
    }.distinctUntilChanged()
}


