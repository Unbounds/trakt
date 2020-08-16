package com.unbounds.trakt.service.api.model

/**
 * Created by maclir on 2015-11-21.
 */
data class Movie(
        val title: String,
        val year: Long,
        val ids: MovieIds,
)

data class MovieIds(
        val trakt: Long,
        val slug: String,
        val imdb: String,
        val tmdb: Long,
)
