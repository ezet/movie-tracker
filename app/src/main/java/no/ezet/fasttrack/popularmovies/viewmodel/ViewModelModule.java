package no.ezet.fasttrack.popularmovies.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@SuppressWarnings("unused")
@Module
public abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(MovieListViewModel.class)
    abstract ViewModel bindMovieListViewModel(MovieListViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel.class)
    abstract ViewModel bindMovieDetailsViewModel(MovieDetailsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteListViewModel.class)
    abstract ViewModel bindFavoriteListViewModel(FavoriteListViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SearchMoviesViewModel.class)
    abstract ViewModel bindSearchMoviesViewModel(SearchMoviesViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(WatchlistViewModel.class)
    abstract ViewModel bindWatchListViewModel(WatchlistViewModel viewModel);


}
