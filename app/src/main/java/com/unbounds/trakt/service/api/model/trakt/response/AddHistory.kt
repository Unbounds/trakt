package com.unbounds.trakt.service.api.model.trakt.response

import com.unbounds.trakt.service.api.model.trakt.Episode
import com.unbounds.trakt.service.api.model.trakt.Movie
import com.unbounds.trakt.service.api.model.trakt.Season
import com.unbounds.trakt.service.api.model.trakt.Show

/**
 * Created by maclir on 2015-11-21.
 */
data class AddHistory(
        val added: Added,
        val not_found: NotFound
) {

    data class Added(
            val movies: Long,
            val episodes: Long
    )

    data class NotFound(
            val movies: List<Movie>,
            val shows: List<Show>,
            val seasons: List<Season>,
            val episodes: List<Episode>
    )
}
