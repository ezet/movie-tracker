package no.ezet.tmdb.view;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import javax.inject.Inject;

import no.ezet.tmdb.api.Mdb3Api;
import no.ezet.tmdb.viewmodel.SearchMoviesViewModel;

public class SearchFragment extends MovieListBaseFragment<SearchMoviesViewModel> {


    public static final String ARG_QUERY = "ARG_QUERY";

    @Inject
    Mdb3Api movieService;
    private String query;

    public static SearchFragment create(String query) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        query = getArguments().getString(ARG_QUERY);
    }

    @Override
    protected SearchMoviesViewModel getViewModel(ViewModelProvider.Factory viewModelFactory) {
        return ViewModelProviders.of(this, viewModelFactory).get(SearchMoviesViewModel.class);
    }

    @Override
    protected void setupViewModel(SearchMoviesViewModel viewModel) {
        viewModel.setQuery(query);
    }


}
