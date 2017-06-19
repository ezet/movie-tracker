package no.ezet.fasttrack.popularmovies.view;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import org.parceler.Parcels;

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
import no.ezet.fasttrack.popularmovies.viewmodel.MoviesViewModel;

/**
 * A fragment representing a single Movie detail screen.
 */
public class MovieDetailFragment extends LifecycleFragment {

    public static final String EXTRA_MOVIE = MovieDetailFragment.class.getPackage() + "movie";

    @Inject
    ImageService imageService;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    IMovieService movieService;

    @Inject
    VideoService videoService;

    private MoviesViewModel viewModel;
    private ImageView backdropImage;
    private MovieDetailContentBinding binding;
    private RecyclerView reviewList;
    private RecyclerView trailerList;
    private ImageView portrait;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this.getActivity(), viewModelFactory).get(MoviesViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_MOVIE)) {
            viewModel.setSelectedMovie(Parcels.unwrap(savedInstanceState.getParcelable(EXTRA_MOVIE)));
        }

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.detail_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initFloatingActionButton();
        setupReviewList(reviewList);
        setupTrailerList(trailerList);

        viewModel.getSelectedMovie().observe(this, this::bindMovie);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_MOVIE, Parcels.wrap(viewModel.getSelectedMovie().getValue()));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = MovieDetailContentBinding.inflate(inflater, container, false);
        backdropImage = (ImageView) binding.getRoot().findViewById(R.id.iv_backdrop_image);
        reviewList = (RecyclerView) binding.getRoot().findViewById(R.id.review_list);
        trailerList = (RecyclerView) binding.getRoot().findViewById(R.id.trailer_list);
        portrait = (ImageView) binding.getRoot().findViewById(R.id.movie_portrait);
        return binding.getRoot();
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

    private void bindMovie(Movie movie) {
        imageService.loadImage(movie.getPosterPath(), ImageService.SIZE_W342, portrait);
        imageService.loadImage(movie.getBackdropPath(), ImageService.SIZE_W342, backdropImage);
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(movie.getOriginalTitle());
        }
        binding.setMovie(movie);
    }

    private void initFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }
}
