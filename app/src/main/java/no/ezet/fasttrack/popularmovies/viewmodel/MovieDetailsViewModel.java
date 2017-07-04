package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.api.model.MovieReview;
import no.ezet.fasttrack.popularmovies.api.model.MovieTrailer;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.FavoriteRepository;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import no.ezet.fasttrack.popularmovies.repository.RatedRepository;
import no.ezet.fasttrack.popularmovies.repository.WatchlistRepository;

public class MovieDetailsViewModel extends ViewModel {

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MediatorLiveData<Movie> movie = new MediatorLiveData<>();
    private final MovieRepository movieRepository;
    private final FavoriteRepository favoriteRepository;
    private final WatchlistRepository watchlistRepository;
    private final RatedRepository ratedRepository;
    private MediatorLiveData<List<MovieReview>> reviews = new MediatorLiveData<>();
    private MediatorLiveData<List<MovieTrailer>> trailers = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> favorite = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> bookmark = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> isRated = new MediatorLiveData<>();

    @Inject
    MovieDetailsViewModel(MovieRepository movieRepository, FavoriteRepository favoriteRepository, WatchlistRepository watchlistRepository, RatedRepository ratedRepository) {
        this.movieRepository = movieRepository;
        this.favoriteRepository = favoriteRepository;
        this.watchlistRepository = watchlistRepository;
        this.ratedRepository = ratedRepository;
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
//                movie.removeSource(selectedMovieSource);
                loading.setValue(false);
            }
            if (movieResource.status == Resource.SUCCESS) {
                movie.setValue(movieResource.data);
                reviews.setValue(movieResource.data.reviews.results);
                trailers.setValue(movieResource.data.videos.results);
            }
        });

        LiveData<Resource<Movie>> favoriteRes = favoriteRepository.getById(id);
        favorite.addSource(favoriteRes, movieResource -> {
            if (movieResource != null && movieResource.status == Resource.SUCCESS) {
                boolean newValue = movieResource.data != null;
                if (favorite.getValue() == null || newValue != favorite.getValue())
                    favorite.setValue(newValue);
            }
        });

        LiveData<Resource<Movie>> watchlistRes = watchlistRepository.getById(id);
        favorite.addSource(watchlistRes, movieResource -> {
            if (movieResource != null && movieResource.status == Resource.SUCCESS) {
                boolean newValue = movieResource.data != null;
                if (bookmark.getValue() == null || newValue != bookmark.getValue())
                    bookmark.setValue(newValue);
            }
        });

        isRated.addSource(ratedRepository.getById(id), movieResource -> {
            if (movieResource != null && movieResource.status == Resource.SUCCESS) {
                boolean newValue = movieResource.data != null;
                if (isRated.getValue() == null || newValue != isRated.getValue())
                    isRated.setValue(newValue);
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
        Boolean isFavorite = favorite.getValue();
        if (isFavorite != null && isFavorite) {
            favoriteRepository.remove(movie.getValue());
        } else {
            favoriteRepository.add(movie.getValue());
        }
    }

    public void toggleBookmark() {
        Boolean isBookmarked = bookmark.getValue();
        if (isBookmarked != null && isBookmarked) {
            watchlistRepository.remove(movie.getValue());
        } else {
            watchlistRepository.add(movie.getValue());
        }
    }

    public LiveData<Boolean> getIsRated() {
        return isRated;
    }

    public void rate(double rating) {
        if (getMovie().getValue() == null) return;
        if (rating < 0.5) rating = 0.5;
        getMovie().getValue().setRating(rating);
        ratedRepository.add(getMovie().getValue());
    }

    public void deleteRating() {
        if (getMovie().getValue() == null) return;
        ratedRepository.remove(getMovie().getValue());
    }
}
