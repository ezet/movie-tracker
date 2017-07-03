package no.ezet.fasttrack.popularmovies.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.ezet.fasttrack.popularmovies.db.AppDatabase;
import no.ezet.fasttrack.popularmovies.db.MovieCacheDao;
import no.ezet.fasttrack.popularmovies.service.PreferenceService;
import no.ezet.fasttrack.popularmovies.viewmodel.ViewModelModule;

@Module(includes = ViewModelModule.class)
class AppModule {

    @Singleton
    @Provides
    Picasso providePicasso(Application application) {
        return new Picasso.Builder(application).loggingEnabled(false).build();
    }

    @Singleton
    @Provides
    AppDatabase provideAppDatabase(Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, "popularmovies.db").build();
    }

    @Singleton
    @Provides
    MovieCacheDao provideMovieCacheDao(AppDatabase appDatabase) {
        return appDatabase.movieCacheDao();
    }

    @Singleton
    @Provides
    PreferenceService providePreferenceService(Application application) {
        return new PreferenceService(application);
    }

    @Singleton
    @Provides
    SharedPreferences provideDefaultSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }


}
