package com.unbounds.trakt.api.model;

/**
 * Created by maclir on 2015-11-21.
 */
public class Show {
    private final String title;
    private final long year;
    private final Ids ids;

    public Show(String title, long year, Ids ids) {
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

    public static class Ids {
        private final long trakt;
        private final String slug;
        private final long tvdb;
        private final String imdb;
        private final long tmdb;
        private final long tvrage;

        public Ids(long trakt, String slug, long tvdb, String imdb, long tmdb, long tvrage) {
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
