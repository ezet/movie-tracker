package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.model.ApiList;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.model.MovieReview;
import no.ezet.fasttrack.popularmovies.model.MovieTrailer;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import no.ezet.fasttrack.popularmovies.service.IMovieService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MovieDetailsViewModel extends ViewModel {


    private static final String SELECTED_MOVIE_ID = "SELECTED_MOVIE_ID";
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private final MediatorLiveData<Movie> selectedMovie = new MediatorLiveData<>();
    private final MovieRepository movieRepository;
    private final IMovieService movieService;
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private int selectedMovieId;
    private MediatorLiveData<List<MovieReview>> reviews = new MediatorLiveData<>();
    private MediatorLiveData<List<MovieTrailer>> trailers = new MediatorLiveData<>();


    @Inject
    MovieDetailsViewModel(MovieRepository movieRepository, IMovieService movieService) {
        this.movieRepository = movieRepository;
        this.movieService = movieService;
    }

    public LiveData<Movie> getSelectedMovie() {
        Timber.d("getSelectedMovie: ");
        return selectedMovie;
    }

    public void setSelectedMovie(int id) {
        this.selectedMovieId = id;
        LiveData<Resource<Movie>> selectedMovieSource = movieRepository.getMovie(id);
        selectedMovie.addSource(selectedMovieSource, movieResource -> {
            if (movieResource == null) return;
            if (movieResource.status == Resource.SUCCESS)
                selectedMovie.removeSource(selectedMovieSource);
            selectedMovie.setValue(movieResource.data);
        });
    }

    public LiveData<List<MovieReview>> getReviews() {
        if (selectedMovieId == 0) throw new IllegalArgumentException();
        movieService.getReviews(selectedMovieId).enqueue(new Callback<ApiList<MovieReview>>() {
            @Override
            public void onResponse(@NonNull Call<ApiList<MovieReview>> call, @NonNull Response<ApiList<MovieReview>> response) {
                if (response.isSuccessful()) //noinspection ConstantConditions
                    reviews.setValue(response.body().results);
            }

            @Override
            public void onFailure(@NonNull Call<ApiList<MovieReview>> call, @NonNull Throwable t) {
                Timber.d("onFailure: ");

            }
        });

        return reviews;
    }

    public LiveData<List<MovieTrailer>> getTrailers() {
        if (selectedMovieId == 0) throw new IllegalArgumentException();
        movieService.getVideos(selectedMovieId).enqueue(new Callback<ApiList<MovieTrailer>>() {
            @Override
            public void onResponse(@NonNull Call<ApiList<MovieTrailer>> call, @NonNull Response<ApiList<MovieTrailer>> response) {
                if (response.isSuccessful()) trailers.setValue(response.body().results);
            }

            @Override
            public void onFailure(@NonNull Call<ApiList<MovieTrailer>> call, @NonNull Throwable t) {

            }
        });
        return trailers;
    }
}
