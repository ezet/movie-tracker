package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.db.Favorite;
import no.ezet.fasttrack.popularmovies.db.MovieReview;
import no.ezet.fasttrack.popularmovies.db.MovieTrailer;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.FavoriteRepository;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import no.ezet.fasttrack.popularmovies.api.Mdb3Api;
import timber.log.Timber;

public class MovieDetailsViewModel extends ViewModel {

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MediatorLiveData<Movie> movie = new MediatorLiveData<>();
    private final MovieRepository movieRepository;
    private final FavoriteRepository favoriteRepository;
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private int movieId;
    private MediatorLiveData<List<MovieReview>> reviews = new MediatorLiveData<>();
    private MediatorLiveData<List<MovieTrailer>> trailers = new MediatorLiveData<>();
    private MediatorLiveData<Boolean> favorite = new MediatorLiveData<>();

    @Inject
    MovieDetailsViewModel(MovieRepository movieRepository, FavoriteRepository favoriteRepository, Mdb3Api movieService) {
        this.movieRepository = movieRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public LiveData<Boolean> getFavorite() {
        return favorite;
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

    public void setMovie(int id) {
        this.movieId = id;
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

        LiveData<Resource<Favorite>> byId = favoriteRepository.getById(id);
        favorite.addSource(byId, favoriteResource -> {
            Timber.d("setMovie: byId trigger");
//            if (favoriteResource.status != Resource.LOADING) {
//                favorite.removeSource(byId);
//            }
            //noinspection ConstantConditions
            if (favoriteResource.status == Resource.SUCCESS) {
                favorite.setValue(favoriteResource.data != null);
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
        Boolean value = !favorite.getValue();
        if (value) {
            favoriteRepository.add(movie.getValue());
        } else {
            favoriteRepository.remove(movie.getValue());
        }
    }
}
