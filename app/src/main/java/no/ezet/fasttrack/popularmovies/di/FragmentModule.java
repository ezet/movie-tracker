package no.ezet.fasttrack.popularmovies.di;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import no.ezet.fasttrack.popularmovies.view.MainActivity;
import no.ezet.fasttrack.popularmovies.view.MovieDetailFragment;
import no.ezet.fasttrack.popularmovies.view.MovieListFragment;

@Module()
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract MovieDetailFragment contributeMovieDetailFragment();

    @ContributesAndroidInjector
    abstract MovieListFragment contributeMovieListFragment();

//    @Provides
//    MovieListFragment.FragmentListener provideMovieListFragmentListener(MainActivity activity) {
//        return activity;
//    }

}
