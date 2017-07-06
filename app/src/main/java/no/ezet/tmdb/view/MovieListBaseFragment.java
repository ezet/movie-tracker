package no.ezet.tmdb.view;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
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

import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import no.ezet.tmdb.R;
import no.ezet.tmdb.service.ImageService;
import no.ezet.tmdb.viewmodel.MovieListItem;

public abstract class MovieListBaseFragment extends LifecycleFragment {

    @Inject
    ImageService imageService;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    // TODO: 22.06.2017 Manage with DI
//    @Inject
    MovieListRecyclerViewAdapter.FragmentListener listener;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorTextView;

    private static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
        if (context instanceof MovieListRecyclerViewAdapter.FragmentListener) {
            listener = (MovieListRecyclerViewAdapter.FragmentListener) context;
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getIsLoading().observe(this, loading -> {
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
        getActivity().supportStartPostponedEnterTransition();
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getListItems().removeObservers(this);
        getIsLoading().removeObservers(this);
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
        getListItems().observe(this, adapter::setMovies);
        recyclerView.setAdapter(adapter);
    }

    protected abstract LiveData<List<MovieListItem>> getListItems();

    protected abstract LiveData<Boolean> getIsLoading();

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

}


