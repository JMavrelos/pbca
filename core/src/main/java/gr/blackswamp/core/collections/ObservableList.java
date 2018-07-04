package gr.blackswamp.core.collections;

import java.util.List;

public interface ObservableList<T> extends List<T> {

    void add_on_list_changed_listener(OnListChangedListener listener);

    void clear_on_list_changed_listeners();

    interface OnListChangedListener {
        void has_changed(ObservedChange change, int start, int count);
    }
}
