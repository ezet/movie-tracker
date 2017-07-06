package no.ezet.tmdb.view;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;

import no.ezet.tmdb.viewmodel.WatchlistViewModel;

@SuppressWarnings("ConstantConditions")
public class WatchlistFragment extends MovieListBaseFragment<WatchlistViewModel> {

    public static WatchlistFragment create() {
        return new WatchlistFragment();
    }

    @Override
    protected WatchlistViewModel getViewModel(ViewModelProvider.Factory viewModelFactory) {
        return ViewModelProviders.of(this, viewModelFactory).get(WatchlistViewModel.class);
    }
}


