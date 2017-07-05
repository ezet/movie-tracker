package no.ezet.tmdb.repository;

import javax.inject.Inject;

import no.ezet.tmdb.api.ApiService;
import no.ezet.tmdb.api.model.ApiList;
import no.ezet.tmdb.api.model.PostResponse;
import no.ezet.tmdb.db.MovieCacheDao;
import no.ezet.tmdb.api.model.Movie;
import retrofit2.Call;

public class WatchlistRepository extends MutableMovieRepository {

    @Inject
    WatchlistRepository(MovieCacheDao movieCacheDao, ApiService apiService) {
        super(Movie.WATCHLIST, movieCacheDao, apiService);
    }

    @Override
    public Call<ApiList<Movie>> createApiCall(ApiService apiService) {
        return apiService.getWatchlist();
    }

    @Override
    protected Call<PostResponse> createAddMovieCall(ApiService apiService, Movie movie) {
        return apiService.setWatchlist(movie.getId(), true);
    }

    @Override
    protected Call<PostResponse> createRemoveMovieCall(ApiService apiService, Movie movie) {
        return apiService.setWatchlist(movie.getId(), false);
    }
}