package no.ezet.fasttrack.popularmovies.repository;

public interface RepositoryListener<T> {

    void onPostExecute(T result);

}
