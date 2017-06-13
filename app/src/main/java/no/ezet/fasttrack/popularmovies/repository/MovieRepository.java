package no.ezet.fasttrack.popularmovies.repository;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.model.MovieList;
import no.ezet.fasttrack.popularmovies.service.IMovieService;
import no.ezet.fasttrack.popularmovies.service.ImageService;
import no.ezet.fasttrack.popularmovies.task.RepositoryListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private IMovieService movieService;
    private ImageService imageService;

    @Inject
    public MovieRepository(IMovieService movieService, ImageService imageService) {
        this.movieService = movieService;
        this.imageService = imageService;
    }

    public void getMovies(String query, RepositoryListener<MovieList> listener) {

        listener.onPreExecute();
        movieService.getMovies(query).enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(@NonNull Call<MovieList> call, @NonNull Response<MovieList> response) {
                listener.onPostExecute(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MovieList> call, @NonNull Throwable t) {
                listener.onPostExecute(null);
            }
        });

    }

}
