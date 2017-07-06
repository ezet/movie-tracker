package no.ezet.tmdb.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import no.ezet.tmdb.api.ApiService;
import no.ezet.tmdb.api.model.ApiList;
import no.ezet.tmdb.api.model.Genre;
import no.ezet.tmdb.api.model.Movie;
import no.ezet.tmdb.api.model.PostResponse;
import no.ezet.tmdb.db.MovieCacheDao;
import no.ezet.tmdb.network.Resource;
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

    public LiveData<Resource<PostResponse>> rate(int movieId, double rating) {
        MutableLiveData<Resource<PostResponse>> liveData = new MutableLiveData<>();
        apiService.rate(movieId, rating).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(@NonNull Call<PostResponse> call, @NonNull Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Resource.success(response.body()));
                } else {
                    Timber.w("onResponse: " + response.message());
                    liveData.setValue(Resource.error(response.message(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<PostResponse> call, @NonNull Throwable t) {
                Timber.w("onFailure: ", t);
                liveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return liveData;
    }

    public LiveData<Resource<PostResponse>> deleteRating(int movieId) {
        MutableLiveData<Resource<PostResponse>> liveData = new MutableLiveData<>();
        apiService.deleteRating(movieId).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.postValue(Resource.success(response.body()));
                } else {
                    Timber.w("onResponse: " + response.message());
                    liveData.setValue(Resource.error(response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Timber.w("onFailure: ", t);
                liveData.setValue(Resource.error(t.getMessage(), null));
            }
        });
        return liveData;
    }

    @NonNull
    public LiveData<Resource<Movie>> getMovieDetails(int id) {
        MutableLiveData<Resource<Movie>> liveData = new MutableLiveData<>();
        apiService.getDetailsWithAppend(id, "videos,reviews,images,similar,recommendations").enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    Timber.w("onResponse: " + response.message());
                    liveData.setValue(Resource.error(response.message(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                Timber.w("onFailure: ", t);
                liveData.setValue(Resource.error(t.getMessage(), null));
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
                if (response.isSuccessful() && response.body() != null) {
                    //noinspection ConstantConditions
                    liveData.setValue(Resource.success(response.body().results));
                } else {
                    Timber.w("onResponse: " + response.message());
                    liveData.setValue(Resource.error(response.message(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiList<Genre>> call, @NonNull Throwable t) {
                Timber.w("onFailure: getGenres()", t);
                liveData.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
        return liveData;
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getPopular() {
        return new CachedMovieListResource(apiService, movieCacheDao, Movie.POPULAR) {
            @Override
            protected Call<ApiList<Movie>> createApiCall(ApiService apiService) {
                return apiService.getMovies(QUERY_POPULAR);
            }
        }.getAsLiveData();
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getUpcoming() {
        return new CachedMovieListResource(apiService, movieCacheDao, Movie.UPCOMING) {
            @Override
            protected Call<ApiList<Movie>> createApiCall(ApiService apiService) {
                return apiService.getMovies(QUERY_UPCOMING);
            }
        }.getAsLiveData();
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getTopRated() {
        return new CachedMovieListResource(apiService, movieCacheDao, Movie.TOP_RATED) {
            @Override
            protected Call<ApiList<Movie>> createApiCall(ApiService apiService) {
                return apiService.getMovies(QUERY_TOP_RATED);
            }
        }.getAsLiveData();
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getNowPlaying() {
        return new CachedMovieListResource(apiService, movieCacheDao, Movie.NOW_PLAYING) {
            @Override
            protected Call<ApiList<Movie>> createApiCall(ApiService apiService) {
                return apiService.getMovies(QUERY_NOW_PLAYING);
            }
        }.getAsLiveData();
    }


}
