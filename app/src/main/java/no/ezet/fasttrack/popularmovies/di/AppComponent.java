package no.ezet.fasttrack.popularmovies.di;


import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import no.ezet.fasttrack.popularmovies.App;
import no.ezet.fasttrack.popularmovies.service.ServiceModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AppModule.class, ActivityModule.class, ServiceModule.class})
public interface AppComponent {
    void inject(App application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

}
