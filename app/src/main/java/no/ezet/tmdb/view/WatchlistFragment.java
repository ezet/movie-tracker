package no.ezet.tmdb.view;

import no.ezet.tmdb.viewmodel.MovieListBaseViewModel;
import no.ezet.tmdb.viewmodel.WatchlistViewModel;

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


