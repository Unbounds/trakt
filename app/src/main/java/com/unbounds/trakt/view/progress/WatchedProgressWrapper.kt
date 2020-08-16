package com.unbounds.trakt.view.progress

import com.unbounds.trakt.service.api.model.trakt.Show
import com.unbounds.trakt.service.api.model.trakt.response.WatchedProgress

/**
 * Created by maclir on 2015-11-22.
 */
data class WatchedProgressWrapper(
        val show: Show,
        var watchedProgress: WatchedProgress? = null,
        var isSelected: Boolean = false,
)
