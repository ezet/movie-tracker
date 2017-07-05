package no.ezet.tmdb.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import no.ezet.tmdb.view.FavoriteListFragment;
import no.ezet.tmdb.view.FilterFragment;
import no.ezet.tmdb.view.MovieDetailFragment;
import no.ezet.tmdb.view.MovieListFragment;
import no.ezet.tmdb.view.RatedListFragment;
import no.ezet.tmdb.view.SearchFragment;
import no.ezet.tmdb.view.WatchlistFragment;

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
    abstract WatchlistFragment contributeWatchListFragment();

    @ContributesAndroidInjector
    abstract RatedListFragment contributeRatedListFragment();

//    @Provides
//    MovieListFragment.FragmentListener provideMovieListFragmentListener(DiscoverActivity activity) {
//        return activity;
//    }

}
