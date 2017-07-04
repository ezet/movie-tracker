package no.ezet.fasttrack.popularmovies.api.requestbody;

public class RequestBody {
    public static final String MOVIE = "movie";
    public static final String TV = "tv";
    public final String mediaType;
    public final int mediaId;
    public final Boolean favorite;
    public final Boolean watchlist;

    private RequestBody(String mediaType, int mediaId, Boolean favorite, Boolean watchlist) {
        if (!mediaType.equals(MOVIE) && !mediaType.equals(TV))
            throw new IllegalArgumentException("MediaType must be TV or MOVIE");
        this.mediaType = mediaType;
        this.mediaId = mediaId;
        this.favorite = favorite;
        this.watchlist = watchlist;
    }

    public static RequestBody watchlist(String mediaType, int movieId, boolean watchlist) {
        return new RequestBody(mediaType, movieId, null, watchlist);
    }

    public static RequestBody favorite(String mediaType, int movieId, boolean favorite) {
        return new RequestBody(mediaType, movieId, favorite, null);
    }

}