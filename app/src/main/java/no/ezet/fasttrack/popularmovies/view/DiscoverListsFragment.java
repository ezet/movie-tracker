package no.ezet.fasttrack.popularmovies.view;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.ezet.fasttrack.popularmovies.R;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
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
        Timber.d("onAttach: ");
        super.onAttach(context);
        host = (TabLayoutHost) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("onCreateView: ");
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

}
