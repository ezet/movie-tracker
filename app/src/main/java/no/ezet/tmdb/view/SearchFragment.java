package no.ezet.tmdb.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import javax.inject.Inject;

import no.ezet.tmdb.api.Mdb3Api;
import no.ezet.tmdb.viewmodel.MovieListItem;
import no.ezet.tmdb.viewmodel.SearchMoviesViewModel;

public class SearchFragment extends MovieListBaseFragment {


    public static final String ARG_QUERY = "ARG_QUERY";

    @Inject
    Mdb3Api movieService;
    private SearchMoviesViewModel viewModel;

    public static SearchFragment create(String query) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchMoviesViewModel.class);
        String query = getArguments().getString(ARG_QUERY);
        viewModel.setQuery(query);
    }

    @Override
    protected LiveData<List<MovieListItem>> getListItems() {
        return viewModel.getMovies();
    }

    @Override
    protected LiveData<Boolean> getIsLoading() {
        return viewModel.getIsLoading();
    }

}
