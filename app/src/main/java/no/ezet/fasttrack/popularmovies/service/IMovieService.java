package no.ezet.fasttrack.popularmovies.service;

import android.arch.lifecycle.LiveData;

import no.ezet.fasttrack.popularmovies.model.ApiList;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.db.MovieReview;
import no.ezet.fasttrack.popularmovies.db.MovieTrailer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMovieService {

    @GET("movie/{sortBy}")
    Call<ApiList<Movie>> getMovies(@Path("sortBy") String sortBy);

    @GET("movie/{movieId}/videos")
    Call<ApiList<MovieTrailer>> getVideos(@Path("movieId") int movieId);

    @GET("movie/{movieId}/reviews")
    Call<ApiList<MovieReview>> getReviews(@Path("movieId") int movieId);

    @GET("movie/{movieId}")
    Call<ApiList<MovieReview>> getDetails(@Path("movieId") int movieId);

    @GET("movie/{movieId}")
    Call<Movie> getDetailsWithAppend(@Path("movieId") int movieId, @Query("append_to_response") String resources);

    @GET("search/movie/")
    Call<ApiList<Movie>> search(@Query("query") String query);


}
