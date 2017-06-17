package no.ezet.fasttrack.popularmovies.view;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import no.ezet.fasttrack.popularmovies.service.ImageService;
import no.ezet.fasttrack.popularmovies.viewmodel.MoviesViewModel;
import timber.log.Timber;

/**
 * A fragment representing a single Movie detail screen.
 */
public class MovieDetailFragment extends LifecycleFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String EXTRA_MOVIE = MovieDetailFragment.class.getPackage() + "movie";

    @Inject
    ImageService imageService;

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    private MoviesViewModel viewModel;


    private Movie movie;
    private ImageView backdropImage;
    private MovieDetailContentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this.getActivity(), viewModelFactory).get(MoviesViewModel.class);

//        if (getArguments().containsKey(EXTRA_MOVIE)) {
//            movie = getArguments().getParcelable(EXTRA_MOVIE);
//        }
        setHasOptionsMenu(true);
    }

    private void initFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Timber.d("onOptionsItemSelected");
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
//        binding.setMovie(movie);
        backdropImage = (ImageView) binding.getRoot().findViewById(R.id.iv_backdrop_image);
//        viewModel.getSelectedMovie().observe(this, this::bindMovie);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.detail_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initFloatingActionButton();
        viewModel.getSelectedMovie().observe(this, this::bindMovie);

//        if (movie != null) {
//            imageService.loadImage(movie.getBackdropPath(), ImageService.SIZE_W342, backdropImage);
//            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
//            if (appBarLayout != null) {
//                appBarLayout.setTitle(movie != null ? movie.getOriginalTitle() : null);
//            }
//        }
    }

    private void bindMovie(Movie movie) {
        imageService.loadImage(movie.getBackdropPath(), ImageService.SIZE_W342, backdropImage);
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(movie != null ? movie.getOriginalTitle() : null);
        }
        binding.setMovie(movie);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }
}
