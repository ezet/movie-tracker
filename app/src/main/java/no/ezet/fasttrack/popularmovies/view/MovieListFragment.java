package no.ezet.fasttrack.popularmovies.view;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.service.ImageService;
import no.ezet.fasttrack.popularmovies.viewmodel.MovieListViewModel;
import no.ezet.fasttrack.popularmovies.viewmodel.MoviesViewModel;
import timber.log.Timber;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailFragment} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@SuppressWarnings("ConstantConditions")
public class MovieListFragment extends LifecycleFragment {

    @Inject
    ImageService imageService;
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // TODO: 22.06.2017 Manage with DI
//    @Inject
    FragmentListener listener;

    @Inject
    NavigationController navigationController;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private Toolbar toolbar;
    private MovieListViewModel viewModel;


    public static MovieListFragment create() {
        return new MovieListFragment();
    }


    private static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        this.listener = (FragmentListener) getActivity();
        postponeEnterTransition();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Timber.d("onCreate: ");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Timber.d("onActivityCreated: ");
        super.onActivityCreated(savedInstanceState);

        initFloatingActionButton();

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieListViewModel.class);
        viewModel.getIsLoading().observe(this, loading -> {
                    if (loading != null && loading) showLoadingIndicator();
                    else showMovieList();
                }
        );
        viewModel.getTitleResourceId().observe(this, integer -> toolbar.setSubtitle(integer));
        viewModel.onRestoreInstanceState(savedInstanceState);

        recyclerView = (RecyclerView) getActivity().findViewById(R.id.movie_list);

        progressBar = (ProgressBar) getActivity().findViewById(R.id.pb_loading_indicator);
        errorTextView = (TextView) getActivity().findViewById(R.id.tv_error_message);

        setupRecyclerView(recyclerView);

        startPostponedEnterTransition();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView: ");
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(getActivity().getTitle());
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView: ");
        super.onDestroyView();
        viewModel.getMovies().removeObservers(this);
        viewModel.getIsLoading().removeObservers(this);
        viewModel.getTitleResourceId().removeObservers(this);
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
        int menuItemId = 0;
        switch (viewModel.getSortBy()) {
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


    private void initFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        fab.setVisibility(View.INVISIBLE);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), calculateNoOfColumns(getContext())));
        MovieListRecyclerViewAdapter adapter = new MovieListRecyclerViewAdapter(imageService, listener);
        viewModel.getMovies().observe(this, adapter::setMovies);
        recyclerView.setAdapter(adapter);
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




    public interface FragmentListener {

        void onItemClick(View f, Movie movie);
    }

    private static class MovieListRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.ViewHolder> {

        private final List<Movie> movies = new ArrayList<>();
        private ImageService imageService;
        private FragmentListener listener;
        private int counter;

        MovieListRecyclerViewAdapter(ImageService imageService, FragmentListener listener) {
            this.imageService = imageService;
            this.listener = listener;
            setHasStableIds(true);
        }

        @Override
        public long getItemId(int position) {
            return movies.get(position).getId().hashCode();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final Movie movie = movies.get(position);
            loadImage(movie.getPosterPath(), holder.posterImage);
            holder.itemView.setOnClickListener((View view) -> listener.onItemClick(view, movie));
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        private void loadImage(String relPath, ImageView imageView) {
            imageService.loadImage(relPath, ImageService.SIZE_W342, imageView);
        }

        void setMovies(List<Movie> movieList) {
            Timber.d("setMovies: " + counter++);
//            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(this.movies, movieList));
            this.movies.clear();
            this.movies.addAll(movieList);
//            diffResult.dispatchUpdatesTo(this);
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView posterImage;

            ViewHolder(View itemView) {
                super(itemView);
                posterImage = (ImageView) itemView.findViewById(R.id.iv_poster_image);
            }
        }

        public class DiffCallback extends DiffUtil.Callback {

            private final List<Movie> newList;
            private final List<Movie> oldList;

            DiffCallback(List<Movie> oldList, List<Movie> newList) {
                this.newList = newList;
                this.oldList = oldList;
            }

            @Override
            public int getOldListSize() {
                return oldList != null ? oldList.size() : 0;
            }

            @Override
            public int getNewListSize() {
                return newList != null ? newList.size() : 0;
            }

            @Override
            public boolean areItemsTheSame(int oldPosition, int newPosition) {
                return newList.get(newPosition) == oldList.get(oldPosition);
            }

            @Override
            public boolean areContentsTheSame(int oldPosition, int newPosition) {
                return newList.get(newPosition).getPosterPath().equals(oldList.get(oldPosition).getPosterPath());
            }
        }
    }
}


