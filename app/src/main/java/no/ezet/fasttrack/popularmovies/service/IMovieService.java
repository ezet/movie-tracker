package no.ezet.fasttrack.popularmovies.service;

import no.ezet.fasttrack.popularmovies.model.ApiList;
import no.ezet.fasttrack.popularmovies.model.MovieList;
import no.ezet.fasttrack.popularmovies.model.MovieReview;
import no.ezet.fasttrack.popularmovies.model.MovieTrailer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IMovieService {

    @GET("movie/{sortBy}")
    Call<MovieList> getVideos(@Path("sortBy") String sortBy);

    @GET("movie/{movieId}/videos")
    Call<ApiList<MovieTrailer>> getVideos(@Path("movieId") int movieId);

    @GET("movie/{movieId}/reviews")
    Call<ApiList<MovieReview>> getReviews(@Path("movieId") int movieId);

}
