package no.ezet.fasttrack.popularmovies.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import no.ezet.fasttrack.popularmovies.view.MainActivity;
import no.ezet.fasttrack.popularmovies.view.MovieDetailActivity;
import no.ezet.fasttrack.popularmovies.view.MovieDetailFragment;
import no.ezet.fasttrack.popularmovies.view.MovieListFragment;

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract MainActivity contributeMovieListActivity();

    @ContributesAndroidInjector
    abstract MovieDetailActivity contributeMovieDetailActivity();

    @ContributesAndroidInjector
    abstract MovieDetailFragment contributeMovieDetailFragment();

    @ContributesAndroidInjector
    abstract MovieListFragment contributeMovieListFragment();
}
