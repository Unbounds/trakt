package com.unbounds.trakt.utils

import com.google.gson.GsonBuilder

object JsonSerializer {
    private val GSON = GsonBuilder().create()

    fun <T> fromJson(json: String?, classOfT: Class<T>): T? {
        return try {
            if (json == null) null else GSON.fromJson(json, classOfT)
        } catch (exception: Exception) {
            throw JsonSerializerException(json, classOfT)
        }
    }

    fun toJson(`object`: Any?): String {
        return GSON.toJson(`object`)
    }

    fun toJson(`object`: Any?, className: Class<*>?): String {
        return GSON.toJson(`object`, className)
    }

    internal class JsonSerializerException(json: String?, classOfT: Class<*>) : RuntimeException(String.format("Could not parse:\n%s\nas %s", json, classOfT.canonicalName))
}
