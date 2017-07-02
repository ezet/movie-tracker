package no.ezet.fasttrack.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.api.ApiService;
import no.ezet.fasttrack.popularmovies.db.Favorite;
import no.ezet.fasttrack.popularmovies.db.FavoriteDao;
import no.ezet.fasttrack.popularmovies.model.ApiList;
import no.ezet.fasttrack.popularmovies.model.Movie;
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
        MutableLiveData<Resource<List<Favorite>>> liveData = new MutableLiveData<>();
        apiService.getFavoriteMovies().enqueue(new Callback<ApiList<Movie>>() {
            @Override
            public void onResponse(Call<ApiList<Movie>> call, Response<ApiList<Movie>> response) {
                Timber.d("onResponse: getAll");
                if (response.isSuccessful()) {
                    List<Favorite> list = new ArrayList<>();
                    for (Movie movie : response.body().results) {
                        list.add(new Favorite(movie.getId(), movie.getPosterPath()));
                    }
                    liveData.setValue(Resource.success(list));
                }
            }

            @Override
            public void onFailure(Call<ApiList<Movie>> call, Throwable t) {
                Timber.d("onFailure: getAll");
            }
        });
        return liveData;
    }

    public boolean add(Movie movie) {
        AsyncTask.execute(() -> favoriteDao.insert(new Favorite(movie.getId(), movie.getPosterPath())));
        return true;
    }

    @NonNull
    public LiveData<Resource<Favorite>> getById(int id) {
        return Transformations.map(favoriteDao.getById(id), Resource::success);
    }

    public void remove(Movie value) {
        AsyncTask.execute(() -> favoriteDao.delete(new Favorite(value.getId(), null)));
    }
}
