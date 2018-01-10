package gr.blackswamp.core.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import gr.blackswamp.core.os.Bundles;

public abstract class BaseDialog extends DialogFragment {
    protected static final String NAMESPACE = "gr.blackswamp.core.dialogs";
    protected static final String MESSAGE = NAMESPACE + ".MESSAGE";
    protected static final String TITLE = NAMESPACE + ".TITLE";
    protected static final String ID = NAMESPACE + ".ID";
    static final String TAG = NAMESPACE + ".DIALOG";
    private static String _Title = "";

    public static void SetDefaultTitle(String title) {
        _Title = title;
    }

    public static void close(FragmentManager manager, int id) {
        Fragment frag = manager.findFragmentByTag(TAG + "_" + id);
        if (frag != null && frag instanceof DialogFragment) {
            ((DialogFragment) frag).dismiss();
        }
    }

    protected final String get_message() {
        return this.getArguments().getString(MESSAGE, "");
    }

    protected final String get_title() {
        return this.getArguments().getString(TITLE, _Title);
    }

    protected final int get_id() {
        return this.getArguments().getInt(ID);
    }

    public final void show(FragmentManager manager) {
        String tag = TAG + "_" + String.valueOf(get_id());
        show(manager, tag);
    }

    protected void finished(int type, String reply) {
        if (!(getActivity() instanceof DialogResult))
            return;
        ((DialogResult) getActivity()).dialog_finished(get_id(), type, reply);
    }

    static abstract class Builder {
        private final FragmentManager _fm;
        private final int _id;
        String _message;
        String _title;

        Builder(FragmentManager fm, int id) {
            _fm = fm;
            _id = id;
        }

        Bundle get_arguments() {
            return Bundles.Create()
                    .put_int(ID, _id)
                    .put_string(MESSAGE, _message)
                    .put_string(TITLE, _title).get();
        }
    }
}
