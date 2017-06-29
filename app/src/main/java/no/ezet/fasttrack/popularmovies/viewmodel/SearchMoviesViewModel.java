package no.ezet.fasttrack.popularmovies.viewmodel;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.model.ApiList;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.service.IMovieService;
import no.ezet.fasttrack.popularmovies.service.PreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchMoviesViewModel extends MovieListBaseViewModel {

    private final IMovieService movieService;
    private final PreferenceService preferenceService;
    private String query;

    @Inject
    SearchMoviesViewModel(IMovieService movieService, PreferenceService preferenceService) {
        this.movieService = movieService;
        this.preferenceService = preferenceService;
    }

    @Override
    public void loadMovies() {
        loading.setValue(true);
        movieService.search(query).enqueue(new Callback<ApiList<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<ApiList<Movie>> call, @NonNull Response<ApiList<Movie>> response) {
                List<MovieListItem> items = new ArrayList<>();
                if (response.body() != null) {
                    for (Movie item : response.body().results) {
                        if (!item.getAdult() || preferenceService.getBoolean(R.string.pkey_adult)) {
                            items.add(MovieListItem.create(item));
                        }
                    }
                }
                movies.setValue(items);
                loading.setValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<ApiList<Movie>> call, @NonNull Throwable t) {
                loading.setValue(false);
                errorMessage.setValue(t.getLocalizedMessage());
            }
        });
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
