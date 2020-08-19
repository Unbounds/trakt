package com.unbounds.trakt.service.api.model.trakt.response

import com.unbounds.trakt.service.api.model.trakt.Episode
import com.unbounds.trakt.service.api.model.trakt.Season

/**
 * Created by maclir on 2015-11-21.
 */
data class WatchedProgress(
        val aired: Long,
        val completed: Long,
        val last_watched_at: String?,
        val seasons: List<ExtendedSeason>,
        val hidden_seasons: List<Season>,
        val next_episode: Episode?,
) {
    val isCompleted: Boolean
        get() = aired == completed
}
