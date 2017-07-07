//package no.ezet.tmdb.di;
//
//import android.app.Activity;
//
//import dagger.Binds;
//import dagger.Module;
//import dagger.android.ActivityKey;
//import dagger.android.AndroidInjector;
//import dagger.multibindings.IntoMap;
//import no.ezet.tmdb.view.DiscoverActivity;
//import no.ezet.tmdb.view.MovieDetailActivity;
//
////@Module
////abstract class ActivityModule {
////
////    @ContributesAndroidInjector(modules = DiscoverModule.class)
////    abstract DiscoverActivity contributeDiscoverActivity();
////
////    @ContributesAndroidInjector(modules = DiscoverModule.class)
////    abstract MovieDetailActivity contributeMovieDetailActivity();
////
////}
//
//@Module(subcomponents = MovieDetailActivitySubcomponent.class)
//abstract class MovieDetailActivityModule {
//
//    @Binds
//    @IntoMap
//    @ActivityKey(MovieDetailActivity.class)
//    abstract AndroidInjector.Factory<? extends Activity>
//    bindMovieDetailActivityInjectorFactory(MovieDetailActivitySubcomponent.Builder builder);
//}
