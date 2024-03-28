package de.glawleschkoff.scannerapp.util;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class AndLiveData extends MediatorLiveData {

    private int count = 0;
    private LifecycleOwner lifecycleOwner;
    private int numberLiveData = 0;

    private AndLiveData(LifecycleOwner lifecycleOwner){
        this.lifecycleOwner = lifecycleOwner;
    }

    public static AndLiveData use(LifecycleOwner lifecycleOwner){
        return new AndLiveData(lifecycleOwner);
    }

    public AndLiveData add(LiveData liveData){
        numberLiveData++;
        addSource(liveData, new Observer() {
            private boolean b = true;
            @Override
            public void onChanged(Object o) {
                if(lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.RESUMED && b){
                    count = (count+1) % numberLiveData;
                    b = false;
                    if(count == 0){
                        setValue(new Object());
                    }
                }
            }
        });
        return this;
    }

}
