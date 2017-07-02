package no.ezet.fasttrack.popularmovies.api.body;

public class PostFavoriteBody {
    public static final String MOVIE = "movie";
    public static final String TV = "tv";
    private final String mediaType;
    private final int movieId;
    private final boolean favorite;

    public PostFavoriteBody(String mediaType, int movieId, boolean favorite) {
        if (!mediaType.equals(MOVIE) && !mediaType.equals(TV))
            throw new IllegalArgumentException("MediaType must be TV or MOVIE");
        this.mediaType = mediaType;
        this.movieId = movieId;
        this.favorite = favorite;
    }
}
