package no.ezet.fasttrack.popularmovies.services;

import no.ezet.fasttrack.popularmovies.models.MovieList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IMovieService {

    static String apiKey = "f1b9458c5a22388abc326bc55eab3216";
    static  String baseUrl = "https://api.themoviedb.org/3/";

    @GET("movie/popular/")
    Call<MovieList> getPopular(@Query("api_key") String apiKey);

}
