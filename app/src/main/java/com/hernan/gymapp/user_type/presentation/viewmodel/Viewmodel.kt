package com.hernan.gymapp.user_type.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hernan.gymapp.user_type.domain.model.Client
import com.hernan.gymapp.user_type.domain.usecase.GetClientUsecase
import com.hernan.gymapp.common.state.ResourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val getClientUsecase: GetClientUsecase
) : ViewModel() {

    private val _clientState = MutableStateFlow<ResourceFlow<List<Client>>>(ResourceFlow.Loading)
    val clientState: StateFlow<ResourceFlow<List<Client>>> = _clientState.asStateFlow()

    /*init {
        fetchClient("")
    }*/
    fun fetchClient(clientId: String) {
        /**
         * Esta llamada garantiza que solo busque una vez al backend
         * **/
        if (_clientState.value is ResourceFlow.Success) return
        viewModelScope.launch {
            getClientUsecase(clientId).collectLatest { result ->
                _clientState.value = result
            }
        }
    }
}