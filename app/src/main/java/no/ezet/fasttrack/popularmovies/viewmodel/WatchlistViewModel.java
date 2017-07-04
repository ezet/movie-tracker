package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.WatchlistRepository;

public class WatchlistViewModel extends MovieListBaseViewModel {

    private final WatchlistRepository repository;

    @Inject
    WatchlistViewModel(WatchlistRepository repository) {
        this.repository = repository;
    }


    @Override
    public LiveData<Resource<List<Movie>>> loadMovies() {
        return repository.getAll();
    }
}