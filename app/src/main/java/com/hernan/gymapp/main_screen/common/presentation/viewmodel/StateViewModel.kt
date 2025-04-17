package com.hernan.gymapp.main_screen.common.presentation.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hernan.gymapp.main_screen.list_elements.domain.model.FirstState
import com.hernan.gymapp.main_screen.list_elements.domain.usecase.FirstStateUseCase
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.main_screen.admin_qr.domain.model.QrAdminInfo
import com.hernan.gymapp.main_screen.admin_qr.domain.usecase.QrAdminUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StateViewModel @Inject constructor(
    private val firstStateUseCase: FirstStateUseCase,
    private val qrAdminUseCase: QrAdminUseCase
) : ViewModel() {
    private val _firstState = MutableStateFlow<ResourceFlow<List<FirstState>>>(ResourceFlow.Loading)
    val firstState: StateFlow<ResourceFlow<List<FirstState>>> = _firstState.asStateFlow()

    private val _qrCheckIn = MutableStateFlow<ResourceFlow<List<QrAdminInfo>>>(ResourceFlow.Loading)
    val qrCheckIn: StateFlow<ResourceFlow<List<QrAdminInfo>>> = _qrCheckIn.asStateFlow()

    init {
        fetchFirstState()
        fetchQrCheckIn()
    }
    fun fetchFirstState() {
        viewModelScope.launch {
            firstStateUseCase().collect { result ->
                _firstState.value = result
            }
        }
    }

    fun fetchQrCheckIn() {
        viewModelScope.launch {
            qrAdminUseCase().collectLatest { result ->
                _qrCheckIn.value = result
            }
        }
    }
}