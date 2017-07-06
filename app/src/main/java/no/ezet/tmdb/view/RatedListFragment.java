package no.ezet.tmdb.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import no.ezet.tmdb.viewmodel.MovieListItem;
import no.ezet.tmdb.viewmodel.RatedListViewModel;

public class RatedListFragment extends MovieListBaseFragment {

    private RatedListViewModel viewModel;

    public static RatedListFragment create() {
        return new RatedListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RatedListViewModel.class);
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
