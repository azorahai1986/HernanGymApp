package com.hernan.gymapp.account.domain.usecase

import com.hernan.gymapp.account.data.model.AdditionalDateDto
import com.hernan.gymapp.account.domain.repository.CreateAdditionalDateRepo
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddtionalDateUseCase @Inject constructor(
    private val createAdditionalDateRepo: CreateAdditionalDateRepo
) {
    suspend operator fun invoke(
        collectionPath: String,
        documentId: String,
        data: AdditionalDateDto
    ): Flow<ResourceFlow<Unit>> =
        createAdditionalDateRepo.setDocument(collectionPath, documentId, data)
}