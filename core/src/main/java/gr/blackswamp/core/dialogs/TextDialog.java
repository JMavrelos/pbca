package gr.blackswamp.core.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import gr.blackswamp.core.R;
import gr.blackswamp.core.Strings;
import gr.blackswamp.core.os.Bundles;

@SuppressWarnings("unused")
public class TextDialog extends BaseDialog implements DialogInterface.OnClickListener {
    protected static final String POSITIVE = NAMESPACE + ".POSITIVE";
    protected static final String NEGATIVE = NAMESPACE + ".NEGATIVE";
    protected static final String NEUTRAL = NAMESPACE + ".NEUTRAL";
    static final String TAG = NAMESPACE + ".DIALOG";

    public TextDialog() {
    }

    private static TextDialog NewInstance(String positive, String negative, String neutral) {
        TextDialog dialog = new TextDialog();
        Bundles bundles = Bundles.From(dialog)
                .put_string(POSITIVE, positive);
        if (!Strings.IsNullOrWhitespace(negative))
            bundles.put_string(NEGATIVE, negative);
        if (!Strings.IsNullOrWhitespace(neutral))
            bundles.put_string(NEUTRAL, neutral);
        return dialog;
    }


    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_text, null, false);
        builder.setView(view)
                .setCancelable(false);
        if (!Strings.IsNullOrWhitespace(get_title()))
            builder.setTitle(get_title());
        switch (get_buttons()) {
            case 1:
                builder.setPositiveButton(get_positive(), this);
                break;
            case 2:
                builder.setPositiveButton(get_positive(), this);
                builder.setNegativeButton(get_negative(), this);
                break;
            case 3:
                builder.setPositiveButton(get_positive(), this);
                builder.setNegativeButton(get_negative(), this);
                builder.setNeutralButton(get_neutral(), this);
                break;
        }
        ((TextView) view.findViewById((R.id.dialog_text_text))).setText(get_message());
        return builder.create();
    }

    private int get_buttons() {
        if (getArguments().containsKey(NEUTRAL))
            return 3;
        else if (getArguments().containsKey(NEGATIVE))
            return 2;
        return 1;
    }

    private String get_positive() {
        if (get_buttons() == 3)
            return getArguments().getString(POSITIVE, getString(R.string.dialog_yes));
        return getArguments().getString(POSITIVE, getString(R.string.dialog_ok));
    }

    private String get_negative() {
        if (get_buttons() == 3)
            return getArguments().getString(NEGATIVE, getString(R.string.dialog_no));
        return getArguments().getString(NEGATIVE, getString(R.string.dialog_cancel));
    }

    private String get_neutral() {
        return getArguments().getString(NEUTRAL, getString(R.string.dialog_cancel));
    }

    @Override
    public final void onClick(DialogInterface dialog, int which) {
        finished(which, null);
        dismiss();
    }

    @Override
    public final void onCancel(DialogInterface dialog) {
        if (get_buttons() == 3)
            finished(DialogInterface.BUTTON_NEUTRAL, null);
        else
            finished(DialogInterface.BUTTON_NEGATIVE, null);
    }

    @Override
    public final void onDismiss(DialogInterface dialog) {

    }

    public final static class Builder extends BaseDialog.Builder {
        String _positive;
        String _negative;
        String _neutral;

        protected Builder(FragmentManager fm, int id) {
            super(fm, id);
        }


        public TextDialog.Builder set_message(String message) {
            _message = message;
            return this;
        }

        public TextDialog.Builder set_title(String title) {
            _title = title;
            return this;
        }

        public TextDialog.Builder set_positive(String text) {
            _positive = text;
            return this;
        }

        public TextDialog.Builder set_negative(String text) {
            _negative = text;
            return this;
        }

        public TextDialog.Builder set_neutral(String text) {
            _neutral = text;
            return this;
        }

        public TextDialog build() {
            TextDialog dialog = TextDialog.NewInstance(_positive, _negative, _neutral);
            dialog.getArguments().putAll(super.get_arguments());
            return dialog;
        }
    }
}
