package com.hernan.gymapp.routins_and_excercises.data.model

import com.hernan.gymapp.routins_and_excercises.domain.model.RoutinService

data class RoutinServiceDto(
    val videoName: String = "",
    val videoLink: String = ""
) {
    fun toDomain(): RoutinService {
        return RoutinService(videoName ,videoLink)
    }
}
