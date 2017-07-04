package no.ezet.fasttrack.popularmovies.view;

import no.ezet.fasttrack.popularmovies.viewmodel.RatedListViewModel;

public class RatedListFragment extends MovieListBaseFragment<RatedListViewModel> {

    public static RatedListFragment create() {
        return new RatedListFragment();
    }

    @Override
    protected Class<RatedListViewModel> getViewModelClass() {
        return RatedListViewModel.class;
    }
}
