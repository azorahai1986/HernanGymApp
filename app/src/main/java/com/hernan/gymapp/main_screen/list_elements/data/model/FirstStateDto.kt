package com.hernan.gymapp.main_screen.list_elements.data.model

import com.hernan.gymapp.main_screen.list_elements.domain.model.FirstState

data class FirstStateDto(
    val name: String = ""
) {
    fun toDomain(): FirstState {
        return FirstState(name)
    }
}
