package com.unbounds.trakt.service.repository

import androidx.lifecycle.*
import com.unbounds.trakt.service.api.IMAGE_PREFIX
import com.unbounds.trakt.service.api.TmdbApi
import com.unbounds.trakt.service.api.TraktApi
import com.unbounds.trakt.service.api.model.trakt.request.Episode
import com.unbounds.trakt.service.api.model.trakt.request.EpisodeIds
import com.unbounds.trakt.service.api.model.trakt.request.WatchedItems
import com.unbounds.trakt.service.api.model.trakt.response.Search
import com.unbounds.trakt.service.api.model.trakt.response.WatchedProgress
import com.unbounds.trakt.utils.ListState
import com.unbounds.trakt.utils.replaceOrAdd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.set

@Singleton
class SearchRepository @Inject constructor(
        private val api: TraktApi,
        private val tmdbApi: TmdbApi,
) {

    private val refreshingMutable = MutableLiveData<ListState>()
    val refreshing: LiveData<ListState> = refreshingMutable

    private val progressMutables = HashMap<Long, MutableLiveData<WatchedProgress>>()
    private val mediator = MediatorLiveData<List<ShowProgress<Double>>>()
    private val searchMutable = MutableLiveData<List<Search>>()
    val shows = searchMutable.switchMap { list ->
        progressMutables.clear()
        mediator.value = listOf()
        var loadingCounter = 0

        list.forEach { search ->
            loadingCounter++
            val showId = search.show.ids.trakt
            val progressMutable = getProgress(showId)
            progressMutables[showId] = progressMutable

            mediator.addSource(progressMutable) { progress ->
                if (progress?.next_episode != null && !progress.isCompleted) {
                    val showProgress = ShowProgress(search.show, progress, progress.next_episode, search.score)
                    with(mediator.value ?: listOf()) {
                        mediator.value = replaceOrAdd(showProgress) { loadedProgress -> loadedProgress.show.ids.trakt == showId }
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        val tmdbShowId = search.show.ids.tmdb ?: return@launch

                        val images = tmdbApi.getImages(tmdbShowId.toInt(), progress.next_episode.season.toInt()).await().body()?.posters
                        if (images.isNullOrEmpty()) return@launch

                        withContext(Dispatchers.Main) {
                            with(mediator.value ?: listOf()) {
                                mediator.value = replaceOrAdd(showProgress.copy(imageUrl = "$IMAGE_PREFIX${images.first().file_path}")) { loadedProgress -> loadedProgress.show.ids.trakt == showId }
                            }
                        }
                    }
                } else {
                    with(mediator.value ?: listOf()) {
                        mediator.value = filterNot { loadedProgress -> loadedProgress.show.ids.trakt == showId }
                    }
                }

                loadingCounter--
                if (loadingCounter <= 0) {
                    refreshingMutable.value = if (mediator.value.isNullOrEmpty()) ListState.EMPTY else ListState.LOADED
                }
            }
        }

        mediator
    }.map { list ->
        list.sortedByDescending { item -> item.sort }
    }.distinctUntilChanged()

    private fun getProgress(showId: Long) = MutableLiveData<WatchedProgress>().apply {
        CoroutineScope(Dispatchers.IO).launch {
            val response = api.getWatchedProgress(showId).await()
            if (response.isSuccessful) {
                postValue(response.body())
            } else {
                searchMutable.postValue(listOf())
                refreshingMutable.postValue(ListState.ERROR)
            }
        }
    }

    fun search(query: String) = CoroutineScope(Dispatchers.IO).launch {
        refreshingMutable.postValue(ListState.LOADING)

        val response = api.search(query).await()
        if (response.isSuccessful) {
            val list = response.body()
            if (list.isNullOrEmpty()) refreshingMutable.postValue(ListState.EMPTY)
            searchMutable.postValue(list ?: listOf())
        } else {
            searchMutable.postValue(listOf())
            refreshingMutable.postValue(ListState.ERROR)
        }
    }

    fun episodeWatched(showId: Long, episodeId: Long) = CoroutineScope(Dispatchers.IO).launch {
        withContext(Dispatchers.Main) {
            with(mediator.value ?: listOf()) {
                mediator.value = map {
                    if (it.show.ids.trakt == showId) it.copy(loading = true) else it
                }
            }
        }
        api.postWatchedItems(WatchedItems(episodes = listOf(Episode(ids = EpisodeIds(trakt = episodeId))))).await()
        progressMutables[showId]?.postValue(api.getWatchedProgress(showId).await().body())
    }
}
