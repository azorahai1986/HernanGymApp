package com.hernan.gymapp.routins_and_excercises.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.routins_and_excercises.domain.model.RoutinService
import com.hernan.gymapp.routins_and_excercises.domain.usecase.RoutineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val routineUseCase: RoutineUseCase
): ViewModel() {
    private val _routines = MutableStateFlow<ResourceFlow<List<RoutinService>>>(ResourceFlow.Loading)
    val routines: StateFlow<ResourceFlow<List<RoutinService>>> = _routines.asStateFlow()

    fun fetchRoutineData() {
        /**
         * Esta llamada garantiza que solo busque una vez al backend
         * **/
        if (_routines.value is ResourceFlow.Success) return
        viewModelScope.launch {
            routineUseCase().collectLatest { result ->
                _routines.value = result
            }
        }
    }
}