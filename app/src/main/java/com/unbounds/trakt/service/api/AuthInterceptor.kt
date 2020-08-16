package com.unbounds.trakt.service.api

import android.content.SharedPreferences
import com.unbounds.trakt.service.api.model.response.Token
import com.unbounds.trakt.utils.JsonSerializer
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(private val storage: SharedPreferences) : Interceptor {
    var token: Token? = JsonSerializer.fromJson(storage.getString(KEY_TOKEN, null), Token::class.java)
        set(value) {
            field = value
            if (value != null) {
                storage.edit().putString(KEY_TOKEN, JsonSerializer.toJson(token)).apply()
            } else {
                storage.edit().remove(KEY_TOKEN).apply()
            }
        }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        val token = token
        if (token != null) {
            requestBuilder.addHeader("Authorization", "Bearer ${token.access_token}")
        }
        return chain.proceed(requestBuilder.build())
    }

    companion object {
        private const val KEY_TOKEN = "KEY_TOKEN"
    }
}
