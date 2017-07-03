package no.ezet.fasttrack.popularmovies.viewmodel;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.api.model.ApiList;
import no.ezet.fasttrack.popularmovies.api.model.Movie;
import no.ezet.fasttrack.popularmovies.api.ApiService;
import no.ezet.fasttrack.popularmovies.service.PreferenceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchMoviesViewModel extends MovieListBaseViewModel {

    private final ApiService apiService;
    private final PreferenceService preferenceService;
    private String query;

    @Inject
    SearchMoviesViewModel(ApiService apiService, PreferenceService preferenceService) {
        this.apiService = apiService;
        this.preferenceService = preferenceService;
    }

    @Override
    public void loadMovies() {
        loading.setValue(true);
        apiService.search(query).enqueue(new Callback<ApiList<Movie>>() {
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
