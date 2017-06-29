package no.ezet.fasttrack.popularmovies.view;

import android.os.Bundle;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.service.IMovieService;
import no.ezet.fasttrack.popularmovies.viewmodel.SearchMoviesViewModel;

public class SearchFragment extends MovieListBaseFragment<SearchMoviesViewModel> {


    public static final String ARG_QUERY = "ARG_QUERY";

    @Inject
    IMovieService movieService;
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
    protected void setupViewModel(SearchMoviesViewModel viewModel) {
        viewModel.setQuery(query);
    }

    @Override
    protected Class<SearchMoviesViewModel> getViewModelClass() {
        return SearchMoviesViewModel.class;
    }

}
