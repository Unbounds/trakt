package com.unbounds.trakt.service.api.model.trakt

/**
 * Created by maclir on 2015-11-21.
 */
data class Season(
        val number: Long,
        val ids: SeasonIds,
)

data class SeasonIds(
        val trakt: Long,
        val tvdb: Long,
        val tmdb: Long?,
)
