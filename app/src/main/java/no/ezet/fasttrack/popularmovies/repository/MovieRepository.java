package no.ezet.fasttrack.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.db.MovieDao;
import no.ezet.fasttrack.popularmovies.model.ApiList;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.network.NetworkResource;
import no.ezet.fasttrack.popularmovies.network.NetworkResponse;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.service.IMovieService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MovieRepository {

    private static final String QUERY_POPULAR = "popular";
    private static final String QUERY_UPCOMING = "upcoming";
    private static final String QUERY_TOPRATED = "top_rated";
    private final IMovieService movieService;
    private final MovieDao movieDao;

    @Inject
    MovieRepository(IMovieService movieService, MovieDao movieDao) {
        this.movieService = movieService;
        this.movieDao = movieDao;
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getPopularMovies() {
        return getMovies(QUERY_POPULAR);
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getUpcomingMovies() {
        return getMovies(QUERY_UPCOMING);
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getTopRatedMovies() {
        return getMovies(QUERY_TOPRATED);
    }

    private LiveData<Resource<List<Movie>>> getMovies(String query) {
        return new NetworkBoundResource(query, movieDao, movieService).getAsLiveData();
    }

    private class NetworkBoundResource extends NetworkResource<List<Movie>, ApiList<Movie>> {

        private MovieDao movieDao;
        private IMovieService movieService;
        private String query;
        private boolean cached = false;

        private NetworkBoundResource(String query, MovieDao movieDao, IMovieService movieService) {
            this.query = query;
            this.movieDao = movieDao;
            this.movieService = movieService;
        }

        @Override
        protected void saveCallResult(ApiList<Movie> movies) {
            Timber.d("saveCallResult: ");
            for (Movie movie : movies.results) {
                switch (query) {
                    case QUERY_POPULAR:
                        movie.setType(Movie.POPULAR);
                        break;
                    case QUERY_UPCOMING:
                        movie.setType(Movie.UPCOMING);
                        break;
                    case QUERY_TOPRATED:
                        movie.setType(Movie.TOP_RATED);
                        break;
                }
            }
            movieDao.insert(movies.results);
            cached = true;
        }

        @Override
        protected boolean shouldFetch(List<Movie> data) {
            return !cached;
        }

        @Override
        protected LiveData<List<Movie>> loadFromDb() {
            Timber.d("loadFromDb: ");
            switch (query) {
                case QUERY_POPULAR:
                    return movieDao.getPopular();
                case QUERY_UPCOMING:
                    return movieDao.getUpcoming();
                case QUERY_TOPRATED:
                    return movieDao.getTopRated();
                default:
                    throw new IllegalArgumentException("Cannot load: " + query);
            }
        }

        @Override
        protected LiveData<NetworkResponse<ApiList<Movie>>> createCall() {
            Timber.d("createCall: ");
            final MutableLiveData<NetworkResponse<ApiList<Movie>>> livedata = new MutableLiveData<>();
            movieService.getVideos(query).enqueue(new Callback<ApiList<Movie>>() {
                @Override
                public void onResponse(@NonNull Call<ApiList<Movie>> call, @NonNull Response<ApiList<Movie>> response) {
                    Timber.d("onResponse");
                    livedata.setValue(new NetworkResponse<>(response));
                }

                @Override
                public void onFailure(@NonNull Call<ApiList<Movie>> call, @NonNull Throwable t) {
                    Timber.d("onFailure");
                    livedata.setValue(new NetworkResponse<>(t));
                }
            });
            return livedata;
        }
    }

//    @SuppressWarnings("unused")
//    private boolean isOnline() {
//        ConnectivityManager cm =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        return netInfo != null && netInfo.isConnectedOrConnecting();
//    }


}
