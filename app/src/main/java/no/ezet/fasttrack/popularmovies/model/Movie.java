package no.ezet.fasttrack.popularmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

import no.ezet.fasttrack.popularmovies.db.MovieReview;
import no.ezet.fasttrack.popularmovies.db.MovieTrailer;

@SuppressWarnings("WeakerAccess")
@Entity
public class Movie {

    public static final int POPULAR = 0;
    public static final int UPCOMING = 1;
    public static final int TOP_RATED = 2;
    public static final int NOW_PLAYING = 3;
    public int budget;
    public String homepage;
    public String imdbId;
    public String status;
    public String tagline;
    public int revenue;
    public int runtime;
    @Ignore
    public ApiList<MovieTrailer> videos;
    @Ignore
    public ApiList<MovieReview> reviews;
    @Ignore
    public List<Genre> genres;
    @PrimaryKey
    private int id;
    private String posterPath;
    private Boolean adult;
    private String overview;
    private String releaseDate;
    @Ignore
    private List<Integer> genreIds;
    private String originalTitle;
    private String originalLanguage;
    private String title;
    private String backdropPath;
    private Double popularity;
    private Integer voteCount;
    private Boolean video;
    private Double voteAverage;
    private int Type;

    public String getGenresAsString() {
        if (genres.size() == 0) return "";
        StringBuilder stringBuilder = new StringBuilder();
        for (Genre genre : genres) {
            stringBuilder.append(genre.name);
            stringBuilder.append(", ");
        }
        stringBuilder.delete(stringBuilder.lastIndexOf(","), stringBuilder.length());
        return stringBuilder.toString();
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getVoteAverageString() {
        return String.valueOf(voteAverage);
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public static class Genre {

        public final int id;
        public final String name;

        public Genre(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
