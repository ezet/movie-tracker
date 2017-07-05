package no.ezet.tmdb.api.model;

public class PostResponse {

    public final int statusCode;

    public final String statusMessage;

    public PostResponse(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
