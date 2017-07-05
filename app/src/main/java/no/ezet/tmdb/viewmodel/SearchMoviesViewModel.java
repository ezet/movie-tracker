package no.ezet.tmdb.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import no.ezet.tmdb.api.ApiService;
import no.ezet.tmdb.api.model.ApiList;
import no.ezet.tmdb.api.model.Movie;
import no.ezet.tmdb.network.Resource;
import no.ezet.tmdb.service.PreferenceService;
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
    public LiveData<Resource<List<Movie>>> loadMovies() {
        MutableLiveData<Resource<List<Movie>>> liveData = new MutableLiveData<>();
        apiService.search(query).enqueue(new Callback<ApiList<Movie>>() {
            @Override
            public void onResponse(@NonNull Call<ApiList<Movie>> call, @NonNull Response<ApiList<Movie>> response) {
                liveData.setValue(Resource.success(response.body().results));
            }

            @Override
            public void onFailure(@NonNull Call<ApiList<Movie>> call, @NonNull Throwable t) {
                liveData.setValue(Resource.error(t.getLocalizedMessage(), null));
            }
        });
        return liveData;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
