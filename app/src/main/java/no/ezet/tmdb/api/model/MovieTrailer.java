package no.ezet.tmdb.api.model;

public class MovieTrailer {

    public final String id;

    public final String key;

    public final String name;

    public final String site;

    public final int size;

    public final String type;

    public MovieTrailer(String id, String key, String name, String site, int size, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }
}
