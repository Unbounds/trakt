package com.unbounds.trakt.service.api.model.response

/**
 * Created by maclir on 2015-11-08.
 */
data class Token(
        val access_token: String? = null,
        val token_type: String? = null,
        val expires_in: String? = null,
        val refresh_token: String? = null,
        val scope: String? = null,
)
