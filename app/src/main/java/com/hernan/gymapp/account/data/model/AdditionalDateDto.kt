package com.hernan.gymapp.account.data.model

import com.hernan.gymapp.account.domain.model.AdditionalDate

data class AdditionalDateDto(
    var typeId: String = "",
    var name: String = "",
    var userImage: String = "",
    var classes: String = "",
    var edad: String = "",
    var estadoSalud: String = "",
    var objetivo: String = "",
    var phoneNumber: String = "",
    var weight: String = "",
    var gender: String = "",
    var pay: Boolean = false,
) {
    fun toDomain(): AdditionalDate {
        return AdditionalDate(
            typeId,
            name,
            userImage,
            classes,
            edad,
            estadoSalud,
            objetivo,
            phoneNumber,
            weight,
            gender,
            pay
        )
    }
}