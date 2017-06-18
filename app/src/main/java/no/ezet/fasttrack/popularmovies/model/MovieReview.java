package no.ezet.fasttrack.popularmovies.model;

public class MovieReview {

    public String id;
    public String author;
    public String content;
    public String url;


    public MovieReview(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }
}
