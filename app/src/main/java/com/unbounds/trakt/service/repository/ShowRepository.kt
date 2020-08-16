package com.unbounds.trakt.service.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.unbounds.trakt.service.api.TraktApi
import com.unbounds.trakt.service.api.model.trakt.request.Episode
import com.unbounds.trakt.service.api.model.trakt.request.EpisodeIds
import com.unbounds.trakt.service.api.model.trakt.request.WatchedItems
import com.unbounds.trakt.service.api.model.trakt.response.WatchedProgress
import com.unbounds.trakt.service.api.model.trakt.response.WatchedShow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShowRepository @Inject constructor(private val api: TraktApi) {

    private val watchedShowsMutable = MutableLiveData<List<WatchedShow>>()
    val watchedShows: LiveData<List<WatchedShow>> = watchedShowsMutable.map {
        progressMutables.clear()
        it
    }

    private val progressMutables = HashMap<Long, MutableLiveData<WatchedProgress>>()

    fun getProgress(showId: Long): LiveData<WatchedProgress> = MutableLiveData<WatchedProgress>().apply {
        progressMutables[showId] = this
        CoroutineScope(Dispatchers.IO).launch {
            postValue(api.getWatchedProgress(showId).await().body())
        }
    }

    fun reload() = CoroutineScope(Dispatchers.IO).launch {
        watchedShowsMutable.postValue(listOf())
        watchedShowsMutable.postValue(api.getWatchedShows().await().body() ?: listOf())
    }

    fun episodeWatched(showId: Long, episodeId: Long) = CoroutineScope(Dispatchers.IO).launch {
        api.postWatchedItems(WatchedItems(episodes = listOf(Episode(ids = EpisodeIds(trakt = episodeId))))).await()
        progressMutables[showId]?.postValue(api.getWatchedProgress(showId).await().body())
    }
}
