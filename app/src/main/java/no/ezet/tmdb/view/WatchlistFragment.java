package no.ezet.tmdb.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import no.ezet.tmdb.viewmodel.MovieListItem;
import no.ezet.tmdb.viewmodel.WatchlistViewModel;

@SuppressWarnings("ConstantConditions")
public class WatchlistFragment extends MovieListBaseFragment {

    private WatchlistViewModel viewModel;

    public static WatchlistFragment create() {
        return new WatchlistFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WatchlistViewModel.class);
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


