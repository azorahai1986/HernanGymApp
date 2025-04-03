package com.hernan.gymapp.user_type_selected.data.model

import com.hernan.gymapp.user_type_selected.domain.model.UserTypeSelected

data class UserTypeSelectedDto(
    val typeId: String = "",
    val name: String = "",
    val userImage: String = ""
) {
    fun toDomain(): UserTypeSelected {
        return UserTypeSelected(typeId, name ,userImage)
    }
}
