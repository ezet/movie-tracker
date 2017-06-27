package no.ezet.fasttrack.popularmovies.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import no.ezet.fasttrack.popularmovies.view.DiscoverActivity;
import no.ezet.fasttrack.popularmovies.view.MovieDetailActivity;

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract DiscoverActivity contributeDiscoverActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MovieDetailActivity contributeMovieDetailActivity();

}
//@Module(subcomponents = MainActivitySubcomponent.class)
//abstract class ActivityModule {
//    @Binds
//    @IntoMap
//    @ActivityKey(DiscoverActivity.class)
//    abstract AndroidInjector.Factory<? extends Activity>
//    bindMainActivityInjectorFactory(MainActivitySubcomponent.Builder builder);
//
//}
