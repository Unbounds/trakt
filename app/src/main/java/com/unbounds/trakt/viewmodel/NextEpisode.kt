package com.unbounds.trakt.viewmodel

data class NextEpisode(
        val lastWatchedAt: String,
        val episodeId: Long,
        val episodeTitle: String,
        val showId: Long,
        val showTitle: String,
        val progress: Int,
        val progressText: String,
        val imageUrl: String?,
        val selected: Boolean,
)
