package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import timber.log.Timber;


public class MovieListViewModel extends MovieListBaseViewModel {

    public static final int POPULAR = 0;
    public static final int UPCOMING = 1;
    public static final int TOP_RATED = 2;
    public static final int NOW_PLAYING = 3;
    private static final String STATE_CURRENT_SORT = "STATE_CURRENT_SORT";
    private final MovieRepository movieRepository;

    public void setListType(int listType) {
        this.listType = listType;
    }

    private int listType = -1;

    @Inject
    MovieListViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void loadMovies() {
        loading.setValue(true);
        switch (listType) {
            case POPULAR:
                loadMovies(movieRepository.getPopular());
                break;
            case UPCOMING:
                loadMovies(movieRepository.getUpcoming());
                break;
            case TOP_RATED:
                loadMovies(movieRepository.getTopRated());
                break;
            case NOW_PLAYING:
                loadMovies(movieRepository.getNowPlaying());
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void loadMovies(LiveData<Resource<List<Movie>>> liveData) {
        movies.addSource(liveData, resource -> {
            //noinspection ConstantConditions
            if (resource.status != Resource.LOADING) {
                movies.removeSource(liveData);
                loading.setValue(false);
            }
            if (resource.status == Resource.SUCCESS) {
                List<MovieListItem> list = new ArrayList<>();
                for (Movie item : resource.data) {
                    list.add(MovieListItem.create(item));
                }
                movies.setValue(list);
            }
        });

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_CURRENT_SORT)) {
            Timber.d("onRestoreInstanceState: loading saved state");
//            setSortBy(savedInstanceState.getInt(STATE_CURRENT_SORT));
        }
    }

}
