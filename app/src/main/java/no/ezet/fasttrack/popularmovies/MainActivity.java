package no.ezet.fasttrack.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import no.ezet.fasttrack.popularmovies.models.MovieList;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String apiKey = "f1b9458c5a22388abc326bc55eab3216";
    private String baseUrl = "https://api.themoviedb.org/3";
    private String apiKeyQuery = "api_key=";
    private String popularMovies = baseUrl + "movie/popular/" + "?" + apiKeyQuery + apiKey;
    private String topRatedMovies = baseUrl + "movie/top_rated/" + "?" + apiKeyQuery + apiKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchPopularMoviesTask().execute();


    }

    public class FetchPopularMoviesTask extends AsyncTask<String, Void, MovieList> {

        @Override
        protected MovieList doInBackground(String... params) {
            if (!isOnline()) return null;
            MovieDbService movieService = createMovieService();
            MovieList movies = null;
            try {
                movies = movieService.getPopular(apiKey).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movies;
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private MovieDbService createMovieService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(MovieDbService.class);
    }
}
