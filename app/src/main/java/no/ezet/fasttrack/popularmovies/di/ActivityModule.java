package no.ezet.fasttrack.popularmovies.di;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import no.ezet.fasttrack.popularmovies.view.MainActivity;

//@Module
//abstract class ActivityModule {

//    @ContributesAndroidInjector(modules = FragmentModule.class)
//    abstract MainActivity contributeMainActivity();
//}

@Module(subcomponents = MainActivitySubcomponent.class)
abstract class ActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity>
    bindMainActivityInjectorFactory(MainActivitySubcomponent.Builder builder);
}
