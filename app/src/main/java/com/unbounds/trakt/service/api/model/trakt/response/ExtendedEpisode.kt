package com.unbounds.trakt.service.api.model.trakt.response

import com.unbounds.trakt.service.api.model.trakt.EpisodeIds

/**
 * Created by maclir on 2015-11-21.
 */
data class ExtendedEpisode(
        val season: Long,
        val number: Long,
        val title: String,
        val ids: EpisodeIds,
        val plays: Long,
        val is_completed: Boolean,
        val last_watched_at: String,
)
