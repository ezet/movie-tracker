package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.FavoriteRepository;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import timber.log.Timber;


public class MovieListViewModel extends ViewModel {

    public static final int POPULAR = 0;
    public static final int UPCOMING = 1;
    public static final int TOP_RATED = 2;
    public static final int FAVORITES = 3;
    private static final String STATE_CURRENT_SORT = "STATE_CURRENT_SORT";
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MovieRepository movieRepository;
    private final FavoriteRepository favoriteRepository;
    private final MediatorLiveData<List<MovieListItem>> movies = new MediatorLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private int currentSortBy = -1;

    @Inject
    MovieListViewModel(MovieRepository movieRepository, FavoriteRepository favoriteRepository) {
        this.movieRepository = movieRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public int getSortBy() {
        return currentSortBy;
    }

    public void setSortBy(int sortBy) {
        currentSortBy = sortBy;
        loadMovies(sortBy);
    }


    public LiveData<List<MovieListItem>> getMovies() {
        Timber.d("getMovies: ");
        return movies;
    }

    private void loadMovies(int sortBy) {
        loading.setValue(true);
        switch (sortBy) {
            case POPULAR:
                loadMovies(movieRepository.getPopularMovies());
                break;
            case UPCOMING:
                loadMovies(movieRepository.getUpcomingMovies());
                break;
            case TOP_RATED:
                loadMovies(movieRepository.getTopRatedMovies());
                break;
        }
    }

    private void loadMovies(LiveData<Resource<List<Movie>>> liveData) {
        movies.addSource(liveData, resource -> {
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

    public LiveData<Boolean> getIsLoading() {
        return loading;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_CURRENT_SORT)) {
            Timber.d("onRestoreInstanceState: loading saved state");
            setSortBy(savedInstanceState.getInt(STATE_CURRENT_SORT));
        } else if (currentSortBy == -1) {
            setSortBy(0);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        Timber.d("onSaveInstanceState: ");
        outState.putInt(STATE_CURRENT_SORT, currentSortBy);
    }

}
