package no.ezet.fasttrack.popularmovies.di;

import android.app.Application;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.ezet.fasttrack.popularmovies.viewmodel.ViewModelModule;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Singleton
    @Provides
    Picasso providePicasso(Application application) {
        return new Picasso.Builder(application).loggingEnabled(false).build();
    }
}
