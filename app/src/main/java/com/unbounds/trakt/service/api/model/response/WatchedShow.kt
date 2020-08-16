package com.unbounds.trakt.service.api.model.response

import com.unbounds.trakt.service.api.model.Show

/**
 * Created by maclir on 2015-11-17.
 */
data class WatchedShow(
        val plays: Long,
        val last_watched_at: String,
        val show: Show,
        val seasons: List<ExtendedSeason>
)
