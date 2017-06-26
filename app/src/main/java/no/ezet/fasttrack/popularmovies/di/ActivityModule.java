package no.ezet.fasttrack.popularmovies.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import no.ezet.fasttrack.popularmovies.view.MainActivity;
import no.ezet.fasttrack.popularmovies.view.MovieDetailActivity;

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MovieDetailActivity contributeMovieDetailActivity();

}
//@Module(subcomponents = MainActivitySubcomponent.class)
//abstract class ActivityModule {
//    @Binds
//    @IntoMap
//    @ActivityKey(MainActivity.class)
//    abstract AndroidInjector.Factory<? extends Activity>
//    bindMainActivityInjectorFactory(MainActivitySubcomponent.Builder builder);
//
//}
