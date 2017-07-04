package no.ezet.fasttrack.popularmovies.view;

import no.ezet.fasttrack.popularmovies.viewmodel.FavoriteListViewModel;
import no.ezet.fasttrack.popularmovies.viewmodel.MovieListBaseViewModel;

public class FavoriteListFragment extends MovieListBaseFragment {

    public static FavoriteListFragment create() {
        return new FavoriteListFragment();
    }

    @Override
    protected Class<? extends MovieListBaseViewModel> getViewModelClass() {
        return FavoriteListViewModel.class;
    }

}


