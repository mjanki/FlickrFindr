package org.br.viewmodel.models

import org.br.util.enums.ErrorNetworkTypes

data class ErrorNetworkViewModelEntity(
        var id: Long? = null,
        var type: ErrorNetworkTypes = ErrorNetworkTypes.OTHER,
        var shouldPersist: Boolean = false,
        var code: Int = 0,
        var message: String = "",
        var action: String = ""
)