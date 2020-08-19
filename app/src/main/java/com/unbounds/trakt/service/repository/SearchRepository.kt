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

    private val refreshingMutable = MutableLiveData<Boolean>()
    val refreshing: LiveData<Boolean> = refreshingMutable

    private val progressMutables = HashMap<Long, MutableLiveData<WatchedProgress>>()
    private val mediator = MediatorLiveData<List<ShowProgress>>()
    private val searchMutable = MutableLiveData<List<Search>>()
    val shows = searchMutable.map { list ->
        list.map { search ->
            search.show
        }
    }.switchMap { list ->
        progressMutables.clear()
        mediator.value = listOf()
        var loadingCounter = 0

        refreshingMutable.value = list.isNotEmpty()
        list.forEach { show ->
            loadingCounter++
            val showId = show.ids.trakt
            val progressMutable = getProgress(showId)
            progressMutables[showId] = progressMutable

            mediator.addSource(progressMutable) { progress ->
                if (progress.next_episode != null && !progress.isCompleted) {
                    with(mediator.value ?: listOf()) {
                        mediator.value = filterNot { loadedProgress -> loadedProgress.show.ids.trakt == showId }.plus(ShowProgress(show, progress, progress.next_episode))
                    }

                    CoroutineScope(Dispatchers.IO).launch {
                        val tmdbShowId = show.ids.tmdb ?: return@launch

                        val images = tmdbApi.getImages(tmdbShowId.toInt(), progress.next_episode.season.toInt()).await().body()?.posters
                        if (images.isNullOrEmpty()) return@launch

                        withContext(Dispatchers.Main) {
                            with(mediator.value ?: listOf()) {
                                mediator.value = filterNot { loadedProgress -> loadedProgress.show.ids.trakt == showId }.plus(ShowProgress(show, progress, progress.next_episode, "$IMAGE_PREFIX${images.first().file_path}"))
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
                    refreshingMutable.value = false
                }
            }
        }

        mediator
    }.map { list ->
        list.sortedByDescending { item -> item.progress.last_watched_at }
    }.distinctUntilChanged()

    private fun getProgress(showId: Long) = MutableLiveData<WatchedProgress>().apply {
        CoroutineScope(Dispatchers.IO).launch {
            postValue(api.getWatchedProgress(showId).await().body())
        }
    }

    fun search(query: String) = CoroutineScope(Dispatchers.IO).launch {
        refreshingMutable.postValue(true)
        searchMutable.postValue(listOf())
        searchMutable.postValue(api.search(query).await().body() ?: listOf())
    }

    fun episodeWatched(showId: Long, episodeId: Long) = CoroutineScope(Dispatchers.IO).launch {
        withContext(Dispatchers.Main) {
            with(mediator.value ?: listOf()) {
                val oldValue = find { loadedProgress -> loadedProgress.show.ids.trakt == showId }
                        ?: return@with
                mediator.value = minus(oldValue).plus(
                        ShowProgress(oldValue.show, oldValue.progress, oldValue.nextEpisode, oldValue.imageUrl, true)
                )
            }
        }
        api.postWatchedItems(WatchedItems(episodes = listOf(Episode(ids = EpisodeIds(trakt = episodeId))))).await()
        progressMutables[showId]?.postValue(api.getWatchedProgress(showId).await().body())
    }
}
