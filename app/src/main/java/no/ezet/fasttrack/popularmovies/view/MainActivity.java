package no.ezet.fasttrack.popularmovies.view;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.View;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.viewmodel.MoviesViewModel;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailFragment} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MainActivity extends AppCompatActivity implements LifecycleRegistryOwner, HasSupportFragmentInjector, MovieListFragment.FragmentListener {

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    private MoviesViewModel moviesViewModel;
    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moviesViewModel = ViewModelProviders.of(this, viewModelFactory).get(MoviesViewModel.class);
        if (findViewById(R.id.movie_detail_container) != null) {
            twoPane = true;
        }

        if (savedInstanceState == null) {
            MovieListFragment fragment = MovieListFragment.create();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        moviesViewModel.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        moviesViewModel.onSaveInstanceState(outState);
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
    public void onItemClick(View view, Movie movie) {
        MovieDetailFragment fragment = MovieDetailFragment.create(movie.getId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            fragment.setEnterTransition(new Fade());
            fragment.setSharedElementEnterTransition(new ChangeBounds().setDuration(100));
        }
        ViewCompat.setTransitionName(view, "transition");

        int target = twoPane ? R.id.movie_detail_container : R.id.root_container;
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addSharedElement(view, "transition")
                .addToBackStack(null)
                .replace(target, fragment)
                .commitAllowingStateLoss();
    }
}

