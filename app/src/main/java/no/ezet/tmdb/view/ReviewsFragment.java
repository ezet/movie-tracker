package no.ezet.tmdb.view;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.ezet.tmdb.R;
import no.ezet.tmdb.api.model.MovieReview;
import no.ezet.tmdb.viewmodel.MovieDetailsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends LifecycleFragment {

    private MovieDetailsViewModel viewModel;

    public ReviewsFragment() {
        // Required empty public constructor
    }

    public static ReviewsFragment create(int movieId, String posterPath) {
        Bundle args = new Bundle();
        ReviewsFragment fragment = new ReviewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(MovieDetailsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_reviews, container, false);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.review_list);
        setupReviewList(recyclerView);
        return inflate;
    }


    private void setupReviewList(RecyclerView recyclerView) {
        ReviewListAdapter reviewListAdapter = new ReviewListAdapter((movieReview, i) -> openReview(movieReview));
        recyclerView.setAdapter(reviewListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        viewModel.getReviews().observe(this, reviewListAdapter::setReviews);
    }

    private void openReview(MovieReview movieReview) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(movieReview.url)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.getReviews().removeObservers(this);
    }
}
