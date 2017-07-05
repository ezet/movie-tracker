package no.ezet.tmdb.viewmodel;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import no.ezet.tmdb.api.model.Movie;
import no.ezet.tmdb.network.Resource;
import no.ezet.tmdb.repository.WatchlistRepository;

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