package no.ezet.fasttrack.popularmovies.util;

import android.arch.lifecycle.LiveData;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

import no.ezet.fasttrack.popularmovies.R;
import no.ezet.fasttrack.popularmovies.network.Resource;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter implements CallAdapter<R, LiveData<Resource<R>>> {

    private final Type responseType;

    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<Resource<R>> adapt(Call<R> call) {
        return new LiveData<Resource<R>>() {
            AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    postValue(Resource.loading(null));
                    call.enqueue(new Callback<R>() {
                        @Override
                        public void onResponse(Call<R> call, Response<R> response) {
                            postValue(Resource.success(response.body()));
                        }

                        @Override
                        public void onFailure(Call<R> call, Throwable throwable) {
                            postValue(Resource.error(throwable.getLocalizedMessage(), null));
                        }
                    });
                }
            }
        };
    }
}
