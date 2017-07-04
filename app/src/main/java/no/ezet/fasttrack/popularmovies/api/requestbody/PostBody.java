package no.ezet.fasttrack.popularmovies.api.requestbody;

public class PostBody {
    public static final String MOVIE = "movie";
    public static final String TV = "tv";
    public final String mediaType;
    public final int mediaId;
    public final Boolean favorite;
    public final Boolean watchlist;

    private PostBody(String mediaType, int mediaId, Boolean favorite, Boolean watchlist) {
        if (!mediaType.equals(MOVIE) && !mediaType.equals(TV))
            throw new IllegalArgumentException("MediaType must be TV or MOVIE");
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.favorite = favorite;
        this.watchlist = watchlist;
    }

    public static PostBody watchlist(String mediaType, int movieId, boolean watchlist) {
        return new PostBody(mediaType, movieId, null, watchlist);
    }

    public static PostBody favorite(String mediaType, int movieId, boolean favorite) {
        return new PostBody(mediaType, movieId, favorite, null);
    }

}