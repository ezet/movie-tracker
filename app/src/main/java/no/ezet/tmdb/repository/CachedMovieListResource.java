package no.ezet.tmdb.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

import no.ezet.tmdb.api.ApiService;
import no.ezet.tmdb.api.model.ApiList;
import no.ezet.tmdb.api.model.Movie;
import no.ezet.tmdb.db.MovieCacheDao;
import no.ezet.tmdb.network.NetworkResource;
import no.ezet.tmdb.network.NetworkResponse;
import no.ezet.tmdb.service.PreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public abstract class CachedMovieListResource extends NetworkResource<List<Movie>, ApiList<Movie>> {

    private static final int CACHE_DURATION_IN_MINUTES = 1000 * 60 * 120;
    private static final String KEY_PREFIX = "KEY_UPDATED_";
    private final String KEY;
    private final ApiService apiService;
    private final MovieCacheDao movieCacheDao;
    private final int type;
    private final PreferenceService preferenceService;


    CachedMovieListResource(ApiService apiService, MovieCacheDao movieCacheDao, int type, PreferenceService preferenceService) {
        this.apiService = apiService;
        this.movieCacheDao = movieCacheDao;
        this.type = type;
        this.preferenceService = preferenceService;
        this.KEY = KEY_PREFIX + type;
    }

    @Override
    protected void saveCallResult(ApiList<Movie> item) {
        movieCacheDao.deleteByType(type);
        for (Movie movie : item.results) {
            movie.setType(type);
        }
        movieCacheDao.insert(item.results);
        preferenceService.putLong(KEY_PREFIX + type, System.currentTimeMillis());
    }

    @Override
    protected boolean shouldFetch(List<Movie> data) {
        if (cacheIsInvalid())
            return true;
        else
            return false;
    }

    private boolean cacheIsInvalid() {
        long lastUpdate = preferenceService.getLong(KEY);
        return lastUpdate == 0 || System.currentTimeMillis() > lastUpdate + CACHE_DURATION_IN_MINUTES;
    }

    @Override
    protected LiveData<List<Movie>> loadFromDb() {
        Timber.d("loadFromDb: ");
        MediatorLiveData<List<Movie>> liveData = new MediatorLiveData<>();
        AsyncTask.execute(() -> liveData.addSource(movieCacheDao.getByType(type), liveData::postValue));
        return liveData;
    }

    @Override
    protected LiveData<NetworkResponse<ApiList<Movie>>> createCall() {
        Timber.d("createCall: ");
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
