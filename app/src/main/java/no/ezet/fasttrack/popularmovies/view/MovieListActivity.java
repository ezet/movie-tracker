package no.ezet.fasttrack.popularmovies.view;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.model.MovieList;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import no.ezet.fasttrack.popularmovies.service.ImageService;
import no.ezet.fasttrack.popularmovies.viewmodel.MoviesViewModel;

import static no.ezet.fasttrack.popularmovies.viewmodel.MoviesViewModel.POPULAR;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements LifecycleRegistryOwner, Observer<Boolean>, HasSupportFragmentInjector {


    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    @Inject
    ImageService imageService;
    @Inject
    MovieRepository movieRepository;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    private RecyclerView recyclerView;
    private MovieListRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private Toolbar toolbar;
    private boolean twoPane;
    private MoviesViewModel moviesViewModel;

    private static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        moviesViewModel = ViewModelProviders.of(this, viewModelFactory).get(MoviesViewModel.class);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        initFloatingActionButton();
        moviesViewModel.getIsLoading().observe(this, loading -> {
                    if (loading != null && loading) showLoadingIndicator();
                    else showMovieList();
                }
        );


        recyclerView = (RecyclerView) findViewById(R.id.movie_list);

        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        errorTextView = (TextView) findViewById(R.id.tv_error_message);

        setupRecyclerView(recyclerView);

        if (findViewById(R.id.movie_detail_container) != null) {
            twoPane = true;
        }

        loadPopular();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar, menu);
        return true;
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

    private void loadUpcoming() {
        adapter.loadUpcoming();
        toolbar.setSubtitle(R.string.upcoming);
    }

    private void loadTopRated() {
        adapter.loadTopRated();
        toolbar.setSubtitle(R.string.top_rated);
    }

    private void loadPopular() {
        adapter.loadPopular();
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        fab.setVisibility(View.INVISIBLE);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(this, calculateNoOfColumns(getBaseContext())));
        adapter = new MovieListRecyclerViewAdapter(imageService, movieRepository);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public void onChanged(@Nullable Boolean aBoolean) {
        if (aBoolean != null && aBoolean) {
            showLoadingIndicator();
        } else {
            showMovieList();
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    public class MovieListRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.ViewHolder> implements Observer<MovieList> {

        private MovieRepository movieRepository;
        private ImageService imageService;
        private MovieList movies;


        MovieListRecyclerViewAdapter(ImageService imageService, MovieRepository movieRepository) {
            this.imageService = imageService;
            this.movieRepository = movieRepository;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.movie = movies.getMovies().get(position);
            loadImage(holder.movie.getPosterPath(), holder.posterImage);


            holder.view.setOnClickListener(v -> {
                Bundle arguments = new Bundle();
                arguments.putParcelable(MovieDetailFragment.EXTRA_MOVIE, holder.movie);
                MovieDetailFragment fragment = new MovieDetailFragment();
                fragment.setArguments(arguments);
                if (twoPane) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commitAllowingStateLoss();
                } else {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, MovieDetailActivity.class);
//                    intent.putExtra(MovieDetailFragment.EXTRA_MOVIE, holder.movie);
//                    context.startActivity(intent);
                    getSupportFragmentManager().beginTransaction().replace(R.id.root_view, fragment).commitAllowingStateLoss();
                }
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

        void loadPopular() {
            moviesViewModel.getMovies(POPULAR).observe(MovieListActivity.this, this);
        }

        void loadTopRated() {
            moviesViewModel.getMovies(MoviesViewModel.TOP_RATED).observe(MovieListActivity.this, this);
        }

        void loadUpcoming() {
            moviesViewModel.getMovies(MoviesViewModel.UPCOMING).observe(MovieListActivity.this, this);
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

