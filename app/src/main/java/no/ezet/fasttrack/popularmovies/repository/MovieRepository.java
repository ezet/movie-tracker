package no.ezet.fasttrack.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.api.ApiService;
import no.ezet.fasttrack.popularmovies.db.MovieCacheDao;
import no.ezet.fasttrack.popularmovies.api.model.ApiList;
import no.ezet.fasttrack.popularmovies.api.model.Genre;
import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MovieRepository {

    private static final String QUERY_POPULAR = "popular";
    private static final String QUERY_UPCOMING = "upcoming";
    private static final String QUERY_TOP_RATED = "top_rated";
    private static final String QUERY_NOW_PLAYING = "now_playing";
    private final ApiService apiService;
    private final MovieCacheDao movieCacheDao;

    @Inject
    MovieRepository(ApiService apiService, MovieCacheDao movieCacheDao) {
        this.apiService = apiService;
        this.movieCacheDao = movieCacheDao;
    }

    @NonNull
    public LiveData<Resource<Movie>> getMovieDetails(int id) {
        MutableLiveData<Resource<Movie>> liveData = new MutableLiveData<>();
        apiService.getDetailsWithAppend(id, "videos,reviews,images").enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                Timber.d("onResponse: getMovieDetails()");
                if (response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else if (response.errorBody() != null) {
                    //noinspection ConstantConditions
                    liveData.setValue(Resource.error(response.errorBody().toString(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error(t.getMessage(), null));
                Timber.d("onFailure: ");
            }
        });
        return liveData;
    }

    @NonNull
    public LiveData<Resource<List<Genre>>> getGenres() {
        MutableLiveData<Resource<List<Genre>>> liveData = new MutableLiveData<>();
        apiService.getGenres().enqueue(new Callback<ApiList<Genre>>() {
            @Override
            public void onResponse(@NonNull Call<ApiList<Genre>> call, @NonNull Response<ApiList<Genre>> response) {
                Timber.d("onResponse: getGenres()");
                if (response.body() != null) {
                    //noinspection ConstantConditions
                    liveData.setValue(Resource.success(response.body().results));
                } else if (response.errorBody() != null) {
                    //noinspection ConstantConditions
                    liveData.setValue(Resource.error(response.errorBody().toString(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiList<Genre>> call, @NonNull Throwable t) {
                Timber.d("onFailure: getGenres()");
                liveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return liveData;
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getPopular() {
        return new CachedMovieResource(apiService, movieCacheDao, Movie.POPULAR) {
            @Override
            protected Call<ApiList<Movie>> createApiCall(ApiService apiService) {
                return apiService.getMovies(QUERY_POPULAR);
            }
        }.getAsLiveData();
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getUpcoming() {
        return new CachedMovieResource(apiService, movieCacheDao, Movie.UPCOMING) {
            @Override
            protected Call<ApiList<Movie>> createApiCall(ApiService apiService) {
                return apiService.getMovies(QUERY_UPCOMING);
            }
        }.getAsLiveData();
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getTopRated() {
        return new CachedMovieResource(apiService, movieCacheDao, Movie.TOP_RATED) {
            @Override
            protected Call<ApiList<Movie>> createApiCall(ApiService apiService) {
                return apiService.getMovies(QUERY_TOP_RATED);
            }
        }.getAsLiveData();
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getNowPlaying() {
        return new CachedMovieResource(apiService, movieCacheDao, Movie.NOW_PLAYING) {
            @Override
            protected Call<ApiList<Movie>> createApiCall(ApiService apiService) {
                return apiService.getMovies(QUERY_NOW_PLAYING);
            }
        }.getAsLiveData();
    }

}
