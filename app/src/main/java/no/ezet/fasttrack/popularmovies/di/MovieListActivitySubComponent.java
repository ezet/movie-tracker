package no.ezet.fasttrack.popularmovies.di;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import no.ezet.fasttrack.popularmovies.view.MovieListActivity;


@Subcomponent()
public interface MovieListActivitySubComponent extends AndroidInjector<MovieListActivity> {

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MovieListActivity> {
    }

}
