package com.hernan.gymapp.account.domain.model

data class AccountData(
    var name: String = "",
    var email: String = "",
    var date: String = "",
    var phoneNumber: String = "",
    var gender: String = "", // sexo. 3 sexos. recomendable un check para seleccionar uno de los 3
)
