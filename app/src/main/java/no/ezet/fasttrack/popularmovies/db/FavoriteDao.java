package no.ezet.fasttrack.popularmovies.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.support.annotation.WorkerThread;

import java.util.List;

@Dao
@WorkerThread
public abstract class FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(Favorite movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insert(List<Favorite> movies);

    @Delete
    public abstract int delete(Favorite... movies);

    @Query("SELECT * FROM favorite")
    public abstract LiveData<List<Favorite>> getFavorites();

    @Query("SELECT * from favorite " +
            "WHERE movieId = :id")
    public abstract LiveData<Favorite> getById(int id);
}
