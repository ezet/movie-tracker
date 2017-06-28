package no.ezet.fasttrack.popularmovies.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import no.ezet.fasttrack.popularmovies.model.Movie;

@Dao
public abstract class MovieCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract List<Long> insert(List<Movie> movies);

    @Query("SELECT * FROM movie " +
            "WHERE type = " + Movie.POPULAR + " " +
            "LIMIT 20")
    public abstract LiveData<List<Movie>> getPopular();

    @Query("SELECT * FROM movie " +
            "WHERE type = " + Movie.UPCOMING + " " +
            "LIMIT 20")
    public abstract LiveData<List<Movie>> getUpcoming();

    @Query("SELECT * FROM movie " +
            "WHERE type = " + Movie.TOP_RATED + " " +
            "LIMIT 20")
    public abstract LiveData<List<Movie>> getTopRated();

    @Query("SELECT * FROM movie " +
            "WHERE type = " + Movie.NOW_PLAYING + " " +
            "LIMIT 20")
    public abstract LiveData<List<Movie>> getNowPlaying();

    @Query("SELECT * FROM movie " +
            "WHERE id = :id")
    public abstract LiveData<Movie> getById(int id);

}