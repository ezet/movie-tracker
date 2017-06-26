package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.model.Favorite;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.FavoriteRepository;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import timber.log.Timber;


public class FavoriteListViewModel extends MovieListViewModel {

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
    FavoriteListViewModel(MovieRepository movieRepository, FavoriteRepository favoriteRepository) {
        super(movieRepository, favoriteRepository);
        this.movieRepository = movieRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public int getSortBy() {
        return currentSortBy;
    }

    public void setSortBy(int sortBy) {
        currentSortBy = sortBy;
        loadMovies();
    }


    public LiveData<List<MovieListItem>> getMovies() {
        Timber.d("getMovies: ");
        return movies;
    }

    private void loadMovies() {
        loading.setValue(true);
        loadFavorites(favoriteRepository.getAll());

    }

    private void loadFavorites(LiveData<Resource<List<Favorite>>> liveData) {
        movies.addSource(liveData, resource -> {
            //noinspection ConstantConditions
            if (resource.status != Resource.LOADING) {
                movies.removeSource(liveData);
                loading.setValue(false);
            }
            if (resource.status == Resource.SUCCESS) {
                List<MovieListItem> list = new ArrayList<>();
                for (Favorite item : resource.data) {
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
        setSortBy(FAVORITES);
    }

    public void onSaveInstanceState(Bundle outState) {

    }
}
