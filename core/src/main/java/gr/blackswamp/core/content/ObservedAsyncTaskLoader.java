package gr.blackswamp.core.content;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import gr.blackswamp.core.functions.UniAction;


public abstract class ObservedAsyncTaskLoader<D> extends AsyncTaskLoader<D> {
    final List<UniAction<Object>> _observers = new ArrayList<>();

    public ObservedAsyncTaskLoader(Context context) {
        super(context);
    }

    public synchronized void add_observer(UniAction<Object> observer) {
        _observers.add(observer);
    }

    protected synchronized void publish(Object progress) {
        for (UniAction<Object> observer : _observers) observer.call(progress);
    }

    public synchronized void remove_observer(UniAction<Object> observer) {
        _observers.remove(observer);
    }


}
