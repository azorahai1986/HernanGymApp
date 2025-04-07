package com.hernan.gymapp.user_type_selected.data.model

import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected

data class UserTypeSelectedDto(
    var typeId: String = "",
    var name: String = "",
    var userImage: String = "",
    var classes: String = "",
    var edad: String = "",
    var estadoSalud: String = "",
    var objetivo: String = "",
    var phoneNumber: String = "",
    var weight: String = "",
    var pay: Boolean = false,
) {
    fun toDomain(): UserTypeSelected {
        return UserTypeSelected(
            typeId,
            name,
            userImage,
            classes,
            edad,
            estadoSalud,
            objetivo,
            phoneNumber,
            weight,
            pay
        )
    }
}
