package no.ezet.fasttrack.popularmovies.view;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.viewmodel.MovieListItem;
import timber.log.Timber;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailFragment} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class DiscoverActivity extends AppCompatActivity implements LifecycleRegistryOwner, HasSupportFragmentInjector, MovieListBaseFragment.FragmentListener, NavigationView.OnNavigationItemSelectedListener {

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
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


        if (savedInstanceState == null) setupFragment();
    }

    private void setupFragment() {
        MovieListFragment fragment = MovieListFragment.create();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Timber.d("onCreateOptionsMenu: ");
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        item.setChecked(true);
        if (id == R.id.nav_discover) {
            MovieListFragment fragment = MovieListFragment.create();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commitAllowingStateLoss();
        } else if (id == R.id.nav_favorites) {
            FavoriteListFragment fragment = FavoriteListFragment.create();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commitAllowingStateLoss();

        } else if (id == R.id.nav_search) {

        } else if (id == R.id.nav_settings) {

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
            MovieDetailFragment fragment = MovieDetailFragment.create(movie.id);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                fragment.setEnterTransition(new Fade());
                fragment.setSharedElementEnterTransition(new ChangeBounds().setDuration(100));
            }
            ViewCompat.setTransitionName(view, "transition");
            getSupportFragmentManager().beginTransaction()
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addSharedElement(view, "transition")
                    .addToBackStack(null)
                    .replace(R.id.movie_detail_container, fragment)
                    .commitAllowingStateLoss();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.id);
            startActivity(intent);
        }
    }


}

