package no.ezet.tmdb.view;

import no.ezet.tmdb.viewmodel.RatedListViewModel;

public class RatedListFragment extends MovieListBaseFragment<RatedListViewModel> {

    public static RatedListFragment create() {
        return new RatedListFragment();
    }

    @Override
    protected Class<RatedListViewModel> getViewModelClass() {
        return RatedListViewModel.class;
    }
}
