package no.ezet.fasttrack.popularmovies.task;

public interface RepositoryListener<T> {

    void onPostExecute(T result);

    void onPreExecute();

}
