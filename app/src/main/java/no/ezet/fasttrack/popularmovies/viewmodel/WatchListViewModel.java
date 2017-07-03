package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.WatchListRepository;

public class WatchListViewModel extends MovieListBaseViewModel {

    private final WatchListRepository repository;

    @Inject
    WatchListViewModel(WatchListRepository repository) {
        this.repository = repository;
    }


    @Override
    public LiveData<Resource<List<Movie>>> loadMovies() {
        return repository.getAll();
    }
}