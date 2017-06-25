package no.ezet.fasttrack.popularmovies.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.ezet.fasttrack.popularmovies.db.AppDatabase;
import no.ezet.fasttrack.popularmovies.db.FavoriteDao;
import no.ezet.fasttrack.popularmovies.db.MovieCacheDao;
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
    FavoriteDao provideFavoriteDao(AppDatabase appDatabase) {
        return appDatabase.favoriteDao();
    }


}
