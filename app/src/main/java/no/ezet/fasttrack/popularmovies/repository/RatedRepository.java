package no.ezet.fasttrack.popularmovies.repository;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.api.ApiService;
import no.ezet.fasttrack.popularmovies.api.model.ApiList;
import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.api.model.PostResponse;
import no.ezet.fasttrack.popularmovies.db.MovieCacheDao;
import retrofit2.Call;

public class RatedRepository extends MutableMovieRepository {

    @Inject
    RatedRepository(MovieCacheDao movieCacheDao, ApiService apiService) {
        super(Movie.RATED, movieCacheDao, apiService);
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
