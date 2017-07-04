package no.ezet.fasttrack.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

import no.ezet.fasttrack.popularmovies.api.ApiService;
import no.ezet.fasttrack.popularmovies.api.model.ApiList;
import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.api.model.PostResponse;
import no.ezet.fasttrack.popularmovies.db.MovieCacheDao;
import no.ezet.fasttrack.popularmovies.network.Resource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public abstract class MutableMovieRepository {
    protected final int type;
    private final ApiService apiService;
    private MovieCacheDao movieCacheDao;

    MutableMovieRepository(int type, MovieCacheDao movieCacheDao, ApiService apiService) {
        this.type = type;
        this.movieCacheDao = movieCacheDao;
        this.apiService = apiService;
    }

    public LiveData<Resource<List<Movie>>> getAll() {
        return new CachedMovieResource(apiService, movieCacheDao, type) {
            @Override
            protected Call<ApiList<Movie>> createApiCall(ApiService apiService) {
                return MutableMovieRepository.this.createApiCall(apiService);
            }
        }.getAsLiveData();
    }

    protected abstract Call<ApiList<Movie>> createApiCall(ApiService apiService);

    protected abstract Call<PostResponse> createAddMovieCall(ApiService apiService, Movie movie);

    protected abstract Call<PostResponse> createRemoveMovieCall(ApiService apiService, Movie movie);


    public boolean add(Movie movie) {
        createAddMovieCall(apiService, movie).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    movie.setType(type);
                    AsyncTask.execute(() -> movieCacheDao.insert(movie));
                } else {
                    Timber.d("onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                Timber.w("onFailure: ", t);
            }
        });
        return true;
    }

    @NonNull
    public LiveData<Resource<Movie>> getById(int id) {
        return Transformations.map(movieCacheDao.getById(id, type), Resource::success);
    }

    public void remove(Movie movie) {
        createRemoveMovieCall(apiService, movie).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    movie.setType(type);
                    AsyncTask.execute(() -> movieCacheDao.delete(movie));
                } else {
                    Timber.d("onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                Timber.w("onFailure: ", t);
            }
        });
    }

//    @SuppressWarnings("unused")
//    private boolean isOnline() {
//        ConnectivityManager cm =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnectedOrConnecting();
//    }
}
