package no.ezet.fasttrack.popularmovies.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import no.ezet.fasttrack.popularmovies.db.AppDatabase;
import no.ezet.fasttrack.popularmovies.db.MovieDao;
import no.ezet.fasttrack.popularmovies.view.MainActivity;
import no.ezet.fasttrack.popularmovies.view.MovieListFragment;
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
    MovieDao provideMovieDao(AppDatabase appDatabase) {
        return appDatabase.movieDao();
    }


}
