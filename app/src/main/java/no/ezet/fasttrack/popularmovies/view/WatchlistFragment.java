package no.ezet.fasttrack.popularmovies.view;

import no.ezet.fasttrack.popularmovies.viewmodel.MovieListBaseViewModel;
import no.ezet.fasttrack.popularmovies.viewmodel.WatchlistViewModel;

@SuppressWarnings("ConstantConditions")
public class WatchlistFragment extends MovieListBaseFragment {

    public static WatchlistFragment create() {
        return new WatchlistFragment();
    }

    @Override
    protected Class<? extends MovieListBaseViewModel> getViewModelClass() {
        return WatchlistViewModel.class;
    }

}


