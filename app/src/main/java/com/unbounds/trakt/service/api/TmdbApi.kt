package com.unbounds.trakt.service.api

import com.unbounds.trakt.service.api.model.tmdb.Images
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TmdbApi {

    @GET("tv/{tv_id}/season/{season_number}/images")
    fun getImages(
            @Path("tv_id") showId: Int,
            @Path("season_number") seasonNumber: Int,
    ): Deferred<Response<Images>>
}

const val IMAGE_PREFIX = "https://image.tmdb.org/t/p/w200"
