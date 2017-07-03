package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;


public class MovieListViewModel extends MovieListBaseViewModel {

    public static final int POPULAR = 0;
    public static final int UPCOMING = 1;
    public static final int TOP_RATED = 2;
    public static final int NOW_PLAYING = 3;
    private final MovieRepository movieRepository;
    private int listType = -1;

    @Inject
    MovieListViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void setListType(int listType) {
        this.listType = listType;
    }

    @Override
    public LiveData<Resource<List<Movie>>> loadMovies() {
        switch (listType) {
            case POPULAR:
                return movieRepository.getPopular();
            case UPCOMING:
                return movieRepository.getUpcoming();
            case TOP_RATED:
                return movieRepository.getTopRated();
            case NOW_PLAYING:
                return movieRepository.getNowPlaying();
            default:
                throw new IllegalArgumentException();
        }
    }

}
