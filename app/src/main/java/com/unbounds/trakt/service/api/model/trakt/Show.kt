package com.unbounds.trakt.service.api.model.trakt

/**
 * Created by maclir on 2015-11-21.
 */
data class Show(
        val title: String,
        val year: Long?,
        val ids: ShowIds,
)

data class ShowIds(
        val trakt: Long,
        val slug: String,
        val tvdb: Long?,
        val imdb: String,
        val tmdb: Long?,
)
