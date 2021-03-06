package com.unbounds.trakt.service.api

import com.unbounds.trakt.service.api.model.trakt.request.Code
import com.unbounds.trakt.service.api.model.trakt.request.WatchedItems
import com.unbounds.trakt.service.api.model.trakt.response.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface TraktApi {

    @POST("oauth/token")
    fun getToken(@Body code: Code): Deferred<Response<Token>>

    @GET("sync/watched/shows")
    fun getWatchedShows(): Deferred<Response<List<WatchedShow>>>

    @GET("shows/{showId}/progress/watched")
    fun getWatchedProgress(@Path("showId") showId: Long): Deferred<Response<WatchedProgress>>

    @GET("search/show")
    fun search(@Query("query") query: String): Deferred<Response<List<Search>>>

    @POST("sync/history")
    fun postWatchedItems(@Body watchedItems: WatchedItems): Deferred<Response<AddHistory>>
}
