/*
package no.ezet.fasttrack.popularmovies.parcelconverter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Parcel;

import org.parceler.ParcelConverter;
import org.parceler.Parcels;

public class MutableLiveDataParcelConverter<T> implements ParcelConverter<MutableLiveData<T>> {
    @Override
    public void toParcel(MutableLiveData<T> input, Parcel parcel) {
        parcel.writeParcelable(Parcels.wrap(input.getValue()), 0);
    }

    @Override
    public MutableLiveData<T> fromParcel(Parcel parcel) {
        MutableLiveData<T> liveData = new MutableLiveData<>();
        T unwrap = Parcels.unwrap(parcel.readParcelable(LiveData.class.getClassLoader()));
        liveData.setValue(unwrap);
        return liveData;
    }
}
*/
