package com.unbounds.trakt.progress;

import com.unbounds.trakt.api.model.Show;
import com.unbounds.trakt.api.model.response.WatchedProgress;

/**
 * Created by maclir on 2015-11-22.
 */
class WatchedProgressWrapper {
    private final Show mShow;
    private WatchedProgress mWatchedProgress;
    private boolean selected;

    WatchedProgressWrapper(final WatchedProgress watchedProgress, final Show show) {
        mWatchedProgress = watchedProgress;
        mShow = show;
    }

    Show getShow() {
        return mShow;
    }

    WatchedProgress getWatchedProgress() {
        return mWatchedProgress;
    }

    boolean isSelected() {
        return selected;
    }

    WatchedProgressWrapper setWatchedProgress(final WatchedProgress watchedProgress) {
        mWatchedProgress = watchedProgress;
        return this;
    }

    void setSelected(final boolean selected) {
        this.selected = selected;
    }
}
