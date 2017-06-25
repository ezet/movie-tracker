package no.ezet.fasttrack.popularmovies.view;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.databinding.MovieDetailContentBinding;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.model.MovieReview;
import no.ezet.fasttrack.popularmovies.model.MovieTrailer;
import no.ezet.fasttrack.popularmovies.service.IMovieService;
import no.ezet.fasttrack.popularmovies.service.ImageService;
import no.ezet.fasttrack.popularmovies.service.VideoService;
import no.ezet.fasttrack.popularmovies.viewmodel.MovieDetailsViewModel;

/**
 * A fragment representing a single Movie detail screen.
 */
public class MovieDetailFragment extends LifecycleFragment {

    public static final String MOVIE_ID = "MOVIE_ID";

    @Inject
    ImageService imageService;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    IMovieService movieService;

    @Inject
    VideoService videoService;

    private MovieDetailsViewModel viewModel;
    private ImageView backdropImage;
    private MovieDetailContentBinding binding;
    private RecyclerView reviewList;
    private RecyclerView trailerList;
    private ImageView portrait;
    private boolean initialized;

    @NonNull
    public static MovieDetailFragment create(Integer movieId) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putInt(MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = MovieDetailContentBinding.inflate(inflater, container, false);
        backdropImage = (ImageView) binding.getRoot().findViewById(R.id.iv_backdrop_image);
        reviewList = (RecyclerView) binding.getRoot().findViewById(R.id.review_list);
        trailerList = (RecyclerView) binding.getRoot().findViewById(R.id.trailer_list);
        portrait = (ImageView) binding.getRoot().findViewById(R.id.movie_portrait);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel.class);
        setHasOptionsMenu(true);
        Bundle args = getArguments();
        if (args != null && args.containsKey(MOVIE_ID)) {
            viewModel.setSelectedMovie(args.getInt(MOVIE_ID));
        }

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.detail_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initFavoriteButton();
        setupReviewList(reviewList);
        setupTrailerList(trailerList);

        viewModel.getSelectedMovie().observe(this, movie -> {
            if (movie != null) {
                bindMovie(movie);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupReviewList(RecyclerView recyclerView) {
        ReviewListAdapter reviewListAdapter = new ReviewListAdapter((movieReview, i) -> openReview(movieReview));
        recyclerView.setAdapter(reviewListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        viewModel.getReviews().observe(this, reviewListAdapter::setReviews);
    }

    private void openReview(MovieReview movieReview) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movieReview.url)));
    }

    private void setupTrailerList(RecyclerView recyclerView) {
        TrailerListAdapter trailerListAdapter = new TrailerListAdapter(videoService, (movieTrailer, i) -> openTrailer(movieTrailer));
        recyclerView.setAdapter(trailerListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        viewModel.getTrailers().observe(this, trailerListAdapter::setVideos);
    }

    private void openTrailer(MovieTrailer movieTrailer) {
        String base = "http://www.youtube.com/watch?v=";
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(base + movieTrailer.key)));
    }

    private void bindMovie(@NonNull Movie movie) {
        imageService.loadImage(movie.getPosterPath(), ImageService.SIZE_W342, portrait);
        imageService.loadImage(movie.getBackdropPath(), ImageService.SIZE_W342, backdropImage);
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(movie.getOriginalTitle());
        }
        binding.setMovie(movie);
    }

    @SuppressWarnings("ConstantConditions")
    private void initFavoriteButton() {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        viewModel.getFavorite().observe(this, isFavorite -> {
            if (isFavorite) {
                fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_black_24dp));
                if (initialized) {
                    Snackbar.make(getView(), "Added to favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } else {
                fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_star_border_black_24dp));
                if (initialized) {
                    Snackbar.make(getView(), "Removed from favorites", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
            initialized = true;
        });
        fab.setOnClickListener(view -> {
            viewModel.toggleFavorite();
        });
    }
}
