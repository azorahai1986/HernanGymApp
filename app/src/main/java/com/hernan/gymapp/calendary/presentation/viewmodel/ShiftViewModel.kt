package com.hernan.gymapp.calendary.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hernan.gymapp.calendary.domain.model.Shift
import com.hernan.gymapp.calendary.domain.usecase.ShiftUseCase
import com.hernan.gymapp.common.state.ResourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShiftViewModel @Inject constructor(
    private val shiftUseCase: ShiftUseCase
): ViewModel() {
    private val _shift = MutableStateFlow<ResourceFlow<List<Shift>>>(ResourceFlow.Loading)
    val shift: StateFlow<ResourceFlow<List<Shift>>> = _shift.asStateFlow()

    fun getAllShifts() {
        if (_shift.value is ResourceFlow.Success) return

            viewModelScope.launch {
                shiftUseCase().collectLatest { result ->
                    _shift.value = result
                }
            }


    }
}