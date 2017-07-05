package no.ezet.tmdb.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import no.ezet.tmdb.api.ApiService;
import no.ezet.tmdb.api.model.ApiList;
import no.ezet.tmdb.api.model.Movie;
import no.ezet.tmdb.db.MovieCacheDao;
import no.ezet.tmdb.network.NetworkResource;
import no.ezet.tmdb.network.NetworkResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public abstract class CachedMovieResource extends NetworkResource<Movie, ApiList<Movie>> {

    private final ApiService apiService;
    private final MovieCacheDao movieCacheDao;
    private final int id;
    private final int type;
    private boolean shouldFetch = true;


    CachedMovieResource(ApiService apiService, MovieCacheDao movieCacheDao, int id, int type) {
        this.apiService = apiService;
        this.movieCacheDao = movieCacheDao;
        this.id = id;
        this.type = type;
    }

    @Override
    protected void saveCallResult(ApiList<Movie> item) {
        movieCacheDao.deleteByType(type);
        for (Movie movie : item.results) {
            movie.setType(type);
        }
        movieCacheDao.insert(item.results);
    }

    @Override
    protected boolean shouldFetch(Movie data) {
        if (shouldFetch) {
            shouldFetch = false;
            return true;
        }
        return false;
    }

    @Override
    protected LiveData<Movie> loadFromDb() {
        MediatorLiveData<Movie> liveData = new MediatorLiveData<>();
        AsyncTask.execute(() -> liveData.addSource(movieCacheDao.getById(id, type), liveData::postValue));
        return liveData;
    }

    @Override
    protected LiveData<NetworkResponse<ApiList<Movie>>> createCall() {
        MutableLiveData<NetworkResponse<ApiList<Movie>>> networkResponse = new MutableLiveData<>();
        createApiCall(apiService).enqueue(new Callback<ApiList<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<ApiList<Movie>> call, @NonNull Response<ApiList<Movie>> response) {
                if (response.isSuccessful()) {
                    networkResponse.setValue(new NetworkResponse<>(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiList<Movie>> call, @NonNull Throwable t) {
                Timber.w("onFailure: ", t);
            }
        });
        return networkResponse;
    }

    abstract protected Call<ApiList<Movie>> createApiCall(ApiService apiService);
}
