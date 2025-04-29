package com.hernan.gymapp.account.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hernan.gymapp.account.data.model.AdditionalDateDto
import com.hernan.gymapp.account.domain.model.AdditionalDate
import com.hernan.gymapp.account.domain.usecase.AddtionalDateUseCase
import com.hernan.gymapp.account.domain.usecase.StorageUseCase
import com.hernan.gymapp.common.state.ResourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AdditionalDateViewModel @Inject constructor(
    private val additionalDateUseCase: AddtionalDateUseCase,
    private val storageUseCase: StorageUseCase
):ViewModel() {


    private val _userAcconted = MutableStateFlow<ResourceFlow<Unit>>(ResourceFlow.Loading)
    val userAcconted: StateFlow<ResourceFlow<Unit>> = _userAcconted

    private val _uploadImage = MutableStateFlow<ResourceFlow<String>>(ResourceFlow.Loading)
    val uploadImage: StateFlow<ResourceFlow<String>> = _uploadImage

    fun setimageToStorage(userId: String?, imageBytes: ByteArray) {
        viewModelScope.launch {
            if (userId != null) {
                storageUseCase(userId, imageBytes)
                    .collect { state ->
                        _uploadImage.value = state
                    }
            }
        }
    }

    fun saveUser(collectionPath: String,
                 documentId: String,
                 user: AdditionalDateDto) {
        viewModelScope.launch {
            additionalDateUseCase(collectionPath, documentId, user).collect { state ->
                    _userAcconted.value = state
                }
        }
    }
}