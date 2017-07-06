package no.ezet.tmdb.view;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;

import no.ezet.tmdb.viewmodel.FavoriteListViewModel;
import no.ezet.tmdb.viewmodel.MovieListBaseViewModel;

public class FavoriteListFragment extends MovieListBaseFragment {

    public static FavoriteListFragment create() {
        return new FavoriteListFragment();
    }

    @Override
    protected MovieListBaseViewModel getViewModel(ViewModelProvider.Factory viewModelFactory) {
        return ViewModelProviders.of(this, viewModelFactory).get(FavoriteListViewModel.class);
    }

}


