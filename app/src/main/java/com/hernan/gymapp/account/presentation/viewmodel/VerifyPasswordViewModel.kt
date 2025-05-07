package com.hernan.gymapp.account.presentation.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class VerifyPasswordViewModel: ViewModel() {
    var password by mutableStateOf("")
        private set

    val passwordHasErrors by derivedStateOf {
        if (password.isNotEmpty()) {
            !Regex("^(?=.*[A-Z])(?=.*\\d).{8,}\$").matches(password)
        } else {
            false
        }
    }

    fun updatePassword(input: String) {
        password = input
    }
}