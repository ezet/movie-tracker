package no.ezet.fasttrack.popularmovies;

public interface AsyncTaskCompleteListener<T> {

    void onPostExecute(T result);

    void onPreExecute();

}
