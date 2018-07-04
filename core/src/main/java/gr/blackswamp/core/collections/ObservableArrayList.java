package gr.blackswamp.core.collections;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ObservableArrayList<T> extends ArrayList<T> implements ObservableList<T> {
    private final List<OnListChangedListener> listeners = new ArrayList<>();

    @Override
    public void add_on_list_changed_listener(OnListChangedListener listener) {
        if (listener != null && !listeners.contains(listener))
            listeners.add(listener);
    }

    @Override
    public void clear_on_list_changed_listeners() {
        listeners.clear();
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        clear_on_list_changed_listeners();
    }

    @Override
    public boolean add(T object) {
        boolean reply = super.add(object);
        notify(ObservedChange.Added, size() - 1, 1);
        return reply;
    }

    @Override
    public void add(int index, T object) {
        super.add(index, object);
        notify(ObservedChange.Added, index, 1);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> collection) {
        int starting_size = size();
        boolean added = super.addAll(collection);
        if (added)
            notify(ObservedChange.Added, starting_size, size() - starting_size);

        return added;
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends T> collection) {
        boolean added = super.addAll(index, collection);
        if (added)
            notify(ObservedChange.Added, index, collection.size());
        return added;
    }

    @Override
    public void clear() {
        int starting_size = size();
        super.clear();
        if (starting_size != 0)
            notify(ObservedChange.Removed, 0, starting_size);
    }

    @Override
    public T remove(int index) {
        T val = super.remove(index);
        notify(ObservedChange.Removed, index, 1);
        return val;
    }

    @Override
    public boolean remove(Object object) {
        int idx = indexOf(object);
        if (idx == -1)
            return false;
        boolean removed = super.remove(object);
        if (removed)
            notify(ObservedChange.Removed, idx, 1);
        return removed;
    }

    @Override
    public T set(int index, T object) {
        T val = super.set(index, object);
        notify(ObservedChange.Changed, index, 1);
        return val;
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        super.removeRange(fromIndex, toIndex);
        notify(ObservedChange.Removed, fromIndex, toIndex - fromIndex);
    }

    private void notify(ObservedChange change, int start, int count) {
        for (OnListChangedListener listener : listeners) {
            listener.has_changed(change, start, count);
        }
    }
}
