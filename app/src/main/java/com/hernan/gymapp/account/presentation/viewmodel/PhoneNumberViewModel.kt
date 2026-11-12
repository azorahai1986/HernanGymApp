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
            phoneNumber.count(Char::isDigit) !in 8..15

        } else {
            false
        }
    }

    fun updatePhoneNumber(input: String) {
        phoneNumber = input.filter { it.isDigit() || it in "+ ()-" }.take(20)
    }
}
