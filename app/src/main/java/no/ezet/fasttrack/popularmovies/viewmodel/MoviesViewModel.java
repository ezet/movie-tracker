package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.model.MovieList;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;

public class MoviesViewModel extends ViewModel {

    public static final int POPULAR = 0;
    public static final int UPCOMING = 1;
    public static final int TOP_RATED = 2;
    private static final String CURRENT_SORT_BY = "CURRENT_SORT_BY";
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Movie> selectedMovie = new MutableLiveData<>();
    private final MovieRepository movieRepository;
    private final MediatorLiveData<MovieList> movies = new MediatorLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentSortBy = new MutableLiveData<>();
    private final LiveData<Integer> titleResourceId;
    private LiveData<Resource<MovieList>> movieResource;


    @Inject
    MoviesViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        movies.addSource(currentSortBy, this::loadMovies);
        currentSortBy.setValue(0);
        titleResourceId = Transformations.map(currentSortBy, integer -> {
            if (integer == POPULAR) return R.string.popular;
            if (integer == UPCOMING) return R.string.upcoming;
            else return R.string.top_rated;
        });
    }

    public MutableLiveData<Integer> getSortBy() {
        return currentSortBy;
    }

    public void setSortBy(int sortBy) {
        currentSortBy.setValue(sortBy);
    }

    public LiveData<Movie> getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(Movie movie) {
        selectedMovie.setValue(movie);
    }

    public LiveData<MovieList> getMovies() {
        if (currentSortBy.getValue() != null) {
            loadMovies(currentSortBy.getValue());
        }
        return movies;
    }

    private LiveData<MovieList> loadMovies(int sortBy) {
        movies.removeSource(movieResource);
        loading.setValue(true);
        switch (sortBy) {
            case POPULAR:
                movieResource = movieRepository.getPopularMovies();
                break;
            case UPCOMING:
                movieResource = movieRepository.getUpcomingMovies();
                break;
            case TOP_RATED:
                movieResource = movieRepository.getTopRatedMovies();
                break;
        }
        movies.addSource(movieResource, movieListResource -> {
            if (movieListResource == null) return;
            if (movieListResource.status == Resource.SUCCESS) {
                movies.setValue(movieListResource.data);
            }
            if (movieListResource.status != Resource.LOADING) loading.setValue(false);
        });
        return this.movies;
    }

    public LiveData<Boolean> getIsLoading() {
        return loading;
    }

    public void onViewStateRestored(Bundle savedInstanceState) {
        if (savedInstanceState == null) return;
        currentSortBy.setValue(savedInstanceState.getInt(CURRENT_SORT_BY));
    }


    public void onSaveInstanceState(Bundle outState) {
        if (currentSortBy.getValue() == null) return;
        outState.putInt(CURRENT_SORT_BY, currentSortBy.getValue());

    }

    public LiveData<Integer> getTitleResourceId() {
        return titleResourceId;
    }
}
