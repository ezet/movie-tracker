package no.ezet.fasttrack.popularmovies;

import no.ezet.fasttrack.popularmovies.model.MovieList;
import no.ezet.fasttrack.popularmovies.service.IMovieService;
import no.ezet.fasttrack.popularmovies.service.MovieServiceFactory;
import org.junit.Assert;
import org.junit.Test;
import retrofit2.Response;

public class FetchPopularMovies {

    private static final String API_KEY = "f1b9458c5a22388abc326bc55eab3216";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    @Test
    public void fetch_popularMovies() throws Exception {
        IMovieService service = MovieServiceFactory.getMovieService(BASE_URL, API_KEY);
        Response<MovieList> response = service.getMovies("").execute();
        Assert.assertTrue(response.isSuccessful());
        Assert.assertNotEquals(0, response.body().getTotalResults().longValue());
    }

}