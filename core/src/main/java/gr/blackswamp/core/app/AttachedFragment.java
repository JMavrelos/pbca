package gr.blackswamp.core.app;

import android.app.Activity;
import android.content.Context;

public abstract class AttachedFragment<T extends CoreInteraction> extends CoreFragment {
    private T _parent;
    protected T parent() {
        return _parent;
    }

    @Override
    @Deprecated
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        do_attach(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        do_attach(context);
    }

    private void do_attach(final Context context) {
        //this SHOULD throw an exception if the parent is not set correctly
        //noinspection unchecked
        _parent = (T) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _parent = null;
    }


}
