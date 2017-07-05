package no.ezet.tmdb.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import no.ezet.tmdb.view.DiscoverActivity;
import no.ezet.tmdb.view.MovieDetailActivity;

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
