package no.ezet.fasttrack.popularmovies;


import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import no.ezet.fasttrack.popularmovies.di.MovieListActivityModule;
import no.ezet.fasttrack.popularmovies.service.MovieModule;

// TODO: 13.06.2017 Make MovieModule a component

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, MovieListActivityModule.class, MovieModule.class})
public interface AppComponent {
    void inject(App application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }
}
