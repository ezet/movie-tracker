package no.ezet.fasttrack.popularmovies.view;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.viewmodel.MovieListViewModel;
import timber.log.Timber;

class DiscoverPageAdapter extends FragmentPagerAdapter {

    private final Resources resources;

    DiscoverPageAdapter(Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int resId;
        switch (position) {
            case MovieListViewModel.POPULAR:
                resId = R.string.title_popular;
                break;
            case MovieListViewModel.UPCOMING:
                resId = R.string.title_upcoming;
                break;
            case MovieListViewModel.TOP_RATED:
                resId = R.string.title_top_rated;
                break;
            case MovieListViewModel.NOW_PLAYING:
                resId = R.string.title_now_playing;
                break;
            default:
                throw new IllegalArgumentException();
        }
        return resources.getString(resId);
    }

    @Override
    public Fragment getItem(int i) {
        Timber.d("getItem: " + i);
        switch (i) {
            case MovieListViewModel.POPULAR:
                return MovieListFragment.create(MovieListViewModel.POPULAR);
            case MovieListViewModel.UPCOMING:
                return MovieListFragment.create(MovieListViewModel.UPCOMING);
            case MovieListViewModel.TOP_RATED:
                return MovieListFragment.create(MovieListViewModel.TOP_RATED);
            case MovieListViewModel.NOW_PLAYING:
                return MovieListFragment.create(MovieListViewModel.NOW_PLAYING);
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
