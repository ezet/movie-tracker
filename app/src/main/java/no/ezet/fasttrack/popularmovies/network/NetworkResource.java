package no.ezet.fasttrack.popularmovies.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Helper class to manage network resources that can be cached in a local database.
 *
 * @param <ResultType>  Type for the DB data
 * @param <RequestType> Type for the API response
 */
public abstract class NetworkResource<ResultType, RequestType> {

    private MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkResource() {
        result.setValue(Resource.loading(null));

    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        LiveData<NetworkResponse<RequestType>> liveResponse = createCall();
        // we re-attach dbSource as a new source,
        // it will dispatch its latest value quickly
        result.addSource(dbSource,
                newData -> result.setValue(Resource.loading(newData)));
        result.addSource(liveResponse, response -> {
            result.removeSource(liveResponse);
            result.removeSource(dbSource);
            //noinspection ConstantConditions
            if (response.isSuccessful()) {
                saveResultAndReInit(response);
            } else {
                onFetchFailed();
                result.addSource(dbSource,
                        newData -> result.setValue(
                                Resource.error(response.errorMessage, newData)));
            }
        });
    }

    @MainThread
    private void saveResultAndReInit(NetworkResponse<RequestType> response) {
        new SaveResultTask(response).execute();
    }

    /**
     * Called to store the result of the API response into the database.
     *
     * @param item The request
     */
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    /**
     * Called with the data in the database to decide whether it should be fetched from the network.
     *
     * @param data the data from the database
     * @return true if it should fetch new data from the network
     */
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    /**
     * Called to get the cached data from the database.
     *
     * @return the data from the database
     */
    @MainThread
    @NonNull
    protected abstract LiveData<ResultType> loadFromDb();

    /**
     * Called to create the API call.
     *
     * @return The request wrapped in a LiveData instance.
     */
    @NonNull
    @MainThread
    protected abstract LiveData<NetworkResponse<RequestType>> createCall();

    /**
     * Called when the fetch fails. The child class may want to reset components like rate limiters.
     */
    @MainThread
    protected void onFetchFailed() {

    }

    @NonNull
    public final LiveData<Resource<ResultType>> getAsLiveData() {
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource(dbSource, data -> {
            if (shouldFetch(data)) {
                result.removeSource(dbSource);
                fetchFromNetwork(dbSource);
            } else {
//                result.addSource(dbSource,
//                        newData -> result.setValue(Resource.success(newData)));
                result.setValue(Resource.success(data));
            }
        });
        return result;
    }

    private class SaveResultTask extends AsyncTask<Void, Void, Void> {

        private final NetworkResponse<RequestType> response;

        public SaveResultTask(NetworkResponse<RequestType> response) {
            this.response = response;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            saveCallResult(response.body);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // we specially request a new live data,
            // otherwise we will get immediately last cached value,
            // which may not be updated with latest results received from network.
            result.addSource(loadFromDb(),
                    newData -> result.setValue(Resource.success(newData)));
        }
    }
}
