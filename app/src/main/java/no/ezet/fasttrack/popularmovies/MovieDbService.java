package no.ezet.fasttrack.popularmovies;

import no.ezet.fasttrack.popularmovies.models.MovieList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieDbService {

    @GET("movie/popular/")
    Call<MovieList> getPopular(@Query("api_key") String apiKey);

}
