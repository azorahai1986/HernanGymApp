package com.hernan.gymapp.user_type.data.model

import com.hernan.gymapp.user_type.domain.model.Client

data class ClientDto(
    val typeClient: String = "",
    val image: String = ""
) {
    fun toDomain(): Client {
        return Client(typeClient ,image)
    }
}
