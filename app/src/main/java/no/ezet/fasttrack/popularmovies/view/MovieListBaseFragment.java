package no.ezet.fasttrack.popularmovies.view;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setupViewModel(viewModel);
        viewModel.onRestoreInstanceState(savedInstanceState);
        viewModel.getIsLoading().observe(this, loading -> {
                    if (loading != null && loading) showLoadingIndicator();
                    else showMovieList();
                }
        );
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
        recyclerView.setAdapter(null);
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
        errorTextView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showMovieList() {
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

}


