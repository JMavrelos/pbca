package gr.blackswamp.core.app;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class CoreFragment extends Fragment {
    private View _view;

    protected Object holder() {
        return _view.getTag();
    }

    protected final void set_holder(Object holder) {
        _view.setTag(holder);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _view = inflater.inflate(get_resource_layout_id(), container, false);
        assign_views(true);
        attach_listeners(true);
        post_create_view(getArguments(), savedInstanceState);
        return _view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        attach_listeners(false);
        assign_views(false);
    }

    /**
     * Helper method that accepts a resource id and casts it to the required view type, may throw ClassCastException
     *
     * @param id  the resource id to find
     * @param <T> the view that this will be cast to
     * @return the view cast to T
     */
    protected final <T extends View> T find(@IdRes int id) {
        //noinspection unchecked
        return _view.findViewById(id);
    }
    //region Abstract methods

    /**
     * This is just to force implementations to always have a tag
     *
     * @return the tag
     */
    protected abstract String TAG();

    /**
     * The resource id that should be inflated as the view of this fragment
     *
     * @return the resource id
     */
    protected abstract @LayoutRes
    int get_resource_layout_id();
    //endregion

    //region virtual methods

    /**
     * Called when the view has been fully created
     *
     * @param arguments the arguments passed during creation
     * @param state     the state of the fragment when created (null if it is new, not null if it is recovering)
     */
    protected void post_create_view(final Bundle arguments, final Bundle state) {

    }

    /**
     * Called when the listeners should be attached/detached
     *
     * @param attach if the listeners should be attached/detached
     */
    protected void attach_listeners(final boolean attach) {
    }

    /**
     * assign views to the viewholder
     *
     * @param assign if the views should be assigned
     */
    protected void assign_views(final boolean assign) {
    }

    /**
     * Will be called when the fragment needs to be notified that the back button was pressed
     */
    protected void back_pressed() {

    }


    //endregion

    public final <T extends Fragment> List<T> fragments(final Class<T> clazz) {
        List<T> children = new ArrayList<>();
        if (this.isAdded())
            return children;
        if (getChildFragmentManager().getFragments() == null)
            return children;
        for (Fragment child : getChildFragmentManager().getFragments()) {
            if (clazz.isInstance(child)) {
                children.add(clazz.cast(child));
            }
            if (child instanceof CoreFragment) {
                List<T> sub_children = ((CoreFragment) child).fragments(clazz);
                children.addAll(sub_children);
            }
        }
        return children;
    }
}
