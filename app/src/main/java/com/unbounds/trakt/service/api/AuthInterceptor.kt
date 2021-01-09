package com.unbounds.trakt.service.api

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.unbounds.trakt.service.api.model.trakt.response.Token
import com.unbounds.trakt.utils.Event
import com.unbounds.trakt.utils.JsonSerializer
import com.unbounds.trakt.utils.toEvent
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
        private val storage: SharedPreferences,
        @Named("auth")
        private val authError: MutableLiveData<Event<Unit>>,
) : Interceptor {
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
        requestBuilder.addHeader("Authorization", "Bearer ${token?.access_token}")

        val response = chain.proceed(requestBuilder.build())

        if (!response.isSuccessful && response.code == 401) {
            authError.postValue(Unit.toEvent())
        }
        return response
    }

    companion object {
        private const val KEY_TOKEN = "KEY_TOKEN"
    }
}
