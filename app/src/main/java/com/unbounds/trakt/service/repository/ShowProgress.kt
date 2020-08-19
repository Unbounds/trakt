package com.unbounds.trakt.service.repository

import com.unbounds.trakt.service.api.model.trakt.Episode
import com.unbounds.trakt.service.api.model.trakt.Show
import com.unbounds.trakt.service.api.model.trakt.response.WatchedProgress

data class ShowProgress(
        val show: Show,
        val progress: WatchedProgress,
        val nextEpisode: Episode,
        val imageUrl: String? = null,
        val loading: Boolean = false,
)
