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
    var gender: String = "",
    var pay: Boolean = false,
    var preferredDays: List<Int> = emptyList(),
    var preferredHours: Map<String, String> = emptyMap(),
    var weeklyClassLimit: Int = 2,
    var scheduleMode: String = "FIXED",
) {
    fun toDomain(documentId: String = ""): UserTypeSelected {
        return UserTypeSelected(
            documentId = documentId,
            typeId = typeId,
            name = name,
            userImage = userImage,
            classes = classes,
            edad = edad,
            estadoSalud = estadoSalud,
            objetivo = objetivo,
            phoneNumber = phoneNumber,
            weight = weight,
            gender = gender,
            pay = pay,
            preferredDays = preferredDays,
            preferredHours = preferredHours,
            weeklyClassLimit = weeklyClassLimit,
            scheduleMode = scheduleMode
        )
    }
}
