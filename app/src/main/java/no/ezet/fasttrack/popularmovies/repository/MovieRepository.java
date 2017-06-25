package no.ezet.fasttrack.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.db.MovieCacheDao;
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

    private final IMovieService movieService;
    private final MovieCacheDao movieCacheDao;

    @Inject
    MovieRepository(IMovieService movieService, MovieCacheDao movieCacheDao) {
        this.movieService = movieService;
        this.movieCacheDao = movieCacheDao;
    }

    @NonNull
    public LiveData<Resource<Movie>> getMovie(int id) {
        return Transformations.map(movieCacheDao.getById(id), Resource::success);
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getPopularMovies() {
        return getMovies(MovieListResource.QUERY_POPULAR);
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getUpcomingMovies() {
        return getMovies(MovieListResource.QUERY_UPCOMING);
    }

    @NonNull
    public LiveData<Resource<List<Movie>>> getTopRatedMovies() {
        return getMovies(MovieListResource.QUERY_TOP_RATED);
    }

    @NonNull
    private LiveData<Resource<List<Movie>>> getMovies(String query) {
        return new MovieListResource(query, movieCacheDao, movieService).getAsLiveData();
    }

    private static class MovieListResource extends NetworkResource<List<Movie>, ApiList<Movie>> {

        private static final String QUERY_POPULAR = "popular";
        private static final String QUERY_UPCOMING = "upcoming";
        private static final String QUERY_TOP_RATED = "top_rated";
        private static boolean[] cached = new boolean[3];
        private MovieCacheDao movieCacheDao;
        private IMovieService movieService;
        private String query;

        private MovieListResource(String query, MovieCacheDao movieCacheDao, IMovieService movieService) {
            this.query = query;
            this.movieCacheDao = movieCacheDao;
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
                    case QUERY_TOP_RATED:
                        movie.setType(Movie.TOP_RATED);
                        break;
                }
            }
            cached[movies.results.get(0).getType()] = true;
            movieCacheDao.insert(movies.results);
        }

        @Override
        protected boolean shouldFetch(List<Movie> data) {
            return data.size() == 0 || !cached[data.get(0).getType()];
        }

        @Override
        protected LiveData<List<Movie>> loadFromDb() {
            Timber.d("loadFromDb: ");
            switch (query) {
                case QUERY_POPULAR:
                    return movieCacheDao.getPopular();
                case QUERY_UPCOMING:
                    return movieCacheDao.getUpcoming();
                case QUERY_TOP_RATED:
                    return movieCacheDao.getTopRated();
                default:
                    throw new IllegalArgumentException("Cannot execute: " + query);
            }
        }

        @Override
        protected LiveData<NetworkResponse<ApiList<Movie>>> createCall() {
            Timber.d("createCall: ");
            final MutableLiveData<NetworkResponse<ApiList<Movie>>> networkResponse = new MutableLiveData<>();
            movieService.getMovies(query).enqueue(new Callback<ApiList<Movie>>() {
                @Override
                public void onResponse(@NonNull Call<ApiList<Movie>> call, @NonNull Response<ApiList<Movie>> response) {
                    Timber.d("onResponse");
                    networkResponse.setValue(new NetworkResponse<>(response));
                }

                @Override
                public void onFailure(@NonNull Call<ApiList<Movie>> call, @NonNull Throwable t) {
                    Timber.d("onFailure");
                    networkResponse.setValue(new NetworkResponse<>(t));
                }
            });
            return networkResponse;
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
