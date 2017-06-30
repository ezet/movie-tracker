package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

import java.util.List;


public abstract class MovieListBaseViewModel extends ViewModel {

    protected final MediatorLiveData<List<MovieListItem>> movies = new MediatorLiveData<>();
    protected final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    protected final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private boolean loaded = false;

    public LiveData<List<MovieListItem>> getMovies() {
        if (!loaded) {
            loadMovies();
            loaded = true;
        }
        return movies;
    }

    public abstract void loadMovies();

    public LiveData<Boolean> getIsLoading() {
        return loading;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    public void onSaveInstanceState(Bundle outState) {
    }

}
