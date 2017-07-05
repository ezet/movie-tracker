package no.ezet.tmdb.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import no.ezet.tmdb.api.model.Movie;

@Database(entities = {Movie.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MovieCacheDao movieCacheDao();
}
