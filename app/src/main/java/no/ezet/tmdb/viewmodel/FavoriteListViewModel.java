package no.ezet.tmdb.viewmodel;

import android.arch.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import no.ezet.tmdb.api.model.Movie;
import no.ezet.tmdb.network.Resource;
import no.ezet.tmdb.repository.FavoriteRepository;


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
