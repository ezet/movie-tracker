package no.ezet.fasttrack.popularmovies.repository;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.api.ApiService;
import no.ezet.fasttrack.popularmovies.api.model.ApiList;
import no.ezet.fasttrack.popularmovies.db.MovieCacheDao;
import no.ezet.fasttrack.popularmovies.api.model.Movie;
import retrofit2.Call;

public class WatchListRepository extends MutableMovieRepository {

    @Inject
    WatchListRepository(MovieCacheDao movieCacheDao, ApiService apiService) {
        super(Movie.WATCHLIST, movieCacheDao, apiService);
    }

    @Override
    public Call<ApiList<Movie>> createApiCall(ApiService apiService) {
        return apiService.getWatchlist();
    }
}