package no.ezet.fasttrack.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.model.MovieList;
import no.ezet.fasttrack.popularmovies.network.NetworkResource;
import no.ezet.fasttrack.popularmovies.network.NetworkResponse;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.service.IMovieService;
import no.ezet.fasttrack.popularmovies.service.ImageService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MovieRepository {

    private static final String QUERY_POPULAR = "popular";
    private static final String QUERY_UPCOMING = "upcoming";
    private static final String QUERY_TOPRATED = "toprated";
    private IMovieService movieService;
    private ImageService imageService;

    @Inject
    MovieRepository(IMovieService movieService, ImageService imageService) {
        this.movieService = movieService;
        this.imageService = imageService;
    }

    @NonNull
    public LiveData<Resource<MovieList>> getPopularMovies() {
        return getMovies(QUERY_POPULAR);
    }

    @NonNull
    public LiveData<Resource<MovieList>> getUpcomingMovies() {
        return getMovies(QUERY_UPCOMING);
    }

    @NonNull
    public LiveData<Resource<MovieList>> getTopRatedMovies() {
        return getMovies(QUERY_TOPRATED);
    }

    private LiveData<Resource<MovieList>> getMovies(String query) {
        return new NetworkBoundResource(query).getAsLiveData();
    }

    private class NetworkBoundResource extends NetworkResource<MovieList, MovieList> {

        private String query;
        private MovieList item;

        private NetworkBoundResource(String query) {
            this.query = query;
        }

        @Override
        protected void saveCallResult(MovieList item) {
            this.item = item;
        }

        @Override
        protected boolean shouldFetch(MovieList data) {
            return true;
        }

        @Override
        protected LiveData<MovieList> loadFromDb() {
            MutableLiveData<MovieList> data = new MutableLiveData<>();
            data.setValue(item);
            return data;
        }

        @Override
        protected LiveData<NetworkResponse<MovieList>> createCall() {
            final MutableLiveData<NetworkResponse<MovieList>> livedata = new MutableLiveData<>();
            movieService.getMovies(query).enqueue(new Callback<MovieList>() {
                @Override
                public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                    Timber.d("onResponse");
                    livedata.setValue(new NetworkResponse<>(response));
                }

                @Override
                public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
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
