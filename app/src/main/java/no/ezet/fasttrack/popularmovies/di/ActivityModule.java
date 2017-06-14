package no.ezet.fasttrack.popularmovies.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import no.ezet.fasttrack.popularmovies.view.MovieDetailActivity;
import no.ezet.fasttrack.popularmovies.view.MovieDetailFragment;
import no.ezet.fasttrack.popularmovies.view.MovieListActivity;

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector()
    abstract MovieListActivity contributeMovieListActivity();

    @ContributesAndroidInjector
    abstract MovieDetailFragment contributeMovieDetailFragment();

    @ContributesAndroidInjector
    abstract MovieDetailActivity contributeMovieDetailActivity();

}
