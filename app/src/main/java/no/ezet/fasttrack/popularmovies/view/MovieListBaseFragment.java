package no.ezet.fasttrack.popularmovies.view;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
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
import no.ezet.fasttrack.popularmovies.service.ImageService;
import no.ezet.fasttrack.popularmovies.viewmodel.MovieListBaseViewModel;
import no.ezet.fasttrack.popularmovies.viewmodel.MovieListItem;
import timber.log.Timber;

/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailFragment} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public abstract class MovieListBaseFragment<T extends MovieListBaseViewModel> extends LifecycleFragment {

    protected T viewModel;
    @Inject
    ImageService imageService;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    // TODO: 22.06.2017 Manage with DI
//    @Inject
    FragmentListener listener;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;

    private static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    protected abstract Class<T> getViewModelClass();

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        if (context instanceof FragmentListener) {
            listener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentListener");
        }
        postponeEnterTransition();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModelClass());
        setupViewModel(viewModel);
        viewModel.onRestoreInstanceState(savedInstanceState);
        viewModel.getIsLoading().observe(this, loading -> {
                    if (loading != null && loading) showLoadingIndicator();
                    else showMovieList();
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_list, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.movie_list);
        progressBar = (ProgressBar) root.findViewById(R.id.pb_loading_indicator);
        errorTextView = (TextView) root.findViewById(R.id.tv_error_message);
        setupRecyclerView(recyclerView);
        startPostponedEnterTransition();
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getMovies().removeObservers(this);
        viewModel.getIsLoading().removeObservers(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), calculateNoOfColumns(getContext())));
        final MovieListRecyclerViewAdapter adapter = new MovieListRecyclerViewAdapter(imageService, listener);
        viewModel.getMovies().observe(this, adapter::setMovies);
        recyclerView.setAdapter(adapter);
    }

    protected void setupViewModel(T viewModel) {
    }

    public void showLoadingIndicator() {
        Timber.d("showLoadingIndicator: ");
        errorTextView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showMovieList() {
        Timber.d("showMovieList: ");
        progressBar.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
    }

    public void showLoadingError() {
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
    }

    public interface FragmentListener {

        void onItemClick(View f, MovieListItem movie);
    }

    private static class MovieListRecyclerViewAdapter
            extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.ViewHolder> {

        private final List<MovieListItem> movies = new ArrayList<>();
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
            return movies.get(position).id;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final MovieListItem movie = movies.get(position);
            loadImage(movie.posterPath, holder.posterImage);
            holder.itemView.setOnClickListener((View view) -> listener.onItemClick(view, movie));
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        private void loadImage(String relPath, ImageView imageView) {
            imageService.loadImage(relPath, ImageService.SIZE_W342, imageView);
        }

        void setMovies(List<MovieListItem> movieList) {
            Timber.d("setMovies: " + counter++);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(this.movies, movieList));
            this.movies.clear();
            this.movies.addAll(movieList);
            diffResult.dispatchUpdatesTo(this);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView posterImage;

            ViewHolder(View itemView) {
                super(itemView);
                posterImage = (ImageView) itemView.findViewById(R.id.iv_poster_image);
            }
        }

        private static class DiffCallback extends DiffUtil.Callback {

            private final List<MovieListItem> newList;
            private final List<MovieListItem> oldList;

            DiffCallback(List<MovieListItem> oldList, List<MovieListItem> newList) {
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
                return newList.get(newPosition).posterPath.equals(oldList.get(oldPosition).posterPath);
            }
        }
    }
}


