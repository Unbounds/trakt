package com.unbounds.trakt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.unbounds.trakt.service.repository.ShowRepository
import com.unbounds.trakt.utils.ListState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressViewModel @Inject constructor(private val repository: ShowRepository) : ViewModel() {

    val refreshing: LiveData<ListState> = repository.refreshing

    val items: LiveData<List<NextEpisode>> = repository.watchedShows.map { list ->
        list.map { item ->
            val progressPercentage = item.progress.completed * 100 / item.progress.aired
            NextEpisode(
                    lastWatchedAt = item.progress.last_watched_at ?: "",
                    episodeId = item.nextEpisode.ids.trakt,
                    episodeTitle = String.format("S%02dE%02d: %s",
                            item.nextEpisode.season,
                            item.nextEpisode.number,
                            item.nextEpisode.title),
                    showId = item.show.ids.trakt,
                    showTitle = item.show.title,
                    imageUrl = item.imageUrl,
                    progress = progressPercentage.toInt(),
                    progressText = String.format("%d/%d (%d%%)", item.progress.completed, item.progress.aired, progressPercentage),
                    selected = item.loading,
            )
        }
    }

    fun episodeWatched(episode: NextEpisode) = repository.episodeWatched(episode.showId, episode.episodeId)

    fun reload() = repository.reload()
}

