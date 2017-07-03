package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import no.ezet.fasttrack.popularmovies.service.PreferenceService;
import timber.log.Timber;


public class MovieListViewModel extends MovieListBaseViewModel {

    public static final int POPULAR = 0;
    public static final int UPCOMING = 1;
    public static final int TOP_RATED = 2;
    public static final int NOW_PLAYING = 3;
    private static final String STATE_CURRENT_SORT = "STATE_CURRENT_SORT";
    private final MovieRepository movieRepository;
    private final PreferenceService preferences;
    private int listType = -1;

    @Inject
    MovieListViewModel(MovieRepository movieRepository, PreferenceService preferences) {
        Timber.d("MovieListViewModel: ");
        this.movieRepository = movieRepository;
        this.preferences = preferences;
    }

    public void setListType(int listType) {
        this.listType = listType;
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
        Timber.d("loadMovies: ");
        movies.addSource(liveData, resource -> {
            Timber.d("loadMovies: callback: status: " + resource.status);
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

}
