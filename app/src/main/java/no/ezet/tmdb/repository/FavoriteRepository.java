package no.ezet.tmdb.repository;

import javax.inject.Inject;

import no.ezet.tmdb.api.ApiService;
import no.ezet.tmdb.api.model.ApiList;
import no.ezet.tmdb.api.model.PostResponse;
import no.ezet.tmdb.db.MovieCacheDao;
import no.ezet.tmdb.api.model.Movie;
import no.ezet.tmdb.service.PreferenceService;
import retrofit2.Call;


public class FavoriteRepository extends MutableMovieRepository {

    @Inject
    FavoriteRepository(MovieCacheDao movieCacheDao, ApiService apiService, PreferenceService preferenceService) {
        super(Movie.FAVORITE, movieCacheDao, apiService, preferenceService);
    }

    @Override
    public Call<ApiList<Movie>> createApiCall(ApiService apiService) {
        return apiService.getFavoriteMovies();
    }

    @Override
    protected Call<PostResponse> createAddMovieCall(ApiService apiService, Movie movie) {
        return apiService.setFavoriteMovie(movie.getId(), true);
    }

    @Override
    protected Call<PostResponse> createRemoveMovieCall(ApiService apiService, Movie movie) {
        return apiService.setFavoriteMovie(movie.getId(), false);
    }
}
