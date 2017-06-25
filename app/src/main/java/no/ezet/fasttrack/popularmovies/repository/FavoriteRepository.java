package no.ezet.fasttrack.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import no.ezet.fasttrack.popularmovies.db.FavoriteDao;
import no.ezet.fasttrack.popularmovies.model.Favorite;
import no.ezet.fasttrack.popularmovies.model.Movie;
import no.ezet.fasttrack.popularmovies.network.Resource;


public class FavoriteRepository {

    private FavoriteDao favoriteDao;

    @Inject
    public FavoriteRepository(FavoriteDao favoriteDao) {
        this.favoriteDao = favoriteDao;
    }

    @NonNull
    public LiveData<Resource<List<Favorite>>> getAll() {
        return Transformations.map(favoriteDao.getFavorites(), Resource::success);
    }

    public boolean add(Movie movie) {
        AsyncTask.execute(() -> favoriteDao.insert(new Favorite(movie.getId(), movie.getPosterPath())));
        return true;
    }

    @NonNull
    public LiveData<Resource<Favorite>> getById(int id) {
        return Transformations.map(favoriteDao.getById(id), Resource::success);
    }

    public void remove(Movie value) {
        AsyncTask.execute(() -> favoriteDao.delete(new Favorite(value.getId(), null)));
    }
}
