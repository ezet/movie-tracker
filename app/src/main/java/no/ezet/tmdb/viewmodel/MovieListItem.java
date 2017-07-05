package no.ezet.tmdb.viewmodel;

import no.ezet.tmdb.api.model.Movie;

public class MovieListItem {

    public final int id;

    public final String posterPath;

    private MovieListItem(int id, String posterPath) {
        this.id = id;
        this.posterPath = posterPath;
    }

    public static MovieListItem create(Movie movie) {
        return new MovieListItem(movie.getId(), movie.getPosterPath());
    }
}
