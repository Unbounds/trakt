package com.unbounds.trakt.api.model.response;

import com.unbounds.trakt.api.model.Show;

/**
 * Created by Evelina Vorobyeva on 01/12/15.
 */
public final class TrendingShow {

    private final long watchers;
    private final Show show;

    private TrendingShow(final long watchers, final Show show) {
        this.watchers = watchers;
        this.show = show;
    }

    public long getWatchers() {
        return watchers;
    }

    public Show getShow() {
        return show;
    }
}
