package com.unbounds.trakt.service.api

import com.unbounds.trakt.service.api.model.request.Code
import com.unbounds.trakt.service.api.model.request.WatchedItems
import com.unbounds.trakt.service.api.model.response.AddHistory
import com.unbounds.trakt.service.api.model.response.Token
import com.unbounds.trakt.service.api.model.response.WatchedProgress
import com.unbounds.trakt.service.api.model.response.WatchedShow
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TraktApi {

    @POST("oauth/token")
    fun getToken(@Body code: Code): Deferred<Response<Token>>

    @GET("sync/watched/shows")
    fun getWatchedShows(): Deferred<Response<List<WatchedShow>>>

    @GET("shows/{showId}/progress/watched")
    fun getWatchedProgress(@Path("showId") showId: Long): Deferred<Response<WatchedProgress>>

    @POST("sync/history")
    fun postWatchedItems(@Body watchedItems: WatchedItems): Deferred<Response<AddHistory>>
}