package no.ezet.tmdb.api;

import android.support.annotation.Nullable;

import no.ezet.tmdb.api.model.AccountDetails;
import no.ezet.tmdb.api.model.ApiList;
import no.ezet.tmdb.api.model.Genre;
import no.ezet.tmdb.api.model.Movie;
import no.ezet.tmdb.api.model.PostResponse;
import no.ezet.tmdb.api.model.RequestToken;
import no.ezet.tmdb.api.model.Session;
import no.ezet.tmdb.api.requestbody.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Mdb3Api {

    @GET("movie/{sortBy}")
    Call<ApiList<Movie>> getMovies(@Path("sortBy") String sortBy);

    @GET("movie/{mediaId}")
    Call<Movie> getDetailsWithAppend(@Path("mediaId") int movieId, @Query("append_to_response") String resources);

    @GET("search/movie")
    Call<ApiList<Movie>> search(@Query("query") String query);

    @GET("genre/movie/list")
    Call<ApiList<Genre>> getGenres();

    @GET("discover/movie/")
    Call<ApiList<Movie>> filter(@Query("with_genres") @Nullable Integer genreId, @Query("sort_by") String sortBy, @Query("primary_release_year") @Nullable Integer releaseYear);

    @GET("account/{accountId}/favorite/movies")
    Call<ApiList<Movie>> getFavoriteMovies(@Path("accountId") int accountId, @Query("session_id") String sessionId);

    @GET("account/{accountId}/rated/movies")
    Call<ApiList<Movie>> getRatedMovies(@Path("accountId") int accountId, @Query("session_id") String sessionId);

    @GET("authentication/token/new")
    Call<RequestToken> createRequestToken();

    @GET("authentication/session/new")
    Call<Session> createSession(@Query("request_token") String requestToken);

    @GET("account")
    Call<AccountDetails> getAccountDetails(@Query("session_id") String sessionId);

    @POST("account/{accountId}/favorite")
    Call<PostResponse> setFavorite(@Path("accountId") int accountId, @Query("session_id") String sessionId, @Body RequestBody requestBody);

    @GET("account/{accountId}/watchlist/movies")
    Call<ApiList<Movie>> getWatchlist(@Path("accountId") int accountId, @Query("session_id") String sessionId);

    @POST("account/{accountId}/watchlist")
    Call<PostResponse> setWatchlist(@Path("accountId") int accountId, @Query("session_id") String sessionId, @Body RequestBody requestBody);

    @POST("movie/{movie_id}/rating")
    Call<PostResponse> rate(@Path("movie_id") int movieId, @Query("session_id") String sessionId, @Body okhttp3.RequestBody requestBody);

    @DELETE("movie/{movie_id}/rating")
    Call<PostResponse> deleteRating(@Path("movie_id") int movieId, @Query("session_id") String sessionId);


}

