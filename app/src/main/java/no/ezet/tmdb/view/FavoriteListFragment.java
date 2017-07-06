package no.ezet.tmdb.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import no.ezet.tmdb.viewmodel.FavoriteListViewModel;
import no.ezet.tmdb.viewmodel.MovieListItem;

public class FavoriteListFragment extends MovieListBaseFragment {


    private FavoriteListViewModel viewModel;

    public static FavoriteListFragment create() {
        return new FavoriteListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FavoriteListViewModel.class);
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


