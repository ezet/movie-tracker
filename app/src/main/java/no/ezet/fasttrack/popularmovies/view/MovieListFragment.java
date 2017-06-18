package no.ezet.fasttrack.popularmovies.view;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.model.MovieList;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import no.ezet.fasttrack.popularmovies.service.ImageService;
import no.ezet.fasttrack.popularmovies.viewmodel.MoviesViewModel;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailFragment} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@SuppressWarnings("ConstantConditions")
public class MovieListFragment extends LifecycleFragment implements Observer<Boolean> {

    @Inject
    ImageService imageService;
    @Inject
    MovieRepository movieRepository;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RecyclerView recyclerView;
    private MovieListRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private Toolbar toolbar;
    private boolean twoPane;
    private MoviesViewModel viewModel;

    private static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        viewModel.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFloatingActionButton();

        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(MoviesViewModel.class);
        viewModel.getIsLoading().observe(this, loading -> {
                    if (loading != null && loading) showLoadingIndicator();
                    else showMovieList();
                }
        );
        viewModel.getTitleResourceId().observe(this, integer -> toolbar.setSubtitle(integer));
        viewModel.setSelectedMovie(null);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.movie_list);

        progressBar = (ProgressBar) getActivity().findViewById(R.id.pb_loading_indicator);
        errorTextView = (TextView) getActivity().findViewById(R.id.tv_error_message);

        setupRecyclerView(recyclerView);

        if (getActivity().findViewById(R.id.movie_detail_container) != null) {
            twoPane = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(getActivity().getTitle());
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isChecked()) return true;
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.action_popular: {
                loadPopular();
                break;
            }
            case R.id.action_top_rated: {
                loadTopRated();
                break;
            }
            case R.id.action_upcoming: {
                loadUpcoming();
                break;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_bar, menu);
        if (viewModel.getSortBy().getValue() != null) {
            int menuItemId = 0;
            switch (viewModel.getSortBy().getValue()) {
                case MoviesViewModel.POPULAR:
                    menuItemId = R.id.action_popular;
                    break;
                case MoviesViewModel.UPCOMING:
                    menuItemId = R.id.action_upcoming;
                    break;
                case MoviesViewModel.TOP_RATED:
                    menuItemId = R.id.action_top_rated;
                    break;
            }
            menu.findItem(menuItemId).setChecked(true);
        }
    }

    private void loadUpcoming() {
        viewModel.setSortBy(MoviesViewModel.UPCOMING);
        toolbar.setSubtitle(R.string.upcoming);
    }

    private void loadTopRated() {
        viewModel.setSortBy(MoviesViewModel.TOP_RATED);
        toolbar.setSubtitle(R.string.top_rated);
    }

    private void loadPopular() {
        viewModel.setSortBy(MoviesViewModel.POPULAR);
        toolbar.setSubtitle(R.string.popular);
    }

    public void showLoadingIndicator() {
        errorTextView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void showLoadingError() {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    private void showMovieList() {
        progressBar.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void initFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        fab.setVisibility(View.INVISIBLE);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), calculateNoOfColumns(getContext())));
        adapter = new MovieListRecyclerViewAdapter(imageService);
        viewModel.getMovies().observe(this, adapter);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onChanged(@Nullable Boolean aBoolean) {
        if (aBoolean != null && aBoolean) {
            showLoadingIndicator();
        } else {
            showMovieList();
        }
    }

    public class MovieListRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.ViewHolder> implements Observer<MovieList> {

        private ImageService imageService;
        private MovieList movies;


        MovieListRecyclerViewAdapter(ImageService imageService) {
            this.imageService = imageService;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_item, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.movie = movies.getMovies().get(position);
            loadImage(holder.movie.getPosterPath(), holder.posterImage);


            holder.view.setOnClickListener(v -> {
                MovieDetailFragment fragment = new MovieDetailFragment();
                viewModel.setSelectedMovie(holder.movie);
                int target = twoPane ? R.id.movie_detail_container : R.id.root_container;
                getFragmentManager().beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .replace(target, fragment)
                        .commitAllowingStateLoss();
            });
        }

        @Override
        public int getItemCount() {
            if (movies == null) return 0;
            return movies.getMovies().size();
        }


        private void loadImage(String relPath, ImageView imageView) {
            imageService.loadImage(relPath, ImageService.SIZE_W342, imageView);
        }

        @Override
        public void onChanged(@Nullable MovieList movies) {
            this.movies = movies;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final View view;
            final ImageView posterImage;
            Movie movie;

            ViewHolder(View view) {
                super(view);
                this.view = view;
                posterImage = (ImageView) view.findViewById(R.id.iv_poster_image);
            }
        }
    }
}


