package com.hernan.gymapp.user_type_selected.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hernan.gymapp.common.state.ResourceFlow
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import com.hernan.gymapp.user_type_selected.domain.usecase.UserTypeSelectedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userTypeSelectedUseCase: UserTypeSelectedUseCase
) : ViewModel() {
        private val _selectedUser = MutableStateFlow<UserTypeSelected?>(null)
        val selectedUser: StateFlow<UserTypeSelected?> = _selectedUser.asStateFlow()
        private val _scheduleSave = MutableStateFlow<ResourceFlow<Unit>>(ResourceFlow.Idle)
        val scheduleSave: StateFlow<ResourceFlow<Unit>> = _scheduleSave.asStateFlow()
        private val _userLoad = MutableStateFlow<ResourceFlow<UserTypeSelected>>(ResourceFlow.Loading)
        val userLoad: StateFlow<ResourceFlow<UserTypeSelected>> = _userLoad.asStateFlow()

        fun loadUserById(documentId: String) {
            if (_userLoad.value is ResourceFlow.Success && _selectedUser.value?.documentId == documentId) return
            viewModelScope.launch {
                _userLoad.value = ResourceFlow.Loading
                when (val result = userTypeSelectedUseCase.getUserById(documentId)) {
                    is ResourceFlow.Success -> {
                        _selectedUser.value = result.data
                        _scheduleSave.value = ResourceFlow.Idle
                        _userLoad.value = result
                    }
                    is ResourceFlow.Error -> _userLoad.value = result
                    is ResourceFlow.Loading -> _userLoad.value = ResourceFlow.Loading
                    is ResourceFlow.Idle -> _userLoad.value = ResourceFlow.Idle
                }
            }
        }

        fun setSelectedUser(user: UserTypeSelected) {
            _selectedUser.value = user
            _scheduleSave.value = ResourceFlow.Idle
        }

        fun clearScheduleStatus() {
            if (_scheduleSave.value !is ResourceFlow.Loading) _scheduleSave.value = ResourceFlow.Idle
        }

        fun saveSchedule(
            preferredDays: List<Int>,
            preferredHours: Map<String, String>,
            weeklyClassLimit: Int,
            scheduleMode: String
        ) {
            val user = _selectedUser.value ?: return
            if (weeklyClassLimit !in setOf(1, 2, 3, 7) ||
                preferredDays.distinct().size != weeklyClassLimit || preferredDays.any { it !in 1..7 }
            ) {
                _scheduleSave.value = ResourceFlow.Error("Elegí la cantidad de días que permite el plan")
                return
            }
            if (preferredDays.any { preferredHours[it.toString()].isNullOrBlank() }) {
                _scheduleSave.value = ResourceFlow.Error("Elegí un horario para cada día seleccionado")
                return
            }
            if (scheduleMode !in setOf("FIXED", "FLEXIBLE")) {
                _scheduleSave.value = ResourceFlow.Error("La frecuencia seleccionada no es válida")
                return
            }
            _scheduleSave.value = ResourceFlow.Loading
            viewModelScope.launch {
                val result = userTypeSelectedUseCase.updateSchedule(
                    user.documentId,
                    preferredDays,
                    preferredHours.filterKeys { key -> key.toIntOrNull()?.let { it in preferredDays } == true },
                    weeklyClassLimit,
                    scheduleMode
                )
                _scheduleSave.value = result
                if (result is ResourceFlow.Success) {
                    _selectedUser.value = user.copy(
                        preferredDays = preferredDays,
                        preferredHours = preferredHours,
                        weeklyClassLimit = weeklyClassLimit,
                        scheduleMode = scheduleMode
                    )
                }
            }
        }
}
