package no.ezet.fasttrack.popularmovies.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import no.ezet.fasttrack.popularmovies.view.FavoriteListFragment;
import no.ezet.fasttrack.popularmovies.view.FilterFragment;
import no.ezet.fasttrack.popularmovies.view.MovieDetailFragment;
import no.ezet.fasttrack.popularmovies.view.MovieListFragment;
import no.ezet.fasttrack.popularmovies.view.SearchFragment;
import no.ezet.fasttrack.popularmovies.view.WatchListFragment;

@Module()
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract MovieDetailFragment contributeMovieDetailFragment();

    @ContributesAndroidInjector
    abstract MovieListFragment contributeMovieListFragment();

    @ContributesAndroidInjector
    abstract FavoriteListFragment contributeFavoriteListFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract FilterFragment contributeFilterFragment();

    @ContributesAndroidInjector
    abstract WatchListFragment contributeWatchListFragment();

//    @Provides
//    MovieListFragment.FragmentListener provideMovieListFragmentListener(DiscoverActivity activity) {
//        return activity;
//    }

}
