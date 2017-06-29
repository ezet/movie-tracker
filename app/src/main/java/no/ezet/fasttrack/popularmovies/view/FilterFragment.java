package no.ezet.fasttrack.popularmovies.view;


import android.arch.lifecycle.LifecycleFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.model.ApiList;
import no.ezet.fasttrack.popularmovies.model.Genre;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.repository.MovieRepository;
import no.ezet.fasttrack.popularmovies.service.IMovieService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class FilterFragment extends LifecycleFragment {
    @Inject
    MovieRepository movieRepository;

    @Inject
    IMovieService movieService;
    private Spinner releaseYearSpinner;
    private Spinner genreSpinner;
    private Spinner orderBySpinner;
    private Spinner orderSpinner;
    private List<Genre> genres;

    public static android.support.v4.app.Fragment create() {
        return new FilterFragment();
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_filter, container, false);
        setupOrderSpinner(root);
        setupOrderBySpinner(root);
        setupYearSpinner(root);
        setupGenreSpinner(root);
        setupButtons(root);
        setupRegions(root);
        return root;

    }

    private void setupRegions(View root) {
        // TODO: 29.06.2017 add support for regions
    }

    private void setupButtons(View root) {
        root.findViewById(R.id.button_filter_search).setOnClickListener(this::doFilter);
        root.findViewById(R.id.button_filter_clear).setOnClickListener(this::doClear);
    }

    private void doClear(View view) {

    }

    private void doFilter(View view) {
        String releaseYearString = releaseYearSpinner.getSelectedItem().toString();
        Integer releaseYear = releaseYearString.equals("") ? null : Integer.parseInt(releaseYearString);

        Integer genre = genreSpinner.getSelectedItemPosition() == 0 ? null : genres.get(genreSpinner.getSelectedItemPosition() + 1).id;

        StringBuilder sortBy = new StringBuilder();
        sortBy.append(orderBySpinner.getSelectedItem().toString().replace(" ", "_").toLowerCase());
        sortBy.append(".");
        sortBy.append(orderSpinner.getSelectedItem().toString().equals("Ascending") ? "asc" : "desc");

        movieService.filter(genre, sortBy.toString(), releaseYear).enqueue(new Callback<ApiList<Movie>>() {
            @Override
            public void onResponse(Call<ApiList<Movie>> call, Response<ApiList<Movie>> response) {
                Timber.d("onResponse: ");
            }

            @Override
            public void onFailure(Call<ApiList<Movie>> call, Throwable t) {
                Timber.d("onFailure: ");
            }
        });


    }

    private void setupGenreSpinner(View root) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("");
        movieRepository.getGenres().observe(this, listResource -> {
            if (listResource != null && listResource.data != null) {
                this.genres = listResource.data;
                for (Genre genre : listResource.data) {
                    adapter.add(genre.name);
                }
                adapter.notifyDataSetChanged();
            }
        });
        genreSpinner = (Spinner) root.findViewById(R.id.spinner_genre);
        genreSpinner.setAdapter(adapter);
    }

    private void setupYearSpinner(View root) {
        releaseYearSpinner = (Spinner) root.findViewById(R.id.spinner_year);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        adapter.add("");
        for (int i = 1970; i <= year; ++i) {
            adapter.add(String.valueOf(i));
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        releaseYearSpinner.setAdapter(adapter);
    }

    private void setupOrderBySpinner(View root) {
        ArrayAdapter<CharSequence> adapter;
        orderBySpinner = (Spinner) root.findViewById(R.id.spinner_order_by);
        adapter = ArrayAdapter.createFromResource(getContext(), R.array.filter_order_by, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderBySpinner.setAdapter(adapter);
    }

    private void setupOrderSpinner(View root) {
        orderSpinner = (Spinner) root.findViewById(R.id.spinner_order);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.filter_order, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderSpinner.setAdapter(adapter);
    }
}
