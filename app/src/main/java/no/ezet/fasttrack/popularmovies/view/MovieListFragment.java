package no.ezet.fasttrack.popularmovies.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.ezet.fasttrack.popularmovies.viewmodel.MovieListViewModel;
import timber.log.Timber;

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

    public int mode;

    public static MovieListFragment create(int mode) {
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        MovieListFragment fragment = new MovieListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = getArguments().getInt(ARG_MODE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected Class<MovieListViewModel> getViewModelClass() {
        return MovieListViewModel.class;
    }

    @Override
    protected void setupViewModel(MovieListViewModel viewModel) {
        viewModel.setListType(getArguments().getInt(ARG_MODE));
    }
}


