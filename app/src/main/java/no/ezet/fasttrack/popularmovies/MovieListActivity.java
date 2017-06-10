package no.ezet.fasttrack.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.model.MovieList;
import no.ezet.fasttrack.popularmovies.service.IMovieService;
import no.ezet.fasttrack.popularmovies.service.ImageService;
import no.ezet.fasttrack.popularmovies.service.MovieServiceFactory;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity {


    private static final String API_KEY = "f1b9458c5a22388abc326bc55eab3216";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private RecyclerView recyclerView;
    private MovieListRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private Toolbar toolbar;
    private boolean twoPane;

    private static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

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
        adapter = new MovieListRecyclerViewAdapter(ImageService.getImageService(this));
        recyclerView.setAdapter(adapter);
    }

    public class MovieListRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.ViewHolder> implements AsyncTaskCompleteListener<MovieList> {

        private IMovieService movieService;
        private ImageService imageService;
        private MovieList movies;


        MovieListRecyclerViewAdapter(ImageService imageService) {
            this.imageService = imageService;
            movieService = MovieServiceFactory.getMovieService(BASE_URL, API_KEY);
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
                setMovies(movieList);
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
            new FetchMoviesTask(movieService, this).execute("popular");
        }

        void loadTopRated() {
            new FetchMoviesTask(movieService, this).execute("top_rated");
        }

        void loadUpcoming() {
            new FetchMoviesTask(movieService, this).execute("upcoming");
        }

        private boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
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

