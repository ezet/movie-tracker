package no.ezet.fasttrack.popularmovies;

import android.app.Application;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module()
public final class AppModule {

    @Singleton
    @Provides
    Picasso providePicasso(Application application) {
        return new Picasso.Builder(application).loggingEnabled(false).build();
    }


}
