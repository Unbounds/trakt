package com.unbounds.trakt.service.api.model.tmdb

/**
 * Created by maclir on 2015-11-21.
 */
data class Images(
        val id: Long,
        val posters: List<Poster>,
) {
    data class Poster(
            val aspect_ratio: Double,
            val file_path: String,
            val height: Int,
            val vote_average: Double,
            val vote_count: Int,
            val width: Int,
    )
}
