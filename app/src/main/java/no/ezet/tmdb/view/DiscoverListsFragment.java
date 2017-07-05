package no.ezet.tmdb.view;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.ezet.tmdb.R;
import no.ezet.tmdb.viewmodel.MovieListViewModel;
import timber.log.Timber;

public class DiscoverListsFragment extends Fragment {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private TabLayoutHost host;
    private ViewPager viewPager;

    public static DiscoverListsFragment create() {
        return new DiscoverListsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        host = (TabLayoutHost) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discover_lists, container, false);
        viewPager = (ViewPager) root.findViewById(R.id.view_pager);
        viewPager.setAdapter(new DiscoverPageAdapter(getResources(), getActivity().getSupportFragmentManager()));

        appBarLayout = host.getAppBarLayout();
        tabLayout = (TabLayout) inflater.inflate(R.layout.tablayout_discover_lists, appBarLayout, false);
        tabLayout.setupWithViewPager(viewPager);

        appBarLayout.addView(tabLayout);

        return root;
    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView: ");
        super.onDestroyView();
        appBarLayout.removeView(tabLayout);
    }

    @Override
    public void onDestroy() {
        Timber.d("onDestroy: ");
        super.onDestroy();
        viewPager.setAdapter(null);
        tabLayout.setupWithViewPager(null);
        appBarLayout = null;
        tabLayout = null;
    }

    public interface TabLayoutHost {
        AppBarLayout getAppBarLayout();
    }

    private static class DiscoverPageAdapter extends FragmentPagerAdapter {

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
}
