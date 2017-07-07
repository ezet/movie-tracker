package no.ezet.tmdb.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.ezet.tmdb.db.AppDatabase;
import no.ezet.tmdb.db.MovieCacheDao;
import no.ezet.tmdb.service.PreferenceService;
import no.ezet.tmdb.viewmodel.ViewModelModule;

@Module(includes = ViewModelModule.class)
class AppModule {

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
