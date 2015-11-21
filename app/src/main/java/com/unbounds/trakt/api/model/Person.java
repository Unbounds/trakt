package com.unbounds.trakt.api.model;

/**
 * Created by maclir on 2015-11-21.
 */
public final class Person {
    private final String name;
    private final Ids ids;

    public Person(String name, Ids ids) {
        this.name = name;
        this.ids = ids;
    }

    public Ids getIds() {
        return ids;
    }

    public String getName() {
        return name;
    }

    public static final class Ids {
        private final long trakt;
        private final String slug;
        private final String imdb;
        private final long tmdb;
        private final long tvrage;

        public Ids(long trakt, String slug, String imdb, long tmdb, long tvrage) {
            this.trakt = trakt;
            this.slug = slug;
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
