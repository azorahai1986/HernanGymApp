package com.hernan.gymapp.account.data.repository

import com.google.firebase.storage.FirebaseStorage
import com.hernan.gymapp.account.domain.repository.StorageRepository
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage: FirebaseStorage
): StorageRepository {
    override fun uploadProfileImage(
        userId: String,
        imageBytes: ByteArray
    ): Flow<ResourceFlow<String>> = flow {
        emit(ResourceFlow.Loading)
        try {
            val ref = storage.reference.child("profileImages/$userId.jpg")
            ref.putBytes(imageBytes).await()
            val downloadUrl = ref.downloadUrl.await().toString()
            emit(ResourceFlow.Success(downloadUrl))
        } catch (e: Exception) {
            emit(ResourceFlow.Error(e.message ?: "Error uploading image"))
        }
    }

}