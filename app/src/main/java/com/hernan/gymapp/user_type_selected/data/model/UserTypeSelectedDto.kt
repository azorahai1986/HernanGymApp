package com.hernan.gymapp.user_type_selected.data.model

import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected

data class UserTypeSelectedDto(
    var typeId: String = "",
    var typeUserIcon: String = "",
    var name: String = "",
    var userImage: String = "",
    var classes: String = "",
    var classesIcon: String = "",
    var edad: String = "",
    var edadIcon: String = "",
    var estadoSalud: String = "",
    var estadoSaludIcon: String = "",
    var payIcon: String = "",
    var objetivo: String = "",
    var objetivoIcon: String = "",
    var phoneIcon: String = "",
    var phoneNumber: String = "",
    var weight: String = "",
    var weightIcon: String = "",
    var pay: String = "false",
) {
    fun toDomain(): UserTypeSelected {
        return UserTypeSelected(
            typeId,
            typeUserIcon,
            name,
            userImage,
            classes,
            classesIcon,
            edad,
            edadIcon,
            estadoSalud,
            estadoSaludIcon,
            payIcon,
            objetivo,
            objetivoIcon,
            phoneIcon,
            phoneNumber,
            weight,
            weightIcon,
            pay
        )
    }
}
