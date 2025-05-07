package com.hernan.gymapp.account.presentation.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class VerifyEmailViewModel: ViewModel() {
    var email by mutableStateOf("")
        private set

    val emailHasErrors by derivedStateOf {
        if (email.isNotEmpty()) {
            // Email is considered erroneous until it completely matches EMAIL_ADDRESS.
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    fun updateEmail(input: String) {
        email = input
    }
}