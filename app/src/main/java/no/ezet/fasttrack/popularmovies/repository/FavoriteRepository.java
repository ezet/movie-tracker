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
import no.ezet.fasttrack.popularmovies.db.FavoriteDao;
import no.ezet.fasttrack.popularmovies.model.ApiList;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.network.NetworkResource;
import no.ezet.fasttrack.popularmovies.network.NetworkResponse;
import no.ezet.fasttrack.popularmovies.network.Resource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class FavoriteRepository {

    private final ApiService apiService;
    private FavoriteDao favoriteDao;

    @Inject
    public FavoriteRepository(FavoriteDao favoriteDao, ApiService apiService) {
        this.favoriteDao = favoriteDao;
        this.apiService = apiService;
    }

//    @NonNull
//    public LiveData<Resource<List<Favorite>>> getAll() {
//        return Transformations.map(favoriteDao.getFavorites(), Resource::success);
//    }

    public LiveData<Resource<List<Favorite>>> getAll() {
        return new FavoriteListResource(apiService, favoriteDao).getAsLiveData();
    }

    public boolean add(Movie movie) {
        apiService.setFavoriteMovie(movie.getId(), true).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                AsyncTask.execute(() -> favoriteDao.insert(new Favorite(movie.getId(), movie.getPosterPath())));
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {

            }
        });
        return true;
    }

    @NonNull
    public LiveData<Resource<Favorite>> getById(int id) {
        return Transformations.map(favoriteDao.getById(id), Resource::success);
    }

    public void remove(Movie movie) {
        apiService.setFavoriteMovie(movie.getId(), false).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                AsyncTask.execute(() -> favoriteDao.delete(new Favorite(movie.getId(), null)));
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {

            }
        });
    }

    private static class FavoriteListResource extends NetworkResource<List<Favorite>, ApiList<Movie>> {

        private final ApiService apiService;
        private final FavoriteDao favoriteDao;

        private FavoriteListResource(ApiService apiService, FavoriteDao favoriteDao) {
            this.apiService = apiService;
            this.favoriteDao = favoriteDao;
        }

        @Override
        protected void saveCallResult(ApiList<Movie> item) {
            List<Favorite> favorites = new ArrayList<>();
            for (Movie movie : item.results) {
                favorites.add(new Favorite(movie.getId(), movie.getPosterPath()));
            }
            favoriteDao.insert(favorites);
        }

        @Override
        protected boolean shouldFetch(List<Favorite> data) {
            return true;
        }

        @Override
        protected LiveData<List<Favorite>> loadFromDb() {
            MediatorLiveData<List<Favorite>> liveData = new MediatorLiveData<>();
            AsyncTask.execute(() -> liveData.addSource(favoriteDao.getFavorites(), liveData::setValue));
            return liveData;
        }

        @Override
        protected LiveData<NetworkResponse<ApiList<Movie>>> createCall() {
            MutableLiveData<NetworkResponse<ApiList<Movie>>> networkResponse = new MutableLiveData<>();
            apiService.getFavoriteMovies().enqueue(new Callback<ApiList<Movie>>() {
                @Override
                public void onResponse(Call<ApiList<Movie>> call, Response<ApiList<Movie>> response) {
                    Timber.d("onResponse: getAll");
                    if (response.isSuccessful()) {
                        List<Favorite> list = new ArrayList<>();
                        for (Movie movie : response.body().results) {
                            list.add(new Favorite(movie.getId(), movie.getPosterPath()));
                        }
                        networkResponse.setValue(new NetworkResponse<>(response));
                    }
                }

                @Override
                public void onFailure(Call<ApiList<Movie>> call, Throwable t) {
                    Timber.d("onFailure: getAll");
                }
            });
            return networkResponse;
        }
    }
}
