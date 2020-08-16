package com.unbounds.trakt.service.api.model

/**
 * Created by maclir on 2015-11-21.
 */
data class Person(
        val name: String,
        val ids: PersonIds,
)

data class PersonIds(
        val trakt: Long,
        val slug: String,
        val imdb: String,
        val tmdb: Long,
)
