package no.ezet.fasttrack.popularmovies;

import no.ezet.fasttrack.popularmovies.models.MovieList;
import no.ezet.fasttrack.popularmovies.services.IMovieService;
import org.junit.Assert;
import org.junit.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FetchPopularMovies {

    private String apiKey = "f1b9458c5a22388abc326bc55eab3216";
    private String baseUrl = "https://api.themoviedb.org/3/";

    @Test
    public void fetch_popularMovies() throws Exception {
        IMovieService service = createMovieService();
        Response<MovieList> response = service.getPopular(apiKey).execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertNotEquals(0, response.body().getTotalResults().longValue());
    }

    private IMovieService createMovieService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(IMovieService.class);
    }


}