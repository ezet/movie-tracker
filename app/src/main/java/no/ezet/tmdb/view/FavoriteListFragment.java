package no.ezet.tmdb.view;

import no.ezet.tmdb.viewmodel.FavoriteListViewModel;
import no.ezet.tmdb.viewmodel.MovieListBaseViewModel;

public class FavoriteListFragment extends MovieListBaseFragment {

    public static FavoriteListFragment create() {
        return new FavoriteListFragment();
    }

    @Override
    protected Class<? extends MovieListBaseViewModel> getViewModelClass() {
        return FavoriteListViewModel.class;
    }

}


