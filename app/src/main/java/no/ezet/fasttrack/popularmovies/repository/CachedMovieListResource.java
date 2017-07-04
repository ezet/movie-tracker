package no.ezet.fasttrack.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

import no.ezet.fasttrack.popularmovies.api.ApiService;
import no.ezet.fasttrack.popularmovies.api.model.ApiList;
import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.db.MovieCacheDao;
import no.ezet.fasttrack.popularmovies.network.NetworkResource;
import no.ezet.fasttrack.popularmovies.network.NetworkResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public abstract class CachedMovieListResource extends NetworkResource<List<Movie>, ApiList<Movie>> {

    private final ApiService apiService;
    private final MovieCacheDao movieCacheDao;
    private final int type;
    private boolean shouldFetch = true;

    CachedMovieListResource(ApiService apiService, MovieCacheDao movieCacheDao, int type) {
        this.apiService = apiService;
        this.movieCacheDao = movieCacheDao;
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
    protected boolean shouldFetch(List<Movie> data) {
        if (shouldFetch) {
            shouldFetch = false;
            return true;
        }
        return false;
    }

    @Override
    protected LiveData<List<Movie>> loadFromDb() {
        MediatorLiveData<List<Movie>> liveData = new MediatorLiveData<>();
        AsyncTask.execute(() -> liveData.addSource(movieCacheDao.getByType(type), liveData::setValue));
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
