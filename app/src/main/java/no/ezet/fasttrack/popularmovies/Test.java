package no.ezet.fasttrack.popularmovies;

import android.arch.lifecycle.MutableLiveData;

import org.parceler.Parcel;
import org.parceler.Transient;

@Parcel()
public class Test {

    @Transient
    MutableLiveData<Integer> test = new MutableLiveData<>();

//    @ParcelProperty("test")
//    public MutableLiveData<Integer> getTest() {
//        return test;
//    }

//    @ParcelProperty("test")
//    public void setTest(int value) {
//        test.setValue(value);
//    }

}
