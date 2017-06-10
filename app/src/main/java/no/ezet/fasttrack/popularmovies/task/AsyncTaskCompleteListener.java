package no.ezet.fasttrack.popularmovies.task;

public interface AsyncTaskCompleteListener<T> {

    void onPostExecute(T result);

    void onPreExecute();

}
