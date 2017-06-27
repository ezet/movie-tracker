package no.ezet.fasttrack.popularmovies.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.WorkerThread;

import java.util.List;

@Dao
@WorkerThread
public abstract class FavoriteDao {

    @Insert
    public abstract List<Long> insert(Favorite... movies);

    @Delete
    public abstract int delete(Favorite... movies);

    @Query("SELECT * FROM favorite")
    public abstract LiveData<List<Favorite>> getFavorites();

    @Query("SELECT * from favorite " +
            "WHERE movieId = :id")
    public abstract LiveData<Favorite> getById(int id);
}
