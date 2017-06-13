package no.ezet.fasttrack.popularmovies.di;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import no.ezet.fasttrack.popularmovies.view.MovieListActivity;

@Module(subcomponents = {MovieListActivitySubComponent.class})
public abstract class MovieListActivityModule {

    @Binds
    @IntoMap
    @ActivityKey(MovieListActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMovieListActivityInjectorFactory(MovieListActivitySubComponent.Builder builder);
}
