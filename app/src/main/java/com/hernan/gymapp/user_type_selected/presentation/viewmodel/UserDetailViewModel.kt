package com.hernan.gymapp.user_type_selected.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor() : ViewModel() {
        private val _selectedUser = MutableStateFlow<UserTypeSelected?>(null)
        val selectedUser: StateFlow<UserTypeSelected?> = _selectedUser.asStateFlow()

        fun setSelectedUser(user: UserTypeSelected) {
            _selectedUser.value = user
        }
}