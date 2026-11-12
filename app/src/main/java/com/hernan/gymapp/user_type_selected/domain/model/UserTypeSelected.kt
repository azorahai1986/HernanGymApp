package com.hernan.gymapp.user_type_selected.domain.model

data class UserTypeSelected(
    var documentId: String = "",
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
)
