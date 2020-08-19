package com.unbounds.trakt.service.api.model.trakt.response

import com.squareup.moshi.Json
import com.unbounds.trakt.service.api.model.trakt.Movie
import com.unbounds.trakt.service.api.model.trakt.Show

data class Search(
        val type: Type,
        val score: Double,
        val movie: Movie,
        val show: Show
) {
    enum class Type {
        @Json(name = "movie")
        MOVIE,

        @Json(name = "show")
        SHOW,
    }
}
