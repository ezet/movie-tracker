package no.ezet.fasttrack.popularmovies.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import no.ezet.fasttrack.popularmovies.model.Favorite;
import no.ezet.fasttrack.popularmovies.model.Movie;

@Database(entities = {Movie.class, Favorite.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MovieCacheDao movieCacheDao();

    public abstract FavoriteDao favoriteDao();

}
