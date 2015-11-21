package com.unbounds.trakt.api.model.response;

import com.unbounds.trakt.api.model.Episode;
import com.unbounds.trakt.api.model.Season;
import com.unbounds.trakt.api.model.Show;

/**
 * Created by maclir on 2015-11-21.
 */
public final class WatchedProgress {
    private final long aired;
    private final long completed;
    private final String lastWatchedAt;
    private final ExtendedSeason seasons[];
    private final Season hiddenSeasons[];
    private final Episode nextEpisode;
    private Show show;

    private WatchedProgress(final long aired, final long completed, final String lastWatchedAt, final ExtendedSeason[] seasons, final Season[] hiddenSeasons, final Episode nextEpisode) {
        this.aired = aired;
        this.completed = completed;
        this.lastWatchedAt = lastWatchedAt;
        this.seasons = seasons;
        this.hiddenSeasons = hiddenSeasons;
        this.nextEpisode = nextEpisode;
    }

    public long getAired() {
        return aired;
    }

    public long getCompleted() {
        return completed;
    }

    public String getLastWatchedAt() {
        return lastWatchedAt;
    }

    public ExtendedSeason[] getSeasons() {
        return seasons;
    }

    public Season[] getHiddenSeasons() {
        return hiddenSeasons;
    }

    public Episode getNextEpisode() {
        return nextEpisode;
    }

    public boolean isCompleted() {
        return aired == completed;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }
}