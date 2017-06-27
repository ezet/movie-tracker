package no.ezet.fasttrack.popularmovies.viewmodel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.model.ApiList;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.service.IMovieService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchMoviesViewModel extends MovieListBaseViewModel {

    private final IMovieService movieService;
    private String query;

    @Inject
    SearchMoviesViewModel(IMovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public void loadMovies() {
        loading.setValue(true);
        movieService.search(query).enqueue(new Callback<ApiList<Movie>>() {
            @Override
            public void onResponse(Call<ApiList<Movie>> call, Response<ApiList<Movie>> response) {
                List<MovieListItem> items = new ArrayList<>();
                for (Movie movie : response.body().results) {
                    items.add(MovieListItem.create(movie));
                }
                movies.setValue(items);
                loading.setValue(false);
            }

            @Override
            public void onFailure(Call<ApiList<Movie>> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue(t.getLocalizedMessage());
            }
        });
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
