package com.unbounds.trakt.api.model.response;

/**
 * Created by maclir on 2015-11-17.
 */
public final class WatchedShow {
    public final long plays;
    public final String last_watched_at;
    public final Show show;
    public final Season seasons[];

    private WatchedShow(long plays, String last_watched_at, Show show, Season[] seasons) {
        this.plays = plays;
        this.last_watched_at = last_watched_at;
        this.show = show;
        this.seasons = seasons;
    }

    public long getPlays() {
        return plays;
    }

    public String getLast_watched_at() {
        return last_watched_at;
    }

    public Show getShow() {
        return show;
    }

    public Season[] getSeasons() {
        return seasons;
    }

    public static final class Show {
        public final String title;
        public final long year;
        public final Ids ids;

        private Show(String title, long year, Ids ids) {
            this.title = title;
            this.year = year;
            this.ids = ids;
        }

        public String getTitle() {
            return title;
        }

        public long getYear() {
            return year;
        }

        public Ids getIds() {
            return ids;
        }

        public static final class Ids {
            public final long trakt;
            public final String slug;
            public final long tvdb;
            public final String imdb;
            public final long tmdb;
            public final long tvrage;

            private Ids(long trakt, String slug, long tvdb, String imdb, long tmdb, long tvrage) {
                this.trakt = trakt;
                this.slug = slug;
                this.tvdb = tvdb;
                this.imdb = imdb;
                this.tmdb = tmdb;
                this.tvrage = tvrage;
            }

            public long getTrakt() {
                return trakt;
            }

            public String getSlug() {
                return slug;
            }

            public long getTvdb() {
                return tvdb;
            }

            public String getImdb() {
                return imdb;
            }

            public long getTmdb() {
                return tmdb;
            }

            public long getTvrage() {
                return tvrage;
            }
        }
    }

    public static final class Season {
        public final long number;
        public final Episode episodes[];

        private Season(long number, Episode[] episodes) {
            this.number = number;
            this.episodes = episodes;
        }

        public long getNumber() {
            return number;
        }

        public Episode[] getEpisodes() {
            return episodes;
        }

        public static final class Episode {
            public final long number;
            public final long plays;
            public final String last_watched_at;

            private Episode(long number, long plays, String last_watched_at) {
                this.number = number;
                this.plays = plays;
                this.last_watched_at = last_watched_at;
            }

            public long getNumber() {
                return number;
            }

            public long getPlays() {
                return plays;
            }

            public String getLast_watched_at() {
                return last_watched_at;
            }
        }
    }
}
