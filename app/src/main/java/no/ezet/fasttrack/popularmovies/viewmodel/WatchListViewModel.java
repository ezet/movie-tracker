package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.WatchListRepository;

public class WatchListViewModel extends MovieListBaseViewModel {

    private final WatchListRepository repository;

    @Inject
    public WatchListViewModel(WatchListRepository repository) {
        this.repository = repository;
    }


    @Override
    public void loadMovies() {
        load(repository.getAll());
    }

    private void load(LiveData<Resource<List<Movie>>> source) {
        loading.setValue(true);
        movies.addSource(source, listResource -> {
            if (listResource.status != Resource.LOADING)
                loading.setValue(false);
            if (listResource.status == Resource.SUCCESS) {
                List<MovieListItem> list = new ArrayList<>();
                for (Movie movie : listResource.data) {
                    list.add(MovieListItem.create(movie));
                }
                movies.setValue(list);
            }
        });
    }
}