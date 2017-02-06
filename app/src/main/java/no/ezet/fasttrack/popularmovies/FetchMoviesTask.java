package no.ezet.fasttrack.popularmovies;

import android.os.AsyncTask;
import android.util.Log;
import no.ezet.fasttrack.popularmovies.model.MovieList;
import no.ezet.fasttrack.popularmovies.service.IMovieService;

import java.io.IOException;

public class FetchMoviesTask extends AsyncTask<String, Void, MovieList> {
    public final static String TAG = FetchMoviesTask.class.getSimpleName();

    private IMovieService movieService;
    private AsyncTaskCompleteListener<MovieList> listener;

    FetchMoviesTask(IMovieService movieService, AsyncTaskCompleteListener<MovieList> listener) {
        this.movieService = movieService;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        listener.onPreExecute();
    }

    @Override
    protected MovieList doInBackground(String... params) {
        String sortBy = params[0];
//        if (!isOnline()) return null;
        MovieList movies = null;
        try {
            movies = movieService.getMovies(sortBy).execute().body();
        } catch (IOException e) {
            Log.w(TAG, "Could not execute task.");
        }
        return movies;
    }

    @Override
    protected void onPostExecute(MovieList movieList) {
        listener.onPostExecute(movieList);
    }


}
