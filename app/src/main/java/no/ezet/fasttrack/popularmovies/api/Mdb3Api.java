package no.ezet.fasttrack.popularmovies.api;

import android.support.annotation.Nullable;

import no.ezet.fasttrack.popularmovies.api.model.AccountDetails;
import no.ezet.fasttrack.popularmovies.api.model.ApiList;
import no.ezet.fasttrack.popularmovies.api.model.Genre;
import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.api.model.PostResponse;
import no.ezet.fasttrack.popularmovies.api.model.RequestToken;
import no.ezet.fasttrack.popularmovies.api.model.Session;
import no.ezet.fasttrack.popularmovies.api.requestbody.PostFavoriteBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Mdb3Api {

    @GET("movie/{sortBy}")
    Call<ApiList<Movie>> getMovies(@Path("sortBy") String sortBy);

    @GET("movie/{movieId}")
    Call<Movie> getDetailsWithAppend(@Path("movieId") int movieId, @Query("append_to_response") String resources);

    @GET("search/movie")
    Call<ApiList<Movie>> search(@Query("query") String query);

    @GET("genre/movie/list")
    Call<ApiList<Genre>> getGenres();

    @GET("discover/movie/")
    Call<ApiList<Movie>> filter(@Query("with_genres") @Nullable Integer genreId, @Query("sort_by") String sortBy, @Query("primary_release_year") @Nullable Integer releaseYear);

    @GET("account/{accountId}/favorite/movies")
    Call<ApiList<Movie>> getFavoriteMovies(@Path("accountId") int accountId, @Query("session_id") String sessionId);

    @GET("authentication/token/new")
    Call<RequestToken> createRequestToken();

    @GET("authentication/session/new")
    Call<Session> createSession(@Query("request_token") String requestToken);

    @GET("account")
    Call<AccountDetails> getAccountDetails(@Query("session_id") String sessionId);

    @POST("account/{accountId}/favorite")
    Call<PostResponse> setFavorite(@Path("accountId") int accountId, @Query("session_id") String sessionId, @Body PostFavoriteBody postFavoriteBody);

    @GET("account/{accountId}/watchlist/movies")
    Call<ApiList<Movie>> getWatchlist(@Path("accountId") int accountId, @Query("session_id") String sessionId);
}
