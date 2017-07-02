package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.db.Favorite;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.FavoriteRepository;
import timber.log.Timber;


public class FavoriteListViewModel extends MovieListBaseViewModel {

    private final FavoriteRepository favoriteRepository;

    @Inject
    FavoriteListViewModel(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public void loadMovies() {
        loadFavorites(favoriteRepository.getAll());
    }

    private void loadFavorites(LiveData<Resource<List<Favorite>>> liveData) {
        loading.setValue(true);
        movies.addSource(liveData, resource -> {
            Timber.d("loadFavorites: favorites has changed");
            //noinspection ConstantConditions
            if (resource.status != Resource.LOADING) {
//                movies.removeSource(liveData);
                loading.setValue(false);
            }
            if (resource.status == Resource.SUCCESS) {
                List<MovieListItem> list = new ArrayList<>();
                for (Favorite item : resource.data) {
                    list.add(MovieListItem.create(item));
                }
                movies.setValue(list);
            }
        });
    }

}
