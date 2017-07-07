package no.ezet.tmdb.di;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import no.ezet.tmdb.service.ImageService;
import no.ezet.tmdb.view.DiscoverActivity;
import no.ezet.tmdb.view.FavoriteListFragment;
import no.ezet.tmdb.view.FilterFragment;
import no.ezet.tmdb.view.MovieDetailActivity;
import no.ezet.tmdb.view.MovieDetailFragment;
import no.ezet.tmdb.view.MovieListFragment;
import no.ezet.tmdb.view.RatedListFragment;
import no.ezet.tmdb.view.RecommendedListFragment;
import no.ezet.tmdb.view.SearchFragment;
import no.ezet.tmdb.view.SimilarListFragment;
import no.ezet.tmdb.view.WatchlistFragment;

@Module()
abstract class MovieDetailModule {

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

    @ContributesAndroidInjector
    abstract SimilarListFragment contributeSimilarListFragment();

    @ContributesAndroidInjector
    abstract RecommendedListFragment contributeRecommendedListFragment();

}
