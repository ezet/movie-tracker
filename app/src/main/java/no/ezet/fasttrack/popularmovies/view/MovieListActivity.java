package no.ezet.fasttrack.popularmovies.view;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import no.ezet.fasttrack.popularmovies.App;
import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.model.MovieList;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import no.ezet.fasttrack.popularmovies.service.ImageService;
import no.ezet.fasttrack.popularmovies.task.RepositoryListener;
import no.ezet.fasttrack.popularmovies.viewmodel.MoviesViewModel;
import timber.log.Timber;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity implements LifecycleRegistryOwner, Observer<List<Movie>> {


    @Inject
    ImageService imageService;
    @Inject
    MovieRepository movieRepository;
    private RecyclerView recyclerView;
    private MovieListRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private Toolbar toolbar;
    private boolean twoPane;
    private MoviesViewModel moviesViewModel;
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    private static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        AndroidInjection.inject(this);

        moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);

        moviesViewModel.getMovies("").observe(this, this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        initFloatingActionButton();

        recyclerView = (RecyclerView) findViewById(R.id.movie_list);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        errorTextView = (TextView) findViewById(R.id.tv_error_message);

        setupRecyclerView(recyclerView);
        loadPopular();

        if (findViewById(R.id.movie_detail_container) != null) {
            twoPane = true;
        }
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
    public void onChanged(@Nullable List<Movie> movies) {

    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    public class MovieListRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.ViewHolder> implements RepositoryListener<MovieList>, Observer<List<Movie>> {

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
                if (twoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(MovieDetailFragment.EXTRA_MOVIE, holder.movie);
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailFragment.EXTRA_MOVIE, holder.movie);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (movies == null) return 0;
            return movies.getMovies().size();
        }

        @Override
        public void onPostExecute(MovieList movieList) {
            if (movieList != null) {
//                setMovies(movieList);
                showMovieList();
            } else {
                showLoadingError();
            }
        }

        @Override
        public void onPreExecute() {
            showLoadingIndicator();
        }

        private void loadImage(String relPath, ImageView imageView) {
            imageService.loadImage(relPath, ImageService.SIZE_W342, imageView);
        }

        private void setMovies(MovieList movies) {
            this.movies = movies;
            notifyDataSetChanged();
        }

        void loadPopular() {
//            movieRepository.getMovies("popular", this);
            moviesViewModel.getMovies("popular").observe(MovieListActivity.this, this);
        }

        void loadTopRated() {
            movieRepository.getMovies("top_rated", this);
        }

        void loadUpcoming() {
            movieRepository.getMovies("upcoming", this);
        }

        @SuppressWarnings("unused")
        private boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }

        @Override
        public void onChanged(@Nullable List<Movie> movies) {
            Timber.d("onChanged");
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

