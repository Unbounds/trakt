package com.unbounds.trakt.service.api.model.trakt.response

import com.unbounds.trakt.service.api.model.trakt.SeasonIds

/**
 * Created by maclir on 2015-11-21.
 */
data class ExtendedSeason(
        val number: Long,
        val ids: SeasonIds,
        val episodes: List<ExtendedEpisode>,
        val aired: Long,
        val completed: Long
)
