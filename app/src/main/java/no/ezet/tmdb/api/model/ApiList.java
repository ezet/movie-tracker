package no.ezet.tmdb.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiList<T> {

    public int page;
    public int totalPages;
    public int totalResults;

    @SerializedName(value = "results", alternate = {"genres"})
    public List<T> results = null;

    public ApiList() {
    }

    public ApiList(int page, int totalPages, int totalResults) {
        this.page = page;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }
}
