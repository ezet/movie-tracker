package no.ezet.fasttrack.popularmovies.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import no.ezet.fasttrack.popularmovies.model.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MovieDao movieDao();

}
