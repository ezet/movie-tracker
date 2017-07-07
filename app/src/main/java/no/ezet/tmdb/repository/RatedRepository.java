package no.ezet.tmdb.repository;

import javax.inject.Inject;

import no.ezet.tmdb.api.ApiService;
import no.ezet.tmdb.api.model.ApiList;
import no.ezet.tmdb.api.model.Movie;
import no.ezet.tmdb.api.model.PostResponse;
import no.ezet.tmdb.db.MovieCacheDao;
import no.ezet.tmdb.service.PreferenceService;
import retrofit2.Call;

public class RatedRepository extends MutableMovieRepository {

    @Inject
    RatedRepository(MovieCacheDao movieCacheDao, ApiService apiService, PreferenceService preferenceService) {
        super(Movie.RATED, movieCacheDao, apiService, preferenceService);
    }

    @Override
    protected Call<ApiList<Movie>> createApiCall(ApiService apiService) {
        return apiService.getRatedMovies();
    }

    @Override
    protected Call<PostResponse> createAddMovieCall(ApiService apiService, Movie movie) {
        return apiService.rate(movie.getId(), movie.getRating());
    }

    @Override
    protected Call<PostResponse> createRemoveMovieCall(ApiService apiService, Movie movie) {
        return apiService.deleteRating(movie.getId());
    }
}
