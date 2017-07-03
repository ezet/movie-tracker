package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.FavoriteRepository;


public class FavoriteListViewModel extends MovieListBaseViewModel {

    private final FavoriteRepository favoriteRepository;

    @Inject
    FavoriteListViewModel(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public LiveData<Resource<List<Movie>>> loadMovies() {
        return favoriteRepository.getAll();
    }
}
