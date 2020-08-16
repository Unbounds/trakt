package com.unbounds.trakt.service.api.model.trakt.request

import com.unbounds.trakt.service.api.model.trakt.Movie
import com.unbounds.trakt.service.api.model.trakt.Show

/**
 * Created by maclir on 2015-11-21.
 */
data class WatchedItems(
        val movies: List<Movie>? = null,
        val shows: List<Show>? = null,
        val episodes: List<Episode>? = null,
)

data class Episode(
        val ids: EpisodeIds,
)

data class EpisodeIds(
        val trakt: Long,
)
