package no.ezet.tmdb.view;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import no.ezet.tmdb.R;
import no.ezet.tmdb.api.model.Movie;
import no.ezet.tmdb.viewmodel.MovieDetailsViewModel;
import no.ezet.tmdb.viewmodel.MovieListItem;
import timber.log.Timber;

public class MovieDetailActivity extends AppCompatActivity implements LifecycleRegistryOwner, HasSupportFragmentInjector, MovieListRecyclerViewAdapter.FragmentListener {

    public static final String EXTRA_MOVIE_ID = "EXTRA_MOVIE_ID";
    public static final String EXTRA_POSTER_PATH = "EXTRA_POSTER_PATH";
    private static final String BOTTOM_NAV_TRANSITION_NAME = "BOTTOM_NAV_TRANSITION_NAME";
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private BottomNavigationView bottomNavigation;
    private int movieId;
    private FloatingActionButton favoriteFab;
    private FloatingActionButton bookmarkFab;
    private FloatingActionButton rateFab;
    private MovieDetailsViewModel viewModel;
    private CoordinatorLayout coordinatorLayout;
    private boolean isFavoriteButtonInitialized;
    private boolean isBookmarkButtonInitialized;
    private boolean isRateButtonInitialized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate: ");
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieId = getIntent().getIntExtra(EXTRA_MOVIE_ID, 0);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        bookmarkFab = (FloatingActionButton) findViewById(R.id.fab_bookmark);
        favoriteFab = (FloatingActionButton) findViewById(R.id.fab_favorite);
        rateFab = (FloatingActionButton) findViewById(R.id.fab_rate);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        ViewCompat.setTransitionName(bottomNavigation, BOTTOM_NAV_TRANSITION_NAME);
        bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.bnav_details:
                    gotoDetails();
                    break;
                case R.id.bnav_similar:
                    gotoSimilar();
                    break;
                case R.id.bnav_recommended:
                    gotoRecommended();
                    break;
                case R.id.bnav_reviews:
                    gotoReviews();
                    break;
            }
            return true;
        });
        setupViewModel(movieId);
        setupFavoriteButton();
        setupBookmarkButton();
        setupRateButton();
        gotoDetails();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Timber.d("onNewIntent: ");
        super.onNewIntent(intent);
    }

    private void showFabs() {
        favoriteFab.setVisibility(View.VISIBLE);
        rateFab.setVisibility(View.VISIBLE);
        bookmarkFab.setVisibility(View.VISIBLE);
    }

    private void hideFabs() {
        favoriteFab.setVisibility(View.GONE);
        rateFab.setVisibility(View.GONE);
        bookmarkFab.setVisibility(View.GONE);
    }

    private void setupViewModel(int movieId) {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel.class);
        viewModel.setMovie(movieId);
    }

    private void gotoReviews() {

    }

    private void gotoRecommended() {
        String posterPath = getIntent().getStringExtra(EXTRA_POSTER_PATH);
        RecommendedListFragment fragment = RecommendedListFragment.create(movieId, posterPath);
        setRoot(fragment);
        hideFabs();
    }

    private void gotoSimilar() {
        String posterPath = getIntent().getStringExtra(EXTRA_POSTER_PATH);
        SimilarListFragment fragment = SimilarListFragment.create(movieId, posterPath);
        setRoot(fragment);
        hideFabs();
    }

    private void gotoDetails() {
        String posterPath = getIntent().getStringExtra(EXTRA_POSTER_PATH);
        MovieDetailFragment fragment = MovieDetailFragment.create(movieId, posterPath);
        setRoot(fragment);
        showFabs();
    }

    private void setRoot(Fragment fragment) {
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
//            pairs.add(Pair.create(appBarLayout, APP_BAR_TRANSITION_NAME));
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.id);
        intent.putExtra(MovieDetailActivity.EXTRA_POSTER_PATH, movie.posterPath);
        Pair<View, String>[] sharedElements = pairs.toArray(new Pair[pairs.size()]);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElements);
        startActivity(intent, options.toBundle());
    }


    private void abortBackTransition() {
        ViewCompat.setTransitionName(findViewById(R.id.movie_portrait), null);
    }

    private void setupFavoriteButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_favorite);
        viewModel.getIsFavorite().observe(this, isFavorite -> {
            if (isFavorite != null && isFavorite) {
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp));
                if (isFavoriteButtonInitialized) {
                    Snackbar.make(coordinatorLayout, R.string.favorite_added, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } else {
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
                if (isFavoriteButtonInitialized) {
                    Snackbar.make(coordinatorLayout, R.string.favorite_removed, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
            isFavoriteButtonInitialized = true;
        });
        fab.setOnClickListener(view -> {
            viewModel.toggleFavorite();
            abortBackTransition();
        });
    }

    private void setupBookmarkButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_bookmark);
        viewModel.getIsBookmark().observe(this, isBookmarked -> {
            if (isBookmarked != null && isBookmarked) {
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark_white_24dp));
                if (isBookmarkButtonInitialized) {
                    Snackbar.make(coordinatorLayout, R.string.bookmark_added, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } else {
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_bookmark_border_white_24dp));
                if (isBookmarkButtonInitialized) {
                    Snackbar.make(coordinatorLayout, R.string.bookmark_removed, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
            isBookmarkButtonInitialized = true;
        });
        fab.setOnClickListener(view -> {
            viewModel.toggleBookmark();
            abortBackTransition();
        });
    }


    private void setupRateButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_rate);
        viewModel.getIsRated().observe(this, isRated -> {
            if (isRated != null && isRated) {
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_white_24dp));
                if (isRateButtonInitialized) {
                    Snackbar.make(coordinatorLayout, R.string.rating_successful, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } else {
                fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_border_white_24dp));
                if (isRateButtonInitialized) {
                    Snackbar.make(coordinatorLayout, R.string.rating_deleted, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
            isRateButtonInitialized = true;
        });
        fab.setOnClickListener(view -> {
            abortBackTransition();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            @SuppressLint("InflateParams") View content = getLayoutInflater().inflate(R.layout.dialog_rate, null);
            builder.setView(content);
            SeekBar seekBar = (SeekBar) content.findViewById(R.id.seekbar);
            TextView textView = (TextView) content.findViewById(R.id.tv_rate);
            seekBar.setMax(10);
            seekBar.setKeyProgressIncrement(1);
            final SeekBarChangeListener seekBarChangeListener = new SeekBarChangeListener(textView);
            seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
            builder.setTitle(R.string.dialog_title_rate)
                    .setPositiveButton(R.string.dialog_btn_rate, (dialog, which) -> viewModel.rate(seekBarChangeListener.rating))
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel())
                    .setNeutralButton(R.string.delete, (dialog, which) -> viewModel.deleteRating())
                    .create().show();
            Movie movie = viewModel.getMovie().getValue();
            if (movie != null && movie.getRating() != null) {
                seekBar.setProgress(movie.getRating().intValue());
            }
        });
    }

    private static class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        private final TextView textView;
        public int rating;

        SeekBarChangeListener(TextView textView) {
            this.textView = textView;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            this.rating = progress;
            textView.setText(String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}

