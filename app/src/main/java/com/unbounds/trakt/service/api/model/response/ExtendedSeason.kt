package com.unbounds.trakt.service.api.model.response

import com.unbounds.trakt.service.api.model.SeasonIds

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
