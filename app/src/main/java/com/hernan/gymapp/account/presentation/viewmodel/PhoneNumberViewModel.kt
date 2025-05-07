package com.hernan.gymapp.account.presentation.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PhoneNumberViewModel: ViewModel() {

    var phoneNumber by mutableStateOf("")
        private set

    val phoneNumberHasErrors by derivedStateOf {
        if (phoneNumber.isNotEmpty()) {
            !Regex("^\\d{1,10}$").matches(phoneNumber)

        } else {
            false
        }
    }

    fun updatePhoneNumber(input: String) {
        phoneNumber = input
    }
}