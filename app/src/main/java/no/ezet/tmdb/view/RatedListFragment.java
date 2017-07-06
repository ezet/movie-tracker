package no.ezet.tmdb.view;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;

import no.ezet.tmdb.viewmodel.RatedListViewModel;

public class RatedListFragment extends MovieListBaseFragment<RatedListViewModel> {

    public static RatedListFragment create() {
        return new RatedListFragment();
    }

    @Override
    protected RatedListViewModel getViewModel(ViewModelProvider.Factory viewModelFactory) {
        return ViewModelProviders.of(this, viewModelFactory).get(RatedListViewModel.class);
    }
}
