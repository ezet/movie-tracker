package no.ezet.fasttrack.popularmovies.db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Favorite {

    @PrimaryKey
    private final int movieId;
    private String posterPath;

    public Favorite(int movieId, String posterPath) {
        this.movieId = movieId;
        this.posterPath = posterPath;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
