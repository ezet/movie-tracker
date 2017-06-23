package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import no.ezet.fasttrack.popularmovies.service.IMovieService;
import timber.log.Timber;

public class MovieListViewModel extends ViewModel {

    public static final int POPULAR = 0;
    public static final int UPCOMING = 1;
    public static final int TOP_RATED = 2;
    public static final int DEFAULT_SORT_BY = 0;
    private static final String CURRENT_SORT_BY = "CURRENT_SORT_BY";
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MovieRepository movieRepository;
    private final IMovieService movieService;
    private final MediatorLiveData<List<Movie>> movies = new MediatorLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Integer> titleResourceId = new MutableLiveData<>();
    private int currentSortBy = DEFAULT_SORT_BY;


    @Inject
    MovieListViewModel(MovieRepository movieRepository, IMovieService movieService) {
        this.movieRepository = movieRepository;
        this.movieService = movieService;
        setSortBy(DEFAULT_SORT_BY);
    }

    public int getSortBy() {
        return currentSortBy;
    }

    public void setSortBy(int sortBy) {
        currentSortBy = sortBy;
        setTitle(sortBy);
        loadMovies(sortBy);
    }

    private void setTitle(int sortBy) {
        switch (sortBy) {
            case POPULAR:
                titleResourceId.setValue(R.string.popular);
                break;
            case UPCOMING:
                titleResourceId.setValue(R.string.upcoming);
                break;
            case TOP_RATED:
                titleResourceId.setValue(R.string.top_rated);
                break;
        }
    }

    public LiveData<List<Movie>> getMovies() {
        Timber.d("getMovies: ");
        return movies;
    }

    private void loadMovies(int sortBy) {
        loading.setValue(true);
        LiveData<Resource<List<Movie>>> movieListSource = null;
        switch (sortBy) {
            case POPULAR:
                movieListSource = movieRepository.getPopularMovies();
                break;
            case UPCOMING:
                movieListSource = movieRepository.getUpcomingMovies();
                break;
            case TOP_RATED:
                movieListSource = movieRepository.getTopRatedMovies();
                break;
        }
        LiveData<Resource<List<Movie>>> finalMovieListSource = movieListSource;
        movies.addSource(finalMovieListSource, resource -> {
            //noinspection ConstantConditions
            Timber.d("loadMovies: " + resource.status);
            if (resource.status != Resource.LOADING) {
                movies.removeSource(finalMovieListSource);
                loading.setValue(false);
            }
            if (resource.status == Resource.SUCCESS) {
                movies.setValue(resource.data);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return loading;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_SORT_BY)) {
//            setSortBy(savedInstanceState.getInt(CURRENT_SORT_BY));
//        } else {
//            setSortBy(DEFAULT_SORT_BY);
//        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_SORT_BY, currentSortBy);
    }

    public LiveData<Integer> getTitleResourceId() {
        return titleResourceId;
    }
}
