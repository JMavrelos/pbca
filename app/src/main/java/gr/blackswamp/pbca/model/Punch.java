package gr.blackswamp.pbca.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import gr.blackswamp.pbca.R;


public class Punch implements Parcelable {
    public static final Creator<Punch> CREATOR = new Creator<Punch>() {
        @Override
        public Punch createFromParcel(Parcel in) {
            return new Punch(in);
        }

        @Override
        public Punch[] newArray(int size) {
            return new Punch[size];
        }
    };
    private int _number;
    private String _name;
    private String _file;

    private Punch(int id, String name, String file) {
        _number = id;
        _name = name;
        _file = file;
    }

    private Punch(Parcel in) {
        _number = in.readInt();
        _name = in.readString();
        _file = in.readString();
    }

    public static List<Punch> Build(final Context ctx) {
        return new ArrayList<Punch>() {{
            add(new Punch(1, ctx.getString(R.string.punch_left_direct), "one.wav"));
            add(new Punch(2, ctx.getString(R.string.punch_right_direct), "two.wav"));
            add(new Punch(3, ctx.getString(R.string.punch_left_hook), "three.wav"));
            add(new Punch(4, ctx.getString(R.string.punch_right_hook), "four.wav"));
            add(new Punch(5, ctx.getString(R.string.punch_left_uppercut), "five.wav"));
            add(new Punch(6, ctx.getString(R.string.punch_right_uppercut), "six.wav"));
        }};
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_number);
        dest.writeString(_name);
        dest.writeString(_file);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int get_number() {
        return _number;
    }

    public void set_number(int _number) {
        this._number = _number;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_file() {
        return _file;
    }

    public void set_file(String _file) {
        this._file = _file;
    }

    public boolean is_left() {
        return _number % 2 == 1;
    }

    @Override
    public int hashCode() {
        return _number;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj!= null && obj.getClass().equals(Punch.class) && ((Punch)obj)._number == _number);
    }
}
