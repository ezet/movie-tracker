package no.ezet.fasttrack.popularmovies.api;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.api.requestbody.PostFavoriteBody;
import no.ezet.fasttrack.popularmovies.api.model.ApiList;
import no.ezet.fasttrack.popularmovies.api.model.PostResponse;
import no.ezet.fasttrack.popularmovies.api.model.Session;
import no.ezet.fasttrack.popularmovies.api.model.Genre;
import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;
import no.ezet.fasttrack.popularmovies.service.PreferenceService;
import retrofit2.Call;
import timber.log.Timber;

@Singleton
public class ApiService {

    private static final String BEARER = "Bearer ";
    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMWI5NDU4YzVhMjIzODhhYmMzMjZiYzU1ZWFiMzIxNiIsInN1YiI6IjU4OGRlODlkOTI1MTQxMTg0NjAwOTE4ZCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.SuMpD9TLWU7BgX6yBKiab7hDsSUDMPqNUTs5R-CP6jM";
    private static LiveData<Resource<Session>> session;
    private final Mdb3Api api3;
    private final Auth3Service auth3Service;
    private final PreferenceService preferenceService;


    @Inject
    public ApiService(Mdb3Api api3, Auth3Service auth3Service, PreferenceService preferenceService) {
        this.api3 = api3;
        this.auth3Service = auth3Service;
        this.preferenceService = preferenceService;
    }

    private String token() {
        return BEARER + TOKEN;
    }

    public Call<ApiList<Movie>> getMovies(@Nullable String sortBy) {
        return api3.getMovies(sortBy);
    }

    @SuppressWarnings("SameParameterValue")
    public Call<Movie> getDetailsWithAppend(int movieId, @Nullable String append) {
        return api3.getDetailsWithAppend(movieId, append);
    }

    public Call<ApiList<Movie>> search(String query) {
        return api3.search(query);
    }

    public Call<ApiList<Genre>> getGenres() {
        return api3.getGenres();
    }

    public Call<ApiList<Movie>> filter(@Nullable Integer genreId, String sortBy, @Nullable Integer releaseYear) {
        return api3.filter(genreId, sortBy, releaseYear);
    }

    public Call<PostResponse> setFavoriteMovie(int movieId, boolean favorite) {
        return api3.setFavorite(id(), session(), new PostFavoriteBody(PostFavoriteBody.MOVIE, movieId, favorite));
    }

    public Call<ApiList<Movie>> getFavoriteMovies() {
        //noinspection ConstantConditions
        if (session.getValue().status != Resource.SUCCESS) {
            Timber.d("getFavoriteMovies: not authenticated");
            return null;
        }
        return api3.getFavoriteMovies(id(), session());
    }

    private String session() {
        return session.getValue().data.sessionId;
    }

    private int id() {
        return preferenceService.getInt(R.string.pk_account_id);
    }

    public LiveData<Resource<Session>> authenticate(Activity activity) {
        session = auth3Service.authenticate(activity);
        return session;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        auth3Service.onActivityResult(requestCode, resultCode, data);
    }

    public Call<ApiList<Movie>> getWatchlist() {
        //noinspection ConstantConditions
        if (session.getValue().status != Resource.SUCCESS) {
            Timber.d("getWatchlist: not authenticated");
            return null;
        }
        return api3.getWatchlist(id(), session());
    }
}
