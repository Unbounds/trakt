package com.unbounds.trakt.search;

import com.unbounds.trakt.api.model.Show;

/**
 * Created by Eff on 12/8/15.
 */
public class ShowWrapper {

    private final Long mWatchers;
    private final Show mShow;

    ShowWrapper(final Long watchers, final Show show) {
        mWatchers = watchers;
        mShow = show;
    }

    public ShowWrapper(final Show show) {
        mWatchers = null;
        mShow = show;
    }

    public long getWatchers() {
        return mWatchers;
    }

    public Show getShow() {
        return mShow;
    }

    public boolean hasWatchers() {
        return mWatchers == null ? false : true;
    }
}
