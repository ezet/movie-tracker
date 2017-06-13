package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.ContributesAndroidInjector;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.model.MovieList;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import no.ezet.fasttrack.popularmovies.task.RepositoryListener;

public class MoviesViewModel extends ViewModel implements RepositoryListener<MovieList> {


    private final MovieRepository movieRepository;
    private final MutableLiveData<Boolean> loading;
    private final LiveData<List<Movie>> movies;
    private final MutableLiveData<Movie> selectedMovie;



    @Inject
    public MoviesViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        loading = new MutableLiveData<>();
        movies = new MutableLiveData<>();
        selectedMovie = new MutableLiveData<>();
        loading.setValue(false);
    }

    public LiveData<List<Movie>> getMovies(String query) {
        movieRepository.getMovies(query, this);
        return movies;
    }

    @Override
    public void onPostExecute(MovieList result) {
        loading.setValue(false);
    }

    @Override
    public void onPreExecute() {
        loading.setValue(true);
    }
}
