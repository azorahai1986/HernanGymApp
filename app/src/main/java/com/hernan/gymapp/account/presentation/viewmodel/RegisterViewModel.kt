package com.hernan.gymapp.account.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hernan.gymapp.account.domain.model.AccountData
import com.hernan.gymapp.account.domain.usecase.AccountUseCase
import com.hernan.gymapp.common.state.ResourceFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel@Inject constructor(
    private val registerUserUseCase: AccountUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ResourceFlow<AccountData>>(ResourceFlow.Loading)
    val state: StateFlow<ResourceFlow<AccountData>> = _state

    fun register(accountData: AccountData) {

        if (_state.value is ResourceFlow.Success) return
        _state.value = ResourceFlow.Loading
        viewModelScope.launch {
            registerUserUseCase(accountData).collectLatest { result ->
                _state.value = result
            }
        }
    }
}
