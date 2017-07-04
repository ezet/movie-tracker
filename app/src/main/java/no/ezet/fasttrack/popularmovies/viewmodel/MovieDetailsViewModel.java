package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.api.model.MovieReview;
import no.ezet.fasttrack.popularmovies.api.model.MovieTrailer;
import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.FavoriteRepository;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import no.ezet.fasttrack.popularmovies.repository.WatchlistRepository;
import timber.log.Timber;

public class MovieDetailsViewModel extends ViewModel {

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MediatorLiveData<Movie> movie = new MediatorLiveData<>();
    private final MovieRepository movieRepository;
    private final FavoriteRepository favoriteRepository;
    private final WatchlistRepository watchlistRepository;
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MediatorLiveData<List<MovieReview>> reviews = new MediatorLiveData<>();
    private MediatorLiveData<List<MovieTrailer>> trailers = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> favorite = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> bookmark = new MediatorLiveData<>();

    @Inject
    MovieDetailsViewModel(MovieRepository movieRepository, FavoriteRepository favoriteRepository, WatchlistRepository watchlistRepository) {
        this.movieRepository = movieRepository;
        this.favoriteRepository = favoriteRepository;
        this.watchlistRepository = watchlistRepository;
    }

    @NonNull
    public LiveData<Boolean> getIsFavorite() {
        return favorite;
    }

    @NonNull
    public LiveData<Boolean> getIsBookmark() {
        return bookmark;
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

    public void setMovie(int id) {
        loading.setValue(true);
        LiveData<Resource<Movie>> selectedMovieSource = movieRepository.getMovieDetails(id);
        movie.addSource(selectedMovieSource, movieResource -> {
            //noinspection ConstantConditions
            if (movieResource.status != Resource.LOADING) {
                movie.removeSource(selectedMovieSource);
                loading.setValue(false);
            }
            if (movieResource.status == Resource.SUCCESS) {
                movie.setValue(movieResource.data);
                reviews.setValue(movieResource.data.reviews.results);
                trailers.setValue(movieResource.data.videos.results);
            }
        });

        LiveData<Resource<Movie>> favoriteRes = favoriteRepository.getById(id);
        favorite.addSource(favoriteRes, favoriteResource -> {
            //noinspection ConstantConditions
            if (favoriteResource.status == Resource.SUCCESS) {
                favorite.setValue(favoriteResource.data != null);
            }
        });

        LiveData<Resource<Movie>> watchlistRes = watchlistRepository.getById(id);
        favorite.addSource(watchlistRes, bookmarkResource -> {
            //noinspection ConstantConditions
            if (bookmarkResource.status == Resource.SUCCESS) {
                bookmark.setValue(bookmarkResource.data != null);
            }
        });
    }

    public LiveData<List<MovieReview>> getReviews() {
        return reviews;
    }

    public LiveData<List<MovieTrailer>> getTrailers() {
        return trailers;
    }

    public void toggleFavorite() {
        Timber.d("toggleFavorite: ");
        Boolean isFavorite = favorite.getValue();
        if (isFavorite) {
            favoriteRepository.remove(movie.getValue());
        } else {
            favoriteRepository.add(movie.getValue());
        }
    }

    public void toggleBookmark() {
        Boolean isBookmarked = bookmark.getValue();
        if (isBookmarked) {
            watchlistRepository.remove(movie.getValue());
        } else {
            watchlistRepository.add(movie.getValue());
        }
    }
}
