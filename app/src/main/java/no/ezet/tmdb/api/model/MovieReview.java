package no.ezet.tmdb.api.model;

public class MovieReview {

    public final String id;
    public final String author;
    public final String content;
    public final String url;


    public MovieReview(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }
}
