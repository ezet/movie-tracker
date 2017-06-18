package no.ezet.fasttrack.popularmovies.model;


import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@SuppressWarnings("unused")

@Parcel
public class MovieList {

    @SerializedName("page")
    Integer page;

    @SerializedName("results")
    List<Movie> results = null;

    @SerializedName("total_results")
    Integer totalResults;

    @SerializedName("total_pages")
    Integer totalPages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return results;
    }

    public void setMovies(List<Movie> movies) {
        this.results = movies;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

}


