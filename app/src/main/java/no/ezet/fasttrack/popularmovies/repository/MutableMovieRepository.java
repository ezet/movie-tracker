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

    public abstract Call<ApiList<Movie>> createApiCall(ApiService apiService);


    public boolean add(Movie movie) {
        apiService.setFavoriteMovie(movie.getId(), true).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                movie.setType(type);
                AsyncTask.execute(() -> movieCacheDao.insert(movie));
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
        apiService.setFavoriteMovie(movie.getId(), false).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                movie.setType(type);
                AsyncTask.execute(() -> movieCacheDao.delete(movie));
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
