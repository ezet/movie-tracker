package no.ezet.fasttrack.popularmovies.view;

import no.ezet.fasttrack.popularmovies.viewmodel.MovieListBaseViewModel;
import no.ezet.fasttrack.popularmovies.viewmodel.WatchListViewModel;

@SuppressWarnings("ConstantConditions")
public class WatchListFragment extends MovieListBaseFragment {

    public static WatchListFragment create() {
        return new WatchListFragment();
    }

    @Override
    protected Class<? extends MovieListBaseViewModel> getViewModelClass() {
        return WatchListViewModel.class;
    }

}


