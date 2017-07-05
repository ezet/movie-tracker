package no.ezet.tmdb.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import no.ezet.tmdb.api.model.Movie;
import no.ezet.tmdb.network.Resource;


public abstract class MovieListBaseViewModel extends ViewModel {

    private final MediatorLiveData<List<MovieListItem>> movies = new MediatorLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private boolean loaded = false;

    public LiveData<List<MovieListItem>> getMovies() {
        if (!loaded) {
            loadMoviesInternal();
            loaded = true;
        }
        return movies;
    }

    private void loadMoviesInternal() {
        LiveData<Resource<List<Movie>>> liveData = loadMovies();
        movies.addSource(liveData, resource -> {
            //noinspection ConstantConditions
            if (resource.status != Resource.LOADING) {
                loading.setValue(false);
            }
            if (resource.status == Resource.SUCCESS) {
                List<MovieListItem> list = new ArrayList<>();
                for (Movie item : resource.data) {
                    list.add(MovieListItem.create(item));
                }
                movies.setValue(list);
            }
        });
    }

    public abstract LiveData<Resource<List<Movie>>> loadMovies();

    public LiveData<Boolean> getIsLoading() {
        return loading;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    public void onSaveInstanceState(Bundle outState) {
    }

}
