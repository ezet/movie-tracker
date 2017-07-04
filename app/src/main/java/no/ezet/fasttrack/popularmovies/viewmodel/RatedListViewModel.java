package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.RatedRepository;

public class RatedListViewModel extends MovieListBaseViewModel {

    private final RatedRepository repository;

    @Inject
    RatedListViewModel(RatedRepository repository) {
        this.repository = repository;
    }

    @Override
    public LiveData<Resource<List<Movie>>> loadMovies() {
        return repository.getAll();
    }
}
