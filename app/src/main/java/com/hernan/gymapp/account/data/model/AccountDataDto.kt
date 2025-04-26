package com.hernan.gymapp.account.data.model

import com.hernan.gymapp.account.domain.model.AccountData

data class AccountDataDto(
    var name: String = "",
    var email: String = "",
    var date: String = "",
    var phoneNumber: String = "",
    var gender: String = "",
    var password: String = ""// sexo. 3 sexos. recomendable un check para seleccionar uno de los 3
) {
    fun toDomain(): AccountData =
        AccountData(
            name,
            email,
            date,
            phoneNumber,
            gender,
            password
        )
}