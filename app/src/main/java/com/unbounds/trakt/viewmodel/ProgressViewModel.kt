package com.unbounds.trakt.viewmodel

import androidx.lifecycle.*
import com.unbounds.trakt.service.api.IMAGE_PREFIX
import com.unbounds.trakt.service.repository.ShowRepository
import com.unbounds.trakt.service.repository.TmdbRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProgressViewModel @Inject constructor(private val repository: ShowRepository, private val tmdbRepository: TmdbRepository) : ViewModel() {


    private val mediator = MediatorLiveData<List<NextEpisode>>()
    val items: LiveData<List<NextEpisode>> = repository.watchedShows.switchMap { list ->
        mediator.value = listOf()

        var loadingCounter = 0

        list.forEach { watchedShow ->
            loadingCounter++
            mediator.addSource(repository.getProgress(watchedShow.show.ids.trakt)) { progress ->
                val nextEpisode = progress.next_episode
                if (nextEpisode != null && !progress.isCompleted) {
                    val loadedList = mediator.value ?: listOf()
                    val progressPercentage = progress.completed * 100 / progress.aired
                    val episode = NextEpisode(
                            lastWatchedAt = progress.last_watched_at,
                            episodeId = nextEpisode.ids.trakt,
                            episodeTitle = String.format("S%02dE%02d: %s",
                                    nextEpisode.season,
                                    nextEpisode.number,
                                    nextEpisode.title),
                            showId = watchedShow.show.ids.trakt,
                            showTitle = watchedShow.show.title,
                            progress = progressPercentage.toInt(),
                            progressText = String.format("%d/%d (%d%%)", progress.completed, progress.aired, progressPercentage),
                    )
                    mediator.value = loadedList.filterNot { oldProgress -> oldProgress.showId == episode.showId }.plus(episode)

                    CoroutineScope(Dispatchers.IO).launch {
                        val showId = watchedShow.show.ids.tmdb ?: return@launch
                        val images = tmdbRepository.getImages(showId.toInt(), nextEpisode.season.toInt(), nextEpisode.number.toInt())
                        if (images.isNullOrEmpty()) return@launch

                        withContext(Dispatchers.Main) {
                            val newList = mediator.value ?: listOf()
                            mediator.value = newList.filterNot { oldProgress -> oldProgress.showId == episode.showId }.plus(NextEpisode(episode, "$IMAGE_PREFIX${images.first().file_path}"))
                        }
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
        list.sortedByDescending { item -> item.lastWatchedAt }
    }

    private val refreshingMutable = MutableLiveData<Boolean>()
    val refreshing: LiveData<Boolean> = refreshingMutable

    fun episodeWatched(episode: NextEpisode) {
        val loadedList = mediator.value ?: listOf()
        mediator.value = loadedList.filterNot { oldProgress -> oldProgress.showId == episode.showId }.plus(
                NextEpisode(
                        episode,
                        selected = true,
                )
        )
        repository.episodeWatched(episode.showId, episode.episodeId)
    }

    fun reload() {
        repository.reload()
        refreshingMutable.value = true
    }
}

data class NextEpisode(
        val lastWatchedAt: String,
        val episodeId: Long,
        val episodeTitle: String,
        val showId: Long,
        val showTitle: String,
        val progress: Int,
        val progressText: String,
        val imageUrl: String? = null,
        val selected: Boolean = false,
) {
    constructor(episode: NextEpisode, selected: Boolean) : this(episode.lastWatchedAt, episode.episodeId, episode.episodeTitle, episode.showId, episode.showTitle, episode.progress, episode.progressText, episode.imageUrl, selected)
    constructor(episode: NextEpisode, imageUrl: String) : this(episode.lastWatchedAt, episode.episodeId, episode.episodeTitle, episode.showId, episode.showTitle, episode.progress, episode.progressText, imageUrl, episode.selected)
}
