package no.ezet.tmdb.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import no.ezet.tmdb.view.DiscoverActivity;
import no.ezet.tmdb.view.MovieDetailActivity;

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = DiscoverModule.class)
    abstract DiscoverActivity contributeDiscoverActivity();

    @ContributesAndroidInjector(modules = MovieDetailModule.class)
    abstract MovieDetailActivity contributeMovieDetailActivity();

}

//@ActivityScope
//@Module(subcomponents = DiscoverActivitySubcomponent.class)
//abstract class ActivityModule {
//    @Binds
//    @IntoMap
//    @ActivityKey(DiscoverActivity.class)
//    abstract AndroidInjector.Factory<? extends Activity>
//    bindDiscoverActivityInjectorFactory(DiscoverActivitySubcomponent.Builder builder);
//}
