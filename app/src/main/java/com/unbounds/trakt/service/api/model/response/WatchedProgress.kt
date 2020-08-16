package com.unbounds.trakt.service.api.model.response

import com.unbounds.trakt.service.api.model.Episode
import com.unbounds.trakt.service.api.model.Season

/**
 * Created by maclir on 2015-11-21.
 */
data class WatchedProgress(
        val aired: Long,
        val completed: Long,
        val last_watched_at: String,
        val seasons: List<ExtendedSeason>,
        val hidden_seasons: List<Season>,
        val next_episode: Episode?,
) {

    val isCompleted = aired == completed

}
