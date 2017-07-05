package no.ezet.tmdb.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import no.ezet.tmdb.api.model.Movie;

@Dao
public abstract class MovieCacheDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract Long insert(Movie movie);

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
            "WHERE type = " + Movie.WATCHLIST)
    public abstract LiveData<List<Movie>> getWatchlist();

    @Query("SELECT * FROM movie " +
            "WHERE type = " + Movie.FAVORITE)
    public abstract LiveData<List<Movie>> getFavorites();

    @Query("SELECT * FROM movie " +
            "WHERE id = :id " +
            "AND type = :type")
    public abstract LiveData<Movie> getById(int id, int type);

    @Delete
    public abstract int delete(Movie movie);

    @Query("SELECT * FROM movie " +
            "WHERE type = :type")
    public abstract LiveData<List<Movie>> getByType(int type);

    @Query("DELETE FROM movie " +
            "WHERE type = :type")
    public abstract int deleteByType(int type);
}