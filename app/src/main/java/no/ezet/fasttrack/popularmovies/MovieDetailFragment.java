package no.ezet.fasttrack.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.service.ImageService;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String EXTRA_MOVIE = MovieDetailFragment.class.getPackage() + "movie";
    private static final String TAG = MovieDetailFragment.class.getSimpleName();

    private Movie movie;
    private ImageService imageService;

    private ImageView posterImage;

    private CollapsingToolbarLayout appBarLayout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageService = ImageService.getImageService(getContext());

        if (getArguments().containsKey(EXTRA_MOVIE)) {
            movie = getArguments().getParcelable(EXTRA_MOVIE);

            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(movie.getOriginalTitle());
            }

            posterImage = (ImageView) activity.findViewById(R.id.iv_backdrop_image);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail_content, container, false);

        if (movie != null) {
            ((TextView) rootView.findViewById(R.id.tv_movie_overview)).setText(movie.getOverview());
            ((TextView) rootView.findViewById(R.id.tv_movie_release_date)).setText(movie.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.tv_movie_rating)).setText(String.valueOf(movie.getVoteAverage()));
            imageService.loadImage(movie.getBackdropPath(), ImageService.SIZE_W342, posterImage);
        }
        return rootView;
    }
}
