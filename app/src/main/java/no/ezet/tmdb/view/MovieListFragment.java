package no.ezet.tmdb.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import no.ezet.tmdb.viewmodel.MovieListItem;
import no.ezet.tmdb.viewmodel.MovieListViewModel;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailFragment} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@SuppressWarnings("ConstantConditions")
public class MovieListFragment extends MovieListBaseFragment {

    public static final String ARG_MODE = "ARG_MODE";

    public int mode;
    private MovieListViewModel viewModel;

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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieListViewModel.class);
        viewModel.setListType(getArguments().getInt(ARG_MODE));
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


