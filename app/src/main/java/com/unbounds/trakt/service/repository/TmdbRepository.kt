package com.unbounds.trakt.service.repository

import com.unbounds.trakt.service.api.TmdbApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TmdbRepository @Inject constructor(private val api: TmdbApi) {

    suspend fun getImages(showId: Int, seasonNumber: Int, episodeNumber: Int) = withContext(Dispatchers.IO) {
        api.getImages(showId, seasonNumber).await().body()?.posters ?: listOf()
    }
}
