package no.ezet.fasttrack.popularmovies.model;

import java.util.List;

public class ApiList<T> {

    public int page;
    public int totalPages;
    public int totalResults;
    public List<T> results = null;

    public ApiList() {
    }

    public ApiList(int page, int totalPages, int totalResults) {
        this.page = page;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }
}
