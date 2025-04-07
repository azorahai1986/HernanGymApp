package com.hernan.gymapp.user_type_selected.domain.model

data class UserTypeSelected(
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
)