package no.ezet.fasttrack.popularmovies.view;

import android.os.Bundle;

import no.ezet.fasttrack.popularmovies.viewmodel.MovieListViewModel;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailFragment} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@SuppressWarnings("ConstantConditions")
public class MovieListFragment extends MovieListBaseFragment<MovieListViewModel> {

    public static final String ARG_MODE = "ARG_MODE";

    public static MovieListFragment create(int mode) {
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        MovieListFragment fragment = new MovieListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected Class<MovieListViewModel> getViewModelClass() {
        return MovieListViewModel.class;
    }

    @Override
    protected void setupViewModel(MovieListViewModel viewModel) {
        viewModel.setListType(getArguments().getInt(ARG_MODE));
        super.setupViewModel(viewModel);

    }
}


