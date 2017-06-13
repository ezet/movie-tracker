package no.ezet.fasttrack.popularmovies.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.App;
import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.databinding.MovieDetailContentBinding;
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
    @SuppressWarnings("unused")
    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    @Inject
    ImageService imageService;
    private Movie movie;
    private ImageView posterImage;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        App.getInstance().getMovieComponent().inject(this);
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);

        if (getArguments().containsKey(EXTRA_MOVIE)) {
            movie = getArguments().getParcelable(EXTRA_MOVIE);

            if (appBarLayout != null) {
                appBarLayout.setTitle(movie != null ? movie.getOriginalTitle() : null);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MovieDetailContentBinding binding = MovieDetailContentBinding.inflate(inflater, container, false);
        binding.setMovie(movie);
        posterImage = (ImageView) getActivity().findViewById(R.id.iv_backdrop_image);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (movie != null) {
            imageService.loadImage(movie.getBackdropPath(), ImageService.SIZE_W342, posterImage);
        }
    }
}
