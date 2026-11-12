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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

const val ALL_USERS_FILTER = "all"

@HiltViewModel
class UserTypeSelectedVM @Inject constructor(
    private val userTypeSelectedUseCase: UserTypeSelectedUseCase
): ViewModel() {

    private val _userType = MutableStateFlow<ResourceFlow<List<UserTypeSelected>>>(ResourceFlow.Loading)
    val userType: StateFlow<ResourceFlow<List<UserTypeSelected>>> = _userType.asStateFlow()

    fun fetchClient(clientId: String) {
        /**
         * Esta llamada garantiza que solo busque una vez al backend
         * **/
        if (_userType.value is ResourceFlow.Success) return
        viewModelScope.launch {
            val usersFlow = if (clientId == ALL_USERS_FILTER) {
                userTypeSelectedUseCase.getAllUsers()
            } else {
                userTypeSelectedUseCase(clientId)
            }
            usersFlow.collectLatest { result ->
                _userType.value = result
            }
        }
    }
}
