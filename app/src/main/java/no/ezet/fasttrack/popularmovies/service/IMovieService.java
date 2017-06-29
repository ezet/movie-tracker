package no.ezet.fasttrack.popularmovies.service;

import android.support.annotation.Nullable;

import no.ezet.fasttrack.popularmovies.model.ApiList;
import no.ezet.fasttrack.popularmovies.model.Genre;
import no.ezet.fasttrack.popularmovies.model.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMovieService {

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
}
