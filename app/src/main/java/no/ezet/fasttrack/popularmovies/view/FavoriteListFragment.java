package no.ezet.fasttrack.popularmovies.view;

import android.view.Menu;
import android.view.MenuInflater;

import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.viewmodel.FavoriteListViewModel;
import no.ezet.fasttrack.popularmovies.viewmodel.MovieListViewModel;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailFragment} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@SuppressWarnings("ConstantConditions")
public class FavoriteListFragment extends MovieListBaseFragment {

    public static FavoriteListFragment create() {
        return new FavoriteListFragment();
    }

    @Override
    protected Class<? extends MovieListViewModel> getViewModelClass() {
        return FavoriteListViewModel.class;
    }

    @Override
    protected int getSubtitle() {
        return R.string.favorites;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }
}


