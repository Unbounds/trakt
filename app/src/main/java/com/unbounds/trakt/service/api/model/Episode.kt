package com.unbounds.trakt.service.api.model

/**
 * Created by maclir on 2015-11-21.
 */
data class Episode(
        val season: Long,
        val number: Long,
        val title: String,
        val ids: EpisodeIds,
)

data class EpisodeIds(
        val trakt: Long,
        val tvdb: Long,
        val imdb: String,
        val tmdb: Long,
)
