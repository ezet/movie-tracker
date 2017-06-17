package no.ezet.fasttrack.popularmovies.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("WeakerAccess")
@org.parceler.Parcel
public class Movie {
//public class Movie implements Parcelable {

//    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
//        @Override
//        public Movie createFromParcel(Parcel in) {
//            return new Movie(in);
//        }
//
//        @Override
//        public Movie[] newArray(int size) {
//            return new Movie[size];
//        }
//    };

    @SerializedName("poster_path")
    String posterPath;

    @SerializedName("adult")
    Boolean adult;
    @SerializedName("overview")
    String overview;

    @SerializedName("release_date")
    String releaseDate;
    @SerializedName("genre_ids")
    List<Integer> genreIds = null;
    @SerializedName("id")
    Integer id;
    @SerializedName("original_title")
    String originalTitle;
    @SerializedName("original_language")
    String originalLanguage;
    @SerializedName("title")
    String title;
    @SerializedName("backdrop_path")
    String backdropPath;
    @SerializedName("popularity")
    Double popularity;
    @SerializedName("vote_count")
    Integer voteCount;
    @SerializedName("video")
    Boolean video;
    @SerializedName("vote_average")
    Double voteAverage;



//    protected Movie(Parcel in) {
//        posterPath = in.readString();
//        overview = in.readString();
//        releaseDate = in.readString();
//        originalTitle = in.readString();
//        originalLanguage = in.readString();
//        title = in.readString();
//        backdropPath = in.readString();
//        voteAverage = in.readDouble();
//    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(posterPath);
//        dest.writeString(overview);
//        dest.writeString(releaseDate);
//        dest.writeString(originalTitle);
//        dest.writeString(originalLanguage);
//        dest.writeString(title);
//        dest.writeString(backdropPath);
//        dest.writeDouble(voteAverage);
//    }
}
