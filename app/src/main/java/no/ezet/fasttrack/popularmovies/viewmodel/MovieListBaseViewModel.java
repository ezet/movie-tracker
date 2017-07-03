package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import timber.log.Timber;


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
        Timber.d("loadMovies: ");
        LiveData<Resource<List<Movie>>> liveData = loadMovies();
        movies.addSource(liveData, resource -> {
            Timber.d("loadMovies: callback: status: " + resource.status);
            //noinspection ConstantConditions
            if (resource.status != Resource.LOADING) {
                movies.removeSource(liveData);
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
