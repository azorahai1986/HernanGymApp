package com.hernan.gymapp.account.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hernan.gymapp.account.data.model.AdditionalDateDto
import com.hernan.gymapp.account.domain.repository.CreateAdditionalDateRepo
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CreateAdditionalDateRepoImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : CreateAdditionalDateRepo {
    override suspend fun setDocument(
        collectionPath: String,
        documentId: String,
        data: AdditionalDateDto
    ): Flow<ResourceFlow<Unit>> = flow {
        emit(ResourceFlow.Loading)
        try {
            val dataValue = data.toDomain()
            firestore.collection(collectionPath)
                .document(documentId)
                .set(dataValue)
                .await()
            emit(ResourceFlow.Success(Unit))
        } catch (e: Exception) {
            emit(ResourceFlow.Error(e.message ?: "Error saving user data"))
        }
    }


}