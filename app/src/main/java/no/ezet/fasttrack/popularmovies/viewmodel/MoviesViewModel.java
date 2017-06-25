//package no.ezet.fasttrack.popularmovies.viewmodel;
//
//import android.arch.lifecycle.LiveData;
//import android.arch.lifecycle.MediatorLiveData;
//import android.arch.lifecycle.MutableLiveData;
//import android.arch.lifecycle.ViewModel;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//
//import java.util.List;
//
//import javax.inject.Inject;
//
//import no.ezet.fasttrack.popularmovies.R;
//import no.ezet.fasttrack.popularmovies.model.ApiList;
//import no.ezet.fasttrack.popularmovies.model.Movie;
//import no.ezet.fasttrack.popularmovies.model.MovieReview;
//import no.ezet.fasttrack.popularmovies.model.MovieTrailer;
//import no.ezet.fasttrack.popularmovies.network.Resource;
//import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
//import no.ezet.fasttrack.popularmovies.service.IMovieService;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import timber.log.Timber;
//
//public class MoviesViewModel extends ViewModel {
//
//    public static final int POPULAR = 0;
//    public static final int UPCOMING = 1;
//    public static final int TOP_RATED = 2;
//    public static final int FAVORITES = 3;
//    public static final int DEFAULT_SORT_BY = 0;
//    private static final String CURRENT_SORT_BY = "CURRENT_SORT_BY";
//    private static final String SELECTED_MOVIE_ID = "SELECTED_MOVIE_ID";
//    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();
//    private final MediatorLiveData<Movie> selectedMovie = new MediatorLiveData<>();
//    private final MovieRepository movieRepository;
//    private final IMovieService movieService;
//    private final MediatorLiveData<List<Movie>> movies = new MediatorLiveData<>();
//    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
//    private final MutableLiveData<Integer> titleResourceId = new MutableLiveData<>();
//    private int currentSortBy = DEFAULT_SORT_BY;
//    private int selectedMovieId;
//    private MediatorLiveData<List<MovieReview>> reviews = new MediatorLiveData<>();
//    private MediatorLiveData<List<MovieTrailer>> trailers = new MediatorLiveData<>();
//
//
//    @Inject
//    MoviesViewModel(MovieRepository movieRepository, IMovieService movieService) {
//        this.movieRepository = movieRepository;
//        this.movieService = movieService;
//        setSortBy(DEFAULT_SORT_BY);
//    }
//
//    public int getSortBy() {
//        return currentSortBy;
//    }
//
//    public void setSortBy(int sortBy) {
//        currentSortBy = sortBy;
//        setTitle(sortBy);
//        loadMovies(sortBy);
//    }
//
//    private void setTitle(int sortBy) {
//        switch (sortBy) {
//            case POPULAR:
//                titleResourceId.setValue(R.string.popular);
//                break;
//            case UPCOMING:
//                titleResourceId.setValue(R.string.upcoming);
//                break;
//            case TOP_RATED:
//                titleResourceId.setValue(R.string.top_rated);
//                break;
//        }
//    }
//
//    public LiveData<Movie> getSelectedMovie() {
//        Timber.d("getSelectedMovie: ");
//        return selectedMovie;
//    }
//
//    public void setSelectedMovie(int id) {
//        this.selectedMovieId = id;
//        LiveData<Resource<Movie>> selectedMovieSource = movieRepository.getMovie(id);
//        selectedMovie.addSource(selectedMovieSource, movieResource -> {
//            if (movieResource == null) return;
//            if (movieResource.status == Resource.SUCCESS)
//                selectedMovie.removeSource(selectedMovieSource);
//            selectedMovie.setValue(movieResource.data);
//        });
//    }
//
//    public LiveData<List<Movie>> getMovies() {
//        Timber.d("getMovies: ");
//        return movies;
//    }
//
//    private void loadMovies(int sortBy) {
//        loading.setValue(true);
//        LiveData<Resource<List<Movie>>> movieListSource = null;
//        switch (sortBy) {
//            case POPULAR:
//                movieListSource = movieRepository.getPopularMovies();
//                break;
//            case UPCOMING:
//                movieListSource = movieRepository.getUpcomingMovies();
//                break;
//            case TOP_RATED:
//                movieListSource = movieRepository.getTopRatedMovies();
//                break;
//        }
//        LiveData<Resource<List<Movie>>> finalMovieListSource = movieListSource;
//        movies.addSource(finalMovieListSource, resource -> {
//            //noinspection ConstantConditions
//            Timber.d("loadMovies: " + resource.status);
//            if (resource.status != Resource.LOADING) {
//                movies.removeSource(finalMovieListSource);
//                loading.setValue(false);
//            }
//            if (resource.status == Resource.SUCCESS) {
//                movies.setValue(resource.data);
//            }
//        });
//    }
//
//    public LiveData<Boolean> getIsLoading() {
//        return loading;
//    }
//
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
////        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_SORT_BY)) {
////            setSortBy(savedInstanceState.getInt(CURRENT_SORT_BY));
////        } else {
////            setSortBy(DEFAULT_SORT_BY);
////        }
//    }
//
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putInt(CURRENT_SORT_BY, currentSortBy);
//    }
//
//    public LiveData<Integer> getTitleResourceId() {
//        return titleResourceId;
//    }
//
//    public LiveData<List<MovieReview>> getReviews() {
//        if (selectedMovieId == 0) throw new IllegalArgumentException();
//        movieService.getReviews(selectedMovieId).enqueue(new Callback<ApiList<MovieReview>>() {
//            @Override
//            public void onResponse(@NonNull Call<ApiList<MovieReview>> call, @NonNull Response<ApiList<MovieReview>> response) {
//                if (response.isSuccessful()) //noinspection ConstantConditions
//                    reviews.setValue(response.body().results);
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ApiList<MovieReview>> call, @NonNull Throwable t) {
//                Timber.d("onFailure: ");
//
//            }
//        });
//
//        return reviews;
//    }
//
//    public LiveData<List<MovieTrailer>> getTrailers() {
//        if (selectedMovieId == 0) throw new IllegalArgumentException();
//        movieService.getVideos(selectedMovieId).enqueue(new Callback<ApiList<MovieTrailer>>() {
//            @Override
//            public void onResponse(@NonNull Call<ApiList<MovieTrailer>> call, @NonNull Response<ApiList<MovieTrailer>> response) {
//                if (response.isSuccessful()) trailers.setValue(response.body().results);
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ApiList<MovieTrailer>> call, @NonNull Throwable t) {
//
//            }
//        });
//        return trailers;
//    }
//}
