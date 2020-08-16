package com.unbounds.trakt.viewmodel

import androidx.lifecycle.*
import com.unbounds.trakt.service.repository.ShowRepository
import javax.inject.Inject

class ProgressViewModel @Inject constructor(private val repository: ShowRepository) : ViewModel() {


    private val mediator = MediatorLiveData<List<NextEpisode>>()
    val items: LiveData<List<NextEpisode>> = repository.watchedShows.switchMap { list ->
        mediator.value = listOf()

        var loadingCounter = 0

        list.forEach { watchedShow ->
            loadingCounter++
            mediator.addSource(repository.getProgress(watchedShow.show.ids.trakt)) { progress ->
                val nextEpisode = progress.next_episode
                if (nextEpisode != null) {
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
                            selected = false,
                    )
                    mediator.value = loadedList.filterNot { oldProgress -> oldProgress.showId == episode.showId }.plus(episode)
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
                        lastWatchedAt = episode.lastWatchedAt,
                        episodeId = episode.episodeId,
                        episodeTitle = episode.episodeTitle,
                        showId = episode.showId,
                        showTitle = episode.showTitle,
                        progress = episode.progress,
                        progressText = episode.progressText,
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
        val selected: Boolean,
)
