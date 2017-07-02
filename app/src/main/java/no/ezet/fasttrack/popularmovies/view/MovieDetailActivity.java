package no.ezet.fasttrack.popularmovies.view;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import no.ezet.fasttrack.popularmovies.R;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailFragment} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieDetailActivity extends AppCompatActivity implements LifecycleRegistryOwner, HasSupportFragmentInjector {

    public static final String EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID";
    public static final String EXTRA_POSTER_PATH = "EXTRA_POSTER_PATH";
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (findViewById(R.id.movie_detail_container) != null) {
            twoPane = true;
        }

        setupFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                supportFinishAfterTransition();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupFragment() {
        int movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);
        String posterPath = getIntent().getStringExtra(EXTRA_POSTER_PATH);
        MovieDetailFragment fragment = MovieDetailFragment.create(movieId, posterPath);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            fragment.setEnterTransition(new Fade());
//            fragment.setSharedElementEnterTransition(new ChangeBounds().setDuration(100));
//        }
//        ViewCompat.setTransitionName(view, "transition");
        getSupportFragmentManager().beginTransaction()
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .addSharedElement(view, "transition")
                .replace(R.id.movie_detail_container, fragment)
                .commitAllowingStateLoss();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
}

