package no.ezet.tmdb.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.List;

import no.ezet.tmdb.viewmodel.MovieDetailsViewModel;
import no.ezet.tmdb.viewmodel.MovieListItem;

public class RecommendedListFragment extends MovieListBaseFragment {

    private MovieDetailsViewModel viewModel;

    public static RecommendedListFragment create(int movieId, String posterPath) {
        return new RecommendedListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(MovieDetailsViewModel.class);
    }

    @Override
    protected LiveData<List<MovieListItem>> getListItems() {
        return viewModel.getRecommended();
    }

    @Override
    protected LiveData<Boolean> getIsLoading() {
        return viewModel.getIsLoading();
    }
}


