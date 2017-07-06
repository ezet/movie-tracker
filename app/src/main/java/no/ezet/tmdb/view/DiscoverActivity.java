package no.ezet.tmdb.view;

import android.app.SearchManager;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import no.ezet.tmdb.R;
import no.ezet.tmdb.api.ApiService;
import no.ezet.tmdb.api.Mdb3Api;
import no.ezet.tmdb.api.model.Session;
import no.ezet.tmdb.network.Resource;
import no.ezet.tmdb.viewmodel.MovieListItem;
import timber.log.Timber;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailFragment} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class DiscoverActivity extends AppCompatActivity implements LifecycleRegistryOwner, HasSupportFragmentInjector, MovieListRecyclerViewAdapter.FragmentListener, NavigationView.OnNavigationItemSelectedListener, DiscoverListsFragment.TabLayoutHost {

    public static final String BOTTOM_NAV_TRANSITION_NAME = "BOTTOM_NAV_TRANSITION_NAME";
    private static final String APP_BAR_TRANSITION_NAME = "APP_BAR_TRANSITION_NAME";
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    Mdb3Api movieService;

    @Inject
    ApiService apiService;

    private boolean twoPane;
    private AppBarLayout appBarLayout;
    private BottomNavigationView bottomNavigation;
    private Fragment discoverListsFragment;
    private Fragment filterFragment;
    private Fragment favoriteListFragment;
    private Fragment watchlistFragment;
    private Fragment ratedListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        supportPostponeEnterTransition();

        doAuth();

        if (findViewById(R.id.movie_detail_container) != null) {
            twoPane = true;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        ViewCompat.setTransitionName(appBarLayout, APP_BAR_TRANSITION_NAME);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        ViewCompat.setTransitionName(bottomNavigation, BOTTOM_NAV_TRANSITION_NAME);
        bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.bnav_lists:
                    gotoDiscoverLists();
                    break;
                case R.id.bnav_filter:
                    gotoFilter();
                    break;
                case R.id.bnav_favorites:
                    gotoFavorites();
                    break;
                case R.id.bnav_watchlist:
                    gotoWatchList();
                    break;
                case R.id.bnav_rated:
                    gotoRatedList();
                    break;
            }
            return true;
        });

        if (savedInstanceState == null) gotoDiscoverLists();
    }


    private void doAuth() {
        LiveData<Resource<Session>> authenticate = apiService.authenticate(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        apiService.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void gotoDiscoverLists() {
        if (discoverListsFragment == null) discoverListsFragment = DiscoverListsFragment.create();
        setRootFragment(discoverListsFragment);
    }

    private void gotoFilter() {
        if (filterFragment == null || filterFragment.getView() == null) {
            filterFragment = FilterFragment.create();
        }
        setRootFragment(filterFragment);
    }

    private void gotoFavorites() {
        if (favoriteListFragment == null) {
            favoriteListFragment = FavoriteListFragment.create();
        }
        setRootFragment(favoriteListFragment);
    }

    private void gotoWatchList() {
        if (watchlistFragment == null) {
            watchlistFragment = WatchlistFragment.create();
        }
        setRootFragment(watchlistFragment);
    }

    private void gotoRatedList() {
        if (ratedListFragment == null) {
            ratedListFragment = RatedListFragment.create();
        }
        setRootFragment(ratedListFragment);

    }

    private void setRootFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commitAllowingStateLoss();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        search(intent);
    }

    private void search(Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Timber.d("handleIntent: " + query);
            SearchFragment searchFragment = SearchFragment.create(query);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_container, searchFragment)
                    .commitAllowingStateLoss();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.discover_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setChecked(true);
        if (id == R.id.nav_movies) {
            DiscoverListsFragment fragment = DiscoverListsFragment.create();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        } else if (id == R.id.nav_tv_shows) {

        } else if (id == R.id.nav_people) {

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onItemClick(View view, MovieListItem movie) {
        if (twoPane) {
            MovieDetailFragment fragment = MovieDetailFragment.create(movie.id, movie.posterPath);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                fragment.setEnterTransition(new Fade());
                fragment.setSharedElementEnterTransition(new ChangeBounds().setDuration(100));
            }
            getSupportFragmentManager().beginTransaction()
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addSharedElement(view, String.valueOf(movie.id))
                    .addToBackStack(null)
                    .replace(R.id.movie_detail_container, fragment)
                    .commitAllowingStateLoss();
        } else {
            List<Pair<View, String>> pairs = new ArrayList<>();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                View statusBar = findViewById(android.R.id.statusBarBackground);
                if (statusBar != null)
                    pairs.add(Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME));
                View navigationBar = findViewById(android.R.id.navigationBarBackground);
                if (navigationBar != null)
                    pairs.add(Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME));
            }
            pairs.add(Pair.create(view, String.valueOf(movie.id)));
            pairs.add(Pair.create(bottomNavigation, BOTTOM_NAV_TRANSITION_NAME));
            pairs.add(Pair.create(appBarLayout, APP_BAR_TRANSITION_NAME));
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.id);
            intent.putExtra(MovieDetailActivity.EXTRA_POSTER_PATH, movie.posterPath);
            Pair<View, String>[] sharedElements = pairs.toArray(new Pair[pairs.size()]);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElements);
            startActivity(intent, options.toBundle());
        }

    }


    @Override
    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }
}

