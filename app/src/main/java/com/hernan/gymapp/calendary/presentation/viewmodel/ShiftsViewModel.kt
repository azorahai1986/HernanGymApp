package com.hernan.gymapp.calendary.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hernan.gymapp.calendary.data.model.Shifts
import com.hernan.gymapp.calendary.domain.usecase.LocalDateUseCase
import com.hernan.gymapp.common.state.ResourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShiftsViewModel @Inject constructor(
    private val localDateUseCase: LocalDateUseCase
):ViewModel() {

    val _shift = MutableStateFlow<ResourceFlow<String>>(ResourceFlow.Idle)
    val shift: StateFlow<ResourceFlow<String>> = _shift

    fun setShift(shift: Shifts, userId: String?) {
        viewModelScope.launch {
            localDateUseCase(shift, userId).collect { result ->
                _shift.value = result
            }
        }
    }
}