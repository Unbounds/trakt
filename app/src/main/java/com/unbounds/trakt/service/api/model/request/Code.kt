package com.unbounds.trakt.service.api.model.request

import com.unbounds.trakt.BuildConfig

/**
 * Created by maclir on 2015-11-08.
 */
data class Code(
        val code: String,
        val redirect_uri: String,
) {
    val client_id: String = BuildConfig.CLIENT_ID
    val client_secret: String = BuildConfig.CLIENT_SECRET
    val grant_type: String = "authorization_code"
}
