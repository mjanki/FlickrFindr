package org.br.repository.models

import org.br.util.enums.ErrorNetworkTypes

data class ErrorNetworkRepoEntity(
        var id: Long? = null,
        var type: ErrorNetworkTypes = ErrorNetworkTypes.OTHER,
        var shouldPersist: Boolean = false,
        var code: Int = 0,
        var message: String = "",
        var action: String = ""
)