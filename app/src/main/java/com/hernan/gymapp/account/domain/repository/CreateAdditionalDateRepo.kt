package com.hernan.gymapp.account.domain.repository

import com.hernan.gymapp.account.data.model.AdditionalDateDto
import com.hernan.gymapp.common.state.ResourceFlow
import kotlinx.coroutines.flow.Flow

interface CreateAdditionalDateRepo {
    suspend fun setDocument(
        collectionPath: String,
        documentId: String,
        data: AdditionalDateDto
    ): Flow<ResourceFlow<Unit>>
}