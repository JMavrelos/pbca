package gr.blackswamp.core.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import gr.blackswamp.core.Util;
import gr.blackswamp.core.functions.UniAction;
import gr.blackswamp.core.os.Bundles;

public class GenericDialog extends DialogFragment implements DialogInterface.OnClickListener {
    public static final String TAG = "gr.blackswamp.core.dialogs.generic_dialog";
    private static final String POSITIVE = TAG + ".POSITIVE";
    private static final String NEGATIVE = TAG + ".NEGATIVE";
    private static final String NEUTRAL = TAG + ".NEUTRAL";
    private static final String TITLE = TAG + ".TITLE";
    private static final String TITLE_VIEW = TAG + ".TITLE_VIEW";
    private static final String TITLE_LAYOUT = TAG + ".TITLE_LAYOUT";
    private static final String RESOURCE_ID = TAG + ".RESOURCE_ID";
    private static final String CANCELABLE = TAG + ".CANCELABLE";
    private static final String DATA = TAG + ".DATA";
    private static final String ID = TAG + ".ID";
    private FragmentManager _manager;
    private UniAction<View> _init_view_callback;
    private View _dialog_view;

    //region constructor
    public GenericDialog() {
    }

    private static GenericDialog NewInstance(int id, int resource_id, String title, String positive, String neutral, String negative, boolean cancelable, int title_layout, int title_view, Bundle data) {
        GenericDialog dialog = new GenericDialog();
        Bundles.From(dialog)
                .put_string(POSITIVE, positive)
                .put_string(NEGATIVE, negative)
                .put_string(NEUTRAL, neutral)
                .put_string(TITLE, title)
                .put_int(TITLE_VIEW, title_view)
                .put_int(TITLE_LAYOUT, title_layout)
                .put_int(RESOURCE_ID, resource_id)
                .put_int(ID, id)
                .put_boolean(CANCELABLE, cancelable)
                .put_bundle(DATA, data);
        return dialog;
    }
    //endregion

    //region Methods to retrieve data from arguments
    private String get_positive() {
        return this.getArguments().getString(POSITIVE, null);
    }

    private String get_negative() {
        return this.getArguments().getString(NEGATIVE, null);
    }

    private Bundle get_data() {
        return this.getArguments().getBundle(DATA);
    }

    private String get_neutral() {
        return this.getArguments().getString(NEUTRAL, null);
    }

    private String get_title() {
        return this.getArguments().getString(TITLE, null);
    }

    private @LayoutRes
    int get_title_layout() {
        return this.getArguments().getInt(TITLE_LAYOUT, 0);
    }

    private @IdRes
    int get_title_view() {
        return this.getArguments().getInt(TITLE_VIEW, View.NO_ID);
    }

    private boolean get_cancelable() {
        return this.getArguments().getBoolean(CANCELABLE, false);
    }

    private @LayoutRes
    int get_resource_id() {
        return this.getArguments().getInt(RESOURCE_ID, View.NO_ID);
    }

    private int get_id() {
        return this.getArguments().getInt(ID, 0);
    }

    //endregion
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        _dialog_view = LayoutInflater.from(getActivity()).inflate(get_resource_id(), null, false);
        if (_init_view_callback != null)
            _init_view_callback.call(_dialog_view);
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity())
                        .setView(_dialog_view)
                        .setCancelable(get_cancelable())
                        .setOnCancelListener(this);
        View title_view = null;
        String title = get_title();
        if (get_title_view() != 0) {
            title_view = LayoutInflater.from(getActivity()).inflate(get_title_layout(), null, false);
            builder.setCustomTitle(title_view);
        }
        if (!Util.IsNullOrWhitespace(title)) {
            if (title_view != null && get_title_view() != 0) {
                View view = title_view.findViewById(get_title_view());
                if (view instanceof TextView) {
                    ((TextView) view).setText(title);
                }
            } else if (title_view == null) {
                builder.setTitle(title);
            }
        }

        String positive = get_positive();
        String negative = get_negative();
        String neutral = get_neutral();

        if (positive == null && negative == null && neutral == null) {
            builder.setPositiveButton(android.R.string.ok, this);
        } else {
            if (positive != null)
                builder.setPositiveButton(positive, this);
            if (negative != null)
                builder.setNegativeButton(negative, this);
            if (neutral != null)
                builder.setNeutralButton(neutral, this);
        }
        return builder.create();
    }

    public void show() {
        show(_manager, TAG + "." + String.valueOf(get_id()));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_NEGATIVE:
                finished(DialogResult.Negative);
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                finished(DialogResult.Neutral);
                break;
            case DialogInterface.BUTTON_POSITIVE:
                finished(DialogResult.Positive);
                break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        finished(DialogResult.Canceled);
    }

    void finished(DialogResult result) {
        try {
            if (!(getActivity() instanceof OnDialogFinished))
                return;
            OnDialogFinished finished = (OnDialogFinished) getActivity();
            if (finished == null)
                return;
            finished.DialogFinished(result, get_id(), _dialog_view, get_data());
        } finally {
            _dialog_view = null;
            this.dismiss();
        }
    }

    public GenericDialog set_init_view_callback(UniAction<View> init_view_callback) {
        _init_view_callback = init_view_callback;
        return this;
    }

    public enum DialogResult {
        Canceled,
        Negative,
        Neutral,
        Positive
    }

    public interface OnDialogFinished {
        void DialogFinished(DialogResult result, int id, View dialog, Bundle data);
    }

    public final static class Builder {
        private final FragmentManager _fm;
        private final int _id;
        @LayoutRes
        private final int _res;
        String _title;
        String _positive;
        String _negative;
        String _neutral;
        private boolean _cancelable = true;
        @LayoutRes
        private int _title_layout = 0;
        @IdRes
        private int _title_view = 0;
        private Bundle _data;

        public Builder(FragmentManager fm, int id, @LayoutRes int res) {
            _fm = fm;
            _id = id;
            _res = res;
        }

        public Builder set_title(String title) {
            _title = title;
            return this;
        }

        public Builder set_positive(String text) {
            _positive = text;
            return this;
        }

        public Builder set_negative(String text) {
            _negative = text;
            return this;
        }

        public Builder set_neutral(String text) {
            _neutral = text;
            return this;
        }

        public Builder set_cancelable(boolean cancelable) {
            _cancelable = cancelable;
            return this;
        }

        public Builder set_title_resource(@LayoutRes int dialog_title_layout, @IdRes int title_view_id) {
            _title_layout = dialog_title_layout;
            _title_view = title_view_id;
            return this;
        }

        public Builder set_data(Bundle data) {
            this._data = data;
            return this;
        }

        public GenericDialog build() {
            GenericDialog dialog =
                    GenericDialog.NewInstance(_id, _res, _title, _positive, _neutral, _negative, _cancelable, _title_layout, _title_view, _data);
            dialog._manager = _fm;
            return dialog;
        }


    }


}
