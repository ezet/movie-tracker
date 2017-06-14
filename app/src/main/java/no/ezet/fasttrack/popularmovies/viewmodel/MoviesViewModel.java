package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.model.MovieList;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;

public class MoviesViewModel extends ViewModel {

    public static final int POPULAR = 1;
    public static final int UPCOMING = 2;
    public static final int TOP_RATED = 3;
    private final MovieRepository movieRepository;
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MutableLiveData<Movie> selectedMovie;
    private final MediatorLiveData<MovieList> movies = new MediatorLiveData<>();
    private LiveData<Resource<MovieList>> movieResource;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    MoviesViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        selectedMovie = new MutableLiveData<>();
    }

    public LiveData<MovieList> getMovies(int sortBy) {
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

    public void setSelectedMovie(Movie movie) {
        selectedMovie.setValue(movie);
    }
}
