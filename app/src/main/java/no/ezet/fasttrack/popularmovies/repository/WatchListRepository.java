package no.ezet.fasttrack.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.api.ApiService;
import no.ezet.fasttrack.popularmovies.api.model.PostResponse;
import no.ezet.fasttrack.popularmovies.db.Favorite;
import no.ezet.fasttrack.popularmovies.db.MovieCacheDao;
import no.ezet.fasttrack.popularmovies.model.ApiList;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.network.NetworkResource;
import no.ezet.fasttrack.popularmovies.network.NetworkResponse;
import no.ezet.fasttrack.popularmovies.network.Resource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class WatchListRepository {


    private final ApiService apiService;
    private MovieCacheDao movieCacheDao;
    private static final int TYPE = Movie.WATCHLIST;

    @Inject
    public WatchListRepository(MovieCacheDao movieCacheDao, ApiService apiService) {
        this.movieCacheDao = movieCacheDao;
        this.apiService = apiService;
    }

    public LiveData<Resource<List<Movie>>> getAll() {
        return new CachedMovieResource(apiService, movieCacheDao, TYPE) {
            @Override
            protected Call<ApiList<Movie>> createApiCall(ApiService apiService) {
                return apiService.getWatchlist();
            }
        }.getAsLiveData();
    }

    public boolean add(Movie movie) {

        apiService.setFavoriteMovie(movie.getId(), true).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                movie.setType(TYPE);
                AsyncTask.execute(() -> movieCacheDao.insert(movie));
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {

            }
        });
        return true;
    }

    @NonNull
    public LiveData<Resource<Movie>> getById(int id) {
        return Transformations.map(movieCacheDao.getById(id, TYPE), Resource::success);
    }

    public void remove(Movie movie) {
        apiService.setFavoriteMovie(movie.getId(), false).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                AsyncTask.execute(() -> movieCacheDao.delete(movie));
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {

            }
        });
    }

}