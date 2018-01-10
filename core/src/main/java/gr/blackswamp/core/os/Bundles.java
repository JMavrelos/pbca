package gr.blackswamp.core.os;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to allow you to create bundles in a single line
 */
@SuppressWarnings("WeakerAccess,unused")
public class Bundles {
    private Bundle _bundle;

    private Bundles() {
        _bundle = new Bundle();
    }

    @NonNull
    public static Bundles Create() {
        return new Bundles();
    }

    public static Bundles From(Fragment dialog) {
        if (dialog.getArguments() == null)
            dialog.setArguments(new Bundle());
        Bundles reply = Bundles.Create();
        reply._bundle = dialog.getArguments();
        return reply;
    }

    public Bundles put_boolean(@Nullable String key, boolean value) {
        _bundle.putBoolean(key, value);
        return this;
    }

    public Bundles put_byte(@Nullable String key, byte value) {
        _bundle.putByte(key, value);
        return this;
    }

    public Bundles put_char(@Nullable String key, char value) {
        _bundle.putChar(key, value);
        return this;
    }

    public Bundles put_short(@Nullable String key, short value) {
        _bundle.putShort(key, value);
        return this;
    }

    public Bundles put_int(@Nullable String key, int value) {
        _bundle.putInt(key, value);
        return this;
    }

    public Bundles put_long(@Nullable String key, long value) {
        _bundle.putLong(key, value);
        return this;
    }

    public Bundles put_float(@Nullable String key, float value) {
        _bundle.putFloat(key, value);
        return this;
    }

    public Bundles put_double(@Nullable String key, double value) {
        _bundle.putDouble(key, value);
        return this;
    }

    public Bundles put_string(@Nullable String key, @Nullable String value) {
        _bundle.putString(key, value);
        return this;
    }

    public Bundles put_char_sequence(@Nullable String key, @Nullable CharSequence value) {
        _bundle.putCharSequence(key, value);
        return this;
    }

    public Bundles put_integer_array_list(@Nullable String key, @Nullable ArrayList<Integer> value) {
        _bundle.putIntegerArrayList(key, value);
        return this;
    }

    public Bundles put_string_array_list(@Nullable String key, @Nullable ArrayList<String> value) {
        _bundle.putStringArrayList(key, value);
        return this;
    }

    public Bundles put_char_sequence_array_list(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
        _bundle.putCharSequenceArrayList(key, value);
        return this;
    }

    public Bundles put_serializable(@Nullable String key, @Nullable Serializable value) {
        _bundle.putSerializable(key, value);
        return this;
    }

    public Bundles put_boolean_array(@Nullable String key, @Nullable boolean[] value) {
        _bundle.putBooleanArray(key, value);
        return this;
    }

    public Bundles put_byte_array(@Nullable String key, @Nullable byte[] value) {
        _bundle.putByteArray(key, value);
        return this;
    }

    public Bundles put_short_array(@Nullable String key, @Nullable short[] value) {
        _bundle.putShortArray(key, value);
        return this;
    }

    public Bundles put_char_array(@Nullable String key, @Nullable char[] value) {
        _bundle.putCharArray(key, value);
        return this;
    }

    public Bundles put_int_array(@Nullable String key, @Nullable int[] value) {
        _bundle.putIntArray(key, value);
        return this;
    }

    public Bundles put_long_array(@Nullable String key, @Nullable long[] value) {
        _bundle.putLongArray(key, value);
        return this;
    }

    public Bundles put_float_array(@Nullable String key, @Nullable float[] value) {
        _bundle.putFloatArray(key, value);
        return this;
    }

    public Bundles put_double_array(@Nullable String key, @Nullable double[] value) {
        _bundle.putDoubleArray(key, value);
        return this;
    }

    public Bundles put_string_array(@Nullable String key, @Nullable String[] value) {
        _bundle.putStringArray(key, value);
        return this;
    }

    public Bundles put_char_sequence_array(@Nullable String key, @Nullable CharSequence[] value) {
        _bundle.putCharSequenceArray(key, value);
        return this;
    }
    public Bundles put_parcelable(@Nullable String key, @Nullable Parcelable value) {
        _bundle.putParcelable(key,value);
        return this;
    }

    public Bundles put_parcelable_array(@Nullable String key, @Nullable Parcelable[] value) {
        _bundle.putParcelableArray(key,value);
        return this;
    }

    public Bundles put_parcelable_array_list(@Nullable String key,@Nullable ArrayList<? extends Parcelable> value) {
        _bundle.putParcelableArrayList(key,value);
        return this;
    }

    public Bundle get() {
        return _bundle;
    }


}
