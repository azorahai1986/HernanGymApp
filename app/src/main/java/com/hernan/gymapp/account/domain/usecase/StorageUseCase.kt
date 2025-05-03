package com.hernan.gymapp.account.domain.usecase

import com.hernan.gymapp.account.domain.repository.StorageRepository
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StorageUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {
    operator fun invoke(userId: String, imageBytes: ByteArray): Flow<ResourceFlow<String>> =
        storageRepository.uploadProfileImage(userId, imageBytes)


}