package com.hernan.gymapp.main_screen.admin_qr.data.model

import com.hernan.gymapp.main_screen.admin_qr.domain.model.QrAdminInfo

data class QrAdminInfoDto(
    var checkInQr:String = "",
    var aliasQr:String = ""
) {
    fun toDomain(): QrAdminInfo =
        QrAdminInfo(
        checkInQr,
        aliasQr
        )


}
