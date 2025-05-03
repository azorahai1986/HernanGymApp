package com.hernan.gymapp.account.data.model

import com.hernan.gymapp.account.domain.model.UserImageDate

data class UserImageDateDto(
    var username: String = "",
    var profileImageUrl: String = ""
) {
    fun toDomain() = UserImageDate(username, profileImageUrl)
}
